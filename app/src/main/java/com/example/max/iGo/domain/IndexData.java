package com.example.max.iGo.domain;

import java.util.ArrayList;

/**
 * Created by QZ on 2016/4/5.
 */
public class IndexData {

    public ArrayList<IndexMenuData> value;
    public String status;
    public class IndexMenuData{
        public ArrayList<indexTabData> data;
        public String name;
    }


    public class indexTabData{
        public String id;
        public int isVisible;
        public String pid;
        public int popid;
        public int position;
        public String url;
    }
}
