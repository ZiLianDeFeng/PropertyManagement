package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.request.AllOrderSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.CommitRequest;
import com.it.hgad.logisticsmanager.bean.response.AllOrderSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.CommitResponse;
import com.it.hgad.logisticsmanager.bean.response.UpLoadPicResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.fragment.ShelveFragment;
import com.it.hgad.logisticsmanager.fragment.ShouldFragment;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.act.BaseActivity;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.NetUtil;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/19.
 */
public class MaintainTabActivity extends BaseActivity implements View.OnClickListener {

    private static final int RESULT = 112;
    private RadioGroup rg_title;
    private RadioButton rb_should;
    private RadioButton rb_shelve;
    private ShouldFragment shouldFragment;
    private ShelveFragment shelveFragment;
    private RelativeLayout rl_no_net;
    private int userId;
    private ImageView iv_red;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.SHELVE_OK) || action.equals(Constants.CANCEL_SHELVE)) {
                getMaintanceData();
            }
        }
    };
    private IntenterBoradCastReceiver netReceiver;
    private DbUtils db;
    private String haveDone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_tab);
        initView();
        initData();
    }

    //监听网络状态变化的广播接收器
    public class IntenterBoradCastReceiver extends BroadcastReceiver {

        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络

                    }
                    rl_no_net.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Commit();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    ////////网络断开
                    rl_no_net.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    private void registerNetBroadrecevicer() {
        //获取广播对象
        netReceiver = new IntenterBoradCastReceiver();
        //创建意图过滤器
        IntentFilter filter = new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, filter);
    }

    private void initData() {
        if (db == null) {
            db = LocalApp.getDb();
        }
        Intent intent = getIntent();
        String maintainType = intent.getStringExtra(Constants.MAINTAIN_TYPE);
        if (maintainType != null && maintainType.contains(WarningActivity.SHELVE)) {
            ((RadioButton) rg_title.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) rg_title.getChildAt(0)).setChecked(true);
        }
        registerReceiver();
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        getMaintanceData();
    }

    private void initView() {
        haveDone = SPUtils.getString(this, RepairConstants.HAVE_DONE);
        rg_title = (RadioGroup) findViewById(R.id.rg_title);
        rb_should = (RadioButton) findViewById(R.id.rb_should);
        rb_shelve = (RadioButton) findViewById(R.id.rb_shelve);
        rg_title.setOnCheckedChangeListener(listener);
        shouldFragment = new ShouldFragment();
        shelveFragment = new ShelveFragment();
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        LinearLayout ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_search.setOnClickListener(this);
        iv_red = (ImageView) findViewById(R.id.iv_red);
        rl_no_net = (RelativeLayout) findViewById(R.id.rl_no_net);
    }

    private void Commit() throws DbException {
        boolean have = false;
        final List<CommitDb> commitDbs = db.findAll(CommitDb.class);
        if (commitDbs != null && commitDbs.size() != 0) {
            for (int i = 0; i < commitDbs.size(); i++) {
                CommitDb commitDb = commitDbs.get(i);
                if (commitDb.getHasCommit() == 1 && commitDb.getUserId() == userId) {
                    have = true;
                }
            }
            if (have) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(MaintainTabActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                .setTitle("有之前未提交的维修单")//提示框标题
                                .setPositiveButton("提交",//提示框的两个按钮
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
//                                                事件
//                                                isCommiting = true;
                                                CommonUtils.showToast(MaintainTabActivity.this, "已在后台提交中，提交过程时间可能比较长");
                                                for (int i = 0; i < commitDbs.size(); i++) {
                                                    final CommitDb commitDb = commitDbs.get(i);
                                                    if (commitDb.getHasCommit() == 1 && commitDb.getUserId() == userId) {
                                                        final int repairId = commitDb.getRepairId();
                                                        ExecutorService poolThread = SingleThreadPool.getPoolThread();
                                                        poolThread.execute(new Runnable() {
                                                            private String handWrite;
                                                            private List<String> materialPrices = new ArrayList<String>();
                                                            private List<String> materialNums = new ArrayList<String>();
                                                            private List<String> materialTypes = new ArrayList<String>();
                                                            private List<String> materialNames = new ArrayList<String>();
                                                            private List<String> materialIds = new ArrayList<String>();

                                                            @Override
                                                            public void run() {
                                                                List<String> imgPaths = CommonUtils.cast2List(commitDb.getRepairImgPath());
                                                                List<String> imgPathsBefore = CommonUtils.cast2List(commitDb.getSpotImg());
                                                                List<String> repairImgPath = new ArrayList<>();
                                                                List<String> repairImgPathBefore = new ArrayList<>();
                                                                if (imgPaths != null && imgPaths.size() != 0) {
                                                                    for (String path : imgPaths) {
                                                                        if (path != null && !("").equals(path)) {
                                                                            File file = new File(path.trim());
                                                                            try {
                                                                                UpLoadPicResponse upLoadPicResponse = upLoadPic(file, repairId);
                                                                                repairImgPath.add(upLoadPicResponse.getPath());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (imgPathsBefore != null && imgPathsBefore.size() != 0) {
                                                                    for (String path : imgPathsBefore) {
                                                                        if (path != null && !("").equals(path)) {
                                                                            File file = new File(path.trim());
                                                                            try {
                                                                                UpLoadPicResponse upLoadPicResponse = upLoadPic(file, repairId);
                                                                                repairImgPathBefore.add(upLoadPicResponse.getPath());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                String handWrite = commitDb.getHandWrite();
                                                                String signImg = "";
                                                                if (handWrite != null && !"".equals(handWrite)) {
                                                                    File file = new File(handWrite.trim());
                                                                    try {
                                                                        UpLoadPicResponse upLoadPicResponse = upLoadSign(file, repairId);
                                                                        signImg = upLoadPicResponse.getPath();
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                String repairReply = commitDb.getRepairReply();
                                                                materialIds = CommonUtils.cast2List(commitDb.getRepairMaterialId());
                                                                materialNames = CommonUtils.cast2List(commitDb.getRepairMaterialName());
                                                                materialTypes = CommonUtils.cast2List(commitDb.getRepairMaterialType());
                                                                materialNums = CommonUtils.cast2List(commitDb.getRepairMaterialNum());
                                                                materialPrices = CommonUtils.cast2List(commitDb.getRepairMaterialPrice());
                                                                String sameCondition = commitDb.getSameCondition();
                                                                if (sameCondition == null) {
                                                                    sameCondition = "";
                                                                }
                                                                String satisfy = commitDb.getSatisfy();
                                                                String startTime = commitDb.getStartTime();
                                                                if (startTime == null) {
                                                                    startTime = "";
                                                                }
                                                                String delayTime = commitDb.getDelayTime();
                                                                if (delayTime == null) {
                                                                    delayTime = "";
                                                                }
                                                                String finishTime = commitDb.getFinishTime();
                                                                if (finishTime == null) {
                                                                    finishTime = "";
                                                                }
                                                                CommitRequest commitRequest = new CommitRequest(userId + "", repairId + "", repairReply, materialIds, materialNames, materialTypes, materialNums, repairImgPath, repairImgPathBefore, materialPrices, sameCondition, satisfy, delayTime, startTime, finishTime, signImg);
                                                                NetUtil.sendRequest(commitRequest, CommitResponse.class, MaintainTabActivity.this);
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }).setNegativeButton("取消", null).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                });
            }
        }
    }

    private OkHttpClient client = new OkHttpClient();

    private UpLoadPicResponse upLoadPic(File file, int id) throws IOException {
        String ip = SPUtils.getString(this, SPConstants.IP);
        String RequestURL = HttpConstants.HOST + ip + HttpConstants.UPLOADPIC;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("repair.id", id + "");
        builder.addFormDataPart("imgFileName", file.getName());
        builder.addFormDataPart("imgContentType", MaintenanceActivity.MEDIA_TYPE_PNG + "");
        builder.addFormDataPart("img", file.getName(), RequestBody.create(MaintenanceActivity.MEDIA_TYPE_PNG, file));
        MultipartBody body = builder.build();
        Request request = new Request.Builder().url(RequestURL).post(body).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body().string();
        UpLoadPicResponse upLoadPicResponse = null;
        if (responseStr != null) {
            upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
        }
        return upLoadPicResponse;
    }

    private UpLoadPicResponse upLoadSign(File file, int id) throws IOException {
        String ip = SPUtils.getString(this, SPConstants.IP);
        String RequestURL = HttpConstants.HOST + ip + HttpConstants.UPLOADSIGN;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("repair.id", id + "");
        builder.addFormDataPart("imgFileName", file.getName());
        builder.addFormDataPart("imgContentType", MaintenanceActivity.MEDIA_TYPE_PNG + "");
        builder.addFormDataPart("img", file.getName(), RequestBody.create(MaintenanceActivity.MEDIA_TYPE_PNG, file));
        MultipartBody body = builder.build();
        Request request = new Request.Builder().url(RequestURL).post(body).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body().string();
        UpLoadPicResponse upLoadPicResponse = null;
        if (responseStr != null) {
            upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
        }
        return upLoadPicResponse;
    }

    private void getMaintanceData() {
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            AllOrderSizeRequest allOrderSizeRequest = new AllOrderSizeRequest(userId);
            sendRequest(allOrderSizeRequest, AllOrderSizeResponse.class);
        } else {

        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.CANCEL_SHELVE);
        intentFilter.addAction(Constants.SHELVE_OK);
        registerReceiver(receiver, intentFilter);
        registerNetBroadrecevicer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (netReceiver != null) {
            unregisterReceiver(netReceiver);
            netReceiver = null;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl, fragment)
                .commit();
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_should:
                    if (shouldFragment.isAdded()) {
                        replaceFragment(shelveFragment, shouldFragment);
                    } else {
                        replaceFragment(shouldFragment);
                    }
                    break;
                case R.id.rb_shelve:
                    if (shelveFragment.isAdded()) {
                        replaceFragment(shouldFragment, shelveFragment);
                    } else {
                        replaceFragment(shelveFragment);
                    }
                    break;
            }
        }
    };

    public void replaceFragment(Fragment from, Fragment to) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (!to.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.hide(from).add(R.id.fl, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof AllOrderSizeRequest) {
            AllOrderSizeResponse allOrderSizeResponse = (AllOrderSizeResponse) response;
            if (allOrderSizeResponse.getData() != null) {
                AllOrderSizeResponse.DataEntity data = allOrderSizeResponse.getData();
                int shave = data.getShave();
                if (shave == 0) {
                    iv_red.setVisibility(View.GONE);
                } else {
                    iv_red.setVisibility(View.VISIBLE);
                }
            }
        } else if (request instanceof CommitRequest) {
            CommitResponse commitResponse = (CommitResponse) response;
            if (commitResponse != null) {
                if ("0".equals(commitResponse.getResult())) {
                    int commitId = commitResponse.getId();
                    try {
                        final CommitDb commitDb = db.findById(CommitDb.class, commitId);
                        commitDb.setHasCommit(2);
                        final OrderDb orderDb = db.findById(OrderDb.class, commitId);
                        orderDb.setRepairFlag(Integer.parseInt(haveDone));
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(commitDb);
                                    db.saveOrUpdate(orderDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    sendBroadcast();
                }
            }
        }
    }

    private void sendBroadcast() {
        Intent intent = new Intent(Constants.COMMIT_OK);
        sendBroadcast(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_search:
                go2Search();
                break;
        }
    }

    private void go2Search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
