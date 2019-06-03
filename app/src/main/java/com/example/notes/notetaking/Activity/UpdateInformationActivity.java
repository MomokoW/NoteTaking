package com.example.notes.notetaking.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Manager.User;
import com.example.notes.notetaking.Manager.UserManage;
import com.example.notes.notetaking.Model.MainUser;
import com.example.notes.notetaking.R;

public class UpdateInformationActivity extends AppCompatActivity {

    //private Button headChangeBtn;
    private Button inforChangeBtn;
    private EditText nameChange;
    private  EditText passwordOld;
    private EditText passwordChange1;
    private EditText passwordChange2;
    private String headPhotoURL;
    private UserManage userManage;
    private NotesDB notesDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);
        //headChangeBtn = (Button) findViewById(R.id.headChangeButton);

        nameChange = (EditText)findViewById(R.id.nameChangeEdit);
        passwordOld = (EditText)findViewById(R.id.passwordOldEdit);
        passwordChange1 = (EditText)findViewById(R.id.passwordNew1Edit);
        passwordChange2 = (EditText)findViewById(R.id.passwordNew2Edit);

        /*
        MainUser.user.setHeadPhoto(" ");
        MainUser.user.setId("11111111111");
        MainUser.user.setName("1");
        MainUser.user.setPassword("1");
        */
        //User user=new User("11111111111","1","1","");
        //MainUser.user=user;
        userManage = new UserManage();
        notesDB = new NotesDB(this,"data.db",null,1);
        /*
        //选择图片，修改头像
        headChangeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        */

        //确定修改个人信息
        inforChangeBtn = (Button)findViewById(R.id.updateInforBtn);
        inforChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameNew = nameChange.getText().toString();
                String passOld = passwordOld.getText().toString();
                String passNew1 = passwordChange1.getText().toString();
                String passNew2 = passwordChange2.getText().toString();
                if(passOld.equals(MainUser.user.getPassword())){

                    if(nameNew.length()==0){
                        Toast.makeText(UpdateInformationActivity.this, "修改个人资料失败，用户名为空", Toast.LENGTH_SHORT).show();
                    }
                    else if((!passNew1.equals(passNew2))||(passNew1.length()==0)){
                        Toast.makeText(UpdateInformationActivity.this, "修改个人资料失败，新密码格式错误或者两次密码不同", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        /*
                        此处SQL语句有问题，需要修改
                         */
                        //String urlPhoto=MainUser.user.getId();
                       userManage.updateUser(notesDB.getWritableDatabase(),MainUser.user.getId(),passNew1,nameNew);
                       MainUser.user=userManage.getuser(notesDB.getReadableDatabase(),MainUser.user.getId(),passNew1);
                        Toast.makeText(UpdateInformationActivity.this, "修改个人资料成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    Toast.makeText(UpdateInformationActivity.this, "修改个人资料失败，原密码错误", Toast.LENGTH_SHORT).show();

                }
                //Toast.makeText(UpdateInformationActivity.this, "修改个人资料成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
