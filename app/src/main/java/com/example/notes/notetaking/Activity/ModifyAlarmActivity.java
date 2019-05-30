package com.example.notes.notetaking.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.AlarmItem;

import java.util.LinkedList;

public class ModifyAlarmActivity extends AppCompatActivity {
    TextView biaotiTextView,newtimeTextView,actiontimeTextView,textTextView;
    Button btn_set,btn_cancel;
    LinkedList<AlarmItem> alarmItems= new LinkedList<AlarmItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_alarm);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String biaoti = bundle.getString("biaoti");
        String newtime = bundle.getString("newtime");
        String actiontime = bundle.getString("actiontime");
        String text = bundle.getString("text");

        biaotiTextView = findViewById(R.id.alarmbiaoti);
        biaotiTextView.setText(biaoti);


        newtimeTextView = findViewById(R.id.tv_new_time);
        newtimeTextView.setText(newtime);


        actiontimeTextView = findViewById(R.id.tv_selected_time);
        actiontimeTextView.setText(actiontime);

        textTextView = findViewById(R.id.alarmtext);
        textTextView.setText("text");

        btn_set = findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*


                确定按钮的响应函数
                 */
                Toast.makeText(ModifyAlarmActivity.this,"修改闹钟",Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  返回按钮
                 */
                finish();
            }
        });


    }
}
