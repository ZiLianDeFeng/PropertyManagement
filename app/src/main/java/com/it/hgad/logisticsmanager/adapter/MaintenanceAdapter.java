package com.it.hgad.logisticsmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.MaintainActivity;
import com.it.hgad.logisticsmanager.activity.MaintenanceActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.RecieveRequest;
import com.it.hgad.logisticsmanager.bean.request.StartRepairRequest;
import com.it.hgad.logisticsmanager.bean.response.RecieveResponse;
import com.it.hgad.logisticsmanager.bean.response.StartRepairResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.Serializable;
import java.util.List;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.itcast.ecshop.net.NetUtil;

/**
 * Created by Administrator on 2017/1/9.
 */
public class MaintenanceAdapter extends BaseAdapter implements Callback {
    private List<Order> listdata;
    private Context mContext;
    private String type;
    private static final int BLUE = Color.parseColor("#0094ff");
    private static final int WHITE = Color.parseColor("#ffffff");
    private DbUtils db = null;
    private List<CommitDb> commitDbs;
    private int userId;
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;
    //    private int first = 1;

    public AdapterRefreshListener getListener() {
        return listener;
    }

    public void setListener(AdapterRefreshListener listener) {
        this.listener = listener;
    }

    private AdapterRefreshListener listener;

    public MaintenanceAdapter(List<Order> listdata, Context mContext, String type) {
        this.listdata = listdata;
        this.mContext = mContext;
        this.type = type;
        onCreate();
    }

