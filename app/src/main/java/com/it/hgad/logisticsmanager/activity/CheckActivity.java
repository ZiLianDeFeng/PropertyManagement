package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.ParamAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.Param;
import com.it.hgad.logisticsmanager.bean.ParamDbEntity;
import com.it.hgad.logisticsmanager.bean.request.ParamCommitRequest;
import com.it.hgad.logisticsmanager.bean.request.ParamListRequest;
import com.it.hgad.logisticsmanager.bean.response.ParamCommitResponse;
import com.it.hgad.logisticsmanager.bean.response.ParamListResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.dao.ParamDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CheckActivity extends CommonActivity {

    private ListView lv;
    private List<Param> listdata = new ArrayList<>();
    private ParamAdapter paramAdapter;
    private TextView btn_confirm;
    private CheckOrder checkOrder;
    public static final int TYPE = 0;
    private int userId;
    private TextView tv_taskcode;
    private TextView taskName;
    private TextView deviceName;
    //    private TextView deviceCode;
    private TextView planTime;
    //    private TextView shouldTime;
    private TextView responser;
    private TextView inspectorName;
    private ImageView iv_back;
    private DbUtils db;
    private TextView tv_total;
    private LinearLayout ll_param;
    private TextView et_feedback;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.STOP:
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                    }
                    paramAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            ll_param.setFocusable(true);
            ll_param.setFocusableInTouchMode(true);
        }
    };
    private int taskId;
    private String taskCode;
    private boolean net_connet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        initHeader(getString(R.string.device_copy));
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
        userId = SPUtils.getInt(CheckActivity.this, SPConstants.USER_ID);
        Intent intent = getIntent();
        checkOrder = (CheckOrder) intent.getSerializableExtra("data");
        taskCode = checkOrder.getTaskCode();
        tv_taskcode.setText(taskCode);
        taskName.setText(checkOrder.getTaskName());
        deviceName.setText(checkOrder.getDeviceName());
        taskId = checkOrder.getTaskId();
//        deviceCode.setText(checkOrder.getDeviceCode());
        String pTime = checkOrder.getPlanTime();
        if (pTime != null) {
            pTime = pTime.replace("T", " ");
        }
        planTime.setText(pTime);
//        String sTime = checkOrder.getShouldTime();
//        if (sTime != null) {
//            sTime = sTime.replace("T", " ");
//        }
//        shouldTime.setText(sTime);
        responser.setText(checkOrder.getResponser());
        String inspectorNames = checkOrder.getInspectorName();
        inspectorNames = inspectorNames.replace(',', ' ').trim();
        inspectorName.setText(inspectorNames);
        boolean checkNetWork = Utils.checkNetWork(CheckActivity.this);
        if (checkNetWork) {
            ParamListRequest paramListRequest = new ParamListRequest(checkOrder.getId() + "");
            sendRequest(paramListRequest, ParamListResponse.class);
        } else {
            try {
                ParamDb paramDb = db.findById(ParamDb.class, checkOrder.getId());
                if (paramDb != null) {
                    String params = paramDb.getParams();
                    params = "{\"data\" : " + params + "}";
                    ParamDbEntity paramDbEntity = new Gson().fromJson(params, ParamDbEntity.class);
                    for (ParamDbEntity.DataEntity entity : paramDbEntity.getData()
                            ) {
                        String referenceName = entity.getReferenceName();
                        String referenceStartValue = entity.getReferenceStartValue();
                        String referenceEndValue = entity.getReferenceEndValue();
                        String referenceUnit = entity.getReferenceUnit();
                        String remark = entity.getRemark();
                        Param param = new Param(referenceName, referenceStartValue, referenceEndValue, "", referenceUnit, remark);
                        listdata.add(param);
                    }
                    tv_total.setText("(共" + listdata.size() + "项)");
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
            }
            paramAdapter.notifyDataSetChanged();
        }
//        hideInputMethod(et_feedback);
    }

    private boolean hasConnet = true;

    private void initView() {
        customProgressDialog = new CustomProgressDialog(this, "数据加载中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasConnet) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        Utils.showTost(CheckActivity.this, "网络不给力！");
                    }
                }
            }
        }, 10000);
        lv = (ListView) findViewById(R.id.lv_parameter);
        paramAdapter = new ParamAdapter(CheckActivity.this, listdata, TYPE);
        lv.setAdapter(paramAdapter);
        paramAdapter.notifyDataSetChanged();
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_confirm.setVisibility(View.VISIBLE);
        btn_confirm.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        et_feedback = (TextView) findViewById(R.id.et_feedback);
        et_feedback.setOnClickListener(this);
        tv_taskcode = (TextView) findViewById(R.id.tv_taskCode);
        taskName = (TextView) findViewById(R.id.tv_taskName);
        deviceName = (TextView) findViewById(R.id.tv_deviceName);
//        deviceCode = (TextView) findViewById(R.id.tv_deviceCode);
        planTime = (TextView) findViewById(R.id.tv_planTime);
