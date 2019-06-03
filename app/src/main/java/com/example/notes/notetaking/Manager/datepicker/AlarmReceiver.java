package com.example.notes.notetaking.Manager.datepicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.notes.notetaking.R;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
        Play(context);
    }

    public void Play(Context context)
    {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.clock);
        mp.start();
    }
}
