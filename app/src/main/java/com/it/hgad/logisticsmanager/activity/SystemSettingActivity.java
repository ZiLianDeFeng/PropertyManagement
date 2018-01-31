package com.it.hgad.logisticsmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/6/15.
 */
public class SystemSettingActivity extends CommonActivity {

    private ToggleButton tb_shock;
    private ToggleButton tb_sound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initHeader("系统设置");
        initView();
        initData();
    }


    private void initData() {
        boolean notFirst = SPUtils.getBoolean(this, Constants.SETTING_FIRST);
        if (!notFirst) {
            tb_sound.setChecked(true);
            tb_shock.setChecked(true);
            SPUtils.put(this, Constants.SETTING_FIRST, true);
        }
        int shockId = SPUtils.getInt(this, Constants.NOTI_SHOCK);
        int soundId = SPUtils.getInt(this, Constants.NOTI_SOUND);
        tb_sound.setChecked(soundId == 1);
        tb_shock.setChecked(shockId == 1);
    }

    private void initView() {
        tb_shock = (ToggleButton) findViewById(R.id.tb_shock);
        tb_shock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(SystemSettingActivity.this, Constants.NOTI_SHOCK, isChecked ? 1 : 2);
            }
        });
        tb_sound = (ToggleButton) findViewById(R.id.tb_sound);
        tb_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(SystemSettingActivity.this, Constants.NOTI_SOUND, isChecked ? 1 : 2);
            }
        });
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
