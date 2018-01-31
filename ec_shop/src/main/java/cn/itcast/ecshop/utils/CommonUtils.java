package cn.itcast.ecshop.utils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/8/2.
 * 公共的工具类
 */
public class CommonUtils {
    private static  ExecutorService threadPool = Executors.newCachedThreadPool();
    //1、在子线程中执行任务
    public static void runInThread(Runnable task){
        threadPool.execute(task);
    }
    //2、创建handler对象,  Looper.getMainLooper()获取主线程里面的Looper对象
    private static Handler handler=new Handler(Looper.getMainLooper());
    public static Handler getHandler() {
        return handler;
    }
    //3、在主线程执行任务
    public static void runOnUIThread(Runnable task){
        handler.post(task);
    }
    //4、在主线程里面显示吐司  修改成静态吐司
    private static Toast toast;
    public static void showToast(final Context context, final String text){
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
               if (toast==null){
                   toast=Toast.makeText(context,"",Toast.LENGTH_SHORT);
               }
                toast.setText(text);
                toast.show();
            }
        });
    }
    //5、dp转px
    public static float dpToPx(Context context,float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
    //6、sp转px
    public static float spToPx(Context context,float sp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics());
    }
    //7、浮点数的估值
    public static Float evaluateFloat(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
    //8、颜色估值方法
    public static int evaluateArgb(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }

    //获取当前时间
    public static String getCurrentTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTime = format.format(new Date());
        return currentTime;
    }
    //获取状态栏的高度
    public static int getStatusBarHeight(View view){
        Rect rect=new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
}
