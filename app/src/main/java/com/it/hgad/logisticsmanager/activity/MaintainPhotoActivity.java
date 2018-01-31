package com.it.hgad.logisticsmanager.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.adapter.PicAdapter;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.util.BitmapUtils;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.logisticsmanager.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MaintainPhotoActivity extends CommonActivity {
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_BEFORE_REQUEST_TAKEPHOTO = 3;// 拍照
    private static final int PHOTO_BEFORE_REQUEST_GALLERY = 4;// 从相册中选择
    private static final int MAX_SIZE = 30;
    private static final int NOT_FIND = 0;
    private static final int NOTIFI = 10;
    private static final int NOTIFI_BEFORE = 20;
    private static final int UPLOAD_OK = 345;
    private List<File> pics = new ArrayList<>();
    private List<File> picsBefore = new ArrayList<>();
    private List<String> repairImgPath = new ArrayList<>();
    private List<String> repairBeforeImgPath = new ArrayList<>();
    private List<String> imageFilePath = new ArrayList<>();
    private List<String> imageFilePathBefore = new ArrayList<>();
    private ImageView iv_loading;
    private ImageView iv_loading_before;
    private PicAdapter picAdapter;
    private PicAdapter picBeforeAdapter;
    //    private TextView tv_empty_pic_before;
//    private TextView tv_empty_pic;
    private Animation operatingAnim;
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
                    Utils.showTost(MaintainPhotoActivity.this, "最多上传3张图片哦！");
                    break;
                case NOT_FIND:
                    iv_loading.setVisibility(View.INVISIBLE);
                    iv_loading.clearAnimation();
                    iv_loading_before.setVisibility(View.INVISIBLE);
                    iv_loading_before.clearAnimation();
                    Utils.showTost(MaintainPhotoActivity.this, "找不到图片");
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
            }
        }
    };
    private Order order;
    private DbUtils db;
    private CommitDb commitDb;
    private File addPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_photo);
        initHeader("维修照片");
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
            String spotImg = commitDb.getSpotImg();
            String repairImgPath = commitDb.getRepairImgPath();
            if (spotImg != null && !"".equals(spotImg)) {
                String[] spotImgs = spotImg.split(",");
                List<String> beforeImg = Arrays.asList(spotImgs);
                for (String beforePath :
                        beforeImg) {
                    beforePath = beforePath.trim();
                    imageFilePathBefore.add(beforePath);
                    File file = new File(beforePath);
                    picsBefore.add(file);
                }
            }
            if (repairImgPath != null && !"".equals(repairImgPath)) {
                String[] repairImgPaths = repairImgPath.split(",");
                List<String> afterImg = Arrays.asList(repairImgPaths);
                for (String afterPath :
                        afterImg) {
                    afterPath = afterPath.trim();
                    imageFilePath.add(afterPath);
                    File file = new File(afterPath);
                    pics.add(file);
                }
            }
        }
        pics.add(addPic);
        picsBefore.add(addPic);
        picAdapter.notifyDataSetChanged();
        picBeforeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        TextView tv_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_confirm.setOnClickListener(this);
        tv_confirm.setVisibility(View.VISIBLE);
//        ImageView tv_photo = (ImageView) findViewById(R.id.tv_photograph);
//        tv_photo.setOnClickListener(this);
//        ImageView tv_photograph_before = (ImageView) findViewById(R.id.tv_photograph_before);
//        tv_photograph_before.setOnClickListener(this);
        GridView gl_maintenance = (GridView) findViewById(R.id.gl_maintenance);
        GridView gl_maintenance_before = (GridView) findViewById(R.id.gl_maintenance_before);
