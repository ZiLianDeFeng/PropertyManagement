package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.util.CommonUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/19.
 */
public class AbnomalReasonActivity extends CommonActivity{

    private static final int RESULT = 200;
    private EditText et_abnomal_reason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abnomal_reason);
        initHeader("申报原因");
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String reason = intent.getStringExtra(Constants.REASON);
        et_abnomal_reason.setText(reason);
    }

    private void initView() {
        et_abnomal_reason = (EditText) findViewById(R.id.et_abnomal_reason);
        Button btn_confirm = (Button) findViewById(R.id.btn_commit);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                String abnomalReason = et_abnomal_reason.getText().toString().trim();
                if (TextUtils.isEmpty(abnomalReason)) {
                    CommonUtils.showToast(this, "您还没有填写维修反馈哦！");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.REASON, abnomalReason);
                    setResult(RESULT, intent);
                    finish();
                }
                break;
        }
    }
}
