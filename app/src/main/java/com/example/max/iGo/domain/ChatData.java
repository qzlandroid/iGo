package com.example.max.iGo.domain;

import java.util.ArrayList;

/**
 * Created by QZ on 2016/4/7.
 */
public class ChatData {
    public ArrayList<ChatMenuData> value;
    public String status;
    public class ChatMenuData{
        public ArrayList<String> description_title;
        public ArrayList<String> pic;
        public int id;
        public String title;
    }
}
