package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.MaintainAdapter;
import com.it.hgad.logisticsmanager.adapter.MaintenanceAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.OrderListRequest;
import com.it.hgad.logisticsmanager.bean.response.OrderListResponse;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.OrderDb;
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
 * Created by Administrator on 2017/2/21.
 */
public class PullResultActivity extends CommonActivity implements MaintenanceAdapter.AdapterRefreshListener {

    private XListView lv;
    private List<Order> listData = new ArrayList<>();
    private MaintainAdapter adapter;
    private DbUtils db;
    private int userId;
    private int currentPage = 1;
    private String shouldRecieve;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_result);
        initHeader("新的任务");
        initView();
        initData();
        Intent intent = getIntent();
        onNewIntent(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        shouldRecieve = SPUtils.getString(this, RepairConstants.SHOULD_RECIEVE);
        if (db == null) {
            db = LocalApp.getDb();
        }
        listData.clear();
//        Order order = (Order) intent.getSerializableExtra(Constants.NOTIFICATION_MAINTANCE);
//        try {
//            OrderDb orderDb = db.findById(OrderDb.class, order.getId());
//            if(orderDb!=null) {
//                order.setData(orderDb);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        listData.add(order);
//        adapter.notifyDataSetChanged();
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            OrderListRequest orderListRequest = new OrderListRequest(shouldRecieve, userId, currentPage + "");
            sendRequest(orderListRequest, OrderListResponse.class);
        } else {

        }
    }

    private void initView() {
        lv = (XListView) findViewById(R.id.lv_result);
        adapter = new MaintainAdapter(listData, PullResultActivity.this);
        lv.setAdapter(adapter);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(mIXListViewListener);
//        adapter.setListener(this);
    }

    private boolean afterLoadMore;
    private int mCurrentIndex = 0;
    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            forRefresh();
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
        boolean isNetWork = Utils.checkNetWork(PullResultActivity.this);
        if (isNetWork) {
            currentPage = currentPage + 1;
//        OrderListRequest orderListRequest = new OrderListRequest("", userId, currentPage + "");
//        NetUtil.sendRequest(orderListRequest, OrderListResponse.class,MaintencePager.this);
//        onRefreshListener.loadMore();
//        if ("0".equals(type)) {
//            currentPageAll = currentPageAll + 1;
//            nowType = 0;
            OrderListRequest orderListRequest = new OrderListRequest(shouldRecieve, userId, currentPage + "");
            sendRequest(orderListRequest, OrderListResponse.class);
        }
        if (afterLoadMore) {
        }
        afterLoadMore = false;
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof OrderListRequest) {
            OrderListResponse orderListResponse = ((OrderListResponse) response);
            if (orderListResponse != null) {
                final List<OrderListResponse.DataEntity.ListdataEntity> data = orderListResponse.getData().getListdata();
                if (currentPage == 1) {
                    if (listData != null) {
                        listData.clear();
                    }
                }
                if (currentPage == orderListResponse.getData().getCurrentpage()) {
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

    private void getOrderData(final List<OrderListResponse.DataEntity.ListdataEntity> data) {
        for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
            if (entity != null) {
                Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                        entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
                listData.add(order);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void saveToDb(List<OrderListResponse.DataEntity.ListdataEntity> data) throws DbException {
        for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
            Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                    entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
            OrderDb orderDb = new OrderDb();
            orderDb.setData(order);
            orderDb.setUserId(userId);
            db.saveOrUpdate(orderDb);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void startCommit() {

    }

    @Override
    public void forRefresh() {
        listData.clear();
        currentPage = 1;
        OrderListRequest orderListRequest = new OrderListRequest(shouldRecieve, userId, currentPage + "");
        sendRequest(orderListRequest, OrderListResponse.class);
    }
}
