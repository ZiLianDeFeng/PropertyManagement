package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/4/25.
 */
public class TemporaryResponse extends BaseReponse {

    /**
     * result : 0
     * data : {"perpage":10,"recordcount":1,"firstpage":1,"pagecount":1,"currentpage":1,"lastpage":1,"listcount":1,"nextpage":1,"dyntitles":null,"listdata":[{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"}],"prevpage":1,"dyncolsdata":null,"oddsize":false}
     */
    private String result;
    private DataEntity data;

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public DataEntity getData() {
        return data;
    }

    public class DataEntity {
        /**
         * perpage : 10
         * recordcount : 1
         * firstpage : 1
         * pagecount : 1
         * currentpage : 1
         * lastpage : 1
         * listcount : 1
         * nextpage : 1
         * dyntitles : null
         * listdata : [{"deviceType":"空调","finishTime":null,"remark":"闪闪的红星","inspector":"17","deviceName":"风冷热泵机组1","inspectorName":"杨少华","dvcId":"16","taskCode":"20170407","planTime":"2017-04-07T09:19:39","sendNotice":null,"shouldTime":"2017-04-07T09:19:39","taskName":"临时巡检","id":5,"taskStatus":"0"}]
         * prevpage : 1
         * dyncolsdata : null
         * oddsize : false
         */
        private int perpage;
        private int recordcount;
        private int firstpage;
        private int pagecount;
        private int currentpage;
        private int lastpage;
        private int listcount;
        private int nextpage;
        private String dyntitles;
        private List<ListdataEntity> listdata;
        private int prevpage;
        private String dyncolsdata;
        private boolean oddsize;

        public void setPerpage(int perpage) {
            this.perpage = perpage;
        }

        public void setRecordcount(int recordcount) {
            this.recordcount = recordcount;
        }

        public void setFirstpage(int firstpage) {
            this.firstpage = firstpage;
        }

        public void setPagecount(int pagecount) {
            this.pagecount = pagecount;
        }

        public void setCurrentpage(int currentpage) {
            this.currentpage = currentpage;
        }

        public void setLastpage(int lastpage) {
            this.lastpage = lastpage;
        }

        public void setListcount(int listcount) {
            this.listcount = listcount;
        }

        public void setNextpage(int nextpage) {
            this.nextpage = nextpage;
        }

        public void setDyntitles(String dyntitles) {
            this.dyntitles = dyntitles;
        }

        public void setListdata(List<ListdataEntity> listdata) {
            this.listdata = listdata;
        }

        public void setPrevpage(int prevpage) {
            this.prevpage = prevpage;
        }

        public void setDyncolsdata(String dyncolsdata) {
            this.dyncolsdata = dyncolsdata;
        }

        public void setOddsize(boolean oddsize) {
            this.oddsize = oddsize;
        }

        public int getPerpage() {
            return perpage;
        }

        public int getRecordcount() {
            return recordcount;
        }

        public int getFirstpage() {
            return firstpage;
        }

        public int getPagecount() {
            return pagecount;
        }

        public int getCurrentpage() {
            return currentpage;
        }

        public int getLastpage() {
            return lastpage;
        }

        public int getListcount() {
            return listcount;
        }

        public int getNextpage() {
            return nextpage;
        }

        public String getDyntitles() {
            return dyntitles;
        }

        public List<ListdataEntity> getListdata() {
            return listdata;
        }

        public int getPrevpage() {
            return prevpage;
        }

        public String getDyncolsdata() {
            return dyncolsdata;
        }

        public boolean isOddsize() {
            return oddsize;
        }

        public class ListdataEntity {
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
