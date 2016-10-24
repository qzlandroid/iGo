package com.example.max.iGo.UserDefined;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.max.iGo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CollectionsActivity extends Activity {
    private TextView titleBar_textview_title;
    private ListView collections_lv;
    private ImageView image_back;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        collections_lv = (ListView) findViewById(R.id.act_collections_lv);//获取listview

        titleBar_textview_title =(TextView)findViewById(R.id.titlebar_text);
        image_back = (ImageView)findViewById(R.id.titlebar_back);
        titleBar_textview_title.setText("收藏商品");
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });

        collections_lv.setVerticalScrollBarEnabled(false);//隐藏lisetview滚动条
        collections_lv.setAdapter(new MyAdapter());
        collections_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    class MyAdapter extends BaseAdapter {
        public int getCount() {
            return 3;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(CollectionsActivity.this, R.layout.search_result_listview_list, null);
            ImageView search_result_listView_pic = (ImageView)v.findViewById(R.id.search_result_listview_list_pic);
            TextView search_result_listView_name = (TextView) v.findViewById(R.id.search_result_listview_name);
            TextView search_result_listView_price = (TextView) v.findViewById(R.id.search_result_listview_list_price);
            TextView search_result_listView_number = (TextView) v.findViewById(R.id.search_result_listview_number);
            switch (position%3) {
                case 0:
                    search_result_listView_pic.setImageResource(R.drawable.search_result_listview_list_pic1);
                    search_result_listView_name.setText("包顺丰斯帕丁NBA篮球74-604Y室内室外掌控");
                    search_result_listView_price.setText("￥155");
                    search_result_listView_number.setText("302销量");
                    break;
                case 1:
                    search_result_listView_pic.setImageResource(R.drawable.search_result_listview_list_pic2);
                    search_result_listView_name.setText("欧美黑白陶瓷戒指 女款镶18K尊享VIP金戒指");
                    search_result_listView_price.setText("￥600");
                    search_result_listView_number.setText("101销量");
                    break;
                case 2:
                    search_result_listView_pic.setImageResource(R.drawable.search_result_listview_list_pic3);
                    search_result_listView_name.setText("韩国运动鞋女 2016韩国新潮韩版女款透气网面");
                    search_result_listView_price.setText("￥366");
                    search_result_listView_number.setText("789销量");
                    break;
            }
            return v;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
    }



}
