package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/16.
 */
@Table(name = PullDb.TABLE_NAME)
public class PullDb implements Serializable{
    public static final String TABLE_NAME = "PullTable";
    private int id;
    @Column(column = "repairId")
    private int repairId;

    public PullDb(int repairId) {
        this.repairId = repairId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRepairId() {
        return repairId;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    public PullDb() {
    }
}
