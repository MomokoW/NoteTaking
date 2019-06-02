package com.example.notes.notetaking.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.notes.notetaking.Activity.LoginActivity;
import com.example.notes.notetaking.Activity.MainActivity;
import com.example.notes.notetaking.Activity.RegisterActivity;
import com.example.notes.notetaking.Activity.UpdateInformationActivity;
import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Manager.UserManage;
import com.example.notes.notetaking.Model.MainUser;
import com.example.notes.notetaking.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    private RoundedImageView photoHead;
    private Button logoutButton;
    private Button cancelButton;
    private Button changeBtn;
    private TextView usernameText;
    private NotesDB dbManage;
    private UserManage userManage;
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine,null);
        userManage = new UserManage();
        dbManage = new NotesDB(getActivity(),"data.db",null,1);
        photoHead = (RoundedImageView) view.findViewById(R.id.headphotoView_mine);
        changeBtn = (Button)view.findViewById(R.id.changeBtn);
        //修改个人资料
        changeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UpdateInformationActivity.class);
                startActivity(intent);
            }
        });

        //点击头像，修改头像。
        photoHead.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });

        //退出程序
        logoutButton = (Button)view.findViewById(R.id.logoutButton_mine);
        logoutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        //注销帐号
        cancelButton = (Button)view.findViewById(R.id.cancalButton_mine);
        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                userManage.deleteUser(dbManage.getReadableDatabase(),MainUser.user.getId());
                /*
                帐号注销成功后，返回登录界面
                 */
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });
        return view;
    }

}
