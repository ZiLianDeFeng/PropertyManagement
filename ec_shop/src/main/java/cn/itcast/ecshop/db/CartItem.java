package cn.itcast.ecshop.db;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Set;

/**
 * Created by Administrator on 2016/8/4.
 */

@DatabaseTable(tableName = CartItem.SHOP_CART_TABLE_NAME)
public class CartItem {

    public static final String SHOP_CART_TABLE_NAME = "shop_cart_table";

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(columnName = "productId")
    public int productId;
    @DatabaseField(columnName = "productNum")
    public int productNum;
    @DatabaseField(columnName = "properties")
    public String properties;
    @DatabaseField(columnName = "isChecked")
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public CartItem() {

    }

    public CartItem(int productId, int productNum, Set<Integer> productProperties,boolean isChecked) {
        this.productId = productId;
        this.productNum = productNum;
        this.properties = getProductPropertiesString(productProperties);
        this.isChecked=isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }


    public void setProductProperties(Set<Integer> productProperties) {
        this.properties = getProductPropertiesString(productProperties);
        Log.d("CartItem", "productPropertiesStr setProductProperties :"+properties);
    }

    //1,3
    public String getProductPropertiesString(Set<Integer> productProperties) {

        if (productProperties != null && productProperties.size() > 0) {

            StringBuilder stringBuilder = new StringBuilder();
            for (Integer property : productProperties) {

                stringBuilder.append(property).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        } else {

            return "";
        }
    }

    //   1:3:1,3  properties 1,3
    @Override
    public String toString() {

        String cartItemStr = productId + ":" + productNum;
        if (!"".equals(properties)) {

            cartItemStr += ":" + properties;
        }

        return cartItemStr;
    }
}
