package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/2/16.
 */
public class CheckSearchRequest extends BaseRequest{
    private String taskStatus;
    private String userId;
    private String deviceName;
    private int pagenum ;

    public CheckSearchRequest(String taskStatus, String userId, String deviceName, int pagenum) {
        this.taskStatus = taskStatus;
        this.userId = userId;
        this.deviceName = deviceName;
        this.pagenum = pagenum;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"DvcInsAct!searchList.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userId",userId+"");
        map.put("taskStatus",taskStatus);
        map.put("deviceName",deviceName);
        map.put("pagenum",pagenum+"");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
