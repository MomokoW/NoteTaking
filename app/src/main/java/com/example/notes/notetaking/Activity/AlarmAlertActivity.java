package com.example.notes.notetaking.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.notes.notetaking.R;

public class AlarmAlertActivity extends Activity {
    //MediaPlayer mp;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //Play(this);
            super.onCreate(savedInstanceState);
            new AlertDialog.Builder(AlarmAlertActivity.this).setIcon(R.drawable.alarm).setTitle("闹钟响了!!")
                    .setMessage("赶紧起床吧！！！").setPositiveButton("关掉它", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //mp.stop();
                    AlarmAlertActivity.this.finish();
                }
            }).show();
        }
        public void Play(Context context)
        {
            //mp = MediaPlayer.create(context, R.raw.clock);
            //mp.start();
        }
}
