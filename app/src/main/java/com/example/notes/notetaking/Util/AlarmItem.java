package com.example.notes.notetaking.Util;

import com.example.notes.notetaking.Adapter.AlarmAdapter;

public class AlarmItem {
    public String biaoti;
    public String chuangjianshijian;
    public String xiangyingshijian;
    public String text;
    public AlarmItem(String biaoti,String chuangjianshijian,String xiangyingshijian,String text)
    {
        this.biaoti = biaoti;
        this.chuangjianshijian =chuangjianshijian;
        this.xiangyingshijian = xiangyingshijian;
        this.text = text;
    }
}
