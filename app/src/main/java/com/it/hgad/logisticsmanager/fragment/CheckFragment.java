package com.it.hgad.logisticsmanager.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.request.CancelCheckRequest;
import com.it.hgad.logisticsmanager.bean.request.ParamCommitRequest;
import com.it.hgad.logisticsmanager.bean.response.CancelCheckResponse;
import com.it.hgad.logisticsmanager.bean.response.ParamCommitResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CancelCheckDb;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.pager.CheckPager;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;


/**
 * Created by Administrator on 2016/12/26.
 */
public class CheckFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private List<String> menuList = new ArrayList<>();
//    private CheckPager pager = null;
    private DbUtils db;
    private BroadcastReceiver netReceiver;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.PARAM_COMMIT_OK)) {
                pagerAdapter.getPrimaryItem().callRefresh();
            }
        }
    };
    private ViewPagerAdapter pagerAdapter;
    private ImageView search;
    private int userId;
    private RelativeLayout rl_no_net;
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_check, null);
        initView();
        initData();
        return mView;
    }

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PARAM_COMMIT_OK);
        mContext.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registBroadcastReceiver(receiver);
        registerNetBroadrecevicer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext.unregisterReceiver(receiver);
        if (netReceiver != null) {
            mContext.unregisterReceiver(netReceiver);
            netReceiver = null;
        }
    }

    private void registerNetBroadrecevicer() {
        //获取广播对象
        netReceiver = new IntenterBoradCastReceiver();
        //创建意图过滤器
        IntentFilter filter = new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(netReceiver, filter);
    }

    //监听网络状态变化的广播接收器
    public class IntenterBoradCastReceiver extends BroadcastReceiver {

        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络

                    }
                    rl_no_net.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Commit();
                                CommitCancelCheck();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    ////////网络断开
