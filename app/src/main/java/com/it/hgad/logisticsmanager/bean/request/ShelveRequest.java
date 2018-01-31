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
public class ShelveRequest extends BaseRequest{
    private String repairId;
    private String shelveReason;
    private String shelveTime;

    public ShelveRequest(String repairId, String shelveReason) {
        this.repairId = repairId;
        this.shelveReason = shelveReason;
    }

    public ShelveRequest(String repairId, String shelveReason, String shelveTime) {
        this.repairId = repairId;
        this.shelveReason = shelveReason;
        this.shelveTime = shelveTime;
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
        HashMap<String,String> map=new HashMap<>();
        map.put("repair.id",repairId);
        map.put("repair.shelveReason",shelveReason);
        map.put("repair.shelveTime",shelveReason);
        map.put("handle","shelveRepair");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
