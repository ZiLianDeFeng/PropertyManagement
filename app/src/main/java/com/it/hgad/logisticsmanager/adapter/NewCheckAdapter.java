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
import com.it.hgad.logisticsmanager.bean.SectionBean;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CancelCheckDb;
import com.it.hgad.logisticsmanager.dao.CheckCommitDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionBean;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */
public class NewCheckAdapter extends BaseAdapter implements PinnedSectionRefreshListView.PinnedSectionListAdapter {
    private static final int BLUE = Color.parseColor("#0094ff");
    private static final int WHITE = Color.parseColor("#ffffff");
    private List<SectionBean> listdata;
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

    public void setList(List<SectionBean> list) {
        if (list != null) {
            this.listdata = list;
        } else {
            this.listdata = new ArrayList<SectionBean>();
        }
    }


    public NewCheckAdapter(List<SectionBean> listdata, Context mContext, String type) {
        this.setList(listdata);
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
    public SectionBean getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SectionBean bean = getItem(position);
        final CheckOrder order = bean.getCheckOrder();
        int dataCount = bean.getDataCount();
        CommonViewHolder holder = null;
        TextView tv_time = null;
        TextView number = null;
        TextView checkMan = null;
        TextView deviceName = null;
        TextView state = null;
        TextView tv_count = null;
        if (getItemViewType(position) == SectionBean.SECTION) {
            holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_title);
            tv_time = holder.getTv(R.id.tv_time);
            tv_count = holder.getTv(R.id.tv_count);
        } else if (getItemViewType(position) == SectionBean.ITEM) {
            holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_check);
            tv_time = holder.getTv(R.id.tv_time);
            number = holder.getTv(R.id.tv_number);
            checkMan = holder.getTv(R.id.tv_check_man);
            deviceName = holder.getTv(R.id.tv_device_name);
            state = holder.getTv(R.id.tv_state);
            if (Constants.UNNORMAL.equals(type)) {
                state.setVisibility(View.GONE);
            } else {
                state.setVisibility(View.VISIBLE);
            }
        }
        if (bean.getType() == SectionBean.SECTION) {
            String week = CommonUtils.getWeek(order.getCurTime());
            tv_time.setText(order.getCurTime() + " " + week);
            tv_count.setText("（" + dataCount + "）");
        } else if (bean.getType() == SectionBean.ITEM) {
            tv_time.setText(order.getCurTime());
            number.setText(order.getTaskCode());
            String time = order.getPlanTime();
            String inspectorName = order.getInspectorName();
            inspectorName = inspectorName.replace(',', ' ').trim();
            checkMan.setText(inspectorName);
            if (time != null) {
                time = time.replace("T", " ");
            }
//            checkMan.setText(time);
            deviceName.setText(order.getDeviceName());
//            if (should.equals(order.getTaskStatus()) || over.equals(order.getTaskStatus())) {
//                final String finalTime = time;
//                long currentTimeMillis = System.currentTimeMillis();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                long startTimeMillis = 0;
//                try {
//                    startTimeMillis = format.parse(finalTime).getTime();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (startTimeMillis - currentTimeMillis > 600000) {
//                    state.setBackgroundColor(Color.GRAY);
//                    state.setOnClickListener(null);
//                } else {
//                    state.setBackgroundResource(R.color.blue);
//                    try {
//                        all = db.findAll(CheckCommitDb.class);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                    if (all != null && all.size() != 0) {
//                        for (CheckCommitDb commitDb : all) {
//                            int taskId = commitDb.getTaskId();
//                            int id = order.getId();
//                            if (taskId == id && commitDb.getHasCommit() == 1) {
//                                state.setText(CheckConstants.HAVE_SAVE);
////                            operation.setOnClickListener(null);
//                                break;
//                            } else {
//                                state.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(mContext, NewCheckActivity.class);
//                                        intent.putExtra(Constants.DATA, (Serializable) order);
//                                        intent.putExtra(Constants.CHECK_TYPE, type);
//                                        mContext.startActivity(intent);
//                                    }
//                                });
//                            }
//                        }
//                    } else {
//                    }
//                }
//            } else {
//                state.setOnClickListener(null);
//            }
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
//                    state.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext, NewCheckActivity.class);
//                            intent.putExtra(Constants.DATA, (Serializable) order);
//                            intent.putExtra(Constants.CHECK_TYPE, type);
//                            mContext.startActivity(intent);
//                        }
//                    });
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
//                    state.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext, NewCheckActivity.class);
//                            intent.putExtra(Constants.DATA, (Serializable) order);
//                            intent.putExtra(Constants.CHECK_TYPE, type);
//                            mContext.startActivity(intent);
//                        }
//                    });
                }
            } else if (overFinish.equals(order.getTaskStatus())) {
                state.setText(CheckConstants.TEXT_OVER_FINISH);
                state.setBackgroundColor(Color.GRAY);
            }
        }
//        }
        return holder.convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == PinnedSectionBean.SECTION;
    }
}
