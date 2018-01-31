package com.it.hgad.logisticsmanager.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.request.DataDictionaryRequest;
import com.it.hgad.logisticsmanager.bean.request.ForPushRequest;
import com.it.hgad.logisticsmanager.bean.request.LoginRequest;
import com.it.hgad.logisticsmanager.bean.response.DataDictionaryResponse;
import com.it.hgad.logisticsmanager.bean.response.ForPushResponse;
import com.it.hgad.logisticsmanager.bean.response.LoginResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;

import java.util.List;
import java.util.Set;

import cn.itcast.ecshop.act.BaseActivity;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String FIRST_IP = "59.173.12.6:80";
    public static final String CHECK_VALUE = "00002";
    public static final String MAINTAIN_VALUE = "00004";
    private EditText et_username;
    private EditText et_password;
    private String name;
    private String pwd;
    private ImageView iv_setting;
    private PopupWindow popupWindow;
    private Button btn_confirm;
    private EditText et_ip;
    private CheckBox cb_remenber;
    private TextView tv_outLine;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.EXIT)) {
                finish();
            }
        }
    };
    private Button btn_login;
    private CustomProgressDialog customProgressDialog;
    private ProgressDialog mProgressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private boolean notConnect = true;
    private RelativeLayout main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
//        CommonUtils.addLayoutListener(main,btn_login);
    }

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.EXIT);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initData() {
        registBroadcastReceiver(receiver);

        boolean is_pwd = SPUtils.getBoolean(LoginActivity.this, SPConstants.IS_PWD);
        // boolean is_login = sharedPreferences.getBoolean("is_login", false);
        if (is_pwd) {
            String name = SPUtils.getString(LoginActivity.this, SPConstants.NAME);
            String pwd = SPUtils.getString(LoginActivity.this, SPConstants.PWD);
            boolean autoLogin = SPUtils.getBoolean(LoginActivity.this, SPConstants.AUTO_LOGIN);
            if (autoLogin) {
                boolean checkNetWork = Utils.checkNetWork(this);
                if (checkNetWork) {
                    LoginRequest loginRequest = new LoginRequest(name, pwd);
                    sendRequest(loginRequest, LoginResponse.class);
                }
            }
            //3）回显在控件上
            et_username.setText(name);
            et_password.setText(pwd);
            //回显checkbox
            cb_remenber.setChecked(true);
        }
    }

    private void initView() {
        main = (RelativeLayout) findViewById(R.id.main);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        cb_remenber = (CheckBox) findViewById(R.id.cb_remenber);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof LoginRequest) {
            LoginResponse loginResponse = ((LoginResponse) response);
            if (loginResponse.getResult() != null) {
                if ("0".equals(loginResponse.getResult())) {
                    CommonUtils.showToast(LoginActivity.this, "登录成功！");
                    DataDictionaryRequest dataDictionaryRequest = new DataDictionaryRequest();
                    sendRequest(dataDictionaryRequest, DataDictionaryResponse.class);
                    final int userId = loginResponse.getUser().getId();
                    String realName = loginResponse.getUser().getRealName();
                    String userTel = loginResponse.getUser().getUserTel();
                    SPUtils.put(LoginActivity.this, SPConstants.AUTO_LOGIN, true);
                    SPUtils.put(LoginActivity.this, SPConstants.USER_ID, userId);
                    SPUtils.put(LoginActivity.this, SPConstants.REAL_NAME, realName);
                    SPUtils.put(LoginActivity.this, SPConstants.USER_TEL, userTel);
                    if (cb_remenber.isChecked()) {
                        SPUtils.put(LoginActivity.this, SPConstants.PWD, pwd);
                        SPUtils.put(LoginActivity.this, SPConstants.IS_PWD, true);
                    } else {
                        SPUtils.put(LoginActivity.this, SPConstants.PWD, "");
                        SPUtils.put(LoginActivity.this, SPConstants.IS_PWD, false);
                    }
                    SPUtils.put(LoginActivity.this, SPConstants.NAME, name);
                    SPUtils.put(LoginActivity.this, SPConstants.IS_ONLINE, true);
                    Intent intent = new Intent(LoginActivity.this, FragmentTabActivity.class);
                    startActivity(intent);
                    JPushInterface.setAlias(LoginActivity.this, userId + "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            if (i == 0) {
                                ForPushRequest forPushRequest = new ForPushRequest(s);
                                sendRequest(forPushRequest, ForPushResponse.class);
                            }
                            if (i == 6002) {
                            }
                        }
                    });
                    notConnect = false;
                } else if ("1".equals(loginResponse.getResult())) {
                    CommonUtils.showToast(LoginActivity.this, "账号或密码错误！");
                    notConnect = false;
                }
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        } else if (request instanceof DataDictionaryRequest) {
            DataDictionaryResponse dataDictionaryResponse = (DataDictionaryResponse) response;
            if (dataDictionaryResponse != null) {
                if (dataDictionaryResponse.getData() != null) {
                    DataDictionaryResponse.DataEntity data = dataDictionaryResponse.getData();
                    List<DataDictionaryResponse.DataEntity.ListdataEntity> listdata = data.getListdata();
                    for (DataDictionaryResponse.DataEntity.ListdataEntity entity : listdata
                            ) {
                        if (2 == entity.getId() && CHECK_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, CheckConstants.SHOULD, entity.getValue());
                        } else if (4 == entity.getId() && CHECK_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, CheckConstants.DONE, entity.getValue());
                        } else if (5 == entity.getId() && CHECK_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, CheckConstants.OVER, entity.getValue());
                        } else if (15 == entity.getId() && CHECK_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, CheckConstants.OVER_FINISH, entity.getValue());
                        } else if (18 == entity.getId() && CHECK_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, CheckConstants.CANCEL, entity.getValue());
                        } else if (9 == entity.getId() && MAINTAIN_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, RepairConstants.SHOULD_RECIEVE, entity.getValue());
                        } else if (10 == entity.getId() && MAINTAIN_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, RepairConstants.SHOULD_START, entity.getValue());
                        } else if (11 == entity.getId() && MAINTAIN_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, RepairConstants.SHOULD_DO, entity.getValue());
                        } else if (12 == entity.getId() && MAINTAIN_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, RepairConstants.HAVE_SHELVE, entity.getValue());
                        } else if (13 == entity.getId() && MAINTAIN_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, RepairConstants.HAVE_DONE, entity.getValue());
                        } else if (14 == entity.getId() && MAINTAIN_VALUE.equals(entity.getTypeValue())) {
                            SPUtils.put(LoginActivity.this, RepairConstants.HAVE_FINISH, entity.getValue());
                        }
                    }
                }
            }
        }
    }

    public void setting(View view) {
        initPopupwindow();
        popupWindow.showAsDropDown(iv_setting);
    }

    private void initPopupwindow() {
        View contentView = View.inflate(LoginActivity.this, R.layout.popup_setting, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff"))); //设置背景
        popupWindow.setFocusable(true); //设置获取焦点
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);
        et_ip = (EditText) contentView.findViewById(R.id.et_ip);
        String firstIp = SPUtils.getString(LoginActivity.this, SPConstants.IP);
        if (firstIp == null || ("").equals(firstIp)) {
            SPUtils.put(LoginActivity.this, SPConstants.IP, FIRST_IP);
        }
        String ip = SPUtils.getString(LoginActivity.this, SPConstants.IP);
        et_ip.setText(ip);
        btn_confirm = (Button) contentView.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(et_ip.getText().toString().trim())) {
                    String ip = et_ip.getText().toString().trim();
                    SPUtils.put(LoginActivity.this, SPConstants.IP, ip);
//                    LocalApp localApp = (LocalApp) getApplication();
//                    localApp.setIp(ip);
                    popupWindow.dismiss();
                } else {
                    CommonUtils.showToast(LoginActivity.this, "未输入IP地址哦！");
                }

                break;
            case R.id.btn_login:
                name = et_username.getText().toString().trim();
                pwd = et_password.getText().toString().trim();
                boolean checkNetWork = Utils.checkNetWork(this);

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                    CommonUtils.showToast(this, getString(R.string.not_null));
                    return;
                }
                if (checkNetWork) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (notConnect) {
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                    Utils.showTost(LoginActivity.this, "服务器连接错误！");
                                }
                            }
                        }
                    }, 5000);
                    mProgressDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.THEME_HOLO_DARK);
                    mProgressDialog.setMessage("正在登录...");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    Point size = new Point();
                    mProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                    int width = size.x;
                    int height = size.y;
                    WindowManager.LayoutParams params = mProgressDialog.getWindow().getAttributes();
                    params.gravity = Gravity.CENTER;
                    params.alpha = 0.8f;
                    params.height = height / 6;
                    params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
                    mProgressDialog.getWindow().setAttributes(params);
                    LoginRequest loginRequest = new LoginRequest(name, pwd);
                    sendRequest(loginRequest, LoginResponse.class);
//                    btn_login.setClickable(false);
                } else {
                    String OName = et_username.getText().toString().trim();
                    String OPwd = et_password.getText().toString().trim();
                    String LName = SPUtils.getString(LoginActivity.this, SPConstants.NAME);
                    String LPwd = SPUtils.getString(LoginActivity.this, SPConstants.PWD);
                    if (LName.equals(OName) & LPwd.equals(OPwd)) {
                        SPUtils.put(LoginActivity.this, SPConstants.IS_ONLINE, false);
                        Intent intent = new Intent(LoginActivity.this, FragmentTabActivity.class);
                        startActivity(intent);
//                    finish();
                    } else {
                        CommonUtils.showToast(LoginActivity.this, "账号或密码错误！");
                    }
                }
                break;
        }
    }
}
