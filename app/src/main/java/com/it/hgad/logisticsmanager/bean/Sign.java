package com.it.hgad.logisticsmanager.bean;

/**
 * Created by Administrator on 2017/4/24.
 */
public class Sign {

    private String inTime;
    private int id;
    private String outTime;

    public Sign(String inTime, int id, String outTime) {
        this.inTime = inTime;
        this.id = id;
        this.outTime = outTime;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
