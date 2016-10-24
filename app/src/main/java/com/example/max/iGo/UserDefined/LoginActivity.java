package com.example.max.iGo.UserDefined;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.MainActivity;
import com.example.max.iGo.R;
import com.example.max.iGo.Utils.CheckNetState;
import com.example.max.iGo.Utils.GlobalCheckPost;
import com.example.max.iGo.Utils.MD5Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mob.tools.utils.UIHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * 用户登录注册界面
 */
public class LoginActivity extends BaseActivity implements Handler.Callback, PlatformActionListener, View.OnClickListener {

    Boolean isQQ = false;
    Boolean isSina = false;
    private EditText fragment_account_login_phone;
    private EditText fragment_account_login_passwd;
    private LinearLayout sina_login;
    private LinearLayout qq_login;
    private SharedPreferences sp;

    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private TextView forget_passwd;
    private TextView quick_register;
    private String user_phone;
    private String user_passwd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_login);

        ShareSDK.initSDK(this);

        //初始化控件
        forget_passwd = (TextView) findViewById(R.id.forget_passwd);
        quick_register = (TextView) findViewById(R.id.quick_register);
        fragment_account_login_phone = (EditText) findViewById(R.id.fragment_account_login_phone);
        fragment_account_login_passwd = (EditText) findViewById(R.id.fragment_account_login_passwd);
        ImageView fragment_account_login_close = (ImageView) findViewById(R.id.fragment_account_login_close);
        sina_login = (LinearLayout) findViewById(R.id.sina_login);
        qq_login = (LinearLayout) findViewById(R.id.qq_login);
        sp = getSharedPreferences("login_config", MODE_PRIVATE);
        //设置监听
        forget_passwd.setOnClickListener(this);
        quick_register.setOnClickListener(this);
        fragment_account_login_close.setOnClickListener(this);
        fragment_account_login_phone.setOnClickListener(this);
        fragment_account_login_passwd.setOnClickListener(this);
        sina_login.setOnClickListener(this);
        qq_login.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_passwd:
                startActivity(new Intent(LoginActivity.this, ForgetPasswdActivity.class));
                break;
            case R.id.fragment_account_login_close:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

                //跳转动画

                break;
            case R.id.fragment_account_login_phone:
                fragment_account_login_phone.setCursorVisible(true);
                break;
            case R.id.fragment_account_login_passwd:
                fragment_account_login_passwd.setCursorVisible(true);
                break;

            case R.id.sina_login:
                //初始化新浪平台,这两种方式都可以
                //There are two function to initializing SinaWeibo object
                //First,Platform pf = ShareSDK.getPlatform(MainActivity.this, SinaWeibo.NAME);
                SinaWeibo pf = new SinaWeibo(this);
                if (pf.isValid())
                    pf.removeAccount();
                //关闭SSO授权，用网页授权代替；true-关闭；flase，开启
                //Closing SSO authorize, using web authorize replace. True is not sso.
                //If you want to SSO, your app's md5 must be equal with the md5 on SinaWeibo Development Platform
                pf.SSOSetting(false);
                //设置监听
                //Setting authorization listener
                pf.setPlatformActionListener(this);
                //获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
                //Perform showUser action,in order to get user info;
                pf.showUser(null);
                isSina = true;
                break;
            case R.id.qq_login:
                QQ qpf = new QQ(this);
                if (qpf.isValid())
                    qpf.removeAccount();
                qpf.SSOSetting(false);
                qpf.setPlatformActionListener(this);
                qpf.showUser(null);
                isQQ = true;
                break;

        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: { // 成功, successful notification
                        showNotification(2000, "Auth successfully");

                        //授权成功后,获取用户信息，要自己解析，看看oncomplete里面的注释
                        //ShareSDK只保存以下这几个通用值
                        if (isSina) {
                            Platform pf = ShareSDK.getPlatform(LoginActivity.this, SinaWeibo.NAME);
                            Log.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
                            Log.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
                            Log.e("sharesdk use_icon", pf.getDb().getUserIcon());//获取用户头像


                        }
                        if (isQQ) {
                            Platform pf = ShareSDK.getPlatform(LoginActivity.this, QQ.NAME);
                            Log.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
                            Log.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
                            Log.e("sharesdk use_icon", pf.getDb().getUserIcon());//获取用户头像

                        }
                        //pf.author()这个方法每一次都会调用授权，出现授权界面
                        //如果要删除授权信息，重新授权
                        //pf.getDb().removeAccount();
                        //调用后，用户就得重新授权，否则下一次就不用授权

					/*ShareSDK provide some methods to get some normal userinfo,for example
                    *  Platform pf = ShareSDK.getPlatform(MainActivity.this, SinaWeibo.NAME);
					*  Log.e("sharesdk use_id", pf.getDb().getUserId()); //get user id
					*  Log.e("sharesdk use_name", pf.getDb().getUserName());//get user name
					*  Log.e("sharesdk use_icon", pf.getDb().getUserIcon());//get user icon
					*
					*  pf.isValid(),this method is used to determine the weibo whether is authorized or not.
					*  But isValid this method is used to detemine these platform of Wechat\QQ\Google+\Instragram has Client.
					*  If you want to remove authorization, you can use the method of pf.getDB().removeAccount()
					*/
                    }
                    break;
                    case 2: { // 失败, fail notification
                        String expName = msg.obj.getClass().getSimpleName();
                        if ("WechatClientNotExistException".equals(expName)
                                || "WechatTimelineNotSupportedException".equals(expName)) {
                            showNotification(2000, getString(R.string.wechat_client_inavailable));
                        } else if ("GooglePlusClientNotExistException".equals(expName)) {
                            showNotification(2000, getString(R.string.google_plus_client_inavailable));
                        } else {
                            if ("QQClientNotExistException".equals(expName)) {
                                showNotification(2000, getString(R.string.qq_client_inavailable));
                            } else {
                                showNotification(2000, "Auth unsuccessfully");
                            }
                        }
                        isSina = false;
                        isQQ = false;
                    }
                    break;
                    case 3: { // 取消, cancel notification
                        showNotification(2000, "Cancel authorization");
                        isSina = false;
                        isQQ = false;
                    }
                    break;
                }
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }

    // 在状态栏提示分享操作,the notification on the status bar
    private void showNotification(long cancelTime, String text) {
        try {
            Context app = getApplicationContext();
            NotificationManager nm = (NotificationManager) app
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            final int id = Integer.MAX_VALUE / 13 + 1;
            nm.cancel(id);

            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.drawable.logo, text, when);
            PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
//            notification.setLatestEventInfo(app, "sharesdk test", text, pi);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(id, notification);

            if (cancelTime > 0) {
                Message msg = new Message();
                msg.what = MSG_CANCEL_NOTIFY;
                msg.obj = nm;
                msg.arg1 = id;
                UIHandler.sendMessageDelayed(msg, cancelTime, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (isSina) {
            Platform pf = ShareSDK.getPlatform(LoginActivity.this, SinaWeibo.NAME);
            Log.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
            Log.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
            Log.e("sharesdk use_icon", pf.getDb().getUserIcon());//获取用户头像
//            Toast.makeText(getApplicationContext(),pf.getDb().getUserName(),Toast.LENGTH_LONG).show();
            pf.getDb().removeAccount();
            isSina = false;

        }
        if (isQQ) {
            Platform pf = ShareSDK.getPlatform(LoginActivity.this, QQ.NAME);
            Log.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
            Log.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
            Log.e("sharesdk use_icon", pf.getDb().getUserIcon());//获取用户头像
//            Toast.makeText(getApplicationContext(),pf.getDb().getUserName(),Toast.LENGTH_LONG).show();
            pf.getDb().removeAccount();
            isQQ = false;

        }

    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();
        t.getMessage();

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public void login_button(View view) {
        user_phone = fragment_account_login_phone.getText().toString();
        user_passwd = MD5Utils.encode(fragment_account_login_passwd.getText().toString());
        if (CheckNetState.isNetworkAvailable(LoginActivity.this))
            post_data(user_phone, user_passwd);
        else
            Toast.makeText(LoginActivity.this, "请检查您的网络设置", Toast.LENGTH_SHORT).show();
    }

    private void post_data(String phone, String passwd) {
        AsyncHttpClient ahc = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("tel", phone);
        params.put("password", passwd);


        ahc.post(GlobalCheckPost.SERVER_URL + "/mobile_profile/login", params, new MyResponseInterface());
        System.out.println("params" + params.toString());
    }

    class MyResponseInterface extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            String retstr = new String(bytes);
            if (retstr.contains("id") && retstr.contains("validation")) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(retstr);
                    int id = jsonObject.getInt("id");
                    String validation = jsonObject.getString("validation");
                    System.out.println(id + "KLKL" + validation);
                    if ((!TextUtils.isEmpty(id + "")) && (!TextUtils.isEmpty(validation))) {
                        sp.edit().putInt("id", id).commit();
                        sp.edit().putString("validation", validation).commit();
                        sp.edit().putString("user_phone", user_phone).commit();
                        sp.edit().putString("user_passwd", user_passwd).commit();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("account_need_refresh", true);
                    startActivity(intent);
                    finish();
                    //   String  validation =jsonObject.getString("")
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(LoginActivity.this, "密码或者用户名不正确", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
        }
    }
}
