package com.it.hgad.logisticsmanager.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.util.CrashHandler;
import com.lidroid.xutils.DbUtils;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/1/13.
 */
public class LocalApp extends Application {


    private static Context context = null;

    private static DbUtils db;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        LocalApp.context = context;
    }


    public static DbUtils getDb() {
        return db;
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        setIp("192.168.2.80:8080");
//        setIp("172.168.1.39:8080");
//        setIp("27.17.36.227:18865");
        context = getApplicationContext();
//        DbUtils.DaoConfig config = new DbUtils.DaoConfig(LocalApp.this);
        db = DbUtils.create(this, Constants.DB_NAME, 42, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
                if (newVersion != oldVersion) {
                    //按需求进行更新
//                    Utils.updateTable(dbUtils, ParamDb.class,ParamDb.TABLE_NAME);
//                    Utils.updateTable(dbUtils, CommitDb.class,CommitDb.TABLE_NAME);
//                    Utils.updateTable(dbUtils, CheckOrderDb.class,CheckOrderDb.TABLE_NAME);
//                    Utils.updateTable(dbUtils, CheckCommitDb.class,CheckCommitDb.TABLE_NAME);
                }
            }
        });
//        config.setDbName(Constants.DB_NAME);
//        config.setDbVersion(24);
//        //config.setDbDir(File file);
//        //config.setDbDir(File file);
//        // 该语句会将数据库文件保存在你想存储的地方
//        //如果不设置则默认存储在应用程序目录下/database/dbName.db
//        config.setDbUpgradeListener(new DbUtils.DbUpgradeListener() {
//            @Override
//            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
//                //用来监听数据库是否升级
//                //如果当前数据库版本号比已存在的数据库版本号高则执行此片段
//                //用途：软件升级之后在第一次运行时执行一些必要的初始化操作
////                Utils.updateTable(dbUtils, CommitDb.class);
////                Utils.updateTable(dbUtils, CheckOrderDb.class);
////                Utils.updateTable(dbUtils, CheckCommitDb.class);
//                Utils.updateTable(dbUtils, ParamDb.class);
////                try {
//////                    dbUtils.createTableIfNotExist(CommitDb.class);
//////                    dbUtils.execNonQuery("alter table CommitTable add handWrite text");
////                    dbUtils.createTableIfNotExist(ParamDb.class);
////                    dbUtils.execNonQuery("alter table ParamTable add referenceTypeId text");
////                } catch (DbException e) {
////                    e.printStackTrace();
////                }
//
//            }
//        });
//        db = DbUtils.create(config);
        CrashHandler.getInstance().init(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setLatestNotificationNumber(this, 1000);
        initNotification();
    }

    private void initNotification() {
//        CustomPushNotificationBuilder builder = new
//                CustomPushNotificationBuilder(this,
//                R.layout.customer_notitfication_layout,
//                R.id.icon,
//                R.id.title,
//                R.id.text);
//        // 指定定制的 Notification Layout
//        builder.statusBarDrawable = R.mipmap.ic_launcher;
//        // 指定最顶层状态栏小图标
//        builder.layoutIconDrawable = R.mipmap.ic_launcher;
//        // 指定下拉状态栏时显示的通知图标
//        JPushInterface.setPushNotificationBuilder(2, builder);
//        int soundId = SPUtils.getInt(context, Constants.NOTI_SOUND);
//        int shockId = SPUtils.getInt(context, Constants.NOTI_SHOCK);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
//        if (soundId == 1 && shockId == 1) {
            builder.notificationDefaults = Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//        } else if (soundId == 1 && shockId != 1) {
//            builder.notificationDefaults = Notification.DEFAULT_SOUND
//                    | Notification.DEFAULT_LIGHTS;
//        } else if (soundId != 1 && shockId == 1) {
//            builder.notificationDefaults = Notification.DEFAULT_VIBRATE
//                    | Notification.DEFAULT_LIGHTS;
//        } else if (soundId != 1 && shockId != 1) {
//            builder.notificationDefaults = Notification.DEFAULT_LIGHTS;
//        }
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

}
