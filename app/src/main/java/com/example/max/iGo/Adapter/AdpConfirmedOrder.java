package com.example.max.iGo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.iGo.R;
import com.example.max.iGo.Model.ConfirmOrderModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Max on 2016/4/14.
 */
public class AdpConfirmedOrder extends BaseAdapter implements View.OnClickListener{
    public List<ConfirmOrderModel> list;
    private Context context;

    public AdpConfirmedOrder(List<ConfirmOrderModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_confirmed_order_listview, null);
            //存对象到view里。
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        init(holder, convertView, position);


//            System.out.println(countInt);


        return convertView;

    }

    private void init(ViewHolder holder, View convertView, int position) {
        initView(holder,convertView,position);
        //初始化数据
        ConfirmOrderModel confirmOrderModel =list.get(position);
        if (confirmOrderModel == null)
        {
            return ;
        }
        Picasso.with(context).load(confirmOrderModel.getImgUrl()).error(R.drawable.icon).into(holder.imgUrl);
        holder.orderVolum.setText(confirmOrderModel.getOrderVolum());
        holder.goodsName.setText(confirmOrderModel.getGoodsName());
        holder.price.setText(confirmOrderModel.getPrice());
        holder.salesVolume.setText(confirmOrderModel.getSalesVolume());
    }



    /**
     * 初始化View,设置监听
     * @param holder
     * @param convertView
     * @param position
     */
    private void initView(final ViewHolder holder, View convertView, final int position) {

        holder.goodsName = (TextView) convertView.findViewById(R.id.textView_confirm_goodsname);
        holder.salesVolume = (TextView) convertView.findViewById(R.id.textView_confirm_count);
        holder.orderVolum = (TextView) convertView.findViewById(R.id.confirm_count);
        holder.price = (TextView) convertView.findViewById(R.id.textView_confirm_price);
        holder.imgUrl = (ImageView) convertView.findViewById(R.id.imageView_confirm_lv);
        holder.ivUp = (ImageView) convertView.findViewById(R.id.confirm_count_up);
        holder.ivDown = (ImageView) convertView.findViewById(R.id.confirm_count_down);

        holder.ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(list.get(position).getOrderVolum()).intValue();
                count++;
                list.get(position).setOrderVolum(count +"");
                holder.orderVolum.setText(count + "");
                System.out.println("you click th up");
            }
        });
        holder.ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(list.get(position).getOrderVolum()).intValue();

                if (count > 0) {
                    count--;
                    list.get(position).setOrderVolum(count +"");
                    holder.orderVolum.setText(count + "");
                    System.out.println("you click th down");
                }

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {



    }

    public class ViewHolder{
        TextView goodsName;
        TextView price;
        TextView salesVolume;
        ImageView imgUrl;
        TextView orderVolum;
        ImageView ivUp;
        ImageView ivDown;
    }
}


