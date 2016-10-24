package com.example.max.iGo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sdh on 2016/4/7.
 */
public class PrefUtils {
    public final static String PREF_NAME="login_config";
    public static int getId(Context ctx){

        SharedPreferences  sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
       return  sp.getInt("id",-1);
    }
    public static String getValidation(Context ctx){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return  sp.getString("validation", "");
    }
    public static void setValidation(Context ctx,String validation){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString("validation",validation).commit();
    }
    public static void setId(Context ctx,int id){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putInt("id", id).commit();
    }
    public static void setUrlPath(Context ctx,String urlpath){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString("urlpath", urlpath).commit();
    }
    public static String getUrlPath(Context ctx){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return  sp.getString("urlpath", "");
    }
    public static void setUserName(Context ctx,String UserName){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString("UserName", UserName).commit();
    }
    public static String getUserName(Context ctx){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return  sp.getString("UserName", "");
    }
    public static void setSex(Context ctx,String sex){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString("sex", sex).commit();
    }
    public static String getSex(Context ctx){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return  sp.getString("sex", "");
    }
    public static void setHeadImgUrl(Context ctx,String headImgUrl){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString("headImgUrl", headImgUrl).commit();
    }
    public static String getHeadImgUrl(Context ctx){
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return  sp.getString("headImgUrl", "");
    }


}
