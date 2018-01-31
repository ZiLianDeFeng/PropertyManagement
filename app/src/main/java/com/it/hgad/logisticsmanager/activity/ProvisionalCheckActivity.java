package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.CheckAdapter;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/3/27.
 */
public class ProvisionalCheckActivity extends CommonActivity{

    private TextView btn_confirm;
    private XListView lv_add_check;
    private List<CheckOrder> listData = new ArrayList<>();
    private CheckAdapter checkAdapter;
    private TextView tv_empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provisional_check);
        initHeader("临时巡检");
        initView();
        initData();
    }

    protected void initData() {

    }

    protected void initView() {
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        btn_confirm.setText("添加");
        btn_confirm.setVisibility(View.VISIBLE);
        btn_confirm.setOnClickListener(this);
        lv_add_check = (XListView) findViewById(R.id.lv_add_check);
        checkAdapter = new CheckAdapter(listData,this,"2");
        lv_add_check.setAdapter(checkAdapter);
        lv_add_check.setPullLoadEnable(true);
        lv_add_check.setPullRefreshEnable(true);
        lv_add_check.setXListViewListener(mIXListViewListener);
        lv_add_check.setEmptyView(tv_empty);
    }
    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
//            callRefresh();
            lv_add_check.setRefreshTime(CommonUtils.getCurrentTime());
            lv_add_check.stopRefresh();
        }

        @Override
        public void onLoadMore() {
//            mCurrentIndex = maintenanceAdapter.getCount();
//            callLoadMore();
            lv_add_check.stopLoadMore();
//            afterLoadMore = true;
        }
    };
    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
//                addCheck();
                add();
                break;
        }
    }

    private void add() {
        listData.add(new CheckOrder());
        checkAdapter.notifyDataSetChanged();
    }

    private void addCheck() {
        Intent intent = new Intent(ProvisionalCheckActivity.this,AddCheckActivity.class);
        startActivity(intent);
    }
}
