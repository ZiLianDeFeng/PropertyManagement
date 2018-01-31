package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.MaterialAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Material;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/12.
 */
public class MaintainMaterialActivity extends CommonActivity {

    private ListView lv;
    private List<Material> materialData = new ArrayList<>();
    private MaterialAdapter materialAdapter;
    private TextView tv_edit;
    private CheckBox cb_check_all;
    private Button btn_delete;
//    private Button btn_cancel;
    private Button btn_save;
    private Button btn_add;
    private TextView tv_confirm;
    private TextView tv_check_all;
    private DbUtils db;
    private Order order;
    private CommitDb commitDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_material);
        initHeader("维修材料");
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER_DATA);
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(CommitDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        try {
            commitDb = db.findById(CommitDb.class, order.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (commitDb != null) {
            String repairMaterialNames = commitDb.getRepairMaterialName();
            String repairMaterialTypes = commitDb.getRepairMaterialType();
            String repairMaterialNums = commitDb.getRepairMaterialNum();
            String repairMaterialIds = commitDb.getRepairMaterialId();
            if (repairMaterialNames != null && !"".equals(repairMaterialNames)) {
                String[] mNames = repairMaterialNames.split(",");
                String[] mTypes = repairMaterialTypes.split(",");
                String[] mNums = repairMaterialNums.split(",");
                String[] mIds = repairMaterialIds.split(",");
                List<String> names = Arrays.asList(mNames);
                List<String> types = Arrays.asList(mTypes);
                List<String> nums = Arrays.asList(mNums);
                List<String> ids = Arrays.asList(mIds);
                for (int i = 0; i < names.size(); i++) {
                    String id = ids.get(i).trim();
                    String name = names.get(i).trim();
                    String type = types.get(i).trim();
                    String num = nums.get(i).trim();
                    int materialId = Integer.parseInt(id);
                    int count = Integer.parseInt(num);
                    Material material = new Material(materialId, name, type, count, false, "0");
                    materialData.add(material);
                }
            }
            materialAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        tv_edit = (TextView) findViewById(R.id.btn_edit);
        tv_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_edit.setText("编辑");
        tv_edit.setVisibility(View.VISIBLE);
        tv_edit.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.lv_maintain_material);
        materialAdapter = new MaterialAdapter(materialData);
        lv.setAdapter(materialAdapter);
        cb_check_all = (CheckBox) findViewById(R.id.cb_check_all);
        cb_check_all.setOnCheckedChangeListener(checkedChangeListener);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
//        btn_cancel = (Button) findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(this);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        tv_check_all = (TextView) findViewById(R.id.tv_check_all);
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for (Material material :
                        materialData) {
                    material.setChecked(true);
                }
                materialAdapter.notifyDataSetChanged();
            } else {
                for (Material material :
                        materialData) {
                    material.setChecked(false);
                }
                materialAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void back(View view) {
        backWarm();
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                materialAdapter.setCompile();
                showIsEditView();
                break;
            case R.id.btn_confirm:
                materialAdapter.setCompile();
                showUnEditView();
                break;
            case R.id.btn_delete:
                delete();
                break;
//            case R.id.btn_cancel:
//                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.btn_add:
                addMaterial();
                break;
        }
    }


    private List<String> materialId = new ArrayList<>();
    private List<String> materialName = new ArrayList<>();
    private List<String> materialType = new ArrayList<>();
    private List<String> materialCount = new ArrayList<>();
    private List<String> materialPrice = new ArrayList<>();

    private void save() {
//        if (materialData.size()==0){
//            CommonUtils.showToast(this,"还没有选择材料哦！");
//            return;
//        }
        if (commitDb == null) {
            commitDb = new CommitDb();
            commitDb.setRepairId(order.getId());
        }
        materialId.clear();
        materialName.clear();
        materialType.clear();
        materialCount.clear();
        materialPrice.clear();
        for (int i = 0; i < materialData.size(); i++) {
            Material material = materialData.get(i);
            materialId.add(material.getId() + "");
            materialName.add(material.getName());
            materialType.add(material.getType());
            materialCount.add(material.getNumber() + "");
            materialPrice.add(material.getPrice());
        }
        String materialIds = materialId.toString().substring(1, materialId.toString().length() - 1);
        String materialNames = materialName.toString().substring(1, materialName.toString().length() - 1);
        String specification = materialType.toString().substring(1, materialType.toString().length() - 1);
        String materialNum = materialCount.toString().substring(1, materialCount.toString().length() - 1);
        String materialPrices = materialPrice.toString().substring(1, materialPrice.toString().length() - 1);
        commitDb.setRepairMaterialId(materialIds);
        commitDb.setRepairMaterialName(materialNames);
        commitDb.setRepairMaterialType(specification);
        commitDb.setRepairMaterialNum(materialNum);
        commitDb.setRepairMaterialPrice(materialPrices);
        ExecutorService thread = SingleThreadPool.getThread();
        thread.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    db.saveOrUpdate(commitDb);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        finish();
    }

    private void delete() {
        List<Material> del = new ArrayList<>();
        for (int i = 0; i < materialData.size(); i++) {
            Material material = materialData.get(i);
            if (material.isChecked()) {
                del.add(material);
            }
        }
        for (Material material : del) {
            materialData.remove(material);
        }
        materialAdapter.notifyDataSetChanged();
    }

    private void addMaterial() {
        Intent intent = new Intent(MaintainMaterialActivity.this, AddMaterialActivity.class);
        intent.putExtra(Constants.HAS_ADD, (Serializable) materialData);
        startActivityForResult(intent, Constants.IS_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.IS_ADD:
                if (data != null) {
                    List<Material> add = (List<Material>) data.getSerializableExtra(Constants.DATA);
                    for (int i = 0; i < add.size(); i++) {
                        Material material = add.get(i);
                        material.setChecked(false);
                        materialData.add(material);
                    }
                    materialAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void showUnEditView() {
        tv_confirm.setVisibility(View.INVISIBLE);
        btn_delete.setVisibility(View.INVISIBLE);
        cb_check_all.setVisibility(View.INVISIBLE);
        tv_check_all.setVisibility(View.INVISIBLE);
        btn_add.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.VISIBLE);
//        btn_cancel.setVisibility(View.VISIBLE);
        tv_edit.setVisibility(View.VISIBLE);
        if (cb_check_all.isChecked()) {
            cb_check_all.setChecked(false);
        } else {
            for (Material material :
                    materialData) {
                material.setChecked(false);
            }
            materialAdapter.notifyDataSetChanged();
        }
    }

    private void showIsEditView() {
        tv_confirm.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.VISIBLE);
        cb_check_all.setVisibility(View.VISIBLE);
        tv_check_all.setVisibility(View.VISIBLE);
        btn_add.setVisibility(View.INVISIBLE);
        btn_save.setVisibility(View.INVISIBLE);
//        btn_cancel.setVisibility(View.INVISIBLE);
        tv_edit.setVisibility(View.INVISIBLE);
    }
}
