package zzs.com.juhe_weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zzs.com.juhe_weather.adapter.MyFragmentAdapter;
import zzs.com.juhe_weather.dataClass.City;
import zzs.com.juhe_weather.serviceAndBroadCast.NetworkBroadcast;
import zzs.com.juhe_weather.tools.BDLocationUtils;
import zzs.com.juhe_weather.tools.CityUtils;
import zzs.com.juhe_weather.tools.Consts;
import zzs.com.juhe_weather.tools.URLTools;


public class MainActivity extends AppCompatActivity {

    public  ArrayList<City> mCityList = new ArrayList<City>();
    private CityFragmentAdapter mCityAdapter;
    private MyFragmentAdapter myAdapter;
    private SharedPreferences preferences;
    private SharedPreferences fsp;
    private boolean isFirst = false;

    private LinearLayout pointList;
    private LinearLayout settings;
    private int oldPage;
    private  Handler handler;
    private ViewPager mViewPager;

    private City BeiJin= new City(1,"北京","北京","北京");
    private Intent intent;
    private NetworkBroadcast networkBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //获取城市列表
        preferences=getSharedPreferences("Citys",Context.MODE_PRIVATE);
        mCityList = WeatherApplication.cityList;
        registerNetworkReceiver();
        MLog.e("onCreate mCityList size="+mCityList.size());
        //下载城市列表
        downloadCityList();
        intent=getIntent();

        //如果没有网络且本地数据不存在，则加载无网络界面
        if(!WeatherApplication.isNetworkAvailable&&mCityList.size()==0){
            ShowNoNetworkView(MainActivity.this);
        }else {
            ShowMainView();
        }

    }

    @Override
    protected void onRestart() {
        MLog.e("MainActivity  onRestart cityList size = "+mCityList.size());
        if(WeatherApplication.isCityListChanged){
            WeatherApplication.isCityListChanged=false;
            updateMainView();
        }

        super.onRestart();
    }


    //注册网络监听广播
    public void registerNetworkReceiver(){
        if(networkBroadcast==null){
            networkBroadcast = new NetworkBroadcast();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadcast,intentFilter);
    }

    public void ShowMainView(){
        int flag=intent.getIntExtra("FLAG",0);
        if (flag ==Consts.FLAG_ADD_CITY && mCityList.size() != 0) {
            //由别的界面跳转，比如添加城市界面
            int addCityID=intent.getIntExtra("addCityID",0);
            CityUtils utils = new CityUtils();
            City addCity = utils.queryCity(getApplicationContext(),addCityID);
            MLog.e("updateMain ="+addCity.toString()+" flag="+flag);
            initView(addCity);
        }else if(flag ==Consts.FLAG_CHANGED_CITY && mCityList.size() != 0){
            //没有网络则直接初始化
            initView(null);
        }else {
            initView(WeatherApplication.mLocationCity);
        }

    }

    public void ShowNoNetworkView(Activity activity){

        //        1.找到activity根部的ViewGroup，类型都为FrameLayout。
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);//固定写法，返回根视图
//        2.初始化控件显示的位置
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
//        3.设置控件显示位置
        final View view = View.inflate(activity, R.layout.no_network_layout, null);
        Button retry = view.findViewById(R.id.retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置点击 后隐藏加载页
                if(WeatherApplication.isNetworkAvailable){
                    MLog.e("ShowMainView");
                    view.setVisibility(View.GONE);
                    ShowMainView();
                }
            }
        });
        view.setVisibility(View.VISIBLE);
