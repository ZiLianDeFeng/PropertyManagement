package com.it.hgad.logisticsmanager.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.util.ZXingUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/1.
 */
public class ZXingActivity extends CommonActivity {

    private ImageView iv_zxing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);
        initHeader("应用二维码");
        initView();
        initData();
    }

    private void initData() {
        String url = "http://59.173.12.6/MPMS/files/app/file/mpms_1.50.apk";
        WindowManager windowManager = this.getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        int windowWidth = defaultDisplay.getWidth();
        int windowHeight = defaultDisplay.getHeight();
        Bitmap bitmap = ZXingUtils.createQRImage(url, windowWidth*6/10, windowWidth*6/10);
        iv_zxing.setImageBitmap(bitmap);
    }

    private void initView() {
        iv_zxing = (ImageView) findViewById(R.id.iv_zxing);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
