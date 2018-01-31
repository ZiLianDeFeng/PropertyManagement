package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.AddAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Material;
import com.it.hgad.logisticsmanager.bean.request.MaterialRequest;
import com.it.hgad.logisticsmanager.bean.response.MaterialResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.MaterialDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2016/12/29.
 */
public class AddMaterialActivity extends CommonActivity {
    private static final int ADD_MATERIAL = 220;
    private TextView btn_confirm;
    private XListView lv_add;
    private List<MaterialResponse.DataEntity.ListdataEntity> listdata = new ArrayList<>();
    private List<Material> data = new ArrayList<>();
    private List<Material> hasAdd;
    private AddAdapter addAdapter;
    private Button btn_search;
    private EditText et_materialName;
    private int currentPage = 1;
    private String materialName = "";
    private boolean afterSearch = false;
    private DbUtils db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);
        initHeader("选择材料");
        initView();
        initData();

    }

    private void initData() {

        Intent intent = getIntent();
        hasAdd = (List<Material>) intent.getSerializableExtra(Constants.HAS_ADD);

        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(MaterialDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        boolean checkNetWork = Utils.checkNetWork(AddMaterialActivity.this);
        if (!checkNetWork) {
            lv_add.setPullLoadEnable(false);
            lv_add.setPullRefreshEnable(false);
                    try {
                        List<MaterialDb> all = db.findAll(MaterialDb.class);
                        if (all.size() != 0) {
                            for (MaterialDb db : all) {
                                data.add(new Material(db.getMaterialId(), db.getName(), db.getType(), db.getNumber(), false, db.getPrice()));
                            }
                        }
                        for (int i = 0; i < hasAdd.size(); i++) {
                            for (int j = 0; j < data.size(); j++) {
                                if (hasAdd.get(i).getId() == (data.get(j).getId())) {
                                    data.remove(j);
                                    break;
                                }
                            }
                        }
                        addAdapter.notifyDataSetChanged();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
        } else {
            MaterialRequest materialRequest = new MaterialRequest(materialName, currentPage);
            sendRequest(materialRequest, MaterialResponse.class);
        }


    }


    private void initView() {
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_search = (Button) findViewById(R.id.btn_search);
        et_materialName = (EditText) findViewById(R.id.et_materialName);
        btn_search.setOnClickListener(this);
        btn_confirm.setVisibility(View.VISIBLE);
        btn_confirm.setOnClickListener(this);
        lv_add = (XListView) findViewById(R.id.lv_add);
        addAdapter = new AddAdapter(data);
        lv_add.setAdapter(addAdapter);
        lv_add.setPullLoadEnable(true);
        lv_add.setXListViewListener(mIXListViewListener);
    }

    private int mCurrentIndex = 0;
    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            lv_add.setRefreshTime(CommonUtils.getCurrentTime());
            lv_add.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            currentPage = currentPage + 1;
            mCurrentIndex = addAdapter.getCount();
            MaterialRequest materialRequest = new MaterialRequest(materialName, currentPage);
            sendRequest(materialRequest, MaterialResponse.class);
            lv_add.stopLoadMore();
            lv_add.setSelection(mCurrentIndex);
            addAdapter.notifyDataSetChanged();
//            afterLoadMore = true;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                commit();
                break;
            case R.id.btn_search:
                search();
                break;
            default:
                break;
        }
    }

    private void search() {
        boolean isNetWork = Utils.checkNetWork(this);
        if (isNetWork) {
            afterSearch = true;
            materialName = et_materialName.getText().toString().trim();
            if (TextUtils.isEmpty(materialName)) {
                materialName = "";
            }
            currentPage = 1;
            MaterialRequest materialRequest = new MaterialRequest(materialName, currentPage);
            sendRequest(materialRequest, MaterialResponse.class);
            addAdapter.notifyDataSetChanged();
        } else {
            try {
                materialName = et_materialName.getText().toString().trim();
                List<MaterialDb> searchList = null;
                if (!TextUtils.isEmpty(materialName)) {
                    searchList = db.findAll(Selector.from(MaterialDb.class).where(MaterialDb.MATERIAL_NAME, "like", "%" + materialName + "%"));
                } else {
                    searchList = db.findAll(MaterialDb.class);
                }
                getMaterialData(searchList);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void getMaterialData(List<MaterialDb> searchList) {
        data.clear();
        for (MaterialDb materialDb : searchList) {
            data.add(new Material(materialDb.getMaterialId(), materialDb.getName(), materialDb.getType(), materialDb.getNumber(), false, materialDb.getPrice()));
        }
        addAdapter.notifyDataSetChanged();
    }

    private void commit() {
        List<Material> add = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked()) {
                add.add(data.get(i));
            }
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA, (Serializable) add);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data",(Serializable)add);
//                intent.putExtras(bundle);
        setResult(ADD_MATERIAL, intent);
        finish();
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof MaterialRequest) {
            MaterialResponse materialResponse = (MaterialResponse) response;

//        if(currentPage == 1){
            if (listdata != null) {
                listdata.clear();
            }
//        }
            if (afterSearch && currentPage == 1) {
                data.clear();
            }


            if (materialResponse.getData() != null && currentPage == materialResponse.getData().getCurrentpage()) {
                final List<MaterialResponse.DataEntity.ListdataEntity> entities = materialResponse.getData().getListdata();
                for (MaterialResponse.DataEntity.ListdataEntity entity : materialResponse.getData().getListdata()) {
                    listdata.add(entity);
                }
                for (MaterialResponse.DataEntity.ListdataEntity entity :listdata) {
                    String name = entity.getMaterialName();
                    String type = entity.getSpecification();
                    String price = entity.getMaterialPrice();
                    int id = entity.getId();
                    Material material = new Material(id, name, type, 1, false, price);
                    data.add(material);
                }
                ExecutorService thread = SingleThreadPool.getThread();
                thread.execute(new Runnable() {
                    @Override
                    public void run() {
                        getMaterialDb(entities);
                    }
                });
                lv_add.setPullLoadEnable(true);
                lv_add.setPullRefreshEnable(false);
            }
            for (int i = 0; i < hasAdd.size(); i++) {
                for (int j = 0; j < data.size(); j++) {
                    if (hasAdd.get(i).getId() == (data.get(j).getId())) {
                        data.remove(j);
                    }
                }
            }
            addAdapter.notifyDataSetChanged();
        }
    }

    private void getMaterialDb(final List<MaterialResponse.DataEntity.ListdataEntity> list) {
        for (MaterialResponse.DataEntity.ListdataEntity entity : list) {
            String name = entity.getMaterialName();
            String type = entity.getSpecification();
            String price = entity.getMaterialPrice();
            int id = entity.getId();
//            Boolean have = false;
            final MaterialDb materialDb = new MaterialDb(id, name, type, 1, price);

            try {
                db.saveOrUpdate(materialDb);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}
