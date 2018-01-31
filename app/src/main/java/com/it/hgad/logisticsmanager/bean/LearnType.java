package com.it.hgad.logisticsmanager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/26.
 */
public class LearnType implements Serializable{
    private String typeName;
    private String img;

    public LearnType(String typeName, String img) {
        this.typeName = typeName;
        this.img = img;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
