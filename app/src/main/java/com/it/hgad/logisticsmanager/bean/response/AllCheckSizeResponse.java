package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/3/21.
 */
public class AllCheckSizeResponse extends BaseReponse {
    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public class DataEntity {

        private int shouldCheck;
        private int doneCheck;
        private int overCheck;
        private int overFinishCheck;

        public int getShouldCheck() {
            return shouldCheck;
        }

        public void setShouldCheck(int shouldCheck) {
            this.shouldCheck = shouldCheck;
        }

        public int getDoneCheck() {
            return doneCheck;
        }

        public void setDoneCheck(int doneCheck) {
            this.doneCheck = doneCheck;
        }

        public int getOverCheck() {
            return overCheck;
        }

        public void setOverCheck(int overCheck) {
            this.overCheck = overCheck;
        }

        public int getOverFinishCheck() {
            return overFinishCheck;
        }

        public void setOverFinishCheck(int overFinishCheck) {
            this.overFinishCheck = overFinishCheck;
        }
    }
}
