package com.example.max.iGo.UserDefined;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.R;
import com.example.max.iGo.Utils.DownloadUrl;
import com.example.max.iGo.domain.SearchResultData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView search_result_backtop,search_result_sortby,search_result_back;
    private ListView search_result_listView,show_sort_listView;
    private PopupWindow popupWindow;
    private RelativeLayout relative_top;
    private EditText search_result_search_et;
    private String name;
    private String intname;
    private ProgressBar pb_progress;
    private MySearchAdapter mySearchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }
    private void initView(){
        setContentView(R.layout.search_result);
        relative_top = (RelativeLayout) findViewById(R.id.relative_top);
        search_result_back =(ImageView) findViewById(R.id.search_result_back);
        search_result_backtop = (ImageView) findViewById(R.id.search_result_backtop);
        search_result_sortby = (ImageView)findViewById(R.id.search_result_sort);
        search_result_search_et = (EditText) findViewById(R.id.search_result_search_et);
        pb_progress = (ProgressBar)findViewById(R.id.pb_progress);
        search_result_listView = (ListView) findViewById(R.id.search_result_listview);
        show_sort_listView = new ListView(this);
    }
    private void initData(){
        search_result_backtop.setOnClickListener(this);
        search_result_sortby.setOnClickListener(this);
        search_result_back.setOnClickListener(this);
        search_result_search_et.setOnClickListener(this);
        search_result_search_et.setOnKeyListener(onKeyListener);
        search_result_listView.setVerticalScrollBarEnabled(false);
        mySearchAdapter = new MySearchAdapter();
        getPreData();
        search_result_listView.setAdapter(mySearchAdapter);
        search_result_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchResultActivity.this, CommodityDetailActivity.class);
                System.out.println("HHHHHH"+position);
                intent.putExtra("id", search_id.get(position));
                System.out.println("HHHHHH" + search_id.get(position));
                startActivity(intent);
            }
        });
        show_sort_listView.setAdapter(new MySortAdapter());
        //设置ListView条目点击监听
        show_sort_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();

            }
        });
    }
    private void getPreData(){
        Intent intent = getIntent();
        //intname = intent.getStringExtra("search");
        Bundle  bundle = intent.getBundleExtra("search");
        int key = bundle.getInt("key");
        if (key==1){
            String search_name = bundle.getString("search_name");
            search_result_search_et.setText(search_name);
            search_detail_url = DownloadUrl.search_url + search_name;
        }else if (key==2){
            String category_name = bundle.getString("category_name");
            search_detail_url = DownloadUrl.search_category_url + category_name;
        }
        if (search_detail_url!=null){
            getDataFromServer();
        }
    }
    /**
     * 输入法按下搜索的监听
     */
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) search_result_search_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                search_name.clear();
                search_id.clear();
                search_price.clear();
                search_picUrl.clear();
                search_number.clear();
                //pb_progress.setVisibility(View.VISIBLE);
                name = search_result_search_et.getText().toString();
                System.out.println("DATADATA:"+name);
                search_detail_url = DownloadUrl.search_url + name;
                if (search_detail_url!=null){
                    getDataFromServer();
                }
                return true;
            }
            return false;
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_result_backtop:
                search_result_listView.setSelection(0);
                buttondisappear();
                valuec = 1;
                valuea = 0;
                valueb = 1;
                break;
            case R.id.search_result_sort:
                 showSort();
                 break;
            case R.id.search_result_search_et:
                search_result_search_et.setCursorVisible(true);
                break;
            case R.id.search_result_back:
                finish();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }

    }

    private void showSort(){
        if(popupWindow==null){
            /**
             * array1:popupWindow要显示的内容
             * array2:popupWindow的宽度
             * array3:popupWindow的高度
             */
            popupWindow = new PopupWindow(show_sort_listView, relative_top.getWidth(),3*search_result_backtop.getHeight());
        }
            //要让其中的View获取焦点，必须设置为true
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(false);
            popupWindow.showAsDropDown(relative_top, 0, 0);


    }
    /**
     *  从服务器获取数据
     */
    private String search_detail_url;
    private void getDataFromServer(){
        pb_progress.setVisibility(View.VISIBLE);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, search_detail_url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("DATADATA:"+result);
                parseData(result);
                pb_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getApplication(), "网络连接异常，请检查网络设置", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                pb_progress.setVisibility(View.INVISIBLE);
            }
        });

    }
    /**
     * 解析数据
     */
    private ArrayList<String> search_name = new ArrayList<String>();
    private ArrayList<String> search_price = new ArrayList<String>();
    private ArrayList<String> search_number = new ArrayList<String>();
    private ArrayList<String> search_id = new ArrayList<String>();
    private ArrayList<String> search_picUrl = new ArrayList<String>();
    private SearchResultData mSearchData;
    protected void parseData(String result) {
        search_name.clear();
        search_price.clear();
        search_number.clear();
        search_picUrl.clear();
        Gson gson = new Gson();
        mSearchData = gson.fromJson(result, SearchResultData.class);
        if (mSearchData.value.size()==0){
            Toast.makeText(getApplication(), "无此类商品", Toast.LENGTH_LONG).show();
        }else {
            for (int i=0; i< mSearchData.value.size();i++){
                search_name.add(mSearchData.value.get(i).name);
                search_price.add(mSearchData.value.get(i).price);
                search_number.add(mSearchData.value.get(i).sale);
                search_id.add(mSearchData.value.get(i).id);
                search_picUrl.add(DownloadUrl.base_index+mSearchData.value.get(i).pic.get(0));
            }
        }
        mySearchAdapter.notifyDataSetChanged();
    }
    /**
     * 设置箭头的显示与消失
     */
    int valuea = 0;
    int valueb = 0;
    int valuec = 0;
    private void ShowAndMissArr(int position){
        if (position > 10 && valuea==0){
            buttonappear();
            valuea = 1;
            valueb = 1;
            valuec = 0;

        }
        if(position < 10 && valueb==1 && valuec==0){
            buttondisappear();
            valueb = 0;
            valuea = 0;
            valuec = 0;
        }
    }
    private void buttonappear(){
        ObjectAnimator oa = ObjectAnimator.ofFloat(search_result_backtop, "translationY", 0, -150) ;
        oa.setDuration(1000);
        oa.start();
    }
    private void buttondisappear(){
        ObjectAnimator oa = ObjectAnimator.ofFloat(search_result_backtop, "translationY", -150, 0) ;
        oa.setDuration(1000);
        oa.start();
    }


    class MySortAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
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
            View v = View.inflate(SearchResultActivity.this, R.layout.search_result_showsort,null);
            return v;
        }
    }



    class MySearchAdapter extends BaseAdapter{
        private BitmapUtils utils;
        public MySearchAdapter(){
            utils = new BitmapUtils(getApplicationContext());
        }
        public int getCount() {
            return search_name.size();
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.search_result_listview_list, null);
            TextView list_name = (TextView) view.findViewById(R.id.search_result_listview_name);
            TextView list_price = (TextView) view.findViewById(R.id.search_result_listview_list_price);
            TextView list_number = (TextView) view.findViewById(R.id.search_result_listview_number);
            ImageView list_pic = (ImageView) view.findViewById(R.id.search_result_listview_list_pic);
            list_name.setText(search_name.get(position));
            list_number.setText(search_number.get(position));
            list_price.setText(search_price.get(position));
            utils.display(list_pic,search_picUrl.get(position));
            ShowAndMissArr(position);
            return view;
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

}

