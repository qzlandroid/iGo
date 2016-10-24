package com.example.max.iGo.Utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by sdh on 2016/4/7.
 */
public class GlobalCheckPost {
    public final static String SERVER_URL="http://192.168.1.105:3000";
//    public final static String SUCCESS ="success";
//    public final static String FAILURE ="failure";
//    public static String onSuccessMessage="";
//    public static String onFailureMessage="";
//    public static String status="";



    public static void sendToServerForCheck(String path,String id,
                                            String validation,AsyncHttpResponseHandler MyResponseInterface){
        AsyncHttpClient ahc=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("id",id);
        params.put("validation",validation);
        ahc.post(SERVER_URL+path, params, MyResponseInterface);

    }
}

