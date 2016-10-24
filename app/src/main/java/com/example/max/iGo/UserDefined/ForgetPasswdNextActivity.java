package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.R;
import com.example.max.iGo.Utils.GlobalCheckPost;
import com.example.max.iGo.Utils.MD5Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPasswdNextActivity extends AppCompatActivity {
    TextView titleBarText;
    EditText registerNextVeri;
    EditText registerNextPasswd;
    private EventHandler eventHandler;
    ImageView imageView;
    Button registerNextSucceed;
    private EventHandler callback;
    private final String COUNTYRY = "86";
    private String phone;
    private Mycount mc;
    private Button request_for_code;
    private long time;
    private long time_raw;

    private int flag_rquest_code = 1;
    private SharedPreferences sp;
    private EditText confirm_passwd_et;
    private ImageView confirm_passwd_iv;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;


            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    System.out.println(data.toString() + "OOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (data != null) {
                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                        String country_get = (String) phoneMap.get("country");
                        String phone_get = (String) phoneMap.get("phone");
                        System.out.println("PP" + country_get + "PP" + phone_get + "LLLLLLLLLLLLLLLLLLLLMMMGG");
                        if (phone_get != null && country_get != null && country_get.length() != 0) {
                            if (phone.equals(phone_get) && country_get.equals(COUNTYRY)) {
                               post_data();
                            } else {
                                //  Toast.makeText(RegisterNextActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                System.out.println("注册失败");
                            }
                        }
                    }

                }

            } else {
                //  ((Throwable) data).printStackTrace();
                try {
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");//错误描述
                    int status = object.optInt("status");//错误代码
                    if (status > 0 && !TextUtils.isEmpty(des)) {
                        if (status == 468) {
                            Toast.makeText(ForgetPasswdNextActivity.this, "您输入的验证码不正确", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(ForgetPasswdNextActivity.this, des, Toast.LENGTH_SHORT).show();
                            System.out.println("错误代码" + status + "错误描述" + des);
                            System.out.println("以上是错误");

                        }

                        return;
                    }
                } catch (Exception e) {
                    //do something
                    e.printStackTrace();
                }
            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_register_next);
        init();
        setOnClick();
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone_register");
        eventHandler = new EventHandler() {

            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
     //   SMSSDK.getSupportedCountries();


    }
    class MyResponseInterface extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            System.out.println("KLKL"+new String(bytes));
            if("success".equals(new String(bytes))){
                Toast.makeText(ForgetPasswdNextActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetPasswdNextActivity.this,LoginActivity.class));

            }else{

            }

        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            Toast.makeText(ForgetPasswdNextActivity.this,"网络错误",Toast.LENGTH_SHORT);
        }
    }

    public void init() {
        time_raw = 0;
        Button register_button= (Button) findViewById(R.id.registerNext_succeed);
        TextView passwd_first = (TextView) findViewById(R.id.passwd_first);
        sp = getSharedPreferences("time_config", MODE_PRIVATE);
        request_for_code = (Button) findViewById(R.id.request_for_code);
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        imageView = (ImageView) findViewById(R.id.titlebar_back);
        registerNextVeri = (EditText) findViewById(R.id.registerNext_veri);
        registerNextPasswd = (EditText) findViewById(R.id.registerNext_passwd);
        registerNextSucceed = (Button) findViewById(R.id.registerNext_succeed);
        confirm_passwd_et = (EditText) findViewById(R.id.confirm_passwd_et);
        confirm_passwd_iv = (ImageView) findViewById(R.id.confirm_passwd_iv);
        titleBarText.setText("忘记密码");
        passwd_first.setText("修改密码");
        register_button.setText("确认修改");

    }

    public void setOnClick() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });


        //验证码输入框监听
        registerNextVeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNextVeri.setCursorVisible(true);
                registerNextVeri.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            }
        });

        //密码输入框监听
        registerNextPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNextPasswd.setCursorVisible(true);
            }
        });
    }

    public void check_time() {
        long later_time = sp.getLong("later_time", 0);
        long former_time = sp.getLong("former_time", 0);
        long rest_time = sp.getLong("rest_time", 0);
        long time_division = later_time - former_time;
        System.out.println(later_time + ":" + former_time + ":" + rest_time + "LLLLLL");
        if (former_time > 0 && rest_time > 0 && time_division < rest_time && later_time > 0) {
            Mycount mc2 = new Mycount(rest_time - time_division, 1000);
            request_for_code.setEnabled(false);
            request_for_code.setBackgroundColor(Color.GRAY);
            mc2.start();
        }

    }

    public void check_for_code(View v) {

        SMSSDK.getVerificationCode(COUNTYRY, phone);
        v.setEnabled(false);
        v.setBackgroundColor(Color.GRAY);
        mc = new Mycount(60000, 1000);
        mc.start();
        System.out.println(phone + ":" + COUNTYRY);

    }

    public void setRegisterCallback(EventHandler callback) {
        this.callback = callback;
    }
    //password_modify tel password
    public void register_button(View v) {
        final String code = registerNextVeri.getText().toString();
        final String passwd_user = registerNextPasswd.getText().toString();
        String passwd_confirm=confirm_passwd_et.getText().toString();
        if(TextUtils.isEmpty(passwd_user)||TextUtils.isEmpty(passwd_confirm)){
            Toast.makeText(ForgetPasswdNextActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
        }else if(!passwd_confirm.equals(passwd_user)){
            Toast.makeText(ForgetPasswdNextActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
        }else {
            SMSSDK.submitVerificationCode(COUNTYRY, phone, code);
          //  test();
            System.out.println("点击确认密码按钮");
        }


    }

    class Mycount extends CountDownTimer {
        public Mycount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            time_raw = millisUntilFinished;
            time = (long) (Math.floor(millisUntilFinished / 1000.0));
            request_for_code.setText("重新发送" + time + "s");
        }

        @Override
        public void onFinish() {
            request_for_code.setEnabled(true);
            request_for_code.setBackgroundColor(0xff06b5e2);
            request_for_code.setText("请求验证码");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        sp.edit().putLong("later_time", System.currentTimeMillis()).commit();
        check_time();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sp.edit().putLong("former_time", System.currentTimeMillis()).commit();
        sp.edit().putLong("rest_time", time_raw).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    public void clear_data(View view) {
        switch (view.getId()) {
            case R.id.clear_code:
                registerNextVeri.setText("");
                break;
            case R.id.clear_passwd:
                registerNextPasswd.setText("");
                break;
            case R.id.confirm_passwd_iv:
                confirm_passwd_et.setText("");
                break;
        }

    }
    public void post_data(){
        AsyncHttpClient ahc=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        JSONObject resgister_msg=new JSONObject();
        try {
            resgister_msg.put("tel",phone);
            resgister_msg.put("password", MD5Utils.encode(registerNextPasswd.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator keys = resgister_msg.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                params.put(key, resgister_msg.get(key).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println("post执行成功！");

        ahc.post(GlobalCheckPost.SERVER_URL+"/mobile_profile/password_modify", params, new MyResponseInterface());
                System.out.println("params" + params.toString());

        //     ahc.post(RegisterNextActivity.this,"http://192.168.1.102:3000/mobile_profile/register",null, params,
        //           RequestParams.APPLICATION_JSON, new MyResponseInterface());


    }
}
