package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.ParamAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.NewParamDbEntity;
import com.it.hgad.logisticsmanager.bean.Param;
import com.it.hgad.logisticsmanager.bean.ParamDbEntity;
import com.it.hgad.logisticsmanager.bean.request.TemParamRequest;
import com.it.hgad.logisticsmanager.bean.response.TempParamResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.dao.ParamDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class CheckCopyActivity extends CommonActivity {

    public static final int TYPE = 0;
    private static final int PARAM_RESULT = 110;
    private ListView lv;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                    }
                    paramAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private List<Param> listData = new ArrayList<>();
    private ParamAdapter paramAdapter;
    //    private TextView tv_total;
    private CheckOrder checkOrder;
    private DbUtils db;
    private int userId;
    private TextView tv_type_name;
    private List<String> paramTypeIds;
    private List<String> paramTypeNames;
    private NewParamDbEntity paramDbEntity;
    private String checkType;
    private ParamDbEntity dbEntity;
    private LinearLayout main;
    private EditText et_abnomal_describ;
    private TextView tv_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_copy);
        initHeader("巡检抄表");
        initView();
        initData();
    }

    private void initData() {
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(ParamDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        boolean checkNetWork = Utils.checkNetWork(this);
        Intent intent = getIntent();
        checkOrder = (CheckOrder) intent.getSerializableExtra(Constants.DATA);
        checkType = intent.getStringExtra(Constants.CHECK_TYPE);
//        if (checkNetWork) {
//            ParamListRequest paramListRequest = new ParamListRequest(checkOrder.getId() + "");
//            sendRequest(paramListRequest, ParamListResponse.class);
//        } else {
        try {
            CheckCommitDb checkCommitDb = db.findById(CheckCommitDb.class, checkOrder.getId());
            String feedback = checkCommitDb.getFeedback();
            et_abnomal_describ.setText(feedback);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (CheckConstants.TEMP.equals(checkType)) {
            try {
                ParamDb paramDb = db.findById(ParamDb.class, checkOrder.getId());
                if (paramDb != null) {
                    String params = paramDb.getParams();
//                params = "{\"deviceTaskResult\" : " + params + "}";
                    dbEntity = new Gson().fromJson(params, ParamDbEntity.class);
                    for (ParamDbEntity.DataEntity entity : dbEntity.getData()
                            ) {
                        String actualValue = entity.getActualValue();
                        String referenceName = entity.getReferenceName();
                        String referenceStartValue = entity.getReferenceStartValue();
                        String referenceEndValue = entity.getReferenceEndValue();
                        String referenceUnit = entity.getReferenceUnit();
                        String remark = entity.getRemark();
                        Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark);
                        listData.add(param);
                    }
                } else {
                    TemParamRequest paramListRequest = new TemParamRequest(checkOrder.getId() + "");
                    sendRequest(paramListRequest, TempParamResponse.class);
                }
                tv_type_name.setText("巡检情况");
                tv_type_name.setOnClickListener(null);
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ParamDb paramDb = db.findById(ParamDb.class, checkOrder.getId());
                if (paramDb != null) {
                    String params = paramDb.getParams();
//                params = "{\"deviceTaskResult\" : " + params + "}";
                    paramDbEntity = new Gson().fromJson(params, NewParamDbEntity.class);
                    String referenceParamTypeId = paramDbEntity.getReferenceParamTypeId();
                    String referenceParamTypeName = paramDbEntity.getReferenceParamTypeName();
                    if (referenceParamTypeId != null) {
                        paramTypeIds = CommonUtils.cast2List(referenceParamTypeId);
                        paramTypeNames = CommonUtils.cast2List(referenceParamTypeName);
                        for (NewParamDbEntity.DeviceParamSetEntity entity : paramDbEntity.getDeviceParamSet()
                                ) {
                            String fId = paramTypeIds.get(0);
                            String actualValue = entity.getActualValue();
                            String referenceName = entity.getReferenceName();
                            String referenceStartValue = entity.getReferenceStartValue();
                            String referenceEndValue = entity.getReferenceEndValue();
                            String referenceUnit = entity.getReferenceUnit();
                            String remark = entity.getRemark();
                            String referenceTypeId = entity.getReferenceTypeId();
                            Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark, referenceTypeId);
                            if (fId.equals(referenceTypeId)) {
                                listData.add(param);
                            }
                        }
                        tv_type_name.setText(paramTypeNames.get(0));
                        tv_count.setText("（" + listData.size() + "项）");
                    }
//                tv_total.setText("(共" + listData.size() + "项)");
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        paramAdapter.notifyDataSetChanged();
//        }
    }

    private boolean hasConnet = true;

    private void initView() {
        lv = (ListView) findViewById(R.id.lv_parameter);
        main = (LinearLayout) findViewById(R.id.main);
        paramAdapter = new ParamAdapter(CheckCopyActivity.this, listData, TYPE);
        lv.setAdapter(paramAdapter);
//        tv_total = (TextView) findViewById(R.id.tv_total_param);
        TextView tv_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_confirm.setVisibility(View.VISIBLE);
        tv_confirm.setOnClickListener(this);
        tv_type_name = (TextView) findViewById(R.id.tv_type_name);
        et_abnomal_describ = (EditText) findViewById(R.id.et_abnomal_describ);
        tv_count = (TextView) findViewById(R.id.tv_count);
        LinearLayout ll_type = (LinearLayout) findViewById(R.id.ll_type);
        ll_type.setOnClickListener(this);
    }

    @Override
    public void back(View view) {
        backWarm();
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof TemParamRequest) {
            TempParamResponse paramListResponse = (TempParamResponse) response;
            if (paramListResponse.getData() != null) {
                for (TempParamResponse.DataEntity entity : paramListResponse.getData()
                        ) {
                    TempParamResponse.DataEntity.TempResultEntity tempResult = entity.getTempResult();
                    int id = tempResult.getId();
                    String referenceName = entity.getReferenceName();
                    String referenceStartValue = entity.getReferenceStartValue();
                    String referenceEndValue = entity.getReferenceEndValue();
                    String actualValue = entity.getActualValue();
                    String referenceUnit = entity.getReferenceUnit();
                    String remark = entity.getRemark();
                    Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark);
                    listData.add(param);
                }
                hasConnet = false;
//                tv_total.setText("(共" + listData.size() + "项)");
                handler.sendEmptyMessageDelayed(Constants.STOP, 300);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                try {
                    confirm();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_type:
                chooseType();
                break;
        }
    }

    private void chooseType() {
        if (paramTypeNames != null) {
            final String[] types = paramTypeNames.toArray(new String[paramTypeNames.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
            builder.setTitle("参数类型");
            //    指定下拉列表的显示数据
            //    设置一个下拉的列表选择项
            builder.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listData.clear();
                    for (NewParamDbEntity.DeviceParamSetEntity entity : paramDbEntity.getDeviceParamSet()
                            ) {
                        String cId = paramTypeIds.get(which);
                        String actualValue = entity.getActualValue();
                        String referenceName = entity.getReferenceName();
                        String referenceStartValue = entity.getReferenceStartValue();
                        String referenceEndValue = entity.getReferenceEndValue();
                        String referenceUnit = entity.getReferenceUnit();
                        String remark = entity.getRemark();
                        String referenceTypeId = entity.getReferenceTypeId();
                        Param param = new Param(referenceName, referenceStartValue, referenceEndValue, actualValue, referenceUnit, remark, referenceTypeId);
                        if (cId.equals(referenceTypeId)) {
                            listData.add(param);
                        }
                    }
                    tv_type_name.setText(types[which]);
                    tv_count.setText("（" + listData.size() + "项）");
                    paramAdapter.notifyDataSetChanged();
                }
            });
            builder.show();
        }
    }

    private void confirm() throws DbException {
        boolean haveNullParam = false;
        if (CheckConstants.TEMP.equals(checkType)) {
            CheckCommitDb checkCommitDb = db.findById(CheckCommitDb.class, checkOrder.getId());
            String etText = et_abnomal_describ.getText().toString().trim();
            checkCommitDb.setFeedback(etText);
            db.saveOrUpdate(checkCommitDb);
            ParamDb tempDb = getTempDb(listData, checkOrder.getId());
            db.saveOrUpdate(tempDb);
            finish();
        } else {
            if (paramDbEntity != null) {
                CheckCommitDb checkCommitDb = db.findById(CheckCommitDb.class, checkOrder.getId());
//                String feedback = checkCommitDb.getFeedback();
                String etText = et_abnomal_describ.getText().toString().trim();
//                feedback = feedback == null ? etText : feedback + "，" + etText;
                checkCommitDb.setFeedback(etText);
                db.saveOrUpdate(checkCommitDb);
                List<NewParamDbEntity.DeviceParamSetEntity> deviceParamSet = paramDbEntity.getDeviceParamSet();
                for (int i = 0; i < deviceParamSet.size(); i++) {
                    for (int j = 0; j < listData.size(); j++) {
                        NewParamDbEntity.DeviceParamSetEntity deviceParamSetEntity = deviceParamSet.get(i);
                        Param param = listData.get(j);
                        if (param.getActualValue() == null) {
                            haveNullParam = true;
                        }
                        if (param.getReferenceTypeId().equals(deviceParamSetEntity.getReferenceTypeId()) && param.getReferenceName().equals(deviceParamSetEntity.getReferenceName()))
                            deviceParamSetEntity.setActualValue(param.getActualValue());
                    }
                }
                if (haveNullParam) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setTitle("提示").setMessage("还有参数未填，是否确认保存");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ParamDb paramDb = getParamDb(paramDbEntity, checkOrder.getId());
                            try {
                                db.saveOrUpdate(paramDb);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            finish();
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
                } else {
                    ParamDb paramDb = getParamDb(paramDbEntity, checkOrder.getId());
                    db.saveOrUpdate(paramDb);
                    finish();
                }
            }
        }
    }

    private ParamDb getParamDb(NewParamDbEntity deviceParamSet, int id) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        String paramsJson = gson.toJson(deviceParamSet);
        ParamDb paramDb = new ParamDb(id, paramsJson, listData.get(0).getReferenceTypeId());
        return paramDb;
    }

    private ParamDb getTempDb(List<Param> listData, int id) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        String paramsJson = gson.toJson(listData);
        paramsJson = "{\"data\" : " + paramsJson + "}";
        ParamDb paramDb = new ParamDb(id, paramsJson, "");
        return paramDb;
    }
}
