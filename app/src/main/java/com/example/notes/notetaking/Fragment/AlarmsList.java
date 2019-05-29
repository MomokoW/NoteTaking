package com.example.notes.notetaking.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.notes.notetaking.Activity.ClockActivity;
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
        btnAdd =(FloatingActionButton)view.findViewById(R.id.addNotes);
        btnAdd.setOnClickListener(this);
        return view;
       /* TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;*/
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addNotes:
                startActivity(new Intent(getActivity(), ClockActivity.class));
                break;
        }
    }


}
