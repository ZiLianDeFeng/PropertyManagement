package cn.itcast.ecshop.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2016/8/8.
 */
public class FavorityResponse  extends BaseReponse {


    /**
     * response : addfavorites
     */

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "FavorityResponse{" +
                "response='" + response + '\'' +
                '}';
    }
}
