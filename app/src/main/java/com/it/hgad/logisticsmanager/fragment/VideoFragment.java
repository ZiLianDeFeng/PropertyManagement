package com.it.hgad.logisticsmanager.fragment;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.VideoData;
import com.it.hgad.logisticsmanager.bean.request.VideoRequest;
import com.it.hgad.logisticsmanager.bean.response.VideoResponse;
import com.it.hgad.logisticsmanager.view.CheckedImageView;
import com.it.hgad.logisticsmanager.view.VideoDialog;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.visibility_utils.scroll_utils.ListViewItemPositionGetter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/31.
 */
public class VideoFragment extends BaseFragment {

    private View mView;
    private List<VideoData> list = new ArrayList<>();
    private int playPosition = -1;
    private boolean isPaused = false;
    private boolean isPlaying = false;
    private int currentIndex = -1;
    private Handler mainHandler = new Handler();

    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!list.isEmpty()) {
                if ((currentIndex < firstVisibleItem || currentIndex > lv.getLastVisiblePosition()) && isPlaying) {
//                    System.out.println("滑动的："+mVideoView.toString());
                    playPosition = mVideoView.getCurrentPosition();
                    mVideoView.pause();
                    list.get(currentIndex).setPlaying(false);
                    mVideoView.setMediaController(null);
                    isPaused = true;
                    isPlaying = false;
//                    CommonUtils.showToast(mContext, "视屏已暂停");
                }
            }
        }
    };
    private ListViewItemPositionGetter mItemsPositionGetter;
    private ListView lv;
    private MediaController mMediaCtrl;
    private VideoView mVideoView;
    private NewVideoAdapter newVideoAdapter;
    //    private MediaController mediaController;
    private VideoDialog videoDialog;
    private String currentPath;
    private int backPosition;
    private final static int WHAT = 0;
    private Timer timer;
    private boolean backPlaying;
    private Animation scaleAnimation;
    private Animation translateAnimation;

    @Override
    protected void initData() {
        list.add(new VideoData(13, "结婚当天小姑子在婚礼上大闹，丈夫的态度让我坚决离婚！", "video_sample_1.mp4", false, "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1225040570,1155990249&fm=23&gp=0.jpg", mContext, false, false));
        list.add(new VideoData(14, "拥有刀刻般的八块腹肌是怎样一种体验？", "video_sample_1.mp4", false, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496227174366&di=f41df3169e0fd487b672a4d47e0abbfa&imgtype=0&src=http%3A%2F%2Fi1.topit.me%2F1%2Fff%2F40%2F1194978948d1040ff1o.jpg", mContext, false, false));
        list.add(new VideoData(15, "与异性合租是一种怎样的体验？", "video_sample_1.mp4", false, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496227174367&di=d0ac3c5b319e5d5ae540efc980188606&imgtype=0&src=http%3A%2F%2Fimg1.pconline.com.cn%2Fpiclib%2F200906%2F16%2Fbatch%2F1%2F35412%2F1245119293386tv7sapi8x4.jpg", mContext, false, false));
        list.add(new VideoData(16, "男子河边抓到一只蚁后， 接下来一幕让我惊呆了！", "video_sample_1.mp4", false, "http://d.hiphotos.baidu.com/zhidao/pic/item/241f95cad1c8a7862239b60f6109c93d70cf50a4.jpg", mContext, false, false));
        list.add(new VideoData(17, "女司机写的情书，太有才了！", "video_sample_1.mp4", false, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496227174359&di=24c744dd93e649d57138a099e1a147c6&imgtype=0&src=http%3A%2F%2Fimgb.mumayi.com%2Fandroid%2Fwallpaper%2F2012%2F01%2F03%2Fsl_600_2012010302213667522752.jpg", mContext, false, false));
        list.add(new VideoData(18, "大学校长的秘密情史，太让人意外了！", "video_sample_1.mp4", false, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496227174359&di=b1d83951268801db17ea1a50e64da0f4&imgtype=0&src=http%3A%2F%2Ffile.mumayi.com%2Fforum%2F201305%2F16%2F171333grx16e26je1q1m1e.jpg", mContext, false, false));
        newVideoAdapter.notifyDataSetInvalidated();
        VideoRequest videoRequest = new VideoRequest();
        sendRequest(videoRequest, VideoResponse.class);
    }

    @Override
    protected void initView() {
        lv = (ListView) mView.findViewById(R.id.lv_video);
//        videoAdapter = new VideoAdapter(list, mContext, mVideoPlayerManager);
        newVideoAdapter = new NewVideoAdapter();
        lv.setAdapter(newVideoAdapter);
//        mItemsPositionGetter = new ListViewItemPositionGetter(lv);
        lv.setOnScrollListener(scrollListener);
        videoDialog = new VideoDialog(mContext, ProgressDialog.THEME_HOLO_DARK);
        videoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                normalScreen();
            }
        });
        videoDialog.setDialogCallBackListener(new VideoDialog.DialogCallBackListener() {
            @Override
            public void callBack(int position) {
                backPosition = position;
            }

            @Override
            public void isPlaying(boolean isPlay) {
                backPlaying = isPlay;
            }
        });
        scaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.praise);
        LinearInterpolator lin = new LinearInterpolator();
        scaleAnimation.setInterpolator(lin);
        translateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.add);
        translateAnimation.setInterpolator(lin);
    }

    private void normalScreen() {
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
//        int currentPosition = mVideoView.getCurrentPosition();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mVideoView.seekTo(backPosition);
        if (backPlaying) {
            mVideoView.start();
            list.get(currentIndex).setPlaying(true);
        } else {
            mVideoView.pause();
            list.get(currentIndex).setPlaying(false);
        }
//        newVideoAdapter.notifyDataSetInvalidated();
//        lv.setSelection(currentIndex);
//        mVideoView.requestFocus();
//            fullscreen = false;//改变全屏/窗口的标记
    }

    // 自动隐藏自定义播放器控制条的时间
    private static final int HIDDEN_TIME = 5000;
    private LinearLayout currentBotton;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // 又回到了主线程
            showOrHiddenController(currentBotton);
        }
    };

    class NewVideoAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final int mPosition = position;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_new_video, null);
                holder = new ViewHolder();
                holder.videoImage = (ImageView) convertView.findViewById(R.id.video_image);
                holder.videoNameText = (TextView) convertView.findViewById(R.id.title);
                holder.videoPlayBtn = (ImageButton) convertView.findViewById(R.id.video_play_btn);
                holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.progressbar);
                holder.full = (ImageView) convertView.findViewById(R.id.imageView_fullscreen);
                holder.video = ((RelativeLayout) convertView.findViewById(R.id.rl_video));
                holder.play = (ImageView) convertView.findViewById(R.id.imageView_play);
                holder.bottonLayout = ((LinearLayout) convertView.findViewById(R.id.bottom_layout));
                holder.seekBar = ((SeekBar) convertView.findViewById(R.id.seekbar));
                holder.playtime = ((TextView) convertView.findViewById(R.id.tv_playtime));
                holder.totalTime = ((TextView) convertView.findViewById(R.id.tv_totaltime));
                holder.praiseView = ((CheckedImageView) convertView.findViewById(R.id.praise_view));
                holder.add = ((TextView) convertView.findViewById(R.id.tv_add));
                convertView.setTag(holder);
