package com.it.hgad.logisticsmanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.DownloadPicAdapter;
import com.it.hgad.logisticsmanager.adapter.MaterialDetailAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Material;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.JobDetailRequest;
import com.it.hgad.logisticsmanager.bean.response.JobDetailResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.MaterialDb;
import com.it.hgad.logisticsmanager.dao.UseMaterialDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2016/12/28.
 */
public class JobDetailActivity extends CommonActivity {
    private ListView lv_material;
    private List<Material> data = new ArrayList<>();
    private Order order;
    private TextView mTv_from;
    private TextView mTv_phone;
    private TextView mTv_department;
    private TextView mTv_address;
    private TextView mTv_regist;
    private TextView mTv_content;
    private TextView mTv_type;
    private TextView mTv_name;
    private TextView mTv_time;
    private TextView mTv_hang_time;
    private TextView mTv_reason;
    private TextView mTv_main_people;
    private TextView mTv_finish_time;
    private TextView mTv_feedback;
    private DbUtils db;
    private MaterialDetailAdapter materialDetailAdapter;
    private List<String> pics = new ArrayList<>();
    private List<String> picsBefore = new ArrayList<>();
    private GridView gl_maintenance;
    private DownloadPicAdapter picAdapter;
    private GridView gl_maintenance_before;
    private DownloadPicAdapter picBeforeAdapter;
    private LinearLayout ll_not_same;
    private TextView tv_not_same;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STOP:
                    customProgressDialog.dismiss();
                    break;
            }
        }
    };
    private boolean notConnect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        initHeader("工单详情");
        initView();
        initData();

    }

    private void initData() {
        if (db == null) {
            db = ((LocalApp) getApplication()).getDb();
            try {
                db.createTableIfNotExist(MaterialDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.DATA);
        mTv_from.setText(order.getRepairSrc());
        mTv_phone.setText(order.getRepairTel());
        mTv_department.setText(order.getRepairDept());
        mTv_address.setText(order.getRepairLoc());
        mTv_regist.setText(order.getRegisterMan());
        mTv_content.setText(order.getRepairContent());
        mTv_type.setText(order.getRepairType());
        mTv_name.setText(order.getRepairMan());
        String registerTime = order.getRegisterTime();
        if (registerTime != null) {
            registerTime = registerTime.replace("T", " ");
        }
        mTv_time.setText(registerTime);
        String shelveTime = order.getShelveTime();
        if (shelveTime != null) {
            shelveTime = shelveTime.replace("T", " ");
        }
        mTv_hang_time.setText(shelveTime);
        mTv_reason.setText(order.getShelveReason());
        String finishTime = order.getFinishTime();
        if (finishTime != null) {
            finishTime = finishTime.replace("T", " ");
        }
        mTv_finish_time.setText(finishTime);
        mTv_feedback.setText(order.getRepairReply());
        String realName = order.getUserNames();
        mTv_main_people.setText(realName);
        String repairImg = order.getRepairImg();
        if (repairImg != null) {
            repairImg = repairImg.replace('|', ',');
//        List<String> list = Arrays.asList(repairImg);
            String[] split = repairImg.split(",");
            for (String imagePath : split
                    ) {
                if (imagePath != null && !"".equals(imagePath)) {
                    imagePath = imagePath.toString().trim();
                    pics.add(imagePath);
                }
            }
            picAdapter.notifyDataSetChanged();
        }
        String spotImg = order.getSpotImg();
        if (spotImg != null) {
            spotImg = spotImg.replace('|', ',');
//        List<String> list = Arrays.asList(repairImg);
            String[] split = spotImg.split(",");
            for (String imagePath : split
                    ) {
                if (imagePath != null && !"".equals(imagePath)) {
                    imagePath = imagePath.toString().trim();
                    picsBefore.add(imagePath);
                }
            }
            picBeforeAdapter.notifyDataSetChanged();
        }
        String sameCondition = order.getSameCondition();
        if (sameCondition == null || sameCondition.equals("")) {
            ll_not_same.setVisibility(View.GONE);
        } else {
            ll_not_same.setVisibility(View.VISIBLE);
            tv_not_same.setText(sameCondition);
        }
        boolean checkNetWork = Utils.checkNetWork(JobDetailActivity.this);
        if (!checkNetWork) {
//            try {
//                UseMaterialDb useMaterialDb = db.findById(UseMaterialDb.class, order.getId());
//                for (int i = 0; i < useMaterialDb.getMaterialId().size(); i++) {
//                    Integer id = useMaterialDb.getMaterialId().get(i);
//                    String name = useMaterialDb.getName().get(i);
//                    String type = useMaterialDb.getType().get(i);
//                    Integer count = useMaterialDb.getNumber().get(i);
//                    String price = useMaterialDb.getPrice().get(i);
//                    data.add(new Material(id, name, type, count, false, price));
//                }
//
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
//            materialDetailAdapter.notifyDataSetChanged();
            customProgressDialog.dismiss();
            CommonUtils.showToast(JobDetailActivity.this,getString(R.string.no_net));
        } else {
            JobDetailRequest jobDetailRequest = new JobDetailRequest(order.getId() + "");
            sendRequest(jobDetailRequest, JobDetailResponse.class);
        }
    }

    private void initView() {
        customProgressDialog = new CustomProgressDialog(this, "数据加载中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (notConnect) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        Utils.showTost(JobDetailActivity.this, "网络不给力！");
                    }
                }
            }
        }, 10000);
        lv_material = ((ListView) findViewById(R.id.lv_material));
        materialDetailAdapter = new MaterialDetailAdapter(data);
        lv_material.setAdapter(materialDetailAdapter);
        mTv_from = (TextView) findViewById(R.id.tv_from);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mTv_department = (TextView) findViewById(R.id.tv_department);
        mTv_address = (TextView) findViewById(R.id.tv_address);
        mTv_regist = (TextView) findViewById(R.id.tv_regist);
        mTv_content = (TextView) findViewById(R.id.tv_content);
        mTv_type = (TextView) findViewById(R.id.tv_type);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mTv_hang_time = (TextView) findViewById(R.id.tv_hang_time);
        mTv_reason = (TextView) findViewById(R.id.tv_reason);
        mTv_main_people = (TextView) findViewById(R.id.tv_main_people);
        mTv_finish_time = (TextView) findViewById(R.id.tv_finish_time);
        mTv_feedback = (TextView) findViewById(R.id.tv_feedback);
        picAdapter = new DownloadPicAdapter(JobDetailActivity.this, pics);
        picBeforeAdapter = new DownloadPicAdapter(JobDetailActivity.this, picsBefore);
        gl_maintenance = (GridView) findViewById(R.id.gl_maintenance);
        gl_maintenance_before = (GridView) findViewById(R.id.gl_maintenance_before);
        initGridView(gl_maintenance, picAdapter);
        initGridView(gl_maintenance_before, picBeforeAdapter);
        ll_not_same = (LinearLayout) findViewById(R.id.ll_not_same);
        tv_not_same = (TextView) findViewById(R.id.tv_not_same);
