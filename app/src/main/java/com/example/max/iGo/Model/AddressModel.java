package com.example.max.iGo.Model;

import java.io.Serializable;

/**
 * Created by Max on 2016/4/14.
 */
public class AddressModel implements Serializable{
    private String name;
    private String iphone;
    private String address;

    public AddressModel(String name, String address, String iphone) {
        this.name = name;
        this.address = address;
        this.iphone = iphone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getIphone() {
        return iphone;
    }

    public String getAddress() {
        return address;
    }
}
