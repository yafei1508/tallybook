package com.lyf.tallybook.model;

import android.content.Intent;

import cn.bmob.v3.BmobObject;

public class Category extends BmobObject {
    private Integer id;
    private String categoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
