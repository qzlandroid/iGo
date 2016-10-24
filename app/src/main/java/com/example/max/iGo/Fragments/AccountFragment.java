package com.example.max.iGo.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.FeedbackActivity;
import com.example.max.iGo.R;
import com.example.max.iGo.SettingAllActivity;
import com.example.max.iGo.SettingInfoActivity;
import com.example.max.iGo.Utils.CheckNetState;
import com.example.max.iGo.Utils.CircleImageView;
import com.example.max.iGo.Utils.GlobalCheckGet;
import com.example.max.iGo.Utils.PrefUtils;
import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * 界面account
 */

public class AccountFragment extends Fragment implements View.OnClickListener{
    private ListView lv;
    private SharedPreferences sp;
    private String getFromServer="";
    private ImageView account_login;
    private TextView user_name;
    private LinearLayout after_login;
    private ImageView login_iv;
    private CircleImageView account_head_sculpture;
    private TextView account_username_nickname;
    private TextView fragment_account_tv_order;
    private TextView fragment_account_tv_collection;
    private TextView fragment_account_tv_order_number;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, null);
       //sp= getActivity().getSharedPreferences("login_config", Context.MODE_PRIVATE);
        initView(view);
        initListView(view);
        return view;
    }



    public void initView(View view){
        login_iv = (ImageView) view.findViewById(R.id.account_login);
        account_head_sculpture = (CircleImageView) view.findViewById(R.id.account_head_sculpture);
        after_login = (LinearLayout) view.findViewById(R.id.after_login);
        user_name = (TextView) view.findViewById(R.id.user_name);
        account_username_nickname = (TextView) view.findViewById(R.id.account_username_nickname);
        account_login = (ImageView) view.findViewById(R.id.account_login);
        fragment_account_tv_order = (TextView) view.findViewById(R.id.fragment_account_tv_order);
        fragment_account_tv_order_number = (TextView) view.findViewById(R.id.fragment_account_tv_order_number);
        fragment_account_tv_collection = (TextView) view.findViewById(R.id.fragment_account_tv_collection);
        login_iv.setOnClickListener(this);
        fragment_account_tv_order.setOnClickListener(this);
        fragment_account_tv_collection.setOnClickListener(this);
        account_head_sculpture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SettingInfoActivity.class);
                intent.putExtra("above","true");
                startActivity(intent);
            }
        });
      //  System.out.println("AAAAAAAAAAAAAAAAAAAA再次执行");
     //   System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAsucess Message before" + GlobalCheckGet.onSuccessMessage);
        if (CheckNetState.isNetworkAvailable(getActivity())){
            GlobalCheckGet.sendToServerForCheck("/mobile_profile" + "?id=" + PrefUtils.getId(getActivity()) + "" + "&validation=" + URLEncoder.encode(PrefUtils.getValidation(getActivity())),
                    new MyResponseInterface());
            GlobalCheckGet.sendToServerForCheck("/mobile_orders/count" + "?id=" + PrefUtils.getId(getActivity()) + "" + "&validation=" + URLEncoder.encode(PrefUtils.getValidation(getActivity())),
                    new MyResponseOrder());
        }else{
            Toast.makeText(getActivity(), "请检查您的网络设置", Toast.LENGTH_SHORT).show();

        }



    }
    class MyResponseOrder extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            if (bytes.length>0){
                try {
                    System.out.println(new String(bytes)+"LLLLLLLLL");
                    JSONObject jsonObject=new JSONObject(new String(bytes));
                    fragment_account_tv_order_number.setText(jsonObject.getString("count"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }
    }
    class MyResponseInterface extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            JSONObject jsonObject;
            if (bytes.length>0){
                getFromServer =new String(bytes);

            }
            if (getFromServer.startsWith("{")&& getFromServer.endsWith("}")){
                account_login.setVisibility(View.GONE);
                after_login.setVisibility(View.VISIBLE);
                System.out.println("AAAAAAAAAshi以大括号开头的");
                System.out.println("?????????????????另一个"+getFromServer);

                try {
                     jsonObject=new JSONObject(getFromServer);
                    System.out.println("数据"+getFromServer);
                    System.out.println("AAAAAAAAA"+jsonObject.getString("name")+"BBBB");
                    if(jsonObject.getString("name") != null &&
                            ! jsonObject.getString("name").equals("null")&&
                            ! jsonObject.getString("name").equals("")){
                        account_username_nickname.setText(jsonObject.getString("name"));
                        System.out.println("tel:tel:"+jsonObject.get("tel"));
                    }else{
                        account_username_nickname.setText(jsonObject.getString("tel"));
                        System.out.println("执行了");
                    }

                    if(jsonObject.getString("icon") != null && ! jsonObject.getString("icon").equals("null")){
                        BitmapUtils bitmapUtils=new BitmapUtils(getActivity());
                        bitmapUtils.display(account_head_sculpture, GlobalCheckGet.SERVER_URL+jsonObject.getString("icon"));
                    }else{
                        account_head_sculpture.setImageResource(R.drawable.touxing_comment);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            else{
                account_login.setVisibility(View.VISIBLE);
                after_login.setVisibility(View.GONE);
            }

        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
       //   handler.sendEmptyMessage(0);

        }
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_login:
                Intent intent = new Intent(getActivity(),com.example.max.iGo.UserDefined.LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_account_tv_order:
                Intent intent2 = new Intent(getActivity(),com.example.max.iGo.UserDefined.OrdersActivity.class);
                startActivity(intent2);
                break;
            case R.id.fragment_account_tv_collection:
                Intent intent3 = new Intent(getActivity(),com.example.max.iGo.UserDefined.CollectionsActivity.class);
                startActivity(intent3);
                break;
        }

    }
    public void initListView(View view) {
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter(view));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView itemTv = (TextView) view.findViewById(R.id.tv);
                String text = itemTv.getText().toString();
                if (text.equals("意见反馈")) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), FeedbackActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    getActivity().finish();

                } else if (text.equals("设置")) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SettingAllActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    getActivity().finish();

                } else if (text.equals("客服热线")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.account_center_cargo);
                    builder.setTitle("您将拨打客服热线");
                    builder.setMessage("110110110");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:110110110"));
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                            getActivity().finish();

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            }
        });
    }




    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 3;
        }
        View view;

        public MyAdapter(View v){
            view = v;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(view.getContext(), R.layout.activity_setting_all_listview, null);
            TextView tv = (TextView) v.findViewById(R.id.tv);
            ImageView img1 = (ImageView) v.findViewById(R.id.img1);
            ImageView img2 = (ImageView) v.findViewById(R.id.img2);

            switch (position) {
                case 0:
                    tv.setText("设置");
                    img1.setImageResource(R.drawable.account_list_setup);
                    break;
                case 1:
                    tv.setText("客服热线");
                    img1.setImageResource(R.drawable.account_list_service);
                    break;
                case 2:
                    tv.setText("意见反馈");
                    img1.setImageResource(R.drawable.account_list_idea);
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

