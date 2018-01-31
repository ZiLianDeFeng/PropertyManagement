package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/10.
 */
@Table(name = CancelCheckDb.TABLE_NAME)
public class CancelCheckDb implements Serializable{
    public static final String TABLE_NAME = "CancelCheckTable";
    public static final String HAS_COMMIT = "hasCommit";
    @Id
    @NoAutoIncrement
    private int taskId;
    @Column(column = HAS_COMMIT)
    private int hasCommit;

    public CancelCheckDb() {
    }

    public CancelCheckDb(int taskId, int hasCommit) {
        this.taskId = taskId;
        this.hasCommit = hasCommit;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getHasCommit() {
        return hasCommit;
    }

    public void setHasCommit(int hasCommit) {
        this.hasCommit = hasCommit;
    }
}

