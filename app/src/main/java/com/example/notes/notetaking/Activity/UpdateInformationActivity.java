package com.example.notes.notetaking.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.notes.notetaking.R;

public class UpdateInformationActivity extends AppCompatActivity {

    private Button headChangeBtn;
    private Button inforChangeBtn;
    private EditText nameChange;
    private  EditText passwordOld;
    private EditText passwordChange1;
    private EditText passwordChange2;
    private String headPhotoURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);
        headChangeBtn = (Button) findViewById(R.id.headChangeButton);
        nameChange = (EditText)findViewById(R.id.nameChangeEdit);
        passwordOld = (EditText)findViewById(R.id.passwordOldEdit);
        passwordChange1 = (EditText)findViewById(R.id.passwordNew1Edit);
        passwordChange2 = (EditText)findViewById(R.id.passwordNew2Edit);
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
                String nameNew = nameChange.getText().toString();
                String passOld = passwordOld.getText().toString();
                String passNew1 = passwordChange1.getText().toString();
                String passNew2 = passwordChange2.getText().toString();

            }
        });
    }
}
