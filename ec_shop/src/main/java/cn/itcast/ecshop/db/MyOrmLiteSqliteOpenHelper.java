package cn.itcast.ecshop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/8/4.
 */
public class MyOrmLiteSqliteOpenHelper extends OrmLiteSqliteOpenHelper {

    private static MyOrmLiteSqliteOpenHelper instance;
    private Dao<CartItem, ?> mShopCartDao;

    public static MyOrmLiteSqliteOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (MyOrmLiteSqliteOpenHelper.class) {
                if (instance == null) {
                    instance = new MyOrmLiteSqliteOpenHelper(context);
                }
            }
        }
        return instance;
    }


    public MyOrmLiteSqliteOpenHelper(Context context) {
        super(context, "ec_shop.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, CartItem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dao getShopCartDao() {

        try {
            if (mShopCartDao == null) {

                mShopCartDao = getDao(CartItem.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mShopCartDao;
    }

    @Override
    public void close() {
        super.close();
        mShopCartDao = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
