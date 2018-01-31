package com.it.hgad.logisticsmanager.pager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.JobDetailActivity;
import com.it.hgad.logisticsmanager.activity.MaintenanceActivity;
import com.it.hgad.logisticsmanager.adapter.MaintenanceAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.CancelShelveRequest;
import com.it.hgad.logisticsmanager.bean.request.OrderListRequest;
import com.it.hgad.logisticsmanager.bean.request.ShelveRequest;
import com.it.hgad.logisticsmanager.bean.request.StartRepairRequest;
import com.it.hgad.logisticsmanager.bean.response.CancelShelveResponse;
import com.it.hgad.logisticsmanager.bean.response.OrderListResponse;
import com.it.hgad.logisticsmanager.bean.response.ShelveResponse;
import com.it.hgad.logisticsmanager.bean.response.StartRepairResponse;
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

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.itcast.ecshop.net.NetUtil;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/1/3.
 */
public class MaintencePager extends FrameLayout implements Callback<BaseReponse>, MaintenanceActivity.OnRefreshListener, MaintenanceAdapter.AdapterRefreshListener {
    protected Context mContext;
    protected View mView;
    private XListView lv;
    private List<Order> listdata = new ArrayList<>();
    public MaintenanceAdapter maintenanceAdapter;
    private String type;
    private int userId;
    private DbUtils db;
    private int currentPage = 1;
    private Animation operatingAnim;
    private ImageView infoOperating;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    infoOperating.setVisibility(INVISIBLE);
                    infoOperating.clearAnimation();
                    maintenanceAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private RelativeLayout rl_infoOperating;
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;

    public String getType() {
        return type;
    }

    public MaintencePager(Context mContext, String type) {
        super(mContext);
        this.mContext = mContext;
        this.type = type;
        init();
    }

    private void init() {
        initView();
        initData();
    }

    public void setType(String type) {
        this.type = type;
    }

