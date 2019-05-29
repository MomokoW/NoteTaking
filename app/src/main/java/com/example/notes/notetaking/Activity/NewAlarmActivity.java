package com.example.notes.notetaking.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.datepicker.CustomDatePicker;
import com.example.notes.notetaking.Manager.datepicker.DateFormatUtils;
import com.example.notes.notetaking.R;

import java.util.Calendar;

public class NewAlarmActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mTvSelectedDate, mTvSelectedTime;
    private CustomDatePicker mDatePicker, mTimerPicker;
    private  Button btn_set;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        //给布局添加一个点击事件的响应
        //获取日期的文本框，并复制
        findViewById(R.id.ll_date).setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date);
        //initDatePicker();

        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        //initTimerPicker();

        btn_set = findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 **********
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 * 此处应将或取得时间存入数据库。
                 */
                Toast.makeText(NewAlarmActivity.this,"保存入数据库",Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mDatePicker.onDestroy();
        finish();
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
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
//                SimpleDateFormat format = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                }
//                String d = format.format(timestamp);
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    try {
//                        Date date=format.parse(d);
//                        Timer t = new Timer();
//                        t.schedule(new Task(),date);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }

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
