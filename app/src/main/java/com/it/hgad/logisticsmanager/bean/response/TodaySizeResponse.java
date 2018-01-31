package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/5/19.
 */
public class TodaySizeResponse extends BaseReponse {


    /**
     * data : {"repair":[{"date":"2017-05-11T16:07:43","count":1},{"date":"2017-05-12T10:23:15","count":17},{"date":"2017-05-13T08:59:29","count":6},{"date":"2017-05-14T10:00:44","count":3},{"date":"2017-05-15T07:56:34","count":20},{"date":"2017-05-16T08:58:18","count":6},{"date":"2017-05-17T13:21:36","count":1},{"date":"2017-05-18T10:21:04","count":1}],"inspect":[{"date":"2017-05-11T16:07:43","count":1},{"date":"2017-05-12T10:23:15","count":17},{"date":"2017-05-13T08:59:29","count":6},{"date":"2017-05-14T10:00:44","count":3},{"date":"2017-05-15T07:56:34","count":20},{"date":"2017-05-16T08:58:18","count":6},{"date":"2017-05-17T13:21:36","count":1},{"date":"2017-05-18T10:21:04","count":1}]}
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
         * repair : [{"date":"2017-05-11T16:07:43","count":1},{"date":"2017-05-12T10:23:15","count":17},{"date":"2017-05-13T08:59:29","count":6},{"date":"2017-05-14T10:00:44","count":3},{"date":"2017-05-15T07:56:34","count":20},{"date":"2017-05-16T08:58:18","count":6},{"date":"2017-05-17T13:21:36","count":1},{"date":"2017-05-18T10:21:04","count":1}]
         * inspect : [{"date":"2017-05-11T16:07:43","count":1},{"date":"2017-05-12T10:23:15","count":17},{"date":"2017-05-13T08:59:29","count":6},{"date":"2017-05-14T10:00:44","count":3},{"date":"2017-05-15T07:56:34","count":20},{"date":"2017-05-16T08:58:18","count":6},{"date":"2017-05-17T13:21:36","count":1},{"date":"2017-05-18T10:21:04","count":1}]
         */
        private List<RepairEntity> repair;
        private List<InspectEntity> inspect;

        public void setRepair(List<RepairEntity> repair) {
            this.repair = repair;
        }

        public void setInspect(List<InspectEntity> inspect) {
            this.inspect = inspect;
        }

        public List<RepairEntity> getRepair() {
            return repair;
        }

        public List<InspectEntity> getInspect() {
            return inspect;
        }

        public class RepairEntity {
            /**
             * date : 2017-05-11T16:07:43
             * count : 1
             */
            private String date;
            private int count;

            public void setDate(String date) {
                this.date = date;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getDate() {
                return date;
            }

            public int getCount() {
                return count;
            }
        }

        public class InspectEntity {
            /**
             * date : 2017-05-11T16:07:43
             * count : 1
             */
            private String date;
            private int count;

            public void setDate(String date) {
                this.date = date;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getDate() {
                return date;
            }

            public int getCount() {
                return count;
            }
        }
    }
}
