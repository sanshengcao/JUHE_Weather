package zzs.com.juhe_weather;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import zzs.com.juhe_weather.dataClass.City;
import zzs.com.juhe_weather.serviceAndBroadCast.NetworkUtils;
import zzs.com.juhe_weather.tools.CityUtils;
import zzs.com.juhe_weather.tools.Consts;

public class ActivityCityAdd extends AppCompatActivity {

    private ArrayList<City> cities ;
    private ShowCityAdapter adapter;
    private  TextView show_error;
    private ListView showCityList;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_add);
        mContext = this;
        cities = new ArrayList<>();
        SearchView search = findViewById(R.id.search);
        ImageView cancel = findViewById(R.id.cancel);
        show_error = findViewById(R.id.query_error);
        showCityList = findViewById(R.id.show_city_list);

        adapter = new ShowCityAdapter();
        showCityList.setAdapter(adapter);
        NetworkUtils.pingNetWork(getApplicationContext());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCityAdd.this.finish();
            }
        });

        showCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(WeatherApplication.isNetworkAvailable) {
                    City city = cities.get(position);
                    MLog.e("add city success = "+city.toString());
                    Intent intent = new Intent("com.MainActivity");
                    intent.putExtra("FLAG",Consts.FLAG_ADD_CITY);
                    intent.putExtra("addCityID",city.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    ActivityCityAdd.this.finish();
                }else{
                    Toast.makeText(ActivityCityAdd.this,getString(R.string.network_error),Toast.LENGTH_SHORT).show();
                }

            }
        });

        search.setQueryHint("搜索城市");
        search.setSubmitButtonEnabled(true);
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                new Thread(){
                    @Override
                    public void run() {
                        cities.clear();
                        CityUtils cityUtils = new CityUtils();
                        Pattern pattern = Pattern.compile("[0-9]*");
                        if(newText!=null&&newText.length()!=0) {
                            if(pattern.matcher(newText).matches()) {
                                int id = Integer.parseInt(newText);
                                City city = cityUtils.queryCity(getApplicationContext(), id);
                                if(city!=null&&city.getId()!=-1) {
                                    cities.add(city);
                                }
                            }else {
                                cities = (ArrayList<City>) cityUtils
                                        .queryCity(getApplicationContext(), newText);
                            }
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(newText!=null&&newText.length()!=0&&(cities.size()==0)){
                                    showCityList.setVisibility(View.GONE);
                                    show_error.setVisibility(View.VISIBLE);
                                }else {
                                    showCityList.setVisibility(View.VISIBLE);
                                    show_error.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }.start();
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    class ShowCityAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return cities.size();
        }

        @Override
        public Object getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ActivityCityAdd.this)
                        .inflate( R.layout.show_quer_city_layout, null);
                holder.textView = convertView.findViewById(R.id.item_show_city);
                holder.added = convertView.findViewById(R.id.added);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            City city = cities.get(position);

            if(city!=null) {
                if(WeatherApplication.cityList.contains(city)){
                    holder.added.setText(getString(R.string.added));
                }else {
                    holder.added.setText("");
                }

                if (!city.getCity().equals(city.getProvince())) {
                    String s =String.format(getString(R.string.show_city_item),city.getDistrict(),
                            city.getCity(),city.getProvince());
                    holder.textView.setText(s);
                } else {
                    holder.textView.setText(String.format(getString(R.string.show_city_item1),city.getDistrict(),
                            city.getCity()));
                }
            }
            return convertView;
        }
        private class ViewHolder{
            TextView textView;
            TextView added;
        }
    }


}
