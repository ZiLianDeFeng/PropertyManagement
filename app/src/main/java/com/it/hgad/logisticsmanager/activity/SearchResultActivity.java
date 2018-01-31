package com.it.hgad.logisticsmanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.CheckAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.request.NewCheckRequest;
import com.it.hgad.logisticsmanager.bean.response.NewCheckOrderResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CancelCheckDb;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.ParamDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/1/12.
 */
public class SearchResultActivity extends CommonActivity {
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;
    private XListView lv;
    private List<CheckOrder> listData = new ArrayList<>();
    private CheckAdapter adapter;
    private LinearLayout ll_sort;
    private LinearLayout ll_condition;
    private LinearLayout ll_date_range;
    private Animation operatingAnim;
    private ImageView infoOperating;
    private int mCurrentIndex = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    rl_info.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private LinearLayout ll_top;
    private TextView tv_data_range;
    private TextView tv_condition;
    private TextView tv_sort;
    private ImageView iv_triangle_range;
    private ImageView iv_triangle_condition;
    private ImageView iv_triangle_sort;
    private int userId;
    private int currentPage = 1;
    private DbUtils db;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.PARAM_COMMIT_OK)) {
                callRefresh();
            }
        }
    };
    private String deviceName;
    private String taskFlag;
    private String taskNo;
    //参数名
    private List<String> paramNames = new ArrayList<>();
    //参数值
    private List<String> paramValues = new ArrayList<>();
    //查询条件 like or =
    private List<String> paramConditons = new ArrayList<>();
    //数据类型 string Integer
    private List<String> paramTypes = new ArrayList<>();
    private String deviceType;
    private String planTime;
    private RelativeLayout rl_info;
    private String taskName;

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PARAM_COMMIT_OK);
        registerReceiver(receiver, intentFilter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_check);
        initHeader("巡检任务");
        intiView();
        intiData();
    }

    private void intiData() {
        registBroadcastReceiver(receiver);
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(CheckOrderDb.class);
                db.createTableIfNotExist(ParamDb.class);
                db.createTableIfNotExist(CancelCheckDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        boolean isNetWork = Utils.checkNetWork(this);
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        Intent intent = getIntent();
        taskName = intent.getStringExtra(Constants.TASK_NAME);
        deviceName = intent.getStringExtra(Constants.DEVICE_NAME);
        taskFlag = intent.getStringExtra(Constants.TASK_FLAG);
        taskNo = intent.getStringExtra(Constants.TASK_NO);
//        deviceType = intent.getStringExtra(Constants.DEVICE_TYPE);
//        planTime = intent.getStringExtra(Constants.PLAN_TIME);
        if (!isNetWork) {
//            getOutlineData();
//            lv.setPullRefreshEnable(false);
//            lv.setPullLoadEnable(false);
        } else {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 10000);
            }
            paramNames.add("deviceTask.inspector");
            paramValues.add(userId + "");
            paramConditons.add("like");
            paramTypes.add("FIND_IN_SET");
            if (!taskFlag.equals("")) {
                paramNames.add("deviceTask.taskStatus");
                paramValues.add(taskFlag);
                paramConditons.add("like");
                paramTypes.add("OR");
            }
            if (!taskName.equals("")) {
                paramNames.add("deviceTask.taskName");
                paramValues.add(taskName);
                paramConditons.add("like");
                paramTypes.add("string");
            }
            if (!deviceName.equals("")) {
                paramNames.add("deviceTask.deviceName");
                paramValues.add(deviceName);
                paramConditons.add("like");
                paramTypes.add("string");
            }
            if (!taskNo.equals("")) {
                paramNames.add("deviceTask.taskCode");
                paramValues.add(taskNo);
                paramConditons.add("like");
                paramTypes.add("string");
            }
//            if (!deviceType.equals("")) {
//                paramNames.add("deviceTask.deviceType");
//                paramValues.add(deviceType);
//                paramConditons.add("like");
//                paramTypes.add("string");
//            }
//            if (!planTime.equals("")) {
//                paramNames.add("deviceTask.planTime");
//                paramValues.add(planTime);
//                paramConditons.add("like");
//                paramTypes.add("string");
//            }
//            CheckSearchRequest checkSearchRequest = new CheckSearchRequest(taskFlag, userId + "", deviceName, currentPage);
//            sendRequest(checkSearchRequest, CheckOrderListResponse.class);
            NewCheckRequest newCheckRequest = new NewCheckRequest(paramNames, paramValues, paramConditons, paramTypes, null, currentPage);
            sendRequest(newCheckRequest, NewCheckOrderResponse.class);
            currentPage--;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
                            listData.add(new CheckOrder(checkOrderDb.getDeviceType(), checkOrderDb.getFinishTime(), checkOrderDb.getDeviceCode(), checkOrderDb.getInspector(), checkOrderDb.getDeviceName()
                                    , checkOrderDb.getInspectorName(), checkOrderDb.getArrageId(), checkOrderDb.getTaskCode(), checkOrderDb.getPlanTime(), checkOrderDb.getResponser(), checkOrderDb.getShouldTime(), checkOrderDb.getTaskName(), checkOrderDb.getTaskId(), checkOrderDb.getTaskStatus(), checkOrderDb.getTaskResultId()));
                        }
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (listData != null && listData.size() != 0) {
            Collections.sort(listData, new Comparator<CheckOrder>() {
                @Override
                public int compare(CheckOrder lhs, CheckOrder rhs) {
                    return rhs.getPlanTime().compareTo(lhs.getPlanTime());
                }
            });
            Collections.sort(listData, new Comparator<CheckOrder>() {
                @Override
                public int compare(CheckOrder lhs, CheckOrder rhs) {
                    return lhs.getTaskStatus().compareTo(rhs.getTaskStatus());
                }
            });
            lv.setSelection(0);
        }
        adapter.notifyDataSetChanged();
    }

    private void intiView() {
        should = SPUtils.getString(this, CheckConstants.SHOULD);
        done = SPUtils.getString(this, CheckConstants.DONE);
        over = SPUtils.getString(this, CheckConstants.OVER);
        overFinish = SPUtils.getString(this, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(this, CheckConstants.CANCEL);
        lv = (XListView) findViewById(R.id.lv_check);
        lv.setVisibility(View.VISIBLE);
        PinnedSectionRefreshListView goLv = (PinnedSectionRefreshListView) findViewById(R.id.lv_check_new);
        goLv.setVisibility(View.GONE);
        adapter = new CheckAdapter(listData, this, "");
        lv.setAdapter(adapter);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(mIXListViewListener);
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setEmptyView(findViewById(R.id.tv_empty));
        ll_date_range = (LinearLayout) findViewById(R.id.ll_date_range);
        ll_date_range.setOnClickListener(this);
        ll_condition = (LinearLayout) findViewById(R.id.ll_condition);
        ll_condition.setOnClickListener(this);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        ll_sort.setOnClickListener(this);
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        ll_top.setVisibility(View.GONE);
        initAnimation();
        tv_data_range = (TextView) findViewById(R.id.tv_data_range);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        iv_triangle_range = (ImageView) findViewById(R.id.iv_triangle_range);
        iv_triangle_condition = (ImageView) findViewById(R.id.iv_triangle_condition);
        iv_triangle_sort = (ImageView) findViewById(R.id.iv_triangle_sort);
        LinearLayout ll_search = (LinearLayout) findViewById(R.id.ll_search);
//        ll_search.setVisibility(View.VISIBLE);
        ll_search.setOnClickListener(this);

    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(SearchResultActivity.this, CheckDetailActivity.class);
            CheckOrder order = listData.get(position - 1);
            intent.putExtra(Constants.DATA, (Serializable) order);
            startActivity(intent);
        }
    };

    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
