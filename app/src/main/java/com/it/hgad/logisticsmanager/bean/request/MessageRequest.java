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
 * Created by Administrator on 2017/5/12.
 */
public class MessageRequest extends BaseRequest{
    //参数名
    private List<String> paramNames;
    //参数值
    private List<String> paramValues;
    //查询条件 like or =
    private List<String> paramConditons;
    //数据类型 string Integer
    private List<String> paramTypes;
    private int pagenum;

    public MessageRequest(List<String> paramNames, List<String> paramValues, List<String> paramConditons, List<String> paramTypes,  int pagenum) {
        this.paramNames = paramNames;
        this.paramValues = paramValues;
        this.paramConditons = paramConditons;
        this.paramTypes = paramTypes;
        this.pagenum = pagenum;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address);
        return ip + "noticeAction!getList.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        String names = paramNames.toString().substring(1, paramNames.toString().length() - 1);
        names = names.replace(", ", ";");
        String values = paramValues.toString().substring(1, paramValues.toString().length() - 1);
        values = values.replace(", ", ";");
        String conditons = paramConditons.toString().substring(1, paramConditons.toString().length() - 1);
        conditons = conditons.replace(", ", ";");
        String types = paramTypes.toString().substring(1, paramTypes.toString().length() - 1);
        types = types.replace(", ", ";");
        HashMap<String, String> map = new HashMap<>();
        map.put("paramNames", names);
        map.put("paramValues", values);
        map.put("paramConditons", conditons);
        map.put("paramTypes", types);
        map.put("pagenum", pagenum + "");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
