package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/23.
 */
@Table(name = CheckOrderDb.TABLE_NAME)
public class CheckOrderDb implements Serializable {
    public static final String TABLE_NAME = "CheckOrderTable";

    @Id
    @NoAutoIncrement
    private int taskId;
    @Column(column = "userId")
    private int userId;
    @Column(column = "deviceType")
    private String deviceType;
    @Column(column = "finishTime")
    private String finishTime;
    @Column(column = "deviceCode")
    private String deviceCode;
    @Column(column = "inspector")
    private String inspector;
    @Column(column = "deviceName")
    private String deviceName;
    @Column(column = "inspectorName")
    private String inspectorName;
    @Column(column = "arrageId")
    private int arrageId;
    @Column(column = "taskCode")
    private String taskCode;
    @Column(column = "planTime")
    private String planTime;
    @Column(column = "responser")
    private String responser;
    @Column(column = "shouldTime")
    private String shouldTime;
    @Column(column = "taskName")
    private String taskName;
    @Column(column = "taskStatus")
    private String taskStatus;
    @Column(column = "taskResultId")
    private int taskResultId;


    public CheckOrderDb() {
    }

    public CheckOrderDb(int id, int userId, String deviceType, String finishTime, String deviceCode, String inspector, String deviceName, String inspectorName, int arrageId, String taskCode, String planTime, String responser, String shouldTime, String taskName, String taskStatus,int taskResultId) {
        this.taskId = id;
        this.userId = userId;
        this.deviceType = deviceType;
        this.finishTime = finishTime;
        this.deviceCode = deviceCode;
        this.inspector = inspector;
        this.deviceName = deviceName;
        this.inspectorName = inspectorName;
        this.arrageId = arrageId;
        this.taskCode = taskCode;
        this.planTime = planTime;
        this.responser = responser;
        this.shouldTime = shouldTime;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskResultId = taskResultId;
    }

    public int getTaskResultId() {
        return taskResultId;
    }

    public void setTaskResultId(int taskResultId) {
        this.taskResultId = taskResultId;
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public int getArrageId() {
        return arrageId;
    }

    public void setArrageId(int arrageId) {
        this.arrageId = arrageId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getResponser() {
        return responser;
    }

    public void setResponser(String responser) {
        this.responser = responser;
    }

    public String getShouldTime() {
        return shouldTime;
    }

    public void setShouldTime(String shouldTime) {
        this.shouldTime = shouldTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }


}
