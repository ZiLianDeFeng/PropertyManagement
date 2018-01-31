package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/3/22.
 */
public class ForPushResponse extends BaseReponse{

    /**
     * mes : success
     */
    private String mes;

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMes() {
        return mes;
    }
}
