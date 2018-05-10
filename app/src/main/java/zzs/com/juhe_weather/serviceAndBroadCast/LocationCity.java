package zzs.com.juhe_weather.serviceAndBroadCast;

import android.content.Context;
import android.os.Handler;

import zzs.com.juhe_weather.tools.BDLocationUtils;

/**
 * Created by ENIAC on 2018/5/1.
 */

public class LocationCity {
    private BDLocationUtils bdLocationUtils ;
    public LocationCity(Context context,Handler handler){
        bdLocationUtils = new BDLocationUtils(context,handler);
    }

    public BDLocationUtils getBDLocationUtils(){
        return bdLocationUtils;
    }

    public void setLocationCity(){
        bdLocationUtils.doLocation();
        bdLocationUtils.locationClient.start();
    }

    public void stopLocation(){
        bdLocationUtils.locationClient.stop();
    }


}
