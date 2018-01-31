package com.it.hgad.logisticsmanager.pager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.CheckDetailActivity;
import com.it.hgad.logisticsmanager.adapter.CheckAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.request.AllCheckRequest;
import com.it.hgad.logisticsmanager.bean.request.CancelCheckRequest;
import com.it.hgad.logisticsmanager.bean.request.CheckOrderListRequest;
import com.it.hgad.logisticsmanager.bean.response.CancelCheckResponse;
import com.it.hgad.logisticsmanager.bean.response.CheckOrderListResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.itcast.ecshop.net.NetUtil;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/2/5.
 */
public class CheckPager extends FrameLayout implements Callback<BaseReponse> {
    protected Context mContext;
    private String type;
    protected View mView;
    private XListView lv;
    private int userId;
    private CheckAdapter checkAdapter;
    private int mCurrentIndex = 0;
    private int currentPage = 1;
    private List<CheckOrder> listdata = new ArrayList<>();
    private DbUtils db;
    private ImageView infoOperating;
    private Animation operatingAnim;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    infoOperating.setVisibility(INVISIBLE);
                    infoOperating.clearAnimation();
                    checkAdapter.notifyDataSetChanged();
                    break;
                case Constants.WARM:
                    Utils.showTost(mContext, mContext.getString(R.string.no_net) + "\n该次巡检会在有网络信号时自动取消");
                    callRefresh();
                    break;
            }
        }
    };
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;

    public CheckPager(Context mContext, String type) {
        super(mContext);
        this.mContext = mContext;
        this.type = type;
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initData() {
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
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);

        checkAdapter.notifyDataSetChanged();
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            if (operatingAnim != null) {
                infoOperating.setVisibility(VISIBLE);
                infoOperating.startAnimation(operatingAnim);
            }
            CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, type, currentPage + "");
            NetUtil.sendRequest(checkOrderListRequest, CheckOrderListResponse.class, CheckPager.this);
