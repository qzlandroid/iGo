package com.example.max.iGo.UserDefined;

/**
 * Created by sdh on 2016/3/18.
 */
public class Item_goods {
    private String text;
    private String picname;
    private String path;

    public String getText() {
        return text;
    }

    public String getPicname() {
        return picname;
    }

    public String getPath() {
        return path;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Item_goods{" +
                "text='" + text + '\'' +
                ", picname='" + picname + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
