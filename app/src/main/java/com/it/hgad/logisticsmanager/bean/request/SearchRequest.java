package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/1/16.
 */
public class SearchRequest extends BaseRequest {
    private String repairFlag;
    private int userId;
    private String repairNo ;
    private String repairTel ;
    private int pagenum ;
    private String sendTime;
    private String eventType;

//    public SearchRequest(String repairFlag, int userId, String repairNo, String repairTel, int pagenum) {
//        this.repairFlag = repairFlag;
//        this.userId = userId;
//        this.repairNo = repairNo;
//        this.repairTel = repairTel;
//        this.pagenum = pagenum;
//    }


    public SearchRequest(String repairFlag, int userId, String repairNo, String repairTel, int pagenum, String sendTime, String eventType) {
        this.repairFlag = repairFlag;
        this.userId = userId;
        this.repairNo = repairNo;
        this.repairTel = repairTel;
        this.pagenum = pagenum;
        this.sendTime = sendTime;
        this.eventType = eventType;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"dvcRepairAction!searchList.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userId",userId+"");
        map.put("repairFlag",repairFlag);
        map.put("repairNo",repairNo);
        map.put("repairTel",repairTel);
        map.put("pagenum",pagenum+"");
        map.put("sendTime",sendTime);
        map.put("eventType",eventType);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
