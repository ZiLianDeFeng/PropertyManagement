package com.it.hgad.logisticsmanager.bean.request;

import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/6/8.
 */
public class LearnRequest extends BaseRequest {


    @Override
    public String getUrl() {
//        String ip = null;
//        InetAddress x = null;
//        try {
//            String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
//            address ="a.example.com/";
//            x = InetAddress.getByName(address);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//
//        ip = x.getHostAddress();
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip+"trianAct!getArticleList.do";
//        return "http://a.example.com/device/trianAct!getArticleList.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
