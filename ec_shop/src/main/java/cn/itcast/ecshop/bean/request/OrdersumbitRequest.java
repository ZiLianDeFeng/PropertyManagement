package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by Administrator on 2016/8/8.
 */
public class OrdersumbitRequest extends BaseRequest {
    @Override
    public String getUrl() {
        return Constant.formatUrl("ordersumbit");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {

     /*   HashMap<String, String> map = new HashMap<>();
        try {
          map.put("sku", ShopCartManager.getShopCartStr());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("addressId", "139");
        map.put("paymentType", "1");
        map.put("deliveryType", "1");
        map.put("invoiceType", "1");
        map.put("invoiceTitle", "传智");
        map.put("invoiceContent", "汽车");*/


        return null;
    }

    @Override
    public Map<String, String> getHeaders() {

        HashMap<String, String> map = new HashMap<>();
        map.put("userid", "2042822");
        return map;
    }
}
