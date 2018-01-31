package com.it.hgad.logisticsmanager.constants;

import android.os.Environment;

/**
 * Created by Administrator on 2017/2/10.
 */
public class Constants {
    public static final String COMMIT_OK = "commit_ok";
    public static final String SAVE_DR = "photos";
    public static final String PARAM_COMMIT_OK = "param_commot_ok";
    public static final String EXIT = "exit";
    public static final String FOR_WAIT = "该功能还未开放";
    public static final String NOTIFICATION = "notification";
    public static final int NOTIFICATION_MAINTANCE_CODE = 11;
    public static final int NOTIFICATION_CHECK_CODE = 12;
    public static final String NOTIFICATION_MAINTANCE = "notification_maintance";
    public static final String NOTIFICATION_CHECK = "notification_check";
    private static final String ABSOLUTE_BASE_PATH_PREFIX = Environment.getExternalStorageDirectory().getPath()
            + "/logistics/";
    public static final String ABSOLUTE_LOG_PATH_PREFIX = ABSOLUTE_BASE_PATH_PREFIX + "Log/";

    public static final String UNCOMMIT = "unCommit";
    public static final String ORDER_DATA = "orderData";
    public static final String UNCOMMIT_DATA = "unCommitData";
    public static final String DB_NAME = "hgad";
    public static final int STOP = 110;
    public static final int WARM = 911;
    public static final String BITMAP = "bitmap";
    public static final String DATA = "data";
    public static final String PARAM_RESULT = "paramResult";
    public static final String FEEDBACK = "feedback";;
    public static final String DESCRIBE = "describe";
    public static final String REASON = "reason";
    public static final String SHELVE_OK = "shelve_ok";
    public static final String CANCEL_SHELVE = "cancel_shelve";
    public static final String REPAIR_FLAG = "repairFlag";
    public static final String REPAIR_NO = "repairNo";
    public static final String REPAIR_TEL = "repairTel";
    public static final String DEVICE_NAME = "deviceName";
    public static final String TASK_NO = "taskNo";
    public static final String TASK_FLAG = "checkFlag";
    public static final String REPAIR_TASK_TYPE = "taskType";
    public static final String REPAIR_EVENT_TYPE = "eventType";
    public static final String SIGN_STATE = "signState";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String PLAN_TIME = "planTime";
    public static final String TASK_NAME = "taskName";
    public static final String MAINTAIN_TYPE = "maintainType";
    public static final String EDIT_CONTENT = "editContent";
    public static final String OVER = "over";
    public static final String UNNORMAL = "unnormal";
    public static final String LEARN = "learn";
    public static final String COLLECTION = "colletion";
    public static final String PHOTOS = "photos";
    public static final String DEVICE = "device";
    public static final String NOTI_SHOCK = "notiShock";
    public static final String NOTI_SOUND = "notiSound";
    public static final String SETTING_FIRST = "settingFirst";
    public static boolean ISDEBUG = false;
    public static final String CHECK_TYPE = "checkType";
    public static final String TODAY = "today";
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String TEMPORARY = "temporary";
    public static final int IS_ADD = 110;
    public static final String HAS_ADD = "hasAdd";
    public static final String HANDWRITE_CACHE = "/handwriteCache/";
}
