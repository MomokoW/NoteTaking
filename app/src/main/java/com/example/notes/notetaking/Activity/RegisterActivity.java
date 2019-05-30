package com.example.notes.notetaking.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Manager.UserManage;
import com.example.notes.notetaking.R;

public class RegisterActivity extends AppCompatActivity {


    private EditText IDInput;
    private EditText passwordInput1;
    private EditText passwordInput2;
    private EditText nameInput;
    private Button   sureButton;
    private Button   backButton;
    private UserManage userManage;
    private NotesDB notesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        IDInput = (EditText)findViewById(R.id.idInput_register);
        passwordInput1 = (EditText)findViewById(R.id.passwordInput1_register);
        passwordInput2 = (EditText)findViewById(R.id.passwordInput2_register);
        nameInput = (EditText)findViewById(R.id.nameInput_register);
        sureButton = (Button)findViewById(R.id.sureButton_register);
        backButton = (Button)findViewById(R.id.backButton_register);
        userManage = new UserManage();
        notesDB = new NotesDB(this,"data.db",null,1);
        //点击确定按钮，进行注册
        sureButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String ID = IDInput.getText().toString();
                String password1 = passwordInput1.getText().toString();
                String password2 = passwordInput2.getText().toString();
                String name = nameInput.getText().toString();
                boolean idResult=true;
                if(ID.length()==11){
                    for(int i=0;i<11;i++){
                        if((ID.charAt(i)<'0')||(ID.charAt(i)>'9')){
                            idResult=false;
                        }
                    }
                }
                else{
                    idResult=false;
                }
                if (!idResult) {
                    Toast.makeText(RegisterActivity.this, "注册失败，手机号格式错误", Toast.LENGTH_SHORT).show();
                } else if ((!password1.equals(password2)) || (password1.length() == 0)) {
                    Toast.makeText(RegisterActivity.this, "注册失败，两次密码不同或者密码为空", Toast.LENGTH_SHORT).show();
                } else if(userManage.queryuesr(notesDB.getReadableDatabase(),ID)){
                    Toast.makeText(RegisterActivity.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "准备注册", Toast.LENGTH_SHORT).show();
                    userManage.insertUser(notesDB.getReadableDatabase(),ID,password1,name);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                   finish();
                }
            }
        });
        //点击返回按钮，销毁当前活动。
        backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
