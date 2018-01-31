package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/22.
 */
public class EditContentActivity extends CommonActivity {

    private static final int RESULT = 330;
    private Order order;
    private DbUtils db;
    private CommitDb commitDb;
    private EditText et_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        initHeader("报修内容");
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER_DATA);
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(CommitDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        try {
            commitDb = db.findById(CommitDb.class, order.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (commitDb != null) {
            String repairContent = commitDb.getRepairContent();
            if (repairContent != null) {
                et_content.setText(repairContent);
            }
        }

    }

    private void initView() {
        et_content = (EditText) findViewById(R.id.et_content);
        Button btn_confirm = (Button) findViewById(R.id.btn_commit);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void back(View view) {
        backWarm();
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    CommonUtils.showToast(this, "您还没有填写报修内容哦！");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EDIT_CONTENT, content);
                    setResult(RESULT, intent);
                    finish();
                }
                break;
        }
    }
}
