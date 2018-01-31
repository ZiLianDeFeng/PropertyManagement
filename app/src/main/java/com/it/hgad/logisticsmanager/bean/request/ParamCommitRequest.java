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
 * Created by Administrator on 2017/2/14.
 */
public class ParamCommitRequest extends BaseRequest{
    private String userId;
    private String id;
    private List<String> actualValues;
    private String description;
    private String finishTime;
    private String referenceParamTypeId;

    public ParamCommitRequest(String userId, String id, List<String> actualValues, String description,String finishTime) {
        this.userId = userId;
        this.id = id;
        this.actualValues = actualValues;
        this.description = description;
        this.finishTime = finishTime;
    }

    public ParamCommitRequest(String userId, String id, List<String> actualValues, String description, String finishTime, String referenceParamTypeId) {
        this.userId = userId;
        this.id = id;
        this.actualValues = actualValues;
        this.description = description;
        this.finishTime = finishTime;
        this.referenceParamTypeId = referenceParamTypeId;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"DvcInsAct!doInspect.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String,String> map=new HashMap<>();
        String actualValue = actualValues.toString().substring(1, actualValues.toString().length() - 1);
        map.put("userId",userId);
        map.put("deviceTaskResult.id",id);
        map.put("deviceTaskResult.actualParamTypeId",referenceParamTypeId);
        map.put("deviceTaskResultParam.actualValue",actualValue);
        map.put("deviceTaskResult.description",description);
        map.put("deviceTaskResult.finishTime",finishTime);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
