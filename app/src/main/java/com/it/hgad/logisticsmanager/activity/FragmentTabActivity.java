package com.it.hgad.logisticsmanager.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.request.CheckVersionRequest;
import com.it.hgad.logisticsmanager.bean.response.CheckVersionResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.fragment.DataCenterFragment;
import com.it.hgad.logisticsmanager.fragment.NewHomeFragment;
import com.it.hgad.logisticsmanager.fragment.PerformanceFragment;
import com.it.hgad.logisticsmanager.fragment.UserFragment;
import com.it.hgad.logisticsmanager.service.PollingService;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CommonDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;

import static cn.itcast.ecshop.net.NetUtil.sendRequest;

/**
 * Created by Administrator on 2016/12/26.
 */
public class FragmentTabActivity extends FragmentActivity implements Callback {
    //    private RadioButton[] rbs = new RadioButton[4];
    protected static final int NEED_UPDATE = 0;
    private static final int DOWN_OK = 1; // 下载完成
    private static final int DOWN_ERROR = 2;
    private RadioGroup rg;
    //    private ArrayList<Fragment> fragments;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case NEED_UPDATE:
                    // 提醒用户是否更新
                    String url = (String) msg.obj;
                    String fileUrl = getFileName(url);
                    verifyStoragePermissions(FragmentTabActivity.this);
                    showUpdateDialog(fileUrl);
                    break;
                case DOWN_OK:
                    // 下载完成，点击安装
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                    notification.setLatestEventInfo(FragmentTabActivity.this, getString(R.string.app_name), "下载完成", pendingIntent);
                    contentView.setTextViewText(R.id.notificationTitle, "下载完成");
                    notificationManager.notify(notification_id, notification);
                    notificationManager.cancel(notification_id);
                    installApk(destFile);
//                    stopService(updateIntent);
                    break;
                case DOWN_ERROR:
//                    notification.setLatestEventInfo(FragmentTabActivity.this, getString(R.string.app_name), "下载失败", pendingIntent);
                    contentView.setTextViewText(R.id.notificationTitle, "下载失败");
                    break;
                default:
//                    stopService(updateIntent);
                    break;
            }
        }

        ;
    };
    private String ip;
    private PendingIntent pendingIntent;
    private Intent updateIntent;
    private Notification notification;
    private NotificationManager notificationManager;
    private File destFile;
    private int userId;
    private DbUtils db;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment mCurrentFragment = new Fragment();

    /**
     * 根据URL切割文件名
     *
     * @param url
     * @return
     */
    protected String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private void showUpdateDialog(final String url) {
//        LayoutInflater factory = LayoutInflater.from(FragmentTabActivity.this);//提示框
//        final View dialogView = factory.inflate(R.layout.dialog_title, null);//这里必须是final的
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
//        builder.setTitle("更新信息");
////        builder.setCustomTitle(dialogView);
//        builder.setMessage("已发布最新版本，建议立即更新使用");
//        AlertDialog alertDialog = builder.setPositiveButton("开始更新", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //下载APK 安装APK
//
//                Random random = new Random();
//                int nextInt = random.nextInt();
//                nextInt = Math.abs(nextInt);
//                // SD卡  内部存储
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    //SD卡存在
//                    destFile = new File(Environment.getExternalStorageDirectory(), nextInt + url);
//                } else {
//                    //SD卡不存在
//                    destFile = new File(getFilesDir(), nextInt + url);
//                }
//                int i = 0;
//                downLoadAPk(url, destFile);
//                dialog.dismiss();
//            }
//        }).setNegativeButton("暂不更新", null).create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
        final CommonDialog commonDialog = new CommonDialog(FragmentTabActivity.this, "更新信息", "已发布最新版本，建议立即更新使用", "开始更新", "暂不更新");
        commonDialog.setClicklistener(new CommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                Random random = new Random();
                int nextInt = random.nextInt();
                nextInt = Math.abs(nextInt);
                // SD卡  内部存储
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //SD卡存在
                    destFile = new File(Environment.getExternalStorageDirectory(), nextInt + url);
                } else {
                    //SD卡不存在
                    destFile = new File(getFilesDir(), nextInt + url);
                }
                int i = 0;
                downLoadAPk(url, destFile);
                commonDialog.dismiss();
            }

            @Override
            public void doCancel() {
                commonDialog.dismiss();
            }
        });
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.show();
    }

    private void downLoadAPk(final String url, final File destFile) {
        final HttpUtils httpUtils = new HttpUtils();
        HttpHandler<File> httpHandler = httpUtils.download(HttpConstants.APKFormatUrl(ip) + url, destFile.getPath(), true, true, new RequestCallBack<File>() {

            @Override
            public void onStart() {
                createNotification();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                double x_double = current * 1.0;
                double tempresult = x_double / total;
                DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
                // 百分比格式，后面不足2位的用0补齐
                String result = df1.format(tempresult);
                contentView.setTextViewText(R.id.notificationPercent, (int) (Float.parseFloat(result) * 100) + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, (int) (Float.parseFloat(result) * 100), false);
                notificationManager.notify(notification_id, notification);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
//                Utils.showTost(FragmentTabActivity.this, "下载完成");
                // 下载成功
                Message message = handler.obtainMessage();
                message.what = DOWN_OK;
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message message = handler.obtainMessage();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
//                Utils.showTost(FragmentTabActivity.this, "下载失败");
            }
        });
    }

    /***
     * 创建通知栏
     */
    RemoteViews contentView;
    private int notification_id = 1;

    @SuppressWarnings("deprecation")
    public void createNotification() {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
//        // 这个参数是通知提示闪出来的值.
        notification.tickerText = "开始下载";
        // pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

        // 这里面的参数是通知栏view显示的内容
//        notification.setLatestEventInfo(this, R.string.app_name, "下载：0%", pendingIntent);
        // notificationManager.notify(notification_id, notification);

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;

//        updateIntent = new Intent(this,LoginActivity.class);
//        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

//        notification.contentIntent = pendingIntent;
        notificationManager.notify(notification_id, notification);
    }

    /**
     * 安装apk的方法
     *
     * @param apkFile
     */
    public void installApk(File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //Uri.parse("file://"+apkFile.getAbsolutePath()) 以前的写法
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        //启动系统中的安装界面Activity  获取结果
        startActivityForResult(intent, 250);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //用户点击取消按钮取消安装  就会返回RESULT_CANCELED结果码
//        if (requestCode == 250 && resultCode == RESULT_CANCELED) {
////            enterMainUi();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
//        PollingUtils.startPollingService(this, 10, PollingService.class, PollingService.ACTION);
        Intent intent = new Intent(this, PollingService.class);
        startService(intent);
        setContentView(R.layout.activity_fragment);
        initView();
        initData();
        initFrangment();
        checkVersion();
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            CommonUtils.showToast(activity, "请允许权限进行更新下载");
        }

    }

    private void checkVersion() {
        String versionName = getVersionName();
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            CheckVersionRequest checkVersionRequest = new CheckVersionRequest("v" + versionName);
            sendRequest(checkVersionRequest, CheckVersionResponse.class, this);
        } else {

        }
    }

    private void initFrangment() {
//        fragments = new ArrayList<>();
    }

    private void initData() {
        ip = SPUtils.getString(this, SPConstants.IP);
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
//        final HomeFragment homeFragment = new HomeFragment();
        final NewHomeFragment newHomeFragment = new NewHomeFragment();
//        final MaintenanceFragment maintenanceFragment = new MaintenanceFragment();
        final DataCenterFragment dataCenterFragment = new DataCenterFragment();
//        final CheckFragment checkFragment = new CheckFragment();
        final PerformanceFragment performanceFragment = new PerformanceFragment();
        final UserFragment userFragment = new UserFragment();
//        rbs[0].setChecked(true);
//        replaceFragment(homeFragment);
//        fragments.add(newHomeFragment);
//        fragments.add(dataCenterFragment);
//        fragments.add(performanceFragment);
//        fragments.add(userFragment);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        addHideShow(newHomeFragment);
                        break;
                    case R.id.rb_data_centre:
                        addHideShow(dataCenterFragment);
                        break;
                    case R.id.rb_performance:
                        addHideShow(performanceFragment);
                        break;
                    case R.id.rb_user:
                        addHideShow(userFragment);
                        break;
                    default:
                        break;
                }
            }
        });
        ((RadioButton) rg.getChildAt(0)).setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = getIntent();
