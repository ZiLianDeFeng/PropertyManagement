package com.it.hgad.logisticsmanager.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.HydroelectricActivity;
import com.it.hgad.logisticsmanager.activity.ProvisionalCheckActivity;
import com.it.hgad.logisticsmanager.bean.request.AllCheckSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.AllOrderSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.SignInRequest;
import com.it.hgad.logisticsmanager.bean.request.SignStateRequest;
import com.it.hgad.logisticsmanager.bean.response.AllCheckSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.AllOrderSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.SignOutResponse;
import com.it.hgad.logisticsmanager.bean.response.SignStateResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;

import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/2/5.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private TextView tv_sign;
    private int userId;
    private String lastData;
    private String currentDate;
    private TextView tv_should;
    private TextView tv_done;
    private TextView tv_finish;
    private TextView tv_up;
    private TextView tv_should_do;
    private TextView tv_over;
    private TextView tv_finish_check;
    private TextView tv_over_finish;
    private TextView tv_recieve;
    private TextView tv_start;
    private PopupWindow popupWindow;
    private ImageView tv_more;
    private LinearLayout ll_title;
    private TextView tv_hydroelectric;
    private TextView tv_provisional;
    private CustomProgressDialog customProgressDialog;

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        initView();
        initData();
        return mView;
    }


    protected void initData() {
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork){
            customProgressDialog = new CustomProgressDialog(mContext, "数据加载中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
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
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                        customProgressDialog.dismiss();
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask,5000);
        }
//        getSignState();
//        getMaintanceData();
//        getCheckData();
    }

    private void getCheckData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            AllCheckSizeRequest allCheckSizeRequest = new AllCheckSizeRequest(userId);
//            AllCheckRequest allCheckRequest = new AllCheckRequest(userId);
            sendRequest(allCheckSizeRequest, AllCheckSizeResponse.class);
        } else {

        }
    }

    private void getMaintanceData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            AllOrderSizeRequest allOrderSizeRequest = new AllOrderSizeRequest(userId);
