package com.it.hgad.logisticsmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.CheckCopyActivity;
import com.it.hgad.logisticsmanager.activity.CheckDetailActivity;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Param;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;
import com.lidroid.xutils.DbUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class ParamAdapter extends BaseAdapter {

    private Context mContext;
    private List<Param> listdata = new ArrayList<>();
    private int type;
    private DbUtils db;

    public ParamAdapter(Context mContext, List<Param> listdata, int type) {
        this.mContext = mContext;
        this.listdata = listdata;
        this.type = type;
        if (db == null) {
            db = LocalApp.getDb();
        }
    }

    @Override
    public int getCount() {
        return listdata == null ? 0 : listdata.size();
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_param);
        TextView paramName = holder.getTv(R.id.tv_param);
//        TextView paramRange = holder.getTv(R.id.tv_range);
//        final TextView reality = holder.getTv(R.id.tv_reality);
        final TextView state = holder.getTv(R.id.tv_state);

        LinearLayout ll_param = (LinearLayout) holder.convertView.findViewById(R.id.ll_param);
        final Param param = listdata.get(position);
        paramName.setText((position + 1) + "：" + param.getReferenceName());
        final String range = getRange(param.getReferenceStartValue(), param.getReferenceEndValue(), param.getReferenceUnit());
//        paramRange.setText("参考：" + range);
        final float startValue = Float.parseFloat(param.getReferenceStartValue());
        final float endValue = Float.parseFloat(param.getReferenceEndValue());
        final String paramActualValue = param.getActualValue();
        float actualValue = 0;
        if (paramActualValue != null && !"".equals(paramActualValue)) {
            actualValue = Float.parseFloat(paramActualValue);
        }
        if (paramActualValue == null || "".equals(paramActualValue)) {
            state.setText("未输入");
            state.setTextColor(Color.BLUE);
        } else if (actualValue >= startValue && actualValue <= endValue) {
            state.setText("正常");
            state.setTextColor(Color.BLACK);
        } else {
            state.setText("异常");
            state.setTextColor(Color.RED);
        }
//        reality.setText(param.getActualValue());
        if (type == CheckCopyActivity.TYPE) {
//            reality.setBackgroundResource(R.drawable.shape_circle_white);
            ll_param.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from(mContext);//提示框
                    final View dialogView = factory.inflate(R.layout.editbox_number_layout, null);//这里必须是final的
                    final EditText edit = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
                    final TextView tv_remark = (TextView) dialogView.findViewById(R.id.tv_remark);//获得输入框对象
                    final TextView tv_range = (TextView) dialogView.findViewById(R.id.tv_range);//获得输入框对象
//                    EditText et_abnomal = (EditText)dialogView.findViewById(R.id.et_abnomal);
                    String actualValue = param.getActualValue();
                    if (actualValue == null) {
                        edit.setText("");
                    } else {
                        edit.setText(actualValue);
                    }
                    String remark = param.getRemark();
                    tv_range.setText(range);
                    if (remark == null || remark.equals("")) {
                        tv_remark.setText("无");
                    } else {
                        tv_remark.setText(remark);
                    }
                    new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
//                            .setTitle("详情")//提示框标题
                            .setView(dialogView)
                            .setPositiveButton("确定",//提示框的两个按钮
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            //事件
                                            String et_reality = edit.getText().toString().trim();
                                            if (!TextUtils.isEmpty(et_reality)) {
//                                                reality.setText(et_reality);
                                                param.setActualValue(et_reality);
                                                float actualValue = Float.parseFloat(et_reality.toString().trim());
                                                if (actualValue >= startValue && actualValue <= endValue) {
                                                    state.setText("正常");
                                                    state.setTextColor(Color.BLACK);
                                                } else {
                                                    state.setText("异常");
                                                    state.setTextColor(Color.RED);
//                                                    reality.setTextColor(Color.RED);
                                                }
                                            } else {
                                                CommonUtils.showToast(mContext, "请填写实际值");
                                            }
                                            closeKeybord(edit, mContext);
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            closeKeybord(edit, mContext);
                        }
                    }).create().show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showInputMethod();
                        }
                    }, 100);
                }

                public void closeKeybord(EditText mEditText, Context mContext) {
                    InputMethodManager imm = (InputMethodManager) mContext.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }

                private void showInputMethod() {
                    //自动弹出键盘
                    InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    //强制隐藏Android输入法窗口
                    // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
                }
            });
        } else if (type == CheckDetailActivity.TYPE) {
            TextView tv_acture = holder.getTv(R.id.tv_acture);
            tv_acture.setVisibility(View.VISIBLE);
            String trueValue = param.getActualValue();
            tv_acture.setText(trueValue);
            if (trueValue != null && !"".equals(trueValue)) {
                if (actualValue >= startValue && actualValue <= endValue) {
                    state.setText("正常");
                    state.setTextColor(Color.BLACK);
                } else {
                    state.setText("异常");
                    state.setTextColor(Color.RED);
                }
            }
            ll_param.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from(mContext);//提示框
                    final View dialogView = factory.inflate(R.layout.text_number_layout, null);//这里必须是final的
                    final TextView tv_acture = (TextView) dialogView.findViewById(R.id.tv_acture);
                    final TextView tv_remark = (TextView) dialogView.findViewById(R.id.tv_remark);
                    final TextView tv_range = (TextView) dialogView.findViewById(R.id.tv_range);
                    String actualValue = param.getActualValue();
                    if (actualValue == null) {
                        tv_acture.setText("未输入");
                    } else {
                        tv_acture.setText(actualValue);
                    }
                    String remark = param.getRemark();
                    tv_range.setText(range);
                    if (remark == null || remark.equals("")) {
                        tv_remark.setText("无");
                    } else {
                        tv_remark.setText(remark);
                    }
                    new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
//                            .setTitle("详情")//提示框标题
                            .setView(dialogView)
                            .setNegativeButton("确定", null).create().show();
                }
            });
        }

        return holder.convertView;
    }

    private String getRange(String referenceStartValue, String referenceEndValue, String referenceUnit) {
        return referenceStartValue + "~" + referenceEndValue + "(" + referenceUnit + ")";
    }
}