//                currentBotton = holder.bottonLayout;
//                currentSeekBar = holder.seekBar;
//                currentPlayTime = holder.playtime;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Handler handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case WHAT:
                            if (mVideoView != null) {
                                int currentPlayer = mVideoView.getCurrentPosition();
                                if (currentPlayer > 0) {
                                    mVideoView.getCurrentPosition();
                                    holder.playtime.setText(formatTime(currentPlayer));

                                    // 让seekBar也跟随改变
                                    int progress = (int) ((currentPlayer / (float) mVideoView
                                            .getDuration()) * 100);

                                    holder.seekBar.setProgress(progress);
                                } else {
                                    holder.playtime.setText("00:00");
                                    holder.seekBar.setProgress(0);
                                }
                            }

                            break;

                        default:
                            break;
                    }
                }
            };
            Picasso.with(mContext).load(list.get(position).getPic()).resize(350, 350).centerCrop().into(holder.videoImage);
            holder.videoNameText.setText(list.get(position).getType());
            holder.videoPlayBtn.setVisibility(View.VISIBLE);
            holder.videoImage.setVisibility(View.VISIBLE);
            holder.praiseView.setChecked(list.get(position).isPraise());
            holder.praiseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    holder.praiseView.toggle();
                    if (holder.praiseView.isChecked()) {
                        holder.praiseView.setChecked(false);
                    } else {
                        holder.praiseView.setChecked(true);
                        holder.praiseView.startAnimation(scaleAnimation);
                        holder.add.setVisibility(View.VISIBLE);
                        holder.add.startAnimation(translateAnimation);
                        ObjectAnimator animator = ObjectAnimator
                                .ofFloat(holder.add, "alpha", 1, 0)
                                .setDuration(1000);
                        animator.start();
                    }
