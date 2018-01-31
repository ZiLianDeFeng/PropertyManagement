package cn.itcast.ecshop.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by ck on 2016/8/12.
 */
public class LogoutReponse extends BaseReponse {
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
