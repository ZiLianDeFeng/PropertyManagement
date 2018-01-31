package com.it.hgad.logisticsmanager.constants;

/**
 * Created by ck on 2016/8/8.
 */
public class HttpConstants {

    public static final String HOST = "http://";

    public static final String REQ = "/MPMS/device/";

    public static final String PIC = "/MPMS/files/app/photo/";

    public static final String APK = "/MPMS/files/app/file/";

    public static final String PROJECT = "/MPMS/";

    public static final String UPLOADPIC = "/MPMS/device/dvcRepairAction!uploadFile.do";

    public static final String UPLOADSIGN = "/MPMS/device/dvcRepairAction!uploadSignFile.do";

    public static String picFormatUrl(String url) {
        return HOST + url + PIC;
    }

    public static String ReqFormatUrl(String url) {
        return HOST + url + REQ;
    }

    public static String APKFormatUrl(String url) {
        return HOST + url + APK;
    }

    public static String ProFormatUrl(String url) {
        return HOST + url + PROJECT;
    }


}
