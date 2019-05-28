package com.example.notes.notetaking.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.notes.notetaking.R;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);

        //选中条目的监听事件
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        textView.setText(menuItem.getTitle().toString());
                        return true;
                    }
                }
        );

    }
}
