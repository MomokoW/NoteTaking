package com.example.notes.notetaking.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.notes.notetaking.Manager.datepicker.CustomDatePicker;
import com.example.notes.notetaking.Manager.datepicker.DateFormatUtils;
import com.example.notes.notetaking.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class DoListActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView mTvSelectedDate, mTvSelectedTime;
    private CustomDatePicker mDatePicker, mTimerPicker;

    private Button btn_save, btn_back;
    private EditText text_title, text_remark;
    String cur_date, cur_time;
    String fut_time;

    private String con_title = "";
    private String con_remark = "";
    private SQLiteDatabase dbWriter;
    //NotesDB notesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dolist);
        initView();
    }

    //初始化按钮响应
    public void initView() {
        //初始化导航栏和时间
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑待办事项");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        //初始化时间显示,默认提示时间为创建时间点
        fut_time=getTime();

        //初始化按钮
        btn_save = (Button)findViewById(R.id.save);
        btn_back = (Button)findViewById(R.id.goback);
        text_title = (EditText)findViewById(R.id.text_title);
        text_remark = (EditText) findViewById(R.id.text_remark);

        //监听按钮事件
        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        //创建数据库对象
        ///dbWriter = getDataBase();

        //日期选择器
        findViewById(R.id.ll_date).setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date);
        initDatePicker();

        //时间选择器
        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        initTimerPicker();
    }
    //响应按钮事件

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_date:
                // 日期格式为yyyy-MM-dd
                mDatePicker.show(mTvSelectedDate.getText().toString());
                break;

            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;

            case R.id.save:
                //addDolist();
                finish();
                break;

            case R.id.goback:
                dbWriter.close();
                finish();
                break;
        }
    }
/*
    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        notesDB = new NotesDB(this,"notes.db",null,1);
        return notesDB.getWritableDatabase();
    }

    public void addDolist(){
        ContentValues cv = new ContentValues();
        con_title = text_title.getText().toString();
        con_remark = text_remark.getText().toString();
        cv.put(NotesDB.DO_TITLE,con_title);
        cv.put(NotesDB.DO_REMARK,con_remark);
        cv.put(NotesDB.CUR_TIME,cur_date);
        cv.put(NotesDB.FUT_TIME,fut_time);
        dbWriter.insert(NotesDB.TABLE_DO,null,cv);
        Toast.makeText(getApplicationContext(),"新建待办事项成功!",Toast.LENGTH_LONG).show();

    }
*/
    //获取创建时间
    private String getTime() {
        java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        cur_date = format1.format(date);
        cur_time = cur_date.substring(12);
        return cur_date;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mTvSelectedDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        mTvSelectedTime.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                fut_time=DateFormatUtils.long2Str(timestamp, true);
                mTvSelectedTime.setText(fut_time);
                Intent intent =new Intent("MYALARMRECEIVER");
                PendingIntent sender= PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
}
