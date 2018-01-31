package com.it.hgad.logisticsmanager.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.CommitRequest;
import com.it.hgad.logisticsmanager.bean.response.CommitResponse;
import com.it.hgad.logisticsmanager.bean.response.UpLoadPicResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
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
 * Created by Administrator on 2017/5/9.
 */
public class CopyMaintainActivity extends CommonActivity {

    private static final int FEEDBACK = 201;
    private static final int EDIT_CONTENT = 202;
    private Order order;
    private TextView tv_number;
    private TextView tv_from;
    private TextView tv_tel;
    private TextView tv_content;
    private TextView tv_type;
    private TextView tv_regist_time;
    private TextView tv_address;
    private TextView tv_feedback;
    private DbUtils db;
    private int userId;
    private int id;
    private String feedback = "";
    private CustomProgressDialog customProgressDialog;
    private boolean no_net;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_maintain);
        initHeader("新建工单");
        initView();
        initData();
    }

    private void initData() {
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
        String realName = SPUtils.getString(this, SPConstants.REAL_NAME);
        id = order.getId() * 10;
        order.setId(id);
        String repairType = order.getRepairType();
        String registerTime = order.getRegisterTime();
        registerTime = registerTime.replace("T", " ");
        String repairLoc = order.getRepairLoc();
//        tv_number.setText(repairNo);
        tv_from.setText(realName);
//        tv_tel.setText(repairTel);
//        tv_content.setText(repairContent);
//        tv_type.setText(repairType);
        String currentTime = CommonUtils.getCurrentTime();
        tv_regist_time.setText(currentTime);
        tv_address.setText(repairLoc);
        try {
            CommitDb commitDb = db.findById(CommitDb.class, id);
            if (commitDb != null) {
                String repairReply = commitDb.getRepairReply();
                String repairContent = commitDb.getRepairContent();
                if (repairReply != null) {
                    feedback = repairReply;
                }
                if (repairContent != null) {
                    tv_content.setText(repairContent.length() > 3 ? repairContent.substring(0, 3) + "..." : repairContent);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        tv_feedback.setText(feedback.length() > 3 ? feedback.substring(0, 3) + "..." : feedback);
    }

    private void initView() {
        TextView tv_material = (TextView) findViewById(R.id.tv_material);
        tv_material.setOnClickListener(this);
        TextView tv_photo = (TextView) findViewById(R.id.tv_photo);
        tv_photo.setOnClickListener(this);
        TextView tv_evaluate = (TextView) findViewById(R.id.tv_evaluate);
        tv_evaluate.setOnClickListener(this);
        RelativeLayout rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rl_feedback.setOnClickListener(this);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_from = (TextView) findViewById(R.id.tv_from);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        LinearLayout ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_content.setOnClickListener(this);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_regist_time = (TextView) findViewById(R.id.tv_regist_time);
        tv_address = (TextView) findViewById(R.id.tv_address);
        TextView btn_commit = (TextView) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        LinearLayout ll_type = (LinearLayout) findViewById(R.id.ll_type);
        ll_type.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof CommitRequest) {
            CommitResponse commitResponse = (CommitResponse) response;
            if (commitResponse.getResult() != null) {
                if ("0".equals(commitResponse.getResult())) {
                    int id = commitResponse.getId();
                    CommonUtils.showToast(CopyMaintainActivity.this, "提交成功");
                    try {
                        CommitDb commitDb = db.findById(CommitDb.class, id);
                        commitDb.setHasCommit(2);
                        db.saveOrUpdate(commitDb);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    finish();
                } else if ("1".equals(commitResponse.getResult())) {
                    CommonUtils.showToast(CopyMaintainActivity.this, "提交失败");
                }
            }
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
            }
            no_net = false;
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
            case EDIT_CONTENT:
                if (data != null) {
                    String content = data.getStringExtra(Constants.EDIT_CONTENT);
                    tv_content.setText(content.length() > 3 ? content.substring(0, 3) + "..." : content);
                    try {
                        CommitDb nowCommitDb = db.findById(CommitDb.class, id);
                        if (nowCommitDb == null) {
                            nowCommitDb = new CommitDb();
                            nowCommitDb.setRepairId(id);
                        }
                        nowCommitDb.setRepairContent(content);
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
    public void back(View view) {
        backWarm();
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_material:
                go2Material();
                break;
            case R.id.tv_photo:
                go2Phpto();
                break;
            case R.id.tv_evaluate:
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
            case R.id.ll_type:
                chooseEventType();
                break;
            case R.id.ll_content:
                editContent();
                break;
        }
    }

    private void editContent() {
        Intent intent = new Intent(this, EditContentActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivityForResult(intent, EDIT_CONTENT);
    }

    private String[] eventTypes = {"水", "电", "气", "门窗", "其他"};

    private void chooseEventType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("事件类型");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(eventTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_type.setText(eventTypes[which]);
            }
        });
        builder.show();
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
            if (repairReply == null || satisfy == null) {
                CommonUtils.showToast(CopyMaintainActivity.this, "反馈内容和科室评价不能为空哦！");
                return;
            }
            no_net = true;
            boolean isNetWork = Utils.checkNetWork(CopyMaintainActivity.this);
            if (isNetWork) {
                customProgressDialog = new CustomProgressDialog(CopyMaintainActivity.this, "数据提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
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
                                    CommonUtils.showToast(CopyMaintainActivity.this, "网络异常");
                                }
                            }
                        }, 60000);
                    }
                });
            } else {
//                commitDb.setHasCommit(1);
//                commitDb.setUserId(userId);
//                commitDb.setRepairNo(order.getRepairNo());
//                commitDb.setFinishTime(currentTime);
//                db.saveOrUpdate(commitDb);
//                Utils.showTost(CopyMaintainActivity.this, getString(R.string.no_net) + "\n已将数据存入本地，之后可进行提交");
//                finish();
            }
        } else {
            CommonUtils.showToast(CopyMaintainActivity.this, "您还没有填写任何维修信息哦！");
        }
    }

    private OkHttpClient client = new OkHttpClient();

    private UpLoadPicResponse upLoadPic(File file, int id) throws IOException {
        String ip = SPUtils.getString(CopyMaintainActivity.this, SPConstants.IP);
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
        String ip = SPUtils.getString(CopyMaintainActivity.this, SPConstants.IP);
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

    private void feedback() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivityForResult(intent, FEEDBACK);
    }

    private void go2Evaluate() {
        Intent intent = new Intent(this, EvaluateActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }

    private void go2Phpto() {
        Intent intent = new Intent(this, MaintainPhotoActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }

    private void go2Material() {
        Intent intent = new Intent(this, MaintainMaterialActivity.class);
        intent.putExtra(Constants.ORDER_DATA, order);
        startActivity(intent);
    }
}
