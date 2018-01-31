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
 * Created by Administrator on 2017/4/25.
 */
public class TempParamCommitRequest extends BaseRequest {
    private String userId;
    private String id;
    private List<String> actualValues;
    private String description;
    private String finishTime;

    public TempParamCommitRequest(String userId, String id, List<String> actualValues, String description, String finishTime) {
        this.userId = userId;
        this.id = id;
        this.actualValues = actualValues;
        this.description = description;
        this.finishTime = finishTime;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip + "dvcTempTaskAct!doTask.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        String actualValue = actualValues.toString().substring(1, actualValues.toString().length() - 1);
        map.put("user.id", userId);
        map.put("tempTaskResult.id", id);
        map.put("tempTaskResultParam.actualValue", actualValue);
        map.put("tempTaskResult.description", description);
        map.put("tempTaskResult.finishTime", finishTime);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
