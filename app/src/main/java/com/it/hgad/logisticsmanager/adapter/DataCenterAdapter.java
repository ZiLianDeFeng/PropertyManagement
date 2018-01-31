package com.it.hgad.logisticsmanager.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.PersonalData;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public class DataCenterAdapter extends BaseAdapter {
    private List<PersonalData> listData;

    public DataCenterAdapter(List<PersonalData> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_data);
        ImageView iv_ranking = holder.getIv(R.id.iv_ranking);
        TextView tv_ranking = holder.getTv(R.id.tv_ranking);
        if (position==0){
            iv_ranking.setImageResource(R.mipmap.gold);
            iv_ranking.setVisibility(View.VISIBLE);
            tv_ranking.setVisibility(View.INVISIBLE);
        }else if (position == 1){
            iv_ranking.setImageResource(R.mipmap.silver);
            iv_ranking.setVisibility(View.VISIBLE);
            tv_ranking.setVisibility(View.INVISIBLE);
        }else if (position == 2){
            iv_ranking.setImageResource(R.mipmap.copper);
            iv_ranking.setVisibility(View.VISIBLE);
            tv_ranking.setVisibility(View.INVISIBLE);
        }else {
            iv_ranking.setVisibility(View.INVISIBLE);
            tv_ranking.setText(position+1+"");
            tv_ranking.setVisibility(View.VISIBLE);
        }
        return holder.convertView;
    }
}
