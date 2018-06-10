package com.lyf.tallybook.acticity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lyf.tallybook.R;
import com.lyf.tallybook.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class SignInActivity extends AppCompatActivity {

    EditText userNameET;
    EditText passwordET;
    Button regButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("用户注册");
        userNameET = findViewById(R.id.reg_user_name);
        passwordET = findViewById(R.id.reg_password);
        regButton = findViewById(R.id.reg_button);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userNameET.getText().toString().equals("") && !passwordET.getText().toString().equals("")) {
                    BmobQuery<User> query = new BmobQuery<>();
                    query.addWhereEqualTo("userName", userNameET.getText().toString());
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if(e == null) {
                                if(list.size() == 0) {
                                    User u = new User();
                                    u.setUserName(userNameET.getText().toString());
                                    u.setPassword(passwordET.getText().toString());
                                    u.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "注册失败，请检查注册信息", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "服务器出现问题", Toast.LENGTH_SHORT).show();
                                Log.i("Bmob", e.getMessage() + e.getErrorCode());
                            }
                        }
                    });
                }else {
                    Toast.makeText(v.getContext(), "注册失败！请检查用户名与密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
