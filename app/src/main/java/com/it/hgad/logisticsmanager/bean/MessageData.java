package com.it.hgad.logisticsmanager.bean;

/**
 * Created by Administrator on 2017/4/14.
 */
public class MessageData {
    private String content;
    private String time;
    private String type;
    private int id;
    private boolean havaRead;
    private int showCount;

    public MessageData(String content, String time, String type, int id, boolean havaRead) {
        this.content = content;
        this.time = time;
        this.type = type;
        this.id = id;
        this.havaRead = havaRead;
        this.showCount = havaRead ? 0 : 1;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public boolean isHavaRead() {
        return havaRead;
    }

    public void setHavaRead(boolean havaRead) {
        this.havaRead = havaRead;
        this.showCount = havaRead ? 0 : 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
