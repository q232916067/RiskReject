package com.example.administrator.riskreject.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/7/11.
 */

public class SplashInfo extends BmobObject {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
