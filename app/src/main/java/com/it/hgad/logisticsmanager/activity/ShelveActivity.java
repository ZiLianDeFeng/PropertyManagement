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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.ShelveRequest;
import com.it.hgad.logisticsmanager.bean.response.ShelveResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.util.CommonUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/19.
 */
public class ShelveActivity extends CommonActivity {

    private EditText et_shelve_reason;
    private Order order;
    private int repairId;
    private TextView tv_common_reason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelve);
        initHeader("挂起理由");
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER_DATA);
        repairId = order.getId();
    }

    private void initView() {
        et_shelve_reason = (EditText) findViewById(R.id.et_shelve_reason);
        tv_common_reason = (TextView) findViewById(R.id.tv_common_reason);
        RelativeLayout rl_shelve_reason = (RelativeLayout) findViewById(R.id.rl_shelve_reason);
        rl_shelve_reason.setOnClickListener(this);
        Button btn_confirm = (Button) findViewById(R.id.btn_commit);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof ShelveRequest) {
            ShelveResponse shelveResponse = (ShelveResponse) response;

            if (shelveResponse != null) {
                if ("0".equals(shelveResponse.getResult())) {
                    CommonUtils.showToast(this, "挂起成功");
                    finish();
                    sendBroadcast();
                } else {
                    CommonUtils.showToast(this, "挂起失败");
                }
            }
        }
    }

    private void sendBroadcast() {
        Intent intent = new Intent(Constants.SHELVE_OK);
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                break;
            case R.id.rl_shelve_reason:
//                chooseReason();
                break;
        }

    }

    private String[] reasons = {"有另一个更加紧急的任务"
            , "因个人私事需请假"
            , "午休时间到了"
            , "下班了，明天再处理"
    };

    private void chooseReason() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShelveActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("挂起原因");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(reasons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = reasons[which];
                if (reason.length() > 10) {
                    reason = reason.substring(0, 10) + "...";
                }
                tv_common_reason.setText(reason);
            }
        });
        builder.show();
    }

    private void commit() {
        String currentTime = CommonUtils.getCurrentTime();
        String shelveReason = et_shelve_reason.getText().toString().trim();
        if (TextUtils.isEmpty(shelveReason)) {
            CommonUtils.showToast(this, "您还没有填写挂起原因哦！");
        } else {
            ShelveRequest shelveRequest = new ShelveRequest(repairId + "", shelveReason,currentTime);
            sendRequest(shelveRequest, ShelveResponse.class);
        }
    }
}
