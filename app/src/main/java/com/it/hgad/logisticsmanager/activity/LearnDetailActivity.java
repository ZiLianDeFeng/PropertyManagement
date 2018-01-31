package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/25.
 */
public class LearnDetailActivity extends CommonActivity {

    private TextView tv_content;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_detail);
        initHeader("资料详情");
        initView();
        initData();
    }

    private void initData() {
        String address = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.LEARN);
        String ip = HttpConstants.ProFormatUrl(address);
        String localPath = ip + path;
        webView.loadUrl(localPath);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int width = getWindowManager().getDefaultDisplay().getWidth()-metrics.densityDpi;
//        int scaleInPercent = width * 100 / 322;
//        webView.setInitialScale(scaleInPercent);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //此处可作转向操作
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              String url) {
                //TODO Here we can replace the url
                if (url.endsWith("cj_all_picture.png")) {
//                    try {
//                        InputStream is = mContext.getResources().getAssets().open("cj_all_tab_bg.png");
//                        WebResourceResponse response = new WebResourceResponse("image/gif", "utf-8", is);
//                        return response;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
                return null;
            }
        });
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
