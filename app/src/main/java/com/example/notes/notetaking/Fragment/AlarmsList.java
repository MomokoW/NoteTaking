package com.example.notes.notetaking.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notes.notetaking.Activity.AddNotesActivity;
import com.example.notes.notetaking.Activity.ModifyAlarmActivity;
import com.example.notes.notetaking.Activity.NewAlarmActivity;
import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Adapter.AlarmAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmsList extends Fragment implements View.OnClickListener{

    public FloatingActionButton btnAdd;
    public ListView lv;
    public AlarmsList() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_alarm_list,null);
        lv = (ListView)view.findViewById(R.id.alarmlist);
        lv.setAdapter(new AlarmAdapter(getActivity()));





        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"点击事件"+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ModifyAlarmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("biaoti","这是标题");
                bundle.putCharSequence("newtime","2099-09-09   10:11:11");
                bundle.putCharSequence("actiontime","2099-09-09   10:11:11");
                bundle.putCharSequence("text","这是正文");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"长按事件"+position,Toast.LENGTH_SHORT).show();
                return false;
            }
        });




        btnAdd =(FloatingActionButton)view.findViewById(R.id.addalarm);
        btnAdd.setOnClickListener(this);
        return view;

        /*
            刺出对listview的点击事件进行监听
         */
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addNotes:
                startActivity(new Intent(getActivity(), NewAlarmActivity.class));
                break;
        }
    }


}
