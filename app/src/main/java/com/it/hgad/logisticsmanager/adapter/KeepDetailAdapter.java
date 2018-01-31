package com.it.hgad.logisticsmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.DeviceDetail;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.it.hgad.logisticsmanager.view.CheckedImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */
public class KeepDetailAdapter extends BaseAdapter {
    private List<DeviceDetail> list;
    private Context mContext;

    public KeepDetailAdapter(List<DeviceDetail> list, Context mContext) {
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
        final DeviceDetail deviceDetail = list.get(position);
        CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_keep_detail);
        TextView tv_detail = viewHolder.getTv(R.id.tv_detail);
        final CheckedImageView checkedImageView = (CheckedImageView) viewHolder.getView(R.id.id_item_select);
        checkedImageView.setChecked(deviceDetail.isHasKeep());
        checkedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceDetail.isHasKeep()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                            .setTitle("提示")
                            .setMessage("您已经保养过该项了，确认是否清除该项记录？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deviceDetail.setHasKeep(false);
                                    checkedImageView.setChecked(deviceDetail.isHasKeep());
                                }
                            }).setNegativeButton("取消", null)
                            .create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    deviceDetail.setHasKeep(true);
                    checkedImageView.setChecked(deviceDetail.isHasKeep());
                }
            }
        });
        tv_detail.setText(deviceDetail.getDetailName());
        return viewHolder.convertView;
    }
}
