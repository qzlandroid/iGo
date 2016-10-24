package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.Toast;

import com.example.max.iGo.R;

public class categoryOfOne extends AppCompatActivity {


    List<Item_goods> goods_pic_text;
    ImageView iv;
    ApplicationInfo appInfo;
    private TextView titleBarText;
    private ImageView image_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_clothing);
        titleBarText = (TextView)findViewById(R.id.titlebar_text);
        image_back = (ImageView)findViewById(R.id.titlebar_back);
        setTitleBarText();
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        String[] textname=new String[]{
                "服装","美食","居家","数码","家电","母婴","图书","家装","美装","箱包","运动","游戏","品牌"
        };
        String[] xmlname=new String[]{
                "clothing","food","jujia","digit","electrical","muyin",
                "books","jiazhuang","meizhuang","xiangbao","sports","games","pingpai"
        };
        Intent intent=getIntent();
        int type=intent.getIntExtra("type", -1);
        appInfo = getApplicationInfo();
        int xmlID=getResources().getIdentifier(xmlname[type],"raw",appInfo.packageName);
        InputStream is=getResources().openRawResource(xmlID);
        parseXML(is);
        GridView grid_item_goods=(GridView)findViewById(R.id.grid_item_goods);
        grid_item_goods.setAdapter(new GridAdapter());
        grid_item_goods.setOnItemClickListener(new MyItemListenner());


        System.out.println("-------"+type+"------------------------------");

    }
    private void setTitleBarText(){
        Intent intent = getIntent();
        int num = intent.getIntExtra("type",0);
        if(num==0)titleBarText.setText("服装");
        if(num==1)titleBarText.setText("美食");
        if(num==2)titleBarText.setText("居家");
        if(num==3)titleBarText.setText("数码");
        if(num==4)titleBarText.setText("家电");
        if(num==5)titleBarText.setText("母婴");
        if(num==6)titleBarText.setText("图书");
        if(num==7)titleBarText.setText("家装");
        if(num==8)titleBarText.setText("美妆");
        if(num==9)titleBarText.setText("箱包");
        if(num==10)titleBarText.setText("运动");
        if(num==11)titleBarText.setText("游戏");
        if(num==12)titleBarText.setText("品牌");

    }
    class MyItemListenner implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyIntent(goods_pic_text.get(position).getText());
        }
    }
    public void MyIntent(String name){
        Intent intent=new Intent(categoryOfOne.this,SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("key", 2);
        bundle.putString("category_name", name);
        intent.putExtra("search",bundle);
        startActivity(intent);
    }
    class GridAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return goods_pic_text.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        class ViewHolder{
            //条目的布局文件中有什么组件，这里就定义什么属性
            TextView cloth_type;
            ImageView siv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item_goods item=goods_pic_text.get(position);
            View view=null;
            ViewHolder myholder ;
            if(convertView==null){
                view=View.inflate(categoryOfOne.this, R.layout.category_goods_pic_item, null);
                myholder=new ViewHolder();
                myholder.cloth_type=(TextView)view.findViewById(R.id.tv_belowpic);
                myholder.siv=(ImageView)view.findViewById(R.id.pic_item);
                view.setTag(myholder);
            }
            else {
                view=convertView;
                myholder=(ViewHolder)view.getTag();
            }
          //   iv=(ImageView)view.findViewById(R.id.pic_item);
            myholder.cloth_type.setText(item.getText());
            int resID = getResources().getIdentifier(item.getPicname(), "drawable", appInfo.packageName);
            //iv.setImageResource(resID);
            myholder.siv.setImageResource(resID);
            //setImage(item.getPath());
            //   iv.setImageUrl(item.getPath());
            // iv.setImageResource(pic_item[position]);
            //iv.setImageDrawable();



            return view;
        }
    }



    private void parseXML(InputStream is)
    {
        XmlPullParser  xpp= Xml.newPullParser();
        try {
            xpp.setInput(is,"utf-8");
            int type=xpp.getEventType();
            Item_goods item=null;
            while(type != XmlPullParser.END_DOCUMENT){
                switch (type){
                    case XmlPullParser.START_TAG:
                        if("goods".equals(xpp.getName()))
                        {
                            goods_pic_text=new ArrayList<Item_goods>();
                        }
                        else if("item".equals(xpp.getName()))
                        {
                            item=new Item_goods();
                        }
                        else if("text".equals(xpp.getName()))
                        {
                            String text=xpp.nextText();
                            System.out.println(text);
                            item.setText(text);
                        }
                        else if("picname".equals(xpp.getName()))
                        {
                            String text=xpp.nextText();
                            System.out.println(text);
                            item.setPicname(text);
                        }
                        else if("path".equals(xpp.getName()))
                        {

                            String text=xpp.nextText();
                            System.out.println(text);
                            item.setPath(text);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("item".equals(xpp.getName()))
                        {
                            goods_pic_text.add(item);
                        }

                        break;
                }
                type=xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    }

