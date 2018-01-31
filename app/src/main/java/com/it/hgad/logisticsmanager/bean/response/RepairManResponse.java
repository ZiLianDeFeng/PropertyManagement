package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/1/17.
 */
public class RepairManResponse extends BaseReponse {

    /**
     * realNames : {}
     */
    private RealNamesEntity realNames;

    public void setRealNames(RealNamesEntity realNames) {
        this.realNames = realNames;
    }

    public RealNamesEntity getRealNames() {
        return realNames;
    }

    public class RealNamesEntity {
    }
}