//        4.将控件加到根节点下
        rootContainer.addView(view);

    }


    public void initView(City city){
        pointList = findViewById(R.id.pointlist);
        settings = findViewById(R.id.setting);
        settings.setOnClickListener(new MainListener());

        //是定位城市则添加
        if(city!=null&&city.getId()==Consts.LOCATION_CITY_ID){
            if(mCityList.size()!=0&&city.getId()==mCityList.get(0).getId()){
                mCityList.remove(0);
                mCityList.add(0,city);
            }else if(mCityList.size()==0){
                mCityList.add(0,city);
            }
        }else if(city!=null){
            mCityList.add(city);
        }

        FragmentManager mFManager = getSupportFragmentManager();
        // myAdapter = new MyFragmentAdapter(mFManager,mCityList);
        mCityAdapter = new CityFragmentAdapter(mFManager,mCityList);
        mViewPager = findViewById(R.id.vp);
        mViewPager.addOnPageChangeListener(new PagerListener());

        if(mCityList!=null&&mCityList.size()!=0) {
            for (int i = 0; i < mCityList.size(); i++) {

                //初始化跟随页面滑动的点
                Point point =getNewPoint(getApplicationContext());
                pointList.addView(point);
            }
        }

        mViewPager.setAdapter(mCityAdapter);
        if(city!=null&&city.getId()!=Consts.LOCATION_CITY_ID){
            mViewPager.setCurrentItem(mCityList.size()-1);

        }else{
            Point p=(Point) pointList.getChildAt(0);
            p.setColor(R.color.black);
            mViewPager.setCurrentItem(0);
        }


    }


    private void downloadCityList() {
        fsp= getSharedPreferences("isFirst",Context.MODE_PRIVATE);
        isFirst=fsp.getBoolean("isFirst",true);
        if(isFirst){
            getCitys();
        }
    }

    public void getCitys(){
        String citysURL = URLTools.getCitysURL();
        OkHttpClient httpClient = new OkHttpClient();
        final Request.Builder builder = new Request.Builder();
        Request request =builder.get().url(citysURL).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isFirst = true;
                SharedPreferences.Editor edit = fsp.edit();
                edit.putBoolean("isFirst",isFirst);
                edit.apply();
            }

            @Override
            public void onResponse(Call call, Response rs) throws IOException {
                String resCall = rs.body().string();
                SharedPreferences.Editor edit = fsp.edit();
                if((resCall.indexOf("html")!=-1)&&(resCall.indexOf("head")!=-1)
                        &&(resCall.indexOf("body")!=-1)){
                    isFirst = true;
                    edit.putBoolean("isFirst", isFirst);
                    edit.apply();
                }else {
                    CityUtils utils = new CityUtils();
                    ArrayList<City> cityArrayList = utils.parserCitys(resCall);
                    utils.storeCity(getApplicationContext(),cityArrayList);
                    isFirst = false;
                    MLog.e("city size ="+cityArrayList.size()+" isFirst = "+isFirst);
                    edit.putBoolean("isFirst", isFirst);
                    edit.apply();
                }
            }
        });
    }

    //获取一个新的小点
    public Point getNewPoint(Context context){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(18,18);
        Point point = new Point(context);
        layoutParams.leftMargin = 20;
        point.setLayoutParams(layoutParams);
        return point;
    }

    @Override
    protected void onPause() {

        //本地存储修改后的城市列表
        //保存城市列表
        SharedPreferences.Editor edit = preferences.edit();
        if(mCityList.size()>0) {
            WeatherApplication.cityList = mCityList;
            String citystring = JSON.toJSONString(WeatherApplication.cityList);
            edit.putString("citys", citystring);
            edit.apply();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        MLog.e("MainActivity onDestroy");
        unregisterReceiver(networkBroadcast);
        super.onDestroy();
    }

    private class MainListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.setting:
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this,settings);
                    popupMenu.getMenuInflater().inflate(R.menu.setting_menu,popupMenu.getMenu());

                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.shared:

                                    break;
                                case R.id.more_setting:

                                    break;
                                case R.id.city_manager:
                                    Intent intent = new Intent("zzs.com.juhe_weather.CityManagerActivity");
                                    startActivity(intent);
                                    break;
                                default:

                                    break;
                            }
                            return true;
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }


    private class CityFragmentAdapter extends FragmentPagerAdapter{

        List<City> cities;
        List<Integer> ids = new ArrayList<>();
        Map<String,Integer> IdsMap=new HashMap<>();
        int id=1;
        private CityFragmentAdapter(FragmentManager fm,List<City> cities) {
            super(fm);
            this.cities = cities;
            for(City city:cities){
                if(!IdsMap.containsKey(String.valueOf(city.getId()))){
                    IdsMap.put(String.valueOf(city.getId()),id++);
                }
            }
            id = 1;
        }

        @Override
        public int getItemPosition(Object object) {
            CityFragment fragment= (CityFragment) object;
            City city=fragment.getCity();
            int cityId = city.getId();
            int preId = ids.indexOf(city.getId());
            int newId=-1;
            int i=0;
            int size=getCount();
            for(;i<size;i++){
                if(Integer.parseInt(getPageTitle(i).toString())==cityId){
                    newId=i;
                    break;
                }
            }
            if(newId!=-1&&newId==preId){
                MLog.e("cityId="+cityId+" POSITION_UNCHANGED");
                return POSITION_UNCHANGED;
            }
            if(newId!=-1){
                MLog.e("cityId="+cityId+" newId="+newId);
                return newId;
            }
            MLog.e("cityId="+cityId+" POSITION_NONE");
            return POSITION_NONE;
        }
                @Override
        public Fragment getItem(int position) {
            City city=cities .get(position);
            CityFragment cityFragment =new CityFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("city",city);
            cityFragment.setArguments(bundle);

            return cityFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(cities.get(position).getId());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return cities.size();
        }

        @Override
        public void notifyDataSetChanged() {

            for(City city:cities){
                if(!IdsMap.containsKey(String.valueOf(city.getId()))){
                    IdsMap.put(String.valueOf(city.getId()),id++);
                }
            }
            id=1;
            super.notifyDataSetChanged();
            ids.clear();
            for(int i=0;i<mCityList.size();i++){
                ids.add(mCityList.get(i).getId());
            }
        }

        @Override
        public long getItemId(int position) {
            return IdsMap.get(getPageTitle(position));
        }
    }

    class PagerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int newPage) {

            Point newPoint= (Point) pointList.getChildAt(newPage);
            Point oldPoint= (Point) pointList.getChildAt(oldPage);
            if(oldPage!=newPage) {
                newPoint.setColor(R.color.black);
                if (oldPoint != null) {
                    oldPoint.setColor(R.color.lightgrey);
                }
            }
            oldPage = newPage;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public static class MyHandler extends Handler{
        //对Activity的弱引用
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(){
            mActivity = null;
        }
        public MyHandler(MainActivity activity){
            mActivity=new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Thread thread = Thread.currentThread();
            MLog.e("threadId="+thread.getId()+" name"+thread.getName());
            MainActivity activity = mActivity.get();

            MLog.e("threadId="+thread.getId()+" name"+thread.getName());
            switch (msg.what){
                case 100:

                    break;
                case 101:

                    break;
                case 102:

                    break;
                case 103:
                    Toast.makeText(activity,activity.getString(R.string.service_error)
                            ,Toast.LENGTH_LONG).show();
                    break;
                case 104:
                    Toast.makeText(activity,activity.getString(R.string.network_error)
                            ,Toast.LENGTH_LONG).show();
                    break;
                case Consts.FLAG_ADD_CITY:

                    break;
                default:

                    break;

            }

        }
    }

    /**
     * 从城市管理界面返回，并且删除或者移动过城市顺序
     */
    public void updateMainView(){
        mCityList=WeatherApplication.cityList;
        int count = pointList.getChildCount()-mCityList.size();
        if(count!=0){
            for(int i=0;i<count;i++){
                pointList.removeViewAt(pointList.getChildCount()-1);
            }
        }
        mCityAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(0);
    }

}
