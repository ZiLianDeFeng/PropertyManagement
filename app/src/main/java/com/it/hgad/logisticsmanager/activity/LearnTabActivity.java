package com.it.hgad.logisticsmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.fragment.ArticleFragment;
import com.it.hgad.logisticsmanager.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.act.BaseActivity;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/31.
 */
public class LearnTabActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup rg_title;
    private ArticleFragment articleFragment;
    private VideoFragment videoFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment mCurrentFragment = new Fragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_tab);
        initView();
        initData();
    }

    private void initData() {
        ((RadioButton) rg_title.getChildAt(0)).setChecked(true);
    }

    private void initView() {
        rg_title = (RadioGroup) findViewById(R.id.rg_title);
        rg_title.setOnCheckedChangeListener(listener);
        articleFragment = new ArticleFragment();
        videoFragment = new VideoFragment();
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_article:
                    addHideShow(articleFragment);
                    break;
                case R.id.rb_video:
                    addHideShow(videoFragment);
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
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    /**
     * 解决App重启后导致Fragment重叠的问题
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
