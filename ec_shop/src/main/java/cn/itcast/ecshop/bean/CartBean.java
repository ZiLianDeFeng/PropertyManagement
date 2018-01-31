package cn.itcast.ecshop.bean;

public class CartBean {
        private int prodNum;
        /**
         * buyLimit : 10
         * id : 1
         * name : 韩版时尚蕾丝裙
         * number : 100
         * pic : /images/product/detail/c3.jpg
         * price : 350
         * productProperty : [{"id":1,"k":"颜色","v":"红色"},{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"},{"id":4,"k":"尺码","v":"XXL"}]
         */

        private Product product;

        public int getProdNum() {
            return prodNum;
        }

        public void setProdNum(int prodNum) {
            this.prodNum = prodNum;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

    @Override
    public String toString() {
        return "CartBean{" +
                "prodNum=" + prodNum +
                ", product=" + product +
                '}';
    }
}