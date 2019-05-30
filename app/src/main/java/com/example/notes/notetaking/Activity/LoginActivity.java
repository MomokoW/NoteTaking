package com.example.notes.notetaking.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Manager.User;
import com.example.notes.notetaking.Manager.UserManage;
//import com.example.notes.notetaking.Model.MainUser;
import com.example.notes.notetaking.R;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegister;
    private EditText idInput;
    private EditText passwordInput;
    private NotesDB  dbHelper;
    private UserManage userManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonRegister = (Button)findViewById(R.id.buttonReg);
        idInput = (EditText)findViewById(R.id.idInput);
        passwordInput = (EditText)findViewById(R.id.passwordInput);
        userManage = new UserManage();
        dbHelper = new NotesDB(this,"data.db",null,1);
        //登录按钮响应事件
        buttonLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String id = idInput.getText().toString();
                String password = passwordInput.getText().toString();
                User user;
                user=userManage.getuser(dbHelper.getReadableDatabase(),id,password);
                if(user==null){
                    Toast.makeText(LoginActivity.this, "登录失败，请检查帐号密码", Toast.LENGTH_SHORT).show();
                }
                else{
                   // MainUser.user=user;
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        //注册按钮响应事件
        buttonRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}
