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
public class TemporaryRequest extends BaseRequest{
    private int userId;
    private String pagenum;

    public TemporaryRequest(int userId, String pagenum) {
        this.userId = userId;
        this.pagenum = pagenum;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"dvcTempTaskAct!list.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String,String> map=new HashMap<>();
        map.put("user.id",userId+"");
        map.put("pagenum",pagenum);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
