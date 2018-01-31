package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.LearnAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.LearnData;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.ColletionDb;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/26.
 */
public class CollectionActivity extends CommonActivity {

    private ListView lv;
    private List<LearnData> list = new ArrayList<>();
    private LearnAdapter learnAdapter;
    private DbUtils db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initHeader("我的收藏");
        initView();
        initData();
    }

    private void initData() {
        try {
            if (db == null) {
                db = LocalApp.getDb();
                db.createTableIfNotExist(ColletionDb.class);
            }
            List<ColletionDb> colletionDbs = db.findAll(ColletionDb.class);
            for (ColletionDb colletionDb :
                    colletionDbs) {
                LearnData learnData = new LearnData(colletionDb.getId(), colletionDb.getTitle(), colletionDb.getPath(), colletionDb.isTop());
                if (colletionDb.isTop()) {
                    list.add(0, learnData);
                } else {
                    list.add(learnData);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        learnAdapter.closeAllItems();
                        lv.setAdapter(learnAdapter);
                    }
                });
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv_collection);
        learnAdapter = new LearnAdapter(list, this, Constants.COLLECTION);
        lv.setAdapter(learnAdapter);
        lv.setOnItemClickListener(onItemClickListener);
        learnAdapter.setCallFreshListener(freshListener);
    }

    private LearnAdapter.CallFreshListener freshListener = new LearnAdapter.CallFreshListener() {
        @Override
        public void callFresh() {
            list.clear();
            initData();
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(CollectionActivity.this, LearnDetailActivity.class);
            intent.putExtra(Constants.LEARN, list.get(position).getPath());
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
