package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.NewCheckAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.SectionBean;
import com.it.hgad.logisticsmanager.bean.request.CancelCheckRequest;
import com.it.hgad.logisticsmanager.bean.request.NewCheckRequest;
import com.it.hgad.logisticsmanager.bean.request.ParamCommitRequest;
import com.it.hgad.logisticsmanager.bean.response.CancelCheckResponse;
import com.it.hgad.logisticsmanager.bean.response.NewCheckOrderResponse;
import com.it.hgad.logisticsmanager.bean.response.ParamCommitResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CancelCheckDb;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.ParamDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.PopupWindowUtil;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.NetUtil;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/4/11.
 */
public class TaskCheckActivity extends CommonActivity {

    public static final String ROWS = "-1";
    private static final int RESULT = 112;
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;
    private PinnedSectionRefreshListView lv;
    private List<CheckOrder> listData = new ArrayList<>();
    private NewCheckAdapter adapter;
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
                    lv.setSelection(position);
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
    private List<SectionBean> newList = new ArrayList<>();
    private String currentDate = "";
    private String currentCondition = "";
    private String currentType = "";
    private RelativeLayout rl_info;
    private IntenterBoradCastReceiver netReceiver;
    private RelativeLayout rl_no_net;


    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PARAM_COMMIT_OK);
        registerReceiver(receiver, intentFilter);
        registerNetBroadrecevicer();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_check);
        initHeader("巡检任务");
        initView();
        initData();
    }

    //参数名
    private List<String> paramNames = new ArrayList<>();
    //参数值
    private List<String> paramValues = new ArrayList<>();
    //查询条件 like or =
    private List<String> paramConditons = new ArrayList<>();
    //数据类型 string Integer
    private List<String> paramTypes = new ArrayList<>();

    private void initData() {
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
        String type = intent.getStringExtra(Constants.CHECK_TYPE);
        tv_data_range.setText("全部日期");
        if (Constants.TODAY.equals(type)) {
//            tv_data_range.setText("今日");
            paramValues.add(0, "day");
        } else if (Constants.WEEK.equals(type)) {
//            tv_data_range.setText("本周");
            paramValues.add(0, "week");
        } else if (Constants.MONTH.equals(type)) {
//            tv_data_range.setText("本月");
            paramValues.add(0, "month");
        }
        paramNames.add(0, "deviceTask.orderType");
        paramNames.add(1, "deviceTask.inspector");
        paramNames.add(2, "deviceTask.taskStatus");
        paramValues.add(1, userId + "");
        paramValues.add(2, over + "," + should);
        paramConditons.add(0, "=");
        paramConditons.add(1, "like");
        paramConditons.add(2, "like");
        paramTypes.add(0, "string");
        paramTypes.add(1, "FIND_IN_SET");
        paramTypes.add(2, "OR");
        if (!isNetWork) {
            getOutlineData();
        } else {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 10000);
            }
            try {
                CommitCancelCheck();
            } catch (DbException e) {
                e.printStackTrace();
            }
