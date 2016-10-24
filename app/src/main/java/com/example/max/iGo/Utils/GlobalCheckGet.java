package com.example.max.iGo.Utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by sdh on 2016/4/7.
 */
public class GlobalCheckGet {
    public final static String SERVER_URL="http://192.168.1.105:3000";
//    public final static String SUCCESS ="success";
//    public final static String FAILURE ="failure";
//    public static String onSuccessMessage="";
//    public static String onFailureMessage="";
//    public static String status;
    public static void sendToServerForCheck(String path,AsyncHttpResponseHandler MyResponseInterface){
        AsyncHttpClient ahc=new AsyncHttpClient();
        ahc.get(SERVER_URL+path, MyResponseInterface);

    }
}

