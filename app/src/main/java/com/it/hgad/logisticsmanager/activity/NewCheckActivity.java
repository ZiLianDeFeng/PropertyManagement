package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.NewParamDbEntity;
import com.it.hgad.logisticsmanager.bean.Param;
import com.it.hgad.logisticsmanager.bean.ParamDbEntity;
import com.it.hgad.logisticsmanager.bean.request.CancelCheckRequest;
import com.it.hgad.logisticsmanager.bean.request.ParamCommitRequest;
import com.it.hgad.logisticsmanager.bean.request.TempParamCommitRequest;
import com.it.hgad.logisticsmanager.bean.response.CancelCheckResponse;
import com.it.hgad.logisticsmanager.bean.response.ParamCommitResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CancelCheckDb;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.ParamDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class NewCheckActivity extends CommonActivity {

    private static final int PARAM = 100;
    private static final int DESCRIBE = 130;
    private LinearLayout ll_more;
    private PopupWindow popupWindow;
    private CheckOrder checkOrder;
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;
    private DbUtils db;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
//                    infoOperating.setVisibility(INVISIBLE);
//                    infoOperating.clearAnimation();
//                    checkAdapter.notifyDataSetChanged();
                    break;
                case Constants.WARM:
                    Utils.showTost(NewCheckActivity.this, getString(R.string.no_net) + "\n该次巡检会在有网络信号时自动取消");
                    finish();
                    break;
            }
        }
    };
    private TextView tv_number;
    private TextView tv_device_name;
    private TextView tv_start_time;
    private TextView tv_duty;
    private TextView tv_check_man;
    private List<Param> params = new ArrayList<>();
    private boolean net_connet;
    private int userId;
    private int taskId;
    private String taskCode;
    private String checkType;
    private int resultId;
    private TextView tv_describe;
    private ImageView iv_copy;
    private LinearLayout ll_cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new);
        initHeader("巡检任务");
        initView();
        initData();
    }

    private void initData() {
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(CheckCommitDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        Intent intent = getIntent();
        checkOrder = (CheckOrder) intent.getSerializableExtra(Constants.DATA);
        checkType = intent.getStringExtra(Constants.CHECK_TYPE);
        tv_device_name.setText(checkOrder.getDeviceName());
        String pTime = checkOrder.getPlanTime();
        if (pTime != null) {
            pTime = pTime.replace("T", " ");
        }
        tv_start_time.setText(pTime);
        tv_number.setText(checkOrder.getTaskCode());
        tv_duty.setText(checkOrder.getResponser());
        String inspectorNames = checkOrder.getInspectorName();
        inspectorNames = inspectorNames.replace(',', ' ').trim();
        tv_check_man.setText(inspectorNames);
        taskId = checkOrder.getId();
        resultId = checkOrder.getTaskId();
        taskCode = checkOrder.getTaskCode();
        try {
            CheckCommitDb checkCommitDb = db.findById(CheckCommitDb.class, this.taskId);
            if (checkCommitDb != null) {
                String feedback = checkCommitDb.getFeedback();
                if (feedback != null) {
                    describe = feedback;
                }
            } else {
                checkCommitDb = new CheckCommitDb();
                checkCommitDb.setTaskId(taskId);
                db.saveOrUpdate(checkCommitDb);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        tv_describe.setText(describe.length() > 3 ? describe.substring(0, 3) + "..." : describe);
        if (CheckConstants.TEMP.equals(checkType)) {
            ll_cancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            CheckCommitDb checkCommitDb = db.findById(CheckCommitDb.class, taskId);
            if (checkCommitDb != null) {
                String feedback = checkCommitDb.getFeedback();
                if (feedback != null) {
                    describe = feedback;
                }
            } else {
                checkCommitDb = new CheckCommitDb();
                checkCommitDb.setTaskId(taskId);
                db.saveOrUpdate(checkCommitDb);
            }
            ParamDb paramDb = db.findById(ParamDb.class, taskId);
            boolean cannotCommit = false;
            if (paramDb != null) {
                if (paramDb.getReferenceTypeId() == null || "".equals(paramDb.getReferenceTypeId())) {
                    iv_copy.setImageResource(R.mipmap.meter_reading);
                } else {
                    String paramDbParams = paramDb.getParams();
//            paramDbParams = "{\"data\" : " + paramDbParams + "}";
                    if (CheckConstants.TEMP.equals(checkType)) {
                        ParamDbEntity paramDbEntity = new Gson().fromJson(paramDbParams, ParamDbEntity.class);
                        List<ParamDbEntity.DataEntity> data = paramDbEntity.getData();
                        for (ParamDbEntity.DataEntity entity : data
                                ) {
                            String actualValue = entity.getActualValue();
                            if (actualValue == null || "".equals(actualValue)) {
                                cannotCommit = true;
                                break;
                            }
                        }
                    } else {
                        NewParamDbEntity paramDbEntity = new Gson().fromJson(paramDbParams, NewParamDbEntity.class);
                        List<NewParamDbEntity.DeviceParamSetEntity> deviceParamSet = paramDbEntity.getDeviceParamSet();
                        for (NewParamDbEntity.DeviceParamSetEntity entity : deviceParamSet
                                ) {
                            if (paramDb.getReferenceTypeId().equals(entity.getReferenceTypeId())) {
                                String actualValue = entity.getActualValue();
                                if (actualValue == null || "".equals(actualValue)) {
                                    cannotCommit = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (cannotCommit) {
                        iv_copy.setImageResource(R.mipmap.meter_reading);
                    } else {
                        iv_copy.setImageResource(R.mipmap.complete);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        tv_describe.setText(describe.length() > 3 ? describe.substring(0, 3) + "..." : describe);
    }

    private void initView() {
        should = SPUtils.getString(this, CheckConstants.SHOULD);
        done = SPUtils.getString(this, CheckConstants.DONE);
        over = SPUtils.getString(this, CheckConstants.OVER);
        overFinish = SPUtils.getString(this, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(this, CheckConstants.CANCEL);
        ImageView iv_more = (ImageView) findViewById(R.id.search);
        iv_more.setImageResource(R.mipmap.and);
        ll_more = (LinearLayout) findViewById(R.id.ll_search);
        ll_more.setVisibility(View.VISIBLE);
        ll_more.setOnClickListener(this);
        initPopupWindow();
        LinearLayout ll_copy = (LinearLayout) findViewById(R.id.ll_copy);
        ll_copy.setOnClickListener(this);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_cancel.setOnClickListener(this);
        LinearLayout ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        ll_photo.setOnClickListener(this);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_device_name = (TextView) findViewById(R.id.tv_device_name);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_duty = (TextView) findViewById(R.id.tv_duty);
        tv_check_man = (TextView) findViewById(R.id.tv_check_man);
//        tv_address = (TextView) findViewById(R.id.tv_address);
        TextView btn_commit = (TextView) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
//        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(this);
        RelativeLayout rl_describe = (RelativeLayout) findViewById(R.id.rl_describe);
        rl_describe.setOnClickListener(this);
        tv_describe = (TextView) findViewById(R.id.tv_describe);
        iv_copy = (ImageView) findViewById(R.id.iv_copy);
    }

    private void initPopupWindow() {
        View contentView = View.inflate(NewCheckActivity.this, R.layout.popup_check, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
        popupWindow.setFocusable(true); //设置获取焦点
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof ParamCommitRequest) {
            ParamCommitResponse paramCommitResponse = (ParamCommitResponse) response;
            if (paramCommitResponse != null) {
                if ("0".equals(paramCommitResponse.getResult())) {
                    CommonUtils.showToast(NewCheckActivity.this, "提交成功");
                    int id = paramCommitResponse.getId();
                    try {
                        CheckCommitDb commitDb = db.findById(CheckCommitDb.class, id);
                        commitDb.setHasCommit(2);
                        CheckOrderDb orderDb = db.findById(CheckOrderDb.class, id);
                        orderDb.setTaskStatus(done);
                        db.saveOrUpdate(commitDb);
                        db.saveOrUpdate(orderDb);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    finish();
                    sendBroadcast();
                } else if ("1".equals(paramCommitResponse.getResult())) {
                    CommonUtils.showToast(NewCheckActivity.this, "提交失败");
                }
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                net_connet = false;
            }
        } else if (request instanceof CancelCheckRequest) {
            CancelCheckResponse cancelCheckResponse = (CancelCheckResponse) response;
            if (cancelCheckResponse != null) {
                if ("0".equals(cancelCheckResponse.getResult())) {
                    CommonUtils.showToast(NewCheckActivity.this, "取消成功");
                    finish();
                    sendBroadcast();
                } else {
                    CommonUtils.showToast(NewCheckActivity.this, "取消失败");
                }
            }
        } else if (request instanceof TempParamCommitRequest) {
            ParamCommitResponse paramCommitResponse = (ParamCommitResponse) response;
            if (paramCommitResponse != null) {
                if ("0".equals(paramCommitResponse.getResult())) {
                    CommonUtils.showToast(NewCheckActivity.this, "提交成功");
                    finish();
                    sendBroadcast();
                } else if ("1".equals(paramCommitResponse.getResult())) {
                    CommonUtils.showToast(NewCheckActivity.this, "提交失败");
                }
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                net_connet = false;
            }
        }
    }

    private void sendBroadcast() {
        Intent intent = new Intent(Constants.PARAM_COMMIT_OK);
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                popupWindow.showAsDropDown(ll_more);
                backgroundAlpha(0.8f);
                break;
            case R.id.ll_copy:
                go2Copy();
                break;
            case R.id.ll_cancel:
                cancelCheck();
                break;
            case R.id.ll_photo:
                takePhoto();
                break;
            case R.id.btn_commit:
                try {
                    commit();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_describe:
                describe();
                break;
        }

    }

    private void takePhoto() {
        CommonUtils.showToast(this, Constants.FOR_WAIT);
    }


    private String describe = "";
    private CustomProgressDialog customProgressDialog;

    private void describe() {
        Intent intent = new Intent(NewCheckActivity.this, ResultDescribeActivity.class);
        intent.putExtra(Constants.ORDER_DATA, checkOrder);
        startActivityForResult(intent, DESCRIBE);
    }

    private List<String> actualValues = new ArrayList<>();

    private void commit() throws DbException {
        final ParamDb paramDb = db.findById(ParamDb.class, taskId);
        boolean cannotCommit = false;
        if (paramDb != null) {
            if (paramDb.getReferenceTypeId() == null || "".equals(paramDb.getReferenceTypeId())) {
                CommonUtils.showToast(NewCheckActivity.this, "还有参数未输入参数值哦！");
            } else {
                String paramDbParams = paramDb.getParams();
//            paramDbParams = "{\"data\" : " + paramDbParams + "}";
                if (CheckConstants.TEMP.equals(checkType)) {
                    ParamDbEntity paramDbEntity = new Gson().fromJson(paramDbParams, ParamDbEntity.class);
                    List<ParamDbEntity.DataEntity> data = paramDbEntity.getData();
                    for (ParamDbEntity.DataEntity entity : data
                            ) {
                        String actualValue = entity.getActualValue();
                        if (actualValue == null || "".equals(actualValue)) {
                            cannotCommit = true;
                            break;
                        }
                        String referenceName = entity.getReferenceName();
                        String referenceStartValue = entity.getReferenceStartValue();
                        String referenceEndValue = entity.getReferenceEndValue();
                        String referenceUnit = entity.getReferenceUnit();
                        String remark = entity.getRemark();
                        Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark);
                        params.add(param);
                    }
                } else {
                    NewParamDbEntity paramDbEntity = new Gson().fromJson(paramDbParams, NewParamDbEntity.class);
                    List<NewParamDbEntity.DeviceParamSetEntity> deviceParamSet = paramDbEntity.getDeviceParamSet();
                    for (NewParamDbEntity.DeviceParamSetEntity entity : deviceParamSet
                            ) {
                        if (paramDb.getReferenceTypeId().equals(entity.getReferenceTypeId())) {
                            String actualValue = entity.getActualValue();
                            if (actualValue == null || "".equals(actualValue)) {
                                cannotCommit = true;
                                break;
                            }
                            String referenceName = entity.getReferenceName();
                            String referenceStartValue = entity.getReferenceStartValue();
                            String referenceEndValue = entity.getReferenceEndValue();
                            String referenceUnit = entity.getReferenceUnit();
                            String remark = entity.getRemark();
                            String referenceTypeId = entity.getReferenceTypeId();
                            Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark, referenceTypeId);
                            params.add(param);
                        }
                    }
                }
//            for (Param param : params) {
//                String actualValue = param.getActualValue();
//                if (actualValue == null || "".equals(actualValue)) {
//                    cannotCommit = true;
//                    break;
//                }
//            }
                if (cannotCommit) {
                    CommonUtils.showToast(NewCheckActivity.this, "还有参数未输入参数值哦！");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setTitle("提示");
                    builder.setMessage("是否确定提交");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String currentTime = CommonUtils.getCurrentTime();
                            for (int i = 0; i < params.size(); i++) {
                                Param param = params.get(i);
                                String actualValue = param.getActualValue();
                                actualValues.add(actualValue);
                            }
                            boolean checkNetWork = Utils.checkNetWork(NewCheckActivity.this);
                            if (checkNetWork) {
                                net_connet = true;
                                customProgressDialog = new CustomProgressDialog(NewCheckActivity.this, "数据提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
                                customProgressDialog.setCancelable(false);
                                customProgressDialog.setCanceledOnTouchOutside(false);
                                customProgressDialog.show();
                                Point size = new Point();
                                customProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                                int width = size.x;
                                int height = size.y;
                                WindowManager.LayoutParams params = customProgressDialog.getWindow().getAttributes();
                                params.gravity = Gravity.CENTER;
                                params.alpha = 0.8f;
//                        params.height = height/6;
                                params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
                                customProgressDialog.getWindow().setAttributes(params);
                                if (CheckConstants.TEMP.equals(checkType)) {
                                    TempParamCommitRequest tempParamCommitRequest = new TempParamCommitRequest(userId + "", resultId + "", actualValues, describe, currentTime);
                                    sendRequest(tempParamCommitRequest, ParamCommitResponse.class);
                                } else {
                                    ParamCommitRequest paramCommitRequest = new ParamCommitRequest(userId + "", resultId + "", actualValues, describe, currentTime, paramDb.getReferenceTypeId());
                                    sendRequest(paramCommitRequest, ParamCommitResponse.class);
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (net_connet) {
                                            if (customProgressDialog != null) {
                                                customProgressDialog.dismiss();
                                            }
                                            CommonUtils.showToast(NewCheckActivity.this, "网络异常");
                                        }
                                    }
                                }, 10000);
                            } else {
                                String actualValue = actualValues.toString().substring(1, actualValues.toString().length() - 1);
                                final CheckCommitDb checkCommitDb = new CheckCommitDb(taskId, userId, taskCode, actualValue, describe, currentTime, 1, paramDb.getReferenceTypeId(), resultId);
                                ExecutorService thread = SingleThreadPool.getThread();
                                thread.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            db.saveOrUpdate(checkCommitDb);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Utils.showTost(NewCheckActivity.this, getString(R.string.no_net) + "\n已将数据存入本地，之后可进行提交");
                                                    finish();
                                                    sendBroadcast();
                                                }
                                            });
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        } else {
            CommonUtils.showToast(NewCheckActivity.this, "任务错误，无法提交");
        }

    }

    private void cancelCheck() {
        if ((checkOrder.getTaskStatus()).equals(should) || (checkOrder.getTaskStatus()).equals(over)) {
            new AlertDialog.Builder(NewCheckActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle("确定取消该次巡检")//提示框标题
                    .setPositiveButton("确定",//提示框的两个按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //事件
                                    int taskId = checkOrder.getId();
                                    boolean checkNetWork = Utils.checkNetWork(NewCheckActivity.this);
                                    if (checkNetWork) {
                                        CancelCheckRequest cancelCheckRequest = new CancelCheckRequest(taskId + "");
                                        sendRequest(cancelCheckRequest, CancelCheckResponse.class);
                                    } else {
                                        int hasCommit = 1;
                                        final CancelCheckDb cancelCheckDb = new CancelCheckDb(taskId, hasCommit);
                                        CheckOrderDb checkOrderDb = null;
                                        try {
                                            checkOrderDb = db.findById(CheckOrderDb.class, taskId);
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                        if (checkOrderDb != null) {
                                            checkOrderDb.setTaskStatus(cancel);
                                        }
                                        ExecutorService thread = SingleThreadPool.getThread();
                                        final CheckOrderDb finalCheckOrderDb = checkOrderDb;
                                        thread.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    db.saveOrUpdate(cancelCheckDb);
                                                    db.saveOrUpdate(finalCheckOrderDb);
                                                    handler.sendEmptyMessage(Constants.WARM);
                                                } catch (DbException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).setNegativeButton("取消", null).create().show();
        }
    }

    private void go2Copy() {
        Intent intent = new Intent(NewCheckActivity.this, CheckCopyActivity.class);
        intent.putExtra(Constants.DATA, (Serializable) checkOrder);
        intent.putExtra(Constants.CHECK_TYPE, checkType);
        startActivity(intent);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DESCRIBE:
                if (data != null) {
                    describe = data.getStringExtra(Constants.DESCRIBE);
                    tv_describe.setText(describe.length() > 3 ? describe.substring(0, 3) + "..." : describe);
                    try {
                        CheckCommitDb nowCommitDb = db.findById(CheckCommitDb.class, checkOrder.getId());

                        if (nowCommitDb == null) {
                            nowCommitDb = new CheckCommitDb();
                        }
                        nowCommitDb.setTaskId(taskId);
                        nowCommitDb.setFeedback(describe);
                        ExecutorService thread = SingleThreadPool.getThread();
                        final CheckCommitDb finalNowCommitDb = nowCommitDb;
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(finalNowCommitDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
