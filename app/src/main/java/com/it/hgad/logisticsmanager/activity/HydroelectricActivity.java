package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/3/27.
 */
public class HydroelectricActivity extends CommonActivity {

    private TextView tv_water;
    private TextView tv_electric;
    private TextView tv_gas;
    private TextView tv_date;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydroelectric);
        initHeader("水电抄表");
        initView();
        initData();
    }

    private void initData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = format.format(new Date());
        tv_date.setText(currentTime);
    }

    private void initView() {
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_water = (TextView) findViewById(R.id.tv_water);
        tv_electric = (TextView) findViewById(R.id.tv_electric);
        tv_gas = (TextView) findViewById(R.id.tv_gas);
        Button btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        TextView tv_water_copy = (TextView) findViewById(R.id.tv_water_copy);
        TextView tv_electric_copy = (TextView) findViewById(R.id.tv_electric_copy);
        TextView tv_gas_copy = (TextView) findViewById(R.id.tv_gas_copy);
        tv_water_copy.setOnClickListener(this);
        tv_electric_copy.setOnClickListener(this);
        tv_gas_copy.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_water_copy:
                showEditDialog("水表值", tv_water, "请填写水表值");
                break;
            case R.id.tv_electric_copy:
                showEditDialog("电表值", tv_electric, "请填写电表值");
                break;
            case R.id.tv_gas_copy:
                showEditDialog("气表值", tv_gas, "请填写气表值");
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        if ("0".equals(tv_water.getText().toString().trim()) || "0".equals(tv_electric.getText().toString().trim()) || "0".equals(tv_gas.getText().toString().trim())) {
            CommonUtils.showToast(this, "还未记录完水电气表哦！");
            return;
        }
        customProgressDialog = new CustomProgressDialog(this, "数据提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
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
                customProgressDialog.dismiss();
            }
        }, 2000);
    }

    private void showEditDialog(String title, final TextView tv, final String showToast) {
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View dialogView = factory.inflate(R.layout.editbox_layout, null);//这里必须是final的
        final EditText edit = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(title)//提示框标题
                .setView(dialogView)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //事件
                                String value = edit.getText().toString().trim();
                                if (!TextUtils.isEmpty(value)) {
                                    tv.setText(value);
                                } else {
                                    CommonUtils.showToast(HydroelectricActivity.this, showToast);
                                }
                                CommonUtils.closeKeybord(edit, HydroelectricActivity.this);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.closeKeybord(edit, HydroelectricActivity.this);
            }
        }).create().show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showInputMethod(HydroelectricActivity.this);
            }
        }, 100);
    }
}
