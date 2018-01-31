package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.CardAdapter;
import com.it.hgad.logisticsmanager.bean.LearnType;
import com.it.hgad.logisticsmanager.constants.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/26.
 */
public class LearnTypeActivity extends CommonActivity {

    private List<LearnType> list = new ArrayList<>();
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_type);
        initHeader("资料分类");
        initView();
        initData();
    }

    private void initData() {
        list.add(new LearnType("办公设备", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495775035157&di=7b174a4fc3f04648347f2436f21a91b7&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fu%2F00%2F38%2F01%2F53%2F55fb69ad802f4.jpg"));
        list.add(new LearnType("日杂百货", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495776529091&di=7401935e6cd08f758ad830e532aeb947&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fqk%2Fback_origin_pic%2F00%2F02%2F49%2Fc1002ff61423d49fe42c66a43f0a192d.jpg"));
        list.add(new LearnType("办公耗材", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495775035154&di=35c59cdfae1d41ab176bbdae3339e9cd&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fqk%2Fback_origin_pic%2F00%2F02%2F60%2F82521cf78c4b11fe26838a86c3a69945.jpg"));
        list.add(new LearnType("财务用品", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495776529096&di=ad0dd4d4f2433e5cd2579b3756ef6872&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F04%2F20%2F33%2Ff7093214cbc4cff42236e02951e838cf.jpg"));
        list.add(new LearnType("文具事务用品", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495776529093&di=2c4673047cae2f1be1d42bb534c77dcb&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fu%2F00%2F38%2F01%2F53%2F55fb620512e1e.jpg"));
        cardAdapter.notifyDataSetChanged();
    }

    private void initView() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_type);
        rv.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        cardAdapter = new CardAdapter(list, this);
        rv.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(new CardAdapter.RvItemClickListener() {
            @Override
            public void onItemClick(View view, LearnType learnType) {
                Intent intent = new Intent(LearnTypeActivity.this, LearningActivity.class);
                intent.putExtra(Constants.LEARN, (Serializable) learnType);
                startActivity(intent);
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
