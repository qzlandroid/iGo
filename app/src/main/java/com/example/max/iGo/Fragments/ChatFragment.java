package com.example.max.iGo.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.R;
import com.example.max.iGo.UserDefined.RefreshListView;
import com.example.max.iGo.Utils.DownloadUrl;
import com.example.max.iGo.domain.ChatData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.Random;

public class ChatFragment extends Fragment {

    private TextView titleBarText;
    private ImageView image_back;
    private RefreshListView chatListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        titleBarText = (TextView)view.findViewById(R.id.titlebar_text);
        image_back = (ImageView)view.findViewById(R.id.titlebar_back);
        titleBarText.setText("社区");
        image_back.setVisibility(View.INVISIBLE);
        //初始化标题栏
        initListView(view);
        getDataFromServer();
        return view;

    }
    private MyAdapter myAdapter;

    public void initListView(View view) {
        chatListView = (RefreshListView)view.findViewById(R.id.listView_chat);
        myAdapter = new MyAdapter(view);
        chatListView.setAdapter(myAdapter);
        chatListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatPicUrl.clear();
                chattvTitle.clear();
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(getActivity(), "最后一页了", Toast.LENGTH_SHORT).show();
                    chatListView.onRefreshComplete(false);
                }
            }
        });
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TextView itemTv = (TextView) view.findViewById(R.id.tv);
                // String text = itemTv.getText().toString();
            }
        });
    }
    /**
     *  从服务器获取数据
     */
    String mMoreUrl;
    private void getDataFromServer(){
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, DownloadUrl.chat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("URL..."+result);
                parseData(result,false);
                chatListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                error.printStackTrace();
                chatListView.onRefreshComplete(true);
            }
        });

    }

    /**
     * 加载更多
     */
    private void getMoreDataFromServer(){
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                parseData(result,true);
                chatListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getActivity(), "网络连接异常，请检查网络设置", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                chatListView.onRefreshComplete(false);

            }
        });

    }

    /**
     * 解析数据
     */
    private ArrayList<String> chatPicUrl = new ArrayList<String>();
    private ArrayList<String> chattvTitle = new ArrayList<String>();
    private ChatData mChatData;


    protected void parseData(String result,boolean isMore){
        Gson gson = new Gson();
        mChatData = gson.fromJson(result, ChatData.class);
        int m = mChatData.value.size();
        for(int i = 0; i< m ;i++){
            chatPicUrl.add(DownloadUrl.base_index+mChatData.value.get(i).pic.get(0));
            chattvTitle.add(mChatData.value.get(i).title);
        }
        if(!TextUtils.isEmpty(result)) {
            if(m>0){
                mMoreUrl = DownloadUrl.chat + "?id=" + mChatData.value.get(m-1).id;
            }
            if (m < 5) {
                mMoreUrl = null;
            }
        }
        //if (isMore) {
            myAdapter.notifyDataSetChanged();
        //}

    }



    class MyAdapter extends BaseAdapter {
        private BitmapUtils utils;
        @Override
        public int getCount() {
            return chatPicUrl.size();
        }
        View view;

        public MyAdapter(View v){
            view = v;
            utils = new BitmapUtils(getActivity());
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {
                convertView = View.inflate(view.getContext(), R.layout.fragment_chat_listview, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_card);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.fragment_chat_tv_content);
                holder.tvcommon = (TextView) convertView.findViewById(R.id.fragment_chat_tv_common);
                holder.tvlike = (TextView) convertView.findViewById(R.id.fragment_chat_tv_like);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            System.out.println("HHHHH"+position);
            utils.display(holder.ivPic, chatPicUrl.get(position));
            holder.tvTitle.setText(chattvTitle.get(position));
            Random random = new Random();
            int nextInt = random.nextInt(500);
            holder.tvcommon.setText(nextInt+"");
            nextInt = random.nextInt(200);
            holder.tvlike.setText(nextInt+"");
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
    }
    static class ViewHolder{
        public TextView tvTitle;
        public TextView tvcommon;
        public TextView tvlike;
        public ImageView ivPic;
    }
}