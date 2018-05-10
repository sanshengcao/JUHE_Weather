package zzs.com.juhe_weather.serviceAndBroadCast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;

import zzs.com.juhe_weather.MLog;
import zzs.com.juhe_weather.R;
import zzs.com.juhe_weather.WeatherApplication;

public class NetworkUtils {
    public static void pingNetWork(final Context context){
       Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Process p=null;
                try {
                    String ip = "www.baidu.com";
                    p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping三次
                    // ping的状态
                    int status = p.waitFor();
                    MLog.e("net status = "+status);
                    if (status == 0) {
                        WeatherApplication.isNetworkAvailable = true;
                    }else{
                        WeatherApplication.isNetworkAvailable = false;
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context.getApplicationContext(),context.getString(R.string.network_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(p!=null){
                        p.destroy();
                    }
                }
            }
        };
       WeatherApplication.mThreadPool.execute(runnable);
    }
}
