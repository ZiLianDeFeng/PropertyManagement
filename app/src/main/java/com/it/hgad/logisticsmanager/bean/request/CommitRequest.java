package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/1/11.
 */
public class CommitRequest extends BaseRequest {
    private String userId;
    private String id;
    private String repairReply;
    private List<String> repairMaterialId;
    private List<String> repairMaterialName;
    private List<String> repairMaterialType;
    private List<String> repairMaterialNum;
    private List<String> repairImgPath;
    private List<String> spotImg;
    private List<String> repairMaterialPrice;
    private String sameCondition;
    private String satisfy;
    private String delayTime;
    private String startTime;
    private String finishTime;
    private String signImage;

    public CommitRequest(String userId, String id, String repairReply, List<String> repairMaterialId, List<String> repairMaterialName, List<String> repairMaterialType, List<String> repairMaterialNum, List<String> repairImgPath, List<String> spotImg, List<String> repairMaterialPrice, String sameCondition, String satisfy, String delayTime, String startTime, String finishTime,String signImage) {
        this.userId = userId;
        this.id = id;
        this.repairReply = repairReply;
        this.repairMaterialId = repairMaterialId;
        this.repairMaterialName = repairMaterialName;
        this.repairMaterialType = repairMaterialType;
        this.repairMaterialNum = repairMaterialNum;
        this.repairImgPath = repairImgPath;
        this.spotImg = spotImg;
        this.repairMaterialPrice = repairMaterialPrice;
        this.sameCondition = sameCondition;
        this.satisfy = satisfy;
        this.delayTime = delayTime;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.signImage = signImage;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"dvcRepairAction!updateRepair.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        String materialIds = repairMaterialId.toString().substring(1, repairMaterialId.toString().length()-1);
        String materialName = repairMaterialName.toString().substring(1, repairMaterialName.toString().length()-1);
        String specification = repairMaterialType.toString().substring(1, repairMaterialType.toString().length()-1);
        String materialNum = repairMaterialNum.toString().substring(1, repairMaterialNum.toString().length()-1);
        String materialPrice = repairMaterialPrice.toString().substring(1, repairMaterialPrice.toString().length()-1);
        String repairImg = repairImgPath.toString().substring(1, repairImgPath.toString().length()-1);
        String spotImgs = spotImg.toString().substring(1, spotImg.toString().length()-1);
        repairImg = repairImg.replaceAll(", ","|");
        HashMap<String,String> map=new HashMap<>();
        map.put("repair.id",id);
        map.put("repair.repairReply",repairReply);
//        map.put("material.materialIds",materialIds);
        map.put("materialInfo.materialName",materialIds);
//        map.put("material.specification",specification);
        map.put("materialInfo.balanceNum",materialNum);
        map.put("repair.repairImg",repairImg);
        map.put("repair.spotImg",spotImgs);
//        map.put("material.materialPrice",materialPrice);
        map.put("repair.sameCondition",sameCondition);
        map.put("repair.satisfy",satisfy);
        map.put("repair.startTime",startTime);
        map.put("repair.delayTime",delayTime);
        map.put("repair.finishTime",finishTime);
        map.put("repair.signImg",signImage);
        map.put("handle","doRepair");
        map.put("userId",userId);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
