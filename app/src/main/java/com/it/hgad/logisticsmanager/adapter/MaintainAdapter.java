package com.it.hgad.logisticsmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.MaintainActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.CancelShelveRequest;
import com.it.hgad.logisticsmanager.bean.response.CancelShelveResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.lidroid.xutils.DbUtils;

import java.io.Serializable;
import java.util.List;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.itcast.ecshop.net.NetUtil;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MaintainAdapter extends BaseAdapter implements Callback {
    private List<Order> listData;
    private Context mContext;
    private DbUtils db = null;
    private int userId;
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;

    public MaintainAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.mContext = context;
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

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Order order = listData.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_maintain);
        final TextView number = holder.getTv(R.id.tv_number);
        LinearLayout top = holder.getLl(R.id.ll_top);
        TextView address = holder.getTv(R.id.tv_address);
        final TextView time = holder.getTv(R.id.tv_time);
        TextView phone = holder.getTv(R.id.tv_phone);
        TextView state = holder.getTv(R.id.tv_state);
        number.setText(order.getRepairNo());
        address.setText(order.getRepairLoc());
        String registerTime = order.getRegisterTime();
        if (registerTime != null) {
            registerTime = registerTime.replace("T", " ");
        }
        time.setText(registerTime);
        phone.setText(order.getRepairTel());
        if (shouldRecieve.equals(order.getRepairFlag() + "") || shouldStart.equals(order.getRepairFlag() + "") || shouldDo.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.SHOULD_DO);
//            state.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            state.setBackgroundResource(R.drawable.shape_min_button);
            state.setTextColor(mContext.getResources().getColor(R.color.light_blue));
            top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MaintainActivity.class);
                    intent.putExtra(Constants.ORDER_DATA, (Serializable) order);
                    mContext.startActivity(intent);
                }
            });
        } else if (haveShelve.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.HAVE_SHELVE);
            state.setBackgroundResource(R.drawable.shape_min_button);
            state.setTextColor(mContext.getResources().getColor(R.color.light_blue));
            top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setTitle("确认解除挂起")//提示框标题
                            .setPositiveButton("确定",//提示框的两个按钮
                                    new android.content.DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            //事件
                                            int repairId = order.getId();
                                            CancelShelveRequest cancelShelveRequest = new CancelShelveRequest(repairId + "");
                                            NetUtil.sendRequest(cancelShelveRequest, CancelShelveResponse.class, MaintainAdapter.this);
                                        }
                                    }).setNegativeButton("取消", null).create().show();
                }
            });
        } else if (haveDone.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.HAVE_DONE);
            state.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
            state.setTextColor(Color.WHITE);
            top.setOnClickListener(null);
        } else if (haveFinish.equals(order.getRepairFlag() + "")) {
            state.setText(RepairConstants.HAVE_FINISH);
            state.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
            state.setTextColor(Color.WHITE);
            top.setOnClickListener(null);
        }
        return holder.convertView;
    }

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof CancelShelveRequest) {
            CancelShelveResponse cancelShelveResponse = (CancelShelveResponse) response;

            if (cancelShelveResponse != null) {
                if ("0".equals(cancelShelveResponse.getResult())) {
                    CommonUtils.showToast(mContext, "取消成功");
                    Intent intent = new Intent(Constants.CANCEL_SHELVE);
                    mContext.sendBroadcast(intent);
                } else {
                    CommonUtils.showToast(mContext, "取消失败");
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

