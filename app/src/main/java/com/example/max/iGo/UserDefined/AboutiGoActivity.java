package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.R;

/** 关于界面
 * Created by Max on 2016/3/9.
 */
public class AboutiGoActivity extends BaseActivity {

    private TextView titleBarText;
    private ImageView image_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutigo);
        titleBarText = (TextView)findViewById(R.id.titlebar_text);
        image_back = (ImageView)findViewById(R.id.titlebar_back);
        titleBarText.setText("关于");
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

}
