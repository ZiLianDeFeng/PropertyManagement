package com.it.hgad.logisticsmanager.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Utils {

    private static Toast toast;

    protected static final int DOWNLOAD_SUCCESS = 0;
    protected static final int DOWNLOAD_FAILED = 1;
    private static int code;

    // 检查网络状态 如果移动数据网络和WiFi任意一个可用则判断为有网
    public static boolean checkNetWork(Context context) {
        // 获得联网的管理者
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取网络状态的NetworkInfo
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        int type = info.getType();
        if (type == ConnectivityManager.TYPE_MOBILE || type == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //判断文件是否存在
    public static boolean existsInSd(String fileName) {
        return new File(Environment.getExternalStorageDirectory(), fileName).exists();

    }

    public static boolean isConnection = true;

    public static boolean isConnectionService() throws IOException {

        new Thread() {
            @Override
            public void run() {
                super.run();
                URL url = null;
                try {
                    url = new URL(HttpConstants.HOST);
                    HttpURLConnection connection
                            = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(1000);
                    connection.connect();
                    int code = connection.getResponseCode();
                    if (code != 200) {
                        isConnection = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return isConnection;
    }

    public static void showTost(Context context, String text) {

        if (toast == null) {

            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        toast.setText(text);
        toast.show();
    }


    public interface OnDownloadListener {

        /**
         * 下载成功的回调方法
         * @param destFile
         */
        void onDownloadSuccess(File destFile);

        /**
         * 下载失败的回调方法
         */
        void onDownlaodFailed();
    }

    public static void updateTable(DbUtils dbUtils, Class<?> tClass,String tableName) {
        try {
            if (dbUtils.tableIsExist(tClass)) {
//                String tableName =  tClass.getName();
//                tableName = tableName.replace(".", "_");
                String sql = "select * from " + tableName;
                //声名一个map用来保存原有表中的字段
                Map<String, String> filedMap = new HashMap<>();
                //执行自定义的sql语句
                Cursor cursor = dbUtils.execQuery(sql);
                int count = cursor.getColumnCount();
                for (int i = 0; i < count; i++) {
                    filedMap.put(cursor.getColumnName(i), cursor.getColumnName(i));
                }
                //cursor在用完之后一定要close
                cursor.close();
                //下面用到了一些反射知识，下面是获取实体类的所有私有属性（即我们更改表结构后的所有字段名）
                Field[] fields = tClass.getDeclaredFields();

                for (int i = 0; i < fields.length; i++) {
                    if (filedMap.containsKey(fields[i].getName())) {
                        //假如字段名已存在就进行下次循环
                        String name = fields[i].getName();
                        Log.e("updateTable", "noUpdateTable: "+name);
                        continue;
                    } else {
                        //不存在，就放到map中，并且给表添加字段
                        filedMap.put(fields[i].getName(), fields[i].getName());
                        //根据属性的类型给表增加字段
                        if (fields[i].getType().toString().equals("class java.lang.String")) {
                            String name = fields[i].getName();
                            Log.e("updateTable", "updateTable: "+name);
                            dbUtils.execNonQuery("alter table " + tableName + " add " + fields[i].getName() + " TEXT ");
                        } else if (fields[i].getType().equals("int") || fields[i].getType().equals("long") || fields[i].getType().equals("boolean")) {
                            String name = fields[i].getName();
                            Log.e("updateTable", "updateTable: "+name);
                            dbUtils.execNonQuery("alter table " + tableName + " add " + fields[i].getName() + " INTEGER ");
                        }
                    }
                }


            }
        } catch (DbException e) {
            e.printStackTrace();
        }


    }
}