//                    holder.praiseView.setChecked(!holder.praiseView.isChecked());
                    list.get(position).setPraise(holder.praiseView.isChecked());
                }
            });
            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                // 表示手指拖动seekbar完毕，手指离开屏幕会触发以下方法
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // 让计时器延时执行
                    mainHandler.postDelayed(r, HIDDEN_TIME);
                }

                // 在手指正在拖动seekBar，而手指未离开屏幕触发的方法
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // 让计时器取消计时
                    mainHandler.removeCallbacks(r);
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    if (fromUser) {
                        int playtime = progress * mVideoView.getDuration() / 100;
                        mVideoView.seekTo(playtime);
                    }

                }
            });
            if (mVideoView != null) {
                holder.play.setImageResource(list.get(position).isPlaying() ? R.mipmap.icon_video_pause : R.mipmap.icon_video_play);
            }
            holder.bottonLayout.setVisibility(View.INVISIBLE);
//            currentBotton = holder.bottonLayout;
//            currentSeekBar = holder.seekBar;
//            currentPlayTime = holder.playtime;
            holder.video.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!holder.videoPlayBtn.isShown()) {
                                showOrHiddenController(holder.bottonLayout);
                            }
                            break;
                    }
                    return true;
                }
            });
            holder.play.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (holder.videoPlayBtn.getVisibility() == View.VISIBLE) {
//                        currentIndex = mPosition;
                        playPosition = -1;
                        for (int i = 0; i < list.size(); i++) {
                            if (i != position) {
                                list.get(i).setPlaying(false);
                            } else {
                                list.get(i).setPlaying(true);
                            }
                        }
                        newVideoAdapter.notifyDataSetChanged();
                    } else {
                        if (list.get(position).isPlaying()) {
                            mVideoView.pause();
                            holder.play.setImageResource(R.mipmap.icon_video_play);
                            list.get(position).setPlaying(false);
                        } else {
                            mVideoView.start();
                            holder.play.setImageResource(R.mipmap.icon_video_pause);
                            list.get(position).setPlaying(true);
                        }
                    }
                }
            });
            holder.full.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullScreen();
                }
            });
//            mediaController = new MediaController(mContext);
            if (currentIndex == position) {
                holder.videoPlayBtn.setVisibility(View.INVISIBLE);
                holder.videoImage.setVisibility(View.INVISIBLE);
                holder.bottonLayout.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (holder.bottonLayout.isShown()) {
                            holder.bottonLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                }, HIDDEN_TIME);
//                holder.videoNameText.setVisibility(View.INVISIBLE);

                if (isPlaying || playPosition == -1) {
                    if (mVideoView != null) {
                        mVideoView.setVisibility(View.GONE);
                        mVideoView.stopPlayback();
                        holder.bottonLayout.setVisibility(View.INVISIBLE);
                        holder.mProgressBar.setVisibility(View.GONE);
                    }
                }
                mVideoView = (VideoView) convertView.findViewById(R.id.videoview);
                mVideoView.setVisibility(View.VISIBLE);
