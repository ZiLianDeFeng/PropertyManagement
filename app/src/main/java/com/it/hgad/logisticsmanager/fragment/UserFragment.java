package com.it.hgad.logisticsmanager.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.CollectionActivity;
import com.it.hgad.logisticsmanager.activity.SettingActivity;
import com.it.hgad.logisticsmanager.bean.response.LoginOutResponse;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.FastBlurUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;


/**
 * Created by Administrator on 2016/12/26.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {
    private static final int LOAD = 1;
    private View mView;
    private TextView tv_loginout;
    private LoginOutResponse loginOutResponse;
    private TextView tv_more_name;
    private ImageView setting;
    private RelativeLayout rl_user_detail;

    private RelativeLayout rl_user;
    private ImageView iv_user_icon;
    private ImageView iv_blur;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD:
                    iv_user_icon.setImageBitmap(roundBitmap);
                    rl_user.setBackground(new BitmapDrawable(blurBitmap));
                    break;
            }
        }
    };
    private Bitmap blurBitmap;
    private Bitmap roundBitmap;
    private TextView tv_user_name;
    private TextView tv_user_tel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_user, null);
        initView();
        initData();
        return mView;
    }

    protected void initData() {
        String realName = SPUtils.getString(mContext, SPConstants.REAL_NAME);
        tv_more_name.setText(realName);
        String userTel = SPUtils.getString(mContext, SPConstants.USER_TEL);
        tv_user_name.setText(realName);
        tv_user_tel.setText(userTel);
        if (blurBitmap == null || roundBitmap == null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //获取用户的头像地址，通过picasso下载并转换成bitmap
////                try {
////                    Bitmap bitmap = Picasso.with(mContext).load("").get();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.admin);
                    roundBitmap = FastBlurUtils.toRoundBitmap(bitmap);
//                    int mBitmapWidth = bitmap.getWidth();
//                    int mBitmapHeight = bitmap.getHeight();
//                    Bitmap mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);//创建资源文件图片大小的图片，返回mutable
//                    int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
//                    int mArrayColor[] = new int[mArrayColorLengh];
//                    int count = 0;
//                    for (int i = 0; i < mBitmapHeight; i++) {
//                        for (int j = 0; j < mBitmapWidth; j++) {
//                            //获得Bitmap 图片中每一个点的color颜色值
//                            int color = bitmap.getPixel(j, i);
//                            mBitmap.setPixel(j, i, color);  //给mutable的Bitmap设置像素点，使其显示成资源文件里面的图片
//                            //将颜色值存在一个数组中 方便后面修改
//                            mArrayColor[count] = color;
//                            //如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理 笔者在这里就不做更多解释
//                            int r = Color.red(color);
//                            int g = Color.green(color);
//                            int b = Color.blue(color);
//                            count++;
//                        }
//                    }
//                    blurBitmap = FastBlurUtils.doBlur(mBitmap, 20, true);
//                    handler.sendEmptyMessage(LOAD);
//                }
//            }).start();
            iv_user_icon.setImageBitmap(roundBitmap);
        } else {
            iv_user_icon.setImageBitmap(roundBitmap);
//            rl_user.setBackground(new BitmapDrawable(blurBitmap));
        }
    }

    protected void initView() {
//        tv_loginout = (TextView) mView.findViewById(R.id.tv_loginout);
        tv_more_name = (TextView) mView.findViewById(R.id.tv_more_name);
        setting = (ImageView) mView.findViewById(R.id.iv_setting);
//        rl_user_detail = (RelativeLayout) mView.findViewById(R.id.rl_user_detail);
//        rl_user_detail.setOnClickListener(this);

//        tv_loginout.setOnClickListener(this);
        setting.setOnClickListener(this);
        rl_user = (RelativeLayout) mView.findViewById(R.id.rl_user);
        iv_user_icon = (ImageView) mView.findViewById(R.id.iv_user_icon);
//        iv_blur = (ImageView) mView.findViewById(R.id.iv_blur);
        tv_user_name = (TextView) mView.findViewById(R.id.tv_user_name);
        tv_user_tel = (TextView) mView.findViewById(R.id.tv_user_tel);
        RelativeLayout rl_collection =  (RelativeLayout) mView.findViewById(R.id.rl_collection);
        rl_collection.setOnClickListener(this);
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting:
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_collection:
                Intent intentThree = new Intent(mContext, CollectionActivity.class);
                startActivity(intentThree);
                break;
        }
    }
}
