package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.Sign;
import com.it.hgad.logisticsmanager.bean.request.SignInRequest;
import com.it.hgad.logisticsmanager.bean.request.SignStateRequest;
import com.it.hgad.logisticsmanager.bean.response.SignOutResponse;
import com.it.hgad.logisticsmanager.bean.response.SignStateResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.it.hgad.logisticsmanager.view.SignCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/2/9.
 */
public class SignActivity extends CommonActivity {
    private static final int RESULT = 101;
    private SignCalendar calendar;
    private String date;
    private int years;
    private String months;
    private Button btn_sign_in;
    private TextView tv_last;
    private TextView tv_next;
    private TextView tv_month;
    private TextView tv_sign_in;
    private TextView tv_sign_out;
    private int userId;
    private Button btn_sign_out;
    private List<String> list = new ArrayList<>();
    private List<Date> dates = new ArrayList<>();
    private CustomProgressDialog customProgressDialog;
    private Date curDate;
    private SimpleDateFormat formatter;
    private CustomProgressDialog progressDialog;
    private List<Sign> signList = new ArrayList<>();
    private String signState;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initHeader("打卡签到");
        initView();
        initData();
    }

    private void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            getSignState();
            customProgressDialog = new CustomProgressDialog(this, "数据加载中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
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
//                    params.height = height/6;
            params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
            customProgressDialog.getWindow().setAttributes(params);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (customProgressDialog != null && customProgressDialog.isShowing()) {
                        customProgressDialog.dismiss();
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 5000);
        }
    }

    private void initView() {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前时间
        curDate = new Date(System.currentTimeMillis());
        date = formatter.format(curDate);
        tv_last = (TextView) findViewById(R.id.tv_last);
        tv_last.setOnClickListener(this);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_sign_in = (TextView) findViewById(R.id.tv_sign_in);
        tv_sign_out = (TextView) findViewById(R.id.tv_sign_out);
        calendar = (SignCalendar) findViewById(R.id.sc_main);
        calendar.setOnCalendarClickListener(onCalendarClickListener);
        calendar.setOnCalendarDateChangedListener(onCalendarDateChangedListener);
        tv_month.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(this);
        btn_sign_out = (Button) findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(this);
        btn_sign_out.setClickable(false);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.SIGN_STATE,signState);
        setResult(RESULT,intent);
        finish();
    }

    SignCalendar.OnCalendarClickListener onCalendarClickListener = new SignCalendar.OnCalendarClickListener() {
        @Override
        public void onCalendarClick(int row, int col, String dateFormat) {
            boolean haveSign = false;
            calendar.removeAllBgColor();
            calendar.setCalendarDayBgColor(dateFormat, Color.GREEN);
            for (Sign sign : signList) {
                String inTime = sign.getInTime();
                inTime = inTime.substring(0, inTime.indexOf("T"));
                if (dateFormat.equals(inTime)) {
                    haveSign = true;
                    String signIn = sign.getInTime();
                    if (signIn != null) {
                        signIn = signIn.substring(signIn.indexOf("T") + 1, signIn.length());
                    } else {
                        signIn = "";
                    }
                    String signOut = sign.getOutTime();
                    if (signOut != null) {
                        signOut = signOut.substring(signOut.indexOf("T") + 1, signOut.length());
                    } else {
                        signOut = "";
                    }
                    tv_sign_in.setText(signIn);
                    tv_sign_out.setText(signOut);
                    break;
                }
            }
            if (!haveSign) {
                tv_sign_in.setText("");
                tv_sign_out.setText("");
            }
        }
    };
    SignCalendar.OnCalendarDateChangedListener onCalendarDateChangedListener = new SignCalendar.OnCalendarDateChangedListener() {
        @Override
        public void onCalendarDateChanged(int year, int month) {
            tv_month.setText(year + "年" + month + "月");
        }
    };

    private void getSignState() {
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            SignStateRequest signStateRequest = new SignStateRequest(userId + "");
            sendRequest(signStateRequest, SignStateResponse.class);
        } else {
//            Utils.showTost(mContext, getString(R.string.no_net));
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof SignStateRequest) {
            SignStateResponse signStateResponse = (SignStateResponse) response;
            if (signStateResponse != null) {
                if (signStateResponse.getData() != null && signStateResponse.getData().size() != 0) {
                    if (signList!=null){
                        signList.clear();
                    }
                    List<SignStateResponse.DataEntity> data = signStateResponse.getData();
                    for (SignStateResponse.DataEntity entity :
                            data) {
                        String inTime = entity.getInTime();
                        inTime = inTime.substring(0, inTime.indexOf("T"));
                        list.add(inTime);
                        Sign sign = new Sign(entity.getInTime(), entity.getId(), entity.getOutTime());
                        signList.add(sign);
                    }
                    calendar.addMarks(list, R.drawable.shape_circle_mark);
                    if (date.equals(data.get(data.size() - 1).getInTime().substring(0, data.get(data.size() - 1).getInTime().indexOf("T")))) {
                        String currentState = data.get(data.size() - 1).getCurrentState();
                        String inTime = data.get(data.size() - 1).getInTime();
                        if (inTime != null) {
                            inTime = inTime.substring(inTime.indexOf("T") + 1, inTime.length());
                        }
                        String outTime = data.get(data.size() - 1).getOutTime();
                        if (outTime != null) {
                            outTime = outTime.substring(outTime.indexOf("T") + 1, outTime.length());
                        }
                        if ("已签到".equals(currentState)) {
                            signState = "已签到";
                            tv_sign_in.setText(inTime);
                            btn_sign_in.setBackgroundResource(R.drawable.shape_circle_button);
                            btn_sign_in.setClickable(false);
                            btn_sign_out.setBackgroundResource(R.drawable.shape_circle_state);
                            btn_sign_out.setClickable(true);
                        } else {
                            signState = "已签退";
                            tv_sign_in.setText(inTime);
                            tv_sign_out.setText(outTime);
                            btn_sign_in.setBackgroundResource(R.drawable.shape_circle_button);
                            btn_sign_in.setClickable(false);
                            btn_sign_out.setBackgroundResource(R.drawable.shape_circle_button);
                            btn_sign_out.setClickable(false);
                        }
                    }else {
                        signState = "未签到";
                        btn_sign_in.setBackgroundResource(R.drawable.shape_circle_state);
                        btn_sign_out.setBackgroundResource(R.drawable.shape_circle_button);
                    }
                } else {
                    signState = "未签到";
                    btn_sign_in.setBackgroundResource(R.drawable.shape_circle_state);
                    btn_sign_out.setBackgroundResource(R.drawable.shape_circle_button);
                }
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.dismiss();
                        }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 500);
            }
        } else if (request instanceof SignInRequest) {
            SignOutResponse signOutResponse = (SignOutResponse) response;
            if (signOutResponse != null) {
                if ("0".equals(signOutResponse.getResult())) {
                    String currentState = signOutResponse.getData().getCurrentState();
                    if ("已签到".equals(currentState)) {
//                        signState = "已签到";
//                        list.add(formatter.format(curDate));
//                        calendar.removeAllMarks();
//                        calendar.addMarks(list, R.drawable.shape_circle_mark);
//                        String inTime = signOutResponse.getData().getInTime();
//                        if (inTime != null) {
//                            inTime = inTime.substring(inTime.indexOf("T") + 1, inTime.length());
//                        }
                        Utils.showTost(SignActivity.this, "签到成功");
                        getSignState();
//                        tv_sign_in.setText("签到时间：" + inTime);
//                        btn_sign_in.setBackgroundResource(R.drawable.shape_circle_button);
//                        btn_sign_in.setClickable(false);
//                        btn_sign_out.setBackgroundResource(R.drawable.shape_circle_state);
//                        btn_sign_out.setClickable(true);
                    } else {
//                        signState = "已签退";
//                        String outTime = signOutResponse.getData().getOutTime();
//                        if (outTime != null) {
//                            outTime = outTime.substring(outTime.indexOf("T") + 1, outTime.length());
//                        }
                        Utils.showTost(SignActivity.this, "签退成功");
                        getSignState();
//                        tv_sign_out.setText("签退时间：" + outTime);
//                        btn_sign_in.setBackgroundResource(R.drawable.shape_circle_button);
//                        btn_sign_in.setClickable(false);
//                        btn_sign_out.setBackgroundResource(R.drawable.shape_circle_button);
//                        btn_sign_out.setClickable(false);
                    }
                } else if ("1".equals(signOutResponse.getResult())) {
                    String currentState = signOutResponse.getData().getCurrentState();
                    if ("已签到".equals(currentState)) {
                        Utils.showTost(SignActivity.this, "签退失败，请重新签退");
                    } else {
                        Utils.showTost(SignActivity.this, "签到失败，请重新签退");
                    }
                }
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 500);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                sign(btn_sign_in);
                break;
            case R.id.btn_sign_out:
                sign(btn_sign_out);
                break;
            case R.id.tv_last:
                calendar.lastMonth();
                break;
            case R.id.tv_next:
                calendar.nextMonth();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void sign(Button view) {
        String currentTime = CommonUtils.getCurrentTime();
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            AlertDialog alertDialog = new AlertDialog.Builder(SignActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle("提示")//提示框标题
                    .setMessage(view.getId() == R.id.btn_sign_in ? "当前时间：" + currentTime + "\n请确认是否签到" : "当前时间：" + currentTime + "\n签退后当天不可再签到，请确认是否签退")
                    .setPositiveButton("确定",//提示框的两个按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SignInRequest signInRequset = new SignInRequest(userId + "");
                                    sendRequest(signInRequset, SignOutResponse.class);
                                    progressDialog = new CustomProgressDialog(SignActivity.this, "提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();
                                    Point size = new Point();
                                    progressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                                    int width = size.x;
                                    int height = size.y;
                                    WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();
                                    params.gravity = Gravity.CENTER;
                                    params.alpha = 0.8f;
//                        params.height = height/6;
                                    params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
                                    progressDialog.getWindow().setAttributes(params);
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            if (progressDialog != null && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                        }
                                    };
                                    Timer timer = new Timer();
                                    timer.schedule(timerTask, 5000);
                                }
                            }).setNegativeButton("取消", null)
                    .create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {
            Utils.showTost(this, getString(R.string.no_net));
        }
    }
}
