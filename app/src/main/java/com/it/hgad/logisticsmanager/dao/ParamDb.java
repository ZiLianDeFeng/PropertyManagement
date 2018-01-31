package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/10.
 */
@Table(name = ParamDb.TABLE_NAME)
public class ParamDb implements Serializable{
    public static final String TABLE_NAME = "ParamTable";
    public static final String REFERENCE_TYPE_ID = "referenceTypeId";
    @Id
    @NoAutoIncrement
    private int taskId;
    @Column(column = "params")
    private String params;
    @Column(column = REFERENCE_TYPE_ID)
    private String referenceTypeId;

    public ParamDb() {
    }

    public ParamDb(int taskId, String params, String referenceTypeId) {
        this.taskId = taskId;
        this.params = params;
        this.referenceTypeId = referenceTypeId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getReferenceTypeId() {
        return referenceTypeId;
    }

    public void setReferenceTypeId(String referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }
}
