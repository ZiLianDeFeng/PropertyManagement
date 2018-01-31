package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/2/22.
 */
public class CheckVersionResponse extends BaseReponse {

    /**
     * result : 0
     * apkName : v1.3.txt
     */
    private String result;
    private String apkName;

    public CheckVersionResponse() {
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getResult() {
        return result;
    }

    public String getApkName() {
        return apkName;
    }
}
