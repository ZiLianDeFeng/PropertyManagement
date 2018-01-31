package com.it.hgad.logisticsmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.DeviceData;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */
public class KeepAdapter extends BaseAdapter {

    private List<DeviceData> list;
    private Context mContext;

    public KeepAdapter(List<DeviceData> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        final DeviceData deviceData = list.get(position);
        CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_keep);
        TextView tv_deviceName = viewHolder.getTv(R.id.tv_device_name);
        TextView tv_deviceType = viewHolder.getTv(R.id.tv_device_type);
//        LinearLayout main = viewHolder.getLl(R.id.ll_main);
//        final LinearLayout detail = viewHolder.getLl(R.id.ll_detail);
//        NoScrollListView lv_keep = (NoScrollListView) viewHolder.getView(R.id.lv_keep);
//        Button btn_commit = (Button) viewHolder.getView(R.id.btn_commit);
//        detail.setVisibility(deviceData.isShown() ? View.VISIBLE : View.GONE);
//        btn_commit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        final KeepDetailAdapter keepDetailAdapter = new KeepDetailAdapter(deviceData.getDeviceDetails(), mContext);
//        lv_keep.setAdapter(keepDetailAdapter);
//        main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deviceData.setShown(!deviceData.isShown());
//                detail.setVisibility(deviceData.isShown() ? View.VISIBLE : View.GONE);
//                keepDetailAdapter.notifyDataSetChanged();
//            }
//        });
        tv_deviceName.setText(deviceData.getDeviceName());
        tv_deviceType.setText(deviceData.getDeviceType());
        return viewHolder.convertView;
    }
}
