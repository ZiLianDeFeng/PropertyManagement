package com.it.hgad.logisticsmanager.adapter;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.MessageData;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.jauker.widget.BadgeView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public class MessageAdapter extends BaseAdapter {
    private List<MessageData> listData;
    private Context context;


    public MessageAdapter(List<MessageData> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size() == 0 ? 0 : listData.size();
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
        MessageData messageData = listData.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_message);
        ImageView iv_pic = holder.getIv(R.id.iv_pic);
        TextView tv_type = holder.getTv(R.id.tv_type);
        TextView tv_content = holder.getTv(R.id.tv_content);
        TextView tv_time = holder.getTv(R.id.tv_time);
        LinearLayout ll_message = holder.getLl(R.id.ll_message);
        BadgeView badgeView = (BadgeView) holder.getView(R.id.bv);
        badgeView.setTargetView(ll_message);
        badgeView.setBadgeMargin(5);
        badgeView.setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        int showCount = messageData.getShowCount();
        badgeView.setBadgeCount(showCount);
        badgeView.setHideOnNull(true);
        tv_content.setText(messageData.getContent());
        tv_time.setText(messageData.getTime());
        if ("巡检".equals(messageData.getType())) {
            tv_type.setText("巡检任务通知");
            iv_pic.setImageResource(R.mipmap.search_notification);
        } else if ("维修".equals(messageData.getType())) {
            tv_type.setText("维修任务通知");
            iv_pic.setImageResource(R.mipmap.maintenance_notification);
        } else if ("2".equals(messageData.getType())) {
            tv_type.setText("系统通知");
            iv_pic.setImageResource(R.mipmap.system_informs);
        } else if ("3".equals(messageData.getType())) {
            tv_type.setText("工作通知");
            iv_pic.setImageResource(R.mipmap.system_informs);
        }
        return holder.convertView;
    }
}
