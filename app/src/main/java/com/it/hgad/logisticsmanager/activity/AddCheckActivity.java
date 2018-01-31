package com.it.hgad.logisticsmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.CheckOrder;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/3/27.
 */
public class AddCheckActivity extends CommonActivity{

    private XListView lv_add_check;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check);
        initHeader("添加任务");
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {

    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
