package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by ck on 2016/8/12.
 */
public class FavoritesRequest extends BaseRequest {
    private String userid;

    public FavoritesRequest(String userid) {
        this.userid = userid;
    }

    @Override
    public String getUrl() {
        return Constant.formatUrl("/favorites");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", "0");
        map.put("pageNum", "10");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", userid);
        return map;
    }
}