//            CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, "", currentPage + "");
//            sendRequest(checkOrderListRequest, NewCheckOrderResponse.class);
            NewCheckRequest newCheckRequest = new NewCheckRequest(paramNames, paramValues, paramConditons, paramTypes, ROWS, 0);
            sendRequest(newCheckRequest, NewCheckOrderResponse.class);
            currentPage--;
        }
    }


    //监听网络状态变化的广播接收器
    public class IntenterBoradCastReceiver extends BroadcastReceiver {

        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络

                    }
                    rl_no_net.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Commit();
                                CommitCancelCheck();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    ////////网络断开
                    rl_no_net.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    private void registerNetBroadrecevicer() {
        //获取广播对象
        netReceiver = new IntenterBoradCastReceiver();
        //创建意图过滤器
        IntentFilter filter = new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, filter);
    }

    private void Commit() throws DbException {
        boolean have = false;
        final List<CheckCommitDb> commitDbs = db.findAll(CheckCommitDb.class);
        if (commitDbs != null && commitDbs.size() != 0) {
            for (int i = 0; i < commitDbs.size(); i++) {
                CheckCommitDb checkCommitDb = commitDbs.get(i);
                if (checkCommitDb.getHasCommit() == 1 && checkCommitDb.getUserId() == userId) {
                    have = true;
                }
            }
            if (have) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(TaskCheckActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                .setTitle("有之前未提交的巡检单")//提示框标题
                                .setPositiveButton("提交",//提示框的两个按钮
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
//                                                事件
                                                for (int i = 0; i < commitDbs.size(); i++) {
                                                    final CheckCommitDb checkCommitDb = commitDbs.get(i);
                                                    if (checkCommitDb.getHasCommit() == 1 && checkCommitDb.getUserId() == userId) {
                                                        int resultId = checkCommitDb.getResultId();
                                                        List<String> actualValues = Arrays.asList(checkCommitDb.getActualValues());
                                                        String feedback = checkCommitDb.getFeedback();
                                                        String finishTime = checkCommitDb.getFinishTime();
                                                        String paramId = checkCommitDb.getParamId();
                                                        ParamCommitRequest commitRequest = new ParamCommitRequest(userId + "", resultId + "", actualValues, feedback, finishTime, paramId);
                                                        NetUtil.sendRequest(commitRequest, ParamCommitResponse.class, TaskCheckActivity.this);
                                                    }
                                                }
                                            }
                                        }).setNegativeButton("取消", null).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                });
            }
        }
    }

    private void CommitCancelCheck() throws DbException {
        boolean have = false;
        List<CancelCheckDb> cancelCheckDbs = db.findAll(Selector.from(CancelCheckDb.class).where(WhereBuilder.b("hasCommit", "=", 1)));
        if (cancelCheckDbs != null && cancelCheckDbs.size() != 0) {
            for (final CancelCheckDb cancelCheckDb : cancelCheckDbs) {
                int taskId = cancelCheckDb.getTaskId();
                CancelCheckRequest cancelCheckRequest = new CancelCheckRequest(taskId + "");
                sendRequest(cancelCheckRequest, CancelCheckResponse.class);
                cancelCheckDb.setHasCommit(2);
                ExecutorService thread = SingleThreadPool.getThread();
                thread.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.saveOrUpdate(cancelCheckDb);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (netReceiver != null) {
            unregisterReceiver(netReceiver);
            netReceiver = null;
        }
    }

    private void getOutlineData() {
        listData.clear();
        try {
            List<CheckOrderDb> all = db.findAll(CheckOrderDb.class);
            if (all != null && all.size() != 0) {
                for (int i = all.size(); i > 0; i--) {
                    CheckOrderDb checkOrderDb = all.get(i - 1);
                    if (checkOrderDb != null) {
                        if (userId == checkOrderDb.getUserId() && (should.equals(checkOrderDb.getTaskStatus()) || over.equals(checkOrderDb.getTaskStatus()))) {
                            CheckOrder checkOrder = new CheckOrder();
                            checkOrder.setData(checkOrderDb);
                            listData.add(checkOrder);
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
                    return lhs.getPlanTime().compareTo(rhs.getPlanTime());
                }
            });
//            Collections.sort(listData, new Comparator<CheckOrder>() {
//                @Override
//                public int compare(CheckOrder lhs, CheckOrder rhs) {
//                    return lhs.getTaskStatus().compareTo(rhs.getTaskStatus());
//                }
//            });
            lv.setSelection(0);
        }
        if (newList != null) {
            newList.clear();
        }
        ArrayList<SectionBean> data = SectionBean.getData(listData);
        newList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        should = SPUtils.getString(this, CheckConstants.SHOULD);
        done = SPUtils.getString(this, CheckConstants.DONE);
        over = SPUtils.getString(this, CheckConstants.OVER);
        overFinish = SPUtils.getString(this, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(this, CheckConstants.CANCEL);
//        lv = (XListView) findViewById(R.id.lv_check);
        lv = (PinnedSectionRefreshListView) findViewById(R.id.lv_check_new);
        adapter = new NewCheckAdapter(newList, this, "");
        lv.setAdapter(adapter);
        lv.setPullLoadEnable(false);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(listener);
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setEmptyView(findViewById(R.id.tv_empty));
        ll_date_range = (LinearLayout) findViewById(R.id.ll_date_range);
        ll_date_range.setOnClickListener(this);
        ll_condition = (LinearLayout) findViewById(R.id.ll_condition);
        ll_condition.setOnClickListener(this);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        ll_sort.setOnClickListener(this);
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        initAnimation();
        tv_data_range = (TextView) findViewById(R.id.tv_data_range);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        tv_condition.setText("全部类别");
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        tv_sort.setText("全部状态");
        iv_triangle_range = (ImageView) findViewById(R.id.iv_triangle_range);
        iv_triangle_condition = (ImageView) findViewById(R.id.iv_triangle_condition);
        iv_triangle_sort = (ImageView) findViewById(R.id.iv_triangle_sort);
        LinearLayout ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_search.setVisibility(View.VISIBLE);
        ll_search.setOnClickListener(this);
        rl_no_net = (RelativeLayout) findViewById(R.id.rl_no_net);
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT);
    }

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(TaskCheckActivity.this, CheckDetailActivity.class);
            CheckOrder order = newList.get(position - 1).getCheckOrder();
            if (order.getDeviceName() == null)
                return;
            intent.putExtra(Constants.DATA, (Serializable) order);
            startActivity(intent);
        }
    };

    private PinnedSectionRefreshListView.IXListViewListener listener = new PinnedSectionRefreshListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            callRefresh();
            lv.setRefreshTime(CommonUtils.getCurrentTime());
            lv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
//            mCurrentIndex = adapter.getCount();
//            callLoadMore();
//            lv.stopLoadMore();
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
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 2000);
            }
            NewCheckRequest newCheckRequest = new NewCheckRequest(paramNames, paramValues, paramConditons, paramTypes, ROWS, 0);
            sendRequest(newCheckRequest, NewCheckOrderResponse.class);
            currentPage--;
        } else {
            getOutlineData();
        }
    }

    private void callLoadMore() {
//        boolean isNetWork = Utils.checkNetWork(this);
//        if (isNetWork) {
//            currentPage++;
//            CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, "", currentPage + "");
//            sendRequest(checkOrderListRequest, NewCheckOrderResponse.class);
//            currentPage--;
//        } else {
//            getOutlineData();
//        }
    }

    private void showPopupWindow(View view, int count, final List<String> items) {
        final PopupWindowUtil popupWindow = new PopupWindowUtil(this, items);
        final View curView = view;
        if (curView.getId() == ll_date_range.getId()) {
            for (String item : items
                    ) {
                if (item.equals(tv_data_range.getText().toString().trim())) {
                    popupWindow.setCheck(item);
                    popupWindow.notifyAdapter();
                }
            }
        } else if (curView.getId() == ll_condition.getId()) {
            for (String item : items
                    ) {
                if (item.equals(tv_condition.getText().toString().trim())) {
                    popupWindow.setCheck(item);
                    popupWindow.notifyAdapter();
                }
            }
        } else if (curView.getId() == ll_sort.getId()) {
            for (String item : items
                    ) {
                if (item.equals(tv_sort.getText().toString().trim())) {
                    popupWindow.setCheck(item);
                    popupWindow.notifyAdapter();
                }
            }
        }
        popupWindow.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (curView.getId() == ll_date_range.getId()) {
                    tv_data_range.setText(items.get(position));
                } else if (curView.getId() == ll_condition.getId()) {
                    tv_condition.setText(items.get(position));
//                    if ("全部类别".equals(items.get(position))) {
//                        paramNames.add(0,);
//                    } else if ("水".equals(items.get(position))) {
//                        currentCondition = "水";
//                    } else if ("电".equals(items.get(position))) {
//                        currentCondition = "电";
//                    } else if ("气".equals(items.get(position))) {
//                        currentCondition = "气";
//                    } else if ("其他".equals(items.get(position))) {
//                        currentCondition = "其他";
//                    }
                } else if (curView.getId() == ll_sort.getId()) {
                    tv_sort.setText(items.get(position));
                    if ("全部状态".equals(items.get(position))) {
                        paramValues.remove(2);
                        paramValues.add(2, should + "," + over);
                    } else if ("待巡检".equals(items.get(position))) {
                        paramValues.remove(2);
                        paramValues.add(2, should);
                    } else if ("已超时".equals(items.get(position))) {
                        paramValues.remove(2);
                        paramValues.add(2, over);
                    }
                }
                boolean isNetWork = Utils.checkNetWork(TaskCheckActivity.this);
                if (isNetWork) {
                    if (operatingAnim != null) {
                        rl_info.setVisibility(View.VISIBLE);
                        infoOperating.startAnimation(operatingAnim);
                        handler.sendEmptyMessageDelayed(Constants.STOP, 10000);
                    }
                    lv.setSelection(0);
                    NewCheckRequest newCheckRequest = new NewCheckRequest(paramNames, paramValues, paramConditons, paramTypes, "-1", 0);
                    sendRequest(newCheckRequest, NewCheckOrderResponse.class);
                    currentPage--;
                } else {
                    CommonUtils.showToast(TaskCheckActivity.this, getString(R.string.no_net));
                }
                popupWindow.dismiss();

            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_data_range.setTextColor(getResources().getColor(R.color.black));
                tv_condition.setTextColor(getResources().getColor(R.color.black));
                tv_sort.setTextColor(getResources().getColor(R.color.black));
                iv_triangle_range.setRotation(0f);
                iv_triangle_condition.setRotation(0f);
                iv_triangle_sort.setRotation(0f);
            }
        });
        popupWindow.show(view, count);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof NewCheckRequest) {
            NewCheckOrderResponse checkOrderListResponse = (NewCheckOrderResponse) response;
            if (checkOrderListResponse.getData() != null) {
                final List<NewCheckOrderResponse.DataEntity.ListdataEntity> data = checkOrderListResponse.getData().getListdata();
                currentPage++;
                if (currentPage == 1) {
                }
                if (listData != null) {
                    listData.clear();
                }
//                if (currentPage == checkOrderListResponse.getData().getCurrentpage()) {
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
//                }
            }
        } else if (request instanceof ParamCommitRequest) {
            ParamCommitResponse paramCommitResponse = (ParamCommitResponse) response;
            if (paramCommitResponse != null) {
                if ("0".equals(paramCommitResponse.getResult())) {
                    int taskId = paramCommitResponse.getId();
                    try {
                        final CheckCommitDb checkCommitDb = db.findById(CheckCommitDb.class, taskId);
                        checkCommitDb.setHasCommit(2);
                        final CheckOrderDb orderDb = db.findById(CheckOrderDb.class, taskId);
                        orderDb.setTaskStatus(done);
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(checkCommitDb);
                                    db.saveOrUpdate(orderDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    sendBroadcast();
                } else {
                }
            }
        }
    }

    private void sendBroadcast() {
        Intent intent = new Intent(Constants.PARAM_COMMIT_OK);
        sendBroadcast(intent);
    }

    private int position = 0;

    private void getOrderData(final List<NewCheckOrderResponse.DataEntity.ListdataEntity> data) {
        for (int i = data.size(); i > 0; i--) {
            NewCheckOrderResponse.DataEntity.ListdataEntity task = data.get(i - 1);
            NewCheckOrderResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
            int taskId = task.getDeviceTaskResult().getId();
            CheckOrder order = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName(), entity.getInspectorName(), entity.getArrageId(),
                    entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
            listData.add(order);
        }
        Collections.sort(listData, new Comparator<CheckOrder>() {
            @Override
            public int compare(CheckOrder lhs, CheckOrder rhs) {
                return lhs.getPlanTime().compareTo(rhs.getPlanTime());
            }
        });
        if (newList != null) {
            newList.clear();
        }
        ArrayList<SectionBean> list = SectionBean.getData(listData);
        newList.addAll(list);
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            SectionBean sectionBean = list.get(i);
            String planTime = sectionBean.getCheckOrder().getPlanTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long planTimeMillis = 0;
            if (planTime != null) {
                try {
                    planTime = planTime.replace("T", " ");
                    planTimeMillis = format.parse(planTime).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if ((currentTimeMillis - 30 * 60 * 1000) < planTimeMillis) {
                position = i;
                break;
            }
        }

//        if (CheckConstants.ALL_CHECK.equals(type)) {
//        Collections.sort(listData, new Comparator<CheckOrder>() {
//            @Override
//            public int compare(CheckOrder lhs, CheckOrder rhs) {
//                if (lhs.getTaskStatus().equals("2") && rhs.getTaskStatus().equals("3")) {
//                    return rhs.getTaskStatus().compareTo(lhs.getTaskStatus());
//                }
//                if (lhs.getTaskStatus().equals(rhs.getTaskStatus())) {
//                    String planTime = lhs.getPlanTime();
//                    String planDate = planTime.substring(0, planTime.lastIndexOf("T"));
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                    String currentDate = format.format(new Date());
//                    if (planDate.equals(currentDate)) {
//                        return lhs.getPlanTime().compareTo(rhs.getPlanTime());
//                    }
//                    return rhs.getPlanTime().compareTo(lhs.getPlanTime());
//                }
//                return lhs.getTaskStatus().compareTo(rhs.getTaskStatus());
//            }
//        });
//        } else {
//        }
//        lv.setSelection(mCurrentIndex);
//        lv.smoothScrollToPosition(mCurrentIndex);
        handler.sendEmptyMessage(Constants.STOP);
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
            ParamDb haveSaveDb = db.findById(ParamDb.class, taskId);
            if (haveSaveDb == null) {
                db.saveOrUpdate(paramDb);
            }
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
            case R.id.ll_date_range:
                List<String> dates = new ArrayList<>();
                dates.add("全部日期");
                dates.add("今日");
                dates.add("本周");
                dates.add("本月");
                tv_data_range.setTextColor(getResources().getColor(R.color.orange));
                iv_triangle_range.setRotation(180f);
                showPopupWindow(ll_date_range, 3, dates);
                break;
            case R.id.ll_condition:
                List<String> conditions = new ArrayList<>();
                conditions.add("全部类别");
                conditions.add("配电");
                conditions.add("空调");
                conditions.add("锅炉");
                conditions.add("医气");
                conditions.add("污水");
                tv_condition.setTextColor(getResources().getColor(R.color.orange));
                iv_triangle_condition.setRotation(180f);
                showPopupWindow(ll_condition, 3, conditions);
                break;
            case R.id.ll_sort:
                List<String> sorts = new ArrayList<>();
                sorts.add("全部状态");
                sorts.add("待巡检");
                sorts.add("已超时");
                tv_sort.setTextColor(getResources().getColor(R.color.orange));
                iv_triangle_sort.setRotation(180f);
                showPopupWindow(ll_sort, 3, sorts);
                break;
            case R.id.ll_search:
                go2Search();
                break;
        }
    }

    private void go2Search() {
        Intent intent = new Intent(this, CheckSearchActivity.class);
        startActivity(intent);
    }
}
