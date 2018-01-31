package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.it.hgad.logisticsmanager.R;

import cn.itcast.ecshop.act.BaseActivity;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/1/4.
 */
public class SplashActivity extends BaseActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
//                }
            }
        }, 2000);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }
}