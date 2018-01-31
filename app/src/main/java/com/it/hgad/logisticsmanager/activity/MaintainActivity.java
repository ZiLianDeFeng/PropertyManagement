package com.it.hgad.logisticsmanager.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.CommitRequest;
import com.it.hgad.logisticsmanager.bean.request.RecieveRequest;
import com.it.hgad.logisticsmanager.bean.request.StartRepairRequest;
import com.it.hgad.logisticsmanager.bean.response.CommitResponse;
import com.it.hgad.logisticsmanager.bean.response.RecieveResponse;
import com.it.hgad.logisticsmanager.bean.response.StartRepairResponse;
import com.it.hgad.logisticsmanager.bean.response.UpLoadPicResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

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
 * Created by Administrator on 2017/4/12.
 */
public class MaintainActivity extends CommonActivity {
    private static final int FEEDBACK = 110;
    private LinearLayout ll_more;
    private PopupWindow popupWindow;
    private TextView tv_number;
    private TextView tv_from;
    private TextView tv_tel;
    private TextView tv_content;
    private DbUtils db;
    private int userId;
    private int id;
    private TextView tv_type;
    private TextView tv_regist_time;
    private TextView tv_address;
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;
    private Order order;
    private CustomProgressDialog customProgressDialog;
    private boolean no_net;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.SHELVE_OK)) {
                finish();
            }
        }
    };
    private TextView tv_feedback;
    private boolean haveStart = false;
    private int repairFlag;
    private ImageView iv_material;
    private ImageView iv_photo;
    private ImageView iv_evaluation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain);
        initHeader("维修任务");
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private void initData() {
        registerReceiver();
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER_DATA);
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(CommitDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        repairFlag = order.getRepairFlag();
        String repairNo = order.getRepairNo();
        String repairMan = order.getRepairMan();
        String repairTel = order.getRepairTel();
        String repairContent = order.getRepairContent();
        id = order.getId();
        String repairType = order.getRepairType();
        String registerTime = order.getRegisterTime();
        registerTime = registerTime.replace("T", " ");
        String repairLoc = order.getRepairLoc();
        tv_number.setText(repairNo);
        tv_from.setText(repairMan);
        tv_tel.setText(repairTel);
        tv_content.setText(repairContent);
        tv_type.setText(repairType);
        tv_regist_time.setText(registerTime);
        tv_address.setText(repairLoc);
        try {
            CommitDb commitDb = db.findById(CommitDb.class, id);
            OrderDb orderDb = db.findById(OrderDb.class, id);
            repairFlag = orderDb.getRepairFlag();
            if (commitDb != null) {
                String repairReply = commitDb.getRepairReply();
                if (repairReply != null) {
                    feedback = repairReply;
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        checkFlag(repairFlag);
        tv_feedback.setText(feedback.length() > 3 ? feedback.substring(0, 3) + "..." : feedback);
        boolean checkNetWork = Utils.checkNetWork(MaintainActivity.this);
//        if (checkNetWork) {
//            JobDetailRequest jobDetailRequest = new JobDetailRequest(order.getId() + "");
//            sendRequest(jobDetailRequest, JobDetailResponse.class);
//        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.SHELVE_OK);
        registerReceiver(receiver, intentFilter);
    }

    private void recieveOrder() {
        String currentTime = CommonUtils.getCurrentTime();
        boolean checkNetWork = Utils.checkNetWork(this);
        if (checkNetWork) {
            RecieveRequest recieveRequest = new RecieveRequest(id + "", currentTime, userId + "");
            sendRequest(recieveRequest, RecieveResponse.class);
        } else {
            try {
                CommitDb commitDb = db.findById(CommitDb.class, id);
                if (commitDb == null) {
                    commitDb = new CommitDb();
                    commitDb.setRepairId(id);
                }
                commitDb.setRecieveTime(currentTime);
                commitDb.setStartTime(currentTime);
                ExecutorService thread = SingleThreadPool.getThread();
                final CommitDb finalNowCommitDb = commitDb;
                thread.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.saveOrUpdate(finalNowCommitDb);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkFlag(int repairFlag) {
        if (shouldRecieve.equals(repairFlag + "")) {
            recieveOrder();
        } else if (shouldStart.equals(repairFlag + "")) {
//            showStartDialog();
//            haveStart = true;
        } else if (shouldDo.equals(repairFlag + "")) {
            haveStart = true;
        }
    }

    private void showStartDialog() {
        startRepair();
    }

    private void startRepair() {
        String currentTime = CommonUtils.getCurrentTime();
        boolean checkNetWork = Utils.checkNetWork(MaintainActivity.this);
        if (checkNetWork) {
            StartRepairRequest startRepairRequest = new StartRepairRequest(order.getId() + "", "", currentTime, "");
            sendRequest(startRepairRequest, StartRepairResponse.class);
        } else {
            try {
                CommitDb commitDb = db.findById(CommitDb.class, id);
                if (commitDb == null) {
                    commitDb = new CommitDb();
                    commitDb.setRepairId(id);
                }
                commitDb.setStartTime(currentTime);
                ExecutorService thread = SingleThreadPool.getThread();
                final CommitDb finalNowCommitDb = commitDb;
                thread.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.saveOrUpdate(finalNowCommitDb);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        shouldRecieve = SPUtils.getString(this, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(this, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(this, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(this, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(this, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(this, RepairConstants.HAVE_FINISH);
        ImageView iv_more = (ImageView) findViewById(R.id.search);
        iv_more.setImageResource(R.mipmap.and);
        ll_more = (LinearLayout) findViewById(R.id.ll_search);
        ll_more.setVisibility(View.VISIBLE);
        ll_more.setOnClickListener(this);
        initPopupWindow();
//        TextView tv_abnormal = (TextView) findViewById(R.id.tv_abnormal);
//        tv_abnormal.setOnClickListener(this);
        LinearLayout ll_material = (LinearLayout) findViewById(R.id.ll_material);
        ll_material.setOnClickListener(this);
        LinearLayout ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        ll_photo.setOnClickListener(this);
        LinearLayout ll_evaluate = (LinearLayout) findViewById(R.id.ll_evaluate);
        ll_evaluate.setOnClickListener(this);
        RelativeLayout rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rl_feedback.setOnClickListener(this);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_from = (TextView) findViewById(R.id.tv_from);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_regist_time = (TextView) findViewById(R.id.tv_regist_time);
        tv_address = (TextView) findViewById(R.id.tv_address);
        TextView btn_commit = (TextView) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        iv_material = (ImageView) findViewById(R.id.iv_material);
        iv_photo = (ImageView) findViewById(R.id.iv_picture);
        iv_evaluation = (ImageView) findViewById(R.id.iv_evaluation);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof CommitRequest) {
            CommitResponse commitResponse = (CommitResponse) response;
            if (commitResponse.getResult() != null) {
                if ("0".equals(commitResponse.getResult())) {
                    int id = commitResponse.getId();
                    CommonUtils.showToast(MaintainActivity.this, "提交成功");
                    try {
                        CommitDb commitDb = db.findById(CommitDb.class, id);
                        commitDb.setHasCommit(2);
                        OrderDb orderDb = db.findById(OrderDb.class, id);
                        orderDb.setRepairFlag(Integer.parseInt(haveDone));
                        db.saveOrUpdate(commitDb);
                        db.saveOrUpdate(orderDb);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    finish();
                    sendBroadcast();
//                onRefreshListener.refresh();
                } else if ("1".equals(commitResponse.getResult())) {
                    CommonUtils.showToast(MaintainActivity.this, "提交失败");
                }
            }
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
            }
            no_net = false;
        } else if (request instanceof RecieveRequest) {
            RecieveResponse recieveResponse = (RecieveResponse) response;
            if (recieveResponse != null) {
                if ("0".equals(recieveResponse.getResult())) {
//                    startRepair();
                }
            }
        } else if (request instanceof StartRepairRequest) {
            StartRepairResponse startRepairResponse = (StartRepairResponse) response;
            if (startRepairResponse.getResult() != null) {
                if ("0".equals(startRepairResponse.getResult())) {
//                    startRepair();
                    haveStart = true;
                    try {
                        OrderDb orderDb = db.findById(OrderDb.class, id);
                        orderDb.setRepairFlag(Integer.parseInt(shouldDo));
                        db.saveOrUpdate(orderDb);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
//        }else if (request instanceof JobDetailRequest) {
//            JobDetailResponse jobDetailResponse = (JobDetailResponse) response;
//            if (jobDetailResponse.getData() != null) {
//                for (JobDetailResponse.DataEntity entity : jobDetailResponse.getData()) {
//                    JobDetailResponse.DataEntity.RepairEntity repairEntity = entity.getRepair();
//                    repairFlag = repairEntity.getRepairFlag();
//                    break;
//                }
//                checkFlag(repairFlag);
//            }
        }
    }


    private void sendBroadcast() {
        Intent intent = new Intent(Constants.COMMIT_OK);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            CommitDb commitDb = db.findById(CommitDb.class, id);
            if (commitDb != null) {
                String repairMaterialId = commitDb.getRepairMaterialId();
                if (repairMaterialId != null) {
                    List<String> ids = CommonUtils.cast2List(repairMaterialId);
                    iv_material.setImageResource(ids.size() == 0 ? R.mipmap.material : ids.get(0) == "" ? R.mipmap.material : R.mipmap.complete);
                }
                String repairImgPath = commitDb.getRepairImgPath();
                String spotImg = commitDb.getSpotImg();
                if ((repairImgPath == null || "".equals(repairImgPath)) && (spotImg == null || "".equals(spotImg))) {
                    iv_photo.setImageResource(R.mipmap.picture);
                } else {
                    iv_photo.setImageResource(R.mipmap.complete);
                }
                String satisfy = commitDb.getSatisfy();
                if (satisfy == null || "".equals(satisfy)) {
                    iv_evaluation.setImageResource(R.mipmap.evaluation);
                } else {
                    iv_evaluation.setImageResource(R.mipmap.complete);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FEEDBACK:
                if (data != null) {
                    feedback = data.getStringExtra(Constants.FEEDBACK);
                    tv_feedback.setText(feedback.length() > 3 ? feedback.substring(0, 3) + "..." : feedback);
                    try {
                        CommitDb nowCommitDb = db.findById(CommitDb.class, id);
                        if (nowCommitDb == null) {
                            nowCommitDb = new CommitDb();
                            nowCommitDb.setRepairId(id);
                        }
                        nowCommitDb.setRepairReply(feedback);
                        ExecutorService thread = SingleThreadPool.getThread();
                        final CommitDb finalNowCommitDb = nowCommitDb;
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(finalNowCommitDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                popupWindow.showAsDropDown(ll_more);
                backgroundAlpha(0.8f);
                break;
            case R.id.ll_abnormal:
                popupWindow.dismiss();
                go2Abnomal();
                break;
            case R.id.ll_material:
                go2Material();
                break;
            case R.id.ll_photo:
                go2Phpto();
                break;
            case R.id.ll_evaluate:
                go2Evaluate();
                break;
            case R.id.rl_feedback:
                feedback();
                break;
            case R.id.btn_commit:
                try {
                    commit();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_shelve:
                popupWindow.dismiss();
                shelve();
                break;
            case R.id.ll_copy_maintain:
                popupWindow.dismiss();
                copy();
                break;
        }
    }

    private void copy() {
//        CommonUtils.showToast(this, Constants.FOR_WAIT);
        Intent intent = new Intent(MaintainActivity.this, CopyMaintainActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }

    private void shelve() {
        if (haveStart) {
            CommonUtils.showToast(this, "开始维修后无法进行挂起");
        } else {
            Intent intent = new Intent(MaintainActivity.this, ShelveActivity.class);
            intent.putExtra(Constants.ORDER_DATA, order);
            startActivity(intent);
        }
    }

    private void commit() throws DbException {
        final String currentTime = CommonUtils.getCurrentTime();
        final CommitDb commitDb = db.findById(CommitDb.class, id);
        if (commitDb != null) {
            final String satisfy = commitDb.getSatisfy();
            final String spotImg = commitDb.getSpotImg();
            final String repairImgPath = commitDb.getRepairImgPath();
            final String handWrite = commitDb.getHandWrite();
            final String repairReply = commitDb.getRepairReply();
            if (repairReply == null || "".equals(repairReply)) {
                CommonUtils.showToast(MaintainActivity.this, "还没有填写反馈内容哦！");
                return;
            }
            if (satisfy == null || "".equals(satisfy)) {
                CommonUtils.showToast(MaintainActivity.this, "还没有填写科室评价哦！");
                return;
            }

            no_net = true;
            boolean isNetWork = Utils.checkNetWork(MaintainActivity.this);
            if (isNetWork) {
                customProgressDialog = new CustomProgressDialog(MaintainActivity.this, "数据提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
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
                ExecutorService poolThread = SingleThreadPool.getPoolThread();
                poolThread.execute(new Runnable() {

                    private List<String> imgPathsBefore = new ArrayList<String>();
                    private List<String> imgPaths = new ArrayList<String>();
                    private List<String> materialPrices = new ArrayList<String>();
                    private List<String> materialNums = new ArrayList<String>();
                    private List<String> materialTypes = new ArrayList<String>();
                    private List<String> materialNames = new ArrayList<String>();
                    private List<String> materialIds = new ArrayList<String>();
                    private String handWritePath = "";

                    @Override
                    public void run() {
                        if (repairImgPath != null) {
                            imgPaths = cast2List(repairImgPath);
                        }
                        if (spotImg != null) {
                            imgPathsBefore = cast2List(spotImg);
                        }
                        List<String> repairImgPath = new ArrayList<>();
                        List<String> repairImgPathBefore = new ArrayList<>();
                        if (imgPaths != null && imgPaths.size() != 0) {
                            for (String path : imgPaths) {
                                if (path != null && !("").equals(path)) {
                                    File file = new File(path.trim());
                                    try {
                                        UpLoadPicResponse upLoadPicResponse = upLoadPic(file, id);
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
                                        UpLoadPicResponse upLoadPicResponse = upLoadPic(file, id);
                                        repairImgPathBefore.add(upLoadPicResponse.getPath());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        if (commitDb.getRepairMaterialId() != null) {
                            materialIds = cast2List(commitDb.getRepairMaterialId());
                            materialNames = cast2List(commitDb.getRepairMaterialName());
                            materialTypes = cast2List(commitDb.getRepairMaterialType());
                            materialNums = cast2List(commitDb.getRepairMaterialNum());
                            materialPrices = cast2List(commitDb.getRepairMaterialPrice());
                        }
                        String sameCondition = commitDb.getSameCondition();
                        if (sameCondition == null) {
                            sameCondition = "";
                        }
                        if (handWrite != null) {
                            File file = new File(handWrite.trim());
                            try {
                                UpLoadPicResponse upLoadPicResponse = upLoadSign(file, id);
                                handWritePath = upLoadPicResponse.getPath();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        CommitRequest commitRequest = new CommitRequest(userId + "", id + "", repairReply, materialIds, materialNames, materialTypes, materialNums, repairImgPath, repairImgPathBefore, materialPrices, sameCondition, satisfy, "", "", currentTime, handWritePath);
                        sendRequest(commitRequest, CommitResponse.class);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (no_net) {
                                    if (customProgressDialog != null) {
                                        customProgressDialog.dismiss();
                                    }
                                    CommonUtils.showToast(MaintainActivity.this, "网络异常");
                                }
                            }
                        }, 60000);
                    }
                });
            } else {
                commitDb.setHasCommit(1);
                commitDb.setUserId(userId);
                commitDb.setRepairNo(order.getRepairNo());
                commitDb.setFinishTime(currentTime);
                db.saveOrUpdate(commitDb);
                Utils.showTost(MaintainActivity.this, getString(R.string.no_net) + "\n已将数据存入本地，之后可进行提交");
                finish();
                sendBroadcast();
            }
        } else {
            CommonUtils.showToast(MaintainActivity.this, "您还没有填写任何维修信息哦！");
        }
    }

    private OkHttpClient client = new OkHttpClient();

    private UpLoadPicResponse upLoadPic(File file, int id) throws IOException {
        String ip = SPUtils.getString(MaintainActivity.this, SPConstants.IP);
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
        String ip = SPUtils.getString(MaintainActivity.this, SPConstants.IP);
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

    private String feedback = "";

    private void feedback() {
        if (!haveStart) {
            startRepair();
        }
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivityForResult(intent, FEEDBACK);
    }

    private void go2Evaluate() {
        if (!haveStart) {
            startRepair();
        }
        Intent intent = new Intent(this, EvaluateActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }

    private void go2Phpto() {
        if (!haveStart) {
            startRepair();
        }
        Intent intent = new Intent(this, MaintainPhotoActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }

    private void go2Material() {
        if (!haveStart) {
            startRepair();
        }
        Intent intent = new Intent(this, MaintainMaterialActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }

    private void go2Abnomal() {
        Intent intent = new Intent(this, AbnomalActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }

    private void initPopupWindow() {
        View contentView = View.inflate(MaintainActivity.this, R.layout.popup_maintain, null);
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
        LinearLayout ll_shelve = (LinearLayout) contentView.findViewById(R.id.ll_shelve);
        ll_shelve.setOnClickListener(this);
        LinearLayout ll_abnormal = (LinearLayout) contentView.findViewById(R.id.ll_abnormal);
        ll_abnormal.setOnClickListener(this);
        LinearLayout ll_copy_maintain = (LinearLayout) contentView.findViewById(R.id.ll_copy_maintain);
        ll_copy_maintain.setOnClickListener(this);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }
}
