package com.it.hgad.logisticsmanager.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.HistoryActivity;
import com.it.hgad.logisticsmanager.activity.KeepActivity;
import com.it.hgad.logisticsmanager.activity.LearnTabActivity;
import com.it.hgad.logisticsmanager.activity.MaintainTabActivity;
import com.it.hgad.logisticsmanager.activity.MessageActivity;
import com.it.hgad.logisticsmanager.activity.SignActivity;
import com.it.hgad.logisticsmanager.activity.SynchronizationActivity;
import com.it.hgad.logisticsmanager.activity.TaskCheckActivity;
import com.it.hgad.logisticsmanager.activity.TemporaryActivity;
import com.it.hgad.logisticsmanager.activity.WarningActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.request.AllCheckSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.AllOrderSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.SignStateRequest;
import com.it.hgad.logisticsmanager.bean.response.AllCheckSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.AllOrderSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.SignStateResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.jauker.widget.BadgeView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.paradoxie.autoscrolltextview.VerticalTextview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/10.
 */
public class NewHomeFragment extends BaseFragment {

    private static final int SIGN_CODE = 100;
    private static final int CHECK = 99;
    private static final int TEMPORARY = 98;
    private static final int MAINTAIN = 97;
    private static final int SYNCHRONIZATION = 96;
    private View mView;
    private ImageView iv_more;
    private PopupWindow popupWindow;
    private TextView tv_name;
    private TextView tv_sign_state;
    private int userId;
    private Date curDate;
    private SimpleDateFormat formatter;
    private String date;
    private CustomProgressDialog customProgressDialog;
    private BadgeView badge_check_today;
    private BadgeView badge_check_week;
    private BadgeView badge_check_month;
    private BadgeView badge_maintain;
    private BadgeView badge_temporary;
    private TextView tv_check_count;
    private TextView tv_maintain_count;
    private Handler handler = new Handler();
    private TextView tv_shelve;
    private DbUtils db;
    private String done;
    private String should;
    private String over;
    private String overFinish;
    private String cancel;
    private String shouldDo;
    private String shouldStart;
    private String shouldRecieve;
    private String haveShelve;
    private int overCheck;
    private int shave;
//    private VerticalTextview tv_scroll;
    private ArrayList<String> textList = new ArrayList<>();
    //    private BadgeView badge_warning;

    @Override
    protected void initData() {
        if (db == null) {
            db = LocalApp.getDb();
        }
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        String realName = SPUtils.getString(mContext, SPConstants.REAL_NAME);
        tv_name.setText(realName);
        refreshData();
//        for (int i = 0; i < 5; i++) {
//            textList.add("第" + (i + 1) + "个广告位出租，广告费很便宜的");
//        }
//        tv_scroll.setTextList(textList);//加入显示内容,集合类型
    }

    @Override
    public void onResume() {
        super.onResume();
//        tv_scroll.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
//        tv_scroll.stopAutoScroll();
    }

