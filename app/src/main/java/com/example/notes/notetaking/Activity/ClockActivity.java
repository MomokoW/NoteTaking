package com.example.notes.notetaking.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.notes.notetaking.R;
import java.util.Calendar;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_set,btn_cancel;
    AlarmManager am;
    PendingIntent pi;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        initView();
    }

    private void initView() {
        btn_set=findViewById(R.id.btn_set);

        btn_cancel=findViewById(R.id.btn_cancel);

        btn_set.setOnClickListener(this);

        btn_cancel.setOnClickListener(this);

        initAlarm();
    }

    private void initAlarm() {
        pi=PendingIntent.getBroadcast(this,0,getMsgIntent(),0);
        time=System.currentTimeMillis();
        am= (AlarmManager) getSystemService(ALARM_SERVICE);
    }
    private Intent getMsgIntent() {
//        //AlarmReceiver 为广播在下面代码中
//        Intent intent=new Intent(this,AlarmReceiver.class);
//        intent.setAction(AlarmReceiver.BC_ACTION);
//        intent.putExtra("msg","闹钟开启");
//        return intent;
        return null;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_set:
                setAlarm();
                Toast.makeText(ClockActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                cancelAlarm();
                Toast.makeText(ClockActivity.this,"取消成功",Toast.LENGTH_SHORT).show();
                break;

        }

    }
    private void cancelAlarm() {
        am.cancel(pi);
    }
    private void setAlarm() {
        //android Api的改变不同版本中设 置有所不同
        if(Build.VERSION.SDK_INT<19){
            am.set(AlarmManager.RTC_WAKEUP,getTimeDiff(),pi);
        }else{
            am.setExact(AlarmManager.RTC_WAKEUP,getTimeDiff(),pi);
        }
    }

    public long getTimeDiff(){
       //这里设置的是当天的14：16分，注意精确到秒，时间可以自由设置
        Calendar ca=Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY,14);
        ca.set(Calendar.MINUTE,16);
        ca.set(Calendar.SECOND,0);
        return ca.getTimeInMillis();
    }
}
