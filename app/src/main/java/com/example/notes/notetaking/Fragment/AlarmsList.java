package com.example.notes.notetaking.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notes.notetaking.Activity.ModifyAlarmActivity;
import com.example.notes.notetaking.Activity.NewAlarmActivity;
import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Model.MainUser;
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
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    AlarmAdapter alarmAdapter;

    public AlarmsList() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_alarm_list,null);
        lv = (ListView)view.findViewById(R.id.alarmlist);


        //创建数据库
        dbReader = getDataBase();
        //获取数据库中的信息，并赋值给一个linklist列表
        getData(dbReader);

        //将信息传递给适配器，并加以输出
        alarmAdapter = new AlarmAdapter(getActivity(),alarmItems);
        lv.setAdapter(alarmAdapter);

        //listview的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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



        //长按列表响应函数
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                alert = null;
                builder = new AlertDialog.Builder(getContext());
                alert = builder.setTitle("温馨提示：")
                        .setMessage("确定删除吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                 *
                                 */
                                AlarmItem alarmitem = (AlarmItem)parent.getItemAtPosition(position);
                                dbReader=getDataBase();
                                String sql = "delete from "+ NotesDB.TABLE_AlARMS
                                        +" where user_id='"+MainUser.user.getId()+"' and fut_time = '"
                                        +alarmitem.xiangyingshijian+"';";
                                deleteData(dbReader,sql);
                                alarmAdapter.deleteAlarm(position);
                                alarmAdapter.notifyDataSetChanged();
                                alarmAdapter.notifyDataSetChanged();
                            }
                        }).create();
                alert.show();

                return true;
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

        Cursor cursor = readableDatabase.rawQuery("Select * from "+ NotesDB .TABLE_AlARMS +" where " +
                NotesDB.USER_ID +" = " +"'"+ MainUser.user.getId()+"'",null);
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
    //从数据库中删除数据
    public void deleteData(SQLiteDatabase writeableDatabase,String sql)
    {
        writeableDatabase.execSQL(sql);
    }

    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        alarmsDB = new NotesDB(getContext(),"notes.db",null,1);
        return alarmsDB.getWritableDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        //刷新数据库信息
        alarmItems.clear();
        //获取数据库
        dbReader = getDataBase();
        //获取数据库中的信息，并赋值给一个linklist列表
        getData(dbReader);
        AlarmAdapter adapter = new AlarmAdapter(getContext(),alarmItems);
        lv.setAdapter(adapter);
    }

}
