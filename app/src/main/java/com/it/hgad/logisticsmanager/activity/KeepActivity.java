package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.KeepAdapter;
import com.it.hgad.logisticsmanager.bean.DeviceData;
import com.it.hgad.logisticsmanager.bean.DeviceDetail;
import com.it.hgad.logisticsmanager.constants.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/6/7.
 */
public class KeepActivity extends CommonActivity {

    private ListView lv;
    private List<DeviceData> listData = new ArrayList<>();
    private KeepAdapter keepAdapter;
    private List<DeviceDetail> details = new ArrayList<>();
    private List<DeviceDetail> details1 = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep);
        initHeader("设备保养");
        initView();
        initData();
    }

    private void initData() {
        details.add(new DeviceDetail("温度计", false));
        details.add(new DeviceDetail("量程表", false));
        details.add(new DeviceDetail("排风管", false));
        details1.add(new DeviceDetail("温度计", false));
        details1.add(new DeviceDetail("量程表", false));
        details1.add(new DeviceDetail("排风管", false));
        details1.add(new DeviceDetail("温度计", false));
        listData.add(new DeviceData("高压电源进线柜G1", "配电", "", details));
        listData.add(new DeviceData("RTHD水冷螺杆式冷水机组", "空调", "", details1));
        listData.add(new DeviceData("污泥压滤机", "污水", "", details1));
        listData.add(new DeviceData("真空罐", "医气", "", details1));
        listData.add(new DeviceData("高压电源进线柜G1", "配电", "", details1));
        listData.add(new DeviceData("RTHD水冷螺杆式冷水机组", "空调", "", details1));
        listData.add(new DeviceData("污泥压滤机", "污水", "", details1));
        listData.add(new DeviceData("真空罐", "医气", "", details1));
        keepAdapter.notifyDataSetChanged();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv_device);
        keepAdapter = new KeepAdapter(listData, this);
        lv.setAdapter(keepAdapter);
        lv.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(KeepActivity.this, DeviceActivity.class);
            intent.putExtra(Constants.DEVICE, ((Serializable) listData.get(position)));
            startActivity(intent);
        }
    };

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
