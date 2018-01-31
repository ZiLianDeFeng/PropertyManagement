package com.it.hgad.logisticsmanager.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */
public class MenuAdapter extends BaseAdapter{
    private List<String> data;

    public MenuAdapter(List<String> data) {
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_menu);
        TextView menu = holder.getTv(R.id.tv_menu);
        menu.setText(data.get(position));
        return holder.convertView;
    }
}
