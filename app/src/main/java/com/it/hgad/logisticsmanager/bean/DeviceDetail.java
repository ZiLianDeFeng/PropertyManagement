package com.it.hgad.logisticsmanager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/7.
 */
public class DeviceDetail implements Serializable {
    private String detailName;
    private boolean hasKeep;

    public DeviceDetail(String detailName, boolean hasKeep) {
        this.detailName = detailName;
        this.hasKeep = hasKeep;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public boolean isHasKeep() {
        return hasKeep;
    }

    public void setHasKeep(boolean hasKeep) {
        this.hasKeep = hasKeep;
    }
}
