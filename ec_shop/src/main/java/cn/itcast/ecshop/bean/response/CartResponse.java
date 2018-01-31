package cn.itcast.ecshop.bean.response;

import java.util.List;

import cn.itcast.ecshop.bean.CartBean;
import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CartResponse extends BaseReponse {


    /**
     * cart : [{"prodNum":3,"product":{"buyLimit":10,"id":1,"name":"韩版时尚蕾丝裙","number":"100","pic":"/images/product/detail/c3.jpg","price":350,"productProperty":[{"id":1,"k":"颜色","v":"红色"},{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"},{"id":4,"k":"尺码","v":"XXL"}]}},{"prodNum":2,"product":{"buyLimit":10,"id":2,"name":"粉色毛衣","number":"13","pic":"/images/product/detail/q1.jpg","price":100,"productProperty":[{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"}]}}]
     * prom : ["促销信息一","促销信息二"]
     * response : cart
     * totalCount : 5
     * totalPoint : 100
     * totalPrice : 1250
     */

    private String response;
    private int totalCount;
    private int totalPoint;
    private int totalPrice;
    /**
     * prodNum : 3
     * product : {"buyLimit":10,"id":1,"name":"韩版时尚蕾丝裙","number":"100","pic":"/images/product/detail/c3.jpg","price":350,"productProperty":[{"id":1,"k":"颜色","v":"红色"},{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"},{"id":4,"k":"尺码","v":"XXL"}]}
     */

    private List<CartBean> cart;
    private List<String> prom;


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartBean> getCart() {
        return cart;
    }

    public void setCart(List<CartBean> cart) {
        this.cart = cart;
    }

    public List<String> getProm() {
        return prom;
    }

    public void setProm(List<String> prom) {
        this.prom = prom;
    }

    @Override
    public String toString() {
        return "CartResponse{" +
                "response='" + response + '\'' +
                ", totalCount=" + totalCount +
                ", totalPoint=" + totalPoint +
                ", totalPrice=" + totalPrice +
                ", cart=" + cart +
                ", prom=" + prom +
                '}';
    }
}
