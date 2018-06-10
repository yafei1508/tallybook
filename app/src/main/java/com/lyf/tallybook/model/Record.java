package com.lyf.tallybook.model;

import java.util.Date;

import cn.bmob.v3.BmobObject;

public class Record extends BmobObject {
    private String userName;
    private Date addTime;
    private Integer Category;
    private Double money;
    private Boolean sign;
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getCategory() {
        return Category;
    }

    public void setCategory(Integer category) {
        Category = category;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Boolean isSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }
}
