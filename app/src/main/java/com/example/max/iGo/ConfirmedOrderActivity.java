package com.example.max.iGo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.max.iGo.Adapter.*;
import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.Model.AddressModel;
import com.example.max.iGo.Model.ConfirmOrderModel;
import com.example.max.iGo.UserDefined.MagAddressActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单页面.
 * 未解决的问题：加减按钮修改textview，点击收货地址选择。
 */
public class ConfirmedOrderActivity extends BaseActivity {
    private AdpConfirmedOrder adpConfirmedOrder;
    private List<ConfirmOrderModel> list;
    private TextView titleBarText;
    private ImageView image_back;
    private ListView confirmListView;
    private TextView addrmName;
    private TextView addriphone;
    private TextView address,count1;
    private RelativeLayout addrLayout;
    private AddressModel addressModel;
    int countInt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_order);
        init();

    }

    public void init() {
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {



        //设置lv里面的数据
        list=new ArrayList<ConfirmOrderModel>();
        for(int i=0;i<10;i++){
            ConfirmOrderModel confirmOrderModel =new ConfirmOrderModel("商品"+i,"155","http://pic18.nipic.com/20120108/6608733_091741091355_2.jpg","302"+i,"1");
            list.add(confirmOrderModel);
        }
        adpConfirmedOrder = new AdpConfirmedOrder(list,this);
        confirmListView.setAdapter(adpConfirmedOrder);

    }

    /**
     * 初始化View
     */
    private void initView() {
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        image_back = (ImageView) findViewById(R.id.titlebar_back);
        count1 = (TextView) findViewById(R.id.confirm_count_1);
        addrmName = (TextView) findViewById(R.id.confirm_name);
        addriphone = (TextView) findViewById(R.id.confirm_phone);
        address = (TextView) findViewById(R.id.confirm_addr);
        addrLayout = (RelativeLayout) findViewById(R.id.confirm_addr_layout);

        titleBarText.setText("确认订单");


        //Listview的设置

        confirmListView = (ListView) findViewById(R.id.listView_confirm);


    }

    private void initListener() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        addrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ConfirmedOrderActivity.this, MagAddressActivity.class);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                startActivityForResult(intent, 1);
            }
        });
        confirmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                // TextView itemTv = (TextView) view.findViewById(R.id.tv);
                // String text = itemTv.getText().toString();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                //接受传递过来的数据
                Bundle bundle = data.getExtras();
                addressModel = (AddressModel) bundle.getSerializable("addr");
                if (addressModel == null){
                   return;
                }
                System.out.println(addressModel.getAddress());
                System.out.println(addressModel.getName());
                System.out.println(addressModel.getIphone());


                //设置接受到的数据
                addrmName.setText(addressModel.getName());
                addriphone.setText(addressModel.getIphone());
                if (addressModel.getAddress().length()<15) {
                    address.setText(addressModel.getAddress());
                }
                else{
                    address.setText(addressModel.getAddress().substring(0,15)+"...");
                }
                break;


        }

    }
}