    @Override
    protected void initView() {
        should = SPUtils.getString(mContext, CheckConstants.SHOULD);
        over = SPUtils.getString(mContext, CheckConstants.OVER);
        shouldRecieve = SPUtils.getString(mContext, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(mContext, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(mContext, RepairConstants.SHOULD_DO);
        haveShelve = SPUtils.getString(mContext, RepairConstants.HAVE_SHELVE);
        TextView tv_message = (TextView) mView.findViewById(R.id.tv_message);
        tv_message.setOnClickListener(this);
        TextView tv_sign = (TextView) mView.findViewById(R.id.tv_sign);
        tv_sign.setOnClickListener(this);
        TextView tv_temporary = (TextView) mView.findViewById(R.id.tv_temporary);
        tv_temporary.setOnClickListener(this);
        badge_temporary = new BadgeView(mContext);
        badge_temporary.setTargetView(tv_temporary);
        badge_temporary.setBadgeMargin(0, 5, 15, 0);
        badge_temporary.setHideOnNull(true);
        TextView tv_hydroelectric = (TextView) mView.findViewById(R.id.tv_hydroelectric);
        tv_hydroelectric.setOnClickListener(this);
        TextView tv_check_today = (TextView) mView.findViewById(R.id.tv_check_today);
        tv_check_today.setOnClickListener(this);
        badge_check_today = new BadgeView(mContext);
        badge_check_today.setTargetView(tv_check_today);
        badge_check_today.setBadgeMargin(0, 5, 15, 0);
        badge_check_today.setHideOnNull(true);
        TextView tv_check_week = (TextView) mView.findViewById(R.id.tv_check_week);
        badge_check_week = new BadgeView(mContext);
        badge_check_week.setTargetView(tv_check_week);
        badge_check_week.setBadgeMargin(0, 5, 15, 0);
        badge_check_week.setHideOnNull(true);
        tv_check_week.setOnClickListener(this);
        TextView tv_check_month = (TextView) mView.findViewById(R.id.tv_check_month);
        tv_check_month.setOnClickListener(this);
        badge_check_month = new BadgeView(mContext);
        badge_check_month.setTargetView(tv_check_month);
        badge_check_month.setBadgeMargin(0, 5, 15, 0);
        badge_check_month.setHideOnNull(true);
        TextView tv_warning = (TextView) mView.findViewById(R.id.tv_warning);
        tv_warning.setOnClickListener(this);
//        badge_warning = new BadgeView(mContext);
//        badge_warning.setTargetView(tv_warning);
//        badge_warning.setBadgeMargin(0, 5, 15, 0);
//        badge_warning.setHideOnNull(true);
        TextView tv_maintain = (TextView) mView.findViewById(R.id.tv_maintain);
        tv_maintain.setOnClickListener(this);
        badge_maintain = new BadgeView(mContext);
        badge_maintain.setTargetView(tv_maintain);
        badge_maintain.setBadgeMargin(0, 5, 15, 0);
        badge_maintain.setHideOnNull(true);
        TextView tv_upkeep = (TextView) mView.findViewById(R.id.tv_upkeep);
        tv_upkeep.setOnClickListener(this);
        TextView tv_history = (TextView) mView.findViewById(R.id.tv_history);
        tv_history.setOnClickListener(this);
        TextView tv_synchronization = (TextView) mView.findViewById(R.id.tv_synchronization);
        tv_synchronization.setOnClickListener(this);
        tv_sign_state = (TextView) mView.findViewById(R.id.tv_sign_state);
        iv_more = (ImageView) mView.findViewById(R.id.more);
        iv_more.setOnClickListener(this);
        tv_name = (TextView) mView.findViewById(R.id.tv_name);
        tv_check_count = (TextView) mView.findViewById(R.id.tv_check_count);
        tv_maintain_count = (TextView) mView.findViewById(R.id.tv_maintain_count);
        tv_shelve = (TextView) mView.findViewById(R.id.tv_shelve);
        initPopupWindow();
//        tv_scroll = (VerticalTextview) mView.findViewById(R.id.tv_scroll);
//        tv_scroll.setText(12, 2, Color.RED);//设置属性,具体跟踪源码
//        tv_scroll.setTextStillTime(5000);//设置停留时长间隔
//        tv_scroll.setAnimTime(300);//设置进入和退出的时间间隔
//        tv_scroll.setOnItemClickListener(onItemClickListener);
    }

    VerticalTextview.OnItemClickListener onItemClickListener = new VerticalTextview.OnItemClickListener() {
        @Override
        public void onItemClick(int i) {

        }
    };

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home_new, null);
        return mView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void refreshData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            getSignState();
            getMaintanceData();
            getCheckData();
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (customProgressDialog != null && customProgressDialog.isShowing()) {
                        customProgressDialog.dismiss();
                        CommonUtils.showToast(mContext, "当前网络信号较差");
                    }
                }
            }, 5000);
        } else {
            try {
                List<OrderDb> orderDbs = db.findAll(OrderDb.class);
                List<CheckOrderDb> checkOrderDbs = db.findAll(CheckOrderDb.class);
                long shouldRecieveCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", shouldRecieve).and("userId", "=", userId)));
                long shouldStartCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", shouldStart).and("userId", "=", userId)));
                long shouldDoCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", shouldDo).and("userId", "=", userId)));
                long haveShelveCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", haveShelve).and("userId", "=", userId)));
                long shouldCount = db.count(Selector.from(CheckOrderDb.class).where(WhereBuilder.b("taskStatus", "=", should).and("userId", "=", userId)));
                long overCount = db.count(Selector.from(CheckOrderDb.class).where(WhereBuilder.b("taskStatus", "=", over).and("userId", "=", userId)));
                int mainCount = (int) (shouldRecieveCount + shouldStartCount + shouldDoCount);
                int checkCount = (int) (shouldCount + overCount);
                tv_check_count.setText(checkCount + "");
                tv_maintain_count.setText(mainCount + "");
                tv_shelve.setText(haveShelveCount + "");
                badge_maintain.setBadgeCount(mainCount);
                badge_check_today.setBadgeCount(checkCount);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    private void getSignState() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            SignStateRequest signStateRequest = new SignStateRequest(userId + "");
            sendRequest(signStateRequest, SignStateResponse.class);
        } else {
//            Utils.showTost(mContext, getString(R.string.no_net));
            tv_sign_state.setText("未签到");
        }
    }

    private void getCheckData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            AllCheckSizeRequest allCheckSizeRequest = new AllCheckSizeRequest(userId);
            sendRequest(allCheckSizeRequest, AllCheckSizeResponse.class);
        } else {

        }
    }

    private void getMaintanceData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            AllOrderSizeRequest allOrderSizeRequest = new AllOrderSizeRequest(userId);
            sendRequest(allOrderSizeRequest, AllOrderSizeResponse.class);
        } else {

        }
    }

    private void initPopupWindow() {
        View contentView = View.inflate(mContext, R.layout.popup_new_home, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
        popupWindow.setFocusable(true); //设置获取焦点
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_CODE && data != null) {
            String signState = data.getStringExtra(Constants.SIGN_STATE);
            if (signState == null || "".equals(signState)) {
                tv_sign_state.setText("未签到");
            } else {
                tv_sign_state.setText(signState);
            }
            refreshData();
        } else if (requestCode == CHECK || requestCode == TEMPORARY || requestCode == MAINTAIN || requestCode == SYNCHRONIZATION) {
            refreshData();
        }
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof AllOrderSizeRequest) {
            AllOrderSizeResponse allOrderSizeResponse = (AllOrderSizeResponse) response;
            if (allOrderSizeResponse.getData() != null) {
                AllOrderSizeResponse.DataEntity data = allOrderSizeResponse.getData();
                shave = data.getShave();
                badge_maintain.setBadgeCount(data.getRecieve() + data.getStart() + data.getMantance() + data.getShave());
                tv_maintain_count.setText(data.getRecieve() + data.getStart() + data.getMantance() + "");
                tv_shelve.setText(data.getShave() + "");
            }
        } else if (request instanceof AllCheckSizeRequest) {
            AllCheckSizeResponse allCheckSizeResponse = (AllCheckSizeResponse) response;
            if (allCheckSizeResponse.getData() != null) {
                AllCheckSizeResponse.DataEntity data = allCheckSizeResponse.getData();
                overCheck = data.getOverCheck();
                badge_check_today.setBadgeCount(data.getShouldCheck() + data.getOverCheck());
                tv_check_count.setText(data.getShouldCheck() + data.getOverCheck() + "");
            }
        } else if (request instanceof SignStateRequest) {
            SignStateResponse signStateResponse = (SignStateResponse) response;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            // 获取当前时间
            curDate = new Date(System.currentTimeMillis());
            date = formatter.format(curDate);
            if (signStateResponse.getData() != null) {
                if (signStateResponse.getData() != null && signStateResponse.getData().size() != 0) {
                    List<SignStateResponse.DataEntity> data = signStateResponse.getData();
                    if (date.equals(data.get(data.size() - 1).getInTime().substring(0, data.get(data.size() - 1).getInTime().indexOf("T")))) {
                        String currentState = data.get(data.size() - 1).getCurrentState();
                        if ("已签到".equals(currentState)) {
                            tv_sign_state.setText("已签到");
                        } else if ("已签退".equals(currentState)) {
                            tv_sign_state.setText("已签退");
                        }
                    } else {
                        tv_sign_state.setText("未签到");
                    }
                }
            }
        }
