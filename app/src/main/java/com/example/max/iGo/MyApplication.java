package com.example.max.iGo;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

/**
 * Created by sdh on 2016/3/28.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
        JPushInterface.setLatestNotificationNumber(this, 3);
        SMSSDK.initSDK(this, "110b00ad9ea80", "11b405855a32734d574679182b37ae45");
    }
}
