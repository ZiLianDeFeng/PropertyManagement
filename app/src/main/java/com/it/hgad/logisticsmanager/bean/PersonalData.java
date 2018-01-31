package com.it.hgad.logisticsmanager.bean;

/**
 * Created by Administrator on 2017/4/14.
 */
public class PersonalData {
    private String name;
    private int maintainCount;
    private int checkCount;
    private String attendanceRate;

    public PersonalData(String name, int maintainCount, int checkCount, String attendanceRate) {
        this.name = name;
        this.maintainCount = maintainCount;
        this.checkCount = checkCount;
        this.attendanceRate = attendanceRate;
    }
}
