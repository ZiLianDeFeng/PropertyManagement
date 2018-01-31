package com.it.hgad.logisticsmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.fragment.MessageFragment;
import com.it.hgad.logisticsmanager.fragment.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MessageActivity extends CommonActivity{

    private RadioGroup rg_message;
    private MessageFragment messageFragment;
    private NotificationFragment notificationFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment mCurrentFragment = new Fragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initHeader("消息通知");
        initView();
        initData();
    }

    private void initData() {
        ((RadioButton) rg_message.getChildAt(0)).setChecked(true);
    }

    private void initView() {
        rg_message = (RadioGroup) findViewById(R.id.rg_message);
        rg_message.setOnCheckedChangeListener(listener);
        messageFragment = new MessageFragment();
        notificationFragment = new NotificationFragment();
        TextView tv_edit = (TextView) findViewById(R.id.btn_edit);
        tv_edit.setVisibility(View.VISIBLE);
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_message:
                    addHideShow(messageFragment);
                    break;
                case R.id.rb_notification:
                    addHideShow(notificationFragment);
                    break;
            }
        }
    };
    private void addHideShow(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragments.contains(fragment)) {
            fragmentTransaction.add(R.id.fl, fragment);
            fragments.add(fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        if (fragments != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }


    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 解决App重启后导致Fragment重叠的问题
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
