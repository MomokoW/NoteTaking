package com.example.notes.notetaking.Manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManage {
    //the insert data to the database

    public void insertUser(SQLiteDatabase readableDatebase, String id, String password,String name)
    {
        ContentValues values=new ContentValues();
        values.put("user_id",id);
        values.put("user_password",password);
        values.put("user_name",name);
        values.put("user_avator","");     //注册时，头像为空
        readableDatebase.insert("user", null, values);
    }//the end

    //判断插入数据是否存在，true表示存在，false表示不存在
     public  boolean queryuesr(SQLiteDatabase readableDatebase,String id)//the begin
    {
        //注意select语句中对于等于符号后面的字符串需要加上引号
        Cursor cursor=readableDatebase.rawQuery("Select * from user where user_id = "+"'"+id+"'", null);
        int amount=cursor.getCount();
        cursor.close();
        if(amount==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }//the end
    //登录界面通过用户密码进行判断登录成功与否
    public User getuser(SQLiteDatabase readableDatebase,String id,String password)//the begin
    {
        //注意select语句中对于等于符号后面的字符串需要加上引号
        Cursor cursor=readableDatebase.query("user",null,null,null,null,null,null);
        User user=null;
        while(cursor.moveToNext())
        {
            String userid=cursor.getString(cursor.getColumnIndex("user_id"));
            String userpassword=cursor.getString(cursor.getColumnIndex("user_password"));
            String username=cursor.getString(cursor.getColumnIndex("user_name"));
            String userphoto=cursor.getString(cursor.getColumnIndex("user_avator"));
            if(id.equals(userid)&&password.equals(userpassword)){
                user=new User(userid,userpassword,username,userphoto);
            }
        }
        cursor.close();
        return user;
    }//then  end

    //更新用户信息
    public boolean updateUser(SQLiteDatabase readableDatebase,String id,String password,String name) {
        boolean result=false;

        String sql = "update user set user_password=" + "'"+password +"'"+ ", user_name=" + "'"+name +"'"+  " where user_id=" +"'"+ id+"'";
        readableDatebase.execSQL(sql);
        /*
        if (queryuesr(readableDatebase, id)) {
            readableDatebase.execSQL(sql);
            result = true;
        }
        */
        return result;
    }

    //删除用户
    public boolean deleteUser(SQLiteDatabase readableDatebase,String id){
        boolean result=false;
        String sql="delete from user where user_id="+"'"+id+"'";
        if(queryuesr(readableDatebase,id)){
            readableDatebase.execSQL(sql);
            result = true;
        }
        return result;
    }

    //更新用户头像
    public boolean changePhoto(SQLiteDatabase readableDatebase,String id,String photo){
        boolean result=false;
        String sql="update user set user_avator ="+"'"+photo+"'"+"where user_id ="+"'"+id+"'";
        if(queryuesr(readableDatebase,id)){
            readableDatebase.execSQL(sql);
            result = true;
        }
        return result;
    }

}
