package com.zw.express.tool;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.StringRequestCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lijie on 2016/4/7.
 */
public class PickUtils {

    private static final String TAG = "PickUtils";
    /**
     * 用来标识请求照相功能的activity
     */
    public static final int CAMERA_WITH_DATA = 168;
    /**
     * 用来标识请求gallery的activity
     */
    public static final int PHOTO_PICKED_WITH_DATA = CAMERA_WITH_DATA + 1;
    /**
     * 图片裁剪
     */
    public static final int PHOTO_CROP = PHOTO_PICKED_WITH_DATA + 1;
    /**
     * 拍照的照片存储位置
     */
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera/");

    private static File mCurrentPhotoFile;// 照相机拍照得到的图片

    /**
     * 得到本地图片路径
     *
     * @return
     */
    public static File getmCurrentPhotoFile() {
        if (mCurrentPhotoFile == null) {
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, "urgootemp" + SystemClock.currentThreadTimeMillis() + ".png");
            if (!mCurrentPhotoFile.exists())
                try {
                    mCurrentPhotoFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return mCurrentPhotoFile;
    }

    //    拨打电话
    public static void doPhone(final Activity context, final String phoneNumber) {
        final Context dialogContext = new ContextThemeWrapper(context,
                android.R.style.Theme_Light);
        View contentView = context.getLayoutInflater().inflate(
                R.layout.ppw_phone, null);
        Button but_pictures = (Button) contentView.findViewById(R.id.but_pictures);
        Button but_cancels = (Button) contentView.findViewById(R.id.but_cancel);
        RelativeLayout RelativeLayoutbj = (RelativeLayout) contentView.findViewById(R.id.RelativeLayoutbj);
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        final PopupWindow pw = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        pw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e0666666")));
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        pw.showAtLocation(new View(context), Gravity.CENTER, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                android.view.WindowManager.LayoutParams param = context
                        .getWindow().getAttributes();
                param.alpha = 1F;
                context.getWindow().setAttributes(param);
                pw.dismiss();
            }
        });

        but_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                //开启系统拨号器
                context.startActivity(intent);
                pw.dismiss();
            }
        });
        but_cancels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        RelativeLayoutbj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }


    /**
     * 开始启动照片选择框
     *
     * @param
     * @param
     */
    public static void doPickPhotoAction(final Activity context) {
        View contentView = context.getLayoutInflater().inflate(
                R.layout.ppw_photo, null);
        Button but_album = (Button) contentView.findViewById(R.id.but_album);
        Button but_pictures = (Button) contentView.findViewById(R.id.but_pictures);
        Button but_cancel = (Button) contentView.findViewById(R.id.but_cancel);
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        final PopupWindow pw = new PopupWindow(contentView,
                (int) (dm.widthPixels * 0.8), LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        android.view.WindowManager.LayoutParams params = context
                .getWindow().getAttributes();
        context.getWindow().setAttributes(params);
        pw.showAtLocation(new View(context), Gravity.CENTER, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                // TODO Auto-generated method stub
                android.view.WindowManager.LayoutParams param = context
                        .getWindow().getAttributes();
                param.alpha = 1F;
                context.getWindow().setAttributes(param);
                pw.dismiss();
            }
        });

        but_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.view.WindowManager.LayoutParams param = context
                        .getWindow().getAttributes();
                param.alpha = 1F;
                context.getWindow().setAttributes(param);
                doPickPhotoFromGallery(context);// 从相册中去获取
                pw.dismiss();
            }
        });
        but_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = Environment
                        .getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                    doTakePhoto(context);// 用户点击了从照相机获取
                } else {
                    Toast.makeText(context, "SDcard not found", Toast.LENGTH_SHORT).show();
                }
                android.view.WindowManager.LayoutParams param = context
                        .getWindow().getAttributes();
                param.alpha = 1F;
                context.getWindow().setAttributes(param);
                pw.dismiss();
            }
        });
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.view.WindowManager.LayoutParams param = context
                        .getWindow().getAttributes();
                param.alpha = 1F;
                context.getWindow().setAttributes(param);
                pw.dismiss();
            }
        });


    }

    /**
     * 拍照获取图片
     */
    private static void doTakePhoto(Activity context) {
        try {
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
//            mCurrentPhotoFile = new File(PHOTO_DIR, "urgootemp" + SystemClock.currentThreadTimeMillis() + ".png");// 给新照的照片文件命名
//            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            File dir = new File("/sdcard/itapimages/");
            if (!dir.exists()) dir.mkdirs();
            File tempPic = new File(dir, String.format("IMG_%d.jpg", System.currentTimeMillis()));
            ZWConfig.pickPicture = tempPic.getAbsolutePath();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 指定调用相机拍照后照片的储存路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempPic));
            context.startActivityForResult(intent,
                    CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * 请求Gallery相册程序
     *
     * @param
     * @param
     */
    private static void doPickPhotoFromGallery(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            ((Activity) context).startActivityForResult(intent,
                    PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
        }
    }


    /**
     * 所有图片裁剪回调
     *
     * @param context
     * @param uri     图片资源地址
     */
    public static void doCropPhoto(Activity context, Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX,outputY 是剪裁图片的宽高
            int width = Util.getDeviceWidth(context);
            width = width > 640 ? 640 : width;
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", width);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            File dir = new File("/sdcard/itapimages/");
            if (!dir.exists()) dir.mkdirs();
            File tempPic = new File(dir, String.format("IMG_%d.jpg", System.currentTimeMillis()));
            ZWConfig.tempPicture = Uri.fromFile(tempPic);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, ZWConfig.tempPicture);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            context.startActivityForResult(intent, PHOTO_CROP);
        } catch (Exception e) {
            Log.e(TAG, "裁剪:" + e.toString());
        }
    }

    /**
     * 获取不裁剪时图片返回的路径
     *
     * @param activity
     * @param data
     * @return path
     */
    public static String getNoCropPath(Activity activity, Intent data) {

        String path = "";
        if (data == null)
            return getmCurrentPhotoFile().toString();
        Uri imageuri = data.getData();
        if (imageuri == null) {
            String str = data.getAction();
            imageuri = Uri.parse(str);
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(imageuri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            path = cursor.getString(column_index);
        }
        return path;
    }

    /**
     * 上传图片
     *
     * @param path
     */
    public static void uploadImage(final Activity context, String path) {

//        new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("request = ", String.valueOf(call));
//                Looper.prepare();
//                Toast.makeText(context, "上传头像失败重新上传", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("response = ", response.body().string());
//                Looper.prepare();
//                Toast.makeText(context, "头像修改成功", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }
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
            switch(orientation) {
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

    /**
     * 如果图片拍照时做了旋转，则旋转为正的，并存储
     *
     * @param path
     */
    public static void rotatePhotoAndSave(String path) {
        int degree = readPictureDegree(path);
        if (degree == 0) return;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,
                bm.getWidth(), bm.getHeight(), matrix, true);
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bm.recycle();
            rotatedBitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    /**
//     * 裁剪后的图片路径
//     *
//     * @return
//     */
//    public static String getCropPath(Intent data) {
//        Bundle extras = data.getExtras();
//        Bitmap myBitmap = (Bitmap) extras.get("data");
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(PickUtils.getmCurrentPhotoFile());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        return PickUtils.getmCurrentPhotoFile().toString();
//    }
}
