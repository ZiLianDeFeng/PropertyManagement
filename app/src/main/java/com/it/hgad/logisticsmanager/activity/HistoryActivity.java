package com.it.hgad.logisticsmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.fragment.CheckFragment;
import com.it.hgad.logisticsmanager.fragment.CheckHistoryFragment;
import com.it.hgad.logisticsmanager.fragment.MaintainHistoryFragment;
import com.it.hgad.logisticsmanager.fragment.MaintenanceFragment;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class HistoryActivity extends CommonActivity {


    private RadioGroup rg_history;
    private View v_check;
    private View v_maintain;
    private MaintenanceFragment maintenanceFragment;
    private CheckFragment checkFragment;
    private MaintainHistoryFragment maintainHistoryFragment;
    private CheckHistoryFragment checkHistoryFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initHeader("数据查询");
        initView();
        initData();
    }

    private void initData() {
        rg_history.check(R.id.rb_check);
    }

    private void initView() {
        rg_history = (RadioGroup) findViewById(R.id.rg_history);
        rg_history.setOnCheckedChangeListener(listener);
        maintainHistoryFragment = new MaintainHistoryFragment();
        checkHistoryFragment = new CheckHistoryFragment();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl, fragment)
                .commit();
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_check:
                    replaceFragment(checkHistoryFragment);
                    break;
                case R.id.rb_maintain:
                    replaceFragment(maintainHistoryFragment);
                    break;
            }
        }
    };

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
