package com.it.hgad.logisticsmanager.activity;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.CommitAdapter;
import com.it.hgad.logisticsmanager.adapter.MaintenanceAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UnCommitCheckActivity extends CommonActivity implements MaintenanceAdapter.AdapterRefreshListener {

    public static final String CHECK_UNCOMMIT = "check";
    private List<CheckCommitDb> commitDbs = new ArrayList<>();
    private CommitAdapter commitAdapter;
    private int userId;
    private DbUtils db;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler();
    private boolean net_connet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncommit);
        initHeader("提交列表");
        initView();
        initData();
    }

    private void initData() {
        userId = SPUtils.getInt(UnCommitCheckActivity.this, SPConstants.USER_ID);
        if (db == null) {
            db = LocalApp.getDb();
        }
        List<CheckCommitDb> all = null;
        try {
            all = db.findAll(CheckCommitDb.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null) {
            for (CheckCommitDb checkCommitDb : all) {
                if (checkCommitDb.getHasCommit() == 1 && checkCommitDb.getUserId() == userId) {
                    commitDbs.add(checkCommitDb);
                }
            }
        }
        commitAdapter.notifyDataSetChanged();

    }

    private void initView() {
        ListView lv = (ListView) findViewById(R.id.lv_commit);
        commitAdapter = new CommitAdapter(UnCommitCheckActivity.this, commitDbs, CHECK_UNCOMMIT);
        commitAdapter.setListener(this);
        lv.setAdapter(commitAdapter);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void startCommit() {
        net_connet = true;
        customProgressDialog = new CustomProgressDialog(UnCommitCheckActivity.this, "数据提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        Point size = new Point();
        customProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        WindowManager.LayoutParams params = customProgressDialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.alpha = 0.8f;
//                        params.height = height/6;
        params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
        customProgressDialog.getWindow().setAttributes(params);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (net_connet) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                    }
                    CommonUtils.showToast(UnCommitCheckActivity.this, "网络异常");
                }
            }
        }, 10000);
    }

    @Override
    public void forRefresh() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        callRefresh();
        net_connet = false;
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
//            }
//        });
    }

    private void callRefresh() {
        commitDbs.clear();
        List<CheckCommitDb> all = null;
        try {
            all = db.findAll(CheckCommitDb.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null) {
            for (CheckCommitDb commitDb : all) {
                if (commitDb.getHasCommit() == 1 && commitDb.getUserId() == userId) {
                    commitDbs.add(commitDb);
                }
            }
        }
        commitAdapter.notifyDataSetChanged();
    }
}
