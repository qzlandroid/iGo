package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.R;
import com.example.max.iGo.SettingInfoActivity;
import com.example.max.iGo.Utils.PrefUtils;

public class Nickname_Setting_Activity extends AppCompatActivity {

    private EditText ev_nickname;
    TextView titleBarText;
    ImageView titleBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname__setting_);
        ev_nickname = (EditText)findViewById(R.id.ev_nickname);
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        titleBack = (ImageView) findViewById(R.id.titlebar_back);
        titleBarText.setText("修改昵称");
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
    }

    public void clear_data_nickname(View view){
        ev_nickname.setText("");
    }
    public void save_nickname(View v){
        Intent intent=new Intent(Nickname_Setting_Activity.this, SettingInfoActivity.class);
        intent.putExtra("changeNickname", "yes");
        if (ev_nickname.getText().toString().contains(" ") ||
                ev_nickname.getText().toString().equals("")||
                ev_nickname.getText().toString().equals("null") )
        {
            Toast.makeText(Nickname_Setting_Activity.this,"昵称不能含有空格,null,",Toast.LENGTH_SHORT).show();
        }else{
            PrefUtils.setUserName(Nickname_Setting_Activity.this, ev_nickname.getText().toString());
            startActivity(intent);
            finish();

        }

    }

}
