package zzs.com.juhe_weather.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ENIAC on 2018/4/2.
 */
public class Weather_id implements Parcelable{

    private String fa;
    private String fb;

    public Weather_id(){
        this(null,null);
    }

    public Weather_id(String fa,String fb){
        this.fa=fa;
        this.fb=fb;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }
    public String getFa() {
        return fa;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }
    public String getFb() {
        return fb;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Weather_id){
            Weather_id wid = (Weather_id) obj;
            if(wid.getFa().equals(this.fa)&&wid.fb.equals(this.fb)){
                return true;
            }
            return false;
        }
        return false;

    }

    @Override
    public int hashCode() {
        int pamre=31;
        int result=1;
        result=pamre*result+(
                fa==null?0:fa.hashCode());
        result=pamre*result+(
                fb==null?0:fb.hashCode());
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(fa);
        out.writeString(fb);
    }

    public static final Parcelable.Creator<Weather_id> CREATOR = new Creator<Weather_id>() {
        @Override
        public Weather_id createFromParcel(Parcel in) {
            return new Weather_id(in.readString(),in.readString());
        }

        @Override
        public Weather_id[] newArray(int size) {
            return new Weather_id[size];
        }
    };
}
