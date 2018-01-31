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
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.CheckAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.request.TemporaryRequest;
import com.it.hgad.logisticsmanager.bean.response.TemporaryResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CancelCheckDb;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.ParamDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/4/25.
 */
public class TemporaryActivity extends CommonActivity {
    private static final int RESULT = 111;
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
                    infoOperating.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();

                    if (Constants.ISDEBUG) {
                        for (int i = 0; i < 5; i++) {
                            listData.add(new CheckOrder("办公设备", "2017-02-10T16:44:31", "DM20170109153523", " 16,15,15", "设备二", "test2, test, test", 9, "2017011800081", "2017-02-08T13:25:00", "dd", "2017-01-18T14:25:00", "设备二巡检", 179, "0", 180));
                        }
                    }
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

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PARAM_COMMIT_OK);
        registerReceiver(receiver, intentFilter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_check);
        initHeader("临时巡检");
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
        if (!isNetWork) {
//            getOutlineData();
//            lv.setPullRefreshEnable(false);
//            lv.setPullLoadEnable(false);
        } else {
            if (operatingAnim != null) {
                infoOperating.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 2000);
            }
            TemporaryRequest temporaryRequest = new TemporaryRequest(userId, currentPage + "");
            sendRequest(temporaryRequest, TemporaryResponse.class);
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
        adapter = new CheckAdapter(listData, this, CheckConstants.TEMP);
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
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(TemporaryActivity.this, CheckDetailActivity.class);
            CheckOrder order = listData.get(position - 1);
            intent.putExtra(Constants.DATA, (Serializable) order);
            intent.putExtra(Constants.CHECK_TYPE, CheckConstants.TEMP);
            startActivity(intent);
        }
    };

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

    private void callRefresh() {
        mCurrentIndex = 0;
        boolean isNetWork = Utils.checkNetWork(this);
        if (isNetWork) {
            currentPage = 1;
            if (listData.size() != 0) {
                lv.setSelection(mCurrentIndex);
            }
            TemporaryRequest temporaryRequest = new TemporaryRequest(userId, currentPage + "");
            sendRequest(temporaryRequest, TemporaryResponse.class);
            currentPage--;
        } else {
//            getOutlineData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT);
    }

    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(this);
        if (isNetWork) {
            currentPage++;
            TemporaryRequest temporaryRequest = new TemporaryRequest(userId, currentPage + "");
            sendRequest(temporaryRequest, TemporaryResponse.class);
            currentPage--;
        } else {
//            getOutlineData();
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof TemporaryRequest) {
            TemporaryResponse temporaryResponse = (TemporaryResponse) response;
            if (temporaryResponse.getData() != null) {
                List<TemporaryResponse.DataEntity.ListdataEntity> data = temporaryResponse.getData().getListdata();
                currentPage++;
                if (currentPage == 1) {
                    if (listData != null) {
                        listData.clear();
                    }
                }
                if (currentPage == temporaryResponse.getData().getCurrentpage()) {
                    if (data != null && data.size() != 0) {
                        for (TemporaryResponse.DataEntity.ListdataEntity task : data) {
                            if (task != null) {
                                int taskId = task.getId();
                                CheckOrder order = new CheckOrder(task.getDeviceType(), task.getFinishTime(), task.getRemark(), task.getInspector(), task.getDeviceName(), task.getInspectorName(), Integer.parseInt(task.getDvcId().trim()),
                                        task.getTaskCode(), task.getPlanTime(), task.getSendNotice(), task.getShouldTime(), task.getTaskName(), task.getId(), task.getTaskStatus(), taskId);
                                listData.add(order);
                            }
                        }
                    }
                }
            }
            handler.sendEmptyMessageDelayed(Constants.STOP, 500);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
