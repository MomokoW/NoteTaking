package com.example.notes.notetaking.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.notes.notetaking.R;

public class UpdateInformationActivity extends AppCompatActivity {

    private Button headChangeBtn;
    private Button inforChangeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);
        headChangeBtn = (Button) findViewById(R.id.headChangeButton);
        //选择图片，修改头像
        headChangeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        //确定修改个人信息
        inforChangeBtn = (Button)findViewById(R.id.updateInforBtn);
        inforChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
