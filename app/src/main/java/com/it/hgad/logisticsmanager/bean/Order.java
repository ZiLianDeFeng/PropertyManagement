package com.it.hgad.logisticsmanager.bean;

import com.it.hgad.logisticsmanager.dao.OrderDb;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/23.
 */
public class Order implements Serializable{
    /**
     * repairReply : null
     * repairMan : 4666
     * finishTime : null
     * repairTel : 46464646
     * registerTime : 2017-01-17T11:34:58
     * repairType : 更换
     * repairNo : 20170117113506
     * shelveTime : null
     * registerMan : Admin
     * repairFlag : 2
     * repairContent : 4666
     * sameCondition : null
     * shelveReason : null
     * userIds : 5, 4
     * userNames : 余建军,凌永平
     * repairDept : 4666
     * repairImg : null
     * id : 33
     * repairSrc : 电话报修
     * repairLoc : 466
     */
    private String repairReply;
    private String repairMan;
    private String finishTime;
    private String repairTel;
    private String registerTime;
    private String repairType;
    private String repairNo;
    private String shelveTime;
    private String registerMan;
    private int repairFlag;
    private String repairContent;
    private String sameCondition;
    private String shelveReason;
    private String userIds;
    private String userNames;
    private String repairDept;
    private String repairImg;
    private String spotImg;
    private int id;
    private String repairSrc;
    private String repairLoc;


    public void setData(OrderDb orderDb){
        this.id = orderDb.getRepairId();
        this.registerMan = orderDb.getRegisterMan();
        this.registerTime = orderDb.getRegisterTime();
        this.repairContent = orderDb.getRepairContent();
        this.repairDept = orderDb.getRepairDept();
        this.repairFlag = orderDb.getRepairFlag();
        this.repairImg = orderDb.getRepairImg();
        this.repairLoc = orderDb.getRepairLoc();
        this.repairNo = orderDb.getRepairNo();
        this.repairSrc = orderDb.getRepairSrc();
        this.repairReply =orderDb.getRepairReply();
        this.finishTime = orderDb.getFinishTime();
        this.shelveReason = orderDb.getShelveReason();
        this.shelveTime = orderDb.getShelveTime();
        this.repairTel = orderDb.getRepairTel();
        this.sameCondition = orderDb.getSameCondition();
        this.spotImg = orderDb.getSpotImg();
        this.userIds = orderDb.getUserIds();
        this.userNames = orderDb.getUserNames();
        this.repairType = orderDb.getRepairType();
        this.repairMan = orderDb.getRepairMan();

    }

    public Order() {
    }

    public Order(String repairReply, String repairMan, String finishTime, String repairTel, String registerTime, String repairType, String repairNo, String shelveTime, String registerMan, int repairFlag, String repairContent, String sameCondition, String shelveReason, String userIds, String userNames, String repairDept, String repairImg, String spotImg, int id, String repairSrc, String repairLoc) {
        this.repairReply = repairReply;
        this.repairMan = repairMan;
        this.finishTime = finishTime;
        this.repairTel = repairTel;
        this.registerTime = registerTime;
        this.repairType = repairType;
        this.repairNo = repairNo;
        this.shelveTime = shelveTime;
        this.registerMan = registerMan;
        this.repairFlag = repairFlag;
        this.repairContent = repairContent;
        this.sameCondition = sameCondition;
        this.shelveReason = shelveReason;
        this.userIds = userIds;
        this.userNames = userNames;
        this.repairDept = repairDept;
        this.repairImg = repairImg;
        this.spotImg = spotImg;
        this.id = id;
        this.repairSrc = repairSrc;
        this.repairLoc = repairLoc;
    }

    public String getSpotImg() {
        return spotImg;
    }

    public void setSpotImg(String spotImg) {
        this.spotImg = spotImg;
    }

    public String getRepairReply() {
        return repairReply;
    }

    public void setRepairReply(String repairReply) {
        this.repairReply = repairReply;
    }

    public String getRepairMan() {
        return repairMan;
    }

    public void setRepairMan(String repairMan) {
        this.repairMan = repairMan;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRepairTel() {
        return repairTel;
    }

    public void setRepairTel(String repairTel) {
        this.repairTel = repairTel;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getRepairNo() {
        return repairNo;
    }

    public void setRepairNo(String repairNo) {
        this.repairNo = repairNo;
    }

    public String getShelveTime() {
        return shelveTime;
    }

    public void setShelveTime(String shelveTime) {
        this.shelveTime = shelveTime;
    }

    public String getRegisterMan() {
        return registerMan;
    }

    public void setRegisterMan(String registerMan) {
        this.registerMan = registerMan;
    }

    public int getRepairFlag() {
        return repairFlag;
    }

    public void setRepairFlag(int repairFlag) {
        this.repairFlag = repairFlag;
    }

    public String getRepairContent() {
        return repairContent;
    }

    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    public String getSameCondition() {
        return sameCondition;
    }

    public void setSameCondition(String sameCondition) {
        this.sameCondition = sameCondition;
    }

    public String getShelveReason() {
        return shelveReason;
    }

    public void setShelveReason(String shelveReason) {
        this.shelveReason = shelveReason;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getRepairDept() {
        return repairDept;
    }

    public void setRepairDept(String repairDept) {
        this.repairDept = repairDept;
    }

    public String getRepairImg() {
        return repairImg;
    }

    public void setRepairImg(String repairImg) {
        this.repairImg = repairImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepairSrc() {
        return repairSrc;
    }

    public void setRepairSrc(String repairSrc) {
        this.repairSrc = repairSrc;
    }

    public String getRepairLoc() {
        return repairLoc;
    }

    public void setRepairLoc(String repairLoc) {
        this.repairLoc = repairLoc;
    }


}
