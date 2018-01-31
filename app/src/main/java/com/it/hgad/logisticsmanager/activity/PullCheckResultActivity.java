package com.it.hgad.logisticsmanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.CheckAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.request.CheckOrderListRequest;
import com.it.hgad.logisticsmanager.bean.response.NewCheckOrderResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.ParamDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/2/23.
 */
public class PullCheckResultActivity extends CommonActivity {
    private XListView lv;
    private List<CheckOrder> listData = new ArrayList<>();
    private CheckAdapter adapter;
    private DbUtils db;
    private int userId;
    private int currentPage = 1;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.PARAM_COMMIT_OK)) {
                callRefresh();
            }
        }
    };
    private String should;
    private String over;

    private void callRefresh() {
        currentPage = 1;
        listData.clear();
        boolean isNetWork = Utils.checkNetWork(PullCheckResultActivity.this);
        if (isNetWork) {
            CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, over, currentPage + "");
            sendRequest(checkOrderListRequest, NewCheckOrderResponse.class);
            currentPage--;
        } else {
//            getOutlineData();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pull_result);
        initHeader("新的任务");
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        should = SPUtils.getString(PullCheckResultActivity.this, CheckConstants.SHOULD);
        over = SPUtils.getString(PullCheckResultActivity.this, CheckConstants.OVER);
        if (db == null) {
            db = LocalApp.getDb();
        }
        listData.clear();
        CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, over, currentPage + "");
        sendRequest(checkOrderListRequest, NewCheckOrderResponse.class);
        currentPage--;
    }

    private void initView() {
        lv = (XListView) findViewById(R.id.lv_result);
        adapter = new CheckAdapter(listData, PullCheckResultActivity.this, should);
        lv.setAdapter(adapter);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(mIXListViewListener);
    }

    private int mCurrentIndex = 0;
    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            callRefresh();
            lv.setRefreshTime(CommonUtils.getCurrentTime());
            lv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            mCurrentIndex = adapter.getCount();
            callLoadMore();
            lv.stopLoadMore();
//            afterLoadMore = true;
        }
    };

    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(PullCheckResultActivity.this);
        if (isNetWork) {
            currentPage++;
            CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, over, currentPage + "");
            sendRequest(checkOrderListRequest, NewCheckOrderResponse.class);
            currentPage--;
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof CheckOrderListRequest) {
            NewCheckOrderResponse checkOrderListResponse = (NewCheckOrderResponse) response;
            if (checkOrderListResponse.getData() != null) {
                final List<NewCheckOrderResponse.DataEntity.ListdataEntity> data = checkOrderListResponse.getData().getListdata();
                currentPage++;
                if (currentPage == 1) {
                    if (listData != null) {
                        listData.clear();
                    }
                }
                if (currentPage == checkOrderListResponse.getData().getCurrentpage()) {
                    getOrderData(data);
                    ExecutorService thread = SingleThreadPool.getThread();
                    thread.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToDb(data);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        }
    }

    private void getOrderData(final List<NewCheckOrderResponse.DataEntity.ListdataEntity> data) {
        for (int i = data.size(); i > 0; i--) {
            NewCheckOrderResponse.DataEntity.ListdataEntity task = data.get(i - 1);
            NewCheckOrderResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
            int taskId = task.getDeviceTaskResult().getId();
            CheckOrder order = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName(), entity.getInspectorName(), entity.getArrageId(),
                    entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
            listData.add(order);
        }
        adapter.notifyDataSetChanged();
    }

    private void saveToDb(final List<NewCheckOrderResponse.DataEntity.ListdataEntity> data) throws DbException {
        for (NewCheckOrderResponse.DataEntity.ListdataEntity task : data) {
            NewCheckOrderResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
            int taskId = task.getDeviceTask().getId();
            CheckOrder checkOrder = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName()
                    , entity.getInspectorName(), entity.getArrageId(), entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
            final CheckOrderDb checkOrderDb = getCheckOrderDb(checkOrder);
            NewCheckOrderResponse.DataEntity.ListdataEntity.DeviceTaskResultEntity deviceTaskResult = task.getDeviceTaskResult();
            ParamDb paramDb = getParamDb(deviceTaskResult, taskId);
            db.saveOrUpdate(checkOrderDb);
            db.saveOrUpdate(paramDb);
        }
    }

    private CheckOrderDb getCheckOrderDb(CheckOrder checkOrder) {
        CheckOrderDb checkOrderDb = new CheckOrderDb(checkOrder.getId(), userId, checkOrder.getDeviceType(), checkOrder.getFinishTime(), checkOrder.getDeviceCode(), checkOrder.getInspector(), checkOrder.getDeviceName(), checkOrder.getInspectorName(), checkOrder.getArrageId(), checkOrder.getTaskCode(), checkOrder.getPlanTime(), checkOrder.getResponser(), checkOrder.getShouldTime(), checkOrder.getTaskName(), checkOrder.getTaskStatus(), checkOrder.getTaskId());
        return checkOrderDb;
    }

    private ParamDb getParamDb(NewCheckOrderResponse.DataEntity.ListdataEntity.DeviceTaskResultEntity deviceTaskResult, int id) {
        Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        String paramsJson = gson.toJson(deviceTaskResult);
        String actualParamTypeId = deviceTaskResult.getActualParamTypeId();
        if (actualParamTypeId == null) {
            actualParamTypeId = "";
        }
        ParamDb paramDb = new ParamDb(id, paramsJson, actualParamTypeId);
        return paramDb;
    }

    @Override
    public void onClick(View v) {

    }

    private void getOutlineData() {
        listData.clear();
        try {
            List<CheckOrderDb> all = db.findAll(CheckOrderDb.class);
            if (all != null && all.size() != 0) {
                for (int i = all.size(); i > 0; i--) {
                    CheckOrderDb checkOrderDb = all.get(i - 1);
                    if (checkOrderDb != null) {
                        if (userId == checkOrderDb.getUserId()) {
                            addToData(checkOrderDb);
                        }
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void addToData(CheckOrderDb checkOrderDb) {
        if ("0".equals(checkOrderDb.getTaskStatus())) {
            CheckOrder checkOrder = new CheckOrder();
            checkOrder.setData(checkOrderDb);
            listData.add(checkOrder);
        }
    }
}
