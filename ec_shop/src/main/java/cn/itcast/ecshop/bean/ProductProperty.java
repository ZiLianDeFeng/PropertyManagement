package cn.itcast.ecshop.bean;

public  class ProductProperty {
        private int id;
        private String k;
        private String v;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getK() {
            return k;
        }

        public void setK(String k) {
            this.k = k;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }

    @Override
    public String toString() {
        return "ProductProperty{" +
                "id=" + id +
                ", k='" + k + '\'' +
                ", v='" + v + '\'' +
                '}';
    }
}