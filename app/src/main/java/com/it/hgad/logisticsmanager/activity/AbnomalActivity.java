package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.StartRepairRequest;
import com.it.hgad.logisticsmanager.bean.response.StartRepairResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.Utils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/12.
 */
public class AbnomalActivity extends CommonActivity {

    private static final int REASON = 199;
    private TextView tv_number;
    private TextView tv_type;
    private String reason = "";
    private Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abnomal);
        initHeader("维修申报");
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER_DATA);
        String repairNo = order.getRepairNo();
        String repairType = order.getRepairType();
        tv_number.setText(repairNo);
        tv_type.setText(repairType);
    }

    private void initView() {
        Button btn_commit_abnomal = (Button) findViewById(R.id.btn_commit_abnomal);
        btn_commit_abnomal.setOnClickListener(this);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_type = (TextView) findViewById(R.id.tv_type);
        RelativeLayout rl_abnormal_reason = (RelativeLayout) findViewById(R.id.rl_abnormal_reason);
        rl_abnormal_reason.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof StartRepairRequest) {
            StartRepairResponse startRepairResponse = (StartRepairResponse) response;
            if (startRepairResponse != null) {
                if ("0".equals(startRepairResponse.getResult())) {
                    Intent intent = new Intent(AbnomalActivity.this, AbnomalResultActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REASON:
                if (data != null) {
                    reason = data.getStringExtra(Constants.REASON);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit_abnomal:
                commit();
                break;
            case R.id.rl_abnormal_reason:
                abnormalReason();
                break;
        }
    }

    private void abnormalReason() {
        Intent intent = new Intent(AbnomalActivity.this, AbnomalReasonActivity.class);
        intent.putExtra(Constants.REASON,reason);
        startActivityForResult(intent, REASON);
//        LayoutInflater factory = LayoutInflater.from(AbnomalActivity.this);//提示框
//        final View dialogView = factory.inflate(R.layout.editbox_layout, null);//这里必须是final的
//        final EditText edit = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
//        edit.setText(reason);
//        new AlertDialog.Builder(AbnomalActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
//                .setTitle("申报原因")//提示框标题
//                .setView(dialogView)
//                .setPositiveButton("确定",//提示框的两个按钮
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                reason = edit.getText().toString().trim();
//                                CommonUtils.closeKeybord(edit, AbnomalActivity.this);
//                            }
//                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                CommonUtils.closeKeybord(edit, AbnomalActivity.this);
//            }
//        }).create().show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                CommonUtils.showInputMethod(AbnomalActivity.this);
//            }
//        }, 100);
    }

    private void commit() {
        if (!"".equals(reason) && reason != null) {
            String currentTime = CommonUtils.getCurrentTime();
            boolean checkNetWork = Utils.checkNetWork(AbnomalActivity.this);
            if (checkNetWork) {
                StartRepairRequest startRepairRequest = new StartRepairRequest(order.getId() + "", reason, "", currentTime);
                sendRequest(startRepairRequest, StartRepairResponse.class);
            }
        }else {
            CommonUtils.showToast(this,"请填写申报原因！");
        }

    }
}
