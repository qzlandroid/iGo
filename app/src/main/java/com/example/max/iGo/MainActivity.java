package com.example.max.iGo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.Fragments.AccountFragment;
import com.example.max.iGo.Fragments.CartFragment;
import com.example.max.iGo.Fragments.ChatFragment;
import com.example.max.iGo.Fragments.HomeFragment;
import com.example.max.iGo.Utils.PrefUtils;


/**
 * MainActivity 创建4个Fragment
 * Created by Max .
 */
public class MainActivity extends Activity implements OnClickListener {

    //定义4个Fragment的对象
    private HomeFragment fg1;
    private ChatFragment fg2;
    private CartFragment fg3;
    private AccountFragment fg4;
    //帧布局对象,就是用来存放Fragment的容器
    private FrameLayout flayout;
    //定义底部导航栏的四个布局
    private RelativeLayout home_layout;
    private RelativeLayout chat_layout;
    private RelativeLayout cart_layout;
    private RelativeLayout account_layout;
    //定义底部导航栏中的ImageView与TextView
    private ImageView home_image;
    private ImageView chat_image;
    private ImageView cart_image;
    private ImageView account_image;
    private TextView home_text;
    private TextView chat_text;
    private TextView cart_text;
    private TextView account_text;
    //定义要用的颜色值
    private int whirt = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int blue = 0xFF0AB2FB;
    //定义FragmentManager对象
    FragmentManager fManager;
    private long[] mHits = new long[2];// 数组长度表示要点击的次数

    @Override
    protected void onSaveInstanceState(Bundle outState) {
      //  super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
       // super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fManager = getFragmentManager();
        //  Intent intent=getIntent();
        // account_need_refresh = intent.getBooleanExtra("account_need_refresh", false);
        initViews();

        //刚进入时显示第1个fragment。home
        FragmentTransaction transaction = fManager.beginTransaction();
        clearChioce();
        hideFragments(transaction);
        home_image.setImageResource(R.drawable.index_tarbar_home_on);
        home_text.setTextColor(blue);
        if (fg1 == null) {
            // 如果fg1为空，则创建一个并添加到界面上
            fg1 = new HomeFragment();
            transaction.add(R.id.content, fg1);
        } else {
            // 如果MessageFragment不为空，则直接将它显示出来
            transaction.show(fg1);
        }
        transaction.commit();

//        //接收intent
//        Intent intent = this.getIntent();
//        String fg = intent.getStringExtra("Fragment");
//        int fgnum = Integer.parseInt(fg);
//        backFragment(fgnum);
    }


    //完成组件的初始化
    public void initViews() {
        home_image = (ImageView) findViewById(R.id.home_image);
        chat_image = (ImageView) findViewById(R.id.chat_image);
        cart_image = (ImageView) findViewById(R.id.cart_image);
        account_image = (ImageView) findViewById(R.id.account_image);
        home_text = (TextView) findViewById(R.id.home_text);
        chat_text = (TextView) findViewById(R.id.chat_text);
        cart_text = (TextView) findViewById(R.id.cart_text);
        account_text = (TextView) findViewById(R.id.account_text);
        home_layout = (RelativeLayout) findViewById(R.id.home_layout);
        chat_layout = (RelativeLayout) findViewById(R.id.chat_layout);
        cart_layout = (RelativeLayout) findViewById(R.id.cart_layout);
        account_layout = (RelativeLayout) findViewById(R.id.account_layout);
        home_layout.setOnClickListener(this);
        chat_layout.setOnClickListener(this);
        cart_layout.setOnClickListener(this);
        account_layout.setOnClickListener(this);

    }

