package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.LearnAdapter;
import com.it.hgad.logisticsmanager.bean.LearnData;
import com.it.hgad.logisticsmanager.constants.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/25.
 */
public class LearningActivity extends CommonActivity {

    private LearnAdapter learnAdapter;
    private List<LearnData> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        initHeader("培训学习");
        initView();
        initData();
    }

    private void initData() {
        list.add(new LearnData(0,"维修技巧一之变频空调故障三大维修方法",null,false));
        list.add(new LearnData(1,"维修技巧二之汽车空调故障诊断方法",null,false));
        list.add(new LearnData(2,"维修技巧三之制冷设备维修方法之自诊检查法",null,false));
        list.add(new LearnData(3,"维修技巧四之制冷设备维修方法之直观检查法",null,false));
        list.add(new LearnData(4,"维修技巧五之制冷设备维修方法之应急拆除法",null,false));
        list.add(new LearnData(5,"维修技巧六之按摩椅常见故障及维修方法",null,false));
        list.add(new LearnData(6,"电磁阀有噪音故障原因分析",null,false));
        list.add(new LearnData(7,"分立元件组成的PC电源+5VSB电路检修",null,false));
        list.add(new LearnData(8,"同机芯不同屏的主板代用方法",null,false));
        list.add(new LearnData(9,"改造坑爹移动电源",null,false));
        list.add(new LearnData(10,"超简单！为笔记本更换固态硬盘",null,false));
        learnAdapter.notifyDataSetChanged();
    }

    private void initView() {
        ListView lv = (ListView) findViewById(R.id.lv_learn);
        learnAdapter = new LearnAdapter(list, this,Constants.LEARN);
        lv.setAdapter(learnAdapter);
        lv.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(LearningActivity.this, LearnDetailActivity.class);
            intent.putExtra(Constants.LEARN, list.get(position).getType());
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