//        int intExtra = intent.getIntExtra(Constants.NOTIFICATION, -1);
//        if (intExtra == Constants.NOTIFICATION_MAINTANCE_CODE) {
//            rg.check(R.id.rb_data_centre);
//        } else if (intExtra == Constants.NOTIFICATION_CHECK_CODE) {
//            rg.check(R.id.rb_performance);
//        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        DbUtils dbUtils = DbUtils.create(this, null, Constants.DB_NAME);
//        if (db == null) {
//            db = LocalApp.getDb();
//        }
//        db.configAllowTransaction(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }

    private void initView() {
        rg = (RadioGroup) findViewById(R.id.rg);
    }


    // 第一次会创建，后续不会销毁，也不会创建
    private void addHideShow(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragments.contains(fragment)) {
            fragmentTransaction.add(R.id.fl, fragment);
            fragments.add(fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        if (fragments != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentTransaction ft = getSupportFragmentManager()
//                .beginTransaction();
//        ft.replace(R.id.fl, fragment)
//                .commit();
//    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            Intent intent = new Intent(Constants.EXIT);
            sendBroadcast(intent);
//            System.exit(0);
        }

    }

    /**
     * 获取版本号
     *
     * @return
     */
    private String getVersionName() {
        // PackageManager
        PackageManager pm = getPackageManager();
        // PackageInfo
        try {
            // flags 代表可以获取的包信息的内容 传0即可 因为任何Flag都可以获取版本号
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof CheckVersionRequest) {
            CheckVersionResponse checkVersionResponse = (CheckVersionResponse) response;
            if (checkVersionResponse != null) {
                if ("0".equals(checkVersionResponse.getResult())) {
                    String url = checkVersionResponse.getApkName();
                    if (url != null) {
                        String version = url.substring(url.lastIndexOf("v") + 1, url.lastIndexOf("v") + 6);
                        String versionName = getVersionName();
                        if (!version.equalsIgnoreCase(versionName)) {
                            Message message = handler.obtainMessage();
                            message.obj = url;
                            message.what = NEED_UPDATE;
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {

    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

    /**
     * 解决App重启后导致Fragment重叠的问题
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
