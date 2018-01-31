package cn.itcast.ecshop;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/4.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //ShopCartManager.init(getApplicationContext());
    }
}
