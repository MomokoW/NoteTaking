package com.example.notes.notetaking.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.notes.notetaking.Activity.AddNotesActivity;
import com.example.notes.notetaking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesList extends Fragment implements View.OnClickListener{

    public FloatingActionButton btnAdd;

    public NotesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_noteslist,null);
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("随手记");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        btnAdd =(FloatingActionButton)view.findViewById(R.id.addNotes);
        btnAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addNotes:
                startActivity(new Intent(getActivity(), AddNotesActivity.class));
                break;
        }
    }
}
