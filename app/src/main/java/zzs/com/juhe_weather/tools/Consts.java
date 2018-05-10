package zzs.com.juhe_weather.tools;

public class Consts {
	//请求网址
	/*例如http://v.juhe.cn/weather/index?format=2
	 *       &cityname=%E8%8B%8F%E5%B7%9E&key=KEY*/	
	public static final String URL_FRO_CITY_ID="http://v.juhe.cn/weather/index";
	
	//http://v.juhe.cn/weather/ip?format=2&key=KEY&ip=58.215.185.154
	public static final String URL_FOR_IP="http://v.juhe.cn/weather/ip";
	
	//http://v.juhe.cn/weather/geo?format=2&key=KEY&lon=116.39277&lat=39.933748
	public static final String URL_FOR_LOCATION="http://v.juhe.cn/weather/geo";
	
	//http://v.juhe.cn/weather/citys?key=KEY
	public static final String URL_QUERY_CITY="http://v.juhe.cn/weather/citys";
	
	//请求Key
	public static final String KEY="295d196ba418a7e43ffb480225ebef87";
	
	//error code
	public static final int RETURN_SUCCESSE=200;
	public static final int RETURN_FALSE=201;

	public static final int Type_Json2=2;
	public static final int Type_Json1=1;

	public static final int LOCATION_GPS = 100;
	public static final int LOCATION_NETWORK = 101;
	public static final int LOCATION_OFFLINE= 102;
	public static final int LOCATION_SERVICE_ERROR = 103;
	public static final int LOCATION_NETWORK_ERROR = 104;

	public static final int FLAG_ADD_CITY = 300;
	public static final int FLAG_CHANGED_CITY = 301;

	public static final int LOCATION_CITY_ID = -1;

}
