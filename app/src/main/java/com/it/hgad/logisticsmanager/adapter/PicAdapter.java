package com.it.hgad.logisticsmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
public class PicAdapter extends BaseAdapter {
    private Context context;
    private List<File> data;

    public PicAdapter(Context context, List<File> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_pic);
        ImageView pic = holder.getIv(R.id.iv_pic);
//        ImageView loading = holder.getIv(R.id.iv_loading);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parent.getWidth() / 35 * 10, parent.getWidth() / 35 * 10);
        pic.setLayoutParams(params);
        pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        pic.setImageBitmap(data.get(position));
        Picasso.with(context).load(data.get(position)).resize(250, 250).centerCrop().into(pic);

        return holder.convertView;
    }
}
