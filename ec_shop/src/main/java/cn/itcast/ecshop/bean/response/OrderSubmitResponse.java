package cn.itcast.ecshop.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2016/8/4.
 *
 *      {"orderInfo":{"orderId":"521281","paymentType":1,"price":450},"response":"orderSubmit"}
 */
public class OrderSubmitResponse  extends BaseReponse {

    @Override
    public String toString() {
        return "OrderSubmitResponse{" +
                "orderInfo=" + orderInfo +
                ", response='" + response + '\'' +
                '}';
    }

    /**
     * orderId : 521281
     * paymentType : 1
     * price : 450
     */

    private OrderInfoBean orderInfo;
    /**
     * orderInfo : {"orderId":"521281","paymentType":1,"price":450}
     * response : orderSubmit
     */

    private String response;

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static class OrderInfoBean {

        @Override
        public String toString() {
            return "OrderInfoBean{" +
                    "orderId='" + orderId + '\'' +
                    ", paymentType=" + paymentType +
                    ", price=" + price +
                    '}';
        }

        private String orderId;
        private int paymentType;
        private int price;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
