package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.R;

public class AddAddressActivity extends AppCompatActivity {

    private TextView titleBarText;
    private ImageView image_back;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcargoadress);
        Button bt = (Button) findViewById(R.id.save_address);
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        image_back = (ImageView) findViewById(R.id.titlebar_back);
        titleBarText.setText("添加收货地址");
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText name = (EditText) findViewById(R.id.add_name);
                EditText iphone = (EditText) findViewById(R.id.add_iphone);
                EditText address_location = (EditText) findViewById(R.id.add_address_location);
                EditText address_area = (EditText) findViewById(R.id.add_address_area);
                String name_text = name.getText().toString();
                String phone_text = iphone.getText().toString();
                String location_text = address_location.getText().toString();
                String area_text = address_area.getText().toString();
                if (is_empty(name_text) || is_empty(phone_text) || is_empty(location_text) || is_empty(area_text)) {

                    Toast.makeText(AddAddressActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(AddAddressActivity.this, MagAddressActivity.class);
                    SharedPreferences sp = getSharedPreferences("Person_Address", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    if (sp.getInt("count", 0) == 0) {
                        //   Log.i("123","whatwhat");
                        edit.putInt("count", 1);
                        edit.commit();
                        num = sp.getInt("count", 0);
                    } else {

                        num = sp.getInt("count", 0) + 1;
                        edit.putInt("count", num);
                        edit.commit();
                    }

                    edit.putString("name" + num, name.getText().toString());
                    edit.putString("phone" + num, iphone.getText().toString());
                    edit.putString("address" + num, address_area.getText().toString() + address_location.getText().toString());
                    edit.commit();
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public boolean is_empty(String text) {
        if (!(null == text)) {
            if(text.length()==0)
                return true;

        } else
            return true;
        return false;
    }

}




