package com.it.hgad.logisticsmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.NewCheckActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.CheckOrder;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CancelCheckDb;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/2/5.
 */
public class CheckAdapter extends BaseAdapter {
    private static final int BLUE = Color.parseColor("#0094ff");
    private static final int WHITE = Color.parseColor("#ffffff");
    private List<CheckOrder> listdata;
    private Context mContext;
    private String type;
    private DbUtils db;
    private int userId;
    private List<CheckCommitDb> all;
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;

    public CheckAdapter(List<CheckOrder> listdata, Context mContext, String type) {
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
        should = SPUtils.getString(mContext, CheckConstants.SHOULD);
        done = SPUtils.getString(mContext, CheckConstants.DONE);
        over = SPUtils.getString(mContext, CheckConstants.OVER);
        overFinish = SPUtils.getString(mContext, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(mContext, CheckConstants.CANCEL);
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
        final CheckOrder order = listdata.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_old_check);
        TextView number = holder.getTv(R.id.tv_number);
//        TextView checkMan = holder.getTv(R.id.tv_check_man);
        TextView deviceName = holder.getTv(R.id.tv_device_name);
        TextView state = holder.getTv(R.id.tv_state);
        TextView planTime = holder.getTv(R.id.tv_plan_time);
        number.setText(order.getTaskCode());
        String time = order.getPlanTime();
        if (time != null) {
            time = time.replace("T", " ");
        }
        planTime.setText(time);
        String inspectorName = order.getInspectorName();
        inspectorName = inspectorName.replace(',', ' ').trim();
//        checkMan.setText(inspectorName);
        deviceName.setText(order.getDeviceName());
//        operation.setClickable("0".equals(order.getTaskStatus()));
//        operation.setBackgroundColor(should.equals(order.getTaskStatus()) | over.equals(order.getTaskStatus()) ? BLUE : WHITE);
//        operation.setVisibility(should.equals(order.getTaskStatus()) | over.equals(order.getTaskStatus()) ? View.VISIBLE : View.GONE);
        if (should.equals(order.getTaskStatus()) || over.equals(order.getTaskStatus())) {
//            if (should.equals(order.getTaskStatus()) ){
//                state
//            }
            final String finalTime = time;
            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTimeMillis = 0;
            try {
                startTimeMillis = format.parse(finalTime).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startTimeMillis - currentTimeMillis > 600000) {
                state.setBackgroundColor(Color.GRAY);
                state.setOnClickListener(null);
            } else {
                state.setBackgroundResource(R.color.blue);
                try {
                    all = db.findAll(CheckCommitDb.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (all != null && all.size() != 0) {
                    for (CheckCommitDb commitDb : all) {
                        int taskId = commitDb.getTaskId();
                        int id = order.getId();
                        if (taskId == id && commitDb.getHasCommit() == 1) {
                            state.setText(CheckConstants.HAVE_SAVE);
//                            operation.setOnClickListener(null);
                            break;
                        } else {
                            state.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, NewCheckActivity.class);
                                    intent.putExtra(Constants.DATA, (Serializable) order);
                                    intent.putExtra(Constants.CHECK_TYPE,type);
                                    mContext.startActivity(intent);
                                }
                            });
//                            operation.setText(CheckConstants.COPY_LIST);
//                            operation.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
////                                long currentTimeMillis = System.currentTimeMillis();
////                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
////                                long startTimeMillis = 0;
////                                try {
////                                    startTimeMillis = format.parse(finalTime).getTime();
////                                } catch (ParseException e) {
////                                    e.printStackTrace();
////                                }
////                                if (startTimeMillis - currentTimeMillis > 600000) {
////                                    Utils.showTost(mContext, "只能在开始时间前10分钟后才能操作");
////                                } else {
//                                    Intent intent = new Intent(mContext, CheckActivity.class);
//                                    intent.putExtra(Constants.DATA, (Serializable) order);
//                                    mContext.startActivity(intent);
////                                }
//                                }
//                            });
                        }
                    }
                } else {
//                    operation.setText(CheckConstants.COPY_LIST);
//                    operation.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                        long currentTimeMillis = System.currentTimeMillis();
////                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
////                        long startTimeMillis = 0;
////                        try {
////                            startTimeMillis = format.parse(finalTime).getTime();
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
////                        if (startTimeMillis - currentTimeMillis > 600000) {
////                            Utils.showTost(mContext, "只能在开始时间前10分钟后才能操作");
////                        } else {
//                            Intent intent = new Intent(mContext, CheckActivity.class);
//                            intent.putExtra(Constants.DATA, (Serializable) order);
//                            mContext.startActivity(intent);
////                        }
//                        }
//                    });
                }
            }
        } else {
            state.setOnClickListener(null);
        }
