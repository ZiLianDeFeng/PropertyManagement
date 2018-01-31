package com.it.hgad.logisticsmanager.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */
public class NewParamDbEntity {


    /**
     * Task : {"PlanTime":"2017-04-27T08:00:00","Responser":"肖虎","ShouldTime":"2017-04-27T08:30:00","TaskStatus":"0","InspectorName":" 杨少华, 杨恒之, ","DeviceType":"配电","TaskCode":"2017042600182","FinishTime":null,"TaskName":"(2D-1  进线柜),日巡检","DeviceCode":"DM20170323145442","Inspector":" 17, 21, ","ArrageId":14,"Id":7542,"DeviceName":"2D-1  进线柜"}
     * Description : null
     * ResultStatus : null
     * DeviceParamSet : [{"Status":null,"ReferenceUnit":"A","ActualValue":null,"ReferenceEndValue":"300","ReferenceStartValue":"100","TaskResult":null,"ReferenceTypeId":"40","Id":68996,"ReferenceName":"电流","Remark":""},{"Status":null,"ReferenceUnit":"V","ActualValue":null,"ReferenceEndValue":"420","ReferenceStartValue":"380","TaskResult":null,"ReferenceTypeId":"40","Id":68997,"ReferenceName":"电压","Remark":""}]
     * ActualParamTypeId : null
     * ReferenceParamTypeName : 2D1低压进线柜
     * Id : 7542
     * ReferenceParamTypeId : 40
     */
    private TaskEntity Task;
    private String Description;
    private String ResultStatus;
    private List<DeviceParamSetEntity> DeviceParamSet;
    private String ActualParamTypeId;
    private String ReferenceParamTypeName;
    private int Id;
    private String ReferenceParamTypeId;

