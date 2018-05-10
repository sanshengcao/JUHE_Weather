package zzs.com.juhe_weather;

import android.util.Log;

/**
 * Created by ENIAC on 2018/3/31.
 */

public class MLog {
    public static  final String TAG = "zzs";
    public static boolean Debug = true;

    public static void d(String msg){
        if(Debug) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg){
        if(Debug) {
            Log.e(TAG, msg);
        }
    }
}
