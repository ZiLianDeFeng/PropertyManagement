package com.it.hgad.logisticsmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.request.CheckOrderListRequest;
import com.it.hgad.logisticsmanager.bean.response.CheckOrderListResponse;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.max.pinnedsectionrefreshlistviewdemo.Messages;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionBean;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView;
import com.max.pinnedsectionrefreshlistviewdemo.WarnDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/25.
 */
public class DemoActivity extends CommonActivity implements PinnedSectionRefreshListView.IXListViewListener {

    private PinnedSectionRefreshListView lv;
    //带时间分类悬浮的listview
    //从后台后去的源数据,主要包含一个时间 和内容item; 这个根据实际需求可以修改
    private List<Messages> mData;
    //将源数据处理成分组后的数据
    private ArrayList<PinnedSectionBean> real_data;
    private WarnDetailAdapter mAdapter;
    private int userId;
    private int currentPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
        initData();
        show();
    }

    private void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        mData = new ArrayList<Messages>();
        CheckOrderListRequest checkOrderListRequest = new CheckOrderListRequest(userId, "", currentPage + "");
        sendRequest(checkOrderListRequest, CheckOrderListResponse.class);
        for (int i = 0; i < 50; i++) {
            Messages messages = new Messages();
            if (i < 10) {
                messages.setTime("2017-4-11 0" + i + ":00:00");
            } else if (i >= 10 && i < 20) {
                messages.setTime("2017-4-12 0" + (i - 10) + ":00:00");
            } else if (i >= 20 && i < 34) {
                messages.setTime("2017-4-12 " + (i - 10) + ":00:00");
            } else if (i >= 34 && i < 44) {
                messages.setTime("2017-4-13 0" + (i - 34) + ":00:00");
            } else if (i >= 44) {
                messages.setTime("2017-4-13 " + (i - 34) + ":00:00");
            }
            messages.setContent("这是第" + i + "项的内容");
            mData.add(messages);
        }
        //把数据转换成带分类后的数据,实际list setadapter是这个real_data;
        real_data = new ArrayList<PinnedSectionBean>();
        real_data = PinnedSectionBean.getData(mData);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);
    }

    private void initView() {
        lv = (PinnedSectionRefreshListView) findViewById(R.id.lv);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
    }

    /**
     * 显示分好类的listview
     **/
    private void show() {
        mAdapter = new WarnDetailAdapter(real_data, this);
        lv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        String currentTime = CommonUtils.getCurrentTime();
        lv.stopRefresh();
        lv.setRefreshTime(currentTime);
    }

    @Override
    public void onLoadMore() {
        lv.stopLoadMore();
    }
}
