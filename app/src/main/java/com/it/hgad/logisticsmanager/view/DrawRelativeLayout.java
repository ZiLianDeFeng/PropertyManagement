package com.it.hgad.logisticsmanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by wanjiawen on 2016/8/11.
 */
public class DrawRelativeLayout extends RelativeLayout{
    public DrawRelativeLayout(Context context) {
        super(context);
    }

    public DrawRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // Log.d("test", "dianjile");

        return true;
    }
}
