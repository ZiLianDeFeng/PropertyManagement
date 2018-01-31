package com.it.hgad.logisticsmanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.ParamAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.Param;
import com.it.hgad.logisticsmanager.bean.request.ParamListRequest;
import com.it.hgad.logisticsmanager.bean.request.TemParamRequest;
import com.it.hgad.logisticsmanager.bean.response.ParamListResponse;
import com.it.hgad.logisticsmanager.bean.response.TempParamResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CheckDetailActivity extends CommonActivity {

    private ListView lv;
    private List<Param> listdata = new ArrayList<>();
    private CheckOrder checkOrder;
    private TextView taskCode;
    private TextView taskName;
    private TextView deviceName;
    //    private TextView deviceCode;
    private TextView planTime;
    //    private TextView shouldTime;
    private TextView responser;
    private TextView inspectorName;
    private TextView finishTime;
    private TextView result;
    private TextView feedback;
    private String resultStatus;
    private String description;
    private ParamAdapter paramAdapter;
    public static final int TYPE = 1;
    private CustomProgressDialog customProgressDialog;
    private boolean notConnect = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                    }
                    break;
            }
        }
    };
    private String checkType;
    private DbUtils db;
    private TextView tv_param_type;
    private LinearLayout ll_result_describ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        initHeader("任务详情");
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
        Intent intent = getIntent();
        checkOrder = (CheckOrder) intent.getSerializableExtra(Constants.DATA);
        checkType = intent.getStringExtra(Constants.CHECK_TYPE);
        taskCode.setText(checkOrder.getTaskCode());
        taskName.setText(checkOrder.getTaskName());
        deviceName.setText(checkOrder.getDeviceName());
//        deviceCode.setText(checkOrder.getDeviceCode());
        String pTime = checkOrder.getPlanTime();
        if (pTime != null) {
            pTime = pTime.replace("T", " ");
        }
        planTime.setText(pTime);
        String sTime = checkOrder.getShouldTime();
        if (sTime != null) {
            sTime = sTime.replace("T", " ");
        }
//        shouldTime.setText(sTime);
        responser.setText(checkOrder.getResponser());
        String inspectorNames = checkOrder.getInspectorName();
        inspectorNames = inspectorNames.replace(',', ' ').trim();
        this.inspectorName.setText(inspectorNames);
        String fTime = checkOrder.getFinishTime();
        if (fTime != null) {
            fTime = fTime.replace("T", " ");
        }
        this.finishTime.setText(fTime);
        boolean checkNetWork = Utils.checkNetWork(CheckDetailActivity.this);
        if (checkNetWork) {
            if (CheckConstants.TEMP.equals(checkType)) {
                TemParamRequest paramListRequest = new TemParamRequest(checkOrder.getId() + "");
                sendRequest(paramListRequest, TempParamResponse.class);
                tv_param_type.setText("参数详情");
            } else {
                ParamListRequest paramListRequest = new ParamListRequest(checkOrder.getId() + "");
                sendRequest(paramListRequest, ParamListResponse.class);
//                try {
//                    ParamDb paramDb = db.findById(ParamDb.class, checkOrder.getId());
//                    if (paramDb != null) {
//                        String params = paramDb.getParams();
//                        String typeId = paramDb.getReferenceTypeId();
////                params = "{\"deviceTaskResult\" : " + params + "}";
//                        NewParamDbEntity paramDbEntity = new Gson().fromJson(params, NewParamDbEntity.class);
//                        String referenceParamTypeId = paramDbEntity.getReferenceParamTypeId();
//                        String referenceParamTypeName = paramDbEntity.getReferenceParamTypeName();
//                        if (referenceParamTypeId != null) {
//                            List<String> paramTypeIds = CommonUtils.cast2List(referenceParamTypeId);
//                            List<String> paramTypeNames = CommonUtils.cast2List(referenceParamTypeName);
//                            if (typeId != null && !"".equals(typeId)) {
//                                for (int i = 0; i < paramTypeIds.size(); i++) {
//                                    String cId = paramTypeIds.get(i);
//                                    if (typeId.equals(cId)) {
//                                        tv_param_type.setText(paramTypeNames.get(i));
//                                        for (NewParamDbEntity.DeviceParamSetEntity entity : paramDbEntity.getDeviceParamSet()
//                                                ) {
//                                            String actualValue = entity.getActualValue();
//                                            String referenceName = entity.getReferenceName();
//                                            String referenceStartValue = entity.getReferenceStartValue();
//                                            String referenceEndValue = entity.getReferenceEndValue();
//                                            String referenceUnit = entity.getReferenceUnit();
//                                            String remark = entity.getRemark();
//                                            String referenceTypeId = entity.getReferenceTypeId();
//                                            Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark, referenceTypeId);
//                                            if (cId.equals(referenceTypeId)) {
//                                                listdata.add(param);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            paramAdapter.notifyDataSetChanged();
//                            if (customProgressDialog != null) {
//                                customProgressDialog.dismiss();
//                            }
//                        }
//                    }
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
            }
        } else {
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
            }
            CommonUtils.showToast(CheckDetailActivity.this, getString(R.string.no_net));
        }
    }


    private void initView() {
        customProgressDialog = new CustomProgressDialog(this, "数据加载中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (notConnect) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        Utils.showTost(CheckDetailActivity.this, "网络不给力！");
                    }
                }
            }
        }, 10000);
        lv = (ListView) findViewById(R.id.lv_parameter);
        paramAdapter = new ParamAdapter(CheckDetailActivity.this, listdata, TYPE);
        lv.setAdapter(paramAdapter);
        lv.setVisibility(View.GONE);
        paramAdapter.notifyDataSetChanged();
        taskCode = (TextView) findViewById(R.id.tv_taskCode);
        taskName = (TextView) findViewById(R.id.tv_taskName);
        deviceName = (TextView) findViewById(R.id.tv_deviceName);
