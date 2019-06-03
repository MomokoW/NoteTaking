package com.example.notes.notetaking.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.DateTime;

public class AddNotesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    //标签内容
    final String items[] = {"未标签","生活","个人","旅游","工作"};
    private String tag = "未标签";
    //笔记内容
    private String content = "";
    //初始化控件
    private Button btnSave,addTag;
    private EditText editText;
    private TextView timeTv;
    private NotesDB notesDB;
    private String dateNow;
    private String timeNow;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        initView();
    }

    public void initView(){
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        toolbar.setTitle("编辑笔记");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //初始化按钮及变量
        dateNow = DateTime.getTime();
        timeNow = dateNow.substring(12);

        btnSave = (Button)findViewById(R.id.btn_ok);
        addTag = (Button)findViewById(R.id.tag);
        editText = (EditText)findViewById(R.id.edit_note);
        timeTv = (TextView)findViewById(R.id.showtime);
        timeTv.setText(timeNow);


        //给按钮添加绑定事件
        btnSave.setOnClickListener(this);
        addTag.setOnClickListener(this);
        editText.setOnClickListener(this);

        //创建数据库对象
        dbWriter = getDataBase();

        //初始化底部导航栏
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.addnotes_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        notesDB = new NotesDB(this,"notes.db",null,1);
        return notesDB.getWritableDatabase();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            dbWriter.close();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //添加底部导航栏点击事件的响应
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    //得到便笺的分类
    public void setTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择标签");
        //设置图标
        builder.setIcon(R.mipmap.icon_launcher);
        //设置单选按钮
        builder.setSingleChoiceItems(items,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取出选择的条目
                String item = items[which];
                tag = item;
                addTag.setText(tag);
                //根据不同的标签来设置ImageView
                switch(tag) {
                    case "个人":
                        addTag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_geren,0,0, 0);
                        break;
                    case "工作":
                        addTag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_gongzuo,0,0, 0);
                        break;
                    case "旅游":
                        addTag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_lvyou,0,0, 0);
                        break;
                    //待修改成为中文
                    case "生活":
                        addTag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_shenghuo,0,0, 0);
                        break;
                    case "未标签":
                        addTag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_none,0,0, 0);
                        break;
                }
                //关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                addNotes();
                finish();
                break;
//            case R.id.voice:
//                initSpeech(v.getContext());
//                break;
            case R.id.tag:
                setTag();
                break;

        }


    }

    private void addNotes() {
        ContentValues cv = new ContentValues();
        content = editText.getText().toString();
        cv.put(NotesDB.USER_ID,"11111111111");
        cv.put(NotesDB.NOTES_TAG,tag);
        cv.put(NotesDB.NOTES_TIME,dateNow);
        cv.put(NotesDB.NOTES_CONTENT,content);
        cv.put(NotesDB.NOTES_STATUS,"0");
        dbWriter.insert(NotesDB.TABLE_NOTE,null,cv);
        Toast.makeText(getApplicationContext(),"添加便笺成功!",Toast.LENGTH_LONG).show();
    }
}