//        badge_warning.setBadgeCount(shave + overCheck);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_message:
                go2Message();
                break;
            case R.id.tv_sign:
                go2Sign();
                break;
            case R.id.tv_temporary:
                go2Temporary();
                break;
            case R.id.tv_hydroelectric:

                break;
            case R.id.tv_check_today:
                go2Check(Constants.TODAY);
                break;
            case R.id.tv_check_week:
                go2Check(Constants.WEEK);
                break;
            case R.id.tv_check_month:
                go2Check(Constants.MONTH);
                break;
            case R.id.tv_warning:
                go2Warning();
                break;
            case R.id.tv_maintain:
                go2Maintain();
                break;
            case R.id.tv_upkeep:
                go2keep();
                break;
            case R.id.tv_history:
                go2History();
                break;
            case R.id.tv_synchronization:
                go2Learn();
                break;
            case R.id.more:
                popupWindow.showAsDropDown(iv_more, 0, 20);
                backgroundAlpha(0.8f);
                break;
        }
    }

    private void go2keep() {
        Intent intent = new Intent(mContext,KeepActivity.class);
        startActivity(intent);
    }

    private void go2Learn() {
        Intent intent = new Intent(mContext, LearnTabActivity.class);
        startActivity(intent);
    }

    private void go2Warning() {
        Intent intent = new Intent(mContext, WarningActivity.class);
        startActivity(intent);
    }

    private void go2Syn() {
        Intent intent = new Intent(mContext, SynchronizationActivity.class);
        startActivityForResult(intent, SYNCHRONIZATION);
    }

    private void go2Temporary() {
        Intent intent = new Intent(mContext, TemporaryActivity.class);
        startActivityForResult(intent, TEMPORARY);
    }

    private void go2Message() {
        Intent intent = new Intent(mContext, MessageActivity.class);
        startActivity(intent);
    }

    private void go2History() {
        Intent intent = new Intent(mContext, HistoryActivity.class);
        startActivity(intent);
    }

    private void go2Sign() {
        Intent intent = new Intent(mContext, SignActivity.class);
        startActivityForResult(intent, SIGN_CODE);
    }


    private void go2Check(String type) {
        Intent intent = new Intent(mContext, TaskCheckActivity.class);
        intent.putExtra(Constants.CHECK_TYPE, type);
        startActivityForResult(intent, CHECK);
    }

    private void go2Maintain() {
        Intent intent = new Intent(mContext, MaintainTabActivity.class);
        startActivityForResult(intent, MAINTAIN);
    }
}
