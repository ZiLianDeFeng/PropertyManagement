package cn.itcast.ecshop.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Util {

    private static Toast toast;

    public static void showTost(Context context, String text) {

        if (toast == null) {

            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        toast.setText(text);
        toast.show();
    }
}