//            AllCheckRequest allCheckRequest = new AllCheckRequest(userId);
//            NetUtil.sendRequest(allCheckRequest, CheckOrderListResponse.class, this);
        } else {
            getOutlineData();
        }
    }

    private void addToData(CheckOrderDb checkOrderDb) {
        if (type.equals(checkOrderDb.getTaskStatus())) {
            CheckOrder checkOrder = new CheckOrder();
            checkOrder.setData(checkOrderDb);
            listdata.add(checkOrder);
        }
    }

    private void initView() {
        should = SPUtils.getString(mContext, CheckConstants.SHOULD);
        done = SPUtils.getString(mContext, CheckConstants.DONE);
        over = SPUtils.getString(mContext, CheckConstants.OVER);
        overFinish = SPUtils.getString(mContext, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(mContext, CheckConstants.CANCEL);
        mView = View.inflate(mContext, R.layout.pager_check, this);
        lv = (XListView) mView.findViewById(R.id.lv_check);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(mIXListViewListener);
        checkAdapter = new CheckAdapter(listdata, mContext, type);
        lv.setAdapter(checkAdapter);
        lv.setEmptyView(mView.findViewById(R.id.tv_empty));
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setOnItemLongClickListener(onItemLongClickListener);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            callRefresh();
            lv.setRefreshTime(CommonUtils.getCurrentTime());
            lv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            mCurrentIndex = checkAdapter.getCount();
            callLoadMore();
            lv.stopLoadMore();
//            afterLoadMore = true;
        }
    };

    private void getOutlineData() {
        listdata.clear();
        try {
            List<CheckOrderDb> all = db.findAll(CheckOrderDb.class);
            if (all != null && all.size() != 0) {
                for (int i = all.size(); i > 0; i--) {
                    CheckOrderDb checkOrderDb = all.get(i - 1);
                    if (checkOrderDb != null) {
                        if (userId == checkOrderDb.getUserId()) {
                            if (RepairConstants.ALL_REPAIR.equals(type)) {
                                listdata.add(new CheckOrder(checkOrderDb.getDeviceType(), checkOrderDb.getFinishTime(), checkOrderDb.getDeviceCode(), checkOrderDb.getInspector(), checkOrderDb.getDeviceName()
                                        , checkOrderDb.getInspectorName(), checkOrderDb.getArrageId(), checkOrderDb.getTaskCode(), checkOrderDb.getPlanTime(), checkOrderDb.getResponser(), checkOrderDb.getShouldTime(), checkOrderDb.getTaskName(), checkOrderDb.getTaskId(), checkOrderDb.getTaskStatus(), checkOrderDb.getTaskResultId()));
                            } else {
                                addToData(checkOrderDb);
                            }
                        }
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (listdata != null && listdata.size() != 0) {
            Collections.sort(listdata, new Comparator<CheckOrder>() {
                @Override
                public int compare(CheckOrder lhs, CheckOrder rhs) {
                    return rhs.getPlanTime().compareTo(lhs.getPlanTime());
                }
            });
            Collections.sort(listdata, new Comparator<CheckOrder>() {
                @Override
                public int compare(CheckOrder lhs, CheckOrder rhs) {
                    return lhs.getTaskStatus().compareTo(rhs.getTaskStatus());
                }
            });
        }
        checkAdapter.notifyDataSetChanged();
    }

    public void callRefresh() {
        mCurrentIndex = 0;
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            listdata.clear();
//            if (operatingAnim != null) {
//                infoOperating.setVisibility(VISIBLE);
//                infoOperating.startAnimation(operatingAnim);
//            }
            currentPage = 1;
            CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, type, currentPage + "");
            NetUtil.sendRequest(checkOrderListRequest, CheckOrderListResponse.class, CheckPager.this);
        } else {
            getOutlineData();
        }
    }

    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            currentPage = currentPage + 1;
            CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, type, currentPage + "");
            NetUtil.sendRequest(checkOrderListRequest, CheckOrderListResponse.class, CheckPager.this);
        }
    }

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mContext, CheckDetailActivity.class);
            CheckOrder order = listdata.get(position - 1);
            intent.putExtra("data", (Serializable) order);
            mContext.startActivity(intent);
        }
    };

    private XListView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            if ((listdata.get(position - 1).getTaskStatus()).equals(should) || (listdata.get(position - 1).getTaskStatus()).equals(over)) {
                new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setTitle("确定取消该次巡检")//提示框标题
                        .setPositiveButton("确定",//提示框的两个按钮
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //事件
                                        CheckOrder checkOrder = listdata.get(position - 1);
                                        int taskId = checkOrder.getId();
                                        boolean checkNetWork = Utils.checkNetWork(mContext);
                                        if (checkNetWork) {
                                            CancelCheckRequest cancelCheckRequest = new CancelCheckRequest(taskId + "");
                                            NetUtil.sendRequest(cancelCheckRequest, CancelCheckResponse.class, CheckPager.this);
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
            return true;
        }
    };

    public View getView() {
        return mView;
    }

    @Override
    public void onSuccess(BaseRequest request, BaseReponse response) {
        if (request instanceof CheckOrderListRequest) {
            CheckOrderListResponse checkOrderListResponse = (CheckOrderListResponse) response;
            if (checkOrderListResponse != null) {
                List<CheckOrderListResponse.DataEntity.ListdataEntity> data = checkOrderListResponse.getData().getListdata();
                if (currentPage == 1) {
                    if (this.listdata != null) {
                        this.listdata.clear();
                    }
                }
                if (currentPage == checkOrderListResponse.getData().getCurrentpage()) {
                    getOrderData(data);
                    saveToDb(data);
                }
            }
        } else if (request instanceof AllCheckRequest) {
            CheckOrderListResponse checkOrderListResponse = (CheckOrderListResponse) response;
            if (checkOrderListResponse != null) {
                final List<CheckOrderListResponse.DataEntity.ListdataEntity> listdata = checkOrderListResponse.getData().getListdata();
            }
        } else if (request instanceof CancelCheckRequest) {
            CancelCheckResponse cancelCheckResponse = (CancelCheckResponse) response;
            if (cancelCheckResponse != null) {
                if ("0".equals(cancelCheckResponse.getResult())) {
                    CommonUtils.showToast(mContext, "取消成功");
                    callRefresh();
                    checkAdapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showToast(mContext, "取消失败");
                }
            }
        }
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {

    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

    private void getOrderData(final List<CheckOrderListResponse.DataEntity.ListdataEntity> data) {
        for (int i = data.size(); i > 0; i--) {
            CheckOrderListResponse.DataEntity.ListdataEntity task = data.get(i - 1);
            CheckOrderListResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
            int taskId = task.getDeviceTaskResult().getId();
            CheckOrder order = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName(), entity.getInspectorName(), entity.getArrageId(),
                    entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
            listdata.add(order);
        }

        if (CheckConstants.ALL_CHECK.equals(type)) {
            Collections.sort(listdata, new Comparator<CheckOrder>() {
                @Override
                public int compare(CheckOrder lhs, CheckOrder rhs) {
                    if (lhs.getTaskStatus().equals("2") && rhs.getTaskStatus().equals("3")) {
                        return rhs.getTaskStatus().compareTo(lhs.getTaskStatus());
                    }
                    if (lhs.getTaskStatus().equals(rhs.getTaskStatus())) {
                        String planTime = lhs.getPlanTime();
                        String planDate = planTime.substring(0, planTime.lastIndexOf("T"));
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        String currentDate = format.format(new Date());
                        if (planDate.equals(currentDate)){
                            return lhs.getPlanTime().compareTo(rhs.getPlanTime());
                        }
                        return rhs.getPlanTime().compareTo(lhs.getPlanTime());
                    }
                    return lhs.getTaskStatus().compareTo(rhs.getTaskStatus());
                }
            });
        } else {
            Collections.sort(listdata, new Comparator<CheckOrder>() {
                @Override
                public int compare(CheckOrder lhs, CheckOrder rhs) {
                    return rhs.getPlanTime().compareTo(lhs.getPlanTime());
                }
            });
        }
        lv.setSelection(mCurrentIndex);
        handler.sendEmptyMessageDelayed(Constants.STOP, 500);
    }

    private void saveToDb(final List<CheckOrderListResponse.DataEntity.ListdataEntity> data) {
        ExecutorService thread = SingleThreadPool.getThread();
        thread.execute(new Runnable() {
            @Override
            public void run() {
                for (CheckOrderListResponse.DataEntity.ListdataEntity task : data) {
                    CheckOrderListResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
                    int taskId = task.getDeviceTaskResult().getId();
                    CheckOrder checkOrder = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName()
                            , entity.getInspectorName(), entity.getArrageId(), entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
                    final CheckOrderDb checkOrderDb = getCheckOrderDb(checkOrder);
                    CheckOrderListResponse.DataEntity.ListdataEntity.DeviceTaskResultEntity deviceTaskResult = task.getDeviceTaskResult();
                    List<CheckOrderListResponse.DataEntity.ListdataEntity.DeviceTaskResultEntity.DeviceParamSetEntity> deviceParamSet = deviceTaskResult.getDeviceParamSet();
                    ParamDb paramDb = getParamDb(deviceParamSet, deviceTaskResult.getId());
                    try {
                        db.saveOrUpdate(checkOrderDb);
                        db.saveOrUpdate(paramDb);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private ParamDb getParamDb(List<CheckOrderListResponse.DataEntity.ListdataEntity.DeviceTaskResultEntity.DeviceParamSetEntity> deviceParamSet, int id) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        String paramsJson = gson.toJson(deviceParamSet);
        ParamDb paramDb = new ParamDb(id, paramsJson,"");
        return paramDb;
    }

    private CheckOrderDb getCheckOrderDb(CheckOrder checkOrder) {
        CheckOrderDb checkOrderDb = new CheckOrderDb(checkOrder.getId(), userId, checkOrder.getDeviceType(), checkOrder.getFinishTime(), checkOrder.getDeviceCode(), checkOrder.getInspector(), checkOrder.getDeviceName(), checkOrder.getInspectorName(), checkOrder.getArrageId(), checkOrder.getTaskCode(), checkOrder.getPlanTime(), checkOrder.getResponser(), checkOrder.getShouldTime(), checkOrder.getTaskName(), checkOrder.getTaskStatus(), checkOrder.getTaskId());
        return checkOrderDb;
    }

    public void onResume() {
        if (infoOperating != null) {
            infoOperating.setVisibility(INVISIBLE);
            infoOperating.clearAnimation();
        }
    }
}
