package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
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
import android.widget.RelativeLayout;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.NewCheckAdapter;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.bean.SectionBean;
import com.it.hgad.logisticsmanager.bean.request.NewCheckRequest;
import com.it.hgad.logisticsmanager.bean.response.NewCheckOrderResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
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
 * Created by Administrator on 2017/5/17.
 */
public class UnNormalActivity extends CommonActivity {

    public static final String ROWS = "-1";
    //    private XListView lv;
    private NewCheckAdapter adapter;
    private String over;
    private List<SectionBean> newList = new ArrayList<>();
    private int currentPage = 1;
    private int userId;
    private Animation operatingAnim;
    private ImageView infoOperating;
    private RelativeLayout rl_info;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    rl_info.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private List<CheckOrder> listData = new ArrayList<>();
    private PinnedSectionRefreshListView lv;
    private String unnormalType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unnormal);
        initHeader("异常预警");
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
        Intent intent = getIntent();
        unnormalType = intent.getStringExtra(Constants.UNNORMAL);
        boolean isNetWork = Utils.checkNetWork(this);
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        paramValues.add(0, "day");
        paramNames.add(0, "deviceTask.orderType");
        paramNames.add(1, "deviceTask.inspector");
        paramNames.add(2, "deviceTask.taskStatus");
        paramValues.add(1, userId + "");
        //// TODO: 2017/6/14 这里需替换成异常待处理的type
        paramValues.add(2, over);
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
            NewCheckRequest newCheckRequest = new NewCheckRequest(paramNames, paramValues, paramConditons, paramTypes, ROWS, 0);
            sendRequest(newCheckRequest, NewCheckOrderResponse.class);
            currentPage--;
        }
    }

    private void initView() {
        over = SPUtils.getString(this, CheckConstants.OVER);
        lv = (PinnedSectionRefreshListView) findViewById(R.id.lv_check);
        adapter = new NewCheckAdapter(newList, this, Constants.UNNORMAL);
        lv.setAdapter(adapter);
        lv.setPullLoadEnable(false);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(listener);
        lv.setOnItemClickListener(mOnItemClickListener);
        lv.setEmptyView(findViewById(R.id.tv_empty));
        initAnimation();
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(UnNormalActivity.this, CheckHandleActivity.class);
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
//            callLoadMore();
//            lv.stopLoadMore();
        }
    };

    private void callRefresh() {
        boolean isNetWork = Utils.checkNetWork(this);
        if (isNetWork) {
            currentPage = 1;
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

    private void getOutlineData() {

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
                getOrderData(data);
            }
        }
    }

    private void getOrderData(final List<NewCheckOrderResponse.DataEntity.ListdataEntity> data) {
        long currentTimeMillis = System.currentTimeMillis();
        String curDate = CommonUtils.getCurrentTime().substring(0, 10);
        for (int i = data.size(); i > 0; i--) {
            NewCheckOrderResponse.DataEntity.ListdataEntity task = data.get(i - 1);
            NewCheckOrderResponse.DataEntity.ListdataEntity.DeviceTaskEntity entity = task.getDeviceTask();
            int taskId = task.getDeviceTaskResult().getId();
            CheckOrder order = new CheckOrder(entity.getDeviceType(), entity.getFinishTime(), entity.getDeviceCode(), entity.getInspector(), entity.getDeviceName(), entity.getInspectorName(), entity.getArrageId(),
                    entity.getTaskCode(), entity.getPlanTime(), entity.getResponser(), entity.getShouldTime(), entity.getTaskName(), entity.getId(), entity.getTaskStatus(), taskId);
//            if (WarningActivity.UNNORMAL_ALL.equals(unnormalType)) {
            listData.add(order);
//            } else if (WarningActivity.UNNORMAL_WEEK.equals(unnormalType)) {
//                String shouldTime = order.getShouldTime().replace("T", " ");
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                try {
//                    Date date = simpleDateFormat.parse(shouldTime);
//                    long time = date.getTime();
//                    if (time >= (currentTimeMillis - 7 * 24 * 60 * 1000)) {
//                        listData.add(order);
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            } else if (WarningActivity.UNNORMAL_DAY.equals(unnormalType)) {
//                String shouldDate = order.getShouldTime().substring(0, 10);
//                if (curDate.equals(shouldDate)) {
//                    listData.add(order);
//                }
//            }
        }
//        listData.add(new CheckOrder("1", "2017-6-14 15:30", "1", "1", "1", "1", 0, "2017-6-14 12:30", "2017-6-14 12:30", "1", "1", "1", 0, "1", 0));
//        listData.add(new CheckOrder("1", "2017-6-14 15:30", "1", "1", "1", "1", 0, "2017-6-13 12:30", "2017-6-14 12:30", "1", "1", "1", 0, "1", 0));
//        listData.add(new CheckOrder("1", "2017-6-14 15:30", "1", "1", "1", "1", 0, "2017-6-12 12:30", "2017-6-14 12:30", "1", "1", "1", 0, "1", 0));
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
        handler.sendEmptyMessage(Constants.STOP);
    }


    @Override
    public void onClick(View v) {

    }
}
