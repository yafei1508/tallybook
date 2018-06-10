package com.lyf.tallybook.acticity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyf.tallybook.MyApplication;
import com.lyf.tallybook.R;
import com.lyf.tallybook.model.User;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LogInActivity extends AppCompatActivity {
    EditText userNameET;
    EditText passwordET;
    TextView reg;
    Button log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("用户登录");
        userNameET = findViewById(R.id.log_user_name);
        passwordET = findViewById(R.id.log_password);
        reg = findViewById(R.id.log_page_reg_button);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击
                Intent intent = new Intent(LogInActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        log = findViewById(R.id.log_button);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("userName", userNameET.getText().toString());
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if(e==null) {
                            if(list.size() >= 1) {
                                String password = list.get(0).getPassword();
                                if(passwordET.getText().toString().equals(password)) {
                                    updateUserInfo(list.get(0));
                                    //更新应用中存取的用户信息
                                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                    //释放登录页面
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "用户名不存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Log.i("Bmpb", "失败:" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });

            }
        });

    }

    /**
     * 登录或者改变用户时进行更新用户信息
     * @param u
     */
    public void updateUserInfo(User u) {
        ((MyApplication)getApplication()).login(u);
        TextView textView = findViewById(R.id.username);
    }
}