    //重写onClick事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_layout:
                setChioceItem(0);
                break;
            case R.id.chat_layout:
                setChioceItem(1);
                break;
            case R.id.cart_layout:
                setChioceItem(2);
                break;
            case R.id.account_layout:
                setChioceItem(3);
                break;
            default:
                break;
        }

    }

    //定义一个选中一个item后的处理
    public void setChioceItem(int home) {
        //重置选项+隐藏所有Fragment
        FragmentTransaction transaction = fManager.beginTransaction();
        clearChioce();
        hideFragments(transaction);
        switch (home) {
            case 0:
                home_image.setImageResource(R.drawable.index_tarbar_home_on);
                home_text.setTextColor(blue);
                if (fg1 == null) {
                    // 如果fg2为空，则创建一个并添加到界面上
                    fg1 = new HomeFragment();
                    transaction.add(R.id.content, fg1);
                } else {
                    // 如果Fragment不为空，则直接将它显示出来
                    transaction.show(fg1);
                }
                break;

            case 1:
                chat_image.setImageResource(R.drawable.index_tarbar_chat_on);
                chat_text.setTextColor(blue);
                if (fg2 == null) {
                    // 如果fg2为空，则创建一个并添加到界面上
                    fg2 = new ChatFragment();
                    transaction.add(R.id.content, fg2);
                } else {
                    // 如果Fragment不为空，则直接将它显示出来
                    transaction.show(fg2);
                }
                break;

            case 2:
                cart_image.setImageResource(R.drawable.index_tarbar_cart_on);
                cart_text.setTextColor(blue);
                if (fg3 == null) {
                    // 如果fg3为空，则创建一个并添加到界面上
                    fg3 = new CartFragment();
                    transaction.add(R.id.content, fg3);
                } else {
                    // 如果Fragment不为空，则直接将它显示出来
                    transaction.show(fg3);
                }
                break;

            case 3:
                account_image.setImageResource(R.drawable.index_tarbar_account_on);
                account_text.setTextColor(blue);
             /*   if (account_need_refresh==true ){
                    fg4 = new AccountFragment();
                    transaction.add(R.id.content, fg4);
                    account_need_refresh=false;
                }else{*/
                if (fg4 == null) {
                    // 如果fg4为空，则创建一个并添加到界面上
                    fg4 = new AccountFragment();
                    transaction.add(R.id.content, fg4);
                } else {
                    // 如果Fragment不为空，则直接将它显示出来
                    //transaction.show(fg4);
                    transaction.remove(fg4);
                    fg4 = new AccountFragment();
                    transaction.add(R.id.content, fg4);

                }
                //  }
                break;
        }
        transaction.commit();
    }

    //隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (fg1 != null) {
            transaction.hide(fg1);
        }
        if (fg2 != null) {
            transaction.hide(fg2);
        }
        if (fg3 != null) {
            transaction.hide(fg3);
        }
        if (fg4 != null) {
            transaction.hide(fg4);
        }
    }


    //定义一个重置所有选项的方法
    public void clearChioce() {
        home_image.setImageResource(R.drawable.index_tabbar_home);
        home_layout.setBackgroundColor(whirt);
        home_text.setTextColor(gray);
        chat_image.setImageResource(R.drawable.index_tarbar_chat);
        chat_layout.setBackgroundColor(whirt);
        chat_text.setTextColor(gray);
        cart_image.setImageResource(R.drawable.index_tabbar_cart);
        cart_layout.setBackgroundColor(whirt);
        cart_text.setTextColor(gray);
        account_image.setImageResource(R.drawable.index_tarbar_account);
        account_layout.setBackgroundColor(whirt);
        account_text.setTextColor(gray);
    }

    //定义一个返回的方法
    public void backFragment(int home) {
        setChioceItem(home);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("应用程序退出");
        PrefUtils.setId(MainActivity.this, -1);
        PrefUtils.setValidation(MainActivity.this, "");
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();

        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
        if (mHits[0] >= (SystemClock.uptimeMillis() - 800)) {
            PrefUtils.setId(MainActivity.this, -1);
            PrefUtils.setValidation(MainActivity.this, "");
            finish();

        }
    }
}

