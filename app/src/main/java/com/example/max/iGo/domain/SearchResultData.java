package com.example.max.iGo.domain;

import java.util.ArrayList;

/**
 * Created by QZ on 2016/4/11.
 */
public class SearchResultData {
    public ArrayList<SearchMenuData> value;
    public String status;
    public class SearchMenuData{
        public String name;
        public String sale;
        public String price;
        public String id;
        public ArrayList<String> pic;
    }
}