    private void onCreate() {
        if (db == null) {
            db = LocalApp.getDb();
        }
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        shouldRecieve = SPUtils.getString(mContext, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(mContext, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(mContext, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(mContext, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(mContext, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(mContext, RepairConstants.HAVE_FINISH);
    }

    public interface AdapterRefreshListener {
        void startCommit();
        void forRefresh();
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
        final Order order = listdata.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_main_manage);
        ImageView iv = holder.getIv(R.id.iv_order);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MaintainActivity.class);
                intent.putExtra(Constants.ORDER_DATA, (Serializable) order);
                mContext.startActivity(intent);
            }
        });
        final TextView opration = holder.getTv(R.id.iv_operation);
        final TextView number = holder.getTv(R.id.tv_number);
        TextView address = holder.getTv(R.id.tv_address);
        final TextView time = holder.getTv(R.id.tv_time);
        TextView phone = holder.getTv(R.id.tv_phone);
        TextView state = holder.getTv(R.id.tv_state);
        TextView people = holder.getTv(R.id.tv_people);
        number.setText(order.getRepairNo());
        address.setText(order.getRepairLoc());
        String registerTime = order.getRegisterTime();
        if (registerTime != null) {
            registerTime = registerTime.replace("T", " ");
        }
        time.setText(registerTime);
        phone.setText(order.getRepairTel());
        people.setText(order.getRegisterMan());
//        opration.setBackgroundColor(order.getRepairFlag() == 2 ? BLUE : order.getRepairFlag() == 3 ? BLUE : order.getRepairFlag() == 4 ? BLUE : WHITE);
        opration.setVisibility((order.getRepairFlag() + "").equals(shouldRecieve) ? View.VISIBLE : (order.getRepairFlag() + "").equals(shouldStart) ? View.VISIBLE : (order.getRepairFlag() + "").equals(shouldDo) ? View.VISIBLE : View.GONE);
        if (shouldRecieve.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.SHOULD_RECIEVE);
            opration.setText(RepairConstants.RECIEVE);
            opration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle("提示")//提示框标题
                            .setMessage("是否确定接收")
                            .setPositiveButton("确定",//提示框的两个按钮
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            //事件
                                            String currentTime = CommonUtils.getCurrentTime();
                                            boolean checkNetWork = Utils.checkNetWork(mContext);
                                            if (checkNetWork) {
                                                RecieveRequest recieveRequest = new RecieveRequest(order.getId() + "", currentTime, userId + "");
                                                NetUtil.sendRequest(recieveRequest, RecieveResponse.class, MaintenanceAdapter.this);
                                            } else {
                                                CommonUtils.showToast(mContext, mContext.getString(R.string.no_net));
                                            }
                                        }
                                    }).setNegativeButton("取消", null)
                            .create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            });
        } else if (shouldStart.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.SHOULD_START);
            opration.setText(RepairConstants.START);
            if (order.getSameCondition() == null) {
                opration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle("提示")//提示框标题
                                .setMessage("请确认现场故障与报修信息是否相符，并在维修前拍摄照片！")
                                .setPositiveButton("相符",//提示框的两个按钮
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                //事件
                                                String currentTime = CommonUtils.getCurrentTime();
                                                boolean checkNetWork = Utils.checkNetWork(mContext);
                                                if (checkNetWork) {
                                                    StartRepairRequest startRepairRequest = new StartRepairRequest(order.getId() + "", "", currentTime, "");
                                                    NetUtil.sendRequest(startRepairRequest, StartRepairResponse.class, MaintenanceAdapter.this);
                                                } else {
                                                    SPUtils.put(mContext, order.getId() + "", currentTime);
                                                    final CommitDb commitDb = new CommitDb();
                                                    commitDb.setStartTime(currentTime);
                                                    commitDb.setUserId(userId);
                                                    commitDb.setRepairId(order.getId());
                                                    commitDb.setSameCondition("");
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                db.saveOrUpdate(commitDb);
                                                                List<OrderDb> all = db.findAll(OrderDb.class);
                                                                for (OrderDb orderDb :
                                                                        all) {
                                                                    if (orderDb.getRepairId() == order.getId()) {
                                                                        orderDb.setRepairFlag(Integer.parseInt(shouldDo));
                                                                        db.saveOrUpdate(orderDb);
                                                                        Intent intent = new Intent(Constants.COMMIT_OK);
                                                                        mContext.sendBroadcast(intent);
                                                                    }
                                                                }

                                                            } catch (DbException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).start();
                                                    Utils.showTost(mContext, mContext.getString(R.string.start_maintance));
                                                }
                                            }
                                        })
                                .setNeutralButton("不相符", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LayoutInflater factory = LayoutInflater.from(mContext);//提示框
                                        final View dialogView = factory.inflate(R.layout.editbox_layout, null);//这里必须是final的
                                        final EditText edit = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
                                        new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                                .setTitle("请填写正确的现场故障")//提示框标题
                                                .setView(dialogView)
                                                .setPositiveButton("确定",//提示框的两个按钮
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog,
                                                                                int which) {
                                                                //事件
                                                                String currentTime1 = CommonUtils.getCurrentTime();
                                                                final String remark = edit.getText().toString().trim();
                                                                boolean checkNetWork = Utils.checkNetWork(mContext);
                                                                if (checkNetWork) {
                                                                    StartRepairRequest startRepairRequest = new StartRepairRequest(order.getId() + "", remark, "", currentTime1);
                                                                    NetUtil.sendRequest(startRepairRequest, StartRepairResponse.class, MaintenanceAdapter.this);
                                                                } else {
                                                                    final CommitDb commitDb = new CommitDb();
                                                                    commitDb.setDelayTime(currentTime1);
                                                                    commitDb.setUserId(userId);
                                                                    commitDb.setRepairId(order.getId());
                                                                    commitDb.setSameCondition(remark);
                                                                    new Thread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            try {
                                                                                boolean have = false;
                                                                                List<CommitDb> dbAll = db.findAll(CommitDb.class);
                                                                                if (dbAll != null) {
                                                                                    for (CommitDb commitDb : dbAll
                                                                                            ) {
                                                                                        if (commitDb.getRepairId() == order.getId()) {
                                                                                            have = true;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (have) {
                                                                                    db.update(commitDb, "delayTime", "sameCondition");
                                                                                } else {
                                                                                    db.saveOrUpdate(commitDb);
                                                                                }
                                                                                List<OrderDb> all = db.findAll(OrderDb.class);
                                                                                for (OrderDb orderDb :
                                                                                        all) {
                                                                                    if (orderDb.getRepairId() == order.getId()) {
                                                                                        orderDb.setSameCondition(remark);
                                                                                        db.saveOrUpdate(orderDb);
                                                                                        Intent intent = new Intent(Constants.COMMIT_OK);
                                                                                        mContext.sendBroadcast(intent);
                                                                                    }
                                                                                }
                                                                            } catch (DbException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }).start();

                                                                }
                                                                CommonUtils.closeKeybord(edit, mContext);
                                                            }
                                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                CommonUtils.closeKeybord(edit, mContext);
                                            }
                                        }).create().show();
                                        Utils.showTost(mContext, "请在保证现场故障相符后再开始维修");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                CommonUtils.showInputMethod(mContext);
                                            }
                                        }, 100);

                                    }
                                }).setNegativeButton("取消", null).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                });
            } else if (!order.getSameCondition().equals("")) {
                opration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle("提示")//提示框标题
                                .setMessage("是否确定开始,记得在维修前拍摄照片！")
                                .setPositiveButton("确定",//提示框的两个按钮
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                //事件
                                                String currentTime = CommonUtils.getCurrentTime();
                                                boolean checkNetWork = Utils.checkNetWork(mContext);
                                                if (checkNetWork) {
                                                    StartRepairRequest startRepairRequest = new StartRepairRequest(order.getId() + "", "", currentTime, "");
                                                    NetUtil.sendRequest(startRepairRequest, StartRepairResponse.class, MaintenanceAdapter.this);
                                                } else {
                                                    final CommitDb commitDb = new CommitDb();
                                                    commitDb.setStartTime(currentTime);
                                                    commitDb.setUserId(userId);
                                                    commitDb.setRepairId(order.getId());
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                boolean have = false;
                                                                List<CommitDb> dbAll = db.findAll(CommitDb.class);
                                                                for (CommitDb commitDb : dbAll
                                                                        ) {
                                                                    if (commitDb.getRepairId() == order.getId()) {
                                                                        have = true;
                                                                        break;
                                                                    }
                                                                }
                                                                if (have) {
                                                                    db.update(commitDb, "startTime");
                                                                } else {
                                                                    db.saveOrUpdate(commitDb);
                                                                }
                                                                List<OrderDb> all = db.findAll(OrderDb.class);
                                                                for (OrderDb orderDb :
                                                                        all) {
                                                                    if (orderDb.getRepairId() == order.getId()) {
                                                                        orderDb.setRepairFlag(Integer.parseInt(shouldDo));
                                                                        db.saveOrUpdate(orderDb);
                                                                        Intent intent = new Intent(Constants.COMMIT_OK);
                                                                        mContext.sendBroadcast(intent);
                                                                    }
                                                                }
                                                            } catch (DbException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).start();
                                                    Utils.showTost(mContext, mContext.getString(R.string.start_maintance));
                                                }
                                            }
                                        })
                                .setNegativeButton("取消", null).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                });
            }
        } else if (shouldDo.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.SHOULD_DO);
            try {
                commitDbs = db.findAll(CommitDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (commitDbs != null && commitDbs.size() != 0) {
                for (CommitDb commitDb : commitDbs) {
                    int repairId = commitDb.getRepairId();
                    int id = order.getId();
//                        String sameCondition = order.getSameCondition();
                    if (repairId == id && commitDb.getHasCommit() == 1) {
                        opration.setText(RepairConstants.HAVE_SAVE);
                        opration.setOnClickListener(null);
                        break;
                    } else {
                        opration.setText(RepairConstants.MAINTANCE);
                        opration.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, MaintenanceActivity.class);
                                intent.putExtra(Constants.ORDER_DATA, (Serializable) order);
                                mContext.startActivity(intent);
                            }
                        });
                    }
                }
            } else {
                opration.setText(RepairConstants.MAINTANCE);
                opration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MaintenanceActivity.class);
                        intent.putExtra(Constants.ORDER_DATA, (Serializable) order);
                        mContext.startActivity(intent);
                    }
                });
            }
        } else if (haveShelve.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.HAVE_SHELVE);
        } else if (haveDone.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.HAVE_DONE);
        } else if (haveFinish.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.HAVE_FINISH);
        }
        return holder.convertView;
    }


    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof RecieveRequest) {
            RecieveResponse recieveResponse = (RecieveResponse) response;
            if (recieveResponse != null) {
                if ("0".equals(recieveResponse.getResult())) {
//                    this.notifyDataSetChanged();
                    if (listener != null) {
                        listener.forRefresh();
                    }
                    CommonUtils.showToast(mContext, mContext.getString(R.string.success_recieve));
                }
            }
        } else if (request instanceof StartRepairRequest) {
            StartRepairResponse startRepairResponse = (StartRepairResponse) response;
            if (startRepairResponse != null) {
                if ("0".equals(startRepairResponse.getResult())) {
//                    this.notifyDataSetChanged();
                    if (listener != null) {
                        listener.forRefresh();
                    }
                    CommonUtils.showToast(mContext, mContext.getString(R.string.start_maintance));
                } else {
                    CommonUtils.showToast(mContext, mContext.getString(R.string.start_fail));
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
