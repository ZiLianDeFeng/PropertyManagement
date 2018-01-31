package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.request.AllCheckSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.AllOrderSizeRequest;
import com.it.hgad.logisticsmanager.bean.response.AllCheckSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.AllOrderSizeResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/17.
 */
public class WarningActivity extends CommonActivity {


    public static final String SHELVE = "shelve";
    public static final String SHELVE_ALL = "shelveAll";
    public static final String SHELVE_WEEK = "shelveWeek";
    public static final String SHELVE_DAY = "shelveDay";
    public static final String OVER_ALL = "overAll";
    public static final String OVER_WEEK = "overWeek";
    public static final String OVER_DAY = "overDay";
    public static final String UNNORMAL_ALL = "unnormalAll";
    public static final String UNNORMAL_WEEK = "unnormalWeek";
    public static final String UNNORMAL_DAY = "unnormalDay";
    public static final String OVER = "over";
    public static final String UNNORMAL = "unnormal";
    private int userId;
    //    private TextView tv_shelve_count;
//    private TextView tv_over_count;
    private TextView tv_unnormal_count;
    private TextView tv_shelve_all;
    private TextView tv_shelve_week;
    private TextView tv_shelve_day;
    private TextView tv_over_all;
    private TextView tv_over_week;
    private TextView tv_over_day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        initHeader("预警异常");
        initView();
        initData();
    }

    private void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        getMaintanceData();
        getCheckData();
    }

    private void initView() {
        RelativeLayout rl_shelve_all = (RelativeLayout) findViewById(R.id.rl_shelve_all);
        rl_shelve_all.setOnClickListener(this);
        RelativeLayout rl_shelve_week = (RelativeLayout) findViewById(R.id.rl_shelve_week);
        rl_shelve_week.setOnClickListener(this);
        RelativeLayout rl_shelve_day = (RelativeLayout) findViewById(R.id.rl_shelve_day);
        rl_shelve_day.setOnClickListener(this);
        RelativeLayout rl_over_all = (RelativeLayout) findViewById(R.id.rl_over_all);
        rl_over_all.setOnClickListener(this);
        RelativeLayout rl_over_week = (RelativeLayout) findViewById(R.id.rl_over_week);
        rl_over_week.setOnClickListener(this);
        RelativeLayout rl_over_day = (RelativeLayout) findViewById(R.id.rl_over_day);
        rl_over_day.setOnClickListener(this);
        tv_shelve_all = (TextView) findViewById(R.id.tv_shelve_all);
        tv_shelve_week = (TextView) findViewById(R.id.tv_shelve_week);
        tv_shelve_day = (TextView) findViewById(R.id.tv_shelve_day);
        tv_over_all = (TextView) findViewById(R.id.tv_over_all);
        tv_over_week = (TextView) findViewById(R.id.tv_over_week);
        tv_over_day = (TextView) findViewById(R.id.tv_over_day);
        LinearLayout ll_unnormal = (LinearLayout) findViewById(R.id.ll_unnormal);
        ll_unnormal.setOnClickListener(this);
//        tv_shelve_count = (TextView) findViewById(R.id.tv_shelve_count);
//        tv_over_count = (TextView) findViewById(R.id.tv_over_count);
//        tv_unnormal_count = (TextView) findViewById(R.id.tv_unnormal_count);
    }

    private void getCheckData() {
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            AllCheckSizeRequest allCheckSizeRequest = new AllCheckSizeRequest(userId);
            sendRequest(allCheckSizeRequest, AllCheckSizeResponse.class);
        } else {

        }
    }

    private void getMaintanceData() {
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            AllOrderSizeRequest allOrderSizeRequest = new AllOrderSizeRequest(userId);
            sendRequest(allOrderSizeRequest, AllOrderSizeResponse.class);
        } else {

        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof AllOrderSizeRequest) {
            AllOrderSizeResponse allOrderSizeResponse = (AllOrderSizeResponse) response;
            if (allOrderSizeResponse.getData() != null) {
                AllOrderSizeResponse.DataEntity data = allOrderSizeResponse.getData();
                int shaveCount = data.getShave();
                tv_shelve_all.setText(shaveCount + "");
            }
        } else if (request instanceof AllCheckSizeRequest) {
            AllCheckSizeResponse allCheckSizeResponse = (AllCheckSizeResponse) response;
            if (allCheckSizeResponse.getData() != null) {
                AllCheckSizeResponse.DataEntity data = allCheckSizeResponse.getData();
                int overCheckCount = data.getOverCheck();
                tv_over_all.setText(overCheckCount + "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        String type = null;
        switch (v.getId()) {
            case R.id.rl_shelve_all:
                type = SHELVE_ALL;
                goNext(type);
                break;
            case R.id.rl_shelve_week:
                type = SHELVE_WEEK;
                goNext(type);
                break;
            case R.id.rl_shelve_day:
                type = SHELVE_DAY;
                goNext(type);
                break;
            case R.id.rl_over_all:
                type = OVER_ALL;
                goNext(type);
                break;
            case R.id.rl_over_week:
                type = OVER_WEEK;
                goNext(type);
                break;
            case R.id.rl_over_day:
                type = OVER_DAY;
                goNext(type);
                break;
            case R.id.rl_unnormal_all:
                type = UNNORMAL_ALL;
                goNext(type);
                break;
            case R.id.rl_unnormal_week:
                type = UNNORMAL_WEEK;
                goNext(type);
                break;
            case R.id.rl_unnormal_day:
                type = UNNORMAL_DAY;
                goNext(type);
                break;
            case R.id.ll_unnormal:
                type = UNNORMAL;
                goNext(type);
                break;
        }
    }

    private void goNext(String type) {
        if (type.contains(SHELVE)) {
            Intent intent = new Intent(this, MaintainTabActivity.class);
            intent.putExtra(Constants.MAINTAIN_TYPE, type);
            startActivity(intent);
        } else if (type.contains(OVER)) {
            Intent intent = new Intent(this, OverActivity.class);
            intent.putExtra(Constants.OVER, type);
            startActivity(intent);
        } else if (type.contains(UNNORMAL)) {
            Intent intent = new Intent(this, UnNormalActivity.class);
            intent.putExtra(Constants.UNNORMAL, type);
            startActivity(intent);
        }
    }
}
