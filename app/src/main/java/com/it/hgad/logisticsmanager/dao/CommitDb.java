package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/23.
 */
@Table(name = CommitDb.TABLE_NAME)
public class CommitDb implements Serializable {
    public static final String TABLE_NAME = "CommitTable";
    public static final String USER_ID = "userId";
    public static final String REPAIR_REPLY = "repairReply";
    public static final String REPAIR_MATERIAL_ID = "repairMaterialId";
    public static final String REPAIR_MATERIAL_NAME = "repairMaterialName";
    public static final String REPAIR_MATERIAL_TYPE = "repairMaterialType";
    public static final String REPAIR_MATERIAL_NUM = "repairMaterialNum";
    public static final String REPAIR_IMG_PATH = "repairImgPath";
    public static final String SPOT_IMG = "spotImg";
    public static final String REPAIR_MATERIAL_PRICE = "repairMaterialPrice";
    public static final String SAME_CONDITION = "sameCondition";
    public static final String START_TIME = "startTime";
    public static final String DELAY_TIME = "delayTime";
    public static final String FINISH_TIME = "finishTime";
    public static final String REPAIR_MAN = "repairMan";
    public static final String REPAIR_TEL = "repairTel";
    public static final String REPAIR_CONTENT = "repairContent";
    public static final String REPAIR_NO = "repairNo";
    public static final String HAS_COMMIT = "hasCommit";
    public static final String HAND_WRITE = "handWrite";
    public static final String RECIEVE_TIME = "recieveTime";
    @Id
    @NoAutoIncrement
    private int repairId;
    @Column(column = USER_ID)
    private int userId;
    @Column(column = REPAIR_REPLY)
    private String repairReply;
    @Column(column = REPAIR_MATERIAL_ID)
    private String repairMaterialId;
    @Column(column = REPAIR_MATERIAL_NAME)
    private String repairMaterialName;
    @Column(column = REPAIR_MATERIAL_TYPE)
    private String repairMaterialType;
    @Column(column = REPAIR_MATERIAL_NUM)
    private String repairMaterialNum;
    @Column(column = REPAIR_IMG_PATH)
    private String repairImgPath;
    @Column(column = SPOT_IMG)
    private String spotImg;
    @Column(column = REPAIR_MATERIAL_PRICE)
    private String repairMaterialPrice;
    @Column(column = SAME_CONDITION)
    private String sameCondition;
    @Column(column = "satisfy")
    private String satisfy;
    @Column(column = START_TIME)
    private String startTime;
    @Column(column = DELAY_TIME)
    private String delayTime;
    @Column(column = FINISH_TIME)
    private String finishTime;
    @Column(column = REPAIR_MAN)
    private String repairMan;
    @Column(column = REPAIR_TEL)
    private String repairTel;
    @Column(column = REPAIR_CONTENT)
    private String repairContent;
    @Column(column = REPAIR_NO)
    private String repairNo;
    @Column(column = HAS_COMMIT)
    private int hasCommit;
    @Column(column = HAND_WRITE)
    private String handWrite;
    @Column(column = RECIEVE_TIME)
    private String recieveTime;

    public CommitDb() {
    }

    public CommitDb(int repairId, int userId, String repairReply, String repairMaterialId, String repairMaterialName, String repairMaterialType, String repairMaterialNum, String repairImgPath, String spotImg, String repairMaterialPrice, String sameCondition, String satisfy, String finishTime, String repairNo, int hasCommit, String handWrite) {
        this.repairId = repairId;
        this.userId = userId;
        this.repairReply = repairReply;
        this.repairMaterialId = repairMaterialId;
        this.repairMaterialName = repairMaterialName;
        this.repairMaterialType = repairMaterialType;
        this.repairMaterialNum = repairMaterialNum;
        this.repairImgPath = repairImgPath;
        this.spotImg = spotImg;
        this.repairMaterialPrice = repairMaterialPrice;
        this.satisfy = satisfy;
        this.finishTime = finishTime;
        this.hasCommit = hasCommit;
        this.repairNo = repairNo;
        this.handWrite = handWrite;
    }

    public String getRecieveTime() {
        return recieveTime;
    }

    public void setRecieveTime(String recieveTime) {
        this.recieveTime = recieveTime;
    }

    public String getHandWrite() {
        return handWrite;
    }

    public void setHandWrite(String handWrite) {
        this.handWrite = handWrite;
    }

    public String getRepairNo() {
        return repairNo;
    }

    public void setRepairNo(String repairNo) {
        this.repairNo = repairNo;
    }

    public String getRepairMan() {
        return repairMan;
    }

    public void setRepairMan(String repairMan) {
        this.repairMan = repairMan;
    }

    public String getRepairContent() {
        return repairContent;
    }

    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getRepairTel() {
        return repairTel;
    }

    public void setRepairTel(String repairTel) {
        this.repairTel = repairTel;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getHasCommit() {
        return hasCommit;
    }

    public void setHasCommit(int hasCommit) {
        this.hasCommit = hasCommit;
    }

    public String getSpotImg() {
        return spotImg;
    }

    public void setSpotImg(String spotImg) {
        this.spotImg = spotImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getRepairMaterialId() {
        return repairMaterialId;
    }

    public void setRepairMaterialId(String repairMaterialId) {
        this.repairMaterialId = repairMaterialId;
    }

    public String getRepairMaterialName() {
        return repairMaterialName;
    }

    public void setRepairMaterialName(String repairMaterialName) {
        this.repairMaterialName = repairMaterialName;
    }

    public String getRepairMaterialType() {
        return repairMaterialType;
    }

    public void setRepairMaterialType(String repairMaterialType) {
        this.repairMaterialType = repairMaterialType;
    }

    public String getRepairMaterialNum() {
        return repairMaterialNum;
    }

    public void setRepairMaterialNum(String repairMaterialNum) {
        this.repairMaterialNum = repairMaterialNum;
    }

    public String getRepairImgPath() {
        return repairImgPath;
    }

    public void setRepairImgPath(String repairImgPath) {
        this.repairImgPath = repairImgPath;
    }

    public String getRepairMaterialPrice() {
        return repairMaterialPrice;
    }

    public void setRepairMaterialPrice(String repairMaterialPrice) {
        this.repairMaterialPrice = repairMaterialPrice;
    }

    public String getSameCondition() {
        return sameCondition;
    }

    public void setSameCondition(String sameCondition) {
        this.sameCondition = sameCondition;
    }

    public String getSatisfy() {
        return satisfy;
    }

    public void setSatisfy(String satisfy) {
        this.satisfy = satisfy;
    }
}
