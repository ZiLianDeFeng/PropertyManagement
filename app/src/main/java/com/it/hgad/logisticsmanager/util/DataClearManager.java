package com.it.hgad.logisticsmanager.util;

import android.content.Context;

import com.it.hgad.logisticsmanager.constants.Constants;
import com.lidroid.xutils.exception.DbException;

import java.io.File;

/**
 * Created by Administrator on 2017/2/27.
 */
public class DataClearManager {
    /*
        清楚本应用内部缓存
     */
    public static void cleanInternalCache(Context context) {
        deleteFileByDirectory(context.getCacheDir());
    }

    /*
        清楚本应用所有数据库
         */
    public static void cleanDatabases(Context context) {
        deleteFileByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /*
        清楚本应用SharePreference
         */
    public static void cleanSharePreference(Context context) {
        deleteFileByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /*
        清楚本应用所有数据
         */
    public static void cleanApplicationData(Context context) throws DbException {
//        cleanInternalCache(context);
//        cleanDatabases(context);
//        cleanSharePreference(context);
//        DbUtils db = LocalApp.getDb();
//        db.dropDb();
        context.deleteDatabase(Constants.DB_NAME);
        BitmapUtils.deleteCacheFile();
        Utils.showTost(context, "清理完成");
    }
    /*
           删除方法
        */
    private static void deleteFileByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }


}
