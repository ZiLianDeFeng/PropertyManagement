package cn.itcast.ecshop.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class Product {


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

    private boolean available;
    private int buyLimit;
    private int commentCount;
    private int id;
    private String inventoryArea;
    private int leftTime;
    private int limitPrice;
    private int marketPrice;
    private String name;
    private int price;
    private int score;
    private List<String> bigPic;
    private List<String> pics;
    private String pic;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Product{" +
                "available=" + available +
                ", buyLimit=" + buyLimit +
                ", commentCount=" + commentCount +
                ", id=" + id +
                ", inventoryArea='" + inventoryArea + '\'' +
                ", leftTime=" + leftTime +
                ", limitPrice=" + limitPrice +
                ", marketPrice=" + marketPrice +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", score=" + score +
                ", bigPic=" + bigPic +
                ", pics=" + pics +
                ", pic='" + pic + '\'' +
                ", number=" + number +
                ", productProperty=" + productProperty +
                '}';
    }

    /**
     * id : 1
     * k : 颜色
     * v : 红色
     */


    private List<ProductProperty> productProperty;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getBuyLimit() {
        return buyLimit;
    }

    public void setBuyLimit(int buyLimit) {
        this.buyLimit = buyLimit;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInventoryArea() {
        return inventoryArea;
    }

    public void setInventoryArea(String inventoryArea) {
        this.inventoryArea = inventoryArea;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public int getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(int limitPrice) {
        this.limitPrice = limitPrice;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getBigPic() {
        return bigPic;
    }

    public void setBigPic(List<String> bigPic) {
        this.bigPic = bigPic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<ProductProperty> getProductProperty() {
        return productProperty;
    }

    public void setProductProperty(List<ProductProperty> productProperty) {
        this.productProperty = productProperty;
    }
}