//            AllOrderRequest allOrderRequest = new AllOrderRequest(userId);
            sendRequest(allOrderSizeRequest, AllOrderSizeResponse.class);
        } else {

        }
    }


    private void getSignState() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            SignStateRequest signStateRequest = new SignStateRequest(userId + "");
            sendRequest(signStateRequest, SignStateResponse.class);
        } else {
//            Utils.showTost(mContext, getString(R.string.no_net));
        }
    }

    protected void initView() {
        tv_more = (ImageView) mView.findViewById(R.id.more);
        tv_more.setOnClickListener(this);
        tv_should = (TextView) mView.findViewById(R.id.tv_should);
        tv_done = (TextView) mView.findViewById(R.id.tv_done);
//        tv_finish = (TextView) mView.findViewById(R.id.tv_finish);
        tv_up = (TextView) mView.findViewById(R.id.tv_up);
        tv_should_do = (TextView) mView.findViewById(R.id.tv_should_do);
        tv_over = (TextView) mView.findViewById(R.id.tv_over);
        tv_finish_check = (TextView) mView.findViewById(R.id.tv_finish_check);
        tv_over_finish = (TextView) mView.findViewById(R.id.tv_over_finish);
//        tv_recieve = (TextView) mView.findViewById(R.id.tv_recieve);
        tv_start = (TextView) mView.findViewById(R.id.tv_start);
        ll_title = (LinearLayout) mView.findViewById(R.id.ll_title);
        initPopupWindow();
    }

    private void initPopupWindow() {
        View contentView = View.inflate(mContext, R.layout.popup_home, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
        popupWindow.setFocusable(true); //设置获取焦点
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);
        tv_sign = (TextView) contentView.findViewById(R.id.tv_sign);
        tv_sign.setOnClickListener(this);
        tv_hydroelectric = (TextView) contentView.findViewById(R.id.tv_hydroelectric);
        tv_hydroelectric.setOnClickListener(this);
        tv_provisional = (TextView) contentView.findViewById(R.id.tv_provisional);
        tv_provisional.setOnClickListener(this);
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof SignInRequest) {
            SignOutResponse signOutResponse = (SignOutResponse) response;
            if (signOutResponse != null) {
                if ("0".equals(signOutResponse.getResult())) {
                    String currentState = signOutResponse.getData().getCurrentState();
                    if ("签到".equals(tv_sign.getText().toString().trim())) {
                        Utils.showTost(mContext, "签到成功");
                        tv_sign.setText("签退");
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//                        lastData = formatter.format(curDate);
//                        SPUtils.put(mContext, SPConstants.LAST_DATA, lastData);
//                        tv_sign.setText("签退");
//                        LocalApp.setSignState("签退");
                    } else if ("签退".equals(tv_sign.getText().toString().trim())) {
                        Utils.showTost(mContext, "签退成功");
                        tv_sign.setText(currentState);
//                        tv_sign.setText("已签退");
//                        tv_sign.setClickable(false);
//                        LocalApp.setSignState("已签退");
                    }
                } else if ("1".equals(signOutResponse.getResult())) {
                    if ("签到".equals(tv_sign.getText().toString().trim())) {
                        Utils.showTost(mContext, "签到失败，请重新签到");
                    } else if ("签退".equals(tv_sign.getText().toString().trim())) {
                        Utils.showTost(mContext, "签退失败，请重新签退");
                    }
                }
            }
        } else if (request instanceof AllOrderSizeRequest) {
            AllOrderSizeResponse allOrderSizeResponse = (AllOrderSizeResponse) response;
            if (allOrderSizeResponse.getData() != null) {
                AllOrderSizeResponse.DataEntity data = allOrderSizeResponse.getData();
                tv_start.setText(data.getStart() + "");
                tv_should.setText(data.getMantance() + "");
                tv_done.setText(data.getFinish() + "");
                tv_up.setText(data.getShave() + "");
            }
        } else if (request instanceof AllCheckSizeRequest) {
            AllCheckSizeResponse allCheckSizeResponse = (AllCheckSizeResponse) response;
            if (allCheckSizeResponse != null) {
                if (allCheckSizeResponse.getData() != null) {
                    AllCheckSizeResponse.DataEntity data = allCheckSizeResponse.getData();
                    tv_should_do.setText(data.getShouldCheck() + "");
                    tv_over.setText(data.getOverCheck() + "");
                    tv_finish_check.setText(data.getDoneCheck() + "");
                    tv_over_finish.setText(data.getOverFinishCheck() + "");
                }
            }
        } else if (request instanceof SignStateRequest) {
            SignStateResponse signStateResponse = (SignStateResponse) response;
            if (signStateResponse != null) {
                if (signStateResponse.getData() != null && signStateResponse.getData().size() != 0) {
                    String currentState = signStateResponse.getData().get(0).getCurrentState();
                    if ("已签到".equals(currentState)) {
                        tv_sign.setText("签退");
                    } else {
                        tv_sign.setText(currentState);
                    }
                } else {
                    tv_sign.setText("签到");
                }
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                            customProgressDialog.dismiss();
                        }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask,500);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getMaintanceData();
        getCheckData();
        getSignState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                popupWindow.showAsDropDown(tv_more);
                break;
            case R.id.tv_sign:
                sign();
                popupWindow.dismiss();
                break;
            case R.id.tv_hydroelectric:
                Utils.showTost(mContext, Constants.FOR_WAIT);
//                goToHydroelectric();
//                popupWindow.dismiss();
                break;
            case R.id.tv_provisional:
                Utils.showTost(mContext, Constants.FOR_WAIT);
//                provisionalAddCheck();
//                popupWindow.dismiss();
                break;
        }
    }

    private void provisionalAddCheck() {
        Intent intent = new Intent(mContext,ProvisionalCheckActivity.class);
        startActivity(intent);
    }

    private void goToHydroelectric() {
        Intent intent = new Intent(mContext,HydroelectricActivity.class);
        startActivity(intent);
    }

    private void sign() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        String currentTime = CommonUtils.getCurrentTime();
        if (checkNetWork) {
            if ("已签退".equals(tv_sign.getText().toString().trim())) {
                return;
            }
            AlertDialog alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle("提示")//提示框标题
                    .setMessage(tv_sign.getText().toString().trim().equals("签到") ? "当前时间：" + currentTime + "\n请确认是否签到" : "当前时间：" + currentTime + "\n签退后当天不可再签到，请确认是否签退")
                    .setPositiveButton("确定",//提示框的两个按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SignInRequest signInRequset = new SignInRequest(userId + "");
                                    sendRequest(signInRequset, SignOutResponse.class);
                                }
                            }).setNegativeButton("取消", null)
                    .create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            Utils.showTost(mContext, getString(R.string.no_net));
        }
    }
}
