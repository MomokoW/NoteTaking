package com.example.notes.notetaking.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.notetaking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmsList extends Fragment {

    public FloatingActionButton btnAdd;
    public AlarmsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_noteslist,null);
        //btnAdd =(FloatingActionButton)view.findViewById(R.id.addNotes);
        //btnAdd.setOnClickListener(this);
        return view;
    }

}
