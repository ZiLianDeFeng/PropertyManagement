package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/1/11.
 */
public class MaterialRequest extends BaseRequest {
    private String materialName;
    private int pageNum;

    public MaterialRequest(String materialName, int pageNum) {
        this.materialName = materialName;
        this.pageNum = pageNum;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"dvcMaterialAct!listSel.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String,String> map=new HashMap<>();
        map.put("materialName",materialName);
        map.put("pagenum",pageNum+"");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
