package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/2/14.
 */
public class ParamCommitResponse extends BaseReponse {

    /**
     * result : 0
     * mes : 操作成功
     * id : 368
     */
    private String result;
    private String mes;
    private int id;

    public void setResult(String result) {
        this.result = result;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public String getMes() {
        return mes;
    }

    public int getId() {
        return id;
    }
}

