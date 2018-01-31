package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/16.
 */
@Table(name = MessageDb.TABLE_NAME)
public class MessageDb implements Serializable{
    public static final String TABLE_NAME = "MessageTable";
    @Id
    @NoAutoIncrement
    private int id;
    @Column(column = "content")
    private String content;
    @Column(column = "sendTime")
    private String sendTime;
    @Column(column = "type")
    private String type;
    @Column(column = "haveRead")
    private boolean haveRead;

    public MessageDb() {
    }

    public MessageDb(int id, String content, String sendTime, String type, boolean haveRead) {
        this.id = id;
        this.content = content;
        this.sendTime = sendTime;
        this.type = type;
        this.haveRead = haveRead;
    }

    public boolean isHaveRead() {
        return haveRead;
    }

    public void setHaveRead(boolean haveRead) {
        this.haveRead = haveRead;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