//        tv_empty_pic_before = (TextView) findViewById(R.id.tv_empty_pic_before);
//        tv_empty_pic = (TextView) findViewById(R.id.tv_empty_pic);
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        iv_loading_before = (ImageView) findViewById(R.id.iv_loading_before);
        picAdapter = new PicAdapter(MaintainPhotoActivity.this, pics);
        picBeforeAdapter = new PicAdapter(MaintainPhotoActivity.this, picsBefore);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        initGridView(gl_maintenance, pics, repairImgPath, imageFilePath, picAdapter);
        initGridView(gl_maintenance_before, picsBefore, repairBeforeImgPath, imageFilePathBefore, picBeforeAdapter);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.photopic);
        addPic = saveBitmapFile(bitmap);
    }

    @Override
    public void back(View view) {
        backWarm();
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

    String[] arrayString = {"拍照", "相册"};
    String[] arrayStringBefore = {"相册"};
    String[] forDelete = {"删除"};

    public File saveBitmapFile(Bitmap bitmap) {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/needUsedPhoto/";
        String filePath = baseDir + "takephoto.jpg";
        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(filePath);//将要保存图片的路径
//        if (file.exists()){   //如果存在，就不再保存
//            return file;
//        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void initGridView(final GridView gridView, final List<File> bitmapList, final List<String> imgPath, final List<String> repairImgPath, final PicAdapter adapter) {

        gridView.setAdapter(adapter);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gridView.getHeight());
//        gridView.setLayoutParams(params);
//        gl_maintenance.setEmptyView(tv_empty_pic);
        gridView.setNumColumns(3);
//        lv_menu.setHorizontalSpacing(10);
//        gl_maintenance.setStretchMode(GridView.NO_STRETCH);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == bitmapList.size() - 1) {
                    if (gridView.getId() == R.id.gl_maintenance) {
                        toPhoto();
                    } else {
                        toPhotoBefore();
                    }

                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position != bitmapList.size() - 1) {
                    AlertDialog dialog = new AlertDialog.Builder(MaintainPhotoActivity.this)
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
//                                    emptyView.setVisibility(View.VISIBLE);
                                    }

                                }
                            })
                            .create();
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.show();
//                    mDialog.setContentView(R.layout.dialog_select_imge);
                        dialog.setCanceledOnTouchOutside(false);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_photograph:
