package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by wanjiawen on 2016/8/11.
 */
public class ShopThirdListRequest extends BaseRequest{
    private  String filter;
    private String cId;
    private String saleDown;

    public ShopThirdListRequest(String cId,String saleDown,String filter) {
        this.cId = cId;
        this.saleDown=saleDown;
        this.filter=filter;
    }

    @Override
    public String getUrl() {
        return Constant.formatUrl("productlist");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page","1");
        map.put("pageNum", "10");
        map.put("cId", cId);
        map.put("orderby",saleDown);
        map.put("filter",filter);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
