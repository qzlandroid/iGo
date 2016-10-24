package com.example.max.iGo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.UserDefined.AboutiGoActivity;
import com.example.max.iGo.UserDefined.LoginActivity;
import com.example.max.iGo.Utils.PrefUtils;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import cn.jpush.android.api.JPushInterface;


public class SettingAllActivity extends BaseActivity {
    private TextView titleBarText;
    private ImageView image_back;
    private RelativeLayout setting_info_layout;
    private MaterialAnimatedSwitch switchButton;
    private SharedPreferences sp_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_all);
        switchButton = (MaterialAnimatedSwitch) findViewById(R.id.switch_button);
        sp_switch = getSharedPreferences("switch_button_is_true", MODE_PRIVATE);

        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        image_back = (ImageView) findViewById(R.id.titlebar_back);
        titleBarText.setText("设置");

        init();
        switchButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (sp_switch.getBoolean("config", false)) {
                    if (!(switchButton.isChecked())) {
                        switchButton.toggle();
                    }
                } else {
                    if (switchButton.isChecked())
                        switchButton.toggle();
                }
            }
        });
        switchButton.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    JPushInterface.resumePush(SettingAllActivity.this);
                    sp_switch.edit().putBoolean("config", true).commit();


                } else {
                    JPushInterface.stopPush(SettingAllActivity.this);
                    sp_switch.edit().putBoolean("config", false).commit();

                }
            }
        });

    }

    //定义一个初始化所有控件的方法
    public void init() {


        //初始化个人资料
        setting_info_layout = (RelativeLayout) findViewById(R.id.setting_info_layout);
        //设置监听
        setting_info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingAllActivity.this, SettingInfoActivity.class);
                intent.putExtra("above","true");
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });


    }

    public void to_about_igo(View v) {
        Intent intent = new Intent(SettingAllActivity.this, AboutiGoActivity.class);
        startActivity(intent);
    }

    public void change_button_state() {
        switchButton.toggle();
    }
    public void sign_out(View view){
        PrefUtils.setId(SettingAllActivity.this,-1);
        PrefUtils.setValidation(SettingAllActivity.this, "");
        startActivity(new Intent(SettingAllActivity.this, LoginActivity.class));
        finish();
    }
}
