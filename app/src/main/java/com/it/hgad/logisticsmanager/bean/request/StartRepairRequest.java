package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/2/15.
 */
public class StartRepairRequest extends BaseRequest{
    private String repairId;
    private String sameCondition;
    private String startTime;
    private String delayTime;

    public StartRepairRequest(String repairId, String sameCondition, String startTime, String delayTime) {
        this.repairId = repairId;
        this.sameCondition = sameCondition;
        this.startTime = startTime;
        this.delayTime = delayTime;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip + "dvcRepairAction!updateRepair.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("repair.id", repairId);
        map.put("handle", "startRepair");
        map.put("repair.sameCondition",sameCondition);
        map.put("repair.startTime",startTime);
        map.put("repair.delayTime",delayTime);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
