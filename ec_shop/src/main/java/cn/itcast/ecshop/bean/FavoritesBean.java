package cn.itcast.ecshop.bean;

/**
 * Created by ck on 2016/8/12.
 */
public class FavoritesBean {
    private int id;
    private int marketPrice;
    private String name;
    private String pic;
    private int price;

    public FavoritesBean() {
    }

    public FavoritesBean(int id, int marketPrice, String name, String pic, int price) {
        this.id = id;
        this.marketPrice = marketPrice;
        this.name = name;
        this.pic = pic;
        this.price = price;
    }

    @Override
    public String toString() {
        return "FavoritesBean{" +
                "id=" + id +
                ", marketPrice=" + marketPrice +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", price=" + price +
                '}';
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
