package com.it.hgad.logisticsmanager.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.LearnDetailActivity;
import com.it.hgad.logisticsmanager.adapter.LearnAdapter;
import com.it.hgad.logisticsmanager.bean.LearnData;
import com.it.hgad.logisticsmanager.bean.request.LearnRequest;
import com.it.hgad.logisticsmanager.bean.response.LearnResponse;
import com.it.hgad.logisticsmanager.constants.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/5/31.
 */
public class ArticleFragment extends BaseFragment {

    private View mView;
    private LearnAdapter learnAdapter;
    private List<LearnData> list = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    learnAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void initData() {
//        list.add(new LearnData(0, "维修技巧一之变频空调故障三大维修方法", null, false, null, "hot", 988));
//        list.add(new LearnData(1, "维修技巧二之汽车空调故障诊断方法", null, false, null, "hot", 1056));
//        list.add(new LearnData(2, "维修技巧三之制冷设备维修方法之自诊检查法", null, false, null, "hot", 846));
//        list.add(new LearnData(3, "维修技巧四之制冷设备维修方法之直观检查法", null, false, null, "new", 128));
//        list.add(new LearnData(4, "维修技巧五之制冷设备维修方法之应急拆除法", null, false, null, "new", 98));
//        list.add(new LearnData(5, "维修技巧六之按摩椅常见故障及维修方法", null, false, null, "new", 107));
//        list.add(new LearnData(6, "电磁阀有噪音故障原因分析", null, false, null, "normal", 334));
//        list.add(new LearnData(7, "分立元件组成的PC电源+5VSB电路检修", null, false, null, "normal", 243));
//        list.add(new LearnData(8, "同机芯不同屏的主板代用方法", null, false, null, "normal", 287));
//        list.add(new LearnData(9, "改造坑爹移动电源", null, false, null, "new", 56));
//        list.add(new LearnData(10, "超简单！为笔记本更换固态硬盘", null, false, null, "new", 98));
//        list.add(new LearnData(11, "超简单1！为笔记本更换固态硬盘", null, false, null, "new", 98));
//        list.add(new LearnData(12, "超简单2！为笔记本更换固态硬盘", null, false, null, "new", 98));
//        learnAdapter.notifyDataSetChanged();
        LearnRequest learnRequest = new LearnRequest();
        sendRequest(learnRequest, LearnResponse.class);
    }

    @Override
    protected void initView() {
        ListView lv = (ListView) mView.findViewById(R.id.lv_article);
        learnAdapter = new LearnAdapter(list, mContext, Constants.LEARN);
        lv.setAdapter(learnAdapter);
        lv.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_article, null);
        return mView;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mContext, LearnDetailActivity.class);
            intent.putExtra(Constants.LEARN, list.get(position).getPath());
            startActivity(intent);
        }
    };

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof LearnRequest) {
            LearnResponse learnResponse = (LearnResponse) response;
            if (learnResponse.getData() != null) {
                List<LearnResponse.DataEntity.ListdataEntity> listdata = learnResponse.getData().getListdata();
                for (LearnResponse.DataEntity.ListdataEntity listdataEntity : listdata) {
                    LearnData learnData = new LearnData(listdataEntity.getId(), listdataEntity.getTitle(), listdataEntity.getUrl(), false, null, "new", Integer.parseInt(listdataEntity.getClickNum()), listdataEntity.getCreateTime());
                    list.add(learnData);
                }
                handler.sendEmptyMessage(Constants.STOP);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
