package com.it.hgad.logisticsmanager.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.JobDetailActivity;
import com.it.hgad.logisticsmanager.adapter.MaintainAdapter;
import com.it.hgad.logisticsmanager.adapter.MaintenanceAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.OrderListRequest;
import com.it.hgad.logisticsmanager.bean.response.OrderListResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

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
 * Created by Administrator on 2017/4/19.
 */
public class ShelveFragment extends BaseFragment {

    private View mView;
    private XListView lv;
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;
    private Animation operatingAnim;
    private List<Order> listData = new ArrayList<Order>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    rl_info.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    lv.setSelection(mCurrentIndex);
                    if (Constants.ISDEBUG) {
                        for (int i = 0; i < 5; i++) {
                            listData.add(new Order("", "4666", "", "15071481976", "2017-01-17T11:34:58", "更换", "20170117113506", "", "Admin", 4, "4666", "", "", "5, 4", "余建军,凌永平", "4666", "", "", 33, "电话报修", "新大楼住院部负1楼供应室二单元15楼"));
                        }
                    }
                    maintainAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ImageView infoOperating;
    private MaintenanceAdapter adapter;
    private PopupWindow popupWindow;
    private LinearLayout ll_date_range;
    private LinearLayout ll_condition;
    private LinearLayout ll_sort;
    private TextView tv_data_range;
    private TextView tv_condition;
    private TextView tv_sort;
    private ImageView iv_triangle_range;
    private ImageView iv_triangle_condition;
    private ImageView iv_triangle_sort;
    private MaintainAdapter maintainAdapter;
    private DbUtils db;
    private int userId;
    private int currentPage = 1;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.COMMIT_OK) || action.equals(Constants.SHELVE_OK) || action.equals(Constants.CANCEL_SHELVE)) {
                callRefresh();
            }
        }
    };
    private RelativeLayout rl_info;

    @Override
    protected void initData() {

        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(OrderDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (!isNetWork) {
            getOutlineData();
//            lv.setPullRefreshEnable(false);
//            lv.setPullLoadEnable(false);
        } else {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 2000);
            }
            OrderListRequest orderListRequest = new OrderListRequest(haveShelve, userId, currentPage + "");
            sendRequest(orderListRequest, OrderListResponse.class);
            currentPage--;
        }
    }

    @Override
    protected void initView() {
        shouldRecieve = SPUtils.getString(mContext, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(mContext, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(mContext, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(mContext, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(mContext, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(mContext, RepairConstants.HAVE_FINISH);
        lv = (XListView) mView.findViewById(R.id.lv_maintain);
        adapter = new MaintenanceAdapter(listData, mContext, "");
        maintainAdapter = new MaintainAdapter(listData, mContext);
        lv.setAdapter(maintainAdapter);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setXListViewListener(mIXListViewListener);
        lv.setEmptyView(mView.findViewById(R.id.tv_empty));
        initAnimation();
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_shelve, null);
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registerReceiver();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mContext, JobDetailActivity.class);
            Order order = listData.get(position - 1);
            intent.putExtra(Constants.DATA, (Serializable) order);
            mContext.startActivity(intent);
        }
    };


    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.COMMIT_OK);
        intentFilter.addAction(Constants.SHELVE_OK);
        intentFilter.addAction(Constants.CANCEL_SHELVE);
        mContext.registerReceiver(receiver, intentFilter);
    }

    private void getOutlineData() {
        listData.clear();
        try {
            List<OrderDb> all = db.findAll(OrderDb.class);
            if (all != null && all.size() != 0) {
                for (int i = all.size(); i > 0; i--) {
                    OrderDb orderDb = all.get(i - 1);
                    if (orderDb != null) {
                        if (userId == orderDb.getUserId()) {
                            if (RepairConstants.ALL_REPAIR.equals(haveShelve)) {
                                listData.add(new Order(orderDb.getRepairReply(), orderDb.getRepairMan(), orderDb.getFinishTime(), orderDb.getRepairTel(), orderDb.getRegisterTime(), orderDb.getRepairType(), orderDb.getRepairNo(), orderDb.getShelveTime(), orderDb.getRegisterMan(), orderDb.getRepairFlag(), orderDb.getRepairContent(), orderDb.getSameCondition(),
                                        orderDb.getShelveReason(), orderDb.getUserIds(), orderDb.getUserNames(), orderDb.getRepairDept(), orderDb.getRepairImg(), orderDb.getSpotImg(), orderDb.getRepairId(), orderDb.getRepairSrc(), orderDb.getRepairLoc()));
//                            } else {
//                                addToData(orderDb);
                            }
                        }
                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        if (listData != null && listData.size() != 0) {
            Collections.sort(listData, new Comparator<Order>() {
                @Override
                public int compare(Order lhs, Order rhs) {
                    return lhs.getRepairFlag() - rhs.getRepairFlag();
                }
            });
        }
        maintainAdapter.notifyDataSetChanged();
    }


    private void initAnimation() {
        rl_info = (RelativeLayout) mView.findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) mView.findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
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

    private void callRefresh() {
        mCurrentIndex = 0;
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            currentPage = 1;
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 2000);
            }
            OrderListRequest orderListRequest = new OrderListRequest(haveShelve, userId, currentPage + "");
            sendRequest(orderListRequest, OrderListResponse.class);
            currentPage--;
        } else {
            getOutlineData();
        }
    }

    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            currentPage++;
            OrderListRequest orderListRequest = new OrderListRequest(haveShelve, userId, currentPage + "");
            sendRequest(orderListRequest, OrderListResponse.class);
            currentPage--;
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof OrderListRequest) {
            OrderListResponse orderListResponse = ((OrderListResponse) response);
            if (orderListResponse != null) {
                final List<OrderListResponse.DataEntity.ListdataEntity> data = orderListResponse.getData().getListdata();
                currentPage++;
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
            Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                    entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
//            try {
//                List<CommitDb> all = db.findAll(CommitDb.class);
//                if (all != null && all.size() != 0) {
//                    for (CommitDb commitDb : all
//                            ) {
//                        if (commitDb.getRepairId() == order.getId() && commitDb.getHasCommit() == 0) {
//                            String startTime = commitDb.getStartTime();
//                            order.setRepairFlag(4);
//                            if (startTime == null) {
//                                startTime = "";
//                            }
//                            String delayTime = commitDb.getDelayTime();
//                            if (delayTime == null) {
//                                delayTime = "";
//                            }
////                            order.setRepairFlag(4);
//                            StartRepairRequest request = new StartRepairRequest(commitDb.getRepairId() + "", "1", startTime, delayTime);
//                            NetUtil.sendRequest(request, StartRepairResponse.class, MaintencePager.this);
//                            commitDb.setHasCommit(2);
//                            db.update(commitDb, "hasCommit");
//                        }
//                    }
//                }
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
            listData.add(order);

        }
//        Collections.sort(listData, new Comparator<Order>() {
//            @Override
//            public int compare(Order lhs, Order rhs) {
//                return lhs.getRepairFlag() - rhs.getRepairFlag();
//            }
//        });
//        Collections.sort(listData, new Comparator<Order>() {
//            @Override
//            public int compare(Order lhs, Order rhs) {
//                return rhs.getRegisterTime().compareTo(lhs.getRegisterTime());
//            }
//        });

        handler.sendEmptyMessageDelayed(Constants.STOP, 500);
    }

    private void saveToDb(List<OrderListResponse.DataEntity.ListdataEntity> data) throws DbException {
        for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
            Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                    entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
            OrderDb orderDb = getOrderDbEntity(order);
            try {
                List<CommitDb> all = db.findAll(CommitDb.class);
                if (all != null && all.size() != 0) {
                    for (CommitDb commitDb : all
                            ) {
                        if (commitDb.getRepairId() == order.getId() && commitDb.getHasCommit() == 0) {
                            orderDb.setRepairFlag(4);
                        }
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            db.saveOrUpdate(orderDb);
        }
    }

    private OrderDb getOrderDbEntity(Order entity) {
        int id = entity.getId();
        String repairReply = entity.getRepairReply();
        String repairMan = entity.getRepairMan();
        String finishTime = entity.getFinishTime();
        String repairTel = entity.getRepairTel();
        String registerTime = entity.getRegisterTime();
        String repairType = entity.getRepairType();
        String repairNo = entity.getRepairNo();
        String shelveTime = entity.getShelveTime();
        String registerMan = entity.getRegisterMan();
        int repairFlag = entity.getRepairFlag();
        String repairContent = entity.getRepairContent();
        String sameCondition = entity.getSameCondition();
        String shelveReason = entity.getShelveReason();
        String userIds = entity.getUserIds();
        String userNames = entity.getUserNames();
        String repairDept = entity.getRepairDept();
        String repairImg = entity.getRepairImg();
        String spotImg = entity.getSpotImg();
        String repairSrc = entity.getRepairSrc();
        String repairLoc = entity.getRepairLoc();
        OrderDb orderDb = new OrderDb(id, userId, repairReply, repairMan, finishTime, repairTel, registerTime, repairType,
                repairNo, shelveTime, registerMan, repairFlag, repairContent, sameCondition, shelveReason, userIds,
                userNames, repairDept, repairImg, spotImg, repairSrc, repairLoc);
        return orderDb;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }

    }

}