    public void setTask(TaskEntity Task) {
        this.Task = Task;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setResultStatus(String ResultStatus) {
        this.ResultStatus = ResultStatus;
    }

    public void setDeviceParamSet(List<DeviceParamSetEntity> DeviceParamSet) {
        this.DeviceParamSet = DeviceParamSet;
    }

    public void setActualParamTypeId(String ActualParamTypeId) {
        this.ActualParamTypeId = ActualParamTypeId;
    }

    public void setReferenceParamTypeName(String ReferenceParamTypeName) {
        this.ReferenceParamTypeName = ReferenceParamTypeName;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setReferenceParamTypeId(String ReferenceParamTypeId) {
        this.ReferenceParamTypeId = ReferenceParamTypeId;
    }

    public TaskEntity getTask() {
        return Task;
    }

    public String getDescription() {
        return Description;
    }

    public String getResultStatus() {
        return ResultStatus;
    }

    public List<DeviceParamSetEntity> getDeviceParamSet() {
        return DeviceParamSet;
    }

    public String getActualParamTypeId() {
        return ActualParamTypeId;
    }

    public String getReferenceParamTypeName() {
        return ReferenceParamTypeName;
    }

    public int getId() {
        return Id;
    }

    public String getReferenceParamTypeId() {
        return ReferenceParamTypeId;
    }

    public class TaskEntity {
        /**
         * PlanTime : 2017-04-27T08:00:00
         * Responser : 肖虎
         * ShouldTime : 2017-04-27T08:30:00
         * TaskStatus : 0
         * InspectorName :  杨少华, 杨恒之,
         * DeviceType : 配电
         * TaskCode : 2017042600182
         * FinishTime : null
         * TaskName : (2D-1  进线柜),日巡检
         * DeviceCode : DM20170323145442
         * Inspector :  17, 21,
         * ArrageId : 14
         * Id : 7542
         * DeviceName : 2D-1  进线柜
         */
        private String PlanTime;
        private String Responser;
        private String ShouldTime;
        private String TaskStatus;
        private String InspectorName;
        private String DeviceType;
        private String TaskCode;
        private String FinishTime;
        private String TaskName;
        private String DeviceCode;
        private String Inspector;
        private int ArrageId;
        private int Id;
        private String DeviceName;

        public void setPlanTime(String PlanTime) {
            this.PlanTime = PlanTime;
        }

        public void setResponser(String Responser) {
            this.Responser = Responser;
        }

        public void setShouldTime(String ShouldTime) {
            this.ShouldTime = ShouldTime;
        }

        public void setTaskStatus(String TaskStatus) {
            this.TaskStatus = TaskStatus;
        }

        public void setInspectorName(String InspectorName) {
            this.InspectorName = InspectorName;
        }

        public void setDeviceType(String DeviceType) {
            this.DeviceType = DeviceType;
        }

        public void setTaskCode(String TaskCode) {
            this.TaskCode = TaskCode;
        }

        public void setFinishTime(String FinishTime) {
            this.FinishTime = FinishTime;
        }

        public void setTaskName(String TaskName) {
            this.TaskName = TaskName;
        }

        public void setDeviceCode(String DeviceCode) {
            this.DeviceCode = DeviceCode;
        }

        public void setInspector(String Inspector) {
            this.Inspector = Inspector;
        }

        public void setArrageId(int ArrageId) {
            this.ArrageId = ArrageId;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }

        public String getPlanTime() {
            return PlanTime;
        }

        public String getResponser() {
            return Responser;
        }

        public String getShouldTime() {
            return ShouldTime;
        }

        public String getTaskStatus() {
            return TaskStatus;
        }

        public String getInspectorName() {
            return InspectorName;
        }

        public String getDeviceType() {
            return DeviceType;
        }

        public String getTaskCode() {
            return TaskCode;
        }

        public String getFinishTime() {
            return FinishTime;
        }

        public String getTaskName() {
            return TaskName;
        }

        public String getDeviceCode() {
            return DeviceCode;
        }

        public String getInspector() {
            return Inspector;
        }

        public int getArrageId() {
            return ArrageId;
        }

        public int getId() {
            return Id;
        }

        public String getDeviceName() {
            return DeviceName;
        }
    }

    public class DeviceParamSetEntity {
        /**
         * Status : null
         * ReferenceUnit : A
         * ActualValue : null
         * ReferenceEndValue : 300
         * ReferenceStartValue : 100
         * TaskResult : null
         * ReferenceTypeId : 40
         * Id : 68996
         * ReferenceName : 电流
         * Remark :
         */
        private String Status;
        private String ReferenceUnit;
        private String ActualValue;
        private String ReferenceEndValue;
        private String ReferenceStartValue;
        private String TaskResult;
        private String ReferenceTypeId;
        private int Id;
        private String ReferenceName;
        private String Remark;

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public void setReferenceUnit(String ReferenceUnit) {
            this.ReferenceUnit = ReferenceUnit;
        }

        public void setActualValue(String ActualValue) {
            this.ActualValue = ActualValue;
        }

        public void setReferenceEndValue(String ReferenceEndValue) {
            this.ReferenceEndValue = ReferenceEndValue;
        }

        public void setReferenceStartValue(String ReferenceStartValue) {
            this.ReferenceStartValue = ReferenceStartValue;
        }

        public void setTaskResult(String TaskResult) {
            this.TaskResult = TaskResult;
        }

        public void setReferenceTypeId(String ReferenceTypeId) {
            this.ReferenceTypeId = ReferenceTypeId;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public void setReferenceName(String ReferenceName) {
            this.ReferenceName = ReferenceName;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getStatus() {
            return Status;
        }

        public String getReferenceUnit() {
            return ReferenceUnit;
        }

        public String getActualValue() {
            return ActualValue;
        }

        public String getReferenceEndValue() {
            return ReferenceEndValue;
        }

        public String getReferenceStartValue() {
            return ReferenceStartValue;
        }

        public String getTaskResult() {
            return TaskResult;
        }

        public String getReferenceTypeId() {
            return ReferenceTypeId;
        }

        public int getId() {
            return Id;
        }

        public String getReferenceName() {
            return ReferenceName;
        }

        public String getRemark() {
            return Remark;
        }
    }
}
