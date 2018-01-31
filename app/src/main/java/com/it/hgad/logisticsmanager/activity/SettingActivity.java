package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.request.LoginOutRequset;
import com.it.hgad.logisticsmanager.bean.response.LoginOutResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.service.PollingService;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.DataClearManager;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.Set;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2017/2/5.
 */
public class SettingActivity extends CommonActivity implements View.OnClickListener {

    public static final String HANDWRITE_CACHE = "/handwriteCache/";
    private TextView tv_loginout;
    private RelativeLayout rl_version;
    private RelativeLayout rl_clear;
    private RelativeLayout rl_change_pwd;
    private RelativeLayout rl_photo;
    private RelativeLayout rl_wifi;
    private DbUtils db;
    private int userId;
    private RelativeLayout rl_zxing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initHeader("设置");
        initView();
        initData();
    }

    private void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        if (db == null) {
            db = LocalApp.getDb();
        }
    }

    private void initView() {
        tv_loginout = (TextView) findViewById(R.id.tv_loginout);
        tv_loginout.setOnClickListener(this);
        rl_version = (RelativeLayout) findViewById(R.id.rl_version);
        rl_version.setOnClickListener(this);
        rl_clear = (RelativeLayout) findViewById(R.id.rl_clear);
        rl_clear.setOnClickListener(this);
        rl_change_pwd = (RelativeLayout) findViewById(R.id.rl_change_pwd);
        rl_change_pwd.setOnClickListener(this);
        rl_photo = (RelativeLayout) findViewById(R.id.rl_photo);
        rl_photo.setOnClickListener(this);
        RelativeLayout rl_system_setting = (RelativeLayout) findViewById(R.id.rl_system_setting);
        rl_system_setting.setOnClickListener(this);
//        rl_wifi = (RelativeLayout) findViewById(R.id.rl_wifi);
//        rl_wifi.setOnClickListener(this);
        RelativeLayout rl_zxing = (RelativeLayout) findViewById(R.id.rl_zxing);
        rl_zxing.setOnClickListener(this);
        RelativeLayout rl_syn = (RelativeLayout) findViewById(R.id.rl_syn);
        rl_syn.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof LoginOutRequset) {
            LoginOutResponse loginOutResponse = (LoginOutResponse) response;
            if (loginOutResponse != null) {
                if ("success".equals(loginOutResponse.getMes())) {
                    CommonUtils.showToast(SettingActivity.this, "注销成功！");
//                finish();
                    JPushInterface.setAlias(SettingActivity.this, "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                        }
                    });
                    Intent serviceIntent = new Intent(SettingActivity.this, PollingService.class);
                    stopService(serviceIntent);
                    SPUtils.put(SettingActivity.this, SPConstants.AUTO_LOGIN, false);
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_loginout:
                loginOut();
                break;
            case R.id.rl_version:
                toVersion();
                break;
            case R.id.rl_clear:
                clearCookie();
                break;
            case R.id.rl_change_pwd:
                changePwd();
                break;
            case R.id.rl_photo:
                goToPhoto();
                break;
            case R.id.rl_syn:
                go2Syn();
                break;
            case R.id.rl_zxing:
                goToZxing();
                break;
            case R.id.rl_system_setting:
                go2SysSetting();
                break;
        }
    }

    private void go2SysSetting() {
        Intent intent = new Intent(SettingActivity.this, SystemSettingActivity.class);
        startActivity(intent);
    }

    private void go2Syn() {
        Intent intent = new Intent(SettingActivity.this, SynchronizationActivity.class);
        startActivity(intent);
    }

    private void goToZxing() {
        Utils.showTost(SettingActivity.this, Constants.FOR_WAIT);
//        Intent intent = new Intent(this,DemoActivity.class);
//        startActivity(intent);
    }

    private void goToPhoto() {
        Utils.showTost(SettingActivity.this, Constants.FOR_WAIT);
    }

    private void changePwd() {
        Utils.showTost(SettingActivity.this, Constants.FOR_WAIT);
//        ClearRequest clearRequest = new ClearRequest(userId+"");
//        sendRequest(clearRequest,ClearResponse.class);
    }

    private void clearCookie() {
        new AlertDialog.Builder(SettingActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle("清除所有缓存")//提示框标题
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //事件
                                try {
                                    DataClearManager.cleanApplicationData(SettingActivity.this);
//                                    db.close();
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
//                                Utils.showTost(SettingActivity.this,Constants.FOR_WAIT);
                            }
                        }).setNegativeButton("取消", null).create().show();
    }


    private void toVersion() {
        Intent intent = new Intent(this, VersionActivity.class);
        startActivity(intent);
    }

    private void loginOut() {
        new AlertDialog.Builder(SettingActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle("注销")//提示框标题
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //事件
                                boolean checkNetWork = Utils.checkNetWork(SettingActivity.this);
                                if (checkNetWork) {
                                    LoginOutRequset loginOutRequset = new LoginOutRequset();
                                    sendRequest(loginOutRequset, LoginOutResponse.class);
                                } else {
                                    Utils.showTost(SettingActivity.this, getString(R.string.no_net));
                                }
                            }
                        }).setNegativeButton("取消", null).create().show();
    }
}
