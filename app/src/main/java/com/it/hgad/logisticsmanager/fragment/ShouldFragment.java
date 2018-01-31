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
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.SearchRequest;
import com.it.hgad.logisticsmanager.bean.response.OrderListResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
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
public class ShouldFragment extends BaseFragment {

    public static final String TODAY = "Today";
    public static final String THIS_WEEK = "ThisWeek";
    public static final String THIS_MONTH = "ThisMonth";
    public static final String ALL_DATE = "全部日期";
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
//                    lv.setSelection(mCurrentIndex);
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
    private String currentDate = "";
    private String currentCondition = "";
    private String currentType = "";
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
        currentType = shouldRecieve + "," + shouldStart + "," + shouldDo;
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
            SearchRequest orderListRequest = new SearchRequest(currentType, userId, "", "", currentPage, currentDate, currentCondition);
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
        maintainAdapter = new MaintainAdapter(listData, mContext);
        lv.setAdapter(maintainAdapter);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setXListViewListener(mIXListViewListener);
        lv.setEmptyView(mView.findViewById(R.id.tv_empty));
        initAnimation();
        ll_date_range = (LinearLayout) mView.findViewById(R.id.ll_date_range);
        ll_date_range.setOnClickListener(this);
        ll_condition = (LinearLayout) mView.findViewById(R.id.ll_condition);
        ll_condition.setOnClickListener(this);
        ll_sort = (LinearLayout) mView.findViewById(R.id.ll_sort);
        ll_sort.setOnClickListener(this);
        tv_data_range = (TextView) mView.findViewById(R.id.tv_data_range);
        tv_data_range.setText("全部日期");
        tv_condition = (TextView) mView.findViewById(R.id.tv_condition);
        tv_condition.setText("全部类别");
        tv_sort = (TextView) mView.findViewById(R.id.tv_sort);
        tv_sort.setText("全部状态");
        iv_triangle_range = (ImageView) mView.findViewById(R.id.iv_triangle_range);
        iv_triangle_condition = (ImageView) mView.findViewById(R.id.iv_triangle_condition);
        iv_triangle_sort = (ImageView) mView.findViewById(R.id.iv_triangle_sort);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_should, null);
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
                        if (userId == orderDb.getUserId() && (shouldDo.equals(orderDb.getRepairFlag() + "") || shouldRecieve.equals(orderDb.getRepairFlag() + "") || shouldStart.equals(orderDb.getRepairFlag() + ""))) {
//                            if (RepairConstants.ALL_REPAIR.equals(type)) {
                            Order order = new Order();
                            order.setData(orderDb);
                            listData.add(order);
                        } else {
//                                addToData(orderDb);
//                            }
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
            lv.setSelection(0);
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

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mContext, JobDetailActivity.class);
            Order order = listData.get(position - 1);
            intent.putExtra(Constants.DATA, (Serializable) order);
            mContext.startActivity(intent);
        }
    };

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
            mCurrentIndex = maintainAdapter.getCount();
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
            if (listData.size() != 0) {
                lv.setSelection(mCurrentIndex);
            }
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 2000);
            }
            SearchRequest orderListRequest = new SearchRequest(currentType, userId, "", "", currentPage, currentDate, currentCondition);
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
            SearchRequest orderListRequest = new SearchRequest(currentType, userId, "", "", currentPage, currentDate, currentCondition);
            sendRequest(orderListRequest, OrderListResponse.class);
            currentPage--;
        } else {
            getOutlineData();
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof SearchRequest) {
            OrderListResponse orderListResponse = ((OrderListResponse) response);
            if (orderListResponse.getData() != null) {
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
//                            sendRequest(request, StartRepairResponse.class);
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
            OrderDb orderDb = new OrderDb();
            orderDb.setData(order);
            orderDb.setUserId(userId);
            try {
                List<CommitDb> all = db.findAll(CommitDb.class);
                if (all != null && all.size() != 0) {
                    for (CommitDb commitDb : all
                            ) {
                        if (commitDb.getRepairId() == order.getId() && commitDb.getHasCommit() == 0) {
                            orderDb.setRepairFlag(Integer.parseInt(shouldDo));
                        }
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            db.saveOrUpdate(orderDb);
        }
    }

//    private OrderDb getOrderDbEntity(Order entity) {
//        int id = entity.getId();
//        String repairReply = entity.getRepairReply();
//        String repairMan = entity.getRepairMan();
//        String finishTime = entity.getFinishTime();
//        String repairTel = entity.getRepairTel();
//        String registerTime = entity.getRegisterTime();
//        String repairType = entity.getRepairType();
//        String repairNo = entity.getRepairNo();
//        String shelveTime = entity.getShelveTime();
//        String registerMan = entity.getRegisterMan();
//        int repairFlag = entity.getRepairFlag();
//        String repairContent = entity.getRepairContent();
//        String sameCondition = entity.getSameCondition();
//        String shelveReason = entity.getShelveReason();
//        String userIds = entity.getUserIds();
//        String userNames = entity.getUserNames();
//        String repairDept = entity.getRepairDept();
//        String repairImg = entity.getRepairImg();
//        String spotImg = entity.getSpotImg();
//        String repairSrc = entity.getRepairSrc();
//        String repairLoc = entity.getRepairLoc();
//        OrderDb orderDb = new OrderDb(id, userId, repairReply, repairMan, finishTime, repairTel, registerTime, repairType,
//                repairNo, shelveTime, registerMan, repairFlag, repairContent, sameCondition, shelveReason, userIds,
//                userNames, repairDept, repairImg, spotImg, repairSrc, repairLoc);
//        return orderDb;
//    }

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
                conditions.add("水");
                conditions.add("电");
                conditions.add("气");
                conditions.add("门窗");
                tv_condition.setTextColor(getResources().getColor(R.color.orange));
                iv_triangle_condition.setRotation(180f);
                showPopupWindow(ll_condition, 3, conditions);
                break;
            case R.id.ll_sort:
                List<String> sorts = new ArrayList<>();
                sorts.add("全部状态");
                sorts.add("待接收");
                sorts.add("待开始");
                sorts.add("待维修");
                tv_sort.setTextColor(getResources().getColor(R.color.orange));
                iv_triangle_sort.setRotation(180f);
                showPopupWindow(ll_sort, 3, sorts);
                break;
        }
    }


    private void showPopupWindow(View view, int count, final List<String> items) {
        final PopupWindowUtil popupWindow = new PopupWindowUtil(mContext, items);
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
                    if (ALL_DATE.equals(items.get(position))) {
                        currentDate = "";
                    } else if ("今日".equals(items.get(position))) {
                        currentDate = TODAY;
                    } else if ("本周".equals(items.get(position))) {
                        currentDate = THIS_WEEK;
                    } else if ("本月".equals(items.get(position))) {
                        currentDate = THIS_MONTH;
                    }
                } else if (curView.getId() == ll_condition.getId()) {
                    tv_condition.setText(items.get(position));
                    if ("全部类别".equals(items.get(position))) {
                        currentCondition = "";
                    } else if ("水".equals(items.get(position))) {
                        currentCondition = "水";
                    } else if ("电".equals(items.get(position))) {
                        currentCondition = "电";
                    } else if ("气".equals(items.get(position))) {
                        currentCondition = "气";
                    } else if ("门窗".equals(items.get(position))) {
                        currentCondition = "门窗";
                    }
                } else if (curView.getId() == ll_sort.getId()) {
                    tv_sort.setText(items.get(position));
                    if ("全部状态".equals(items.get(position))) {
                        currentType = shouldRecieve + "," + shouldStart + "," + shouldDo;
                    } else if ("待接收".equals(items.get(position))) {
                        currentType = shouldRecieve;
                    } else if ("待开始".equals(items.get(position))) {
                        currentType = shouldStart;
                    } else if ("待维修".equals(items.get(position))) {
                        currentType = shouldDo;
                    }
                }
                popupWindow.dismiss();
                currentPage = 1;
                try {
                    String condition = URLEncoder.encode(ShouldFragment.this.currentCondition, "UTF-8");

                    boolean isNetWork = Utils.checkNetWork(mContext);
                    if (isNetWork) {
                        if (operatingAnim != null) {
                            rl_info.setVisibility(View.VISIBLE);
                            infoOperating.startAnimation(operatingAnim);
                            handler.sendEmptyMessageDelayed(Constants.STOP, 2000);
                        }
                        SearchRequest orderListRequest = new SearchRequest(currentType, userId, "", "", currentPage, currentDate, condition);
                        sendRequest(orderListRequest, OrderListResponse.class);
                        currentPage--;
                    } else {
                        CommonUtils.showToast(mContext, getString(R.string.no_net));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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
