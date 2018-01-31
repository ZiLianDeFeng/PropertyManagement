package com.it.hgad.logisticsmanager.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.PullResultActivity;
import com.it.hgad.logisticsmanager.adapter.MessageAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.MessageData;
import com.it.hgad.logisticsmanager.bean.request.MessageRequest;
import com.it.hgad.logisticsmanager.bean.response.MessageResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.MessageDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/4/13.
 */
public class NotificationFragment extends BaseFragment {

    private View mView;
    private List<MessageData> listData = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private XListView lv;
    //参数名
    private List<String> paramNames = new ArrayList<>();
    //参数值
    private List<String> paramValues = new ArrayList<>();
    //查询条件 like or =
    private List<String> paramConditons = new ArrayList<>();
    //数据类型 string Integer
    private List<String> paramTypes = new ArrayList<>();
    private int currentPage = 1;
    private int userId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.STOP:
                    rl_info.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    messageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private Animation operatingAnim;
    private ImageView infoOperating;
    private RelativeLayout rl_info;
    private DbUtils db;

    @Override
    protected void initData() {
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(MessageDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 10000);
            }
            userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
            paramNames.add(0, "notice.type");
            paramNames.add(1, "notice.userId");
            paramValues.add(0, "维修");
            paramValues.add(1, userId + "");
            paramConditons.add(0, "=");
            paramConditons.add(1, "=");
            paramTypes.add(0, "string");
            paramTypes.add(1, "string");
            MessageRequest messageRequest = new MessageRequest(paramNames, paramValues, paramConditons, paramTypes, currentPage);
            sendRequest(messageRequest, MessageResponse.class);
            currentPage--;
        }
    }

    @Override
    protected void initView() {
        lv = (XListView) mView.findViewById(R.id.lv_message);
        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
        messageAdapter = new MessageAdapter(listData, mContext);
        lv.setAdapter(messageAdapter);
        lv.setXListViewListener(listener);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setEmptyView(tv_empty);
        lv.setOnItemClickListener(onItemClickListener);
        initAnimation();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageData messageData = listData.get(position - 1);
            int messageId = messageData.getId();
            String content = messageData.getContent();
            String time = messageData.getTime();
            String type = messageData.getType();
            MessageDb messageDb = new MessageDb(messageId, content, time, type, true);
            try {
                db.saveOrUpdate(messageDb);
            } catch (DbException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(mContext, PullResultActivity.class);
            startActivity(intent);
        }
    };

    private void initAnimation() {
        rl_info = (RelativeLayout) mView.findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) mView.findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    @Override
    public void onResume() {
        super.onResume();
        check();
    }

    private void check() {
        try {
            List<MessageDb> messageDbs = db.findAll(MessageDb.class);
            for (MessageData messageData : listData
                    ) {
                int id = messageData.getId();
                for (MessageDb messageDb :
                        messageDbs) {
                    if (messageDb.getId() == id) {
                        messageData.setHavaRead(true);
                    }
                }
            }
            messageAdapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private XListView.IXListViewListener listener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            callRefresh();
            lv.setRefreshTime(CommonUtils.getCurrentTime());
            lv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            mCurrentIndex = messageAdapter.getCount();
            callLoadMore();
            lv.stopLoadMore();
        }
    };

    private int mCurrentIndex = 0;

    private void callRefresh() {
        mCurrentIndex = 0;
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            currentPage = 1;
            if (listData.size() != 0) {
                lv.setSelection(mCurrentIndex);
            }
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                handler.sendEmptyMessageDelayed(Constants.STOP, 2000);
            }
            MessageRequest messageRequest = new MessageRequest(paramNames, paramValues, paramConditons, paramTypes, currentPage);
            sendRequest(messageRequest, MessageResponse.class);
            currentPage--;
        } else {
        }
    }

    private void callLoadMore() {
        boolean isNetWork = Utils.checkNetWork(mContext);
        if (isNetWork) {
            currentPage++;
            MessageRequest messageRequest = new MessageRequest(paramNames, paramValues, paramConditons, paramTypes, currentPage);
            sendRequest(messageRequest, MessageResponse.class);
            currentPage--;
        } else {
        }
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_notification, null);
        return mView;
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof MessageRequest) {
            MessageResponse messageResponse = (MessageResponse) response;
            if (messageResponse.getData() != null) {
                currentPage++;
                if (currentPage == 1) {
                    if (listData != null) {
                        listData.clear();
                    }
                }
                if (currentPage == messageResponse.getData().getCurrentpage()) {
                    List<MessageResponse.DataEntity.ListdataEntity> listdata = messageResponse.getData().getListdata();
                    try {
                        List<MessageDb> messageDbs = db.findAll(MessageDb.class);
                        for (MessageResponse.DataEntity.ListdataEntity entity : listdata
                                ) {
                            String type = entity.getType();
                            String content = entity.getContent();
                            String sendTime = entity.getSendTime();
                            int id = entity.getId();
                            boolean havaRead = false;
                            for (MessageDb messageDb :
                                    messageDbs) {
                                if (messageDb.getId() == id) {
                                    havaRead = true;
                                }
                            }
                            MessageData messageData = new MessageData(content, sendTime, type, id, havaRead);
                            listData.add(messageData);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessageDelayed(Constants.STOP, 200);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
