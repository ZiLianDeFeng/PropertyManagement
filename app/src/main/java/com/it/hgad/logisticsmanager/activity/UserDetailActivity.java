package com.it.hgad.logisticsmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UserDetailActivity extends CommonActivity{

    private TextView tv_user_name;
    private TextView tv_user_tel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initHeader("个人资料");
        initView();
        initData();
    }

    private void initData() {
        String userName = SPUtils.getString(this, SPConstants.REAL_NAME);
        String userTel = SPUtils.getString(this, SPConstants.USER_TEL);
        tv_user_name.setText(userName);
        tv_user_tel.setText(userTel);
    }

    private void initView() {
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_tel = (TextView) findViewById(R.id.tv_user_tel);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
