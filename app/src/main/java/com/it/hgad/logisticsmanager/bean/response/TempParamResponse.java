package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/4/25.
 */
public class TempParamResponse extends BaseReponse {


    /**
     * result : 0
     * data : [{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5},"referenceUnit":"摄氏度","referenceEndValue":"45","referenceStartValue":"45","actualValue":null,"remark":"","id":1,"referenceName":"冷冻水设定值","status":null},{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5},"referenceUnit":"","referenceEndValue":"1","referenceStartValue":"0","actualValue":null,"remark":"水泵关：0；开为：1。","id":2,"referenceName":"水泵状态","status":null},{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5},"referenceUnit":"","referenceEndValue":"1","referenceStartValue":"0","actualValue":null,"remark":"水流关：0；开为：1。","id":3,"referenceName":"水流开关状态","status":null},{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5},"referenceUnit":"Mpa","referenceEndValue":"0.38","referenceStartValue":"0.32","actualValue":null,"remark":"","id":4,"referenceName":"冷冻进水压力","status":null},{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5},"referenceUnit":"Mpa","referenceEndValue":"0.27","referenceStartValue":"0.23","actualValue":null,"remark":"","id":5,"referenceName":"冷冻出水压力","status":null},{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5,"deviceParamSet":[]},"referenceUnit":"摄氏度","referenceEndValue":"43","referenceStartValue":"36","actualValue":null,"remark":"","id":6,"referenceName":"冷冻进水温度","status":null},{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5},"referenceUnit":"","referenceEndValue":"3","referenceStartValue":"1","actualValue":null,"remark":"运行哪台冷却泵填写哪台冷却泵号码","id":7,"referenceName":"冷冻泵运行泵号","status":null},{"tempResult":{"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5},"referenceUnit":"摄氏度","referenceEndValue":"47","referenceStartValue":"40","actualValue":null,"remark":"","id":8,"referenceName":"冷冻出水温度","status":null}]
     */
    private String result;
    private List<DataEntity> data;

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public class DataEntity {
        /**
         * tempResult : {"resultStatus":null,"tempTask":{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"},"description":null,"id":5}
         * referenceUnit : 摄氏度
         * referenceEndValue : 45
         * referenceStartValue : 45
         * actualValue : null
         * remark :
         * id : 1
         * referenceName : 冷冻水设定值
         * status : null
         */
        private TempResultEntity tempResult;
        private String referenceUnit;
        private String referenceEndValue;
        private String referenceStartValue;
        private String actualValue;
        private String remark;
        private int id;
        private String referenceName;
        private String status;

        public void setTempResult(TempResultEntity tempResult) {
            this.tempResult = tempResult;
        }

        public void setReferenceUnit(String referenceUnit) {
            this.referenceUnit = referenceUnit;
        }

        public void setReferenceEndValue(String referenceEndValue) {
            this.referenceEndValue = referenceEndValue;
        }

        public void setReferenceStartValue(String referenceStartValue) {
            this.referenceStartValue = referenceStartValue;
        }

        public void setActualValue(String actualValue) {
            this.actualValue = actualValue;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setReferenceName(String referenceName) {
            this.referenceName = referenceName;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public TempResultEntity getTempResult() {
            return tempResult;
        }

        public String getReferenceUnit() {
            return referenceUnit;
        }

        public String getReferenceEndValue() {
            return referenceEndValue;
        }

        public String getReferenceStartValue() {
            return referenceStartValue;
        }

        public String getActualValue() {
            return actualValue;
        }

        public String getRemark() {
            return remark;
        }

        public int getId() {
            return id;
        }

        public String getReferenceName() {
            return referenceName;
        }

        public String getStatus() {
            return status;
        }

        public class TempResultEntity {
            /**
             * resultStatus : null
             * tempTask : {"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"}
             * description : null
             * id : 5
             */
            private String resultStatus;
            private TempTaskEntity tempTask;
            private String description;
            private int id;

            public void setResultStatus(String resultStatus) {
                this.resultStatus = resultStatus;
            }

            public void setTempTask(TempTaskEntity tempTask) {
                this.tempTask = tempTask;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getResultStatus() {
                return resultStatus;
            }

            public TempTaskEntity getTempTask() {
                return tempTask;
            }

            public String getDescription() {
                return description;
            }

            public int getId() {
                return id;
            }

            public class TempTaskEntity {
                /**
                 * deviceType : 空调
                 * finishTime : null
                 * remark : 闪闪的红星
                 * inspector : 17
                 * deviceName : 风冷热泵机组1
                 * inspectorName : 杨少华
                 * dvcId : 16
                 * taskCode : 20170407
                 * planTime : 2017-04-07T09:19:39
                 * sendNotice : null
                 * shouldTime : 2017-04-07T09:19:39
                 * taskName : 临时巡检
                 * id : 5
                 * taskStatus : 0
                 */
                private String deviceType;
                private String finishTime;
                private String remark;
                private String inspector;
                private String deviceName;
                private String inspectorName;
                private String dvcId;
                private String taskCode;
                private String planTime;
                private String sendNotice;
                private String shouldTime;
                private String taskName;
                private int id;
                private String taskStatus;

                public void setDeviceType(String deviceType) {
                    this.deviceType = deviceType;
                }

                public void setFinishTime(String finishTime) {
                    this.finishTime = finishTime;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public void setInspector(String inspector) {
                    this.inspector = inspector;
                }

                public void setDeviceName(String deviceName) {
                    this.deviceName = deviceName;
                }

                public void setInspectorName(String inspectorName) {
                    this.inspectorName = inspectorName;
                }

                public void setDvcId(String dvcId) {
                    this.dvcId = dvcId;
                }

                public void setTaskCode(String taskCode) {
                    this.taskCode = taskCode;
                }

                public void setPlanTime(String planTime) {
                    this.planTime = planTime;
                }

                public void setSendNotice(String sendNotice) {
                    this.sendNotice = sendNotice;
                }

                public void setShouldTime(String shouldTime) {
                    this.shouldTime = shouldTime;
                }

                public void setTaskName(String taskName) {
                    this.taskName = taskName;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public void setTaskStatus(String taskStatus) {
                    this.taskStatus = taskStatus;
                }

                public String getDeviceType() {
                    return deviceType;
                }

                public String getFinishTime() {
                    return finishTime;
                }

                public String getRemark() {
                    return remark;
                }

                public String getInspector() {
                    return inspector;
                }

                public String getDeviceName() {
                    return deviceName;
                }

                public String getInspectorName() {
                    return inspectorName;
                }

                public String getDvcId() {
                    return dvcId;
                }

                public String getTaskCode() {
                    return taskCode;
                }

                public String getPlanTime() {
                    return planTime;
                }

                public String getSendNotice() {
                    return sendNotice;
                }

                public String getShouldTime() {
                    return shouldTime;
                }

                public String getTaskName() {
                    return taskName;
                }

                public int getId() {
                    return id;
                }

                public String getTaskStatus() {
                    return taskStatus;
                }
            }
        }
    }
}
