package com.example.notes.notetaking.Manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NotesDB extends SQLiteOpenHelper {
    public static final String TABLE_NOTE = "notes";     //便笺表表名
    public static final String TABLE_AlARMS = "alarm";   //涂鸦存储表名
    public static final String TABLE_USER = "user";      //待办事项表名

    public static final String USER_ID = "user_id";       //user表id,对应另两个表的外键
    public static final String USER_NAME = "user_name";   //用户名
    public static final String PASSWORD = "user_password";//user密码
    public static final String AVATOR = "user_avator";    //user头像

    public static final String NOTES_ID = "notes_id";     //笔记的id，自增
    public static final String NOTES_TAG = "notes_tag";   //笔记的标签
    public static final String NOTES_TIME = "notes_time"; //笔记的创建时间
    public static final String MEDIA_PATH = "media_path"; //笔记中图片，音频，视频的保存路径
    public static final String NOTES_CONTENT = "notes_content";//笔记内容
    public static final String NOTES_STATUS = "notes_status";   //笔记的状态，0未锁定，1锁定

    public static final String ALRAM_ID = "alarm_id";      //待办事项id，自增
    public static final String ALARM_TITLE = "alarm_title";//待办事项标题
    public static final String CON_REMARK = "con_remark";     //待办事项备注
    public static final String CUR_TIME = "cur_ALARMtime"; //待办事项创建的时间
    public static final String FUT_TIME = "fut_time";      //待办事项设置时间
    public NotesDB(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USER + "(" + USER_ID + " CHAR(11) PRIMARY KEY," + USER_NAME +
                " CHAR(20) NOT NULL," + PASSWORD + " CHAR(20) NOT NULL," + AVATOR +" TEXT NOT NULL," +
                " UNIQUE(" + USER_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_NOTE + "(" + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " CHAR(11) NOT NULL," + NOTES_TAG + " CHAR(10) NOT NULL," + NOTES_TIME +
                " TEXT NOT NULL," + NOTES_CONTENT + " TEXT NOT NULL," + MEDIA_PATH + " TEXT," + NOTES_STATUS
                + " CHAR(2) NOT NULL," +
                "FOREIGN KEY(" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + ") ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE " + TABLE_AlARMS + "(" + ALRAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " CHAR(11) NOT NULL," + ALARM_TITLE + " CHAR(20) NOT NULL," + CON_REMARK +
                " TEXT NOT NULL," + CUR_TIME + " TEXT NOT NULL," + FUT_TIME + " TEXT NOT NULL," +
                "FOREIGN KEY(" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + ") ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
