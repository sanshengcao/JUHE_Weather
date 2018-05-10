package zzs.com.juhe_weather;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import java.util.ArrayList;

import zzs.com.juhe_weather.dataClass.City;
import zzs.com.juhe_weather.dragSortListView.DragSortListView;
import zzs.com.juhe_weather.tools.WeatherSQLUtils;

public class ManageCity extends AppCompatActivity {

    private ArrayList<City> mCityList;
    private ArrayList<City> removeList;

    private DragSortListViewAdapter adapter;
    private Boolean isChanged = false;

    //滑动监听
    private DragSortListView.DropListener dropListener = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {

            if(from!=to) {
                MLog.e("drop from="+from+"  to = "+to);
                City city = mCityList.get(from);
                adapter.remove(from);
                adapter.insert(to,city);
                isChanged = true;
                WeatherApplication.isCityListChanged=true;
            }
        }
    };

    // 删除监听器，点击左边差号就触发。删除item操作。
    private DragSortListView.RemoveListener removeListener = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            MLog.e("remove which="+which);
            City city = mCityList.get(which);
            removeList.add(city);
            adapter.remove(which);
            isChanged = true;
            WeatherApplication.isCityListChanged=true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_sort_delete);
        mCityList =WeatherApplication.cityList;
        removeList = new ArrayList<>();
        DragSortListView gslv = findViewById(R.id.gslv);
        TextView cancel =findViewById(R.id.cancel);
        TextView ok = findViewById(R.id.ok);
        ManagerOnclickListener listener= new ManagerOnclickListener();
        cancel.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        gslv.setDropListener(dropListener);
        gslv.setRemoveListener(removeListener);
        adapter = new DragSortListViewAdapter(ManageCity.this);
        gslv.setAdapter(adapter);
        gslv.setDragEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(isChanged){
                    showPopupWindow();
                }else {
                    ManageCity.this.finish();
                }

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    private void showPopupWindow(){
        PopupWindow popupWindow = new PopupWindow(ManageCity.this);
        View contentView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.citylist_change_popup,null);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancel=contentView.findViewById(R.id.cancel_pop);
        Button ok = contentView.findViewById(R.id.ok_pop);
        PopupOnclickListener popupListener = new PopupOnclickListener(popupWindow);
        cancel.setOnClickListener(popupListener);
        ok.setOnClickListener(popupListener);

        popupWindow.setAnimationStyle(R.style.PopupAnim);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(ManageCity.this.findViewById(R.id.ok), Gravity.BOTTOM,0,0);

    }

    private class ManagerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancel:
                    if(isChanged){
                        showPopupWindow();
                    }else {
                        ManageCity.this.finish();
                    }
                    break;
                case R.id.ok:
                    if(isChanged){
                        //删除数据库的天气，使城市列表与改动过后的相同
                        MLog.e("ManagerCity Ok isChanged");
                        WeatherApplication.cityList=mCityList;
                        for(City removeCity:removeList){
                            WeatherSQLUtils weatherSQLUtils = new WeatherSQLUtils();
                            weatherSQLUtils.deleteWeather(getApplicationContext(),removeCity);
                        }
                        ManageCity.this.finish();
                    }else {
                        ManageCity.this.finish();
                    }
                    break;
            }
        }
    }

    private class DragSortListViewAdapter extends BaseAdapter{
        private Context context;
        public DragSortListViewAdapter(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return mCityList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void remove(int position){//删除
            mCityList.remove(position);
            notifyDataSetChanged();
        }
        public void insert(int position,City city){//增加
            mCityList.add(position,city);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            City city = mCityList.get(position);
            ViewHolder holder;
            if(convertView==null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ManageCity.this)
                        .inflate(R.layout.city_sort_remove_tiem,null);
                holder.district=convertView.findViewById(R.id.item_city_sort_district);
                holder.city=convertView.findViewById(R.id.item_city_sort_city);
                holder.click_remove=convertView.findViewById(R.id.click_remove);
                holder.drag_handle=convertView.findViewById(R.id.drag_handle);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            if(city!=null&&city.getId()==-1){
                holder.drag_handle.setImageResource(R.drawable.white_bg);
                holder.click_remove.setImageResource(R.drawable.white_bg);
            }else {
                holder.drag_handle.setImageResource(R.drawable.remove);
                holder.click_remove.setImageResource(R.drawable.remove);
            }
            holder.district.setText(city.getDistrict());
            if(city.getCity().equals(city.getProvince())){
                holder.city.setText(city.getCity());
            }else{
                holder.city.setText(String
                        .format(getString(R.string.city_province),city.getCity(),city.getProvince()));
            }
            return convertView;
        }
    }

    private class ViewHolder{
        TextView district;
        TextView city;
        ImageView drag_handle;
        ImageView click_remove;
    }

    private class PopupOnclickListener implements View.OnClickListener{
        PopupWindow mPopup;
        public PopupOnclickListener(PopupWindow popupWindow){
            mPopup = popupWindow;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancel_pop:
                    mPopup.dismiss();
                    break;
                case R.id.ok_pop:
                    mPopup.dismiss();
                    ManageCity.this.finish();
                    break;
            }
        }
    }

}
