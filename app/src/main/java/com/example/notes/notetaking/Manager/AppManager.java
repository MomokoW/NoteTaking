package com.example.notes.notetaking.Manager;

import android.app.Activity;

import java.util.Stack;

public class AppManager {
    private static Stack<Activity> activityStack = new Stack<Activity>();

    /*
    添加Activity到堆栈中
     */
    public static void addActivity(Activity activity){
        activityStack.push(activity);
    }
    /*
    获得当前Activity
     */
    public static Activity currentActivity(){
        return activityStack.lastElement();
    }
    /*
    结束当前Activity
     */
    public static void finishCurrentActivity(){
        Activity activity = activityStack.pop();
        activity.finish();
    }
    /*
    结束所有Activity
     */
    public static void finishAllActivity(){
        for(Activity activity:activityStack){
            if(activity!=null){
                activity.finish();
            }
        }
        activityStack.clear();
    }
}
