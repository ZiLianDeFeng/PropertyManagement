package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by Administrator on 2016/8/4.
 */
public class OrderListRequest extends BaseRequest {


    @Override
    public String getUrl() {
        return Constant.formatUrl("orderlist");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "0");
        map.put("page", "0");
        map.put("pageNum", "10");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", "20428");
        return map;
    }
}
