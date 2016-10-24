package com.example.max.iGo.UserDefined;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.R;
import com.example.max.iGo.Utils.DownloadUrl;
import com.example.max.iGo.Utils.MyListView;
import com.example.max.iGo.domain.SearchResultDetailData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by QZ on 2016/4/12.
 */
public class CommodityDetailActivity extends Activity implements View.OnClickListener{
    private ImageView arr_back,common_arr,arr_up,goods_share,commodity_collection;
    private ViewPager mViewPager;
    private TextView tvname,tvcomnum,tvprice,tvsale;
    private MyListView mListView;
    private ProgressBar pb_progress;
    private String URL;
    private ScrollView sl;
    private ImageView commodity_detail_cloth;
    private LinearLayout linear;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
        initListener();
    }
    private void initViews() {
        setContentView(R.layout.activity_commodity_detail);
        arr_back = (ImageView) findViewById(R.id.commodity_detail_back);
        goods_share = (ImageView) findViewById(R.id.goods_share);
        common_arr = (ImageView) findViewById(R.id.commodity_common_arr);
        arr_up = (ImageView) findViewById(R.id.commodity_detail_up);
        mViewPager = (ViewPager)findViewById(R.id.commodity_detail_vp);
        tvname = (TextView)findViewById(R.id.commodity_tvname);
        tvprice = (TextView)findViewById(R.id.commodity_tvprice);
        tvsale = (TextView)findViewById(R.id.commodity_tvsale);
        tvcomnum = (TextView)findViewById(R.id.commodity_common_num);
        mListView = (MyListView)findViewById(R.id.commodity_lv);
        pb_progress = (ProgressBar)findViewById(R.id.commodity_pb_progress);
        sl = (ScrollView) findViewById(R.id.commodity_scrollView);
        commodity_detail_cloth =(ImageView) findViewById(R.id.commodity_detail_cloth);
        commodity_collection =(ImageView) findViewById(R.id.commodity_collection);
        linear = (LinearLayout)findViewById(R.id.commodity_linear);
    }
    private void initData() {
        pb_progress.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        System.out.println("HHHHHH" + id);
        URL = DownloadUrl.search_detail_url+id;
        getDataFromServer();

    }



    private void initListener() {
        /*mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/
        sl.setOnTouchListener(new TouchListenerImpl());
        arr_back.setOnClickListener(this);
        arr_up.setOnClickListener(this);
        goods_share.setOnClickListener(this);
        commodity_collection.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < linear.getChildCount(); i++){
                    linear.getChildAt(i).setEnabled(i==position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 从网络获取数据
     */
    private void getDataFromServer(){
        pb_progress.setVisibility(View.VISIBLE);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("RESULT"+result);
                if (!result.equals("[]")){
                    parseData(result);
                }else {
                    Toast.makeText(CommodityDetailActivity.this,"商品已下架",Toast.LENGTH_SHORT).show();
                }

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
    private SearchResultDetailData mSearchDetailData;
    private ArrayList<String> vpUrl = new ArrayList<>();
    private ArrayList<String> lvUrl = new ArrayList<>();
    protected void parseData(String result) {
        Gson gson = new Gson();
        mSearchDetailData = gson.fromJson(result, SearchResultDetailData.class);
        for (int i=0;i< mSearchDetailData.pic.length;i++){
            vpUrl.add(DownloadUrl.base_index+mSearchDetailData.pic[i]);
        }
        for (int i=0;i< mSearchDetailData.description.length;i++){
            lvUrl.add(DownloadUrl.base_index+mSearchDetailData.description[i]);
        }
        sl.smoothScrollTo(0, 0);
        fillData();
        initDots();
        System.out.println("HHHHHH"+mSearchDetailData.name);
    }
    /**
     * 初始化dot
     */
    private void initDots(){
        for (int i = 0; i < vpUrl.size(); i++){
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16,16);
            if(i!=0){
                params.leftMargin = 10;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dots);
            if (i==0){
                view.setEnabled(true);
            }else {
                view.setEnabled(false);
            }

            linear.addView(view);
        }
    }
    /**
     * 填充数据
     */
    private void fillData() {
        tvname.setText(mSearchDetailData.name);
        tvprice.setText(mSearchDetailData.price);
        tvsale.setText(mSearchDetailData.sale);
        mListView.setAdapter(new MyLvAdapter());
        mViewPager.setAdapter(new MyVpAdapter());
        //tvcomnum.setText(mSearchDetailData.comment.length);
    }
    private int i=0;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commodity_detail_up:
                /*scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });*/
                sl.smoothScrollTo(0, 0);
                buttondisappear();
                valuec = 1;
                valuea = 0;
                valueb = 1;
                break;
            case R.id.commodity_detail_back:
                finish();
                break;
            case R.id.goods_share:
                showShare();
                break;
            case R.id.commodity_collection:
                //commodity_collection.setImageResource(R.drawable.product_);
                if (i==0){
                    Toast.makeText(this,"已添加至收藏",Toast.LENGTH_SHORT).show();
                    commodity_collection.setImageResource(R.drawable.product_favorite_on);
                    i++;
                }else {
                    Toast.makeText(this,"已取消收藏",Toast.LENGTH_SHORT).show();
                    commodity_collection.setImageResource(R.drawable.product_favorite_off);
                    i--;
                }

                break;
        }

    }
    /**
     * 计算箭头向上移动的高度
     */
    private int moveHeight;
    private void caculateHeight(){
        int[] location = new int[2];
        commodity_detail_cloth.getLocationOnScreen(location);
        int clothY = location[1];
        System.out.println("y的距离:" + clothY);
        arr_up.getLocationOnScreen(location);
        int upY = location[1];
        System.out.println("y的距离:" + upY);
        moveHeight = upY-clothY;
    }
    /**
     * 按钮移动事件
     */
    private int valuea = 0;
    private int valueb = 0;
    private int valuec = 0;
    private class TouchListenerImpl implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY = view.getScrollY();
                    if (scrollY > 500 && valuea==0) {
                        buttonappear();
                        valuea = 1;
                        valueb = 1;
                        valuec = 0;
                    }
                    if(scrollY < 500 && valueb==1 && valuec==0){
                        buttondisappear();
                        valueb = 0;
                        valuea = 0;
                        valuec = 0;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }

    }
    private void buttonappear(){
        caculateHeight();
        ObjectAnimator oa = ObjectAnimator.ofFloat(arr_up, "translationY", 0, -moveHeight) ;
        oa.setDuration(1000);
        oa.start();
    }
    private void buttondisappear(){
        ObjectAnimator oa = ObjectAnimator.ofFloat(arr_up, "translationY", -moveHeight, 0) ;
        oa.setDuration(1000);
        oa.start();
    }


    /**
     * listview的填充器
     */
    class MyLvAdapter extends BaseAdapter{
        private BitmapUtils utils;
        public MyLvAdapter(){
            utils = new BitmapUtils(getApplicationContext());
        }
        @Override
        public int getCount() {
            return lvUrl.size();
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
            View view = View.inflate(CommodityDetailActivity.this, R.layout.activity_commodity_detail_lvitem,null);
            ImageView iv = (ImageView) view.findViewById(R.id.commoditydetail_listV_ImageV);
            WindowManager wm = CommodityDetailActivity.this.getWindowManager();
            width = wm.getDefaultDisplay().getWidth();
            LinearLayout.LayoutParams lp;
            lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
            lp.width = width;
            lp.height = width;
            iv.setLayoutParams(lp);
            utils.display(iv,lvUrl.get(position));
            return view;
        }
    }

    /**
     * ViewPager适配器
     */
    class MyVpAdapter extends PagerAdapter {
        private BitmapUtils utils;
        public MyVpAdapter(){
            utils = new BitmapUtils(getApplicationContext());
        }
        @Override
        public int getCount() {
            return vpUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(CommodityDetailActivity.this);

            utils.display(imageView, vpUrl.get(position));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

    // 启动分享GUI
        oks.show(this);
    }

}