//        shouldTime = (TextView) findViewById(R.id.tv_shouldTime);
        responser = (TextView) findViewById(R.id.tv_responser);
        inspectorName = (TextView) findViewById(R.id.tv_inspectorName);
        tv_total = (TextView) findViewById(R.id.tv_total_param);
        ll_param = (LinearLayout) findViewById(R.id.ll_param);
    }

    private void hideInputMethod(EditText edit) {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //强制隐藏Android输入法窗口
        inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof ParamListRequest) {
            ParamListResponse paramListResponse = (ParamListResponse) response;
            if (paramListResponse != null) {
                for (ParamListResponse.DataEntity entity : paramListResponse.getData()
                        ) {
                    ParamListResponse.DataEntity.TaskResultEntity taskResult1 = entity.getTaskResult();
                    int id = taskResult1.getId();
                    String referenceName = entity.getReferenceName();
                    String referenceStartValue = entity.getReferenceStartValue();
                    String referenceEndValue = entity.getReferenceEndValue();
                    String actualValue = entity.getActualValue();
                    String referenceUnit = entity.getReferenceUnit();
                    String remark = entity.getRemark();
                    Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark);
                    listdata.add(param);
                    ParamListResponse.DataEntity.TaskResultEntity taskResult = entity.getTaskResult();
                }
                hasConnet = false;
                tv_total.setText("(共" + listdata.size() + "项)");
                handler.sendEmptyMessageDelayed(Constants.STOP,300);
            }
        } else if (request instanceof ParamCommitRequest) {
            ParamCommitResponse paramCommitResponse = (ParamCommitResponse) response;
            if (paramCommitResponse != null) {
                if ("0".equals(paramCommitResponse.getResult())) {
                    CommonUtils.showToast(CheckActivity.this, "操作成功");
                    finish();
                    sendBroadcast();
                } else if ("1".equals(paramCommitResponse.getResult())) {
                    CommonUtils.showToast(CheckActivity.this, "操作失败");
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
            case R.id.btn_confirm:
                commit();
                break;
            case R.id.iv_back:
                backWarm();
                break;
            case R.id.et_feedback:
                feedBack();
                break;
        }
    }

    private void feedBack() {
        LayoutInflater factory = LayoutInflater.from(CheckActivity.this);//提示框
        final View dialogView = factory.inflate(R.layout.editbox_layout, null);//这里必须是final的
        final EditText edit = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
        edit.setText(et_feedback.getText().toString().trim());
        new AlertDialog.Builder(CheckActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle("结果说明")//提示框标题
                .setView(dialogView)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String feedback = edit.getText().toString().trim();
                                if (!TextUtils.isEmpty(feedback)) {
                                    et_feedback.setText(feedback);
                                    CommonUtils.closeKeybord(edit, CheckActivity.this);
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.closeKeybord(edit, CheckActivity.this);
            }
        }).create().show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showInputMethod(CheckActivity.this);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

//    private void backWarm() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        builder.setTitle("提示").setMessage("是否确认返回");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//    }

    private List<String> actualValues = new ArrayList<>();
    private List<String> remarks = new ArrayList<>();
    private CustomProgressDialog customProgressDialog;

    private void commit() {
        boolean cannotCommit = false;
        final String feedback = et_feedback.getText().toString().trim();
        for (Param param : listdata) {
            String actualValue = param.getActualValue();
            if (actualValue == null || "".equals(actualValue)) {
                cannotCommit = true;
                break;
            }
        }
        if (cannotCommit) {
            CommonUtils.showToast(CheckActivity.this, "还有参数未输入参数值哦！");
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("提示");
            builder.setMessage("是否确定提交");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String currentTime = CommonUtils.getCurrentTime();
                    for (int i = 0; i < listdata.size(); i++) {
                        Param param = listdata.get(i);
                        String actualValue = param.getActualValue();
                        String remark = param.getRemark();
                        actualValues.add(actualValue);
                        remarks.add(remark);
                    }
                    boolean checkNetWork = Utils.checkNetWork(CheckActivity.this);
                    if (checkNetWork) {
                        net_connet = true;
                        customProgressDialog = new CustomProgressDialog(CheckActivity.this, "数据提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
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
                        ParamCommitRequest paramCommitRequest = new ParamCommitRequest(userId + "", taskId + "", actualValues, feedback, currentTime);
                        sendRequest(paramCommitRequest, ParamCommitResponse.class);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (net_connet) {
                                    if (customProgressDialog != null) {
                                        customProgressDialog.dismiss();
                                    }
                                    CommonUtils.showToast(CheckActivity.this, "网络异常");
                                }
                            }
                        }, 10000);
                    } else {
                        String actualValue = actualValues.toString().substring(1, actualValues.toString().length() - 1);
                        final CheckCommitDb checkCommitDb = new CheckCommitDb(taskId, userId, taskCode, actualValue, feedback, currentTime, 1,"",1);
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(checkCommitDb);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.showTost(CheckActivity.this, getString(R.string.no_net) + "\n已将数据存入本地，之后可进行提交");
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
}
