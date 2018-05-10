package zzs.com.juhe_weather.tools;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ENIAC on 2018/4/19.
 */

public class WeatherSQLiteHelper extends SQLiteOpenHelper {
    public static  final String WeatherDB = "WeatherDB";
    public static final int version = 1;

    public WeatherSQLiteHelper(Context context){
        super(context,WeatherDB,null,version);
    }

    public WeatherSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandle) {
        super(context, name, factory, version,errorHandle);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS WeatherDB(id INTEGER PRIMARY KEY NOT NULL," +
                "province CHAR(10), city CHAR(15), district CHAR(15)," +
                "real_w VARCHAR(2000),today_w VARCHAR(2000),future_w VARCHAR(2000))" ;
        try {
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
