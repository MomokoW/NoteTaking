package com.example.notes.notetaking.Model;

import android.app.AlarmManager;
import android.app.PendingIntent;

import com.example.notes.notetaking.Manager.User;
import com.example.notes.notetaking.Manager.datepicker.CustomDatePicker;

public class MainUser {
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    public static CustomDatePicker mTimerPicker;
    public static User user;
}
