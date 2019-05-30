package com.example.notes.notetaking.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.NoteItem;


public class NoteAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;

    public NoteAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();

    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        //当convertView不为空，复用convertView
        if(convertView==null){
            //把XML转换成view对象
            view = (RelativeLayout) inflater.inflate(R.layout.note_item,null);
        }
        else{
            view = convertView;
        }
        //设置数据

        TextView contenttv = (TextView) view.findViewById(R.id.note_content);
        TextView timetv =(TextView) view.findViewById(R.id.note_time);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_flag);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex(NotesDB.NOTES_CONTENT));
        String time = cursor.getString(cursor.getColumnIndex(NotesDB.NOTES_TIME));
        String image = cursor.getString(cursor.getColumnIndex(NotesDB.NOTES_TAG));
        //根据不同的标签来设置ImageView
        switch(image) {
            case "个人":
                imageView.setImageResource(R.mipmap.ic_geren);
                break;
            case "工作":
                imageView.setImageResource(R.mipmap.ic_gongzuo);
                break;
            case "旅游":
                imageView.setImageResource(R.mipmap.ic_lvyou);
                break;
                //待修改成为中文
            case "life":
                imageView.setImageResource(R.mipmap.ic_shenghuo);
                break;
            case "未标签":
                imageView.setImageResource(R.mipmap.ic_none);
                break;
        }
        contenttv.setText(content);
        timetv.setText(time);
        return view;
    }
}
