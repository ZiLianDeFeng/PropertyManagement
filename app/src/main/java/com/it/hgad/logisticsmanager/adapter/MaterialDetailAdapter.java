package com.it.hgad.logisticsmanager.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.Material;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */
public class MaterialDetailAdapter extends BaseAdapter {
    private List<Material> data;
    public MaterialDetailAdapter(List<Material> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size()==0?0:data.size();
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
        Material material = data.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_detail);
        TextView tv_name = holder.getTv(R.id.tv_name);
        TextView tv_type = holder.getTv(R.id.tv_type);
        TextView tv_count = holder.getTv(R.id.tv_count);
        tv_name.setText("材料名称："+material.getName());
        tv_type.setText("规格型号："+material.getType());
        tv_count.setText("材料数量："+material.getNumber());
        return holder.convertView;
    }
}
