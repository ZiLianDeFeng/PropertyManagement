package com.it.hgad.logisticsmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.MaintenanceActivity;
import com.it.hgad.logisticsmanager.activity.UnCommitActivity;
import com.it.hgad.logisticsmanager.activity.UnCommitCheckActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.request.CommitRequest;
import com.it.hgad.logisticsmanager.bean.request.ParamCommitRequest;
import com.it.hgad.logisticsmanager.bean.response.CommitResponse;
import com.it.hgad.logisticsmanager.bean.response.ParamCommitResponse;
import com.it.hgad.logisticsmanager.bean.response.UpLoadPicResponse;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.itcast.ecshop.net.NetUtil;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/23.
 */
public class CommitAdapter extends BaseAdapter implements Callback {
    private static final int START_COMMIT = 1;
    private Context context;
    private List<? extends Object> listdata;
    private int userId;
    private DbUtils db;
    private String type;
    private MaintenanceAdapter.AdapterRefreshListener listener;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_COMMIT:
                    if (listener != null) {
                        listener.startCommit();
                    }
                    break;
            }
        }
    };
    private String haveDone;
    private final String done;

    public MaintenanceAdapter.AdapterRefreshListener getListener() {
        return listener;
    }

    public void setListener(MaintenanceAdapter.AdapterRefreshListener listener) {
        this.listener = listener;
    }

    public CommitAdapter(Context context, List<? extends Object> listdata, String type) {
        this.context = context;
        this.listdata = listdata;
        this.type = type;
        userId = SPUtils.getInt(context, SPConstants.USER_ID);
        haveDone = SPUtils.getString(context, RepairConstants.HAVE_DONE);
        done = SPUtils.getString(context, CheckConstants.DONE);
        if (db == null) {
            db = LocalApp.getDb();
        }
    }

    @Override
    public int getCount() {
        return listdata == null ? 0 : listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_commit);
        TextView tv_number = holder.getTv(R.id.tv_order_number);
        TextView tv_open = holder.getTv(R.id.tv_open);
        TextView tv_type = holder.getTv(R.id.tv_type);
        if (type.equals(UnCommitActivity.MAINTANCE_UNCOMMIT)) {
            final CommitDb commitDb = (CommitDb) listdata.get(position);
            final int repairId = commitDb.getRepairId();
            tv_type.setText(context.getString(R.string.order_number));
            tv_number.setText(commitDb.getRepairNo());
            tv_open.setOnClickListener(new View.OnClickListener() {

                private List<String> materialPrices = new ArrayList<String>();
                private List<String> materialNums = new ArrayList<String>();
                private List<String> materialTypes = new ArrayList<String>();
                private List<String> materialNames = new ArrayList<String>();
                private List<String> materialIds = new ArrayList<String>();

                @Override
                public void onClick(View v) {
//                    try {
//                        OrderDb orderDb = db.findById(OrderDb.class, repairId);
//                        Order order = new Order();
//                        order.setData(orderDb);
//                        Intent intent = new Intent(context, MaintainActivity.class);
//                        intent.putExtra(Constants.ORDER_DATA, (Serializable) order);
//                        context.startActivity(intent);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle("提示")//提示框标题
                            .setMessage("确定提交该任务？")
                            .setPositiveButton("确定",//提示框的两个按钮
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            final int repairId = commitDb.getRepairId();
                                            ExecutorService poolThread = SingleThreadPool.getPoolThread();
                                            poolThread.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    boolean checkNetWork = Utils.checkNetWork(context);
                                                    if (checkNetWork) {
                                                        handler.sendEmptyMessage(START_COMMIT);
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
                                                        String handWrite = commitDb.getHandWrite();
                                                        String signImg = "";
                                                        if (handWrite != null && !"".equals(handWrite)) {
                                                            File file = new File(handWrite.trim());
                                                            try {
                                                                UpLoadPicResponse upLoadPicResponse = upLoadSign(file, repairId);
                                                                signImg = upLoadPicResponse.getPath();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        String repairReply = commitDb.getRepairReply();
                                                        materialIds = cast2List(commitDb.getRepairMaterialId());
                                                        materialNames = cast2List(commitDb.getRepairMaterialName());
                                                        materialTypes = cast2List(commitDb.getRepairMaterialType());
                                                        materialNums = cast2List(commitDb.getRepairMaterialNum());
                                                        materialPrices = cast2List(commitDb.getRepairMaterialPrice());
                                                        String sameCondition = commitDb.getSameCondition();
                                                        if (sameCondition == null) {
                                                            sameCondition = "";
                                                        }
                                                        String satisfy = commitDb.getSatisfy();
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
                                                        CommitRequest commitRequest = new CommitRequest(userId + "", repairId + "", repairReply, materialIds, materialNames, materialTypes, materialNums, repairImgPath, repairImgPathBefore, materialPrices, sameCondition, satisfy, delayTime, startTime, finishTime, signImg);
                                                        NetUtil.sendRequest(commitRequest, CommitResponse.class, CommitAdapter.this);
                                                    } else {
                                                        CommonUtils.showToast(context, context.getString(R.string.no_net));
                                                    }
                                                }
                                            });
                                        }
                                    }).setNegativeButton("取消", null)
                            .create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            });
        } else if (type.equals(UnCommitCheckActivity.CHECK_UNCOMMIT)) {
            final CheckCommitDb checkCommitDb = (CheckCommitDb) listdata.get(position);
            tv_type.setText(context.getString(R.string.check_number));
            tv_number.setText(checkCommitDb.getCheckNo());
            tv_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle("提示")//提示框标题
                            .setMessage("确定提交该任务？")
                            .setPositiveButton("确定",//提示框的两个按钮
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            ExecutorService poolThread = SingleThreadPool.getPoolThread();
                                            poolThread.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    boolean checkNetWork = Utils.checkNetWork(context);
                                                    if (checkNetWork) {
                                                        handler.sendEmptyMessage(START_COMMIT);
                                                        int taskId = checkCommitDb.getTaskId();
                                                        int resultId = checkCommitDb.getResultId();
                                                        List<String> actualValues = Arrays.asList(checkCommitDb.getActualValues());
                                                        String feedback = checkCommitDb.getFeedback();
                                                        String finishTime = checkCommitDb.getFinishTime();
                                                        String paramId = checkCommitDb.getParamId();
                                                        ParamCommitRequest commitRequest = new ParamCommitRequest(userId + "", resultId + "", actualValues, feedback, finishTime, paramId);
                                                        NetUtil.sendRequest(commitRequest, ParamCommitResponse.class, CommitAdapter.this);
                                                    } else {
                                                        CommonUtils.showToast(context, context.getString(R.string.no_net));
                                                    }
                                                }
                                            });
                                        }
                                    }).setNegativeButton("取消", null)
                            .create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            });
        }
        return holder.convertView;
    }

    private List<String> cast2List(String str) {
        List<String> list = new ArrayList<>();
        if (str != null) {
            String[] strings = str.split(",");
            for (int i = 0; i < strings.length; i++) {
                list.add(strings[i]);
            }
        }
        return list;
    }

    private OkHttpClient client = new OkHttpClient();

    private UpLoadPicResponse upLoadPic(File file, int id) throws IOException {
        String ip = SPUtils.getString(context, SPConstants.IP);
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
        String ip = SPUtils.getString(context, SPConstants.IP);
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

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof CommitRequest) {
            CommitResponse commitResponse = (CommitResponse) response;
            if (commitResponse.getResult() != null) {
                if ("0".equals(commitResponse.getResult())) {
                    int commitId = commitResponse.getId();
                    try {
                        final CommitDb commitDb = db.findById(CommitDb.class, commitId);
                        commitDb.setHasCommit(2);
                        final OrderDb orderDb = db.findById(OrderDb.class, commitId);
                        orderDb.setRepairFlag(Integer.parseInt(haveDone));
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(commitDb);
                                    db.saveOrUpdate(orderDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    CommonUtils.showToast(context, "提交成功");
                } else {
                    CommonUtils.showToast(context, "提交失败");
                }
                if (listener != null) {
                    listener.forRefresh();
                }
            }
        } else if (request instanceof ParamCommitRequest) {
            ParamCommitResponse paramCommitResponse = (ParamCommitResponse) response;
            if (paramCommitResponse.getResult() != null) {
                if ("0".equals(paramCommitResponse.getResult())) {
                    int id = paramCommitResponse.getId();
                    try {
                        final CheckCommitDb commitDb = db.findById(CheckCommitDb.class, id);
                        commitDb.setHasCommit(2);
                        final CheckOrderDb orderDb = db.findById(CheckOrderDb.class, id);
                        orderDb.setTaskStatus(done);
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.saveOrUpdate(commitDb);
                                    db.saveOrUpdate(orderDb);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    CommonUtils.showToast(context, "提交成功");
                } else {
                    CommonUtils.showToast(context, "提交失败");
                }
                if (listener != null) {
                    listener.forRefresh();
                }
            }
        }
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {

    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }
}
