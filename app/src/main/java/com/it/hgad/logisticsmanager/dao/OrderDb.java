package com.it.hgad.logisticsmanager.dao;

import com.it.hgad.logisticsmanager.bean.Order;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/20.
 */
@Table(name = OrderDb.TABLE_NAME)
public class OrderDb implements Serializable {
    public static final String TABLE_NAME = "OrderTable";
    public static final String REPAIR_NO = "repairNo";
    public static final String REPAIR_TEL = "repairTel";
    public static final String REPAIR_FLAG = "repairFlag";

    //    private int id;
    @Id
    @NoAutoIncrement
    private int repairId;
    @Column(column = "userId")
    private int userId;
    @Column(column = "repairReply")
    private String repairReply;
    @Column(column = "repairMan")
    private String repairMan;
    @Column(column = "finishTime")
    private String finishTime;
    @Column(column = "repairTel")
    private String repairTel;
    @Column(column = "registerTime")
    private String registerTime;
    @Column(column = "repairType")
    private String repairType;
    @Column(column = "repairNo")
    private String repairNo;
    @Column(column = "shelveTime")
    private String shelveTime;
    @Column(column = "registerMan")
    private String registerMan;
    @Column(column = "repairFlag")
    private int repairFlag;
    @Column(column = "repairContent")
    private String repairContent;
    @Column(column = "sameCondition")
    private String sameCondition;
    @Column(column = "shelveReason")
    private String shelveReason;
    @Column(column = "userIds")
    private String userIds;
    @Column(column = "userNames")
    private String userNames;
    @Column(column = "repairDept")
    private String repairDept;
    @Column(column = "repairImg")
    private String repairImg;
    @Column(column = "spotImg")
    private String spotImg;
    @Column(column = "repairSrc")
    private String repairSrc;
    @Column(column = "repairLoc")
    private String repairLoc;

    public OrderDb() {
    }

    public OrderDb(int repairId, int userId, String repairReply, String repairMan, String finishTime, String repairTel, String registerTime, String repairType, String repairNo, String shelveTime, String registerMan, int repairFlag, String repairContent, String sameCondition, String shelveReason, String userIds, String userNames, String repairDept, String repairImg, String spotImg, String repairSrc, String repairLoc) {
        this.repairId = repairId;
        this.userId = userId;
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
        this.repairSrc = repairSrc;
        this.repairLoc = repairLoc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSpotImg() {
        return spotImg;
    }

    public void setSpotImg(String spotImg) {
        this.spotImg = spotImg;
    }

    public int getRepairId() {
        return repairId;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
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

    public void setData(Order entity) {
        this.repairId = entity.getId();
        this.repairReply = entity.getRepairReply();
        this.repairMan = entity.getRepairMan();
        this.finishTime = entity.getFinishTime();
        this.repairTel = entity.getRepairTel();
        this.registerTime = entity.getRegisterTime();
        this.repairType = entity.getRepairType();
        this.repairNo = entity.getRepairNo();
        this.shelveTime = entity.getShelveTime();
        this.registerMan = entity.getRegisterMan();
        this.repairFlag = entity.getRepairFlag();
        this.repairContent = entity.getRepairContent();
        this.sameCondition = entity.getSameCondition();
        this.shelveReason = entity.getShelveReason();
        this.userIds = entity.getUserIds();
        this.userNames = entity.getUserNames();
        this.repairDept = entity.getRepairDept();
        this.repairImg = entity.getRepairImg();
        this.spotImg = entity.getSpotImg();
        this.repairSrc = entity.getRepairSrc();
        this.repairLoc = entity.getRepairLoc();
    }
}
