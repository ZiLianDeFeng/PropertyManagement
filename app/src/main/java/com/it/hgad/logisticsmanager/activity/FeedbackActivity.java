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
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/18.
 */
public class FeedbackActivity extends CommonActivity {


    private static final int RESULT = 120;
    private EditText et_feedback;
    private DbUtils db;
    private Order order;
    private CommitDb commitDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initHeader("维修反馈");
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
            String repairReply = commitDb.getRepairReply();
            if (repairReply != null) {
                et_feedback.setText(repairReply);
            }
        }
        et_feedback.setSelection(et_feedback.getText().length());
    }

    private void initView() {
        et_feedback = (EditText) findViewById(R.id.et_feedback);
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
                final String feedback = et_feedback.getText().toString().trim();
                if (TextUtils.isEmpty(feedback)) {
//                    CommonUtils.showToast(this, "您还没有填写维修反馈哦！");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setTitle("提示").setMessage("您还没有填写维修反馈哦！")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra(Constants.FEEDBACK, feedback);
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
                    intent.putExtra(Constants.FEEDBACK, feedback);
                    setResult(RESULT, intent);
                    finish();
                }
                break;
        }
    }
}
