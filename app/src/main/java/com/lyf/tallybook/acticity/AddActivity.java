package com.lyf.tallybook.acticity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lyf.tallybook.MyApplication;
import com.lyf.tallybook.R;
import com.lyf.tallybook.model.Record;

import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{

    private Button selectDateButton;
    private TextView selectedDateTextView;
    private Button submitRecordButton;
    private EditText numberAdd;
    private EditText describeAdd;
    private Spinner categorySpinner;
    private int category;
    private Spinner signSpinner;
    private boolean sign;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("新建记账条目");
        selectDateButton = findViewById(R.id.select_date);
        selectDateButton.setOnClickListener(this);
        selectedDateTextView = findViewById(R.id.selected_date);
        calendar = Calendar.getInstance();
        //初始化日期
        selectedDateTextView.setText(String.valueOf(calendar.get(Calendar.YEAR)) + "-" +  String.valueOf(calendar.get(Calendar.MONTH)) +"-" +  String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        submitRecordButton = findViewById(R.id.submit_record);
        submitRecordButton.setOnClickListener(this);
        numberAdd = findViewById(R.id.number_add);
        describeAdd = findViewById(R.id.describe_add);

        categorySpinner = findViewById(R.id.category);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = 0;
            }
        });
        signSpinner = findViewById(R.id.sign);
        signSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sign = position == 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sign = true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_date:
                new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDateTextView.setText(year + "-" + month + "-" + dayOfMonth);
                        calendar.set(year, month, dayOfMonth);
                        Log.i("AddActivity", String.valueOf(year));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.submit_record:
                if(!numberAdd.getText().toString().equals("") && !describeAdd.getText().toString().equals("") ) {
                    final Record record = new Record();
                    record.setUserName(((MyApplication)getApplication()).getUser().getUserName());
                    record.setAddTime(new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
                    record.setDetail(describeAdd.getText().toString());
                    record.setCategory(category);
                    record.setSign(sign);
                    record.setMoney(Double.parseDouble(numberAdd.getText().toString()));
                    record.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                                Log.i("lyf", record.getAddTime().toString());
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT).show();
                                Log.i("Bmob", e.getMessage() + e.getErrorCode());
                            }
                        }
                    });
                    Toast.makeText(v.getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(v.getContext(), "请检查填写数据", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
