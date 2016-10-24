package com.example.max.iGo.UserDefined;

/**
 * Created by sdh on 2016/3/10.
 */
public class PersonAddress {
    private String name;
    private String Addr;
    private String phone;

    public PersonAddress(String name, String addr, String phone) {
        this.name = name;
        Addr = addr;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return Addr;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
