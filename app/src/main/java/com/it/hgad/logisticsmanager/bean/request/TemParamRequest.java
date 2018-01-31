package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/25.
 */
public class TemParamRequest extends BaseRequest{
    private String id;

    public TemParamRequest(String id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"dvcTempTaskAct!paramList.do";
    }

    @Override
    public BaseRequest.HttpMethod getHttpMethod() {
        return BaseRequest.HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String,String> map=new HashMap<>();
        map.put("tempTask.id",id);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
