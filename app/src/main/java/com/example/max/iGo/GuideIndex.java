package com.example.max.iGo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static com.example.max.iGo.R.drawable.guide_shape;

public class GuideIndex extends AppCompatActivity {

    private LinearLayout ll_shape;
    private Button explore_btn;
    private View red_oval;
    private int distance;
    private static final int[] GuideImgIds={R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    ArrayList<ImageView> img_guide=new ArrayList<ImageView>();
    private ViewPager vp_guide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide_index);
        vp_guide=(ViewPager)findViewById(R.id.vp_guide);
        ll_shape=(LinearLayout)findViewById(R.id.ll_shape);
        explore_btn=(Button)findViewById(R.id.explore_btn);
        red_oval=(View)findViewById(R.id.red_oval);
       SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
       if(sp.getBoolean("is_guided",false)==true)
       {
           startActivity(new Intent(GuideIndex.this,MainActivity.class));
           finish();
        }
        initdata();
        vp_guide.setAdapter(new MypagerAdapter());
        vp_guide.addOnPageChangeListener(new MypageChanListener());
    }
    class MypageChanListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int move_length_ovalred= (int) (positionOffset * distance +position * distance);
           RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) red_oval.getLayoutParams();
            params.leftMargin=move_length_ovalred;
            params.width=25;
            params.height=25;
            System.out.println("+++++++"+move_length_ovalred+"百分比"+positionOffset+"weizhi"+position);
            red_oval.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
        if(position==GuideImgIds.length-1){
            explore_btn.setVisibility(View.VISIBLE);
        }
            else {
            explore_btn.setVisibility(View.INVISIBLE);
        }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void initdata(){

        for(int i=0;i<GuideImgIds.length;i++){
            ImageView img=new ImageView(this);
            img.setBackgroundResource(GuideImgIds[i]);
            img_guide.add(img);


        }
        for(int j=0;j<GuideImgIds.length;j++){
            View point=new View(this);
            point.setBackgroundResource(guide_shape);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(25,25);
            if(j>0)
                params.leftMargin=10;
            point.setLayoutParams(params);
            ll_shape.addView(point);
        }
        ll_shape.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                distance=ll_shape.getChildAt(1).getLeft()-ll_shape.getChildAt(0).getLeft();
                ll_shape.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    class MypagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(img_guide.get(position));
            return img_guide.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return GuideImgIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
    public void click_start(View v){
      SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
      sp.edit().putBoolean("is_guided",true).commit();
       startActivity(new Intent(GuideIndex.this, MainActivity.class));
       finish();
    }
}