//        deviceCode = (TextView) findViewById(R.id.tv_deviceCode);
        planTime = (TextView) findViewById(R.id.tv_planTime);
//        shouldTime = (TextView) findViewById(R.id.tv_shouldTime);
        responser = (TextView) findViewById(R.id.tv_responser);
        inspectorName = (TextView) findViewById(R.id.tv_inspectorName);
        finishTime = (TextView) findViewById(R.id.tv_finish_time);
        result = (TextView) findViewById(R.id.tv_result);
        feedback = (TextView) findViewById(R.id.tv_feedback);
        feedback.setVisibility(View.GONE);
        tv_param_type = (TextView) findViewById(R.id.tv_param_type);
        tv_param_type.setOnClickListener(this);
        ll_result_describ = (LinearLayout) findViewById(R.id.ll_result_describ);
        ll_result_describ.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof ParamListRequest) {
            ParamListResponse paramListResponse = (ParamListResponse) response;
            if (paramListResponse.getData() != null) {
                for (ParamListResponse.DataEntity entity : paramListResponse.getData()
                        ) {
                    String referenceName = entity.getReferenceName();
                    String referenceStartValue = entity.getReferenceStartValue();
                    String referenceEndValue = entity.getReferenceEndValue();
                    String actualValue = entity.getActualValue();
                    String referenceUnit = entity.getReferenceUnit();
                    String remark = entity.getRemark();
                    String actualParamTypeId = entity.getTaskResult().getActualParamTypeId();
                    if (actualParamTypeId == null) {
                        tv_param_type.setText("参数详情");
                    }
                    String referenceParamTypeId = entity.getTaskResult().getReferenceParamTypeId();
                    String referenceParamTypeName = entity.getTaskResult().getReferenceParamTypeName();
                    if (referenceParamTypeId != null) {
                        List<String> paramTypeIds = CommonUtils.cast2List(referenceParamTypeId);
                        List<String> paramTypeNames = CommonUtils.cast2List(referenceParamTypeName);
                        for (int i = 0; i < paramTypeIds.size(); i++) {
                            if (paramTypeIds.get(i).equals(actualParamTypeId)) {
                                tv_param_type.setText(paramTypeNames.get(i));
                            }
                        }
                    }
                    Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark, actualParamTypeId);
                    listdata.add(param);
                    ParamListResponse.DataEntity.TaskResultEntity taskResult = entity.getTaskResult();
                    resultStatus = taskResult.getResultStatus();
                    description = taskResult.getDescription();
                }
                paramAdapter.notifyDataSetChanged();
                if ("0".equals(resultStatus)) {
                    result.setText("正常");
                } else if ("1".equals(resultStatus)) {
                    result.setText("异常");
                } else if ("2".equals(resultStatus)) {
                    result.setText("异常已处理");
                }
                feedback.setText(description);
            }
            notConnect = false;
            handler.sendEmptyMessageDelayed(Constants.STOP, 500);
        } else if (request instanceof TemParamRequest) {
            TempParamResponse paramListResponse = (TempParamResponse) response;
            if (paramListResponse.getData() != null) {
                for (TempParamResponse.DataEntity entity : paramListResponse.getData()
                        ) {
                    String referenceName = entity.getReferenceName();
                    String referenceStartValue = entity.getReferenceStartValue();
                    String referenceEndValue = entity.getReferenceEndValue();
                    String actualValue = entity.getActualValue();
                    String referenceUnit = entity.getReferenceUnit();
                    String remark = entity.getRemark();
                    Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark);
                    listdata.add(param);
                    TempParamResponse.DataEntity.TempResultEntity tempResult = entity.getTempResult();
                    resultStatus = tempResult.getResultStatus();
                    description = tempResult.getDescription();
                }
                paramAdapter.notifyDataSetChanged();
                if ("0".equals(resultStatus)) {
                    result.setText("正常");
                } else if ("1".equals(resultStatus)) {
                    result.setText("异常");
                } else if ("2".equals(resultStatus)) {
                    result.setText("异常已处理");
                }
                feedback.setText(description);
            }
            notConnect = false;
            handler.sendEmptyMessageDelayed(Constants.STOP, 500);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_param_type:
                showOrCloseLv();
                break;
            case R.id.ll_result_describ:
                showOrCloseTv();
                break;
        }
    }


    private void showOrCloseTv() {
        if (feedback.isShown()) {
            feedback.setVisibility(View.GONE);
        } else {
            feedback.setVisibility(View.VISIBLE);
        }
    }

    private void showOrCloseLv() {
        if (lv.isShown()) {
            lv.setVisibility(View.GONE);
        } else {
            lv.setVisibility(View.VISIBLE);
        }
    }
}