//                mediaController = (MediaController) convertView.findViewById(R.id.mediaController);
//                mediaController.setAnchorView(holder.video);
//                mediaController.setMediaPlayer(mVideoView);
//                mVideoView.setMediaController(mediaController);
//                videoDialog.setMediaController(mediaController);
                mVideoView.requestFocus();
                holder.mProgressBar.setVisibility(View.VISIBLE);
                if (playPosition > 0 && isPaused) {
//                    mVideoView.start();
//                    isPaused = false;
//                    isPlaying = true;
                    holder.mProgressBar.setVisibility(View.GONE);
                } else {
                    String path = Environment.getExternalStorageDirectory().getPath() + "/" + list.get(mPosition).getPath();
//                    String path = list.get(mPosition).getPath();
                    Uri uri = Uri.parse(path);
                    currentPath = path;
                    mVideoView.setVideoURI(uri);
                    isPaused = false;
                    isPlaying = true;
//                    CommonUtils.showToast(mContext, "播放新的视频");
                    holder.bottonLayout.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (holder.bottonLayout.isShown()) {
                                holder.bottonLayout.setVisibility(View.INVISIBLE);
                            }
                        }
                    }, HIDDEN_TIME);
                }
                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mVideoView != null) {
                            mVideoView.seekTo(0);
                            mVideoView.stopPlayback();
                            currentIndex = -1;
                            isPaused = false;
                            isPlaying = false;
                            holder.mProgressBar.setVisibility(View.GONE);
                            newVideoAdapter.notifyDataSetChanged();
                        }
                    }
                });
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        holder.mProgressBar.setVisibility(View.GONE);
                        mVideoView.start();
                        holder.totalTime.setText(formatTime(mVideoView.getDuration()));
                        // 初始化定时器
                        timer = new Timer();
                        timer.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                handler.sendEmptyMessage(WHAT);

                            }
                        }, 0, 1000);
                    }
                });
            } else {
                holder.videoPlayBtn.setVisibility(View.VISIBLE);
                holder.videoImage.setVisibility(View.VISIBLE);
                holder.bottonLayout.setVisibility(View.INVISIBLE);
//                holder.videoNameText.setVisibility(View.VISIBLE);
                holder.mProgressBar.setVisibility(View.GONE);
            }

            holder.videoPlayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex = mPosition;
                    playPosition = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (i != position) {
                            list.get(i).setPlaying(false);
                        } else {
                            list.get(i).setPlaying(true);
                        }
                    }
                    currentBotton = holder.bottonLayout;
                    newVideoAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    private String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private void showOrHiddenController(final LinearLayout bottonLayout) {
        if (bottonLayout.isShown()) {
            bottonLayout.setVisibility(View.INVISIBLE);
            mainHandler.removeCallbacks(r);
        } else {
            bottonLayout.setVisibility(View.VISIBLE);
            mainHandler.postDelayed(r, HIDDEN_TIME);
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (mVideoView != null && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
////
////            // scrollDistance = currentItemView.getTop();
////
////            //获取状态栏高度(如果设置沉浸式状态栏了就不需要获取)
////            Rect rect = new Rect();
////            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
////            lv.getChildAt(currentIndex).setLayoutParams(new AbsListView.
////                    LayoutParams(ListView.LayoutParams.MATCH_PARENT,
////                    getActivity().getWindowManager().getDefaultDisplay().getHeight() - rect.top));
////            //设置横屏后要显示的当前的 itemView
//            lv.post(new Runnable() {
//                @Override
//                public void run() {
//                    //一定要对添加这句话,否则无效,因为界面初始化完成后 listView 失去了焦点
//                    lv.requestFocusFromTouch();
//                    lv.setSelection(currentIndex);
//                }
//            });
//            Log.i("XX", "横屏");
//        } else if (mVideoView != null && newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
////            //横屏时的设置会影响返回竖屏后的效果, 这里设置高度与 xml 文件中的高度相同
////            Log.i("MM", currentIndex + "竖屏");
////            lv.getChildAt(currentIndex).setLayoutParams(new AbsListView.LayoutParams(
////                    ListView.LayoutParams.MATCH_PARENT, getResources()
////                    .getDimensionPixelOffset(R.dimen.itmes_height)));
////            //本来想切换到竖屏后恢复到初始位置,但是上部出现空白
//////            videoList.scrollBy(0, -(scrollDistance));
////            //通过该方法恢复位置,不过还是有点小问题
//            lv.post(new Runnable() {
//                @Override
//                public void run() {
//                    lv.requestFocusFromTouch();
//                    lv.setSelection(lv.getFirstVisiblePosition());
//                }
//            });
////            Log.i("XX", "竖屏");
//        }
//    }

    private void fullScreen() {
        showDialog();
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void showDialog() {
        playPosition = mVideoView.getCurrentPosition();
        int currentPosition = mVideoView.getCurrentPosition();
        videoDialog.setPlay(mVideoView.isPlaying());
        videoDialog.setPath(currentPath);
        videoDialog.setPosition(playPosition);
        videoDialog.show();
        isPaused = true;
        isPlaying = false;
        mVideoView.pause();
    }

    static class ViewHolder {
        ImageView videoImage;
        TextView videoNameText;
        ImageButton videoPlayBtn;
        ProgressBar mProgressBar;
        ImageView full;
        RelativeLayout video;
        ImageView play;
        LinearLayout bottonLayout;
        SeekBar seekBar;
        TextView playtime;
        TextView totalTime;
        CheckedImageView praiseView;
        TextView add;
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_video, null);
        setRetainInstance(true);
        return mView;
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof VideoRequest) {
            VideoResponse videoResponse = (VideoResponse) response;
            if (videoResponse != null) {

            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
