package com.it.hgad.logisticsmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.VideoData;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */
public class VideoAdapter extends BaseAdapter {
    private List<VideoData> dataList;
    private Context mContext;
    private final VideoPlayerManager mVideoPlayerManager;

    public VideoAdapter(List<VideoData> dataList, Context mContext, VideoPlayerManager mVideoPlayerManager) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.mVideoPlayerManager = mVideoPlayerManager;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        VideoData videoData = dataList.get(position);
        String pic = videoData.getPic();
        View resultView;
        if(convertView == null){
            resultView = videoData.createView(parent, mContext.getResources().getDisplayMetrics().widthPixels);
        } else {
            resultView = convertView;
        }

        try {
            videoData.update(position, (VideoData.VideoViewHolder) resultView.getTag(), mVideoPlayerManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final int mPosition = position;
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_new_video);
        return resultView;
    }

}
