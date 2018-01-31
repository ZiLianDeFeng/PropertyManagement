package cn.itcast.ecshop.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/4.
 */
public class ShopCartManager {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void addOrUpdateToCart(int productId, int productNum, Set<Integer> productProperties) throws SQLException {
        if (productProperties == null || productProperties.size() < 1) {
            productProperties = new HashSet<>();
            productProperties.add(0);
        }/* else {
            for (Integer productProperty : productProperties) {
                if (productProperty < 0 || productProperty > 5) {
                    productProperty = 0;
                    productProperties.add(productProperty);
                }
            }
        }*/
        if (productNum == 0) {

            productNum = 1;
        }
        List<CartItem> list = MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao()
                .queryBuilder().where().eq("productId", productId).query();
        if (list != null && list.size() > 0) {

            CartItem item = list.get(0);
            item.setProductNum(productNum);
            item.setProductProperties(productProperties);
            MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().update(item);

           /* CartItem cartItem = new CartItem(productId, productNum, productProperties);
            //MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().update(cartItem);
            MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao()
                    .updateRaw("update " + CartItem.SHOP_CART_TABLE_NAME + "set productNum=? productPropertiesStr=? WHERE productId = ?",
                            cartItem.getProductNum() + "", cartItem.getProductProperties() + "", cartItem.getProductId() + "");*/
        } else {

            MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().create(new CartItem(productId, productNum, productProperties, true));
        }
    }

    //设置勾选状态
    public static void updataCheckedState(int productId, boolean isChecked) throws SQLException {
        List<CartItem> list = MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao()
                .queryBuilder().where().eq("productId", productId).query();
        if (list != null && list.size() > 0) {
            CartItem item = list.get(0);
            item.setIsChecked(isChecked);
            MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().update(item);
        }
    }

    //全部勾选的方法
    public static void checkedAll() throws SQLException {
        List<CartItem> cartlist = getCartlist();
        if (cartlist != null && cartlist.size() > 0) {
            for (CartItem cartItem : cartlist) {
                cartItem.setIsChecked(true);
                MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().update(cartItem);
            }
        }
    }

    //全部不勾选的方法
    public static void checkedOutAll() throws SQLException {
        List<CartItem> cartlist = getCartlist();
        if (cartlist != null && cartlist.size() > 0) {
            for (CartItem cartItem : cartlist) {
                cartItem.setIsChecked(false);
                MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().update(cartItem);
            }
        }
    }

    //判断是否全部勾选
    public static boolean isCheckedAll() throws SQLException {
        boolean flag = true;
        List<CartItem> cartlist = getCartlist();
        if (cartlist != null && cartlist.size() > 0) {
            for (CartItem cartItem : cartlist) {
                if (!cartItem.isChecked()) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    //获取勾选了对象的方法
    public static List<CartItem> getCheckedList() throws SQLException {
        List<CartItem> mCartItems = new ArrayList<>();
        List<CartItem> cartItems = MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().queryForAll();
        if (cartItems == null || cartItems.size() < 1) {
            return null;
        }

        for (CartItem cartItem : cartItems) {
            if (cartItem.isChecked()) {
                mCartItems.add(cartItem);
            }
        }
        return mCartItems;
    }

    //获取对象的方法
    public static CartItem getCartItemByProductId(int productId) throws SQLException {
        int id = getId(productId);
        return (CartItem) MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().queryForId(String.valueOf(id));
    }

    //获取勾选状态的方法
    public static boolean getCheckedState(int productId) throws SQLException {
        CartItem cartItem = getCartItemByProductId(productId);
        return cartItem.isChecked();
    }

    //删除对象的方法
    public static void deleteCartItem(int id) throws SQLException {

        MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().deleteById(id);
    }

    //获取数据库里面ID的方法
    public static int getId(int productId) throws SQLException {
        List<CartItem> list = MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao()
                .queryBuilder().where().eq("productId", productId).query();
        if (list != null && list.size() > 0) {

            CartItem item = list.get(0);
            return item.getId();
        }
        return 0;
    }


    //获取数据库所有数据集合的方法
    public static List<CartItem> getCartlist() throws SQLException {
        return MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().queryForAll();
    }


    public static String getShopCartStr() throws SQLException {

        List<CartItem> cartItems = MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().queryForAll();
        if (cartItems == null || cartItems.size() < 1) {

            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (CartItem cartItem : cartItems) {

            stringBuilder.append(cartItem.toString()).append("|");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    //获取所有需要结算的sku
    public static String getPayCenterStr() throws SQLException {
        List<CartItem> cartItems = MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().queryForAll();
        if (cartItems == null || cartItems.size() < 1) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (CartItem cartItem : cartItems) {
            if (cartItem.isChecked()) {
                stringBuilder.append(cartItem.toString()).append("|");
            }
        }
        if (stringBuilder.length() == 0) {
            return "";
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    ////获取单个商品结算的sku
    public static String getSinglePayCenterStr(int productId) throws SQLException {
        List<CartItem> cartItems = MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao()
                .queryBuilder().where().eq("productId", productId).query();
        if (cartItems == null || cartItems.size() < 1) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (CartItem cartItem : cartItems) {
            if (cartItem.isChecked()) {
                stringBuilder.append(cartItem.toString()).append("|");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    //清空购物车的方法
    public static void clearShopCart() throws SQLException {

        MyOrmLiteSqliteOpenHelper.getInstance(mContext).getShopCartDao().queryRaw("delete from " + CartItem.SHOP_CART_TABLE_NAME);
    }

    //结算后清理数据库的方法
    public static void clearPayShopCart() throws SQLException {
        List<CartItem> cartlist = getCartlist();
        for (CartItem cartItem : cartlist) {
            if (cartItem.isChecked()) {
                deleteCartItem(cartItem.getId());
            }
        }
    }
}
