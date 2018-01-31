package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/1/17.
 */
public class JobDetailResponse extends BaseReponse {


    /**
     * data : [{"repair":{"repairMan":"杨迪","repairTel":"84309797","recieveTime":"2017-04-20T16:51:16","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",47,","startTime":"2017-04-20T16:51:17","id":272,"repairSrc":"电话报修","repairReply":"rrr","finishTime":"2017-04-20T16:52:56","registerTime":"2017-04-20T16:51:21","satisfy":"需改进","repairType":"检修","eventType":"门窗","repairNo":"2017042000004","registerMan":"Admin","sendTime":"2017-04-20T16:51:37","repairContent":"tttt","sameCondition":null,"userNames":"吴含","delayTime":null,"repairDept":"住院药房","repairImg":"","spotImg":"e35ebe8d-0260-42a0-813d-64f7440367bd.png","signImg":"","repairLoc":"住院部1楼西,护理站"},"materialInfo":{"belongType":"1","materialName":"紫外线杀菌灯","applier":"","balanceNum":"-1","materialType":{"name":"照明","remark":"","id":10},"specification":"40W","id":129,"materialUnit":"支","warnValue":null,"materialPrice":"0","operTime":"2017-03-02T11:03:45"},"optNum":"-1","currentNum":"-1","remark":"维修使用","id":17,"optTime":"2017-04-20T16:53:21"},{"repair":{"repairMan":"杨迪","repairTel":"84309797","recieveTime":"2017-04-20T16:51:16","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",47,","startTime":"2017-04-20T16:51:17","id":272,"repairSrc":"电话报修","repairReply":"rrr","finishTime":"2017-04-20T16:52:56","registerTime":"2017-04-20T16:51:21","satisfy":"需改进","repairType":"检修","eventType":"门窗","repairNo":"2017042000004","registerMan":"Admin","sendTime":"2017-04-20T16:51:37","repairContent":"tttt","sameCondition":null,"userNames":"吴含","delayTime":null,"repairDept":"住院药房","repairImg":"","spotImg":"e35ebe8d-0260-42a0-813d-64f7440367bd.png","signImg":"","repairLoc":"住院部1楼西,护理站"},"materialInfo":{"belongType":"1","materialName":"节能灯泡","applier":"","balanceNum":"-19","materialType":{"name":"照明","remark":"","id":10},"specification":"5W","id":131,"materialUnit":"个","warnValue":null,"materialPrice":"0","operTime":"2017-03-02T11:03:58"},"optNum":"-  1","currentNum":"-19","remark":"维修使用","id":18,"optTime":"2017-04-20T16:53:22"},{"repair":{"repairMan":"杨迪","repairTel":"84309797","recieveTime":"2017-04-20T16:51:16","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",47,","startTime":"2017-04-20T16:51:17","id":272,"repairSrc":"电话报修","repairReply":"rrr","finishTime":"2017-04-20T16:52:56","registerTime":"2017-04-20T16:51:21","satisfy":"需改进","repairType":"检修","eventType":"门窗","repairNo":"2017042000004","registerMan":"Admin","sendTime":"2017-04-20T16:51:37","repairContent":"tttt","sameCondition":null,"userNames":"吴含","delayTime":null,"repairDept":"住院药房","repairImg":"","spotImg":"e35ebe8d-0260-42a0-813d-64f7440367bd.png","signImg":"","repairLoc":"住院部1楼西,护理站"},"materialInfo":{"belongType":"1","materialName":"节能灯 ","applier":"","balanceNum":"32","materialType":{"name":"照明","remark":"","id":10},"specification":"13W","id":132,"materialUnit":"个","warnValue":null,"materialPrice":"0","operTime":"2017-03-02T11:04:18"},"optNum":"-  1","currentNum":"32","remark":"维修使用","id":19,"optTime":"2017-04-20T16:53:22"}]
     */
    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public class DataEntity {
        /**
         * repair : {"repairMan":"杨迪","repairTel":"84309797","recieveTime":"2017-04-20T16:51:16","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",47,","startTime":"2017-04-20T16:51:17","id":272,"repairSrc":"电话报修","repairReply":"rrr","finishTime":"2017-04-20T16:52:56","registerTime":"2017-04-20T16:51:21","satisfy":"需改进","repairType":"检修","eventType":"门窗","repairNo":"2017042000004","registerMan":"Admin","sendTime":"2017-04-20T16:51:37","repairContent":"tttt","sameCondition":null,"userNames":"吴含","delayTime":null,"repairDept":"住院药房","repairImg":"","spotImg":"e35ebe8d-0260-42a0-813d-64f7440367bd.png","signImg":"","repairLoc":"住院部1楼西,护理站"}
         * materialInfo : {"belongType":"1","materialName":"紫外线杀菌灯","applier":"","balanceNum":"-1","materialType":{"name":"照明","remark":"","id":10},"specification":"40W","id":129,"materialUnit":"支","warnValue":null,"materialPrice":"0","operTime":"2017-03-02T11:03:45"}
         * optNum : -1
         * currentNum : -1
         * remark : 维修使用
         * id : 17
         * optTime : 2017-04-20T16:53:21
         */
        private RepairEntity repair;
        private MaterialInfoEntity materialInfo;
        private String optNum;
        private String currentNum;
        private String remark;
        private int id;
        private String optTime;

        public void setRepair(RepairEntity repair) {
            this.repair = repair;
        }

        public void setMaterialInfo(MaterialInfoEntity materialInfo) {
            this.materialInfo = materialInfo;
        }

        public void setOptNum(String optNum) {
            this.optNum = optNum;
        }

        public void setCurrentNum(String currentNum) {
            this.currentNum = currentNum;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setOptTime(String optTime) {
            this.optTime = optTime;
        }

        public RepairEntity getRepair() {
            return repair;
        }

        public MaterialInfoEntity getMaterialInfo() {
            return materialInfo;
        }

        public String getOptNum() {
            return optNum;
        }

        public String getCurrentNum() {
            return currentNum;
        }

        public String getRemark() {
            return remark;
        }

        public int getId() {
            return id;
        }

        public String getOptTime() {
            return optTime;
        }

        public class RepairEntity {
            /**
             * repairMan : 杨迪
             * repairTel : 84309797
             * recieveTime : 2017-04-20T16:51:16
             * shelveTime : null
             * repairFlag : 6
             * shelveReason : null
             * userIds : ,47,
             * startTime : 2017-04-20T16:51:17
             * id : 272
             * repairSrc : 电话报修
             * repairReply : rrr
             * finishTime : 2017-04-20T16:52:56
             * registerTime : 2017-04-20T16:51:21
             * satisfy : 需改进
             * repairType : 检修
             * eventType : 门窗
             * repairNo : 2017042000004
             * registerMan : Admin
             * sendTime : 2017-04-20T16:51:37
             * repairContent : tttt
             * sameCondition : null
             * userNames : 吴含
             * delayTime : null
             * repairDept : 住院药房
             * repairImg :
             * spotImg : e35ebe8d-0260-42a0-813d-64f7440367bd.png
             * signImg :
             * repairLoc : 住院部1楼西,护理站
             */
            private String repairMan;
            private String repairTel;
            private String recieveTime;
            private String shelveTime;
            private int repairFlag;
            private String shelveReason;
            private String userIds;
            private String startTime;
            private int id;
            private String repairSrc;
            private String repairReply;
            private String finishTime;
            private String registerTime;
            private String satisfy;
            private String repairType;
            private String eventType;
            private String repairNo;
            private String registerMan;
            private String sendTime;
            private String repairContent;
            private String sameCondition;
            private String userNames;
            private String delayTime;
            private String repairDept;
            private String repairImg;
            private String spotImg;
            private String signImg;
            private String repairLoc;

            public void setRepairMan(String repairMan) {
                this.repairMan = repairMan;
            }

            public void setRepairTel(String repairTel) {
                this.repairTel = repairTel;
            }

            public void setRecieveTime(String recieveTime) {
                this.recieveTime = recieveTime;
            }

            public void setShelveTime(String shelveTime) {
                this.shelveTime = shelveTime;
            }

            public void setRepairFlag(int repairFlag) {
                this.repairFlag = repairFlag;
            }

            public void setShelveReason(String shelveReason) {
                this.shelveReason = shelveReason;
            }

            public void setUserIds(String userIds) {
                this.userIds = userIds;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setRepairSrc(String repairSrc) {
                this.repairSrc = repairSrc;
            }

            public void setRepairReply(String repairReply) {
                this.repairReply = repairReply;
            }

            public void setFinishTime(String finishTime) {
                this.finishTime = finishTime;
            }

            public void setRegisterTime(String registerTime) {
                this.registerTime = registerTime;
            }

            public void setSatisfy(String satisfy) {
                this.satisfy = satisfy;
            }

            public void setRepairType(String repairType) {
                this.repairType = repairType;
            }

            public void setEventType(String eventType) {
                this.eventType = eventType;
            }

            public void setRepairNo(String repairNo) {
                this.repairNo = repairNo;
            }

            public void setRegisterMan(String registerMan) {
                this.registerMan = registerMan;
            }

            public void setSendTime(String sendTime) {
                this.sendTime = sendTime;
            }

            public void setRepairContent(String repairContent) {
                this.repairContent = repairContent;
            }

            public void setSameCondition(String sameCondition) {
                this.sameCondition = sameCondition;
            }

            public void setUserNames(String userNames) {
                this.userNames = userNames;
            }

            public void setDelayTime(String delayTime) {
                this.delayTime = delayTime;
            }

            public void setRepairDept(String repairDept) {
                this.repairDept = repairDept;
            }

            public void setRepairImg(String repairImg) {
                this.repairImg = repairImg;
            }

            public void setSpotImg(String spotImg) {
                this.spotImg = spotImg;
            }

            public void setSignImg(String signImg) {
                this.signImg = signImg;
            }

            public void setRepairLoc(String repairLoc) {
                this.repairLoc = repairLoc;
            }

            public String getRepairMan() {
                return repairMan;
            }

            public String getRepairTel() {
                return repairTel;
            }

            public String getRecieveTime() {
                return recieveTime;
            }

            public String getShelveTime() {
                return shelveTime;
            }

            public int getRepairFlag() {
                return repairFlag;
            }

            public String getShelveReason() {
                return shelveReason;
            }

            public String getUserIds() {
                return userIds;
            }

            public String getStartTime() {
                return startTime;
            }

            public int getId() {
                return id;
            }

            public String getRepairSrc() {
                return repairSrc;
            }

            public String getRepairReply() {
                return repairReply;
            }

            public String getFinishTime() {
                return finishTime;
            }

            public String getRegisterTime() {
                return registerTime;
            }

            public String getSatisfy() {
                return satisfy;
            }

            public String getRepairType() {
                return repairType;
            }

            public String getEventType() {
                return eventType;
            }

            public String getRepairNo() {
                return repairNo;
            }

            public String getRegisterMan() {
                return registerMan;
            }

            public String getSendTime() {
                return sendTime;
            }

            public String getRepairContent() {
                return repairContent;
            }

            public String getSameCondition() {
                return sameCondition;
            }

            public String getUserNames() {
                return userNames;
            }

            public String getDelayTime() {
                return delayTime;
            }

            public String getRepairDept() {
                return repairDept;
            }

            public String getRepairImg() {
                return repairImg;
            }

            public String getSpotImg() {
                return spotImg;
            }

            public String getSignImg() {
                return signImg;
            }

            public String getRepairLoc() {
                return repairLoc;
            }
        }

        public class MaterialInfoEntity {
            /**
             * belongType : 1
             * materialName : 紫外线杀菌灯
             * applier :
             * balanceNum : -1
             * materialType : {"name":"照明","remark":"","id":10}
             * specification : 40W
             * id : 129
             * materialUnit : 支
             * warnValue : null
             * materialPrice : 0
             * operTime : 2017-03-02T11:03:45
             */
            private String belongType;
            private String materialName;
            private String applier;
            private String balanceNum;
            private MaterialTypeEntity materialType;
            private String specification;
            private int id;
            private String materialUnit;
            private String warnValue;
            private String materialPrice;
            private String operTime;

            public void setBelongType(String belongType) {
                this.belongType = belongType;
            }

            public void setMaterialName(String materialName) {
                this.materialName = materialName;
            }

            public void setApplier(String applier) {
                this.applier = applier;
            }

            public void setBalanceNum(String balanceNum) {
                this.balanceNum = balanceNum;
            }

            public void setMaterialType(MaterialTypeEntity materialType) {
                this.materialType = materialType;
            }

            public void setSpecification(String specification) {
                this.specification = specification;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setMaterialUnit(String materialUnit) {
                this.materialUnit = materialUnit;
            }

            public void setWarnValue(String warnValue) {
                this.warnValue = warnValue;
            }

            public void setMaterialPrice(String materialPrice) {
                this.materialPrice = materialPrice;
            }

            public void setOperTime(String operTime) {
                this.operTime = operTime;
            }

            public String getBelongType() {
                return belongType;
            }

            public String getMaterialName() {
                return materialName;
            }

            public String getApplier() {
                return applier;
            }

            public String getBalanceNum() {
                return balanceNum;
            }

            public MaterialTypeEntity getMaterialType() {
                return materialType;
            }

            public String getSpecification() {
                return specification;
            }

            public int getId() {
                return id;
            }

            public String getMaterialUnit() {
                return materialUnit;
            }

            public String getWarnValue() {
                return warnValue;
            }

            public String getMaterialPrice() {
                return materialPrice;
            }

            public String getOperTime() {
                return operTime;
            }

            public class MaterialTypeEntity {
                /**
                 * name : 照明
                 * remark :
                 * id : 10
                 */
                private String name;
                private String remark;
                private int id;

                public void setName(String name) {
                    this.name = name;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public String getRemark() {
                    return remark;
                }

                public int getId() {
                    return id;
                }
            }
        }
    }
}
