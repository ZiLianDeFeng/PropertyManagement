package com.it.hgad.logisticsmanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.SearchResultActivity;
import com.it.hgad.logisticsmanager.constants.CheckConstants;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class CheckHistoryFragment extends BaseFragment {

    private View mView;
    private TextView tv_state;
    private String should;
    private String done;
    private String over;
    private String overFinish;
    private String cancel;
    private int id;
    private EditText et_task_name;
    private EditText et_orderNo;
    private EditText et_device_name;
    //    private TextView tv_device_type;
//    private EditText et_plan_time;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        id = SPUtils.getInt(mContext, SPConstants.USER_ID);
        should = SPUtils.getString(mContext, CheckConstants.SHOULD);
        done = SPUtils.getString(mContext, CheckConstants.DONE);
        over = SPUtils.getString(mContext, CheckConstants.OVER);
        overFinish = SPUtils.getString(mContext, CheckConstants.OVER_FINISH);
        cancel = SPUtils.getString(mContext, CheckConstants.CANCEL);
        tv_state = (TextView) mView.findViewById(R.id.et_state);
        tv_state.setOnClickListener(this);
        Button btn_search = (Button) mView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        et_task_name = (EditText) mView.findViewById(R.id.et_task_name);
        et_orderNo = (EditText) mView.findViewById(R.id.et_orderNo);
        et_device_name = (EditText) mView.findViewById(R.id.et_device_name);
//        et_plan_time = (EditText) mView.findViewById(R.id.et_plan_time);
//        tv_device_type = (TextView) mView.findViewById(R.id.et_device_type);
//        tv_device_type.setOnClickListener(this);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_check_history, null);
        return mView;
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_state:
                chooseState();
                break;
//            case R.id.et_device_type:
//                chooseDeviceType();
//                break;
            case R.id.btn_search:
                search();
                break;
        }
    }

    private String[] types = {"全部", "配电", "空调", "锅炉", "医气", "污水"};

    private void chooseDeviceType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("设备类型");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                tv_device_type.setText(types[which]);
            }
        });
        builder.show();
    }

    private void search() {
        String state = tv_state.getText().toString().trim();
        String taskName = et_task_name.getText().toString().trim();
        String taskNo = et_orderNo.getText().toString().trim();
        String checkFlag = getStateInt(state);
        String deviceName = et_device_name.getText().toString().trim();
//        String deviceType = tv_device_type.getText().toString().trim();
//        String planTime = et_plan_time.getText().toString().trim();
        Intent intent = new Intent(mContext, SearchResultActivity.class);
        intent.putExtra(Constants.TASK_NAME, taskName);
        intent.putExtra(Constants.TASK_NO, taskNo);
        intent.putExtra(Constants.TASK_FLAG, checkFlag);
        intent.putExtra(Constants.DEVICE_NAME, deviceName);
//        intent.putExtra(Constants.DEVICE_TYPE, deviceType);
//        intent.putExtra(Constants.PLAN_TIME, planTime);
        startActivity(intent);
    }

    private String[] states = {"全部", "待巡检", "已完工", "已超时", "超时补录"};

    private void chooseState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("工单状态");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(states, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_state.setText(states[which]);
            }
        });
        builder.show();
    }

    public String getStateInt(String state) {
        String s = "";
        if (getString(R.string.state_all).equals(state)) {
            s = CheckConstants.ALL_CHECK;
        } else if (getString(R.string.should_check).equals(state)) {
            s = should;
        } else if (getString(R.string.check_done).equals(state)) {
            s = done;
        } else if (getString(R.string.check_over).equals(state)) {
            s = over;
        } else if (getString(R.string.over_finish).equals(state)) {
            s = overFinish;
        }
        return s;
    }
}
