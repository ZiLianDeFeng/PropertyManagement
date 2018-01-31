package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.it.hgad.logisticsmanager.R;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/5.
 */
public class SynchronizationActivity extends CommonActivity{
    private RelativeLayout rl_unCommit;
    private RelativeLayout rl_unCommitCheck;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        initHeader("数据同步");
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        rl_unCommit = (RelativeLayout) findViewById(R.id.rl_unCommit);
        rl_unCommit.setOnClickListener(this);
        rl_unCommitCheck = (RelativeLayout) findViewById(R.id.rl_unCommitCheck);
        rl_unCommitCheck.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_unCommit:
                Intent intentOne = new Intent(this, UnCommitActivity.class);
                startActivity(intentOne);
                break;
            case R.id.rl_unCommitCheck:
                Intent intentTwo = new Intent(this, UnCommitCheckActivity.class);
                startActivity(intentTwo);
                break;
        }
    }
}