//            callRefresh();
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

    private void callRefresh() {
        mCurrentIndex = 0;
        boolean isNetWork = Utils.checkNetWork(this);
        if (listData != null) {
            listData.clear();
        }
        if (isNetWork) {
            currentPage = 1;
            if (listData.size() != 0) {
                lv.setSelection(mCurrentIndex);
            }
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 10000);
            }
            NewCheckRequest newCheckRequest = new NewCheckRequest(paramNames, paramValues, paramConditons, paramTypes, null, currentPage);
            sendRequest(newCheckRequest, NewCheckOrderResponse.class);
            currentPage--;
        } else {
//            getOutlineData();
        }
    }

    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(this);
        if (isNetWork) {
            currentPage++;
//            CheckSearchRequest checkSearchRequest = new CheckSearchRequest(taskFlag, userId + "", deviceName, currentPage);
//            sendRequest(checkSearchRequest, CheckOrderListResponse.class);
            NewCheckRequest newCheckRequest = new NewCheckRequest(paramNames, paramValues, paramConditons, paramTypes, null, currentPage);
            sendRequest(newCheckRequest, NewCheckOrderResponse.class);
            currentPage--;
        } else {
//            getOutlineData();
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof NewCheckRequest) {
            NewCheckOrderResponse checkOrderListResponse = (NewCheckOrderResponse) response;
            if (checkOrderListResponse.getData() != null) {
                lv.setPullLoadEnable(true);
                lv.setPullRefreshEnable(false);
                final List<NewCheckOrderResponse.DataEntity.ListdataEntity> data = checkOrderListResponse.getData().getListdata();
                currentPage++;
                if (currentPage == checkOrderListResponse.getData().getCurrentpage()) {
                    if (data != null && data.size() != 0) {
                        for (NewCheckOrderResponse.DataEntity.ListdataEntity task : data) {
                            if (task != null) {
                                NewCheckOrderResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
                                int taskId = task.getDeviceTaskResult().getId();
                                CheckOrder order = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName(), entity.getInspectorName(), entity.getArrageId(),
                                        entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
                                listData.add(order);
                            }
                        }
                    }
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
            } else {
                lv.setPullLoadEnable(false);
                lv.setPullRefreshEnable(false);
            }
            handler.sendEmptyMessageDelayed(Constants.STOP, 500);
        }
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
        switch (v.getId()) {
        }
    }
}
