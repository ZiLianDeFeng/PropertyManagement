package com.it.hgad.logisticsmanager.bean;

import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.it.hgad.logisticsmanager.R;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/31.
 */
public class VideoData implements View.OnClickListener {
    private int id;
    private String type;
    private String path;
    private boolean isTop;
    private String pic;
    private final Rect mCurrentViewRect = new Rect();
    private final Context context;
    private boolean isPlaying;
    private boolean isPraise;

    public VideoData(int id, String type, String path, boolean isTop, String pic, Context context, boolean isPlaying,boolean isPraise) {
        this.id = id;
        this.type = type;
        this.path = path;
        this.isTop = isTop;
        this.pic = pic;
        this.context = context;
        this.isPlaying = isPlaying;
        this.isPraise = isPraise;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public int getVisibilityPercents(View currentView) {
        int percents = 100;

        currentView.getLocalVisibleRect(mCurrentViewRect);

        int height = currentView.getHeight();

        if (viewIsPartiallyHiddenTop()) {
            // view is partially hidden behind the top edge
            percents = (height - mCurrentViewRect.top) * 100 / height;
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height;
        }

        return percents;
    }

    public void update(int position, final VideoViewHolder viewHolder, VideoPlayerManager videoPlayerManager) throws IOException {
        viewHolder.mTitle.setText(type);
//        viewHolder.mCover.setVisibility(View.VISIBLE);
//        Picasso.with(context).load(pic).resize(350, 350).centerCrop().into(viewHolder.mCover);
        String path = Environment.getExternalStorageDirectory().getPath() + "/video_sample_1.mp4";
        Uri uri = Uri.parse(path);
        viewHolder.mPlayer.setMediaController(new MediaController(context));
        viewHolder.mPlayer.setOnCompletionListener(completionListener);
        viewHolder.mPlayer.setVideoURI(uri);
    }

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
        }
    };

    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }



    private MetaData curMetaData;


    public View createView(ViewGroup parent, int screenWidth) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = screenWidth;

        final VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        view.setTag(videoViewHolder);

//        videoViewHolder.mPlayer.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
//            @Override
//            public void onVideoSizeChangedMainThread(int width, int height) {
//            }
//
//            @Override
//            public void onVideoPreparedMainThread() {
//                // When video is prepared it's about to start playback. So we hide the cover
//                videoViewHolder.mCover.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onVideoCompletionMainThread() {
//            }
//
//            @Override
//            public void onErrorMainThread(int what, int extra) {
//            }
//
//            @Override
//            public void onBufferingUpdateMainThread(int percent) {
//            }
//
//            @Override
//            public void onVideoStoppedMainThread() {
//                // Show the cover when video stopped
//                videoViewHolder.mCover.setVisibility(View.VISIBLE);
//            }
//        });
//        videoViewHolder.videoStartImg.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

        public final VideoView mPlayer;
        public final TextView mTitle;
        //        private final ImageView mCover;
        private final FrameLayout viewBox;
//        private final LinearLayout screenSwitchBtn;
//        private final LinearLayout videoControllerLayout;
//        private final LinearLayout touchStatusView;
//        private final ImageView touchStatusImg;
//        private final TextView touchStatusTime;
//        private final TextView videoCurTimeText;
//        private final TextView videoTotalTimeText;
//        private final SeekBar videoSeekBar;
//        private final ImageView videoPlayImg;
//        private final ImageView videoPauseImg;
//        private final ProgressBar progressBar;
//        private final ImageView videoStartImg;
        //        public final TextView mVisibilityPercents;

        public VideoViewHolder(View view) {
            super(view);
            mPlayer = (VideoView) view.findViewById(R.id.player);
            mTitle = (TextView) view.findViewById(R.id.title);
//            mCover = (ImageView) view.findViewById(R.id.cover);
            viewBox = (FrameLayout) view.findViewById(R.id.viewBox);
//            screenSwitchBtn = (LinearLayout) view.findViewById(R.id.screen_status_btn);
//            videoControllerLayout = (LinearLayout) view.findViewById(R.id.videoControllerLayout);
//            touchStatusView = (LinearLayout) view.findViewById(R.id.touch_view);
//            touchStatusImg = (ImageView) view.findViewById(R.id.touchStatusImg);
//            touchStatusTime = (TextView) view.findViewById(R.id.touch_time);
//            videoCurTimeText = (TextView) view.findViewById(R.id.videoCurTime);
//            videoTotalTimeText = (TextView) view.findViewById(R.id.videoTotalTime);
//            videoSeekBar = (SeekBar) view.findViewById(R.id.videoSeekBar);
//            videoPlayImg = (ImageView) view.findViewById(R.id.videoPlayImg);
////            videoPlayImg.setVisibility(View.GONE);
//            videoPauseImg = (ImageView) view.findViewById(R.id.videoPauseImg);
//            videoStartImg = (ImageView) view.findViewById(R.id.videoStartImg);
//            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//
//            videoPauseImg.setOnClickListener(this);
//            videoSeekBar.setOnSeekBarChangeListener(this);
//            videoStartImg.setOnClickListener(this);
////            videoView.setOnPreparedListener(this);
////            videoView.setOnCompletionListener(this);
//            screenSwitchBtn.setOnClickListener(this);
//            videoPlayImg.setOnClickListener(this);
//            videoView.setOnErrorListener(this);
            viewBox.setOnTouchListener(this);
            viewBox.setOnClickListener(this);
//            mVisibilityPercents = (TextView) view.findViewById(R.id.visibility_percents);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.screen_status_img:
//                    setFullScreen();
//                    break;
//                case R.id.videoPauseImg:
//                    pause();
//                    break;
//                case R.id.videoStartImg:
//                    break;
//                case R.id.videoPlayImg:
//                    try {
//                        start();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
            }
        }

        private void pause() {
            mPlayer.pause();
//            videoPauseImg.setVisibility(View.INVISIBLE);
//            videoStartImg.setVisibility(View.VISIBLE);
//            videoPlayImg.setVisibility(View.VISIBLE);
        }

        private void start() throws IOException {
            mPlayer.start();
//            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(path);
//            mVideoPlayerManager.playNewVideo(curMetaData, mPlayer, assetFileDescriptor);
//            videoPauseImg.setVisibility(View.VISIBLE);
//            videoStartImg.setVisibility(View.INVISIBLE);
//            videoPlayImg.setVisibility(View.GONE);
        }

        public void setFullScreen() {
//            touchStatusImg.setImageResource(R.mipmap.iconfont_exit);
//            this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            videoView.requestLayout();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }

}
