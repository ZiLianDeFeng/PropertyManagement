package com.it.hgad.logisticsmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.LearnData;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.ColletionDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/25.
 */
public class LearnAdapter extends BaseSwipeAdapter {

    private List<LearnData> dataList;
    private Context mContext;
    private String type;
    private DbUtils db;
    private CallFreshListener callFreshListener;

    public void setCallFreshListener(CallFreshListener callFreshListener) {
        this.callFreshListener = callFreshListener;
    }

    public LearnAdapter(List<LearnData> dataList, Context mContext, String type) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.type = type;
        if (db == null) {
            db = LocalApp.getDb();
        }
    }

    @Override
    public int getCount() {
        return dataList.size() == 0 ? 0 : dataList.size();
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        if (Constants.LEARN.equals(type)) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_learn, null);
            return v;
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_colletion, null);
            return v;
        }
    }

    @Override
    public void fillValues(int position, View convertView) {
        final LearnData learnData = dataList.get(position);
        String type = learnData.getType();
        TextView t = (TextView) convertView.findViewById(R.id.text_view);// 绑定数据
        LinearLayout item = (LinearLayout) convertView.findViewById(R.id.ll_item);// 绑定数据
        if (learnData.isTop()) {
            item.setBackgroundResource(R.drawable.top_item_selector);
        } else {
            item.setBackgroundResource(R.drawable.item_selector);
        }
        t.setText(type);
        if (Constants.LEARN.equals(this.type)) {
            TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            TextView tv_times = (TextView) convertView.findViewById(R.id.tv_times);
            TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            if ("new".equals(learnData.getState())) {
                tv_state.setBackgroundResource(R.drawable.shape_circle_red);
                tv_state.setTextColor(mContext.getResources().getColor(R.color.red));
                tv_state.setText("最新");
            } else if ("hot".equals(learnData.getState())) {
                tv_state.setBackgroundResource(R.drawable.shape_circle_orange);
                tv_state.setTextColor(mContext.getResources().getColor(R.color.orange));
                tv_state.setText("热门");
            } else {
                tv_state.setBackgroundResource(R.drawable.shape_circle_green);
                tv_state.setTextColor(mContext.getResources().getColor(R.color.green));
                tv_state.setText("精品");
            }
            String pubdate = learnData.getPubdate().substring(0, 10);
            tv_time.setText(pubdate);
            tv_times.setText(learnData.getTimes() + "次阅读");
            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                }
            });
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
                    // SwipeLayout双击时调用
                }
            });
            convertView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 监听SwipeLayout中的组件的点击事件
                }
            });
            convertView.findViewById(R.id.collection).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ColletionDb colletionDb = new ColletionDb(learnData.getId(), learnData.getType(), learnData.getPath(), false);
                    try {
                        db.saveOrUpdate(colletionDb);
                        swipeLayout.close(true);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    CommonUtils.showToast(mContext, "已加入我的收藏");
                }
            });
        } else {
            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.close();
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                }
            });
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
                    // SwipeLayout双击时调用
                }
            });
            convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        swipeLayout.close(true);
                        db.deleteById(ColletionDb.class, learnData.getId());
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (callFreshListener != null) {
                                    callFreshListener.callFresh();
                                }
                            }
                        }, 200);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });
			
            TextView tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            if (learnData.isTop()) {
                tv_top.setText("取消置顶");
            } else {
                tv_top.setText("置顶");
            }
            convertView.findViewById(R.id.set_top).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close(true);
                    try {
                        ColletionDb colletionDb = db.findById(ColletionDb.class, learnData.getId());
                        if (learnData.isTop()) {
                            colletionDb.setTop(false);
                        } else {
                            colletionDb.setTop(true);
                        }
                        db.saveOrUpdate(colletionDb);
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (callFreshListener != null) {
                                    callFreshListener.callFresh();
                                }
                            }
                        }, 200);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public interface CallFreshListener {
        void callFresh();
    }
}
