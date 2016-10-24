package com.example.max.iGo.Model;

import java.net.URL;

/**
 * Created by Max on 2016/4/14.
 */
public class ConfirmOrderModel {
    private String goodsName;
    private String price;
    private String salesVolume;
    private String imgUrl;
    private String orderVolum;

    public ConfirmOrderModel(String goodsName, String price, String imgUrl, String salesVolume, String orderVolum) {
        this.goodsName = goodsName;
        this.price = price;
        this.imgUrl = imgUrl;
        this.salesVolume = salesVolume;
        this.orderVolum = orderVolum;
    }

    public String getPrice() {
        return price;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getOrderVolum() {
        return orderVolum;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setOrderVolum(String orderVolum) {
        this.orderVolum = orderVolum;
    }

    public String getGoodsName() {
        return goodsName;
    }
}
