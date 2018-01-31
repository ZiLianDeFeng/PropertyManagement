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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.MaintainAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.SearchRequest;
import com.it.hgad.logisticsmanager.bean.response.OrderListResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.PopupWindowUtil;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/4/11.
 */
public class TaskMaintainActivity extends CommonActivity {

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
                    maintainAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ImageView infoOperating;
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
    private String repairFlag;
    private String repairNo;
    private String repairTel;
    private String repairTaskType;
    private String repairEventType;
    private RelativeLayout rl_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_maintain);
        initHeader(getString(R.string.maintenance_manage));
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        Intent intent = getIntent();
        repairFlag = intent.getStringExtra(Constants.REPAIR_FLAG);
        repairNo = intent.getStringExtra(Constants.REPAIR_NO);
        repairTel = intent.getStringExtra(Constants.REPAIR_TEL);
//        repairTaskType = intent.getStringExtra(Constants.REPAIR_TASK_TYPE);
        repairEventType = intent.getStringExtra(Constants.REPAIR_EVENT_TYPE);
        try {
            repairEventType = URLEncoder.encode(TaskMaintainActivity.this.repairEventType, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        registerReceiver();
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(OrderDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        boolean isNetWork = Utils.checkNetWork(this);
        if (!isNetWork) {
//            getOutlineData();
//            lv.setPullRefreshEnable(false);
//            lv.setPullLoadEnable(false);
        } else {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 5000);
            }
            SearchRequest searchRequest = new SearchRequest(repairFlag, userId, repairNo, repairTel, currentPage, "", repairEventType);
            sendRequest(searchRequest, OrderListResponse.class);
            currentPage--;
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.COMMIT_OK);
        intentFilter.addAction(Constants.SHELVE_OK);
        intentFilter.addAction(Constants.CANCEL_SHELVE);
        registerReceiver(receiver, intentFilter);
    }

    private void getOutlineData() {

    }

    private void initView() {
        shouldRecieve = SPUtils.getString(this, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(this, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(this, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(this, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(this, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(this, RepairConstants.HAVE_FINISH);
        lv = (XListView) findViewById(R.id.lv_maintain);
        maintainAdapter = new MaintainAdapter(listData, this);
        lv.setAdapter(maintainAdapter);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setXListViewListener(mIXListViewListener);
        lv.setEmptyView(findViewById(R.id.tv_empty));
        initAnimation();
        LinearLayout ll_title = (LinearLayout) findViewById(R.id.ll_title);
        ll_title.setVisibility(View.GONE);
        ll_date_range = (LinearLayout) findViewById(R.id.ll_date_range);
        ll_date_range.setOnClickListener(this);
        ll_condition = (LinearLayout) findViewById(R.id.ll_condition);
        ll_condition.setOnClickListener(this);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        ll_sort.setOnClickListener(this);
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

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(TaskMaintainActivity.this, JobDetailActivity.class);
            Order order = listData.get(position - 1);
            intent.putExtra(Constants.DATA, (Serializable) order);
            startActivity(intent);
        }
    };

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private int mCurrentIndex = 0;
    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
//            callRefresh();
            lv.setRefreshTime(CommonUtils.getCurrentTime());
            lv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            mCurrentIndex = maintainAdapter.getCount();
            callLoadMore();
            lv.stopLoadMore();
        }
    };

    private void callRefresh() {
        mCurrentIndex = 0;
        if (listData != null) {
            listData.clear();
        }
        boolean isNetWork = Utils.checkNetWork(this);
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
            SearchRequest searchRequest = new SearchRequest(repairFlag, userId, repairNo, repairTel, currentPage, "", "");
            sendRequest(searchRequest, OrderListResponse.class);
            currentPage--;
        } else {
//            getOutlineData();
        }
    }

    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(this);
        if (isNetWork) {
            currentPage++;
            SearchRequest searchRequest = new SearchRequest(repairFlag, userId, repairNo, repairTel, currentPage, "", "");
            sendRequest(searchRequest, OrderListResponse.class);
            currentPage--;
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof SearchRequest) {
            OrderListResponse orderListResponse = (OrderListResponse) response;
            if (orderListResponse.getData() != null) {
                lv.setPullLoadEnable(true);
                lv.setPullRefreshEnable(false);
                final List<OrderListResponse.DataEntity.ListdataEntity> data = orderListResponse.getData().getListdata();
                currentPage++;
                if (currentPage == orderListResponse.getData().getCurrentpage()) {
                    for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
                        Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                                entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
                        listData.add(order);
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
            }
            handler.sendEmptyMessageDelayed(Constants.STOP, 500);
        }
    }

    private void saveToDb(List<OrderListResponse.DataEntity.ListdataEntity> data) throws DbException {
        for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
            Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                    entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
            OrderDb orderDb = new OrderDb();
            orderDb.setData(order);
            orderDb.setUserId(userId);
//            try {
//                List<CommitDb> all = db.findAll(CommitDb.class);
//                if (all != null && all.size() != 0) {
//                    for (CommitDb commitDb : all
//                            ) {
//                        if (commitDb.getRepairId() == order.getId() && commitDb.getHasCommit() == 0) {
//                            orderDb.setRepairFlag(Integer.parseInt(shouldDo));
//                        }
//                    }
//                }
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
            db.saveOrUpdate(orderDb);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ll_date_range:
//                List<String> dates = new ArrayList<>();
//                dates.add("按今日");
//                dates.add("按本周");
//                dates.add("按本月");
//                tv_data_range.setTextColor(getResources().getColor(R.color.orange));
//                iv_triangle_range.setRotation(180f);
//                showPopupWindow(ll_date_range, 3, dates);
//                break;
//            case R.id.ll_condition:
//                List<String> conditions = new ArrayList<>();
//                conditions.add("按姓名");
//                conditions.add("按电话");
//                conditions.add("按科室");
//                tv_condition.setTextColor(getResources().getColor(R.color.orange));
//                iv_triangle_condition.setRotation(180f);
//                showPopupWindow(ll_condition, 3, conditions);
//                break;
//            case R.id.ll_sort:
//                List<String> sorts = new ArrayList<>();
//                sorts.add("按时间");
//                sorts.add("按状态");
//                tv_sort.setTextColor(getResources().getColor(R.color.orange));
//                iv_triangle_sort.setRotation(180f);
//                showPopupWindow(ll_sort, 3, sorts);
//                break;
//            case R.id.ll_search:
//                go2Search();
//                break;
        }

    }

    private void go2Search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
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
                } else if (curView.getId() == ll_sort.getId()) {
                    tv_sort.setText(items.get(position));
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
}