//        switch (order.getTaskStatus()) {
//            case CheckConstants.SHOULD_CHECK:
        if (should.equals(order.getTaskStatus())) {
            state.setText(CheckConstants.TEXT_SHOULD);
            List<CancelCheckDb> cancelCheckDbs = null;
            try {
                cancelCheckDbs = db.findAll(Selector.from(CancelCheckDb.class).where(WhereBuilder.b(CancelCheckDb.HAS_COMMIT, "=", 1)));
            } catch (DbException e) {
                e.printStackTrace();
            }
            boolean isCancel = false;
            if (cancelCheckDbs != null && cancelCheckDbs.size() != 0) {
                for (CancelCheckDb cancelCheckDb : cancelCheckDbs) {
                    if (cancelCheckDb.getTaskId() == order.getId()) {
                        state.setText(CheckConstants.HAVE_CANCEL);
                        state.setBackgroundColor(Color.GRAY);
                        state.setOnClickListener(null);
                        isCancel = true;
                        break;
                    }
                }
            }
            if (!isCancel) {
                state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewCheckActivity.class);
                        intent.putExtra(Constants.DATA, (Serializable) order);
                        intent.putExtra(Constants.CHECK_TYPE, type);
                        mContext.startActivity(intent);
                    }
                });
            }
        } else if (done.equals(order.getTaskStatus())) {
            state.setText(CheckConstants.TEXT_DONE);
            state.setBackgroundColor(Color.GRAY);
        } else if (cancel.equals(order.getTaskStatus())) {
            state.setText(CheckConstants.HAVE_CANCEL);
            state.setBackgroundColor(Color.GRAY);
        } else if (over.equals(order.getTaskStatus())) {
            state.setText(CheckConstants.TEXT_OVER);
            List<CancelCheckDb> overCancelCheckDbs = null;
            try {
                overCancelCheckDbs = db.findAll(Selector.from(CancelCheckDb.class).where(WhereBuilder.b("hasCommit", "=", 1)));
            } catch (DbException e) {
                e.printStackTrace();
            }
            boolean isCancel = false;
            if (overCancelCheckDbs != null && overCancelCheckDbs.size() != 0) {
                for (CancelCheckDb cancelCheckDb : overCancelCheckDbs) {
                    if (cancelCheckDb.getTaskId() == order.getId()) {
                        state.setText(CheckConstants.HAVE_CANCEL);
                        state.setBackgroundColor(Color.GRAY);
                        state.setOnClickListener(null);
                        isCancel = true;
                        break;
                    }
                }
            }
            if (!isCancel) {
                state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewCheckActivity.class);
                        intent.putExtra(Constants.DATA, (Serializable) order);
                        intent.putExtra(Constants.CHECK_TYPE, type);
                        mContext.startActivity(intent);
                    }
                });
            }
        } else if (overFinish.equals(order.getTaskStatus())) {
            state.setText(CheckConstants.TEXT_OVER_FINISH);
            state.setBackgroundColor(Color.GRAY);
        }
//        }
        return holder.convertView;
    }
}