    private void initData() {
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(OrderDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);

        maintenanceAdapter.notifyDataSetChanged();
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (!isNetWork) {
            getOutlineData();
//            lv.setPullRefreshEnable(false);
//            lv.setPullLoadEnable(false);
        } else {
            if (operatingAnim != null) {
                infoOperating.setVisibility(VISIBLE);
                infoOperating.startAnimation(operatingAnim);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                       if (){
//
//                       }
//                    }
//                },10000);
            }
            OrderListRequest orderListRequest = new OrderListRequest(type, userId, currentPage + "");
            NetUtil.sendRequest(orderListRequest, OrderListResponse.class, MaintencePager.this);
//            AllOrderRequest allOrderRequest = new AllOrderRequest(userId);
//            NetUtil.sendRequest(allOrderRequest, OrderListResponse.class,this);

        }
    }

    private void getOutlineData() {
        listdata.clear();
        try {
            List<OrderDb> all = db.findAll(OrderDb.class);
            if (all != null && all.size() != 0) {
                for (int i = all.size(); i > 0; i--) {
                    OrderDb orderDb = all.get(i - 1);
                    if (orderDb != null) {
                        if (userId == orderDb.getUserId()) {
                            if (RepairConstants.ALL_REPAIR.equals(type)) {
                                listdata.add(new Order(orderDb.getRepairReply(), orderDb.getRepairMan(), orderDb.getFinishTime(), orderDb.getRepairTel(), orderDb.getRegisterTime(), orderDb.getRepairType(), orderDb.getRepairNo(), orderDb.getShelveTime(), orderDb.getRegisterMan(), orderDb.getRepairFlag(), orderDb.getRepairContent(), orderDb.getSameCondition(),
                                        orderDb.getShelveReason(), orderDb.getUserIds(), orderDb.getUserNames(), orderDb.getRepairDept(), orderDb.getRepairImg(), orderDb.getSpotImg(), orderDb.getRepairId(), orderDb.getRepairSrc(), orderDb.getRepairLoc()));
                            } else {
                                addToData(orderDb);
                            }
                        }
                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        if (listdata != null && listdata.size() != 0) {
            Collections.sort(listdata, new Comparator<Order>() {
                @Override
                public int compare(Order lhs, Order rhs) {
                    return lhs.getRepairFlag() - rhs.getRepairFlag();
                }
            });
        }
        maintenanceAdapter.notifyDataSetChanged();
    }


    private void addToData(OrderDb orderDb) {
        if (type.equals(orderDb.getRepairFlag() + "")) {
            listdata.add(new Order(orderDb.getRepairReply(), orderDb.getRepairMan(), orderDb.getFinishTime(), orderDb.getRepairTel(), orderDb.getRegisterTime(), orderDb.getRepairType(), orderDb.getRepairNo(), orderDb.getShelveTime(), orderDb.getRegisterMan(), orderDb.getRepairFlag(), orderDb.getRepairContent(), orderDb.getSameCondition(),
                    orderDb.getShelveReason(), orderDb.getUserIds(), orderDb.getUserNames(), orderDb.getRepairDept(), orderDb.getRepairImg(), orderDb.getSpotImg(), orderDb.getRepairId(), orderDb.getRepairSrc(), orderDb.getRepairLoc()));

        }
    }

    private void initView() {
        shouldRecieve = SPUtils.getString(mContext, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(mContext, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(mContext, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(mContext, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(mContext, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(mContext, RepairConstants.HAVE_FINISH);
        mView = View.inflate(mContext, R.layout.pager_maintence, this);
        lv = (XListView) mView.findViewById(R.id.lv_last_order);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(mIXListViewListener);
        maintenanceAdapter = new MaintenanceAdapter(listdata, mContext, type);
        maintenanceAdapter.setListener(MaintencePager.this);
        lv.setAdapter(maintenanceAdapter);
        lv.setEmptyView(mView.findViewById(R.id.tv_empty));
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setOnItemLongClickListener(mOnItemLongClickListener);
//        CustomProgressDialog dialog = new CustomProgressDialog()
//        this.addView(mView);
//        rl_infoOperating = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }


    private XListView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            LayoutInflater factory = LayoutInflater.from(mContext);//提示框
            final View dialogView = factory.inflate(R.layout.editbox_layout, null);//这里必须是final的
            final EditText edit = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象

            if ((listdata.get(position - 1).getRepairFlag() + "").equals(shouldStart)) {
                new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setTitle("挂起理由")//提示框标题
                        .setView(dialogView)
                        .setPositiveButton("确定",//提示框的两个按钮
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //事件
                                        String reason = edit.getText().toString().trim();
                                        if (!TextUtils.isEmpty(reason)) {
                                            int repairId = listdata.get(position - 1).getId();
                                            ShelveRequest shelveRequest = new ShelveRequest(repairId + "", reason);
                                            NetUtil.sendRequest(shelveRequest, ShelveResponse.class, MaintencePager.this);
                                        } else {
                                            CommonUtils.showToast(mContext, "请填写理由");
                                        }

                                    }
                                }).setNegativeButton("取消", null).create().show();
            } else if ((listdata.get(position - 1).getRepairFlag() + "").equals(haveShelve)) {
                new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setTitle("确认解除挂起")//提示框标题
                        .setPositiveButton("确定",//提示框的两个按钮
                                new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //事件
                                        int repairId = listdata.get(position - 1).getId();
                                        CancelShelveRequest cancelShelveRequest = new CancelShelveRequest(repairId + "");
                                        NetUtil.sendRequest(cancelShelveRequest, CancelShelveResponse.class, MaintencePager.this);
                                    }
                                }).setNegativeButton("取消", null).create().show();
            }
            return true;
        }
    };
    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mContext, JobDetailActivity.class);
            Order order = listdata.get(position - 1);
            intent.putExtra(Constants.DATA, (Serializable) order);
            mContext.startActivity(intent);
        }
    };
    //    private boolean afterLoadMore;
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
            mCurrentIndex = maintenanceAdapter.getCount();
            callLoadMore();
            lv.stopLoadMore();
//            afterLoadMore = true;
        }
    };

    public void callRefresh() {
        mCurrentIndex = 0;
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
//            listdata.clear();
//            if (operatingAnim != null) {
//                infoOperating.setVisibility(VISIBLE);
//                infoOperating.startAnimation(operatingAnim);
//            }
            currentPage = 1;
            OrderListRequest orderListRequest = new OrderListRequest(type, userId, currentPage + "");
            NetUtil.sendRequest(orderListRequest, OrderListResponse.class, MaintencePager.this);
        } else {
            getOutlineData();
        }
    }


    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            currentPage = currentPage + 1;
//        OrderListRequest orderListRequest = new OrderListRequest("", userId, currentPage + "");
//        NetUtil.sendRequest(orderListRequest, OrderListResponse.class,MaintencePager.this);
//        onRefreshListener.loadMore();
//        if ("0".equals(type)) {
//            currentPageAll = currentPageAll + 1;
//            nowType = 0;

            OrderListRequest orderListRequest = new OrderListRequest(type, userId, currentPage + "");
            NetUtil.sendRequest(orderListRequest, OrderListResponse.class, MaintencePager.this);
        } else {
            getOutlineData();
        }
