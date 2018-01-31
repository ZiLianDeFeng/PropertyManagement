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
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.MaintenanceActivity;
import com.it.hgad.logisticsmanager.activity.SearchActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.CommitRequest;
import com.it.hgad.logisticsmanager.bean.response.CommitResponse;
import com.it.hgad.logisticsmanager.bean.response.UpLoadPicResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.pager.MaintencePager;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.viewpagerindicator.TabPageIndicator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/12/26.
 */
public class MaintenanceFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private List<Fragment> mFragments = new ArrayList<>();
    private ImageView mSearch;
    private Button close;
    private List<String> menuList = new ArrayList<>();
    private static final int REPAIR = 110;
    private String type = "1";
    private List<String> data = new ArrayList<>();
    private int page;
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private final int SEARCH_REQUEST = 110;
    private List<Order> listdata = new ArrayList<>();
    private int userId;
    private int currentPage;
    private DbUtils db;
    private BroadcastReceiver netReceiver;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.COMMIT_OK)) {
                mPageAdapter.getPrimaryItem().callRefresh();
                mPageAdapter.getPrimaryItem().maintenanceAdapter.notifyDataSetChanged();
            }
        }
    };
    private ViewPagerAdapter mPageAdapter;
    private RelativeLayout rl_no_net;
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_maintenance, null);
//        initFragment();
        initView();
        initData();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registBroadcastReceiver(receiver);
        //创建这个方法，开启广播
        registerNetBroadrecevicer();
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

    @Override
    public void onDetach() {
        super.onDetach();
        mContext.unregisterReceiver(receiver);
        if (netReceiver != null) {
            mContext.unregisterReceiver(netReceiver);
            netReceiver = null;
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
        userId = SPUtils.getInt(getContext(), "userId");
        mTabPageIndicator.setCurrentItem(adapterPage);
    }

//    private boolean isCommiting = false;

    private void Commit() throws DbException {
        boolean have = false;
        final List<CommitDb> commitDbs = db.findAll(CommitDb.class);
        if (commitDbs != null && commitDbs.size() != 0) {
            for (int i = 0; i < commitDbs.size(); i++) {
                CommitDb commitDb = commitDbs.get(i);
                if (commitDb.getHasCommit() == 1 && commitDb.getUserId() == userId) {
                    have = true;
                }
            }
            if (have) {
                Activity activity = (Activity) mView.getContext();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                .setTitle("有之前未提交的维修单")//提示框标题
                                .setPositiveButton("提交",//提示框的两个按钮
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
//                                                事件
//                                                isCommiting = true;
                                                CommonUtils.showToast(mContext, "已在后台提交中，提交过程时间可能比较长");
                                                for (int i = 0; i < commitDbs.size(); i++) {
                                                    final CommitDb commitDb = commitDbs.get(i);
                                                    if (commitDb.getHasCommit() == 1 && commitDb.getUserId() == userId) {
                                                        final int repairId = commitDb.getRepairId();
                                                        ExecutorService poolThread = SingleThreadPool.getPoolThread();
                                                        poolThread.execute(new Runnable() {
                                                            private String handWrite;
                                                            @Override
                                                            public void run() {
                                                                List<String> imgPaths = cast2List(commitDb.getRepairImgPath());
                                                                List<String> imgPathsBefore = cast2List(commitDb.getSpotImg());
                                                                List<String> repairImgPath = new ArrayList<>();
                                                                List<String> repairImgPathBefore = new ArrayList<>();
                                                                if (imgPaths != null && imgPaths.size() != 0) {
                                                                    for (String path : imgPaths) {
                                                                        if (path != null && !("").equals(path)) {
                                                                            File file = new File(path.trim());
                                                                            try {
                                                                                UpLoadPicResponse upLoadPicResponse = upLoadPic(file, repairId);
                                                                                repairImgPath.add(upLoadPicResponse.getPath());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (imgPathsBefore != null && imgPathsBefore.size() != 0) {
                                                                    for (String path : imgPathsBefore) {
                                                                        if (path != null && !("").equals(path)) {
                                                                            File file = new File(path.trim());
                                                                            try {
                                                                                UpLoadPicResponse upLoadPicResponse = upLoadPic(file, repairId);
                                                                                repairImgPathBefore.add(upLoadPicResponse.getPath());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                String satisfy = commitDb.getSatisfy();
//                                                                File file = new File(satisfy);
//                                                                try {
//                                                                    UpLoadPicResponse upLoadPicResponse = upLoadPic(file, repairId);
//                                                                    satisfyPath = upLoadPicResponse.getPath();
//                                                                } catch (IOException e) {
//                                                                    e.printStackTrace();
//                                                                }
                                                                String repairReply = commitDb.getRepairReply();
                                                                List<String> materialIds = cast2List(commitDb.getRepairMaterialId());
                                                                List<String> materialNames = cast2List(commitDb.getRepairMaterialName());
                                                                List<String> materialTypes = cast2List(commitDb.getRepairMaterialType());
                                                                List<String> materialNums = cast2List(commitDb.getRepairMaterialNum());
                                                                List<String> materialPrices = cast2List(commitDb.getRepairMaterialPrice());
                                                                String sameCondition = commitDb.getSameCondition();
                                                                if (sameCondition == null) {
                                                                    sameCondition = "";
                                                                }
                                                                String startTime = commitDb.getStartTime();
                                                                if (startTime == null) {
                                                                    startTime = "";
                                                                }
                                                                String delayTime = commitDb.getDelayTime();
                                                                if (delayTime == null) {
                                                                    delayTime = "";
                                                                }
                                                                String finishTime = commitDb.getFinishTime();
                                                                if (finishTime == null) {
                                                                    finishTime = "";
                                                                }
                                                                String handwritePath = commitDb.getHandWrite();
                                                                if (handwritePath!=null){
                                                                    File file = new File(handwritePath.trim());
                                                                    try {
                                                                        UpLoadPicResponse upLoadPicResponse = upLoadSign(file, repairId);
                                                                        handWrite = upLoadPicResponse.getPath();
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                CommitRequest commitRequest = new CommitRequest(userId + "", repairId + "", repairReply, materialIds, materialNames, materialTypes, materialNums, repairImgPath, repairImgPathBefore, materialPrices, sameCondition, satisfy, delayTime, startTime, finishTime,handWrite);
                                                                sendRequest(commitRequest, CommitResponse.class);

                                                            }
                                                        });
                                                    }
                                                }
//                                                Intent intent = new Intent(mContext,UnCommitActivity.class);
//                                                intent.putExtra(Constants.UNCOMMIT,(Serializable) commitDbs);
//                                                startActivity(intent);
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


    private OkHttpClient client = new OkHttpClient();

    private UpLoadPicResponse upLoadPic(File file, int id) throws IOException {
        String ip = SPUtils.getString(mContext, SPConstants.IP);
        String RequestURL = HttpConstants.HOST + ip + HttpConstants.UPLOADPIC;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("repair.id", id + "");
        builder.addFormDataPart("imgFileName", file.getName());
        builder.addFormDataPart("imgContentType", MaintenanceActivity.MEDIA_TYPE_PNG + "");
        builder.addFormDataPart("img", file.getName(), RequestBody.create(MaintenanceActivity.MEDIA_TYPE_PNG, file));
        MultipartBody body = builder.build();
        Request request = new Request.Builder().url(RequestURL).post(body).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body().string();
        UpLoadPicResponse upLoadPicResponse = null;
        if (responseStr != null) {
            upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
        }
        return upLoadPicResponse;
    }
    private UpLoadPicResponse upLoadSign(File file, int id) throws IOException {
        String ip = SPUtils.getString(mContext, SPConstants.IP);
        String RequestURL = HttpConstants.HOST + ip + HttpConstants.UPLOADSIGN;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("repair.id", id + "");
        builder.addFormDataPart("imgFileName", file.getName());
        builder.addFormDataPart("imgContentType", MaintenanceActivity.MEDIA_TYPE_PNG + "");
        builder.addFormDataPart("img", file.getName(), RequestBody.create(MaintenanceActivity.MEDIA_TYPE_PNG, file));
        MultipartBody body = builder.build();
        Request request = new Request.Builder().url(RequestURL).post(body).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body().string();
        UpLoadPicResponse upLoadPicResponse = null;
        if (responseStr != null) {
            upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
        }
        return upLoadPicResponse;
    }

    private List<String> cast2List(String str) {
        String[] strings = str.split(",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            list.add(strings[i]);
        }
        return list;
    }

    protected void initView() {
        shouldRecieve = SPUtils.getString(mContext, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(mContext, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(mContext, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(mContext, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(mContext, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(mContext, RepairConstants.HAVE_FINISH);
        menuList.add(RepairConstants.ALL);
        menuList.add(RepairConstants.SHOULD_RECIEVE);
        menuList.add(RepairConstants.SHOULD_START);
        menuList.add(RepairConstants.SHOULD_DO);
        menuList.add(RepairConstants.HAVE_SHELVE);
        menuList.add(RepairConstants.HAVE_DONE);
        mViewPager = (ViewPager) mView.findViewById(R.id.maintence_vp);
        mTabPageIndicator = (TabPageIndicator) mView.findViewById(R.id.maintence_tpi);
        mPageAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(menuList.size() - 1);
        mTabPageIndicator.setViewPager(mViewPager);
        mTabPageIndicator.setOnPageChangeListener(mPageListener);
        mSearch = (ImageView) mView.findViewById(R.id.search);
        mSearch.setOnClickListener(this);
        rl_no_net = (RelativeLayout) mView.findViewById(R.id.rl_no_net);
    }

    MaintencePager pager = null;

    private class ViewPagerAdapter extends PagerAdapter {
        MaintencePager currentPager;

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

            if (RepairConstants.ALL.equals(menuList.get(position))) {
                pager = new MaintencePager(container.getContext(), RepairConstants.ALL_REPAIR);
            } else if (RepairConstants.SHOULD_RECIEVE.equals(menuList.get(position))) {
                pager = new MaintencePager(container.getContext(), shouldRecieve);
            } else if (RepairConstants.SHOULD_START.equals(menuList.get(position))) {
                pager = new MaintencePager(container.getContext(), shouldStart);
            } else if (RepairConstants.SHOULD_DO.equals(menuList.get(position))) {
                pager = new MaintencePager(container.getContext(), shouldDo);
            } else if (RepairConstants.HAVE_SHELVE.equals(menuList.get(position))) {
                pager = new MaintencePager(container.getContext(), haveShelve);
            } else if (RepairConstants.HAVE_DONE.equals(menuList.get(position))) {
                pager = new MaintencePager(container.getContext(), haveDone);
            }
//            pager.setOnRefreshListener(MaintenanceFragment.this);
            container.addView(pager,container.getChildCount()>position?position:container.getChildCount());
            return pager;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
            currentPager = (MaintencePager) object;
        }

        public MaintencePager getPrimaryItem() {
            return currentPager;
        }
    }

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.COMMIT_OK);
        mContext.registerReceiver(receiver, intentFilter);
    }

    private int adapterPage = 0;
    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.SimpleOnPageChangeListener() {
//        MaintencePager currentPager;

        @Override
        public void onPageSelected(int position) {
            MaintencePager item = mPageAdapter.getPrimaryItem();
            if (item != null) {
                item.callRefresh();
            }
            adapterPage = position;
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (pager != null) {
//            pager.onResume();
//        }
//        mPageAdapter.getPrimaryItem().callRefresh();
//        boolean checkNetWork = Utils.checkNetWork(mContext);
//        if (checkNetWork) {
////            ExecutorService thread = SingleThreadPool.getThread();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Commit();
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof CommitRequest) {
            CommitResponse commitResponse = (CommitResponse) response;
            if (commitResponse != null) {
                if ("0".equals(commitResponse.getResult())) {
                    int commitId = commitResponse.getId();
                    try {
                        final CommitDb commitDb = db.findById(CommitDb.class, commitId);
                        commitDb.setHasCommit(2);
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(commitDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    mPageAdapter.getPrimaryItem().callRefresh();
                } else {
                }
            }
        }
    }


}
