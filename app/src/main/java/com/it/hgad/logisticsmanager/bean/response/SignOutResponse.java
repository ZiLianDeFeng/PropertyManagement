package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/2/9.
 */
public class SignOutResponse extends BaseReponse {

    /**
     * result : 0
     * data : {"inTime":"2017-02-10T14:15:33","id":12,"currentState":"已签到","user":{"userFlag":0,"realName":"凌永平","tskNum":null,"userPass":"lingyongping","userTel":"15071481976","id":4,"userName":"lingyongping"},"outTime":null}
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
         * inTime : 2017-02-10T14:15:33
         * id : 12
         * currentState : 已签到
         * user : {"userFlag":0,"realName":"凌永平","tskNum":null,"userPass":"lingyongping","userTel":"15071481976","id":4,"userName":"lingyongping"}
         * outTime : null
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
             * userPass : lingyongping
             * userTel : 15071481976
             * id : 4
             * userName : lingyongping
             */
            private int userFlag;
            private String realName;
            private String tskNum;
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
