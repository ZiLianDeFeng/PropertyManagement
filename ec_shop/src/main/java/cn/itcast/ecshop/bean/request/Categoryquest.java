package cn.itcast.ecshop.bean.request;

import java.util.Map;

import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.utils.Constant;

/**
 * Created by wanjiawen on 2016/8/10.
 */
public class Categoryquest extends BaseRequest{
    @Override
    public String getUrl() {
        return Constant.formatUrl("category");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
