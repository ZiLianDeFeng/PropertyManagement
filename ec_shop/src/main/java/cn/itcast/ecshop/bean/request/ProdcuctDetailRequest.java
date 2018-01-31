package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by Administrator on 2016/8/8.
 *
 * http://192.168.209.1:8080/market/product?pId=1
 */
public class ProdcuctDetailRequest extends BaseRequest {
    private String pId;

    public ProdcuctDetailRequest(String pId) {
        this.pId = pId;
    }

    @Override
    public String getUrl() {

        return Constant.formatUrl("product");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {

        HashMap<String, String> map = new HashMap<>();
        map.put("pId", pId);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
