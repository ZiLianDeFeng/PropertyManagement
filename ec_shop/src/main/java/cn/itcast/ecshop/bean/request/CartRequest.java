package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CartRequest extends BaseRequest {

    private String sku;

    public CartRequest(String sku) {
        this.sku = sku;
    }

    @Override
    public String getUrl() {
        return Constant.formatUrl("cart");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("sku", sku);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
