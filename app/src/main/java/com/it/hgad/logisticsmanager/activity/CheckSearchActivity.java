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
import com.it.hgad.logisticsmanager.adapter.CheckAdapter;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.request.CheckSearchRequest;
import com.it.hgad.logisticsmanager.bean.response.CheckOrderListResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/2/16.
 */
public class CheckSearchActivity extends CommonActivity {
    private Button btn_search;
    private TextView et_state;
    private PopupWindow popupWindow;
    private TextView sort_all;
    private TextView sort_need;
    private XListView mLv;
    private List<CheckOrder> listData = new ArrayList<>();
    private String state;
    private String checkFlag;
    private int id;
    private CheckAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.PARAM_COMMIT_OK)) {
                search();
            }
        }
    };
    private EditText et_device_name;
    private TextView sort_done;
    private TextView sort_over;
    private TextView sort_over_done;
    private String deviceName;
    private View view_state;
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
    private Animation operatingAnim;
    private ImageView infoOperating;
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_search);
        initHeader("巡检查询");
        initView();
        initData();
    }

    private void initView() {
        id = SPUtils.getInt(CheckSearchActivity.this, SPConstants.USER_ID);
        should = SPUtils.getString(CheckSearchActivity.this, CheckConstants.SHOULD);
        done = SPUtils.getString(CheckSearchActivity.this, CheckConstants.DONE);
        over = SPUtils.getString(CheckSearchActivity.this, CheckConstants.OVER);
        overFinish = SPUtils.getString(CheckSearchActivity.this, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(CheckSearchActivity.this, CheckConstants.CANCEL);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        et_state = (TextView) findViewById(R.id.et_state);
        et_device_name = (EditText) findViewById(R.id.et_device_name);
        view_state = (View) findViewById(R.id.view_state);
//        et_registName = (EditText) findViewById(R.id.et_registName);
        et_state.setOnClickListener(this);
        mLv = (XListView) findViewById(R.id.lv_search);
        adapter = new CheckAdapter(listData, CheckSearchActivity.this, "");
        mLv.setAdapter(adapter);
        mLv.setXListViewListener(mIXListViewListener);
        mLv.setOnItemClickListener(mOnItemClickListener);
        mLv.setPullLoadEnable(false);
        mLv.setPullRefreshEnable(false);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(CheckSearchActivity.this, R.anim.tip);
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
            afterLoadMore = false;
        }

        @Override
        public void onLoadMore() {
            currentPage = currentPage + 1;
            mCurrentIndex = adapter.getCount();
            CheckSearchRequest searchRequest = new CheckSearchRequest(checkFlag, id + "", deviceName, currentPage);
            sendRequest(searchRequest, CheckOrderListResponse.class);
            mLv.stopLoadMore();

//            adapter.notifyDataSetChanged();
            afterLoadMore = true;
        }
    };

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listData != null) {
                Intent intent = new Intent(CheckSearchActivity.this, CheckDetailActivity.class);
                CheckOrder listdataEntity = listData.get(position - 1);
                intent.putExtra("data", (Serializable) listdataEntity);
                CheckSearchActivity.this.startActivity(intent);
            }
        }
    };

    private void initData() {
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
//            case R.id.sort_all:
//                et_state.setText(sort_all.getText());
//                popupWindow.dismiss();
//                break;
//            case R.id.sort_need:
//                et_state.setText(sort_need.getText());
//                popupWindow.dismiss();
//                break;
//            case R.id.sort_done:
//                et_state.setText(sort_done.getText());
//                popupWindow.dismiss();
//                break;
//            case R.id.sort_over:
//                et_state.setText(sort_over.getText());
//                popupWindow.dismiss();
//                break;
//            case R.id.sort_over_done:
//                et_state.setText(sort_over_done.getText());
//                popupWindow.dismiss();
//                break;
//            default:
//                break;
        }
    }

    private String[] states = {"全部", "待巡检", "已完工", "已超时", "超时补录"};

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
        if (operatingAnim != null) {
            infoOperating.setVisibility(View.VISIBLE);
            infoOperating.startAnimation(operatingAnim);
        }
        currentPage = 1;
        state = et_state.getText().toString().trim();
        deviceName = et_device_name.getText().toString().trim();
        checkFlag = getStateInt(state);
        boolean isNetWork = Utils.checkNetWork(CheckSearchActivity.this);
        if (!isNetWork) {
            Utils.showTost(CheckSearchActivity.this, getString(R.string.no_net));
        } else {
            CheckSearchRequest checkSearchRequest = new CheckSearchRequest(checkFlag, id + "", deviceName, currentPage);
            sendRequest(checkSearchRequest, CheckOrderListResponse.class);
        }
    }

    private void initPopupWindow() {
        View contentView = View.inflate(CheckSearchActivity.this, R.layout.popup_search, null);
        popupWindow = new PopupWindow(contentView, et_state.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff"))); //设置背景
        popupWindow.setFocusable(true); //设置获取焦点
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);
        sort_all = (TextView) contentView.findViewById(R.id.sort_all);
        sort_all.setOnClickListener(this);
        sort_need = (TextView) contentView.findViewById(R.id.sort_need);
        sort_need.setOnClickListener(this);
        sort_done = (TextView) contentView.findViewById(R.id.sort_done);
        sort_done.setOnClickListener(this);
        sort_over = (TextView) contentView.findViewById(R.id.sort_over);
        sort_over.setOnClickListener(this);
        sort_over_done = (TextView) contentView.findViewById(R.id.sort_over_done);
        sort_over_done.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof CheckSearchRequest) {
            CheckOrderListResponse checkOrderListResponse = (CheckOrderListResponse) response;
            if (checkOrderListResponse.getData() != null) {
                mLv.setPullLoadEnable(true);
                mLv.setPullRefreshEnable(false);
                List<CheckOrderListResponse.DataEntity.ListdataEntity> data = checkOrderListResponse.getData().getListdata();
                if (currentPage == checkOrderListResponse.getData().getCurrentpage()) {
                    if (data != null && data.size() != 0) {
                        for (CheckOrderListResponse.DataEntity.ListdataEntity task : data) {
                            if (task != null) {
                                CheckOrderListResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
                                int taskId = task.getDeviceTaskResult().getId();
                                CheckOrder order = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName(), entity.getInspectorName(), entity.getArrageId(),
                                        entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
                                listData.add(order);
                            }
                        }
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
        String s = "";
        if (getString(R.string.state_all).equals(state)) {
            s = CheckConstants.ALL_CHECK;
        } else if (getString(R.string.should_check).equals(state)) {
            s = should;
        } else if (getString(R.string.check_done).equals(state)) {
            s = done;
        } else if (getString(R.string.check_over).equals(state)) {
            s = over;
        } else if (getString(R.string.over_finish).equals(state)) {
            s = overFinish;
        }
        return s;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
