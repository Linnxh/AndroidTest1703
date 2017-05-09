package com.test.lxh.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片工具类，对图片进行一些处理
 *
 * @author renyangyang
 */
public class ImageUtil {

    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    /**
     * 图片旋转
     *
     * @param bmp
     * @param degree
     * @return
     */
    public static Bitmap postRotateBitamp(Bitmap bmp, float degree) {
        // 获得Bitmap的高和宽
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        // 产生resize后的Bitmap对象
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
                matrix, true);
        return resizeBmp;
    }

    /**
     * 图片放大缩小
     *
     * @param bmp
     * @return
     */
    public static Bitmap postScaleBitamp(Bitmap bmp, float sx, float sy) {
        // 获得Bitmap的高和宽
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        //System.out.println("before+w+h:::::::::::"+bmpWidth+","+bmpHeight);
        // 产生resize后的Bitmap对象
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
                matrix, true);
        //System.out.println("after+w+h:::::::::::"+resizeBmp.getWidth()+","+resizeBmp.getHeight());
        return resizeBmp;
    }

    /**
     * 图片 亮度调整
     *
     * @return
     */
    public static Bitmap postColorRotateBitamp(int hueValue, int lumValue, Bitmap bm) {
        // 获得Bitmap的高和宽
        //System.out.println(bm.getWidth()+","+bm.getHeight()+"------before");
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(bmp); // 得到画笔对象
        Paint paint = new Paint(); // 新建paint
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

        // 产生resize后的Bitmap对象
        ColorMatrix mAllMatrix = new ColorMatrix();
        ColorMatrix mLightnessMatrix = new ColorMatrix();
        ColorMatrix mSaturationMatrix = new ColorMatrix();
        ColorMatrix mHueMatrix = new ColorMatrix();

        float mHueValue = hueValue * 1.0F / 127;  //亮度
        mHueMatrix.reset();
        mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考

        float mSaturationValue = 127 * 1.0F / 127;//饱和度
        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);

        float mLumValue = (lumValue - 127) * 1.0F / 127 * 180; //色相
        mLightnessMatrix.reset(); // 设为默认值
        mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
        mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
        mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度


        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加
        mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
        canvas.drawBitmap(bm, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
        //System.out.println(bmp.getWidth()+","+bmp.getHeight()+"------after");
        return bmp;
    }

    /**
     * 读取资源图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 对图片进行处理
     * 1，首先判断 图片的宽和高
     * 2，如果宽和高都小于700，就放大到手机的宽度（要判断是否大于700）
     * 3，如果有一项大于700，就进行缩放，都小于700为止
     */
    public static Bitmap parseBitmap(Bitmap mBitmap, String path) {
        //1
        int imgWidth = mBitmap.getWidth();
        int imgHeight = mBitmap.getHeight();
        //2
        if (imgWidth > 700 || imgHeight > 700) {
            float sx = imgWidth > imgHeight ? ((float) 700) / (float) imgWidth
                    : ((float) 700) / (float) imgHeight;

            mBitmap = postScaleBitamp(mBitmap, sx, sx);
        } else {
            /*if(screenWidth<700){
                float sx = imgWidth > imgHeight ? ((float)screenWidth)/(float)imgWidth
						:((float)screenWidth)/(float)imgHeight;
				mBitmap = postScaleBitamp(mBitmap, sx, sx);
			}else{
				float sx = imgWidth > imgHeight ? ((float)700)/(float)imgWidth
						:((float)700)/(float)imgHeight;
				mBitmap = postScaleBitamp(mBitmap, sx, sx);
			}*/
            int value = imgWidth > imgHeight ? imgWidth : imgHeight;
            if (value < 100) {
                mBitmap = getBitmapByPath(path);
            } else {
                return mBitmap;
            }
        }
        return mBitmap;
    }

    public static Bitmap parseBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);


        int mWidth = 640;

        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (max > mWidth) {
            options.inSampleSize = max / mWidth;
            int height = options.outHeight * mWidth / max;
            int width = options.outWidth * mWidth / max;
            options.outWidth = width;
            options.outHeight = height;

        } else {
            options.inSampleSize = 1;
            options.outWidth = options.outWidth;
            options.outHeight = options.outHeight;
        }
        /* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;
        return getBitmapByPath(path, options);
    }

    public static Bitmap parseBitmapToLittle(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);


        int mWidth = 320;

        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (max > mWidth) {
            options.inSampleSize = max / mWidth;
            int height = options.outHeight * mWidth / max;
            int width = options.outWidth * mWidth / max;
            options.outWidth = 320;
            options.outHeight = 320;

        } else {
            options.inSampleSize = max / mWidth;
            options.outWidth = 320;
            options.outHeight = 320;
        }
        /* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;
        return getBitmapByPath(path, options);
    }

    /**
     * 压缩到屏幕尺寸
     *
     * @param path
     * @return
     */