//        if (afterLoadMore) {
//
//        }
//        afterLoadMore = false;
    }


    /**
     * @return 提供给外界使用，暴露出此对象所维护的View
     */
    public View getView() {
        return mView;
    }

    public void onResume() {
        if (infoOperating != null) {
            infoOperating.setVisibility(INVISIBLE);
            infoOperating.clearAnimation();
        }
    }

    @Override
    public void onSuccess(BaseRequest request, BaseReponse response) {
        if (request instanceof ShelveRequest) {
            ShelveResponse shelveResponse = (ShelveResponse) response;

            if (shelveResponse != null) {
                if ("0".equals(shelveResponse.getResult())) {
                    CommonUtils.showToast(mContext, "挂起成功");
                    callRefresh();
                    maintenanceAdapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showToast(mContext, "挂起失败");
                }
            }
        } else if (request instanceof CancelShelveRequest) {
            CancelShelveResponse cancelShelveResponse = (CancelShelveResponse) response;

            if (cancelShelveResponse != null) {
                if ("0".equals(cancelShelveResponse.getResult())) {
                    CommonUtils.showToast(mContext, "取消成功");
                    callRefresh();
                    maintenanceAdapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showToast(mContext, "取消失败");
                }
            }
        } else if (request instanceof OrderListRequest) {
            OrderListResponse orderListResponse = ((OrderListResponse) response);
            if (orderListResponse != null) {
                final List<OrderListResponse.DataEntity.ListdataEntity> data = orderListResponse.getData().getListdata();
                if (currentPage == 1) {
                    if (listdata != null) {
                        listdata.clear();
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
        } else if (request instanceof StartRepairRequest) {
            StartRepairResponse startRepairResponse = (StartRepairResponse) response;
            if (startRepairResponse != null) {
                if ("0".equals(startRepairResponse.getResult())) {
//                    this.notifyDataSetChanged();
//                    if (listener != null) {
//                        listener.forRefresh();
//                    }
//                    CommonUtils.showToast(mContext, mContext.getString(R.string.start_maintance));
                } else {
//                    CommonUtils.showToast(mContext, mContext.getString(R.string.start_fail));
                }
            }
        }
    }


    private void getOrderData(final List<OrderListResponse.DataEntity.ListdataEntity> data) {
        for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
            Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                    entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
            try {
                List<CommitDb> all = db.findAll(CommitDb.class);
                if (all != null && all.size() != 0) {
                    for (CommitDb commitDb : all
                            ) {
                        if (commitDb.getRepairId() == order.getId() && commitDb.getHasCommit() == 0) {
                            String startTime = commitDb.getStartTime();
                            order.setRepairFlag(4);
                            if (startTime == null) {
                                startTime = "";
                            }
                            String delayTime = commitDb.getDelayTime();
                            if (delayTime == null) {
                                delayTime = "";
                            }
//                            order.setRepairFlag(4);
                            StartRepairRequest request = new StartRepairRequest(commitDb.getRepairId() + "", "1", startTime, delayTime);
                            NetUtil.sendRequest(request, StartRepairResponse.class, MaintencePager.this);
                            commitDb.setHasCommit(2);
                            db.update(commitDb, "hasCommit");
                        }
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            listdata.add(order);

        }
        Collections.sort(listdata, new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                return lhs.getRepairFlag() - rhs.getRepairFlag();
            }
        });
        if (!RepairConstants.ALL_REPAIR.equals(type)) {
            Collections.sort(listdata, new Comparator<Order>() {
                @Override
                public int compare(Order lhs, Order rhs) {
                    return rhs.getRegisterTime().compareTo(lhs.getRegisterTime());
                }
            });
        }
        lv.setSelection(mCurrentIndex);
        handler.sendEmptyMessageDelayed(Constants.STOP, 500);
    }

    private void saveToDb(List<OrderListResponse.DataEntity.ListdataEntity> data) throws DbException {
        for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
//            List<OrderDb> all = db.findAll(OrderDb.class);
//            Boolean have = false;
            Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                    entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
            OrderDb orderDb = getOrderDbEntity(order);
//            for (OrderDb orderDb1 : all) {
//                if (orderDb.getRepairId() == orderDb1.getRepairId()) {
//                    have = true;
//                    break;
//                }
//            }
//            if (!have) {
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
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {
    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

    @Override
    public void refresh() {
        callRefresh();
    }

    @Override
    public void startCommit() {

    }

    @Override
    public void forRefresh() {
        callRefresh();
    }

}
