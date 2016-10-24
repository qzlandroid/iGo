package com.example.max.iGo.UserDefined;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.max.iGo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by QZ on 2016/4/2.
 */
public class RefreshListView extends ListView implements OnScrollListener,AdapterView.OnItemClickListener{
    private static final int STATE_PULL_REFRESH = 0; //下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1; //松开刷新
    private static final int STATE_REFRESHING = 2; //正在刷新

    private int CurrentState = STATE_PULL_REFRESH; //当前状态

    private View mHeaderView;
    private int startY = -1; //滑动起点的y坐标
    private int mHeaderViewHeight; //头布局的高度
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private View mFooterView;
    private int mFooterViewHeight;

    public RefreshListView(Context context) {
        super(context);
        initHeadView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeadView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeadView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeadView(){
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header,null);
        this.addHeaderView(mHeaderView);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_fresh_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView)mHeaderView.findViewById(R.id.iv_arr);
        pbProgress = (ProgressBar)mHeaderView.findViewById(R.id.pb_progress);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        //隐藏头布局
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        initArrowAnim();
        tvTime.setText("最后刷新时间:" + getCurrentTime());
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView(){
        mFooterView = View.inflate(getContext(), R.layout.refresh_footer,null);
        this.addFooterView(mFooterView);
        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        //隐藏头布局
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        this.setOnScrollListener(this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(startY == -1){ //确保startY有效
                    startY = (int)ev.getRawY();
                }
                if (CurrentState == STATE_REFRESHING){ //正在刷新时不作处理
                    break;
                }
                int endY = (int)ev.getRawY();
                int dy = endY - startY; //移动偏移量

                if(dy > 0 && getFirstVisiblePosition() == 0){ //只有下拉并且是第一个Item，才显示头部
                    int padding = dy - mHeaderViewHeight;
                    mHeaderView.setPadding(0,padding,0,0);
                    if (padding > 0 && CurrentState != STATE_RELEASE_REFRESH){ //状态改为松开刷新
                        CurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    }else if(padding < 0 && CurrentState != STATE_PULL_REFRESH ){ //状态改为下拉刷新
                        CurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1; //重置
                if (CurrentState == STATE_RELEASE_REFRESH){
                    CurrentState = STATE_REFRESHING; //正在刷新
                    mHeaderView.setPadding(0,0,0,0);
                    refreshState();
                }else if(CurrentState == STATE_PULL_REFRESH){
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }
                break;
            default:
                break;
        }
        try {
            return super.onTouchEvent(ev);
        }catch (Exception e){
            return false;
        }

        //return true;
        
    }
    /**
     *  刷新下拉控件
     */
    private void refreshState(){
        switch (CurrentState){
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(VISIBLE);
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(VISIBLE);
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation(); //必须先清除动画，才能隐藏
                ivArrow.setVisibility(INVISIBLE);
                pbProgress.setVisibility(VISIBLE);
                if(mListener!=null){
                    mListener.onRefresh();
                }
                break;
            default:
                break;
        }
    }

    /**
     *  初始化箭头动画
     */
    private void initArrowAnim(){
        //箭头向上动画
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,
                                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);
        //箭头向下动画
        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,
                                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }
    OnRefreshListener mListener;
    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }




    public interface OnRefreshListener{
        void onRefresh();
        void onLoadMore();
    }

    /**
     * 刷新完成，收起下拉刷新控件
     */
    public void onRefreshComplete(boolean success){
        if(isLoadingMore){
            mFooterView.setPadding(0,-mFooterViewHeight,0,0); //隐藏脚布局
            isLoadingMore = false;
        }else {
            CurrentState = STATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");
            ivArrow.setVisibility(VISIBLE);
            pbProgress.setVisibility(INVISIBLE);
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        }

        if (success){
            tvTime.setText("最后刷新时间:"+getCurrentTime());
        }
    }

    /**
     * 获取当前时间
     */
    public String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
    private boolean isLoadingMore;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING){
            if(getLastVisiblePosition() == getCount() - 1 && !isLoadingMore){ //滑动到了最后
                mFooterView.setPadding(0,0,0,0); //显示脚布局
                setSelection(getCount() - 1); //改变listview显示位置
                isLoadingMore = true;
                if(mListener!=null){
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    OnItemClickListener mItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener = listener;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mItemClickListener!=null){
            mItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }
    }
}
