package com.example.notes.notetaking.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.notes.notetaking.R;

public class AlramListActivity extends AppCompatActivity {
    private ListView mLV1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alram_list);
    }
}
