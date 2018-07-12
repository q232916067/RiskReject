package com.example.administrator.riskreject.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/7/12.
 */

public class PushInfo extends BmobObject {
    private String title;
    private String ticker;
    private String ContentText;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getContentText() {
        return ContentText;
    }

    public void setContentText(String contentText) {
        ContentText = contentText;
    }
}
