package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/1/9.
 */
public class LoginResponse extends BaseReponse{


    /**
     * result : 0
     * mes : 成功
     * user : {"userFlag":null,"realName":"Admin","tskNum":null,"userPass":"123456","userTel":"18607425689","id":14,"userName":"admin"}
     */
    private String result;
    private String mes;
    private UserEntity user;

    public void setResult(String result) {
        this.result = result;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public String getMes() {
        return mes;
    }

    public UserEntity getUser() {
        return user;
    }

    public class UserEntity {
        /**
         * userFlag : null
         * realName : Admin
         * tskNum : null
         * userPass : 123456
         * userTel : 18607425689
         * id : 14
         * userName : admin
         */
        private String userFlag;
        private String realName;
        private String tskNum;
        private String userPass;
        private String userTel;
        private int id;
        private String userName;

        public void setUserFlag(String userFlag) {
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

        public String getUserFlag() {
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
