package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.iGo.R;

public class ForgetPasswdActivity extends AppCompatActivity {
    TextView titleBarText;
    Button registerNext;
    ImageView titleBack;
    EditText register_edit_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_register);
        init();
        setonclick();
    }
    //初始化所有控件的方法
    public void init() {
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        registerNext = (Button) findViewById(R.id.register_next);
        register_edit_phone = (EditText) findViewById(R.id.register_edit_phone);
        titleBack = (ImageView) findViewById(R.id.titlebar_back);
        titleBarText.setText("忘记密码");

    }

    //设置返回键和下一步的监听
    public void setonclick() {
        //下一步
        registerNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phonenumber=register_edit_phone.getText().toString();
                Intent intent = new Intent(ForgetPasswdActivity.this,ForgetPasswdNextActivity.class);
                intent.putExtra("phone_register",phonenumber);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            }
        });

        //返回
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //跳转动画
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
        register_edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_edit_phone.setCursorVisible(true);
                register_edit_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            }
        });
    }
    public void clear_data(View view){
        register_edit_phone.setText("");

    }
}