//    public static Bitmap parseToScreenSize(String path) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;//不占用内存加载图片
//        BitmapFactory.decodeFile(path, options);
//        int bitmapWidth = options.outWidth;
//        int bitmapHeight = options.outHeight;
//        int reqWidth = DensityUtil.getWidth(App.getInstance().getApplication());
//        int reqHeight = DensityUtil.getHeight(App.getInstance().getApplication());
//        int inSampleSize = 1;
//        if (bitmapHeight > reqHeight || bitmapWidth > reqWidth) {
//            if (bitmapWidth > bitmapHeight) {
//                inSampleSize = Math.round((float) bitmapHeight / (float) reqHeight);
//            } else {
//                inSampleSize = Math.round((float) bitmapWidth / (float) reqWidth);
//            }
//        }
//        options.inSampleSize = inSampleSize;
//        options.inJustDecodeBounds = false;
//        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//        return bitmap;
//    }

    /**
     * 压缩到指定比例
     *
     * @param path
     * @return
     */
    public static Bitmap parseBitmapToSize(String path, int reqWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不占用内存加载图片
        BitmapFactory.decodeFile(path, options);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int inSampleSize = 1;
        if (bitmapHeight > reqWidth || bitmapWidth > reqWidth) {
            if (bitmapWidth > bitmapHeight) {
                inSampleSize = Math.round((float) bitmapHeight / (float) reqWidth);
            } else {
                inSampleSize = Math.round((float) bitmapWidth / (float) reqWidth);
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

   /* *//**
     * 写图片文件到SD卡 并且压缩
     *
     * @throws IOException
     *//*
    public static String saveImageToSD(Bitmap source, int quality, int bitmapOutSize) {
        FileOutputStream fos = null;
        ByteArrayOutputStream stream = null;
        Bitmap thumb = null;
        try {
            if (source != null) {
                String filePath = ImageUtil.setCameraImgPath(MyApplication.getInstance());
                fos = new FileOutputStream(filePath);
                stream = new ByteArrayOutputStream();
                int with = source.getWidth();//图片的宽
                int height = source.getHeight();//图片的高
                int max = with > height ? with : height;
                if (max > bitmapOutSize) {
                    //缩放
                    Matrix matrix = new Matrix();
                    float scale = bitmapOutSize * 1f / max;
                    matrix.setScale(scale, scale);
                    thumb = Bitmap.createBitmap(source, 0, 0, with, height, matrix, true);
                    thumb.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                } else {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                }
                byte[] bytes = stream.toByteArray();
                fos.write(bytes);
                fos.flush();
                return filePath;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (!source.isRecycled()) {
                source.recycle();
            }
            if (thumb != null && thumb.isRecycled()) {
                thumb.recycle();
            }
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/

   /* */

    /**
     * 设置保存 照片的路径
     *
     * @return
     */
//    public static String setCameraImgPath(Context context) {
//        String cameraImgPath = null;
//        String foloder = ImageUtil.getCachePath(context) + Constants.uploadImagePath;
//        File savedir = new File(foloder);
//        if (!savedir.exists()) {
//            savedir.mkdirs();
//        }
//        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
//                .format(new Date());
//        // 照片命名
//        String picName = timeStamp + ".jpg";
//        //  裁剪头像的绝对路径
//        cameraImgPath = foloder + picName;
//        return cameraImgPath;
//    }
    public static Bitmap parseHeadBitmapToLittle(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);


        int mWidth = 120;

        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (max > mWidth) {
            options.inSampleSize = max / mWidth;
            options.outWidth = 120;
            options.outHeight = 120;

        } else {
            options.inSampleSize = max / mWidth;
            options.outWidth = 120;
            options.outHeight = 120;
        }
        /* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;
        return getBitmapByPath(path, options);
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    public static String getPathFromUri(String uri, ContentResolver cr) {
        String imgPath = null;
        if (uri != null) {
            //小米手机的适配问题，小米手机的uri以file开头，其他的手机都以content开头
            //以content开头的uri表明图片插入数据库中了，而以file开头表示没有插入数据库
            //所以就不能通过query来查询，否则获取的cursor会为null。
            if (uri.startsWith("file")) {
                //uri的格式为file:///mnt....,将前七个过滤掉获取路径
                imgPath = uri.substring(7, uri.length());
                return imgPath;
            }
            Cursor cursor = cr.query(Uri.parse(uri), null, null, null, null);
            cursor.moveToFirst();
            imgPath = cursor.getString(1); // 图片文件路径
            imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();

        }
        return imgPath;
    }

    /**
     * 得到缩略图的 url
     *
     * @param id
     * @param path
     * @param cr
     * @return
     */
    public static String getThumbnail(int id, String path, ContentResolver cr) {
        //获取大图的缩略图
        Cursor cursor = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""},
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int thumId = cursor.getInt(0);
            String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().
                    appendPath(Integer.toString(thumId)).build().toString();
            cursor.close();
            return uri;
        }
        cursor.close();
        return null;
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        File cacheDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            cacheDir = context.getExternalCacheDir();
            cacheDir = Environment.getExternalStorageDirectory();
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }


    public static Bitmap scaleImageView(Bitmap bitmap, int width) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        //缩放
        Matrix matrix = new Matrix();
        float w_scale = width * 1.0f / bitmapWidth;
        matrix.setScale(w_scale, w_scale);
        Bitmap thumb = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        return thumb;
    }


    public static Bitmap scaleImageViewHeight(Bitmap bitmap, int height) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        //缩放
        Matrix matrix = new Matrix();
        float h_scale = (height + height) * 1.0f / bitmapHeight;
        matrix.setScale(h_scale, h_scale);
        Bitmap thumb = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        return thumb;
    }


    /****
     * 图片压缩
     *
     * @param filePath 文件路径
     * @return
     */
    public static String compressImage(Context context, String filePath) {
//        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true的话，在decode时将会返回null,通过此设置可以去查询一个bitmap的属性，比如bitmap的长与宽，而不占用内存大小
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath.toString(), options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = 0;
        if (actualHeight == 0 || actualWidth == 0) {
            actualHeight = (int) maxHeight;
            actualWidth = (int) maxWidth;
        } else {
            imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            // width and height 设置为bitmap宽高比
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }
        }


        // 获取图像压缩比
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        // 当存储Pixel的内存空间在系统内存不足时可以被回收，
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));

        // 检查图像是否旋转
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            // write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public static Bitmap getCompressImage(Context context, String filePath) {
//        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true的话，在decode时将会返回null,通过此设置可以去查询一个bitmap的属性，比如bitmap的长与宽，而不占用内存大小
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath.toString(), options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = 0;
        if (actualHeight == 0 || actualWidth == 0) {
            actualHeight = (int) maxHeight;
            actualWidth = (int) maxWidth;
        } else {
            imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            // width and height 设置为bitmap宽高比
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }
        }


        // 获取图像压缩比
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        // 当存储Pixel的内存空间在系统内存不足时可以被回收，
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));

        // 检查图像是否旋转
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        FileOutputStream out = null;
//        String filename = getFilename();
//        try {
//            out = new FileOutputStream(filename);
//            // write the compressed bitmap at the destination specified by filename.
//            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        return scaledBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }


    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Folder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    /**
     * 添加水印
     *
     * @param context      上下文
     * @param filePath     原图路径
     * @param markText     水印文字
     * @param markBitmapId 水印图片
     * @return bitmap      打了水印的图
     */
    public static Bitmap createWatermark(Context context, String filePath, String markText, int markBitmapId) {
        Bitmap bitmap = getCompressImage(context, filePath);

        // 当水印文字与水印图片都没有的时候，返回原图
        if (TextUtils.isEmpty(markText) && markBitmapId == 0) {
            return bitmap;
        }

        // 获取图片的宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 创建一个和图片一样大的背景图
        Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        // 画背景图
        canvas.drawBitmap(bitmap, 0, 0, null);
        //-------------开始绘制文字-------------------------------

        // 文字开始的坐标,默认为左上角
        float textX = 0;
        float textY = 0;
        int textWidth = 0;
        int textHeight = 0;

        if (!TextUtils.isEmpty(markText)) {
            // 创建画笔
            Paint mPaint = new Paint();
            // 文字矩阵区域
            Rect textBounds = new Rect();
            // 获取屏幕的密度，用于设置文本大小
            float scale = context.getResources().getDisplayMetrics().density;
            // 水印的字体大小
            mPaint.setTextSize((int) (11 * scale));
//            mPaint.setTextSize(20);
            // 文字阴影
            mPaint.setShadowLayer(0.5f, 0f, 1f, Color.BLACK);
            // 抗锯齿
            mPaint.setAntiAlias(true);
            // 水印的区域
            mPaint.getTextBounds(markText, 0, markText.length(), textBounds);
            // 水印的颜色
            mPaint.setColor(Color.WHITE);

            //  当图片大小小于文字水印大小的3倍的时候，不绘制水印
            if (textBounds.width() > bitmapWidth / 3 || textBounds.height() > bitmapHeight / 3) {
                Toast.makeText(context, "图片太小不适合添加水印文字", Toast.LENGTH_SHORT).show();
                return bitmap;
            }

            textWidth = textBounds.width();
            textHeight = textBounds.height();
            // 文字开始的坐标
            textX = bitmapWidth - textBounds.width() - 20;//这里的-10和下面的+6都是微调的结果
            textY = bitmapHeight - textBounds.height() - 20;
//            textX = bitmapWidth - 100 - 10;
//            textY = bitmapHeight - 100 + 6;
            // 画文字
            canvas.drawText(markText, textX, textY, mPaint);
        }

        //------------开始绘制图片-------------------------

        if (markBitmapId != 0) {
            // 载入水印图片
            Bitmap markBitmap = BitmapFactory.decodeResource(context.getResources(), markBitmapId);

            // 如果图片的大小小于水印的3倍，就不添加水印
            if (markBitmap.getWidth() > bitmapWidth / 3 || markBitmap.getHeight() > bitmapHeight / 3) {
                Toast.makeText(context, "图片太小不适合添加水印图片", Toast.LENGTH_SHORT).show();
                return bitmap;
            }
            int markBitmapWidth = markBitmap.getWidth();
            int markBitmapHeight = markBitmap.getHeight();

            // 图片开始的坐标
//            float bitmapX = (float) (bitmapWidth - markBitmapWidth - 10);//这里的-10和下面的-20都是微调的结果
//            float bitmapY = (float) (textY - markBitmapHeight - 20);
            float bitmapX = (float) (bitmapWidth - textWidth - markBitmapWidth - 20);
            float bitmapY = (float) (bitmapHeight - markBitmapHeight  - 20);

            // 画图
            canvas.drawBitmap(markBitmap, bitmapX, bitmapY, null);
        }

        //保存所有元素
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }

}
