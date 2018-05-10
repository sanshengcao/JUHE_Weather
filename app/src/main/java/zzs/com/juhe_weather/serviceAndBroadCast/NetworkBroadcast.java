package zzs.com.juhe_weather.serviceAndBroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;

import zzs.com.juhe_weather.MLog;
import zzs.com.juhe_weather.R;
import zzs.com.juhe_weather.WeatherApplication;

/**
 * Created by ENIAC on 2018/5/2.
 */

public class NetworkBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                //WIFI已连接,移动数据已连接
                NetworkUtils.pingNetWork(context);
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                //WIFI已连接,移动数据已断开
                NetworkUtils.pingNetWork(context);
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                WeatherApplication.isNetworkAvailable = true;
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
                WeatherApplication.isNetworkAvailable=false;
                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
            }
//API大于23时使用下面的方式进行网络监听
        }else {

            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if(connMgr==null) {
                WeatherApplication.isNetworkAvailable=false;
            }else {
                //获取当前可用网络
                NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
                if(networkInfo==null){
                    WeatherApplication.isNetworkAvailable=false;
                }else {
                    switch (networkInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:
                            if (networkInfo.isAvailable()) {
                                WeatherApplication.isNetworkAvailable = true;
                            }
                            break;
                        case ConnectivityManager.TYPE_WIFI:
                        case ConnectivityManager.TYPE_ETHERNET:
                            if (networkInfo.isAvailable()) {
                                NetworkUtils.pingNetWork(context);
                            } else {
                                WeatherApplication.isNetworkAvailable = false;
                            }
                            break;
                        default:
                            WeatherApplication.isNetworkAvailable = false;
                            break;
                    }
                }
                /*
                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                }
                */
            }
        }
    }

}
