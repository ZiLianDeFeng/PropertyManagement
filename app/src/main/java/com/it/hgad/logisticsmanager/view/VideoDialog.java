package com.it.hgad.logisticsmanager.view;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.it.hgad.logisticsmanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/2.
 */
public class VideoDialog extends Dialog {

    private static final int HIDDEN_TIME = 5000;
    private Context mContext;
    private VideoView videoView;
    private String path;
    private int position;
    private MediaController mediaController;
    private ImageView full;
    private DialogCallBackListener dialogCallBackListener;
    private LinearLayout botton_layout;
    private final static int WHAT = 0;

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // 又回到了主线程
            showOrHiddenController();
        }
    };
    private Timer timer;
    private TextView tv_playTime;
    private TextView tv_totalTime;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT:
                    if (videoView != null) {
                        int currentPlayer = videoView.getCurrentPosition();
                        if (currentPlayer > 0) {
                            videoView.getCurrentPosition();
                            tv_playTime.setText(formatTime(currentPlayer));
                            // 让seekBar也跟随改变
                            int progress = (int) ((currentPlayer / (float) videoView
                                    .getDuration()) * 100);

                            seekBar.setProgress(progress);
                        } else {
                            tv_playTime.setText("00:00");
                            seekBar.setProgress(0);
                        }
                    }

                    break;

                default:
                    break;
            }
        }
    };
    private SeekBar seekBar;
    private boolean isPlay;
    private ImageView iv_play;

    public VideoDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    public void setMediaController(MediaController mediaController) {
        this.mediaController = mediaController;
    }

    public void setDialogCallBackListener(DialogCallBackListener dialogCallBackListener) {
        this.dialogCallBackListener = dialogCallBackListener;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void show() {
        super.show();
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.seekTo(position);
        videoView.start();
        if (isPlay) {
            iv_play.setImageResource(R.mipmap.icon_video_pause);
        } else {
            videoView.pause();
            iv_play.setImageResource(R.mipmap.icon_video_play);
        }
//        mediaController.setPadding(50, 50, 50, 200);
//        videoView.setMediaController(mediaController);
//        videoView.requestFocus();
//        mediaController = new MediaController(mContext);
//        mediaController.setAnchorView(videoView);
//        mediaController.setMediaPlayer(videoView);

    }

    private void initData() {

    }


    private void initView() {
        setContentView(R.layout.dialog_video);
        videoView = (VideoView) findViewById(R.id.videoview);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                exitFull();
            }
        });
        botton_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        botton_layout.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (botton_layout.isShown()) {
                    botton_layout.setVisibility(View.INVISIBLE);
                }
            }
        }, HIDDEN_TIME);
        RelativeLayout rl_full = (RelativeLayout) findViewById(R.id.rl_full);
        rl_full.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showOrHiddenController();
                        break;
                }
                return true;
            }
        });
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    iv_play.setImageResource(R.mipmap.icon_video_play);
                } else {
                    videoView.start();
                    iv_play.setImageResource(R.mipmap.icon_video_pause);
                }
            }
        });
        full = (ImageView) findViewById(R.id.iv_full);
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFull();
            }
        });
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // 表示手指拖动seekbar完毕，手指离开屏幕会触发以下方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 让计时器延时执行
                handler.postDelayed(r, HIDDEN_TIME);
            }

            // 在手指正在拖动seekBar，而手指未离开屏幕触发的方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 让计时器取消计时
                handler.removeCallbacks(r);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    int playtime = progress * videoView.getDuration() / 100;
                    videoView.seekTo(playtime);
                }

            }
        });
        tv_playTime = (TextView) findViewById(R.id.tv_playtime);
        tv_totalTime = (TextView) findViewById(R.id.tv_totaltime);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                tv_totalTime.setText(formatTime(videoView.getDuration()));
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
    }

    private String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private boolean canHide = false;
    private long lastTimeMillis = 0;

    private void showOrHiddenController() {
        if (botton_layout.isShown()) {
            botton_layout.setVisibility(View.INVISIBLE);
        } else {
//            long currentTimeMillis = System.currentTimeMillis();
//            if (lastTimeMillis != 0 && currentTimeMillis - lastTimeMillis >= HIDDEN_TIME) {
//                canHide = true;
//            } else {
//                canHide = false;
//            }
//            lastTimeMillis = currentTimeMillis;
            botton_layout.setVisibility(View.VISIBLE);
            handler.postDelayed(r, HIDDEN_TIME);
        }
    }

    @Override
    public void onBackPressed() {
        int currentPosition = videoView.getCurrentPosition();
        if (dialogCallBackListener != null) {
            dialogCallBackListener.callBack(currentPosition);
            dialogCallBackListener.isPlaying(videoView.isPlaying());
        }
        super.onBackPressed();
    }

    private void exitFull() {
        int currentPosition = videoView.getCurrentPosition();
        if (dialogCallBackListener != null) {
            dialogCallBackListener.callBack(currentPosition);
            dialogCallBackListener.isPlaying(videoView.isPlaying());
        }
        dismiss();
    }

    public void setPlay(boolean play) {
        this.isPlay = play;
    }

    public interface DialogCallBackListener {//通过该接口回调Dialog需要传递的值

        void callBack(int position);//具体方法

        void isPlaying(boolean isPlaying);
    }
}
