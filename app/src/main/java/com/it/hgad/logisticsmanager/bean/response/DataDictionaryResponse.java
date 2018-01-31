package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/3/23.
 */
public class DataDictionaryResponse extends BaseReponse{


    /**
     * data : {"perpage":-1,"recordcount":22,"firstpage":1,"pagecount":-22,"currentpage":1,"lastpage":-22,"listcount":22,"nextpage":-22,"dyntitles":null,"listdata":[{"code":"满意","typeName":"科室确认","remark":"调度","typeValue":"00001","id":1,"value":"1"},{"code":"待巡检","typeName":"巡检状态","remark":"","typeValue":"00002","id":2,"value":"0"},{"code":"巡检中","typeName":"巡检状态","remark":"","typeValue":"00002","id":3,"value":"1"},{"code":"已完工","typeName":"巡检状态","remark":"","typeValue":"00002","id":4,"value":"2"},{"code":"已超时","typeName":"巡检状态","remark":"","typeValue":"00002","id":5,"value":"3"},{"code":"正常","typeName":"巡检结果","remark":"","typeValue":"00003","id":6,"value":"0"},{"code":"异常","typeName":"巡检结果","remark":"","typeValue":"00003","id":7,"value":"1"},{"code":"新建","typeName":"维修状态","remark":"","typeValue":"00004","id":8,"value":"1"},{"code":"未接收","typeName":"维修状态","remark":"","typeValue":"00004","id":9,"value":"2"},{"code":"已接收","typeName":"维修状态","remark":"","typeValue":"00004","id":10,"value":"3"},{"code":"维修中","typeName":"维修状态","remark":"","typeValue":"00004","id":11,"value":"4"},{"code":"已挂起","typeName":"维修状态","remark":"","typeValue":"00004","id":12,"value":"5"},{"code":"已完工","typeName":"维修状态","remark":"","typeValue":"00004","id":13,"value":"6"},{"code":"已结单","typeName":"维修状态","remark":"","typeValue":"00004","id":14,"value":"7"},{"code":"超时补录","typeName":"巡检状态","remark":"","typeValue":"00002","id":15,"value":"4"},{"code":"正常","typeName":"参数状态","remark":"","typeValue":"00005","id":16,"value":"0"},{"code":"异常","typeName":"参数状态","remark":"","typeValue":"00005","id":17,"value":"1"},{"code":"已取消","typeName":"巡检状态","remark":"","typeValue":"00002","id":18,"value":"5"},{"code":"已处理","typeName":"巡检结果","remark":"","typeValue":"00003","id":19,"value":"2"},{"code":"已取消","typeName":"维修状态","remark":"","typeValue":"00004","id":20,"value":"0"},{"code":"发送成功","typeName":"发送状态","remark":"","typeValue":"00007","id":21,"value":"1"},{"code":"发送失败","typeName":"发送状态","remark":"","typeValue":"00007","id":22,"value":"0"}],"prevpage":1,"dyncolsdata":null,"oddsize":true}
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
         * perpage : -1
         * recordcount : 22
         * firstpage : 1
         * pagecount : -22
         * currentpage : 1
         * lastpage : -22
         * listcount : 22
         * nextpage : -22
         * dyntitles : null
         * listdata : [{"code":"满意","typeName":"科室确认","remark":"调度","typeValue":"00001","id":1,"value":"1"},{"code":"待巡检","typeName":"巡检状态","remark":"","typeValue":"00002","id":2,"value":"0"},{"code":"巡检中","typeName":"巡检状态","remark":"","typeValue":"00002","id":3,"value":"1"},{"code":"已完工","typeName":"巡检状态","remark":"","typeValue":"00002","id":4,"value":"2"},{"code":"已超时","typeName":"巡检状态","remark":"","typeValue":"00002","id":5,"value":"3"},{"code":"正常","typeName":"巡检结果","remark":"","typeValue":"00003","id":6,"value":"0"},{"code":"异常","typeName":"巡检结果","remark":"","typeValue":"00003","id":7,"value":"1"},{"code":"新建","typeName":"维修状态","remark":"","typeValue":"00004","id":8,"value":"1"},{"code":"未接收","typeName":"维修状态","remark":"","typeValue":"00004","id":9,"value":"2"},{"code":"已接收","typeName":"维修状态","remark":"","typeValue":"00004","id":10,"value":"3"},{"code":"维修中","typeName":"维修状态","remark":"","typeValue":"00004","id":11,"value":"4"},{"code":"已挂起","typeName":"维修状态","remark":"","typeValue":"00004","id":12,"value":"5"},{"code":"已完工","typeName":"维修状态","remark":"","typeValue":"00004","id":13,"value":"6"},{"code":"已结单","typeName":"维修状态","remark":"","typeValue":"00004","id":14,"value":"7"},{"code":"超时补录","typeName":"巡检状态","remark":"","typeValue":"00002","id":15,"value":"4"},{"code":"正常","typeName":"参数状态","remark":"","typeValue":"00005","id":16,"value":"0"},{"code":"异常","typeName":"参数状态","remark":"","typeValue":"00005","id":17,"value":"1"},{"code":"已取消","typeName":"巡检状态","remark":"","typeValue":"00002","id":18,"value":"5"},{"code":"已处理","typeName":"巡检结果","remark":"","typeValue":"00003","id":19,"value":"2"},{"code":"已取消","typeName":"维修状态","remark":"","typeValue":"00004","id":20,"value":"0"},{"code":"发送成功","typeName":"发送状态","remark":"","typeValue":"00007","id":21,"value":"1"},{"code":"发送失败","typeName":"发送状态","remark":"","typeValue":"00007","id":22,"value":"0"}]
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

        public class ListdataEntity {
            /**
             * code : 满意
             * typeName : 科室确认
             * remark : 调度
             * typeValue : 00001
             * id : 1
             * value : 1
             */
            private String code;
            private String typeName;
            private String remark;
            private String typeValue;
            private int id;
            private String value;

            public void setCode(String code) {
                this.code = code;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setTypeValue(String typeValue) {
                this.typeValue = typeValue;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getCode() {
                return code;
            }

            public String getTypeName() {
                return typeName;
            }

            public String getRemark() {
                return remark;
            }

            public String getTypeValue() {
                return typeValue;
            }

            public int getId() {
                return id;
            }

            public String getValue() {
                return value;
            }
        }
    }
}
