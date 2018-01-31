package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/5/16.
 */
public class MaintainCensusResponse extends BaseReponse {

    /**
     * data : [{"count":319,"username":" 姚庆华"},{"count":319,"username":" 杨少华"},{"count":116,"username":" 毕波"},{"count":55,"username":" 李斌"},{"count":55,"username":" 侯胜利"},{"count":44,"username":" 黄金平"},{"count":44,"username":" 魏迎善"},{"count":20,"username":" 王建平"},{"count":16,"username":" 阮国春"},{"count":8,"username":" 杨恒之"},{"count":8,"username":" 王斌"}]
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
         * count : 319
         * username :  姚庆华
         */
        private int count;
        private String username;

        public void setCount(int count) {
            this.count = count;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getCount() {
            return count;
        }

        public String getUsername() {
            return username;
        }
    }
}
