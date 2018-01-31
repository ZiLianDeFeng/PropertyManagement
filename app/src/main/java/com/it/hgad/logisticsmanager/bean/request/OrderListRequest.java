package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/1/9.
 */
public class OrderListRequest extends BaseRequest{
    private String repairFlag;
    private int userId;
    private String pageNum;

    public String getRepairFlag() {
        return repairFlag;
    }

    public void setRepairFlag(String repairFlag) {
        this.repairFlag = repairFlag;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public OrderListRequest(String repairFlag, int userId, String pageNum) {
        this.repairFlag = repairFlag;
        this.userId = userId;
        this.pageNum = pageNum;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"dvcRepairAction!list.do";
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
        map.put("pagenum",pageNum);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
