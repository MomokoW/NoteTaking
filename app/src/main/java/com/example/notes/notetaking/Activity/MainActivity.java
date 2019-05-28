package com.example.notes.notetaking.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.notes.notetaking.Adapter.MyFragmentPagerAdapter;
import com.example.notes.notetaking.R;

public class MainActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener,BottomNavigationView.OnNavigationItemSelectedListener {

    //UI Objects

    private ViewPager vpager;

    private BottomNavigationView bottomNavigationView;
    private MenuItem menuItem;
    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        //初始化界面控件
        bindViews();
    }

    private void bindViews() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Notes:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.Alarms:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.Mine:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (menuItem != null) {
            menuItem.setChecked(false);
        } else {
            bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        menuItem = bottomNavigationView.getMenu().getItem(position);
        menuItem.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    menuItem = bottomNavigationView.getMenu().getItem(PAGE_ONE);
                    menuItem.setChecked(true);
                    break;
                case PAGE_TWO:
                    menuItem = bottomNavigationView.getMenu().getItem(PAGE_TWO);
                    menuItem.setChecked(true);
                    break;
                case PAGE_THREE:
                    menuItem = bottomNavigationView.getMenu().getItem(PAGE_THREE);
                    menuItem.setChecked(true);
                    break;
            }
        }
    }
}