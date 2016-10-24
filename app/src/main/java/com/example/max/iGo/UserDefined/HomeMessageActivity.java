package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.max.iGo.R;

public class HomeMessageActivity extends AppCompatActivity {
    TextView titleBarText;
    ImageView titleBack;
    private ListView lv_message;
    private RelativeLayout home_message_back;
    private TextView home_message_back_tvmiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_message);
        initData();
        lv_message=(ListView)findViewById(R.id.lv_message);
        lv_message.setAdapter(new MyAdapter());
        lv_message.setOnItemClickListener(new ItemListen());

    }

    private void initData() {
        lv_message=(ListView)findViewById(R.id.lv_message);
        titleBarText = (TextView)findViewById(R.id.titlebar_text);
        titleBarText.setText("消息");
        titleBack = (ImageView)findViewById(R.id.titlebar_back);
        home_message_back = (RelativeLayout) findViewById(R.id.home_message_rela);
        home_message_back_tvmiss = (TextView)findViewById(R.id.home_message_back_tvmiss);
        home_message_back_tvmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_message_back.setVisibility(View.INVISIBLE);
            }
        });
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
        home_message_back.setVisibility(View.INVISIBLE);
        lv_message.setAdapter(new MyAdapter());
        lv_message.setOnItemClickListener(new ItemListen());
    }


    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(HomeMessageActivity.this,R.layout.home_message_listview_list,null);
            ImageView home_message_img = (ImageView) view.findViewById(R.id.home_message_img);
            TextView home_message_name = (TextView) view.findViewById(R.id.home_message_name);
            TextView home_message_date = (TextView) view.findViewById(R.id.home_message_date);
            switch (position%2){
                case 0:
                    home_message_img.setImageResource(R.drawable.notification_music_icon);
                    home_message_name.setText("周末来爱购，音乐Party不停");
                    home_message_date.setText("2/24");
                    break;
                case 1:
                    home_message_img.setImageResource(R.drawable.notification_flower_icon);
                    home_message_name.setText("白色情人节，让鲜花陪伴");
                    home_message_date.setText("3/14");
            }
            return view;
        }

    }
    class ItemListen implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            home_message_back.setVisibility(View.VISIBLE);
        }
    }
}
