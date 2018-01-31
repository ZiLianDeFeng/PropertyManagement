package cn.itcast.ecshop.bean.request;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by Administrator on 2016/8/4.
 */
public class OrderSubmitRequest extends BaseRequest {

    private String userid;
    private String sku;
    private String addressId;
    private String paymentType;
    private String deliveryType;
    private String invoiceType;
    private String invoiceTitle;
    private String invoiceContent;

    public OrderSubmitRequest(String userid, String sku, String addressId, String paymentType, String deliveryType, String invoiceType, String invoiceTitle, String invoiceContent) {
        this.userid = userid;
        this.sku = sku;
        this.addressId = addressId;
        this.paymentType = paymentType;
        this.deliveryType = deliveryType;
        this.invoiceType = invoiceType;
        this.invoiceTitle = invoiceTitle;
        this.invoiceContent = invoiceContent;
    }

    @Override
    public String getUrl() {
        return Constant.formatUrl("ordersumbit");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("sku", sku);
        map.put("addressId", addressId);
        map.put("paymentType", paymentType);
        map.put("deliveryType", deliveryType);
        map.put("invoiceType", invoiceType);
        map.put("invoiceTitle", invoiceTitle);
        map.put("invoiceContent", invoiceContent);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", userid);
        return map;
    }
}
