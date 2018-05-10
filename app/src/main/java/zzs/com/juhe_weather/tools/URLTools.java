package zzs.com.juhe_weather.tools;

/**
 * Created by ENIAC on 2018/4/10.
 */

public class URLTools {

    //http://v.juhe.cn/weather/index?format=2
    //	     &cityname=%E8%8B%8F%E5%B7%9E&key=KEY
    //type 是返回json格式
    public static String getURL_City(String city,int type){

        return Consts.URL_FRO_CITY_ID+"?format="
                +type+"&cityname="+city+"&key="+Consts.KEY;
    }

    public static String getURLForId(int id,int type){

        return Consts.URL_FRO_CITY_ID+"?format="
                +type+"&id="+id+"&key="+Consts.KEY;
    }

    public static String getURLForLocation(double longitude, double latitude,int type){

        http://v.juhe.cn/weather/geo?format=2&key=您申请的KEY&lon=116.39277&lat=39.933748
        return Consts.URL_FOR_LOCATION+"?format="
                +type+"&key="+Consts.KEY+"&lon="+longitude+"&lat="+latitude;
    }

    public static String getURLForIp(String ip,int type){

        return Consts.URL_FOR_IP+"?format="
                +type+"&key="+Consts.KEY+"&ip="+ip;
    }
    public static String getCitysURL(){

        return Consts.URL_QUERY_CITY+"?key="+Consts.KEY;
    }
}
