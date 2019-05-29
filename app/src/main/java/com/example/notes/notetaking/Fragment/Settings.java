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

import com.example.notes.notetaking.Activity.MainActivity;
import com.example.notes.notetaking.Activity.RegisterActivity;
import com.example.notes.notetaking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    private ImageView photoHead;
    private Button logoutButton;
    private Button cancelButton;
    private TextView usernameText;
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine,null);
        photoHead = (ImageView)view.findViewById(R.id.headphotoView_mine);
        //点击头像，修改个人信息。
        photoHead.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
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

            }
        });
        return view;
    }

}
