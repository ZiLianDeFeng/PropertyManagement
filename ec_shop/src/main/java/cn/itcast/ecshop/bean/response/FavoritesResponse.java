package cn.itcast.ecshop.bean.response;

import java.util.List;

import cn.itcast.ecshop.bean.FavoritesBean;
import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by ck on 2016/8/12.
 */
public class FavoritesResponse extends BaseReponse {

    private String response;
    private List<FavoritesBean> productList;
    private int listCount;

    @Override
    public String toString() {
        return "FavoritesResponse{" +
                "response='" + response + '\'' +
                ", productList=" + productList +
                ", listCount=" + listCount +
                '}';
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<FavoritesBean> getProductList() {
        return productList;
    }

    public void setProductList(List<FavoritesBean> productList) {
        this.productList = productList;
    }

    public int getListCount() {
        return listCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }
}
