package com.it.hgad.logisticsmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.view.ImageDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 */
public class DownloadPicAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;

    public DownloadPicAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
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
        final ImageView pic = holder.getIv(R.id.iv_pic);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parent.getWidth()/35*10,parent.getWidth()/35*10);
        pic.setLayoutParams(params);
        pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        String ip = SPUtils.getString(LocalApp.getContext(), SPConstants.IP);
        Picasso.with(context).load(HttpConstants.picFormatUrl(ip) + data.get(position)).resize(250,250).centerCrop().into(pic);
        pic.setDrawingCacheEnabled(true);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog myImageDialog = new ImageDialog(context, R.anim.dialog_in, 0, -300, pic.getDrawingCache());
                myImageDialog.show();
            }
        });
        return holder.convertView;
    }
}
