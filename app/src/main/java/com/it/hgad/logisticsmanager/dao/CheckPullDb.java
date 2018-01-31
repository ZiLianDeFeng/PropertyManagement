package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17.
 */
@Table(name = CheckPullDb.TABLE_NAME)
public class CheckPullDb implements Serializable{
    public static final String TABLE_NAME = "CheckPullTable";

    private int id;
    @Column(column = "checkId")
    private int checkId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CheckPullDb(int checkId) {
        this.checkId = checkId;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public CheckPullDb() {
    }
}
