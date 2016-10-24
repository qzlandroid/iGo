package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.ConfirmedOrderActivity;
import com.example.max.iGo.Model.AddressModel;
import com.example.max.iGo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QZ on 2016/3/10.
 */
public class MagAddressActivity extends BaseActivity {
    private List<AddressModel > list;
    private TextView titleBarText;
    private ImageView image_back;
    private int num;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magcargoadress);
        titleBarText = (TextView)findViewById(R.id.titlebar_text);
        image_back = (ImageView)findViewById(R.id.titlebar_back);
        titleBarText.setText("收货地址管理");
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        //initTitleBar();
        ListView lv = (ListView) findViewById(R.id.address_mana_lv);
         sp=getSharedPreferences("Person_Address", MODE_PRIVATE);
        SharedPreferences.Editor Edit=sp.edit();
        num=sp.getInt("count",0);

        initData();
        lv.setAdapter(new MyAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.getParent().requestDisallowInterceptTouchEvent(true);

                clickLvItem(position);
            }
        });

    }

    /**
     * lv的点击处理事件
     * @param position
     */
    private void clickLvItem(int position) {
        AddressModel addressModel = list.get(position);
        Intent intent = new Intent(this, ConfirmedOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("addr",addressModel);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
//        startActivityForResult(intent, 1);
    }


    private void initData() {
        list=new ArrayList<AddressModel>();
        for (int i = 0 ; i < num ; i++ ){
            String name=sp.getString("name"+(i + 1),"");
            String  iphone=sp.getString("phone" + (i + 1), "");
            String address=sp.getString("address"+(i + 1),"");
            AddressModel addressModel = new AddressModel(name,address,iphone);
            list.add(addressModel);
        }
    }

    public void Add_Address(View v)
    {
        Intent intent = new Intent();
        intent.setClass(this,AddAddressActivity.class);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(intent);
        finish();
    }
    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return num;
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
            View v = View.inflate(MagAddressActivity.this, R.layout.item_listview_address, null);
            String name;
            String iphone;
            String address;
            TextView mag_name = (TextView) v.findViewById(R.id.mag_name);
            TextView mag_iphone = (TextView) v.findViewById(R.id.mag_iphone);
            TextView mag_address = (TextView)v.findViewById(R.id.mag_address);
            name=sp.getString("name"+(position+1),"");
            iphone=sp.getString("phone" + (position + 1), "");
            address=sp.getString("address"+(position+1),"");

            mag_name.setText(name);
            mag_iphone.setText(iphone);
            mag_address.setText(address);
            return v;
        }
    }
}

