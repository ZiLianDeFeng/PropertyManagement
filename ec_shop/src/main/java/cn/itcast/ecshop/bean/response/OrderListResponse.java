package cn.itcast.ecshop.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2016/8/4.
 */
public class OrderListResponse extends BaseReponse {

    @Override
    public String toString() {
        return "OrderListResponse{" +
                "response='" + response + '\'' +
                ", orderList=" + orderList +
                '}';
    }

    /**
     * orderList : [{"flag":1,"orderId":"260092","price":208,"product":{"buyLimit":10,"id":3,"name":"女裙","number":"14","pic":"/images/product/detail/c1.jpg","price":300},"status":"未处理","time":"1439528260115"},{"flag":2,"orderId":"171835","price":208,"status":"未处理","time":"1439529171852"},{"flag":3,"orderId":"286342","price":208,"status":"未处理","time":"1439529286360"},{"flag":1,"orderId":"657072","price":208,"status":"未处理","time":"1439529657077"},{"flag":1,"orderId":"818792","price":208,"product":{"buyLimit":10,"id":3,"name":"女裙","number":"14","pic":"/images/product/detail/c1.jpg","price":300},"status":"未处理","time":"1439529818827"},{"flag":1,"orderId":"862938","price":208,"product":{"buyLimit":10,"id":3,"name":"女裙","number":"14","pic":"/images/product/detail/c1.jpg","price":300},"status":"未处理","time":"1439529862961"},{"flag":3,"orderId":"415230","price":208,"status":"未处理","time":"1439531415262"},{"flag":1,"orderId":"432294","price":208,"product":{"buyLimit":10,"id":3,"name":"女裙","number":"14","pic":"/images/product/detail/c1.jpg","price":300},"status":"未处理","time":"1439531432317"},{"flag":2,"orderId":"490503","price":208,"product":{"buyLimit":10,"id":3,"name":"女裙","number":"14","pic":"/images/product/detail/c1.jpg","price":300},"status":"未处理","time":"1439531490519"},{"flag":1,"orderId":"624490","price":208,"product":{"buyLimit":10,"id":3,"name":"女裙","number":"14","pic":"/images/product/detail/c1.jpg","price":300},"status":"未处理","time":"1439533624526"}]
     * response : orderList
     */

    private String response;
    /**
     * flag : 1
     * orderId : 260092
     * price : 208
     * product : {"buyLimit":10,"id":3,"name":"女裙","number":"14","pic":"/images/product/detail/c1.jpg","price":300}
     * status : 未处理
     * time : 1439528260115
     */

    private List<OrderListBean> orderList;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {

        @Override
        public String toString() {
            return "OrderListBean{" +
                    "flag=" + flag +
                    ", orderId='" + orderId + '\'' +
                    ", price=" + price +
                    ", product=" + product +
                    ", status='" + status + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }

        private int flag;
        private String orderId;
        private int price;
        /**
         * buyLimit : 10
         * id : 3
         * name : 女裙
         * number : 14
         * pic : /images/product/detail/c1.jpg
         * price : 300
         */

        private ProductBean product;
        private String status;
        private String time;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public static class ProductBean {

            @Override
            public String toString() {
                return "ProductBean{" +
                        "buyLimit=" + buyLimit +
                        ", id=" + id +
                        ", name='" + name + '\'' +
                        ", number='" + number + '\'' +
                        ", pic='" + pic + '\'' +
                        ", price=" + price +
                        '}';
            }

            private int buyLimit;
            private int id;
            private String name;
            private String number;
            private String pic;
            private int price;

            public int getBuyLimit() {
                return buyLimit;
            }

            public void setBuyLimit(int buyLimit) {
                this.buyLimit = buyLimit;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }
        }
    }
}
