package com.example.max.iGo.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sdh on 2016/4/10.
 */
public class ReadDbCountry_Phone {
    private final static String path="data/data/com.example.max.igo/files/country_phone.db";
    public static void GetCountryName_NUmber(){
        SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
       Cursor cursor=db.query("country", new String[]{"country_code", "country_name_cn"},
                null, null, null, null, null);
//        Cursor cursor= db.rawQuery("select country_code,country_name_cn  from country",null);
        while(cursor.moveToNext()){
            String countryname=cursor.getString(cursor.getColumnIndex("country_name_cn"));
            String countrycode=cursor.getString(cursor.getColumnIndex("country_code"));
            System.out.println(countrycode+":"+countryname+"NNN");
        }
        cursor.close();
    }

}
