package com.it.hgad.logisticsmanager.bean.response;

import java.io.Serializable;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/1/9.
 */
public class OrderListResponse extends BaseReponse implements Serializable{


    /**
     * data : {"perpage":10,"recordcount":26,"firstpage":1,"pagecount":3,"currentpage":1,"lastpage":3,"listcount":10,"nextpage":2,"dyntitles":null,"listdata":[{"repairMan":"徐徐","repairTel":"84309799","recieveTime":"2017-02-21T13:41:50","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-21T13:41:53","id":41,"repairSrc":"电话报修","repairReply":"haikeyi","finishTime":"2017-02-21T15:04:46","registerTime":"2017-02-21T13:40:26","satisfy":"需改进","repairType":"检修","eventType":"门窗","repairNo":"20170221134101","registerMan":"","sendTime":"2017-02-21T13:41:42","repairContent":"有问题","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"20170221150354.png|20170221150406.png","spotImg":"20170221150340.png, 20170221150348.png","repairLoc":"住院部负1楼,护理站"},{"repairMan":"吴晗","repairTel":"84309803","recieveTime":"2017-02-20T15:45:56","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-20T15:47:50","id":39,"repairSrc":"电话报修","repairReply":"hhhh","finishTime":"2017-02-20T16:07:11","registerTime":"2017-02-20T15:43:35","satisfy":"不满意","repairType":"检修","eventType":"门窗","repairNo":"20170220154352","registerMan":"Admin","sendTime":"2017-02-20T15:43:58","repairContent":"门窗坏了","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"20170220160647.png|20170220160702.png","spotImg":null,"repairLoc":"住院部负1楼,护士长办公室"},{"repairMan":"吴晗","repairTel":"84309799","recieveTime":"2017-02-20T15:42:30","shelveTime":"2017-02-20T16:18:34","repairFlag":6,"shelveReason":"youshi","userIds":",4,","startTime":"2017-02-20T17:00:22","id":38,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-21T10:34:10","registerTime":"2017-02-20T15:29:45","satisfy":"需改进","repairType":"检修","eventType":"电","repairNo":"20170220153007","registerMan":"Admin","sendTime":"2017-02-20T15:34:15","repairContent":"电器有问题","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"","spotImg":null,"repairLoc":"住院部负1楼,护理站"},{"repairMan":"123","repairTel":"84309803","recieveTime":"2017-02-20T13:50:15","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-20T13:50:33","id":37,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-20T13:51:21","registerTime":"2017-02-20T11:30:42","satisfy":"满意","repairType":"检修","eventType":"电","repairNo":"20170220113051","registerMan":"Admin","sendTime":"2017-02-20T11:30:57","repairContent":"123","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"20170220135109.png|20170220135115.png","spotImg":null,"repairLoc":"住院部负1楼,主任办公室"},{"repairMan":"456","repairTel":"84309797","recieveTime":"2017-02-20T11:48:07","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-20T14:01:40","id":35,"repairSrc":"电话报修","repairReply":"2313","finishTime":"2017-02-20T15:33:41","registerTime":"2017-02-20T11:06:28","satisfy":"不满意","repairType":"检修","eventType":"电","repairNo":"20170220110639","registerMan":"Admin","sendTime":"2017-02-20T11:06:42","repairContent":"456","sameCondition":null,"userNames":"凌永平","repairDept":"住院药房","repairImg":"","spotImg":null,"repairLoc":"住院部1楼西,护理站"},{"repairMan":"微微威武","repairTel":"88883333","recieveTime":"2017-02-17T09:32:48","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-17T09:37:51","id":28,"repairSrc":"电话报修","repairReply":"12312","finishTime":"2017-02-17T10:06:20","registerTime":"2017-02-16T17:28:41","satisfy":null,"repairType":"检修","eventType":"水","repairNo":"20170216172849","registerMan":"Admin","sendTime":"2017-02-16T17:28:53","repairContent":"外网","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"我我","repairTel":"88883333","recieveTime":"2017-02-17T09:37:30","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-17T10:02:08","id":27,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-17T10:10:02","registerTime":"2017-02-16T17:24:26","satisfy":null,"repairType":"检修","eventType":"水","repairNo":"20170216172437","registerMan":"Admin","sendTime":"2017-02-16T17:24:49","repairContent":"我我","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"外网","repairTel":"88883333","recieveTime":"2017-02-17T09:37:39","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-17T10:22:55","id":26,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-17T13:38:08","registerTime":"2017-02-16T17:24:14","satisfy":"满意","repairType":"检修","eventType":"水","repairNo":"20170216172424","registerMan":"Admin","sendTime":"2017-02-16T17:24:51","repairContent":"我","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"wh","repairTel":"88883333","recieveTime":"2017-02-16T16:47:59","shelveTime":"2017-02-16T17:54:07","repairFlag":6,"shelveReason":"fdsafasdfasdf","userIds":",4,","startTime":"2017-02-17T14:40:15","id":25,"repairSrc":"电话报修","repairReply":"31231","finishTime":"2017-02-17T14:40:27","registerTime":"2017-02-16T16:35:14","satisfy":"不满意","repairType":"检修","eventType":"水","repairNo":"20170216163536","registerMan":"Admin","sendTime":"2017-02-16T16:36:14","repairContent":"youwenti","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"dasd","repairTel":"88883333","recieveTime":"2017-02-17T10:11:15","shelveTime":"2017-02-17T14:40:36","repairFlag":6,"shelveReason":"123","userIds":",4,","startTime":"2017-02-17T15:20:19","id":24,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-20T10:32:25","registerTime":"2017-02-16T16:24:34","satisfy":"满意","repairType":"检修","eventType":"水","repairNo":"20170216162443","registerMan":"Admin","sendTime":"2017-02-16T16:48:09","repairContent":"dasd1","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"20170220103149.png|20170220103159.png","spotImg":null,"repairLoc":"骨科主任室301"}],"prevpage":1,"dyncolsdata":null,"oddsize":true}
     */
    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public class DataEntity {
        /**
         * perpage : 10
         * recordcount : 26
         * firstpage : 1
         * pagecount : 3
         * currentpage : 1
         * lastpage : 3
         * listcount : 10
         * nextpage : 2
         * dyntitles : null
         * listdata : [{"repairMan":"徐徐","repairTel":"84309799","recieveTime":"2017-02-21T13:41:50","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-21T13:41:53","id":41,"repairSrc":"电话报修","repairReply":"haikeyi","finishTime":"2017-02-21T15:04:46","registerTime":"2017-02-21T13:40:26","satisfy":"需改进","repairType":"检修","eventType":"门窗","repairNo":"20170221134101","registerMan":"","sendTime":"2017-02-21T13:41:42","repairContent":"有问题","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"20170221150354.png|20170221150406.png","spotImg":"20170221150340.png, 20170221150348.png","repairLoc":"住院部负1楼,护理站"},{"repairMan":"吴晗","repairTel":"84309803","recieveTime":"2017-02-20T15:45:56","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-20T15:47:50","id":39,"repairSrc":"电话报修","repairReply":"hhhh","finishTime":"2017-02-20T16:07:11","registerTime":"2017-02-20T15:43:35","satisfy":"不满意","repairType":"检修","eventType":"门窗","repairNo":"20170220154352","registerMan":"Admin","sendTime":"2017-02-20T15:43:58","repairContent":"门窗坏了","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"20170220160647.png|20170220160702.png","spotImg":null,"repairLoc":"住院部负1楼,护士长办公室"},{"repairMan":"吴晗","repairTel":"84309799","recieveTime":"2017-02-20T15:42:30","shelveTime":"2017-02-20T16:18:34","repairFlag":6,"shelveReason":"youshi","userIds":",4,","startTime":"2017-02-20T17:00:22","id":38,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-21T10:34:10","registerTime":"2017-02-20T15:29:45","satisfy":"需改进","repairType":"检修","eventType":"电","repairNo":"20170220153007","registerMan":"Admin","sendTime":"2017-02-20T15:34:15","repairContent":"电器有问题","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"","spotImg":null,"repairLoc":"住院部负1楼,护理站"},{"repairMan":"123","repairTel":"84309803","recieveTime":"2017-02-20T13:50:15","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-20T13:50:33","id":37,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-20T13:51:21","registerTime":"2017-02-20T11:30:42","satisfy":"满意","repairType":"检修","eventType":"电","repairNo":"20170220113051","registerMan":"Admin","sendTime":"2017-02-20T11:30:57","repairContent":"123","sameCondition":null,"userNames":"凌永平","repairDept":"供应室","repairImg":"20170220135109.png|20170220135115.png","spotImg":null,"repairLoc":"住院部负1楼,主任办公室"},{"repairMan":"456","repairTel":"84309797","recieveTime":"2017-02-20T11:48:07","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-20T14:01:40","id":35,"repairSrc":"电话报修","repairReply":"2313","finishTime":"2017-02-20T15:33:41","registerTime":"2017-02-20T11:06:28","satisfy":"不满意","repairType":"检修","eventType":"电","repairNo":"20170220110639","registerMan":"Admin","sendTime":"2017-02-20T11:06:42","repairContent":"456","sameCondition":null,"userNames":"凌永平","repairDept":"住院药房","repairImg":"","spotImg":null,"repairLoc":"住院部1楼西,护理站"},{"repairMan":"微微威武","repairTel":"88883333","recieveTime":"2017-02-17T09:32:48","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-17T09:37:51","id":28,"repairSrc":"电话报修","repairReply":"12312","finishTime":"2017-02-17T10:06:20","registerTime":"2017-02-16T17:28:41","satisfy":null,"repairType":"检修","eventType":"水","repairNo":"20170216172849","registerMan":"Admin","sendTime":"2017-02-16T17:28:53","repairContent":"外网","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"我我","repairTel":"88883333","recieveTime":"2017-02-17T09:37:30","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-17T10:02:08","id":27,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-17T10:10:02","registerTime":"2017-02-16T17:24:26","satisfy":null,"repairType":"检修","eventType":"水","repairNo":"20170216172437","registerMan":"Admin","sendTime":"2017-02-16T17:24:49","repairContent":"我我","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"外网","repairTel":"88883333","recieveTime":"2017-02-17T09:37:39","shelveTime":null,"repairFlag":6,"shelveReason":null,"userIds":",4,","startTime":"2017-02-17T10:22:55","id":26,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-17T13:38:08","registerTime":"2017-02-16T17:24:14","satisfy":"满意","repairType":"检修","eventType":"水","repairNo":"20170216172424","registerMan":"Admin","sendTime":"2017-02-16T17:24:51","repairContent":"我","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"wh","repairTel":"88883333","recieveTime":"2017-02-16T16:47:59","shelveTime":"2017-02-16T17:54:07","repairFlag":6,"shelveReason":"fdsafasdfasdf","userIds":",4,","startTime":"2017-02-17T14:40:15","id":25,"repairSrc":"电话报修","repairReply":"31231","finishTime":"2017-02-17T14:40:27","registerTime":"2017-02-16T16:35:14","satisfy":"不满意","repairType":"检修","eventType":"水","repairNo":"20170216163536","registerMan":"Admin","sendTime":"2017-02-16T16:36:14","repairContent":"youwenti","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"","spotImg":null,"repairLoc":"骨科主任室301"},{"repairMan":"dasd","repairTel":"88883333","recieveTime":"2017-02-17T10:11:15","shelveTime":"2017-02-17T14:40:36","repairFlag":6,"shelveReason":"123","userIds":",4,","startTime":"2017-02-17T15:20:19","id":24,"repairSrc":"电话报修","repairReply":"123","finishTime":"2017-02-20T10:32:25","registerTime":"2017-02-16T16:24:34","satisfy":"满意","repairType":"检修","eventType":"水","repairNo":"20170216162443","registerMan":"Admin","sendTime":"2017-02-16T16:48:09","repairContent":"dasd1","sameCondition":null,"userNames":"凌永平","repairDept":"骨科","repairImg":"20170220103149.png|20170220103159.png","spotImg":null,"repairLoc":"骨科主任室301"}]
         * prevpage : 1
         * dyncolsdata : null
         * oddsize : true
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

        public class ListdataEntity implements Serializable{
           /* *
             * repairMan : 徐徐
             * repairTel : 84309799
             * recieveTime : 2017-02-21T13:41:50
             * shelveTime : null
             * repairFlag : 6
             * shelveReason : null
             * userIds : ,4,
             * startTime : 2017-02-21T13:41:53
             * id : 41
             * repairSrc : 电话报修
             * repairReply : haikeyi
             * finishTime : 2017-02-21T15:04:46
             * registerTime : 2017-02-21T13:40:26
             * satisfy : 需改进
             * repairType : 检修
             * eventType : 门窗
             * repairNo : 20170221134101
             * registerMan :
             * sendTime : 2017-02-21T13:41:42
             * repairContent : 有问题
             * sameCondition : null
             * userNames : 凌永平
             * repairDept : 供应室
             * repairImg : 20170221150354.png|20170221150406.png
             * spotImg : 20170221150340.png, 20170221150348.png
             * repairLoc : 住院部负1楼,护理站*/
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
            private String repairDept;
            private String repairImg;
            private String spotImg;
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

            public void setRepairDept(String repairDept) {
                this.repairDept = repairDept;
            }

            public void setRepairImg(String repairImg) {
                this.repairImg = repairImg;
            }

            public void setSpotImg(String spotImg) {
                this.spotImg = spotImg;
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

            public String getRepairDept() {
                return repairDept;
            }

            public String getRepairImg() {
                return repairImg;
            }

            public String getSpotImg() {
                return spotImg;
            }

            public String getRepairLoc() {
                return repairLoc;
            }
        }
    }
}
