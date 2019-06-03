package com.example.notes.notetaking.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.notes.notetaking.Util.AlarmItem;

import java.util.Calendar;
import java.util.LinkedList;

public class ModifyAlarmActivity extends AppCompatActivity implements View.OnClickListener{
    TextView newtimeTextView,actiontimeTextView;
    EditText biaotiTextView, textTextView;
    Button btn_set,btn_cancel;
    private CustomDatePicker mTimerPicker;
    NotesDB alarmsDB;//数据库辅助对象
    private SQLiteDatabase dbWriter;//数据库对象
    String tempbiaoti,temptext,tempnewTime,tempactiontime;//用于保存活动界面中过临时创建的新的值
    String biaoti,newtime,actiontime,text;//用于接收活动跳转传过来的变量值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_alarm);

        //初始化显示信息
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        biaoti = bundle.getString("biaoti");
        newtime = bundle.getString("newtime");
        actiontime = bundle.getString("actiontime");
        text = bundle.getString("text");

        biaotiTextView = findViewById(R.id.alarmbiaoti);
        biaotiTextView.setText(biaoti);

        newtimeTextView = findViewById(R.id.tv_new_time);
        newtimeTextView.setText(newtime);

        actiontimeTextView = findViewById(R.id.tv_selected_time);
        actiontimeTextView.setText(actiontime);

        textTextView = findViewById(R.id.alarmtext);
        textTextView.setText(text);

        //时间框设置响应函数
        findViewById(R.id.ll_time).setOnClickListener(this);

        //按钮设置响应函数

        btn_set = findViewById(R.id.btn_set);
       btn_set.setOnClickListener(this);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        initTimerPicker();

    }//the end of OnCreate()

    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        tempnewTime = endTime;

        Toast.makeText(ModifyAlarmActivity.this,tempnewTime,Toast.LENGTH_SHORT).show();

        //actiontimeTextView.setText(endTime);
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                actiontimeTextView.setText(DateFormatUtils.long2Str(timestamp, true));
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

    //单机响应事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(actiontimeTextView.getText().toString());
                break;
                //保存按钮响应函数具体实现
            case R.id.btn_set:

                //获取数据库
                dbWriter=getDataBase();
                //保存如数据库
                tempactiontime = actiontimeTextView.getText().toString();
                tempbiaoti = biaotiTextView.getText().toString();
                temptext = textTextView.getText().toString();
               if(tempactiontime.equals(actiontime))
               {
                   if(tempbiaoti.equals(biaoti)&&temptext.equals(text))
                   {
                       Toast.makeText(ModifyAlarmActivity.this,"未更改",Toast.LENGTH_SHORT).show();
                   }else{

                       //获得sql语句
                       String sql = "update " + NotesDB.TABLE_AlARMS + " set "
                               + NotesDB.ALARM_TITLE  + " = " +"'"+tempbiaoti +"' , "
                               + NotesDB.CON_REMARK +" = "+"'"+temptext+"'" + " where " +
                               NotesDB.USER_ID +" = " +"'"+ MainUser.user.getId()+"'" +" and "+
                                NotesDB.FUT_TIME + " = " + "'"+actiontime+"'";
                       //更新到数据库
                       updateData(dbWriter,sql);
                       //关闭数据库

                       Toast.makeText(ModifyAlarmActivity.this,"时间未更改",Toast.LENGTH_SHORT).show();

                   }
               }else
               {
                  //修改数据库中的创建时间
                   newtimeTextView.setText(tempnewTime);
                   //获得sql语句
                   String sql =  "update " + NotesDB.TABLE_AlARMS + " set "
                           + NotesDB.ALARM_TITLE  + " = " +"'"+tempbiaoti +"' , "
                           + NotesDB.CON_REMARK +" = "+"'"+temptext+"' , "
                           + NotesDB.CUR_TIME +" = " + "'"+tempnewTime+"' , "
                           + NotesDB.FUT_TIME  +" = " +"'"+tempactiontime+" ' "
                           + " where " + NotesDB.USER_ID +" = " +"'"+ MainUser.user.getId()+"'" +" and "+
                           NotesDB.FUT_TIME + " = " + "'"+actiontime+"'";
                   //更新到数据库
                   updateData(dbWriter,sql);
                   //关闭数据库

                   Toast.makeText(ModifyAlarmActivity.this,"全部更改",Toast.LENGTH_SHORT).show();

               }
               //退出当前界面
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


    //获取数据库的列表
    public void updateData(SQLiteDatabase writeableDatabase,String sql)
    {
        writeableDatabase.execSQL(sql);
    }



    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        alarmsDB = new NotesDB(this,"notes.db",null,1);
        return alarmsDB.getWritableDatabase();
    }

}
