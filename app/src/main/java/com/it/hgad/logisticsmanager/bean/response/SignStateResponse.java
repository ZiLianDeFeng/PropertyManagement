package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/2/10.
 */
public class SignStateResponse extends BaseReponse {


    /**
     * result : 0
     * data : [{"inTime":"2017-02-16T13:35:13","id":17,"currentState":"已签退","user":{"userFlag":0,"realName":"凌永平","tskNum":null,"inspectState":"0","repairState":"0","userPass":"lingyongping","userTel":"15071481976","id":4,"userName":"lingyongping"},"outTime":"2017-02-16T13:35:19"}]
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
         * inTime : 2017-02-16T13:35:13
         * id : 17
         * currentState : 已签退
         * user : {"userFlag":0,"realName":"凌永平","tskNum":null,"inspectState":"0","repairState":"0","userPass":"lingyongping","userTel":"15071481976","id":4,"userName":"lingyongping"}
         * outTime : 2017-02-16T13:35:19
         */
        private String inTime;
        private int id;
        private String currentState;
        private UserEntity user;
        private String outTime;

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCurrentState(String currentState) {
            this.currentState = currentState;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setOutTime(String outTime) {
            this.outTime = outTime;
        }

        public String getInTime() {
            return inTime;
        }

        public int getId() {
            return id;
        }

        public String getCurrentState() {
            return currentState;
        }

        public UserEntity getUser() {
            return user;
        }

        public String getOutTime() {
            return outTime;
        }

        public class UserEntity {
            /**
             * userFlag : 0
             * realName : 凌永平
             * tskNum : null
             * inspectState : 0
             * repairState : 0
             * userPass : lingyongping
             * userTel : 15071481976
             * id : 4
             * userName : lingyongping
             */
            private int userFlag;
            private String realName;
            private String tskNum;
            private String inspectState;
            private String repairState;
            private String userPass;
            private String userTel;
            private int id;
            private String userName;

            public void setUserFlag(int userFlag) {
                this.userFlag = userFlag;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public void setTskNum(String tskNum) {
                this.tskNum = tskNum;
            }

            public void setInspectState(String inspectState) {
                this.inspectState = inspectState;
            }

            public void setRepairState(String repairState) {
                this.repairState = repairState;
            }

            public void setUserPass(String userPass) {
                this.userPass = userPass;
            }

            public void setUserTel(String userTel) {
                this.userTel = userTel;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public int getUserFlag() {
                return userFlag;
            }

            public String getRealName() {
                return realName;
            }

            public String getTskNum() {
                return tskNum;
            }

            public String getInspectState() {
                return inspectState;
            }

            public String getRepairState() {
                return repairState;
            }

            public String getUserPass() {
                return userPass;
            }

            public String getUserTel() {
                return userTel;
            }

            public int getId() {
                return id;
            }

            public String getUserName() {
                return userName;
            }
        }
    }
}
