package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/1/11.
 */
public class MaterialResponse extends BaseReponse {


    /**
     * data : {"perpage":10,"recordcount":14,"firstpage":1,"pagecount":2,"currentpage":2,"lastpage":2,"listcount":4,"nextpage":2,"dyntitles":null,"listdata":[{"belongType":null,"materialName":"灯管一","applier":"333","balanceNum":"110","materialType":{"name":"白炽灯","remark":"白炽灯","id":2},"specification":"1","id":12,"materialPrice":"2","operTime":"2017-01-17T14:53:50"},{"belongType":null,"materialName":"dd","applier":"e","balanceNum":"6","materialType":{"name":"白炽灯","remark":"白炽灯","id":2},"specification":"3","id":13,"materialPrice":"1","operTime":"2017-01-17T14:53:50"},{"belongType":null,"materialName":"1","applier":"d2","balanceNum":"0","materialType":{"name":"白炽灯","remark":"白炽灯","id":2},"specification":"s","id":14,"materialPrice":"2","operTime":"2017-01-17T14:53:50"},{"belongType":"0","materialName":"11","applier":"","balanceNum":"1","materialType":{"name":"门具","remark":"门具","id":3},"specification":"","id":15,"materialPrice":"0","operTime":"2017-01-18T10:38:57"}],"prevpage":1,"nulldata":["","","","","",""],"dyncolsdata":null,"oddsize":true}
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
         * recordcount : 14
         * firstpage : 1
         * pagecount : 2
         * currentpage : 2
         * lastpage : 2
         * listcount : 4
         * nextpage : 2
         * dyntitles : null
         * listdata : [{"belongType":null,"materialName":"灯管一","applier":"333","balanceNum":"110","materialType":{"name":"白炽灯","remark":"白炽灯","id":2},"specification":"1","id":12,"materialPrice":"2","operTime":"2017-01-17T14:53:50"},{"belongType":null,"materialName":"dd","applier":"e","balanceNum":"6","materialType":{"name":"白炽灯","remark":"白炽灯","id":2},"specification":"3","id":13,"materialPrice":"1","operTime":"2017-01-17T14:53:50"},{"belongType":null,"materialName":"1","applier":"d2","balanceNum":"0","materialType":{"name":"白炽灯","remark":"白炽灯","id":2},"specification":"s","id":14,"materialPrice":"2","operTime":"2017-01-17T14:53:50"},{"belongType":"0","materialName":"11","applier":"","balanceNum":"1","materialType":{"name":"门具","remark":"门具","id":3},"specification":"","id":15,"materialPrice":"0","operTime":"2017-01-18T10:38:57"}]
         * prevpage : 1
         * nulldata : ["","","","","",""]
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
             * belongType : null
             * materialName : 灯管一
             * applier : 333
             * balanceNum : 110
             * materialType : {"name":"白炽灯","remark":"白炽灯","id":2}
             * specification : 1
             * id : 12
             * materialPrice : 2
             * operTime : 2017-01-17T14:53:50
             */
            private String belongType;
            private String materialName;
            private String applier;
            private String balanceNum;
            private MaterialTypeEntity materialType;
            private String specification;
            private int id;
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

            public String getMaterialPrice() {
                return materialPrice;
            }

            public String getOperTime() {
                return operTime;
            }

            public class MaterialTypeEntity {
                /**
                 * name : 白炽灯
                 * remark : 白炽灯
                 * id : 2
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
