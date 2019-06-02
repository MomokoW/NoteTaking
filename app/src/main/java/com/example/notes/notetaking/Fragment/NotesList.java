package com.example.notes.notetaking.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;

import com.example.notes.notetaking.Activity.AddNotesActivity;
import com.example.notes.notetaking.Adapter.NoteAdapter;
import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesList extends Fragment implements View.OnClickListener{

    private FloatingActionButton btnAdd;
    private ListView lv;
    private Intent intent;
    private Cursor cursor;
    private NotesDB notesDB;
    private NoteAdapter adapter;
    private SQLiteDatabase dbReader;

    public NotesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_noteslist,null);
        initView(view);
        initListener();
        return view;
    }

    public void initView(View view)
    {
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.notelist_toolbar);
        toolbar.setTitle("随手记");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        btnAdd =(FloatingActionButton)view.findViewById(R.id.addNotes);
        lv = (ListView) view.findViewById(R.id.notes_list);

        //打开数据库
        notesDB = new NotesDB(getContext(),"notes.db",null,1);
        dbReader = notesDB.getWritableDatabase();
        cursor = dbReader.query(NotesDB.TABLE_NOTE,null,null,
                null,null,null,null);
        adapter = new NoteAdapter(getContext(),cursor);
        lv.setAdapter(adapter);
    }

    public void initListener()
    {
        btnAdd.setOnClickListener(this);
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
