package com.lyf.tallybook;

import android.app.Application;
import android.widget.TextView;

import com.lyf.tallybook.model.User;

public class MyApplication extends Application {
    private User user;


    public User getUser() {
        return user;
    }

    public void login(User user) {
        this.user = user;
    }

    public MyApplication() {
        user = null;
    }

    public void logout() {
        this.user = null;
    }

    public boolean isLogin() {
        return user != null;
    }
}
