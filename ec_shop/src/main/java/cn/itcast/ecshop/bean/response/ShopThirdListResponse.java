package cn.itcast.ecshop.bean.response;

import java.util.List;

import cn.itcast.ecshop.bean.ProductListBean;
import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by wanjiawen on 2016/8/11.
 */
public class ShopThirdListResponse extends BaseReponse{

    /**
     * listCount : 7
     * listFilter : [{"key":"品牌","valueList":[{"id":"s9","name":"爱家"},{"id":"s10","name":"咪咪"},{"id":"s11","name":"防辐射"},{"id":"s12","name":"枕工坊"},{"id":"s13","name":"Nutrilon"},{"id":"s14","name":"Hero"},{"id":"s15","name":"Goo.N"},{"id":"s8","name":"好奇"},{"id":"s1","name":"快乐宝贝"},{"id":"s2","name":"环球娃娃"},{"id":"s3","name":"Kiddy"},{"id":"s4","name":"hogar"},{"id":"s5","name":"奇妮孕妇装"},{"id":"s6","name":"Bio-oil"},{"id":"s7","name":"莫施"},{"id":"s16","name":"IMG"},{"id":"s17","name":"雅培"},{"id":"s18","name":"贝因美"},{"id":"s19","name":"周生生"},{"id":"s20","name":"婴姿坊"},{"id":"s21","name":"飞利浦"}]},{"key":"价格","valueList":[{"id":"p1","name":"100.0"},{"id":"p2","name":"200.0"},{"id":"p3","name":"300.0"},{"id":"p4","name":"400.0"},{"id":"p5","name":"500.0"},{"id":"p6","name":"600.0"},{"id":"p7","name":"700.0"},{"id":"p8","name":"800.0"},{"id":"p9","name":"900.0"}]},{"key":"颜色","valueList":[{"id":"t1","name":"红色"},{"id":"t2","name":"粉色"},{"id":"t3","name":"黑色"},{"id":"t4","name":"深色"},{"id":"t5","name":"浅色"},{"id":"t6","name":"白色"}]}]
     * productList : [{"commentCount":10,"id":8,"marketPrice":200,"name":"情女装","pic":"/images/product/detail/q6.jpg","price":160},{"commentCount":10,"id":9,"marketPrice":300,"name":"女鞋","pic":"/images/product/detail/q7.jpg","price":200},{"commentCount":10,"id":5,"marketPrice":98,"name":"时尚女裙","pic":"/images/product/detail/a1.jpg","price":108},{"commentCount":10,"id":6,"marketPrice":100,"name":"时尚秋装","pic":"/images/product/detail/w2.jpg","price":52},{"commentCount":10,"id":3,"marketPrice":500,"name":"女裙","pic":"/images/product/detail/c1.jpg","price":300},{"commentCount":10,"id":1,"marketPrice":500,"name":"韩版时尚蕾丝裙","pic":"/images/product/detail/c3.jpg","price":350},{"commentCount":10,"id":4,"marketPrice":198,"name":"帽子","pic":"/images/product/detail/b1.jpg","price":168}]
     * response : categoryProductlist
     */

    private int listCount;
    private String response;
    /**
     * key : 品牌
     * valueList : [{"id":"s9","name":"爱家"},{"id":"s10","name":"咪咪"},{"id":"s11","name":"防辐射"},{"id":"s12","name":"枕工坊"},{"id":"s13","name":"Nutrilon"},{"id":"s14","name":"Hero"},{"id":"s15","name":"Goo.N"},{"id":"s8","name":"好奇"},{"id":"s1","name":"快乐宝贝"},{"id":"s2","name":"环球娃娃"},{"id":"s3","name":"Kiddy"},{"id":"s4","name":"hogar"},{"id":"s5","name":"奇妮孕妇装"},{"id":"s6","name":"Bio-oil"},{"id":"s7","name":"莫施"},{"id":"s16","name":"IMG"},{"id":"s17","name":"雅培"},{"id":"s18","name":"贝因美"},{"id":"s19","name":"周生生"},{"id":"s20","name":"婴姿坊"},{"id":"s21","name":"飞利浦"}]
     */

    private List<ListFilterBean> listFilter;
    /**
     * commentCount : 10
     * id : 8
     * marketPrice : 200
     * name : 情女装
     * pic : /images/product/detail/q6.jpg
     * price : 160
     */

    private List<ProductListBean> productList;

    public int getListCount() {
        return listCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<ListFilterBean> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<ListFilterBean> listFilter) {
        this.listFilter = listFilter;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public static class ListFilterBean {
        @Override
        public String toString() {
            return "ListFilterBean{" +
                    "key='" + key + '\'' +
                    ", valueList=" + valueList +
                    '}';
        }

        private String key;
        /**
         * id : s9
         * name : 爱家
         */

        private List<ValueListBean> valueList;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<ValueListBean> getValueList() {
            return valueList;
        }

        public void setValueList(List<ValueListBean> valueList) {
            this.valueList = valueList;
        }

        public static class ValueListBean {
            @Override
            public String toString() {
                return "ValueListBean{" +
                        "isChangeBg=" + isChangeBg +
                        ", id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            private boolean isChangeBg;
            private String id;
            private String name;

            public boolean isChangeBg() {
                return isChangeBg;
            }

            public void setIsChangeBg(boolean isChangeBg) {
                this.isChangeBg = isChangeBg;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }


}
