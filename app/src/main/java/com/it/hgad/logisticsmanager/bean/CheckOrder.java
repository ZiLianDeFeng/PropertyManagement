package com.it.hgad.logisticsmanager.bean;

import com.it.hgad.logisticsmanager.dao.CheckOrderDb;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CheckOrder implements Serializable {
    /**
     * deviceType : 办公设备
     * finishTime : 2017-02-10T16:44:31
     * deviceCode : DM20170109153523
     * inspector : 16,15,15
     * deviceName : 设备二
     * inspectorName : test2, test, test
     * arrageId : 9
     * taskCode : 2017011800081
     * planTime : 2017-02-08T13:25:00
     * responser : dd
     * shouldTime : 2017-01-18T14:25:00
     * taskName : 设备二巡检
     * id : 179
     * taskStatus : 2
     */
    private String deviceType;
    private String finishTime;
    private String deviceCode;
    private String inspector;
    private String deviceName;
    private String inspectorName;
    private int arrageId;
    private String taskCode;
    private String planTime;
    private String responser;
    private String shouldTime;
    private String taskName;
    private int id;
    private String taskStatus;
    private List<Param> params;
    private int taskId;
    private String curTime;

    public void setData(CheckOrderDb checkOrderDb){
        this.deviceType = checkOrderDb.getDeviceType();
        this.finishTime = checkOrderDb.getFinishTime();
        this.deviceCode = checkOrderDb.getDeviceCode();
        this.inspector = checkOrderDb.getInspector();
        this.deviceName = checkOrderDb.getDeviceName();
        this.inspectorName = checkOrderDb.getInspectorName();
        this.arrageId = checkOrderDb.getArrageId();
        this.taskCode = checkOrderDb.getTaskCode();
        this.planTime = checkOrderDb.getPlanTime();
        this.responser = checkOrderDb.getResponser();
        this.shouldTime = checkOrderDb.getShouldTime();
        this.taskName = checkOrderDb.getTaskName();
        this.id = checkOrderDb.getTaskId();
        this.taskStatus = checkOrderDb.getTaskStatus();
        this.taskId = checkOrderDb.getTaskResultId();
    }

    public CheckOrder() {
    }

    public CheckOrder(String deviceType, String finishTime, String deviceCode, String inspector, String deviceName, String inspectorName, int arrageId, String taskCode, String planTime, String responser, String shouldTime, String taskName, int id, String taskStatus, int taskId) {
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
        this.id = id;
        this.taskStatus = taskStatus;
        this.taskId = taskId;
    }

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
