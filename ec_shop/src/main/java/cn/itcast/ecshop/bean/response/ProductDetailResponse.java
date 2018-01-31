package cn.itcast.ecshop.bean.response;

import cn.itcast.ecshop.bean.Product;
import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2016/8/8.
 */
public class ProductDetailResponse extends BaseReponse {


    /**
     * available : true
     * bigPic : ["/images/product/detail/bigcar1.jpg","/images/product/detail/bigcar2.jpg","/images/product/detail/bigcar3.jpg","/images/product/detail/bigcar4.jpg"]
     * buyLimit : 10
     * commentCount : 10
     * id : 1
     * inventoryArea : 全国
     * leftTime : 18000
     * limitPrice : 200
     * marketPrice : 500
     * name : 韩版时尚蕾丝裙
     * pics : ["/images/product/detail/c3.jpg","/images/product/detail/c4.jpg","/images/product/detail/c1.jpg","/images/product/detail/c2.jpg"]
     * price : 350
     * productProperty : [{"id":1,"k":"颜色","v":"红色"},{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"},{"id":4,"k":"尺码","v":"XXL"},{"id":5,"k":"尺码","v":"XXXL"}]
     * score : 5
     */

    private Product product;
    /**
     * product : {"available":true,"bigPic":["/images/product/detail/bigcar1.jpg","/images/product/detail/bigcar2.jpg","/images/product/detail/bigcar3.jpg","/images/product/detail/bigcar4.jpg"],"buyLimit":10,"commentCount":10,"id":1,"inventoryArea":"全国","leftTime":18000,"limitPrice":200,"marketPrice":500,"name":"韩版时尚蕾丝裙","pics":["/images/product/detail/c3.jpg","/images/product/detail/c4.jpg","/images/product/detail/c1.jpg","/images/product/detail/c2.jpg"],"price":350,"productProperty":[{"id":1,"k":"颜色","v":"红色"},{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"},{"id":4,"k":"尺码","v":"XXL"},{"id":5,"k":"尺码","v":"XXXL"}],"score":5}
     * response : product
     */

    private String response;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ProductDetailResponse{" +
                "product=" + product +
                ", response='" + response + '\'' +
                '}';
    }
}
