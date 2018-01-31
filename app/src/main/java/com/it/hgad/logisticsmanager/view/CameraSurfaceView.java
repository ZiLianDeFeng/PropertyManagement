package com.it.hgad.logisticsmanager.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/31.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera = null;
    private SurfaceHolder surfaceHolder = null;
    private Context context;

    public CameraSurfaceView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        initCamera();
    }

    public CameraSurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
//            camera.setPreviewDisplay(surfaceHolder);
//            camera.startPreview();
            initCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initCamera() {

        if (camera != null) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                //设置闪光灯为自动
//                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                camera.setParameters(parameters);
                resetCameraSize(parameters);

                // 设置每秒显示4帧
//              parameters.setPreviewFrameRate(4);
                // 设置图片格式
                parameters.setPictureFormat(PixelFormat.JPEG);
                // 设置JPG照片的质量
                parameters.set("jpeg-quality", 100);

                // android2.3后不需要下面代码
                // camera.setParameters(parameters);
                // 通过SurfaceView显示取景画面
                camera.setPreviewDisplay(surfaceHolder);

                // 开始预览
                camera.startPreview();
                // 自动对焦
                camera.autoFocus(null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            isPreview = true;
        }
    }

    /**
     * 旋转相机和设置预览大小
     *
     * @param parameters
     */
    public void resetCameraSize(Camera.Parameters parameters) {
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            // 如果是竖屏
            camera.setDisplayOrientation(90);
        } else {
            camera.setDisplayOrientation(0);
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        // 设置预览的大小
        parameters.setPreviewSize(width, height);
        // 设置照片大小
        parameters.setPictureSize(width/100, height/100);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        //根本没有可处理的SurfaceView
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        //先停止Camera的预览
        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //这里可以做一些我们要做的变换。

        //重新开启Camera的预览功能
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
