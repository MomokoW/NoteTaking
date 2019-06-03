package com.example.notes.notetaking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.AlarmItem;

import java.util.LinkedList;

public class AlarmAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    LinkedList<AlarmItem> alarmItems= new LinkedList<AlarmItem>();
    //AlarmAdapter的构造函数，一个context类型的参数，也就是一个activity
    //传进去的就是alarmlistactivitty
    public AlarmAdapter(Context context, LinkedList<AlarmItem> alarmItems)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.alarmItems = alarmItems;

    }

    public void deleteAlarm(AlarmItem alarmItem)
    {
        alarmItems.remove(alarmItem);
    }

    @Override
    public int getCount() {
        return alarmItems.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //写一个静态的class，把控件转移过来使用
    static class ViewHolder{
        public TextView textViewbiaoti;
        public TextView textViewCurenttime;
        public TextView textViewSelecttime;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //实例化组件类
        ViewHolder holder = null;
        //如果是图为空
        if(convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.layout_alarmlist_item,null);
            holder = new ViewHolder();
            //把列表栏中的对象传递过来便于赋值
            holder.textViewbiaoti = (TextView)convertView.findViewById(R.id.alarmbiaoti);
            holder.textViewCurenttime = (TextView)convertView.findViewById(R.id.alarmnewtime);
            holder.textViewSelecttime = (TextView)convertView.findViewById(R.id.alarmselecttime);
            //把holder传递进去
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textViewbiaoti.setText(alarmItems.get(position).biaoti);
        holder.textViewCurenttime.setText(alarmItems.get(position).chuangjianshijian);
        holder.textViewSelecttime.setText(alarmItems.get(position).xiangyingshijian);
        return convertView;
    }
}
