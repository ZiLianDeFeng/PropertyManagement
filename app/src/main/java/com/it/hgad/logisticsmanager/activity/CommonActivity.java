package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;

import cn.itcast.ecshop.act.BaseActivity;

/**
 * Created by Administrator on 2016/12/29.
 */
public abstract class CommonActivity extends BaseActivity implements View.OnClickListener {


    protected void initHeader(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(text);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void backWarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("提示").setMessage("是否确认返回");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
