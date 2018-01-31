package com.it.hgad.logisticsmanager.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.Material;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */
public class AddAdapter extends BaseAdapter {
    private List<Material> data;

    public AddAdapter(List<Material> data) {
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_add);
        TextView tv_name = holder.getTv(R.id.tv_name);
        TextView tv_type = holder.getTv(R.id.tv_type);
        CheckBox cb = holder.getCb(R.id.cb);
        final Material material = data.get(position);
        tv_name.setText(material.getName());
        tv_type.setText("型号："+material.getType());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                material.setChecked(isChecked);
            }
        });
        cb.setChecked(material.isChecked());
        return holder.convertView;
    }
}
