package com.example.notes.notetaking.Util;

public class NoteItem {
    private String content;    //笔记内容
    private String time;       //笔记创建时间
    private int imageId;       //左侧标签图片

    public NoteItem(String content, String time, int imageId) {
        this.content = content;
        this.time = time;
        this.imageId = imageId;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getImageId() {
        return imageId;
    }

}
