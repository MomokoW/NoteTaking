package com.example.notes.notetaking.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

    public static String getTime()
    {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        return format1.format(date);
    }
}
