package com.it.hgad.logisticsmanager.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/2/17.
 */
public class VersionActivity extends CommonActivity{

    private TextView tv_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        initHeader("应用信息");
        initView();
        initData();
    }

    private void initData() {
        String versionName = getVersionName();
        tv_version.setText("当前版本号：v"+versionName);
    }

    private void initView() {
        tv_version = (TextView) findViewById(R.id.tv_version);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
    /**
     * 获取版本号
     * @return
     */
    private String getVersionName() {
        // PackageManager
        PackageManager pm = getPackageManager();
        // PackageInfo
        try {
            // flags 代表可以获取的包信息的内容 传0即可 因为任何Flag都可以获取版本号
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
        }
        return null;
    }
}
