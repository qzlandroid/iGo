package com.example.max.iGo.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.max.iGo.R;
import com.example.max.iGo.UserDefined.CommodityDetailActivity;
import com.example.max.iGo.UserDefined.HomeMessageActivity;
import com.example.max.iGo.UserDefined.SearchResultActivity;
import com.example.max.iGo.UserDefined.categoryOfOne;
import com.example.max.iGo.Utils.DownloadUrl;
import com.example.max.iGo.domain.IndexData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private ViewFlipper viewFlipper;
    private ScrollView scrollView;
    // 左右滑动时手指按下的X坐标
    private float touchDownX;
    // 左右滑动时手指松开的X坐标
    private float touchUpX;
    //两个页面的小图标。
    private ImageView category_clothImageV, category_foodImageV, category_livingImageV,
            category_digitImageV, category_householsImageV, category_babyImageV, category_booksImageV,
            category_decorationImageV, category_makeupImageV, category_packetImageV, category_sportImageV,
            category_gamesImageV, category_brandImageV, index_guess0, index_guess2, index_guess3, index_guess4,
            index_guess5, index_guess6, index_guess7, index_guess1,index_hot0, index_hot2, index_hot3, index_hot4,
            index_hot5, index_hot6, index_hot7, index_hot1,index_discount0, index_discount2, index_discount3, index_discount4,
            index_discount5, index_discount6, index_discount7, index_discount1;
    private EditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        //设置viewFlipper
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
        ImageView msg_image = (ImageView) view.findViewById(R.id.msg_image);
        final EditText search_edit = (EditText) view.findViewById(R.id.search_edit);
        search_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edit.setCursorVisible(true);
            }
        });
        msg_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeMessageActivity.class);
                startActivity(intent);
            }
        });
        //get scrollview
        scrollView = (ScrollView) view.findViewById(R.id.ScrollView);
        initfind(view);
        initListener();
        getDataFromServer();
        View parentView = (View) viewFlipper.getParent();

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });


        //设置触摸监听
        viewFlipper.setOnTouchListener(new ViewFlipper.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.getParent().requestDisallowInterceptTouchEvent(true);


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchDownX = event.getX();// 取得左右滑动时手指按下的X坐标

                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    touchUpX = event.getX();// 取得左右滑动时手指松开的X坐标
                    // 从左往右，看前一个View
                    if (touchDownX - touchUpX > 50) {
                        // 设置View切换的动画
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(view.getContext(),
                                R.anim.slide_right_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(view.getContext(),
                                R.anim.slide_left_out));
                        // 显示前一个View
                        viewFlipper.showNext();
                        //交换底部显示器的图标

                    }
                    // 从右往左，看后一个View
                    else if (touchUpX - touchDownX > 10) {
                        // 设置View切换的动画
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(view.getContext(),
                                R.anim.slide_left_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(view.getContext(),
                                R.anim.slide_right_out));

                        // 显示下一个View
                        viewFlipper.showPrevious();
                        //交换底部显示器的图标


                    }
                    return true;
                }
                return false;
            }

        });//new viewFlipper

        return view;
    }//creat view

    public void initfind(View view) {
        category_clothImageV = (ImageView) view.findViewById(R.id.category_clothImageV);
        category_foodImageV = (ImageView) view.findViewById(R.id.category_foodImageV);
        category_livingImageV = (ImageView) view.findViewById(R.id.category_livingImageV);
        category_digitImageV = (ImageView) view.findViewById(R.id.category_digitImageV);
        category_householsImageV = (ImageView) view.findViewById(R.id.category_householsImageV);
        category_babyImageV = (ImageView) view.findViewById(R.id.category_babyImageV);
        category_booksImageV = (ImageView) view.findViewById(R.id.category_booksImageV);
        category_decorationImageV = (ImageView) view.findViewById(R.id.category_decorationImageV);
        category_makeupImageV = (ImageView) view.findViewById(R.id.category_makeupImageV);
        category_packetImageV = (ImageView) view.findViewById(R.id.category_packetImageV);
        category_sportImageV = (ImageView) view.findViewById(R.id.category_sportImageV);
        category_gamesImageV = (ImageView) view.findViewById(R.id.category_gamesImageV);
        category_brandImageV = (ImageView) view.findViewById(R.id.category_brandImageV);
        index_guess0 = (ImageView) view.findViewById(R.id.index_guess_pic1);
        index_guess1 = (ImageView) view.findViewById(R.id.index_guess_pic2);
        index_guess2 = (ImageView) view.findViewById(R.id.index_guess_pic3);
        index_guess3 = (ImageView) view.findViewById(R.id.index_guess_pic4);
        index_guess4 = (ImageView) view.findViewById(R.id.index_guess_pic5);
        index_guess5 = (ImageView) view.findViewById(R.id.index_guess_pic6);
        index_guess6 = (ImageView) view.findViewById(R.id.index_guess_pic7);
        index_guess7 = (ImageView) view.findViewById(R.id.index_guess_pic8);
        index_hot0 = (ImageView) view.findViewById(R.id.index_hot_pic1);
        index_hot1 = (ImageView) view.findViewById(R.id.index_hot_pic2);
        index_hot2 = (ImageView) view.findViewById(R.id.index_hot_pic3);
        index_hot3 = (ImageView) view.findViewById(R.id.index_hot_pic4);
        index_hot4 = (ImageView) view.findViewById(R.id.index_hot_pic5);
        index_hot5 = (ImageView) view.findViewById(R.id.index_hot_pic6);
        index_hot6 = (ImageView) view.findViewById(R.id.index_hot_pic7);
        index_hot7 = (ImageView) view.findViewById(R.id.index_hot_pic8);
        index_discount0 = (ImageView) view.findViewById(R.id.index_discount_pic1);
        index_discount1 = (ImageView) view.findViewById(R.id.index_discount_pic2);
        index_discount2 = (ImageView) view.findViewById(R.id.index_discount_pic3);
        index_discount3 = (ImageView) view.findViewById(R.id.index_discount_pic4);
        index_discount4 = (ImageView) view.findViewById(R.id.index_discount_pic5);
        index_discount5 = (ImageView) view.findViewById(R.id.index_discount_pic6);
        index_discount6 = (ImageView) view.findViewById(R.id.index_discount_pic7);
        index_discount7 = (ImageView) view.findViewById(R.id.index_discount_pic8);
        et_search = (EditText)view.findViewById(R.id.search_edit);
        index_guess.add(index_guess0);
        index_guess.add(index_guess1);
        index_guess.add(index_guess2);
        index_guess.add(index_guess3);
        index_guess.add(index_guess4);
        index_guess.add(index_guess5);
        index_guess.add(index_guess6);
        index_guess.add(index_guess7);

        index_hot.add(index_hot0);
        index_hot.add(index_hot1);
        index_hot.add(index_hot2);
        index_hot.add(index_hot3);
        index_hot.add(index_hot4);
        index_hot.add(index_hot5);
        index_hot.add(index_hot6);
        index_hot.add(index_hot7);

        index_discount.add(index_discount0);
        index_discount.add(index_discount1);
        index_discount.add(index_discount2);
        index_discount.add(index_discount3);
        index_discount.add(index_discount4);
        index_discount.add(index_discount5);
        index_discount.add(index_discount6);
        index_discount.add(index_discount7);




    }

    private void initListener() {
        category_clothImageV.setOnClickListener(this);
        category_foodImageV.setOnClickListener(this);
        category_livingImageV.setOnClickListener(this);
        category_digitImageV.setOnClickListener(this);
        category_householsImageV.setOnClickListener(this);
        category_babyImageV.setOnClickListener(this);
        category_booksImageV.setOnClickListener(this);
        category_decorationImageV.setOnClickListener(this);
        category_makeupImageV.setOnClickListener(this);
        category_packetImageV.setOnClickListener(this);
        category_sportImageV.setOnClickListener(this);
        category_gamesImageV.setOnClickListener(this);
        category_brandImageV.setOnClickListener(this);
        index_guess0.setOnClickListener(this);
        index_guess1.setOnClickListener(this);
        index_guess2.setOnClickListener(this);
        index_guess3.setOnClickListener(this);
        index_guess4.setOnClickListener(this);
        index_guess5.setOnClickListener(this);
        index_guess6.setOnClickListener(this);
        index_guess7.setOnClickListener(this);
        index_hot0.setOnClickListener(this);
        index_hot1.setOnClickListener(this);
        index_hot2.setOnClickListener(this);
        index_hot3.setOnClickListener(this);
        index_hot4.setOnClickListener(this);
        index_hot5.setOnClickListener(this);
        index_hot6.setOnClickListener(this);
        index_hot7.setOnClickListener(this);
        index_discount0.setOnClickListener(this);
        index_discount1.setOnClickListener(this);
        index_discount2.setOnClickListener(this);
        index_discount3.setOnClickListener(this);
        index_discount4.setOnClickListener(this);
        index_discount5.setOnClickListener(this);
        index_discount6.setOnClickListener(this);
        index_discount7.setOnClickListener(this);

        et_search.setOnKeyListener(onKeyListener);
    }

    /**
     * 输入法按下搜索的监听
     */
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager)et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isActive()){
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }

                //Toast.makeText(getActivity(),"success",Toast.LENGTH_SHORT).show();
                MyIntent3();

                return true;
            }
            return false;
        }
    };
    private void MyIntent1(int num) {
        Intent intent = new Intent(getActivity(), categoryOfOne.class);
        intent.putExtra("type", num);
        startActivity(intent);
    }
    private void MyIntent2(String id){
        Intent intent = new Intent(getActivity(), CommodityDetailActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    /**
     * 点击搜索，进入商品搜索详情界面
     */
    private void MyIntent3(){
        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("key", 1);
        bundle.putString("search_name", et_search.getText().toString());
        intent.putExtra("search",bundle);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_clothImageV:
                MyIntent1(0);
                break;
            case R.id.category_foodImageV:
                MyIntent1(1);
                break;
            case R.id.category_livingImageV:
                MyIntent1(2);
                break;
            case R.id.category_digitImageV:
                MyIntent1(3);
                break;
            case R.id.category_householsImageV:
                MyIntent1(4);
                break;
            case R.id.category_babyImageV:
                MyIntent1(5);
                break;
            case R.id.category_booksImageV:
                MyIntent1(6);
                break;
            case R.id.category_decorationImageV:
                MyIntent1(7);
                break;
            case R.id.category_makeupImageV:
                MyIntent1(8);
                break;
            case R.id.category_packetImageV:
                MyIntent1(9);
                break;
            case R.id.category_sportImageV:
                MyIntent1(10);
                break;
            case R.id.category_gamesImageV:
                MyIntent1(11);
                break;
            case R.id.category_brandImageV:
                MyIntent1(12);
                break;
            case R.id.index_guess_pic1:
                MyIntent2(guessId[0]);
                break;
            case R.id.index_guess_pic2:
                MyIntent2(guessId[1]);
                break;
            case R.id.index_guess_pic3:
                MyIntent2(guessId[2]);
                break;
            case R.id.index_guess_pic4:
                MyIntent2(guessId[3]);
                break;
            case R.id.index_guess_pic5:
                MyIntent2(guessId[4]);
                break;
            case R.id.index_guess_pic6:
                MyIntent2(guessId[5]);
                break;
            case R.id.index_guess_pic7:
                MyIntent2(guessId[6]);
                break;
            case R.id.index_guess_pic8:
                MyIntent2(guessId[7]);
                break;

            case R.id.index_hot_pic1:
                MyIntent2(hotId[0]);
                break;
            case R.id.index_hot_pic2:
                MyIntent2(hotId[1]);
                break;
            case R.id.index_hot_pic3:
                MyIntent2(hotId[2]);
                break;
            case R.id.index_hot_pic4:
                MyIntent2(hotId[3]);
                break;
            case R.id.index_hot_pic5:
                MyIntent2(hotId[4]);
                break;
            case R.id.index_hot_pic6:
                MyIntent2(hotId[5]);
                break;
            case R.id.index_hot_pic7:
                MyIntent2(hotId[6]);
                break;
            case R.id.index_hot_pic8:
                MyIntent2(hotId[7]);
                break;

            case R.id.index_discount_pic1:
                MyIntent2(discountId[0]);
                break;
            case R.id.index_discount_pic2:
                MyIntent2(discountId[1]);
                break;
            case R.id.index_discount_pic3:
                MyIntent2(discountId[2]);
                break;
            case R.id.index_discount_pic4:
                MyIntent2(discountId[3]);
                break;
            case R.id.index_discount_pic5:
                MyIntent2(discountId[4]);
                break;
            case R.id.index_discount_pic6:
                MyIntent2(discountId[5]);
                break;
            case R.id.index_discount_pic7:
                MyIntent2(discountId[6]);
                break;
            case R.id.index_discount_pic8:
                MyIntent2(discountId[7]);
                break;
        }
    }




    private ArrayList<ImageView> index_guess = new ArrayList<ImageView>();
    private ArrayList<ImageView> index_hot = new ArrayList<ImageView>();
    private ArrayList<ImageView> index_discount = new ArrayList<ImageView>();


    /**
     *  从服务器获取数据
     */
    private void getDataFromServer(){
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, DownloadUrl.index, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        });

    }
    /**
     * 解析数据
     */
    //private ArrayList<String> hotUrl = new ArrayList<String>(20);
    private String[] hotUrl = new String[20];
    private String[] hotId = new String[20];
    //private ArrayList<String> guessUrl = new ArrayList<String>(20);
    private String[] guessUrl = new String[20];
    //private ArrayList<String> guessId = new ArrayList<String>();
    private String[] guessId = new String[20];
    //private ArrayList<String> discountUrl = new ArrayList<String>(20);
    private String[] discountUrl = new String[20];
    private String[] discountId = new String[20];
    //private ArrayList<String> discountId = new ArrayList<String>();
    private IndexData mIndexData;
    protected void parseData(String result){
        Gson gson = new Gson();
        mIndexData = gson.fromJson(result, IndexData.class);
        for (int i = 0;i<8;i++){
            //discountUrl.add(DownloadUrl.base_index + mIndexData.value.get(1).data.get(i).url);

            int num = mIndexData.value.get(1).data.get(i).position;
            int pos = num%10 + (num/10-1)*4;
            discountUrl[pos-1]=DownloadUrl.base_index + mIndexData.value.get(1).data.get(i).url;
            discountId[pos-1]=mIndexData.value.get(1).data.get(i).pid;



            System.out.println("HAHAHAHA" + mIndexData.value.get(1).data.get(i).id);
        }
        for (int i = 0;i<8;i++){


            int num = mIndexData.value.get(3).data.get(i).position;
            int pos = num%10 + (num/10-1)*4;
            System.out.println("POSPOS"+pos+"..."+num);
            hotUrl[pos-1]=DownloadUrl.base_index + mIndexData.value.get(3).data.get(i).url;
            hotId[pos-1]=mIndexData.value.get(3).data.get(i).pid;
        }
        for (int i = 0;i<8;i++){


            int num = mIndexData.value.get(2).data.get(i).position;
            int pos = num%10 + (num/10-1)*4;
            guessUrl[pos-1]=DownloadUrl.base_index + mIndexData.value.get(2).data.get(i).url;
            guessId[pos-1]=mIndexData.value.get(2).data.get(i).pid;
        }
        DownloadImage();
    }


    private BitmapUtils utils;
    private void DownloadImage(){
        utils = new BitmapUtils(getActivity());
        for(int i=0;i<8;i++){
            utils.display(index_discount.get(i),discountUrl[i]);
        }
        for(int i=0;i<8;i++){
            utils.display(index_hot.get(i),hotUrl[i]);
        }
        for(int i=0;i<8;i++){
            utils.display(index_guess.get(i),guessUrl[i]);
        }
    }

}