//        mIv_detail = (ImageView) findViewById(R.id.iv_detail);
    }

    private void initGridView(GridView gridView, DownloadPicAdapter adapter) {
        gridView.setAdapter(adapter);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        gl_maintenance.setLayoutParams(params);
        gridView.setNumColumns(3);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof JobDetailRequest) {
            JobDetailResponse jobDetailResponse = (JobDetailResponse) response;
            if (jobDetailResponse != null) {
                for (JobDetailResponse.DataEntity entity : jobDetailResponse.getData()) {
                    JobDetailResponse.DataEntity.MaterialInfoEntity materialInfo = entity.getMaterialInfo();
                    int id = materialInfo.getId();
                    String materialName = materialInfo.getMaterialName();
                    String materialNum = materialInfo.getBalanceNum();
                    String materialPrice = materialInfo.getMaterialPrice();
                    String specification = materialInfo.getSpecification();
                    int num = Integer.parseInt(materialNum.trim());
                    Material material = new Material(id, materialName, specification, num, false, materialPrice);
                    data.add(material);
                }
//            try {
//                saveToDb(data);
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
                materialDetailAdapter.notifyDataSetChanged();
            }
            notConnect = false;
            handler.sendEmptyMessageDelayed(Constants.STOP, 500);
        }
    }

    private void saveToDb(List<Material> data) throws DbException {
        Boolean have = false;
        List<Integer> materialId = new ArrayList<>();
        List<String> materialName = new ArrayList<>();
        List<String> materialType = new ArrayList<>();
        List<Integer> materialCount = new ArrayList<>();
        List<String> materialPrice = new ArrayList<>();
        for (Material material : data) {
            materialId.add(material.getId());
            materialName.add(material.getName());
            materialType.add(material.getType());
            materialCount.add(material.getNumber());
            materialPrice.add(material.getPrice() + "");
        }
        UseMaterialDb useMaterialDb = new UseMaterialDb(order.getId(), materialId, materialName, materialType, materialCount, materialPrice);
        List<UseMaterialDb> all = db.findAll(UseMaterialDb.class);

        for (UseMaterialDb useMaterialDb1 : all) {
            if (useMaterialDb1 != null) {
                if (useMaterialDb.getId() == useMaterialDb1.getId()) {
                    have = true;
                }
            }
        }
        if (!have) {
            db.save(useMaterialDb);
        }
    }
}
