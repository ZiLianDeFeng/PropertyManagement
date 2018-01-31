package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/27.
 */
@Table(name = CheckCommitDb.TABLE_NAME)
public class CheckCommitDb implements Serializable{
    public static final String TABLE_NAME = "CheckCommitTable";
    @Id
    @NoAutoIncrement
    private int taskId;
    @Column(column = "userId")
    private int userId;
    @Column(column = "checkNo")
    private String checkNo;
    @Column(column = "actualValues")
    private String actualValues;
    @Column(column = "feedback")
    private String feedback;
    @Column(column = "finishTime")
    private String finishTime;
    @Column(column = "hasCommit")
    private int hasCommit;
    @Column(column = "paramId")
    private String paramId;
    @Column(column =  "resultId")
    private int resultId;

    public CheckCommitDb() {
    }

    public CheckCommitDb(int taskId, int userId, String checkNo, String actualValues, String feedback, String finishTime, int hasCommit, String paramId, int resultId) {
        this.taskId = taskId;
        this.userId = userId;
        this.checkNo = checkNo;
        this.actualValues = actualValues;
        this.feedback = feedback;
        this.finishTime = finishTime;
        this.hasCommit = hasCommit;
        this.paramId = paramId;
        this.resultId = resultId;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getActualValues() {
        return actualValues;
    }

    public void setActualValues(String actualValues) {
        this.actualValues = actualValues;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getHasCommit() {
        return hasCommit;
    }

    public void setHasCommit(int hasCommit) {
        this.hasCommit = hasCommit;
    }
}
