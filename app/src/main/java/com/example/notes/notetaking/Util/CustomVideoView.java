package com.example.notes.notetaking.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Administrator on 2017/5/17.
 */
//自定义VideoView
public class CustomVideoView extends VideoView {
    private PlayPauseListener listener;
    public CustomVideoView(Context context) {
        super(context);
    }
    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setPlayPauseListener(PlayPauseListener listener){
        this.listener = listener;
    }
    @Override
    public void pause() {
        super.pause();
        if (listener!=null){
            listener.onPause();
        }
    }
    @Override
    public void start() {
        super.start();
        if (listener!=null){
            listener.onPlay();
        }
    }
    public interface PlayPauseListener{
        void onPlay();
        void onPause();
    }
}