//                toPhoto();
//                break;
//            case R.id.tv_photograph_before:
//                toPhotoBefore();
//                break;
            case R.id.btn_confirm:
                save();
                break;
        }
    }

    private void save() {
        if (commitDb == null) {
            commitDb = new CommitDb();
            commitDb.setRepairId(order.getId());
        }
        String repairImg = imageFilePath.toString().substring(1, imageFilePath.toString().length() - 1);
        String repairBeforeImg = imageFilePathBefore.toString().substring(1, imageFilePathBefore.toString().length() - 1);
        commitDb.setSpotImg(repairBeforeImg);
        commitDb.setRepairImgPath(repairImg);
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

    private void toPhoto() {
        AlertDialog mDialog = new AlertDialog.Builder(MaintainPhotoActivity.this)
                .setItems(arrayString, onDialogClick)
                .create();
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
//                    mDialog.setContentView(R.layout.dialog_select_imge);
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

    private void toPhotoBefore() {
        AlertDialog mDialog = new AlertDialog.Builder(MaintainPhotoActivity.this)
                .setItems(arrayStringBefore, onBeforeDialogClick)
                .create();
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
//                    mDialog.setContentView(R.layout.dialog_select_imge);
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

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
            if (ContextCompat.checkSelfPermission(MaintainPhotoActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MaintainPhotoActivity.this,
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
                Toast.makeText(MaintainPhotoActivity.this, "sdcard不可用", Toast.LENGTH_SHORT).show();
            }
        }

        private void startImageCaptrue(DialogInterface dialog) {
            dialog.dismiss();
            CommonUtils.verifyStoragePermissions(MaintainPhotoActivity.this);
//            try {
//                Intent intent = new Intent(Intent.ACTION_PICK, null);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        "image/*");
//                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//            } catch (Exception e) {
//
//            }
            Intent intent = new Intent(MaintainPhotoActivity.this, PhotoActivity.class);
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

        }


    };
    private static final int TAKE_PHOTO_REQUEST_CODE = 2;
    DialogInterface.OnClickListener onBeforeDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    startImageCaptrue(dialog);// 开启图库
                    break;
                default:
                    break;
            }
        }

        private void startImageCaptrue(DialogInterface dialog) {
            dialog.dismiss();
            CommonUtils.verifyStoragePermissions(MaintainPhotoActivity.this);
//            try {
//                Intent intent = new Intent(Intent.ACTION_PICK, null);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        "image/*");
//                startActivityForResult(intent, PHOTO_BEFORE_REQUEST_GALLERY);
//            } catch (Exception e) {
//            }
            Intent intent = new Intent(MaintainPhotoActivity.this, PhotoActivity.class);
            startActivityForResult(intent, PHOTO_BEFORE_REQUEST_GALLERY);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (resultCode == RESULT_OK) {
                    if (operatingAnim != null) {
//                        tv_empty_pic.setVisibility(View.INVISIBLE);
                        iv_loading.setVisibility(View.VISIBLE);
                        iv_loading.startAnimation(operatingAnim);
                    }
                    ExecutorService poolThread = SingleThreadPool.getPoolThread();
                    poolThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (data.getData() != null || data.getExtras() != null) { //防止没有返回结果
                                pics.remove(pics.size() - 1);
                                if (pics.size() < 3) {
                                    Uri uri = data.getData();
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
                                    String s = BitmapUtils.compressImageUpload(filePath);
                                    final File file = new File(s);
                                    imageFilePath.add(s);
                                    pics.add(file);
//                                    if (pics.size() < 3) {
//                                    }
                                    handler.sendEmptyMessageDelayed(NOTIFI, 300);
                                } else {
                                    handler.sendEmptyMessage(MAX_SIZE);
                                }
                            } else {
                                handler.sendEmptyMessage(NOT_FIND);
                            }
                            pics.add(addPic);
                        }
                    });
                }
                break;
            case PHOTO_REQUEST_GALLERY:
                if (resultCode == PhotoActivity.CHOOSE_IMAGES) {
                    if (operatingAnim != null) {
//                        tv_empty_pic.setVisibility(View.INVISIBLE);
                        iv_loading.setVisibility(View.VISIBLE);
                        iv_loading.startAnimation(operatingAnim);
                    }
                    ExecutorService poolThread = SingleThreadPool.getPoolThread();
                    poolThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (data != null) {
                                List<String> photos = (List<String>) data.getSerializableExtra(Constants.PHOTOS);
                                pics.remove(pics.size() - 1);
                                for (String path :
                                        photos) {
                                    if (pics.size() < 3) {
//                                        Uri uri = data.getData();
//                                        String[] proj = {MediaStore.Images.Media.DATA};
//                                        //好像是Android多媒体数据库的封装接口，具体的看Android文档
//                                        Cursor cursor = managedQuery(uri, proj, null, null, null);
//                                        //获得用户选择的图片的索引值
//                                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
//                                        cursor.moveToFirst();
//                                        //最后根据索引值获取图片路径
//                                        String filePath = cursor.getString(column_index);
                                        String smallBitmap = BitmapUtils.compressImageUpload(path);
                                        imageFilePath.add(smallBitmap);
                                        final File file = new File(smallBitmap);
                                        pics.add(file);
//                                    if (pics.size() < 3) {
//                                    }
                                        handler.sendEmptyMessageDelayed(NOTIFI, 300);
                                    } else {
                                        handler.sendEmptyMessage(MAX_SIZE);
                                    }
                                }
                                pics.add(addPic);
                            }
                        }
                    });
                }
                break;
            case PHOTO_BEFORE_REQUEST_GALLERY:
                if (resultCode == PhotoActivity.CHOOSE_IMAGES) {
                    if (operatingAnim != null) {
//                        tv_empty_pic_before.setVisibility(View.INVISIBLE);
                        iv_loading_before.setVisibility(View.VISIBLE);
                        iv_loading_before.startAnimation(operatingAnim);
                    }
                    ExecutorService poolThread = SingleThreadPool.getPoolThread();
                    poolThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (data != null) {
                                List<String> photos = (List<String>) data.getSerializableExtra(Constants.PHOTOS);
                                picsBefore.remove(picsBefore.size() - 1);
                                for (String path :
                                        photos) {
                                if (picsBefore.size() < 3) {
//                                    Uri uri = data.getData();
//                                    String[] proj = {MediaStore.Images.Media.DATA};
//                                    //好像是Android多媒体数据库的封装接口，具体的看Android文档
//                                    Cursor cursor = managedQuery(uri, proj, null, null, null);
//                                    //获得用户选择的图片的索引值
//                                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
//                                    cursor.moveToFirst();
//                                    //最后根据索引值获取图片路径
//                                    String filePath = cursor.getString(column_index);
                                    String smallFilePath = BitmapUtils.compressImageUpload(path);
                                    imageFilePathBefore.add(smallFilePath);
                                    final File file = new File(smallFilePath);

                                    picsBefore.add(file);
//                                    if (picsBefore.size() < 3) {
//                                    }
                                    handler.sendEmptyMessageDelayed(NOTIFI_BEFORE, 300);
                                } else {
                                    handler.sendEmptyMessage(MAX_SIZE);
                                }}
                                picsBefore.add(addPic);
                            }
                        }
                    });
                }
                break;
        }
    }
}
