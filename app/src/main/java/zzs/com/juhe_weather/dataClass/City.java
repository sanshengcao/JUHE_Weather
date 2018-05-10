package zzs.com.juhe_weather.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

import zzs.com.juhe_weather.tools.Consts;

/**
 * Created by ENIAC on 2018/3/31.
 */

public class City implements Parcelable{
    public static final String ID = "id";
    public  static final String CityName = "city";
    public  static final String Province = "province";
    public  static final String District = "district";
    private int mId;
    private  String mDistrict;
    private String mProvince;
    private String mCityName;
    private String address;
    private double longitude;
    private double latitude;

    public City(){
        this(Consts.LOCATION_CITY_ID ,null,null,null);
    }

    public City(String province,String cityName,String district){
        this(Consts.LOCATION_CITY_ID ,province,cityName,district);
    }
    public City(int id,String province,String cityName){
        this(id,province,cityName,null);
    }

    public City(int id,String province,String cityName,String district){
        this(id,province,cityName,district,null,0.0,0.0);
    }

    public City(String province,String cityName,String district,String address,double longitude,double latitude) {
        this(Consts.LOCATION_CITY_ID ,province,cityName,district,address,longitude,latitude);
    }


    public City(int id,String province,String cityName,String district,String address,double longitude,double latitude){
        this.mId=id;
        this.mProvince=province;
        this.mCityName=cityName;
        this.mDistrict=district;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getProvince() {
        return mProvince;
    }

    public String getCity() {
        return mCityName;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() { return longitude;   }

    public double getLatitude() {
        return latitude;
    }

    public void setCity(String cityName) {
        this.mCityName = cityName;
    }

    public void setDistrict(String district) {
        this.mDistrict = district;
    }

    public void setProvince(String province) {
        this.mProvince = province;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    @Override
    public int hashCode() {
        int result = 1;
        result = result*31+this.mId;
        result = result*31+(mProvince==null?0:mProvince.hashCode());
        result = result*31+(mCityName==null?0:mCityName.hashCode());
        result = result*31+(mDistrict==null?0:mDistrict.hashCode());
        result = result*31+(address==null?0:address.hashCode());
        long lon = Double.doubleToLongBits(longitude);
        long lat = Double.doubleToLongBits(latitude);
        result=result+(int)(lon^(lon>>>32));
        result = result +(int)(lat^(lat>>>32));


        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return  false;
        }else if(obj instanceof City){
           City city = (City)obj;
           int id = city.getId();
           String province = city.getProvince();
           String cityName= city.getCity();
           String district = city.getDistrict();

           if(     mId == id&&
                   mProvince.equals(province)
                   &&mCityName.equals(cityName)
                   &&mDistrict.equals(district)){
               return true;
           }else {
               return false;
           }
        }
        return false;
    }



    @Override
    public String toString() {
        return "id="+mId+",province="+mProvince+",cityName="+mCityName
                +",district="+mDistrict+"address ="+address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mProvince);
        dest.writeString(mCityName);
        dest.writeString(mDistrict);
        dest.writeString(address);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    public static Parcelable.Creator<City> CREATOR = new Creator<City>(){

        @Override
        public City createFromParcel(Parcel in) {
            return new City(in.readInt(),in.readString(),in.readString(),in.readString(),
                    in.readString(),in.readDouble(),in.readDouble());
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
