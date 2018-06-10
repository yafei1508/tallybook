package com.lyf.tallybook.acticity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyf.tallybook.MyApplication;
import com.lyf.tallybook.R;
import com.lyf.tallybook.model.Category;
import com.lyf.tallybook.model.Record;
import com.lyf.tallybook.model.RecordAdapter;
import com.lyf.tallybook.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.lyf.tallybook.model.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyApplication application;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View navigationHeadView;
    private TextView userNameTextView;
    private ImageView headImageView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Record> records = new ArrayList<>();

    RecyclerView recyclerView;
    RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化Bmob
        Bmob.initialize(this, "2fa828c0e42ce475d3df6989089ad168");
        //初始化各种view对象
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        if (application.isLogin()) {
                            Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                            startActivity(addIntent);
                        }else {
                            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                            startActivity(intent);
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.statistics:
                        if (application.isLogin()) {
                            Intent staticticsIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                            startActivity(staticticsIntent);
                            break;
                        }else  {
                            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                            startActivity(intent);
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about:
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        navigationHeadView = navigationView.getHeaderView(0);
        userNameTextView = navigationHeadView.findViewById(R.id.username);
        userNameTextView.setOnClickListener(this);
        headImageView = navigationHeadView.findViewById(R.id.icon_image);
        headImageView.setOnClickListener(this);
        application = (MyApplication) getApplication();

        //根据当前状态更新view中数据
        //initView();
        //test bmob
//
//
//        Category c = new Category();
//        c.setId(0);
//        c.setCategoryName("吃饭");
//        c.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if(e == null) {
//                    Toast.makeText(getApplicationContext(), "thianjiashuju", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Log.i("Bmob", e.toString());
//                }
//            }
//        });
//        Record r = new Record();
//        r.setSign(true);
//        r.setMoney((double) 100);
//        r.setCategory(0);
//        r.setAddTime(new Date());
//        r.setUserName("root");
//        r.setDetail("first time root submit");
//        r.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    Toast.makeText(getApplicationContext(), "thianjiashuju", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.i("Bmob", e.toString());
//                }
//            }
//        });


        //设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //设置唤醒NavigationView的按钮
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //设置主页面右下角的fliationActionBar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recordAdapter = new RecordAdapter(this.records, this);
        recyclerView.setAdapter(recordAdapter);
        initView();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initView();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }

    private void initView() {
        if (application.isLogin()) {
            userNameTextView.setText(application.getUser().getUserName());
            initList();
            Log.i("MainActivity", application.getUser().getUserName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_settings:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (!this.application.isLogin()) {
            switch (v.getId()) {
                case R.id.icon_image:
                case R.id.username:
                case R.id.fab:
                    Intent intent = new Intent(this, LogInActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;

            }
        } else {
            switch (v.getId()) {
                case R.id.fab:
                    Intent intent = new Intent(this, AddActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    //初始化以及更新records
    public void initList() {
        BmobQuery<Record> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", application.getUser().getUserName());
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> list, BmobException e) {
                if (e == null) {
                    records.clear();
                    records.addAll(list);
                    recordAdapter.notifyDataSetChanged();
                    Log.i("Bmob", String.valueOf(list.size()));
                } else {
                    Log.i("Bmob", e.getMessage() + e.getErrorCode());
                }
            }
        });
    }
}
