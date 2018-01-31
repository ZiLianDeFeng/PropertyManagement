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
public class RecieveRequest extends BaseRequest {
    private String repairId;
    private String recieveTime;
    private String userId;

    public RecieveRequest(String repairId,String recieveTime,String userId) {
        this.repairId = repairId;
        this.recieveTime = recieveTime;
        this.userId = userId;
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
        map.put("userId", userId);
        map.put("handle", "recieveRepair");
        map.put("repair.recieveTime", recieveTime);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
