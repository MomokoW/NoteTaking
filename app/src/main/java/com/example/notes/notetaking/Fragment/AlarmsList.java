package com.example.notes.notetaking.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Adapter.AlarmAdapter;
import com.example.notes.notetaking.Util.AlarmItem;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmsList extends Fragment implements View.OnClickListener{

    public FloatingActionButton btnAdd;//浮动按钮
    public ListView lv;//ListView界面
    LinkedList<AlarmItem> alarmItems= new LinkedList<AlarmItem>();//存储所有数据的列表
    NotesDB alarmsDB;//数据库辅助对象
    private SQLiteDatabase dbReader;//数据库对象
    String biaoti,text,newTime,selecttime;//用于存储标题，创建时间，响应时间，响应事件的字符串
    public AlarmsList() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_alarm_list,null);
        lv = (ListView)view.findViewById(R.id.alarmlist);

        //获取数据库
        dbReader = getDataBase();
        //获取数据库中的信息，并赋值给一个linklist列表
        getData(dbReader);

        //将信息传递给适配器，并加以输出
        lv.setAdapter(new AlarmAdapter(getActivity(),alarmItems));

        //listview的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"点击事件"+position,Toast.LENGTH_SHORT).show();

                AlarmItem alarmitem = (AlarmItem)parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ModifyAlarmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("biaoti",alarmitem.biaoti);
                bundle.putCharSequence("newtime",alarmitem.chuangjianshijian);
                bundle.putCharSequence("actiontime",alarmitem.xiangyingshijian);
                bundle.putCharSequence("text",alarmitem.text);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });//the end of setOnItemClickListener

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"长按事件"+position,Toast.LENGTH_SHORT).show();
                return false;
            }
        });//the end of setOnItemLongClickListener
        //新建闹钟按钮
        btnAdd =(FloatingActionButton)view.findViewById(R.id.addalarm);
        btnAdd.setOnClickListener(this);
        return view;
    }//the end of OnCreate

    //按钮单击响应事件
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addalarm:
                startActivity(new Intent(getContext(), NewAlarmActivity.class));
                break;
        }
    }
    //获取数据库的列表
    public void getData(SQLiteDatabase readableDatabase)
    {
        Cursor cursor = readableDatabase.rawQuery("Select * from alarm",null);
        AlarmItem temp;
        while(cursor.moveToNext())
        {
            biaoti = cursor.getString(cursor.getColumnIndex(NotesDB.ALARM_TITLE));
            text = cursor.getString(cursor.getColumnIndex(NotesDB.CON_REMARK));
            newTime = cursor.getString(cursor.getColumnIndex(NotesDB.CUR_TIME));
            selecttime = cursor.getString(cursor.getColumnIndex(NotesDB.FUT_TIME));
            temp = new AlarmItem(biaoti,newTime,selecttime,text);
            alarmItems.add(temp);
        }
    }

    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        alarmsDB = new NotesDB(getContext(),"notes.db",null,1);
        return alarmsDB.getWritableDatabase();
    }

}
