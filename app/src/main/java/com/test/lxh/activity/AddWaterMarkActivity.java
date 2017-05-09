package com.test.lxh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.lxh.R;
import com.test.lxh.utils.ImageUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddWaterMarkActivity extends AppCompatActivity {

    @BindView(R.id.btn_openCamera)
    Button btn_openCamera;
    @BindView(R.id.btn_openAlbum)
    Button btn_openAlbum;
    @BindView(R.id.iv_waterMark_orignal)
    ImageView iv_waterMark_orignal;
    @BindView(R.id.iv_waterMark_add)
    ImageView iv_waterMark_add;

    private static final int REQUEST_CODE_OPENCAMERA = 2001;
    private static final int REQUEST_CODE_OPENALBUM = 2002;
    String photoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_water_mark);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_openAlbum, R.id.btn_openCamera})
    void onMyClick(View view) {
        switch (view.getId()) {
            case R.id.btn_openCamera:
                photoName = getFileName(1);
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra("return-data", false);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoName)));
                intent1.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent1.putExtra("noFaceDetection", false);
//                mContext.startActivityForResult(intent, requestCode);//or TAKE_SMALL_PICTURE
                startActivityForResult(intent1, REQUEST_CODE_OPENCAMERA);
                break;
            case R.id.btn_openAlbum:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_OPENCAMERA:
                    if (!TextUtils.isEmpty(photoName)) {
                      String path=  ImageUtil.compressImage(this,photoName);
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        // 设为true的话，在decode时将会返回null,通过此设置可以去查询一个bitmap的属性，比如bitmap的长与宽，而不占用内存大小
//                        options.inJustDecodeBounds = true;
//                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        Bitmap waterBit = ImageUtil.createWatermark(getApplication(), photoName, "掌合天下", R.mipmap.ic_launcher);
//                        iv_waterMark_orignal.setImageBitmap(Glide);
                        iv_waterMark_add.setImageBitmap(waterBit);

                        Glide.with(this)
                                .load(photoName)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .priority(Priority.HIGH)
                                .fitCenter()
                                .into(iv_waterMark_orignal);
//                        Glide.with(this)
//                                .load(waterBit)
//                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                                .priority(Priority.HIGH)
//                                .centerCrop()
//                                .into(iv_waterMark_add);
                    }
                    break;
//                case REQUEST_CODE_OPENCAMERA:
////                    UtilTools.makePhoto(getBaseContext(),photoName);
////                    Bitmap bitmap = UtilTools.convertToBitmap(photoName, 100, 100);
////                    ImageView imgview = new ImageView(this);
////                    imgview.setImageBitmap(bitmap);
//                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static String getFileName(int type) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        String name = formater.format(curDate);
        if (type == 1) {
            name = sdPicPath() + name + ".jpg";
        } else if (type == 2) {
//            name = sdVedioPath() + name + ".mp4";
        }
        return name;
    }

    /**
     * 判断是否存在照片存储的路径，不存在则创建
     */
    public static String sdPicPath() {
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/test_17_03/Picture";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        return path + "/";
    }


}
