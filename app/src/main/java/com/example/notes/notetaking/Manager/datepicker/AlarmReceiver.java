package com.example.notes.notetaking.Manager.datepicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

import com.example.notes.notetaking.Activity.AlarmAlertActivity;
import com.example.notes.notetaking.R;

import java.util.Timer;
import java.util.TimerTask;


public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "今天有待办事项哦！", Toast.LENGTH_LONG).show();
        //Intent i = new Intent(context, AlarmAlertActivity.class);
        //Bundle bundle = new Bundle();
        //bundle.putString("Str_caller","");
        //i.putExtras(bundle);
        //context.startActivity(i);
        Play(context);
        //给闹钟设置了一个定时器
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mp.stop();
            }
        },10000);


    }
    public void Play(Context context)
    {
        mp = MediaPlayer.create(context, R.raw.clock);
        mp.start();
    }
}
