package com.example.notes.notetaking.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Manager.datepicker.CustomDatePicker;
import com.example.notes.notetaking.Manager.datepicker.DateFormatUtils;
import com.example.notes.notetaking.Model.MainUser;
import com.example.notes.notetaking.R;

import java.util.Calendar;

public class NewAlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvSelectedDate, mTvSelectedTime;
    private EditText malarmbiaoti,malarmtext;
    private CustomDatePicker mDatePicker, mTimerPicker;
    Button sureButton,cancelButton;
    NotesDB alarmsDB;
    private SQLiteDatabase dbWriter;
    String biaoti,text,newTime,selecttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        //给布局添加一个点击事件的响应
        //获取日期的文本框，并复制
         mTvSelectedDate = findViewById(R.id.tv_new_time);
        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        malarmbiaoti = findViewById(R.id.alarmbiaoti);
        malarmtext = findViewById(R.id.alarmtext);
        sureButton = findViewById(R.id.btn_set);
        sureButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(this);
        initTimerPicker();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
            case R.id.btn_set:
                //获取数据库
                //创建数据库对象
                dbWriter = getDataBase();
                //保存入数据库
                addAlarm();
                //提示保存成功
                Toast.makeText(NewAlarmActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                //跳转到Fragment界面
               dbWriter.close();
               finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        alarmsDB = new NotesDB(this,"notes.db",null,1);
        return alarmsDB.getWritableDatabase();
    }

    public void addAlarm(){
        //获取编辑框里面的数据
        biaoti = malarmbiaoti.getText().toString();
        text = malarmtext.getText().toString();
        newTime = mTvSelectedDate.getText().toString();
        selecttime = mTvSelectedTime.getText().toString();
        //将数据放入ContentValues之中
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.USER_ID,MainUser.user.getId());
        cv.put(NotesDB.ALARM_TITLE,biaoti);
        cv.put(NotesDB.CON_REMARK,text);
        cv.put(NotesDB.CUR_TIME,newTime);
        cv.put(NotesDB.FUT_TIME,selecttime);
        //写入数据库
        dbWriter.insert(NotesDB.TABLE_AlARMS,null,cv);
    }

    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        mTvSelectedTime.setText(endTime);
        mTvSelectedDate.setText(endTime);
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
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
