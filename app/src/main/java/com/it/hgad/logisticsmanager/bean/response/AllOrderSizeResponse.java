package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/3/21.
 */
public class AllOrderSizeResponse extends BaseReponse {
    /**
     * data : {"start":0,"finish":37,"mantance":0,"shave":1,"recieve":0}
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
         * start : 0
         * finish : 37
         * mantance : 0
         * shave : 1
         * recieve : 0
         */
        private int start;
        private int finish;
        private int mantance;
        private int shave;
        private int recieve;

        public void setStart(int start) {
            this.start = start;
        }

        public void setFinish(int finish) {
            this.finish = finish;
        }

        public void setMantance(int mantance) {
            this.mantance = mantance;
        }

        public void setShave(int shave) {
            this.shave = shave;
        }

        public void setRecieve(int recieve) {
            this.recieve = recieve;
        }

        public int getStart() {
            return start;
        }

        public int getFinish() {
            return finish;
        }

        public int getMantance() {
            return mantance;
        }

        public int getShave() {
            return shave;
        }

        public int getRecieve() {
            return recieve;
        }
    }

}
