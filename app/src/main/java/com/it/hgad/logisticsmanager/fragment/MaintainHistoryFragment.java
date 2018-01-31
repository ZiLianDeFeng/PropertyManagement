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
import com.it.hgad.logisticsmanager.activity.TaskMaintainActivity;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.RepairConstants;
import com.it.hgad.logisticsmanager.util.SPUtils;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MaintainHistoryFragment extends BaseFragment {

    private View mView;
    private String shouldRecieve;
    private String shouldStart;
    private String shouldDo;
    private String haveDone;
    private String haveShelve;
    private String haveFinish;
    private TextView tv_state;
    private EditText et_orderNo;
    private EditText et_tel;
//    private TextView tv_task_type;
    private TextView tv_event_type;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        shouldRecieve = SPUtils.getString(mContext, RepairConstants.SHOULD_RECIEVE);
        shouldStart = SPUtils.getString(mContext, RepairConstants.SHOULD_START);
        shouldDo = SPUtils.getString(mContext, RepairConstants.SHOULD_DO);
        haveDone = SPUtils.getString(mContext, RepairConstants.HAVE_DONE);
        haveShelve = SPUtils.getString(mContext, RepairConstants.HAVE_SHELVE);
        haveFinish = SPUtils.getString(mContext, RepairConstants.HAVE_FINISH);
        tv_state = (TextView) mView.findViewById(R.id.et_state);
        tv_state.setOnClickListener(this);
        Button btn_search = (Button) mView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        et_orderNo = (EditText) mView.findViewById(R.id.et_orderNo);
        et_tel = (EditText) mView.findViewById(R.id.et_tel);
//        tv_task_type = (TextView) mView.findViewById(R.id.et_task_type);
//        tv_task_type.setOnClickListener(this);
        tv_event_type = (TextView) mView.findViewById(R.id.et_event_type);
        tv_event_type.setOnClickListener(this);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_maintain_history, null);
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
            case R.id.et_event_type:
                chooseEventType();
                break;
//            case R.id.et_task_type:
//                chooseTaskType();
//                break;
            case R.id.btn_search:
                search();
                break;
        }
    }

    private String[] taskTypes = {"全部", "更换", "检修"};

    private void chooseTaskType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("报修类型");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(taskTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                tv_task_type.setText(taskTypes[which]);
            }
        });
        builder.show();
    }

    private String[] eventTypes = {"全部", "水", "电", "气", "门窗"};

    private void chooseEventType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("事件类型");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(eventTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_event_type.setText(eventTypes[which]);
            }
        });
        builder.show();
    }

    private void search() {
        Intent intent = new Intent(mContext, TaskMaintainActivity.class);
        String state = tv_state.getText().toString().trim();
        String repairFlag = getStateInt(state);
        String repairNo = et_orderNo.getText().toString().trim();
        String tel = et_tel.getText().toString().trim();
//        String taskType = tv_task_type.getText().toString().trim();
        String eventType = tv_event_type.getText().toString().trim();
        if ("全部".equals(eventType)) {
            eventType = "";
        }
        intent.putExtra(Constants.REPAIR_FLAG, repairFlag);
        intent.putExtra(Constants.REPAIR_NO, repairNo);
        intent.putExtra(Constants.REPAIR_TEL, tel);
//        intent.putExtra(Constants.REPAIR_TASK_TYPE, taskType);
        intent.putExtra(Constants.REPAIR_EVENT_TYPE, eventType);
        startActivity(intent);
    }

    private String[] states = {"全部", "待接收", "待开始", "待维修", "已挂起", "已维修", "结单"};

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
        String s = RepairConstants.ALL_REPAIR;
        if (RepairConstants.ALL.equals(state)) {
            s = RepairConstants.ALL_REPAIR;
        } else if (RepairConstants.SHOULD_RECIEVE.equals(state)) {
            s = shouldRecieve;
        } else if (RepairConstants.SHOULD_START.equals(state)) {
            s = shouldStart;
        } else if (RepairConstants.SHOULD_DO.equals(state)) {
            s = shouldDo;
        } else if (RepairConstants.HAVE_SHELVE.equals(state)) {
            s = haveShelve;
        } else if (RepairConstants.HAVE_DONE.equals(state)) {
            s = haveDone;
        } else if (RepairConstants.HAVE_FINISH.equals(state)) {
            s = haveFinish;
        }
        return s;
    }
}
