package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.KeepDetailAdapter;
import com.it.hgad.logisticsmanager.bean.DeviceData;
import com.it.hgad.logisticsmanager.bean.DeviceDetail;
import com.it.hgad.logisticsmanager.bean.request.KeepRequest;
import com.it.hgad.logisticsmanager.bean.response.KeepResponse;
import com.it.hgad.logisticsmanager.constants.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/6/7.
 */
public class DeviceActivity extends CommonActivity {

    private DeviceData deviceData;
    private ListView lv;
    private KeepDetailAdapter keepDetailAdapter;
    private List<DeviceDetail> deviceDetails = new ArrayList<>();
    private TextView tv_name;
    private TextView tv_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        initHeader("设备详情");
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        deviceData = (DeviceData) intent.getSerializableExtra(Constants.DEVICE);
        for (DeviceDetail detail : deviceData.getDeviceDetails()
                ) {
            deviceDetails.add(detail);
        }
        keepDetailAdapter.notifyDataSetChanged();
        tv_name.setText(deviceData.getDeviceName());
        tv_type.setText(deviceData.getDeviceType());
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv_keep);
        Button btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        keepDetailAdapter = new KeepDetailAdapter(deviceDetails, this);
        lv.setAdapter(keepDetailAdapter);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_type = (TextView) findViewById(R.id.tv_type);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof KeepRequest) {
            KeepResponse keepResponse = (KeepResponse) response;
            if (keepResponse != null) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        KeepRequest keepRequset = new KeepRequest();
        sendRequest(keepRequset, KeepResponse.class);
    }
}
