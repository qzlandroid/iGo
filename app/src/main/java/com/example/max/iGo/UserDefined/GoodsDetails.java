package com.example.max.iGo.UserDefined;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.R;
import com.example.max.iGo.Utils.CircleImageView;

public class GoodsDetails extends BaseActivity {
    private TextView titleBarText;
    private ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_goodsdetails);
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        image_back = (ImageView) findViewById(R.id.titlebar_back);
        titleBarText.setText("宝贝详情");
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        ListView comment_lv = (ListView) findViewById(R.id.comment_lv);
        comment_lv.addHeaderView(View.inflate(this, R.layout.chat_above_of_comment, null));
        comment_lv.setAdapter(new MyAdapter());
    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            comment_listView mHolder=null;
            if (convertView == null) {
                convertView = View.inflate(GoodsDetails.this, R.layout.chat_item_comment, null);
                mHolder=new comment_listView();
                mHolder.user_comment_img=(CircleImageView)convertView.findViewById(R.id.comment_img_user);
                mHolder.time=(TextView)convertView.findViewById(R.id.comment_time);
                mHolder.username=(TextView)convertView.findViewById(R.id.user_name);
                mHolder.floor_num=(TextView)convertView.findViewById(R.id.floor_comment);
                convertView.setTag(mHolder);

            }else{
                mHolder=(comment_listView)convertView.getTag();
            }
            mHolder.floor_num.setText((position+1)+ "楼");


            return convertView;
        }

        class comment_listView {
            CircleImageView user_comment_img;
            TextView time;
            TextView username;
            TextView floor_num;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }

    public void goods_detail(View v) {
        System.out.println("Hello Wrold");
    }
}

