package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/1/12.
 */
public class UpLoadPicRequest extends BaseRequest {
    private String id;
    private String img;
    private String imgFileName;
    private String imgContentType;

    public UpLoadPicRequest(String id, String img, String imgFileName, String imgContentType) {
        this.id = id;
        this.img = img;
        this.imgFileName = imgFileName;
        this.imgContentType = imgContentType;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"dvcRepairAction!uploadFile.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String,String> map=new HashMap<>();
        map.put("id",id);
        map.put("img",img);
        map.put("imgFileName",imgFileName);
        map.put("imgContentType",imgContentType);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
