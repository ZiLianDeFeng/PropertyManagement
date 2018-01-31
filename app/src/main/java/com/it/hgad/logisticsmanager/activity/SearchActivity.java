package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/1/5.
 */
public class SearchActivity extends CommonActivity {
    private static final int SEARCH_ORDER = 330;
    private Button btn_search;
    private TextView et_state;
    private PopupWindow popupWindow;
    private TextView sort_all;
    private TextView sort_need;
    private TextView sort_up;
    private TextView sort_done;
    private TextView sort_finish;
    private XListView mLv;
    private List<Order> listData = new ArrayList<>();
    private EditText et_orderNo;
    private EditText et_tel;
    private EditText et_registName;
    private String state;
    private String repairNo;
    private String tel;
    //    private String registName;
    private String repairFlag;
    private int id;
    private MaintainAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.COMMIT_OK)) {
                search();
            }
        }
    };
    private View view_state;
    private TextView sort_recieve;
    private TextView sort_start;
    private DbUtils db;
    private ImageView infoOperating;
    private Animation operatingAnim;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    infoOperating.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    adapter.notifyDataSetChanged();
                    if (afterLoadMore) {
                        mLv.setSelection(mCurrentIndex);
                    } else {
                        mLv.setSelection(0);
                    }
                    break;
            }
        }
    };
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initHeader("报修查询");
        initView();
        initData();
    }

    private void initView() {
        id = SPUtils.getInt(SearchActivity.this, SPConstants.USER_ID);
        shouldRecieve = SPUtils.getString(SearchActivity.this, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(SearchActivity.this, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(SearchActivity.this, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(SearchActivity.this, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(SearchActivity.this, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(SearchActivity.this, RepairConstants.HAVE_FINISH);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        et_state = (TextView) findViewById(R.id.et_state);
        et_orderNo = (EditText) findViewById(R.id.et_orderNo);
        et_tel = (EditText) findViewById(R.id.et_tel);
        view_state = (View) findViewById(R.id.view_state);
//        et_registName = (EditText) findViewById(R.id.et_registName);
        et_state.setOnClickListener(this);
        mLv = (XListView) findViewById(R.id.lv_search);
        adapter = new MaintainAdapter(listData, SearchActivity.this);
        mLv.setAdapter(adapter);
        mLv.setXListViewListener(mIXListViewListener);
        mLv.setOnItemClickListener(mOnItemClickListener);
//        mLv.setOnItemLongClickListener(mOnItemLongClickListener);
        mLv.setPullLoadEnable(false);
        mLv.setPullRefreshEnable(false);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(SearchActivity.this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

    }

    private boolean afterLoadMore;
    private int currentPage = 1;
    private int mCurrentIndex = 0;
    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            mLv.setRefreshTime(CommonUtils.getCurrentTime());
            mLv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            currentPage = currentPage + 1;
            mCurrentIndex = adapter.getCount();
            SearchRequest searchRequest = new SearchRequest(repairFlag, id, repairNo, tel, currentPage,"","");
            sendRequest(searchRequest, OrderListResponse.class);
            mLv.stopLoadMore();
//            adapter.notifyDataSetChanged();
            afterLoadMore = true;
        }
    };
//    private XListView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
//        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//            LayoutInflater factory = LayoutInflater.from(SearchActivity.this);//提示框
//            final View dialogView = factory.inflate(R.layout.editbox_layout, null);//这里必须是final的
//            final EditText edit = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
//
//            if (shouldStart.equals(listData.get(position - 1).getRepairFlag() + "")) {
//                new AlertDialog.Builder(SearchActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
//                        .setTitle("挂起理由")//提示框标题
//                        .setView(dialogView)
//                        .setPositiveButton("确定",//提示框的两个按钮
//                                new android.content.DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        //事件
//                                        String reason = edit.getText().toString().trim();
//                                        if (reason != null) {
//                                            int repairId = listData.get(position - 1).getId();
//                                            ShelveRequest shelveRequest = new ShelveRequest(repairId + "", reason);
//                                            sendRequest(shelveRequest, ShelveResponse.class);
//                                        } else {
//                                            CommonUtils.showToast(SearchActivity.this, "请填写理由");
//                                        }
//
//                                    }
//                                }).setNegativeButton("取消", null).create().show();
//            } else if (haveShelve.equals(listData.get(position - 1).getRepairFlag() + "")) {
//                new AlertDialog.Builder(SearchActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
//                        .setTitle("确认取消挂起")//提示框标题
//                        .setPositiveButton("确定",//提示框的两个按钮
//                                new android.content.DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        //事件
//                                        int repairId = listData.get(position - 1).getId();
//                                        CancelShelveRequest cancelShelveRequest = new CancelShelveRequest(repairId + "");
//                                        sendRequest(cancelShelveRequest, CancelShelveResponse.class);
//                                    }
//                                }).setNegativeButton("取消", null).create().show();
//            }
//            return true;
//        }
//    };

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listData != null) {
                Intent intent = new Intent(SearchActivity.this, JobDetailActivity.class);
                Order listdataEntity = listData.get(position - 1);
                intent.putExtra("data", (Serializable) listdataEntity);
                SearchActivity.this.startActivity(intent);
            }
        }
    };

    private void initData() {
        if (db == null) {
            db = LocalApp.getDb();
        }
        registBroadcastReceiver();
    }

    private void registBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.COMMIT_OK);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                search();
                break;
            case R.id.et_state:
//                initPopupWindow();
//                popupWindow.showAsDropDown(et_state);
                chooseState();
                break;
        }
    }
    private String[] states = {"全部", "待接收", "待开始", "待维修", "已挂起", "已维修", "结单"};

    private void chooseState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("工单状态");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(states, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_state.setText(states[which]);
            }
        });
        builder.show();
    }
    private void search() {
        afterLoadMore = false;
        listData.clear();
        adapter.notifyDataSetChanged();

        currentPage = 1;
        state = et_state.getText().toString().trim();
        repairFlag = getStateInt(state);
        repairNo = et_orderNo.getText().toString().trim();
        tel = et_tel.getText().toString().trim();
        boolean isNetWork = Utils.checkNetWork(SearchActivity.this);
        if (!isNetWork) {
            // TODO: 2017/4/20 未区分用户，需根据userId获取数据
//            List<OrderDb> orderDbList = null;
//            try {
//                if ("".equals(repairFlag)) {
//                    if (TextUtils.isEmpty(tel) & TextUtils.isEmpty(repairNo)) {
//                        orderDbList = db.findAll(OrderDb.class);
//                    } else if (TextUtils.isEmpty(tel)) {
//                        orderDbList = db.findAll(Selector.from(OrderDb.class).where(WhereBuilder.b(OrderDb.REPAIR_NO, "like", "%" + repairNo + "%")));
//                    } else if (TextUtils.isEmpty(repairNo)) {
//                        orderDbList = db.findAll(Selector.from(OrderDb.class).where(WhereBuilder.b(OrderDb.REPAIR_TEL, "like", "%" + tel + "%")));
//                    } else {
//                        orderDbList = db.findAll(Selector.from(OrderDb.class).where(WhereBuilder.b(OrderDb.REPAIR_NO, "like", "%" + repairNo + "%").and(OrderDb.REPAIR_TEL, "like", "%" + tel + "%")));
//                    }
//                } else {
//                    if (TextUtils.isEmpty(tel) & TextUtils.isEmpty(repairNo)) {
//                        orderDbList = db.findAll(Selector.from(OrderDb.class).where(OrderDb.REPAIR_FLAG, "==", repairFlag));
//                    } else if (TextUtils.isEmpty(tel)) {
//                        orderDbList = db.findAll(Selector.from(OrderDb.class).where(WhereBuilder.b(OrderDb.REPAIR_NO, "like", "%" + repairNo + "%").and(OrderDb.REPAIR_FLAG, "==", repairFlag)));
//                    } else if (TextUtils.isEmpty(repairNo)) {
//                        orderDbList = db.findAll(Selector.from(OrderDb.class).where(WhereBuilder.b(OrderDb.REPAIR_TEL, "like", "%" + tel + "%").and(OrderDb.REPAIR_FLAG, "==", repairFlag)));
//                    } else {
//                        orderDbList = db.findAll(Selector.from(OrderDb.class).where(WhereBuilder.b(OrderDb.REPAIR_NO, "like", "%" + repairNo + "%").and(OrderDb.REPAIR_TEL, "like", "%" + tel + "%").and(OrderDb.REPAIR_FLAG, "==", repairFlag)));
//                    }
//                }
////                infoOperating.setVisibility(View.INVISIBLE);
////                infoOperating.clearAnimation();
////                adapter.notifyDataSetChanged();
//                getOrderData(orderDbList);
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
        } else {
            SearchRequest searchRequest = new SearchRequest(repairFlag, id, repairNo, tel, currentPage,"","");
            sendRequest(searchRequest, OrderListResponse.class);
            if (operatingAnim != null) {
                infoOperating.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
            }
        }
    }

    private void getOrderData(List<OrderDb> orderDbList) {
        for (OrderDb orderDb :
                orderDbList) {
            Order order = new Order();
            order.setData(orderDb);
            listData.add(order);
        }
//        adapter.notifyDataSetChanged();
    }

    private void initPopupWindow() {
        View contentView = View.inflate(SearchActivity.this, R.layout.popup_sort, null);
        popupWindow = new PopupWindow(contentView, et_state.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff"))); //设置背景
        popupWindow.setFocusable(true); //设置获取焦点
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);
        sort_all = (TextView) contentView.findViewById(R.id.sort_all);
        sort_all.setOnClickListener(this);
        sort_recieve = (TextView) contentView.findViewById(R.id.sort_recieve);
        sort_recieve.setOnClickListener(this);
        sort_start = (TextView) contentView.findViewById(R.id.sort_start);
        sort_start.setOnClickListener(this);
        sort_need = (TextView) contentView.findViewById(R.id.sort_need);
        sort_need.setOnClickListener(this);
        sort_up = (TextView) contentView.findViewById(R.id.sort_up);
        sort_up.setOnClickListener(this);
        sort_done = (TextView) contentView.findViewById(R.id.sort_done);
        sort_done.setOnClickListener(this);
        sort_finish = (TextView) contentView.findViewById(R.id.sort_finish);
        sort_finish.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof SearchRequest) {
            OrderListResponse mOrderListResponse = (OrderListResponse) response;
            if (mOrderListResponse.getData() != null) {
                mLv.setPullLoadEnable(true);
                mLv.setPullRefreshEnable(false);
                List<OrderListResponse.DataEntity.ListdataEntity> data = mOrderListResponse.getData().getListdata();
                if (currentPage == mOrderListResponse.getData().getCurrentpage()) {
                    for (OrderListResponse.DataEntity.ListdataEntity entity : data) {
                        Order order = new Order(entity.getRepairReply(), entity.getRepairMan(), entity.getFinishTime(), entity.getRepairTel(), entity.getRegisterTime(), entity.getRepairType(), entity.getRepairNo(), entity.getShelveTime(), entity.getRegisterMan(), entity.getRepairFlag(), entity.getRepairContent(), entity.getSameCondition(),
                                entity.getShelveReason(), entity.getUserIds(), entity.getUserNames(), entity.getRepairDept(), entity.getRepairImg(), entity.getSpotImg(), entity.getId(), entity.getRepairSrc(), entity.getRepairLoc());
                        listData.add(order);
                    }
                }
            } else {
                mLv.setPullLoadEnable(false);
                mLv.setPullRefreshEnable(false);
            }
            handler.sendEmptyMessageDelayed(Constants.STOP, 500);
        }
    }

    public String getStateInt(String state) {
        String s = RepairConstants.ALL_REPAIR;
        if (RepairConstants.ALL.equals(state)) {
            s = RepairConstants.ALL_REPAIR;
        } else if (RepairConstants.SHOULD_RECIEVE.equals(state)) {
            s = shouldRecieve;
        } else if (RepairConstants.SHOULD_START.equals(state)) {
            s = shouldStart;
        } else if (RepairConstants.SHOULD_DO.equals(state)) {
            s = shouldDo;
        } else if (RepairConstants.HAVE_SHELVE.equals(state)) {
            s = haveShelve;
        } else if (RepairConstants.HAVE_DONE.equals(state)) {
            s = haveDone;
        } else if (RepairConstants.HAVE_FINISH.equals(state)) {
            s = haveFinish;
        }
        return s;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