//                    Toast.makeText(mContext, "无网络", 0).show();
                    rl_no_net.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void CommitCancelCheck() throws DbException {
        boolean have = false;
        List<CancelCheckDb> cancelCheckDbs = db.findAll(Selector.from(CancelCheckDb.class).where(WhereBuilder.b("hasCommit", "=", 1)));
        if (cancelCheckDbs != null && cancelCheckDbs.size() != 0) {
            for (final CancelCheckDb cancelCheckDb : cancelCheckDbs) {
                int taskId = cancelCheckDb.getTaskId();
                CancelCheckRequest cancelCheckRequest = new CancelCheckRequest(taskId + "");
                sendRequest(cancelCheckRequest, CancelCheckResponse.class);
                cancelCheckDb.setHasCommit(2);
                ExecutorService thread = SingleThreadPool.getThread();
                thread.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.saveOrUpdate(cancelCheckDb);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    protected void initData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (!checkNetWork) {
            rl_no_net.setVisibility(View.VISIBLE);
        } else {
            rl_no_net.setVisibility(View.GONE);
        }
        if (db == null) {
            db = LocalApp.getDb();
        }
        userId = SPUtils.getInt(getContext(), SPConstants.USER_ID);
        mTabPageIndicator.setCurrentItem(adapterPage);
    }

    private void Commit() throws DbException {
        boolean have = false;
        final List<CheckCommitDb> commitDbs = db.findAll(CheckCommitDb.class);
        if (commitDbs != null && commitDbs.size() != 0) {
            for (int i = 0; i < commitDbs.size(); i++) {
                CheckCommitDb checkCommitDb = commitDbs.get(i);
                if (checkCommitDb.getHasCommit() == 1 && checkCommitDb.getUserId() == userId) {
                    have = true;
                }
            }
            if (have) {
                Activity activity = (Activity) mView.getContext();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                .setTitle("有之前未提交的巡检单")//提示框标题
                                .setPositiveButton("提交",//提示框的两个按钮
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
//                                                事件
                                                for (int i = 0; i < commitDbs.size(); i++) {
                                                    final CheckCommitDb checkCommitDb = commitDbs.get(i);
                                                    if (checkCommitDb.getHasCommit() == 1 && checkCommitDb.getUserId() == userId) {
                                                        int taskId = checkCommitDb.getTaskId();

                                                        List<String> actualValues = Arrays.asList(checkCommitDb.getActualValues());
                                                        String feedback = checkCommitDb.getFeedback();
                                                        String finishTime = checkCommitDb.getFinishTime();
                                                        ParamCommitRequest commitRequest = new ParamCommitRequest(userId + "", taskId + "", actualValues, feedback, finishTime);
                                                        sendRequest(commitRequest, ParamCommitResponse.class);

                                                    }
                                                }
                                            }
                                        }).setNegativeButton("取消", null).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                });
            }
        }
    }
    protected void initView() {
        should = SPUtils.getString(mContext, CheckConstants.SHOULD);
        done = SPUtils.getString(mContext, CheckConstants.DONE);
        over = SPUtils.getString(mContext, CheckConstants.OVER);
        overFinish = SPUtils.getString(mContext, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(mContext, CheckConstants.CANCEL);
        menuList.add(CheckConstants.ALL);
        menuList.add(CheckConstants.SHOULD);
        menuList.add(CheckConstants.DONE);
        menuList.add(CheckConstants.OVER);
        menuList.add(CheckConstants.OVER_FINISH);
        mViewPager = (ViewPager) mView.findViewById(R.id.check_vp);
        mTabPageIndicator = (TabPageIndicator) mView.findViewById(R.id.check_tpi);
        search = (ImageView) mView.findViewById(R.id.search);
        search.setOnClickListener(this);
        pagerAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(menuList.size() - 1);
        mTabPageIndicator.setOnPageChangeListener(mPageListener);
        mTabPageIndicator.setViewPager(mViewPager);
        rl_no_net = (RelativeLayout) mView.findViewById(R.id.rl_no_net);
//        mTabPageIndicator.setOnPageChangeListener(mPageListener);
    }

//    private List<CheckPager> pagers = new ArrayList<>();

    private class ViewPagerAdapter extends PagerAdapter {
        CheckPager pager = null;
        CheckPager currentPager;

        @Override
        public int getCount() {
            return menuList == null ? 0 : menuList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menuList.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (CheckConstants.ALL.equals(menuList.get(position))) {
                pager = new CheckPager(container.getContext(), CheckConstants.ALL_CHECK);
            } else if (CheckConstants.SHOULD.equals(menuList.get(position))) {
                pager = new CheckPager(container.getContext(), should);
            } else if (CheckConstants.DONE.equals(menuList.get(position))) {
                pager = new CheckPager(container.getContext(), done);
            } else if (CheckConstants.OVER.equals(menuList.get(position))) {
                pager = new CheckPager(container.getContext(), over);
            } else if (CheckConstants.OVER_FINISH.equals(menuList.get(position))) {
                pager = new CheckPager(container.getContext(), overFinish);
            }
//            pager.setOnRefreshListener(MaintenanceFragment.this);
//            pager = pagers.get(position);
//            container.addView(pager);

            container.addView(pager,0);
            return pager;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentPager = ((CheckPager) object);
        }

        public CheckPager getPrimaryItem() {
            return currentPager;
        }
    }

    private int adapterPage = 0;
    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.SimpleOnPageChangeListener() {
//        MaintencePager currentPager;

        @Override
        public void onPageSelected(int position) {
//            if (position != 0) {
//                pager.onPause();
//            }
//            if (pager!=null) {
//                pager.callRefresh();
//            }
            CheckPager item = pagerAdapter.getPrimaryItem();
            if (item != null) {
                item.callRefresh();
            }
            adapterPage = position;
//            pager = pagers.get(position);
//             position -> NewsCategory -> BasePager
//            NewsCategory key = mShowCategories.get(position);
//            MaintencePager pager = pagers.get(key);
//            pager.onResume();
//            currentPager = pager;
        }
    };


    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof ParamCommitRequest) {
            ParamCommitResponse paramCommitResponse = (ParamCommitResponse) response;
            if (paramCommitResponse != null) {
                if ("0".equals(paramCommitResponse.getResult())) {
                    int taskId = paramCommitResponse.getId();
                    try {
                        final CheckCommitDb checkCommitDb = db.findById(CheckCommitDb.class, taskId);
                        checkCommitDb.setHasCommit(2);
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(checkCommitDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    pagerAdapter.getPrimaryItem().callRefresh();
                } else {
                }
            }
        } else if (request instanceof CancelCheckRequest) {
            CancelCheckResponse cancelCheckResponse = (CancelCheckResponse) response;
            if (cancelCheckResponse != null) {
                if ("0".equals(cancelCheckResponse.getResult())) {
                    pagerAdapter.getPrimaryItem().callRefresh();
                } else {
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (pager != null) {
//            pager.onResume();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
