package zzs.com.juhe_weather.tools;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zzs.com.juhe_weather.MLog;

/**
 * Created by ENIAC on 2018/4/12.
 */

public class CitySQLiteHelper extends SQLiteOpenHelper {

    private static final int version = 1;
    public static final String CityDB = "CityDB";

    public CitySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public CitySQLiteHelper(Context context){
        super(context,CityDB,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table CityDB (id integer primary key NOT NULL, " +
                "province char(10), city char(15), district char(15))" ;
        try {
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        MLog.e("CitySQLiteHelper : create city");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
