package com.example.notes.notetaking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.NoteItem;

import java.util.LinkedList;


public class NoteAdapter extends BaseAdapter {
    private Context context;
    private LinkedList<NoteItem> notesList;

    public NoteAdapter(Context context, LinkedList<NoteItem> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public int getCount() {
        return notesList.size();

    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteItem note =(NoteItem)getItem(position);
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
        imageView.setImageResource(note.getImageId());
        contenttv.setText(note.getContent());
        timetv.setText(note.getTime());
        return view;
    }
}
