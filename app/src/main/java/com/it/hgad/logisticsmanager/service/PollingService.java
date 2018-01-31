package com.it.hgad.logisticsmanager.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.PullCheckResultActivity;
import com.it.hgad.logisticsmanager.activity.PullResultActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.CyclingCheckRequest;
import com.it.hgad.logisticsmanager.bean.request.CyclingPullRequest;
import com.it.hgad.logisticsmanager.bean.response.CheckOrderListResponse;
import com.it.hgad.logisticsmanager.bean.response.OrderListResponse;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckPullDb;
import com.it.hgad.logisticsmanager.dao.PullDb;
import com.it.hgad.logisticsmanager.receiver.PushReceiver;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/2/15.
 */
public class PollingService extends Service implements Callback {
    public static final String ACTION = "com.service.PollingService";

    //    private Notification mNotification;
    private int messageNotificationID = 100;
    private int userId;
    private DbUtils db;
    private PushReceiver pushReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//        getDbUtils();
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        initNotifiManager();
        pushReceiver = new PushReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(JPushInterface.ACTION_NOTIFICATION_RECEIVED);
        registerReceiver(pushReceiver,intentFilter);
    }

    public void getDbUtils() {
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(PullDb.class);
                db.createTableIfNotExist(CheckPullDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        boolean checkNetWork = Utils.checkNetWork(this);
//        if (checkNetWork) {
//            CyclingPullRequest cyclingPullRequest = new CyclingPullRequest(RepairConstants.RECIEVE_REPAIR, userId);
//            NetUtil.sendRequest(cyclingPullRequest, OrderListResponse.class, PollingService.this);
//            CyclingCheckRequest cyclingCheckRequest = new CyclingCheckRequest(userId, CheckConstants.SHOULD_CHECK);
//            NetUtil.sendRequest(cyclingCheckRequest, CheckOrderListResponse.class, PollingService.this);
////            for (int i = 0; i < 2; i++) {
////                double random = Math.random();
////                Order order = new Order("1","1","1","1","1","1",random+"","1","1",2,"1","1","1","1","1","1","1","1",i,"1","1");
////                showNotification(maintanceMessage+random,order);
////            }
//        }

        return super.onStartCommand(intent, flags, startId);
    }


    //初始化通知栏配置
    private void initNotifiManager() {

    }

    //弹出Notification
    private void showNotification(String content) {
        NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.mipmap.ic_launcher;
        Notification.Builder builder =  new Notification.Builder(this);
        builder.setSmallIcon(icon);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmap);
//        mNotification.icon = icon;
//        mNotification.tickerText = "New Message";
        builder.setContentTitle("医疗物业");
        builder.setDefaults(Notification.DEFAULT_SOUND);
//        mNotification.defaults |= Notification.DEFAULT_SOUND;
//        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        builder.setAutoCancel(true);
        Intent intent = null;
        if (content.contains(maintanceMessage)) {
            intent = new Intent(this, PullResultActivity.class);
//            intent.putExtra(Constants.NOTIFICATION_MAINTANCE, ((Order) obj));
        } else if (content.contains(checkMessage)) {
            intent = new Intent(this, PullCheckResultActivity.class);
//            intent.putExtra(Constants.NOTIFICATION_CHECK, ((CheckOrder) obj));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) SystemClock.uptimeMillis(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setShowWhen(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(content);
        Notification notification = builder.build();
//        mNotification.when = System.currentTimeMillis();
        //Navigator to the new activity when click the notification title


//        mNotification.setLatestEventInfo(this,
//                getResources().getString(R.string.app_name), "You have new message!", pendingIntent);
        // 每次通知完，通知ID递增一下，避免消息覆盖掉
        mManager.notify(messageNotificationID, notification);
        messageNotificationID++;
    }


    private String maintanceMessage = "新的维修单";
    private String needRecieve = "需要接收";
    private String checkMessage = "新的巡检单";

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof CyclingPullRequest) {
            final OrderListResponse orderListResponse = (OrderListResponse) response;
            if (orderListResponse != null) {
                ExecutorService thread = SingleThreadPool.getThread();
                thread.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<PullDb> all = db.findAll(PullDb.class);
                            boolean have;
                            List<OrderListResponse.DataEntity.ListdataEntity> listdata = orderListResponse.getData().getListdata();

                            for (OrderListResponse.DataEntity.ListdataEntity entity : listdata) {
                                have = false;
                                int id = entity.getId();
                                if (all != null && all.size() != 0) {
                                    for (PullDb pullDb : all) {
                                        if (pullDb.getRepairId() == id) {
                                            have = true;
                                        }
                                    }
                                }
                                if (!have) {
                                    Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                                            entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
                                    PullDb newPullDb = new PullDb(id);
                                    showNotification(maintanceMessage + entity.getRepairNo() + needRecieve);
                                    db.saveOrUpdate(newPullDb);
                                }
                            }

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else if (request instanceof CyclingCheckRequest) {
            final CheckOrderListResponse checkOrderListResponse = (CheckOrderListResponse) response;
            if (checkOrderListResponse != null) {
                ExecutorService thread = SingleThreadPool.getThread();
                thread.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<CheckPullDb> all = db.findAll(CheckPullDb.class);
                            List<CheckOrderListResponse.DataEntity.ListdataEntity> listdata = checkOrderListResponse.getData().getListdata();
                            for (CheckOrderListResponse.DataEntity.ListdataEntity task : listdata) {
                                CheckOrderListResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
                                int taskId = task.getDeviceTaskResult().getId();
                                boolean have = false;
                                int id = entity.getId();
                                if (all != null && all.size() != 0) {
                                    for (CheckPullDb checkPullDb : all) {
                                        if (checkPullDb.getCheckId() == id) {
                                            have = true;
                                        }
                                    }
                                }
                                if (!have) {
                                    CheckOrder checkOrder = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName()
                                            , entity.getInspectorName(), entity.getArrageId(), entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(),taskId);
                                    CheckPullDb newCheckDb = new CheckPullDb(id);
                                    db.saveOrUpdate(newCheckDb);
                                    showNotification(checkMessage + entity.getTaskCode() + needRecieve);
                                }
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {

    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pushReceiver);
    }
}
