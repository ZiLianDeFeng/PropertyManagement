package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/19.
 */
public class ResultDescribeActivity extends CommonActivity {

    private static final int RESULT = 140;
    private EditText et_describe;
    private DbUtils db;
    private CheckOrder checkOrder;
    private CheckCommitDb checkCommitDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_describe);
        initHeader("结果描述");
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        checkOrder = (CheckOrder) intent.getSerializableExtra(Constants.ORDER_DATA);
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(CheckCommitDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        try {
            checkCommitDb = db.findById(CheckCommitDb.class, checkOrder.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (checkCommitDb != null) {
            String repairReply = checkCommitDb.getFeedback();
            if (repairReply != null) {
                et_describe.setText(repairReply);
            }
        }
        et_describe.setSelection(et_describe.getText().length());
    }

    private void initView() {
        et_describe = (EditText) findViewById(R.id.et_describe);
        Button btn_confirm = (Button) findViewById(R.id.btn_commit);
        btn_confirm.setOnClickListener(this);
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
                final String describe = et_describe.getText().toString().trim();
                if (TextUtils.isEmpty(describe)) {
//                    CommonUtils.showToast(this, "您还没有填写结果描述哦！");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setTitle("提示").setMessage("您还没有填写结果描述哦！")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra(Constants.DESCRIBE, describe);
                                    setResult(RESULT, intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DESCRIBE, describe);
                    setResult(RESULT, intent);
                    finish();
                }
                break;
        }
    }
}
