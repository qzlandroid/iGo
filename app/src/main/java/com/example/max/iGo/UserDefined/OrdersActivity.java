package com.example.max.iGo.UserDefined;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.max.iGo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdersActivity extends Activity {
    private TextView titleBar_textview_title;
    private ListView orders_lv;
    private ImageView image_back;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        orders_lv = (ListView) findViewById(R.id.act_orders_lv);//获取listview

        titleBar_textview_title =(TextView)findViewById(R.id.titlebar_text);
        image_back = (ImageView)findViewById(R.id.titlebar_back);
        titleBar_textview_title.setText("我的订单");
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });

        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                R.layout.activity_orders_lv_item, new String[] { "img", "title", "info" },
                new int[] { R.id.imageView_orders, R.id.textView_orders_GoodsName, R.id.ordersStatus_TextView });
        orders_lv.setAdapter(adapter);

        super.onCreate(savedInstanceState);
    }


    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.account_center_cargo);
        map.put("title", "鹿州春装新款长袖圆领棉麻连衣裙文艺复古小清新宽松中裙女系带裙");
        map.put("info", "待发货");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.account_center_comment);
        map.put("title", "派妃手工双面羊绒大衣女中长款2016春秋新款双面呢羊毛呢外套高端");
        map.put("info", "待发货");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.index_category_baby);
        map.put("title", "QISENCHUN 日系甜美可爱少女学院风萌兔子宽松娃娃领连衣裙");
        map.put("info", "已收货");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.index_category_books);
        map.put("title", "奈昕定制 113455 new dress华丽端庄亮片长款收腰显瘦短袖连衣裙 ");
        map.put("info", "待发货");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.index_category_decoration);
        map.put("title", "Mymo朗黛女装新品上新简单圆领打底裙修身显瘦长袖连衣裙L654D ");
        map.put("info", "交易成功");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.index_category_brand);
        map.put("title", "Zac Posen/扎克·珀森 女装 女式连衣裙 Q01685774 ");
        map.put("info", "订单取消");
        list.add(map);

        return list;
    }

}
