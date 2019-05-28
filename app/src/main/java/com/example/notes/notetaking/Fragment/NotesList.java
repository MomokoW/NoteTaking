package com.example.notes.notetaking.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.notetaking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesList extends Fragment {


    public NotesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_noteslist,null);
        return view;

    }

}
