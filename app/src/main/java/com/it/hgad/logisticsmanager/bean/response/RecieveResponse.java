package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/2/15.
 */
public class RecieveResponse extends BaseReponse{
    private String result;
    private String mes;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
