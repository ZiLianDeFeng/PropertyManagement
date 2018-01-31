package cn.itcast.ecshop.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Constant {
    public static final String MATERIAL_DB = Environment.getExternalStorageDirectory()+"/db";
    public static final String HOST = "http://192.168.16.90:8080/market/";

    public static String formatUrl(String url) {

        return HOST + url;
    }


}
