package com.it.hgad.logisticsmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.MaterialAdapter;
import com.it.hgad.logisticsmanager.adapter.PicAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Material;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.bean.request.CommitRequest;
import com.it.hgad.logisticsmanager.bean.response.CommitResponse;
import com.it.hgad.logisticsmanager.bean.response.UpLoadPicResponse;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.constants.HttpConstants;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.util.BitmapUtils;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.it.hgad.myapplication.HandwritingActivity;
import com.it.hgad.myapplication.WritePadDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/27.
 */
public class MaintenanceActivity extends CommonActivity {
    public static final String HANDWRITE_CACHE = "/handwriteCache/";
    private static final int MAX_SIZE = 30;
    private static final int NOT_FIND = 0;
    private static final int NOTIFI = 10;
    private static final int NOTIFI_BEFORE = 20;
    private static final int UPLOAD_OK = 345;
    private ListView lv_material;
    private List<Material> data = new ArrayList<>();
    private ImageView tv_add;
    private TextView btn_confirm;
    private final int IS_ADD = 110;
    private ImageView tv_photo;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_BEFORE_REQUEST_TAKEPHOTO = 3;// 拍照
    private static final int PHOTO_BEFORE_REQUEST_GALLERY = 4;// 从相册中选择
    private GridView gl_maintenance;
    private List<File> pics = new ArrayList<>();
    private List<File> picsBefore = new ArrayList<>();
    private String sameCondition = "";
    private ImageView iv_del;
    private MaterialAdapter materialAdapter;
    private ImageView iv_back;
    private TextView tv_from;
    private TextView tv_tel;
    private TextView tv_content;
    private EditText et_feedback;
    private int id;
    private DbUtils db;
    protected OnRefreshListener onRefreshListener;
    private int userId;
    private LinearLayout ll_pleased;
    private TextView tv_empty_pic;
    private ImageView tv_photograph_before;
    private GridView gl_maintenance_before;
    private PicAdapter picBeforeAdapter;
    private String repairNo;
    private String repairMan;
    private String repairTel;
    private String repairContent;
    private String pleased;
    private String handwrite;
    private TextView tv_number;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MAX_SIZE:
                    iv_loading_before.setVisibility(View.INVISIBLE);
                    iv_loading_before.clearAnimation();
                    iv_loading.setVisibility(View.INVISIBLE);
                    iv_loading.clearAnimation();
                    Utils.showTost(MaintenanceActivity.this, "最多上传3张图片哦！");
                    break;
                case NOT_FIND:
                    iv_loading.setVisibility(View.INVISIBLE);
                    iv_loading.clearAnimation();
                    iv_loading_before.setVisibility(View.INVISIBLE);
                    iv_loading_before.clearAnimation();
                    Utils.showTost(MaintenanceActivity.this, "找不到图片");
                    break;
                case NOTIFI_BEFORE:
                    iv_loading_before.setVisibility(View.INVISIBLE);
                    iv_loading_before.clearAnimation();
                    picBeforeAdapter.notifyDataSetChanged();
                    break;
                case NOTIFI:
                    iv_loading.setVisibility(View.INVISIBLE);
                    iv_loading.clearAnimation();
                    picAdapter.notifyDataSetChanged();
                    break;
                case UPLOAD_OK:
//                    long endTime = System.currentTimeMillis();
//                    Log.e("upload", "endTime: " + endTime);
                    count++;
                    if (count == imageFilePath.size()) {
                        if (customProgressDialog != null) {
                            customProgressDialog.dismiss();
                        }
                        hasUpload = true;
                    }
                    break;
            }
        }
    };
    private int count;
    private ImageView iv_loading;
    private Animation operatingAnim;
    private ImageView iv_loading_before;
    private LinearLayout main;
    private ImageView iv_upload;
    private ImageView iv_upload_before;
    private TextView tv_empty_pic_before;
    private RadioGroup rg_pleased;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private TextView et_handwrite;
    private Bitmap mSignBitmap;
    private File handWriteFile;


    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener {
        void refresh();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        initHeader("工单维修");
        initView();
        initData();
        CommonUtils.addLayoutListener(main, et_feedback);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initData() {
        if (db == null) {
            db = LocalApp.getDb();
            try {
                db.createTableIfNotExist(CommitDb.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        userId = SPUtils.getInt(MaintenanceActivity.this, SPConstants.USER_ID);
        Intent intent = getIntent();
        Order order = (Order) intent.getSerializableExtra(Constants.ORDER_DATA);
//        if (order == null) {
//            CommitDb commitDb = (CommitDb) intent.getSerializableExtra(Constants.UNCOMMIT_DATA);
//            repairNo = commitDb.getRepairNo();
//            repairMan = commitDb.getRepairMan();
//            repairTel = commitDb.getRepairTel();
//            repairContent = commitDb.getRepairContent();
//            id = commitDb.getRepairId();
//        } else {
        repairNo = order.getRepairNo();
        repairMan = order.getRepairMan();
        repairTel = order.getRepairTel();
        repairContent = order.getRepairContent();
        id = order.getId();
//        }
        tv_number.setText(repairNo);
        tv_from.setText(repairMan);
        tv_tel.setText(repairTel);
        tv_content.setText(repairContent);
        choosePleased();
    }

    private void initView() {
        main = (LinearLayout) findViewById(R.id.main);
        lv_material = (ListView) findViewById(R.id.lv_material);
        materialAdapter = new MaterialAdapter(data);
        lv_material.setAdapter(materialAdapter);
        tv_add = (ImageView) findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_confirm.setVisibility(View.VISIBLE);
        btn_confirm.setOnClickListener(this);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_from = (TextView) findViewById(R.id.tv_from);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_photo = (ImageView) findViewById(R.id.tv_photograph);
        tv_photo.setOnClickListener(this);
        tv_photograph_before = (ImageView) findViewById(R.id.tv_photograph_before);
        tv_photograph_before.setOnClickListener(this);
        iv_del = (ImageView) findViewById(R.id.tv_delete);
        iv_del.setOnClickListener(this);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        gl_maintenance = (GridView) findViewById(R.id.gl_maintenance);
        gl_maintenance_before = (GridView) findViewById(R.id.gl_maintenance_before);
        ll_pleased = (LinearLayout) findViewById(R.id.ll_pleased);
        ll_pleased.setOnClickListener(this);
        rg_pleased = (RadioGroup) findViewById(R.id.rg_pleased);
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        iv_loading_before = (ImageView) findViewById(R.id.iv_loading_before);
//        iv_upload = (ImageView) findViewById(R.id.iv_upload);
//        iv_upload.setOnClickListener(this);
        tv_empty_pic_before = (TextView) findViewById(R.id.tv_empty_pic_before);
        tv_empty_pic = (TextView) findViewById(R.id.tv_empty_pic);
//        iv_upload_before = (ImageView) findViewById(R.id.iv_upload_before);
//        iv_upload_before.setOnClickListener(this);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        rb1 = (RadioButton) findViewById(R.id.rb_1);
        rb2 = (RadioButton) findViewById(R.id.rb_2);
        rb3 = (RadioButton) findViewById(R.id.rb_3);
        picAdapter = new PicAdapter(MaintenanceActivity.this, pics);
        picBeforeAdapter = new PicAdapter(MaintenanceActivity.this, picsBefore);
        initGridView(gl_maintenance, pics, repairImgPath, imageFilePath, picAdapter, tv_empty_pic);
        initGridView(gl_maintenance_before, picsBefore, repairBeforeImgPath, imageFilePathBefore, picBeforeAdapter, tv_empty_pic_before);
        et_handwrite = (TextView) findViewById(R.id.et_handwrite);
    }

    private PicAdapter picAdapter;

    private void initGridView(GridView gridView, final List<File> bitmapList, final List<String> imgPath, final List<String> repairImgPath, final PicAdapter adapter, final TextView emptyView) {

        gridView.setAdapter(adapter);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        gl_maintenance.setLayoutParams(params);
//        gl_maintenance.setEmptyView(tv_empty_pic);
        gridView.setNumColumns(3);
//        lv_menu.setHorizontalSpacing(10);
//        gl_maintenance.setStretchMode(GridView.NO_STRETCH);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MaintenanceActivity.this)
                        .setItems(forDelete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bitmapList.remove(position);
                                if (position <= imgPath.size() - 1) {
                                    imgPath.remove(position);
                                }
                                repairImgPath.remove(position);
                                adapter.notifyDataSetChanged();
                                if (bitmapList.size() == 0) {
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                        .create();
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
//                    mDialog.setContentView(R.layout.dialog_select_imge);
                    dialog.setCanceledOnTouchOutside(false);
                }
                return false;
            }
        });
    }

    String[] arrayString = {"拍照", "相册"};
    String[] arrayStringBefore = {"相册"};
    String[] forDelete = {"删除"};
//    String title = "上传照片";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                toAdd();
                break;
            case R.id.tv_photograph:
                toPhoto();
                break;
            case R.id.tv_photograph_before:
                toPhotoBefore();
                break;
            case R.id.btn_confirm:
                commit();
                break;
            case R.id.tv_delete:
                deleteData();
                break;
            case R.id.iv_back:
                backWarm();
                break;
//            case R.id.iv_upload:
//                upload(imageFilePath, repairImgPath);
//                break;
//            case R.id.iv_upload_before:
//                upload(imageFilePathBefore, repairBeforeImgPath);
//                break;
            default:
                break;
        }
    }

    private boolean hasUpload;

    private void upload(final List<String> paths, final List<String> repairImgPath) {

//        long startTime = System.currentTimeMillis();
//        Log.e("upload", "startTime: " + startTime);
        boolean checkNetWork = Utils.checkNetWork(MaintenanceActivity.this);
        if (checkNetWork) {
            if (paths.size() != 0) {
                count = 0;
                hasUpload = false;
                customProgressDialog = new CustomProgressDialog(MaintenanceActivity.this, "图片上传中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
                customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                customProgressDialog.setCancelable(false);
                customProgressDialog.setCanceledOnTouchOutside(false);
                customProgressDialog.show();
                Point size = new Point();
                customProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                int width = size.x;
                int height = size.y;
                WindowManager.LayoutParams params = customProgressDialog.getWindow().getAttributes();
                params.gravity = Gravity.CENTER;
                params.alpha = 0.8f;
//                        params.height = height/6;
                params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
                customProgressDialog.getWindow().setAttributes(params);
                ExecutorService poolThread = SingleThreadPool.getPoolThread();
                poolThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (paths != null && paths.size() != 0) {
                            repairImgPath.clear();
                            for (String path : paths) {
                                if (path != null && !("").equals(path)) {
                                    File file = new File(path.trim());
                                    try {
                                        UpLoadPicResponse upLoadPicResponse = upLoadPic(file);
                                        repairImgPath.add(upLoadPicResponse.getPath());
                                        handler.sendEmptyMessage(UPLOAD_OK);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!hasUpload) {
                            if (customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.dismiss();
                                CommonUtils.showToast(MaintenanceActivity.this, "上传时间过久，请重新上传");
                            }
                        }
                    }
                }, 60000);
            } else {
                CommonUtils.showToast(MaintenanceActivity.this, "还未添加照片哦！");
            }
        } else {
            CommonUtils.showToast(MaintenanceActivity.this, getString(R.string.no_net));
        }
    }


    private void choosePleased() {
//        LayoutInflater factory = LayoutInflater.from(MaintenanceActivity.this);//提示框
//        final View dialogView = factory.inflate(R.layout.choose_pleased, null);//这里必须是final的
//        final RadioGroup rg_pleased = (RadioGroup) dialogView.findViewById(R.id.rg_pleased);//获得输入框对象
//        final RadioButton rb1 = (RadioButton) dialogView.findViewById(R.id.rb_1);
//        final RadioButton rb2 = (RadioButton) dialogView.findViewById(R.id.rb_2);
//        final RadioButton rb3 = (RadioButton) dialogView.findViewById(R.id.rb_3);
//        rg_pleased.check(0);
//        AlertDialog alertDialog = new AlertDialog.Builder(MaintenanceActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
//                .setTitle("是否满意")//提示框标题
//                .setView(dialogView)
//                .setPositiveButton("确定",//提示框的两个按钮
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
        rg_pleased.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb1.getId() == checkedId) {
//                                    tv_pleased.setText(rb1.getText().toString().trim());
                    pleased = rb1.getText().toString().trim();
                } else if (rb2.getId() == checkedId) {
//                                    tv_pleased.setText(rb2.getText().toString().trim());
                    pleased = rb2.getText().toString().trim();
                } else if (rb3.getId() == checkedId) {
//                                    tv_pleased.setText(rb3.getText().toString().trim());
                    pleased = rb3.getText().toString().trim();
                }
            }
        });
    }


    private void deleteData() {
        List<Material> del = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Material material = data.get(i);
            if (material.isChecked()) {
                del.add(material);
            }
        }
        for (Material material : del) {
            data.remove(material);
        }
        materialAdapter.notifyDataSetChanged();
    }

    private List<String> materialId = new ArrayList<>();
    private List<String> materialName = new ArrayList<>();
    private List<String> materialType = new ArrayList<>();
    private List<String> materialCount = new ArrayList<>();
    private List<String> materialPrice = new ArrayList<>();
    private boolean no_net;

    private void commit() {
        final String feedback = et_feedback.getText().toString().trim();
        materialId.clear();
        materialName.clear();
        materialType.clear();
        materialCount.clear();
        materialPrice.clear();
        if (TextUtils.isEmpty(feedback) || pleased == null) {
            CommonUtils.showToast(MaintenanceActivity.this, "反馈内容和科室确认不能为空哦！");
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("提示");
            if (data.size() == 0) {
                builder.setMessage("未填写维修材料，是否确定提交");
            } else {
                builder.setMessage("是否确定提交");
            }
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String currentTime = CommonUtils.getCurrentTime();
                    for (int i = 0; i < data.size(); i++) {
                        Material material = data.get(i);
                        materialId.add(material.getId() + "");
                        materialName.add(material.getName());
                        materialType.add(material.getType());
                        materialCount.add(material.getNumber() + "");
                        materialPrice.add(material.getPrice() + "");
                    }
                    boolean isNetWork = Utils.checkNetWork(MaintenanceActivity.this);
                    if (!isNetWork) {
                        String materialIds = materialId.toString().substring(1, materialId.toString().length() - 1);
                        String materialNames = materialName.toString().substring(1, materialName.toString().length() - 1);
                        String specification = materialType.toString().substring(1, materialType.toString().length() - 1);
                        String materialNum = materialCount.toString().substring(1, materialCount.toString().length() - 1);
                        String materialPrices = materialPrice.toString().substring(1, materialPrice.toString().length() - 1);
//                        if (imageFilePath.size() != 0) {
                        String repairImg = imageFilePath.toString().substring(1, imageFilePath.toString().length() - 1);
//                        String absolutePath = handWriteFile.getAbsolutePath();
//                        }
//                        if (imageFilePathBefore.size() != 0) {
                        String repairBeforeImg = imageFilePathBefore.toString().substring(1, imageFilePathBefore.toString().length() - 1);
                        String handWriteFileAbsolutePath = handWriteFile.getAbsolutePath();
//                        }
                        final CommitDb commitDb = new CommitDb(id, userId, feedback, materialIds, materialNames, specification, materialNum, repairImg, repairBeforeImg, materialPrices, sameCondition, pleased, currentTime, repairNo, 1, handWriteFileAbsolutePath);
                        ExecutorService thread = SingleThreadPool.getThread();
                        thread.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean have = false;
                                    List<CommitDb> dbAll = db.findAll(CommitDb.class);
                                    if (dbAll != null && dbAll.size() != 0) {
                                        for (CommitDb commit : dbAll
                                                ) {
                                            if (commit.getRepairId() == commitDb.getRepairId()) {
                                                have = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (have) {
                                        db.update(commitDb, CommitDb.USER_ID, CommitDb.REPAIR_REPLY, CommitDb.REPAIR_MATERIAL_ID, CommitDb.REPAIR_MATERIAL_NAME, CommitDb.REPAIR_MATERIAL_TYPE, CommitDb.REPAIR_MATERIAL_NUM, CommitDb.REPAIR_IMG_PATH, CommitDb.SPOT_IMG, CommitDb.REPAIR_MATERIAL_PRICE, "satisfy", CommitDb.FINISH_TIME, CommitDb.HAS_COMMIT, CommitDb.HAND_WRITE);
                                    } else {
                                        db.save(commitDb);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.showTost(MaintenanceActivity.this, getString(R.string.no_net) + "\n已将数据存入本地，之后可进行提交");
                                            finish();
                                            sendBroadcast();
                                        }
                                    });
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
//                        if (repairBeforeImgPath.size() != imageFilePathBefore.size() || repairImgPath.size() != imageFilePath.size()) {
//                            CommonUtils.showToast(MaintenanceActivity.this, "选择的图片还未上传哦！");
//                            return;
//                        }
                        no_net = true;
                        customProgressDialog = new CustomProgressDialog(MaintenanceActivity.this, "数据提交中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
                        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        customProgressDialog.setCancelable(false);
                        customProgressDialog.setCanceledOnTouchOutside(false);
                        customProgressDialog.show();
                        Point size = new Point();
                        customProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                        int width = size.x;
                        int height = size.y;
                        WindowManager.LayoutParams params = customProgressDialog.getWindow().getAttributes();
                        params.gravity = Gravity.CENTER;
                        params.alpha = 0.8f;
//                        params.height = height/6;
                        params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
                        customProgressDialog.getWindow().setAttributes(params);
                        ExecutorService poolThread = SingleThreadPool.getPoolThread();
                        poolThread.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (imageFilePath != null && imageFilePath.size() != 0) {
                                    for (String path : imageFilePath) {
                                        if (path != null && !("").equals(path)) {
                                            File file = new File(path.trim());
                                            try {
                                                UpLoadPicResponse upLoadPicResponse = upLoadPic(file);
                                                repairImgPath.add(upLoadPicResponse.getPath());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                if (imageFilePathBefore != null && imageFilePathBefore.size() != 0) {
                                    for (String path : imageFilePathBefore) {
                                        if (path != null && !("").equals(path)) {
                                            File file = new File(path.trim());
                                            try {
                                                UpLoadPicResponse upLoadPicResponse = upLoadPic(file);
                                                repairBeforeImgPath.add(upLoadPicResponse.getPath());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                if (handWriteFile != null) {
                                    try {
                                        UpLoadPicResponse upLoadPicResponse = upLoadSign(handWriteFile);
                                        handwrite = upLoadPicResponse.getPath();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (handwrite == null) {
                                    handwrite = "";
                                }
                                CommitRequest commitRequest = new CommitRequest(userId + "", id + "", feedback, materialId, materialName, materialType, materialCount, repairImgPath, repairBeforeImgPath, materialPrice, sameCondition, pleased, "", "", currentTime, handwrite);
                                sendRequest(commitRequest, CommitResponse.class);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (no_net) {
                                            if (customProgressDialog != null) {
                                                customProgressDialog.dismiss();
                                            }
                                            CommonUtils.showToast(MaintenanceActivity.this, "网络异常");
                                        }
                                    }
                                }, 60000);
                            }
                        });
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    /*检测相机是否存在*/
    private boolean checkCameraHardWare(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }

    private void toPhotoBefore() {
        AlertDialog mDialog = new AlertDialog.Builder(MaintenanceActivity.this)
                .setItems(arrayStringBefore, onBeforeDialogClick)
                .create();
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
//                    mDialog.setContentView(R.layout.dialog_select_imge);
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

    private void toPhoto() {
        AlertDialog mDialog = new AlertDialog.Builder(MaintenanceActivity.this)
                .setItems(arrayString, onDialogClick)
                .create();
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
//                    mDialog.setContentView(R.layout.dialog_select_imge);
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

    private void toAdd() {
        Intent intent = new Intent(MaintenanceActivity.this, AddMaterialActivity.class);
        intent.putExtra("hasAdd", (Serializable) data);
        startActivityForResult(intent, IS_ADD);
    }

    private static final int TAKE_PHOTO_REQUEST_CODE = 2;

    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    // 对话框
    DialogInterface.OnClickListener onDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    startCamearPicCut(dialog);// 开启照相
//                    if (checkCameraHardWare(getApplicationContext())){
//                        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(getApplicationContext(), "没有相机存在", Toast.LENGTH_SHORT).show();
//                    }
                    break;
                case 1:
                    startImageCaptrue(dialog);// 开启图库
                    break;
                default:
                    break;
            }
        }

        private void startCamearPicCut(DialogInterface dialog) {
            dialog.dismiss();
            // 调用系统的拍照功能
            String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
            if (ContextCompat.checkSelfPermission(MaintenanceActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MaintenanceActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        TAKE_PHOTO_REQUEST_CODE);
            }
            if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
                try {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                } catch (Exception e) {

                }

            } else {
                Toast.makeText(MaintenanceActivity.this, "sdcard不可用", Toast.LENGTH_SHORT).show();
            }
        }

        private void startImageCaptrue(DialogInterface dialog) {
            dialog.dismiss();
            verifyStoragePermissions(MaintenanceActivity.this);
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            } catch (Exception e) {

            }
        }


    };
    DialogInterface.OnClickListener onBeforeDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
//                case 0:
//                    startCamearPicCut(dialog);// 开启照相
//                    break;
                case 0:
                    startImageCaptrue(dialog);// 开启图库
                    break;
                default:
                    break;
            }
        }

        private void startCamearPicCut(DialogInterface dialog) {
            dialog.dismiss();
            // 调用系统的拍照功能
            String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
            if (ContextCompat.checkSelfPermission(MaintenanceActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MaintenanceActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        TAKE_PHOTO_REQUEST_CODE);
            }
            if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
                try {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, PHOTO_BEFORE_REQUEST_TAKEPHOTO);
                } catch (Exception e) {
                }
            } else {
                Toast.makeText(MaintenanceActivity.this, "sdcard不可用", Toast.LENGTH_SHORT).show();
            }
        }

        private void startImageCaptrue(DialogInterface dialog) {
            dialog.dismiss();
            verifyStoragePermissions(MaintenanceActivity.this);
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, PHOTO_BEFORE_REQUEST_GALLERY);
            } catch (Exception e) {
            }
        }
    };

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".png";
    }

    private List<String> imageFilePath = new ArrayList<>();
    private List<String> imageFilePathBefore = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (resultCode == RESULT_OK) {
                    if (operatingAnim != null) {
                        tv_empty_pic.setVisibility(View.INVISIBLE);
                        iv_loading.setVisibility(View.VISIBLE);
                        iv_loading.startAnimation(operatingAnim);
                    }
                    ExecutorService poolThread = SingleThreadPool.getPoolThread();
                    poolThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (data.getData() != null || data.getExtras() != null) { //防止没有返回结果
                                if (pics.size() < 3) {
                                    Uri uri = data.getData();
//                        Uri uri =data.getData();
//                                Log.i("test", uri.toString());
                                    Bitmap bitmap = null;
                                    if (uri != null) {
                                        bitmap = BitmapFactory.decodeFile(uri.getPath()); //拿到图片
                                    }

                                    if (bitmap == null) {
                                        Bundle bundle = data.getExtras();
                                        if (bundle != null) {
                                            bitmap = (Bitmap) bundle.get("data");
                                        }
                                    }
                                    if (uri == null) {
                                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                                    }
                                    String[] proj = {MediaStore.Images.Media.DATA};
                                    //好像是Android多媒体数据库的封装接口，具体的看Android文档
                                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                                    //获得用户选择的图片的索引值
                                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                                    cursor.moveToFirst();
                                    //最后根据索引值获取图片路径
                                    String filePath = cursor.getString(column_index);
//                                        String filePath = cutPictureQuality(photo, Constants.SAVE_DR, PHOTO_REQUEST_TAKEPHOTO);
//                                    Bitmap smallBitmap = getSmallBitmap(filePath);
//                                    String smallFilePath = saveBitmapFile(smallBitmap, Constants.SAVE_DR);
                                    String s = BitmapUtils.compressImageUpload(filePath);
                                    final File file = new File(s);
//                                    String path = saveBitmapFile(newbitmap, Constants.SAVE_DR);
                                    imageFilePath.add(s);
//                                    File newFile = new File(path);
                                    pics.add(file);
                                    handler.sendEmptyMessageDelayed(NOTIFI, 300);
//                                    boolean checkNetWork = Utils.checkNetWork(MaintenanceActivity.this);
//                                    if (checkNetWork) {
//                                        upLoadPic(file, repairImgPath);
//                                    } else {
//
//                                    }
                                } else {
                                    handler.sendEmptyMessage(MAX_SIZE);
                                }
                            } else {
                                handler.sendEmptyMessage(NOT_FIND);
                            }
                        }
                    });
                }
                break;
            case PHOTO_REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    if (operatingAnim != null) {
                        tv_empty_pic.setVisibility(View.INVISIBLE);
                        iv_loading.setVisibility(View.VISIBLE);
                        iv_loading.startAnimation(operatingAnim);
                    }
                    ExecutorService poolThread = SingleThreadPool.getPoolThread();
                    poolThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (data != null) {
                                if (pics.size() < 3) {
                                    Uri uri = data.getData();
                                    String[] proj = {MediaStore.Images.Media.DATA};
                                    //好像是Android多媒体数据库的封装接口，具体的看Android文档
                                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                                    //获得用户选择的图片的索引值
                                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                                    cursor.moveToFirst();
                                    //最后根据索引值获取图片路径
                                    String filePath = cursor.getString(column_index);
//                                ContentResolver resolver = getContentResolver();
//                                Bitmap bitmap = null;
//                                try {
//                                    bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                String mid = CommonUtils.getCurrentTime();
//                                Log.e("time", "mid: "+mid);
//                                String filePath = cutPictureQuality(bitmap, Constants.SAVE_DR, PHOTO_REQUEST_TAKEPHOTO);
//                                String end = CommonUtils.getCurrentTime();
//                                Log.e("time", "end: "+end);
//                                Bitmap smallBitmap = getSmallBitmap(filePath);
//                                String smallFilePath = saveBitmapFile(smallBitmap, Constants.SAVE_DR);
                                    String smallBitmap = BitmapUtils.compressImageUpload(filePath);
//                                * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
//                                            */
//                                int degree = readPictureDegree(filePath);
//
//                                BitmapFactory.Options opts = new BitmapFactory.Options();//获取缩略图显示到屏幕上
//                                opts.inSampleSize = 2;
//                                Bitmap cbitmap = BitmapFactory.decodeFile(filePath, opts);
//
//                                /**
//                                 * 把图片旋转为正的方向
//                                 */
//                                Bitmap newbitmap = rotaingImageView(degree, cbitmap);
                                    imageFilePath.add(smallBitmap);
                                    final File file = new File(smallBitmap);
                                    pics.add(file);
//                                    upLoadPic(file, repairImgPath);
                                    handler.sendEmptyMessageDelayed(NOTIFI, 300);
                                } else {
                                    handler.sendEmptyMessage(MAX_SIZE);
                                }
                            }
                        }
                    });
                }
                break;
            case PHOTO_BEFORE_REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    if (operatingAnim != null) {
                        tv_empty_pic_before.setVisibility(View.INVISIBLE);
                        iv_loading_before.setVisibility(View.VISIBLE);
                        iv_loading_before.startAnimation(operatingAnim);
                    }
                    ExecutorService poolThread = SingleThreadPool.getPoolThread();
                    poolThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (data != null) {
                                if (picsBefore.size() < 3) {
                                    Uri uri = data.getData();
                                    String[] proj = {MediaStore.Images.Media.DATA};
                                    //好像是Android多媒体数据库的封装接口，具体的看Android文档
                                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                                    //获得用户选择的图片的索引值
                                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                                    cursor.moveToFirst();
                                    //最后根据索引值获取图片路径
                                    String filePath = cursor.getString(column_index);
//                                String smallFilePath = cutPictureQuality(bitmap, Constants.SAVE_DR);
//                                Bitmap smallBitmap = getSmallBitmap(filePath);
//                                String smallFilePath = saveBitmapFile(smallBitmap, Constants.SAVE_DR);
                                    String smallFilePath = BitmapUtils.compressImageUpload(filePath);
                                    imageFilePathBefore.add(smallFilePath);
                                    final File file = new File(smallFilePath);
                                    picsBefore.add(file);
                                    handler.sendEmptyMessageDelayed(NOTIFI_BEFORE, 300);
//                                    handler.sendEmptyMessageDelayed(NOTIFI_BEFORE,500);
//                                    upLoadPic(file, repairBeforeImgPath);
                                } else {
                                    handler.sendEmptyMessage(MAX_SIZE);
                                }
                            }
                        }
                    });
                }
                break;
            case IS_ADD:
                if (data != null) {
                    List<Material> add = (List<Material>) data.getSerializableExtra(Constants.DATA);
//                    List<Material> add = (List<Material>)bundle.getSerializable("data");
                    for (int i = 0; i < add.size(); i++) {
                        this.data.add(add.get(i));
                    }
                    materialAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
//        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public String saveBitmapFile(Bitmap bitmap, String savePackage) {

        String fileName = UUID.randomUUID().toString().replace("-", "") + ".png";
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //SD卡存在
            filePath = Environment.getExternalStorageDirectory() + File.separator + savePackage;
        } else {
            //SD卡不存在
            filePath = getFilesDir() + File.separator + savePackage;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath, fileName)));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath + File.separator + fileName;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    private List<String> repairImgPath = new ArrayList<>();
    private List<String> repairBeforeImgPath = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private UpLoadPicResponse upLoadPic(File file) throws IOException {
        String ip = SPUtils.getString(MaintenanceActivity.this, SPConstants.IP);
        String RequestURL = HttpConstants.HOST + ip + HttpConstants.UPLOADPIC;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("id", id + "");
//        map.put("imgFileName", file.getName());
//        map.put("imgContentType", MEDIA_TYPE_PNG + "");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        for (String key:map.keySet()){
        builder.addFormDataPart("repair.id", id + "");
        builder.addFormDataPart("imgFileName", file.getName());
        builder.addFormDataPart("imgContentType", MEDIA_TYPE_PNG + "");
//        }
        builder.addFormDataPart("img", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        MultipartBody body = builder.build();

        Request request = new Request.Builder().url(RequestURL).post(body).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body().string();
        UpLoadPicResponse upLoadPicResponse = null;
        if (responseStr != null) {
            upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
        }
        return upLoadPicResponse;
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("upLoad", "run: " + e.getLocalizedMessage());
////                CommonUtils.showToast(MaintenanceActivity.this,e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseStr = response.body().string();
//                if (responseStr != null) {
//                    UpLoadPicResponse upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
//                    ImgPath.add(upLoadPicResponse.getPath());
//                }
//            }
//        });
    }

    private UpLoadPicResponse upLoadSign(File file) throws IOException {
        String ip = SPUtils.getString(MaintenanceActivity.this, SPConstants.IP);
        String RequestURL = HttpConstants.HOST + ip + HttpConstants.UPLOADSIGN;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("id", id + "");
//        map.put("imgFileName", file.getName());
//        map.put("imgContentType", MEDIA_TYPE_PNG + "");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        for (String key:map.keySet()){
        builder.addFormDataPart("repair.id", id + "");
        builder.addFormDataPart("imgFileName", file.getName());
        builder.addFormDataPart("imgContentType", MEDIA_TYPE_PNG + "");
//        }
        builder.addFormDataPart("img", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        MultipartBody body = builder.build();

        Request request = new Request.Builder().url(RequestURL).post(body).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body().string();
        UpLoadPicResponse upLoadPicResponse = null;
        if (responseStr != null) {
            upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
        }
        return upLoadPicResponse;
    }

    public String cutPictureQuality(Bitmap bitmap, String savePackage) {

        String fileName = UUID.randomUUID().toString().replace("-", "") + ".png";
        String filePath = Environment.getExternalStorageDirectory() + File.separator + savePackage;

        // 判断文件夹存在
        File file = new File(filePath);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        try {
            // 第一次压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            FileOutputStream fos = new FileOutputStream(new File(filePath, fileName));

            int options = 100;
            // 如果大于150kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > 150 && options != 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
                options -= 30;
            }
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();

        } catch (Exception e) {
        }
        return filePath + File.separator + fileName;
    }


    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
        if (request instanceof CommitRequest) {
            CommitResponse commitResponse = (CommitResponse) response;
            if (commitResponse != null) {
                if ("0".equals(commitResponse.getResult())) {
                    CommonUtils.showToast(MaintenanceActivity.this, "提交成功");
                    finish();
                    sendBroadcast();
//                onRefreshListener.refresh();
                } else if ("1".equals(commitResponse.getResult())) {
                    CommonUtils.showToast(MaintenanceActivity.this, "提交失败");
                }
            }
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
            }
            no_net = false;
        }
    }

    private void sendBroadcast() {
        Intent intent = new Intent(Constants.COMMIT_OK);
        sendBroadcast(intent);
    }

    public void edit(View view) {
        WritePadDialog writeTabletDialog = new WritePadDialog(
                MaintenanceActivity.this, new HandwritingActivity.DialogListener() {
            @Override
            public void refreshActivity(Object object) {

                mSignBitmap = (Bitmap) object;
//                signPath = createFile();
                handWriteFile = createFile();
                            /*BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 15;
                            options.inTempStorage = new byte[5 * 1024];
                            Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/
                InsertToEditText(mSignBitmap);
            }
        });
        writeTabletDialog.setCanceledOnTouchOutside(false);
        writeTabletDialog.show();
    }

    /**
     * 创建手写签名文件
     *
     * @return
     */
    private File createFile() {
        ByteArrayOutputStream baos = null;
        File file = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + HANDWRITE_CACHE;
            _path = sign_dir + System.currentTimeMillis() + ".jpg";
            File dir = new File(sign_dir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                file = new File(_path);
                new FileOutputStream(file).write(photoBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private void InsertToEditText(Bitmap mBitmap) {

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int windoHeight = defaultDisplay.getHeight();
        int windowWidth = defaultDisplay.getWidth();
        double h = Math.pow(((double) windoHeight), 2);
        double w = Math.pow(((double) windowWidth), 2);
        double sqrt = Math.sqrt(h + w);
        double newHeight = 50f * sqrt / 160;
        int imgWidth = mBitmap.getWidth();

        int imgHeight = mBitmap.getHeight();

        //缩放比例
        float scaleW = (float) (newHeight / imgWidth);

        float scaleH = (float) (newHeight / imgHeight);


        Matrix mx = new Matrix();

        //对原图片进行缩放

        mx.postScale(scaleW, scaleH);

        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, imgWidth, imgHeight, mx, true);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        //将手写的字插入到edittext中

        SpannableString ss = new SpannableString("1");

        ImageSpan span = new ImageSpan(mBitmap, ImageSpan.ALIGN_BOTTOM);

        ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        et_handwrite.setText(ss);
    }
}
