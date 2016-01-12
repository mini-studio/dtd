package org.mini.frame.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by huangqihua on 15/9/25.
 * <p/>
 * 旋转图片
 */
public class MiniRotateImageUtil {

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）：
     *
     * @param srcPath
     * @return
     */
    public static byte[] getImageBytes(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 设置固定分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 600f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        } else if (w == h) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        int degree = readPictureDegree(srcPath);
        Bitmap newbitmap = rotatingImageView(degree, bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        newbitmap.recycle();
        return baos.toByteArray();// 压缩好比例大小后再进行质量压缩
    }


    /**
     * 获取图片旋转的角度
     *
     * @param path 图片的路径
     * @return 返回旋转的角度值
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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
     *
     * @param angle 图片旋转的角度
     *
     * @param bitmap
     */
    public static Bitmap rotatingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = null;
        if (bitmap != null) {
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return resizedBitmap;
    }


}
