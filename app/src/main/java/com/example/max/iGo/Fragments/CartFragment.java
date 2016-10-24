package com.example.max.iGo.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.ConfirmedOrderActivity;
import com.example.max.iGo.R;
import com.example.max.iGo.UserDefined.RefreshListView;

public class CartFragment extends Fragment {

    private TextView titleBarText;
    private ImageView image_back;
    private ListView cartListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);

        titleBarText = (TextView)view.findViewById(R.id.titlebar_text);
        image_back = (ImageView)view.findViewById(R.id.titlebar_back);
        Button pay = (Button)view.findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfirmedOrderActivity.class);
                startActivity(intent);

            }
        });
        titleBarText.setText("购物车");
        image_back.setVisibility(View.INVISIBLE);

        //初始化标题栏
        initListView(view);
        return view;
    }

    String mMoreUrl;
    public void initListView(View view) {
        cartListView = (ListView) view.findViewById(R.id.listView_cart);
        cartListView.setAdapter(new MyAdapter(view));

        cartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TextView itemTv = (TextView) view.findViewById(R.id.tv);
                // String text = itemTv.getText().toString();
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
            View v = View.inflate(view.getContext(), R.layout.fragment_cart_listview, null);
            ImageView img = (ImageView) v.findViewById(R.id.imageView_cart_lv);
            switch (position) {
                case 0:
                    img.setImageResource(R.drawable.search_result_listview_list_pic1);
                    break;
                case 1:
                    img.setImageResource(R.drawable.search_result_listview_list_pic2);
                    break;
                case 2:
                    img.setImageResource(R.drawable.search_result_listview_list_pic3);
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