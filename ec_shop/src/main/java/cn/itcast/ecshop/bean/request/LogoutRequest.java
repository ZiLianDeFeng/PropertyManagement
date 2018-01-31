package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by ck on 2016/8/12.
 */
public class LogoutRequest extends BaseRequest {

    private String userid;

    public LogoutRequest(String userid) {
        this.userid = userid;
    }

    @Override
    public String getUrl() {
        return Constant.formatUrl("/logout");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", userid);
        return map;
    }
}
