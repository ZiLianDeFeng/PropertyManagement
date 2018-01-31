package com.it.hgad.logisticsmanager.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.Order;
import com.it.hgad.logisticsmanager.constants.Constants;
import com.it.hgad.logisticsmanager.dao.CommitDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SingleThreadPool;
import com.it.hgad.myapplication.HandwritingActivity;
import com.it.hgad.myapplication.WritePadDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/13.
 */
public class EvaluateActivity extends CommonActivity {

    private TextView et_handwrite;
    private Bitmap mSignBitmap;
    private File handWriteFile;
    private Order order;
    private DbUtils db;
    private CommitDb commitDb;
    private RadioGroup rg_pleased;
    private RadioButton rb2;
    private RadioButton rb1;
    private RadioButton rb3;
    private String pleased;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        initHeader("科室评价");
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
            String satisfy = commitDb.getSatisfy();
            if (satisfy != null) {
                if (rb1.getText().toString().trim().equals(satisfy)) {
                    rg_pleased.check(R.id.rb_1);
                    pleased = rb1.getText().toString().trim();
                } else if (rb2.getText().toString().trim().equals(satisfy)) {
                    rg_pleased.check(R.id.rb_2);
                    pleased = rb2.getText().toString().trim();
                } else if (rb3.getText().toString().trim().equals(satisfy)) {
                    rg_pleased.check(R.id.rb_3);
                    pleased = rb3.getText().toString().trim();
                }
            }
            String handWrite = commitDb.getHandWrite();
            if (handWrite != null && !"".equals(handWrite)) {
                mSignBitmap = BitmapFactory.decodeFile(handWrite);
                InsertToEditText(mSignBitmap);
                handWriteFile = new File(handWrite);
            }
        }
        choosePleased();
    }

    private void initView() {
        TextView tv_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_confirm.setOnClickListener(this);
        tv_confirm.setVisibility(View.VISIBLE);
        et_handwrite = (TextView) findViewById(R.id.et_handwrite);
        rg_pleased = (RadioGroup) findViewById(R.id.rg_pleased);
        rb1 = (RadioButton) findViewById(R.id.rb_1);
        rb2 = (RadioButton) findViewById(R.id.rb_2);
        rb3 = (RadioButton) findViewById(R.id.rb_3);
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
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                commit();
                break;
        }

    }

    private void choosePleased() {
        rg_pleased.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb1.getId() == checkedId) {
                    pleased = rb1.getText().toString().trim();
                } else if (rb2.getId() == checkedId) {
                    pleased = rb2.getText().toString().trim();
                } else if (rb3.getId() == checkedId) {
                    pleased = rb3.getText().toString().trim();
                }
            }
        });
    }

    private void commit() {
        if (pleased == null) {
            CommonUtils.showToast(EvaluateActivity.this, "综合评级不能为空哦！");
        } else {
            if (commitDb == null) {
                commitDb = new CommitDb();
                commitDb.setRepairId(order.getId());
            }
            commitDb.setSatisfy(pleased);
            if (handWriteFile!=null) {
                commitDb.setHandWrite(handWriteFile.getAbsolutePath());
            }
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
    }

    public void edit(View view) {
        WritePadDialog writeTabletDialog = new WritePadDialog(
                EvaluateActivity.this, new HandwritingActivity.DialogListener() {
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
            String sign_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.HANDWRITE_CACHE;
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
