package org.mini.frame.toolkit;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Size;

public class MiniImageBitmapUtil {

    public static class MiniSize {
        private int mWidth;
        private int mHeight;

        public MiniSize(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public void setWidth(int mWidth) {
            this.mWidth = mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public void setHeight(int mHeight) {
            this.mHeight = mHeight;
        }
    }

	public static Bitmap getDiskBitmap(String fileName) {
		Bitmap bitmap = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(fileName);
			}
		} catch (Exception e) {
			return null;
		}

		return bitmap;
	}

    public static MiniSize getImageSize(String fileName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(fileName, options);
        return new MiniSize(options.outWidth,options.outHeight);
    }

    public static Bitmap getImage(String fileName, int width, int height) {
        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, newOpts);
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;// be=1表示不缩放
            if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
                be = (newOpts.outWidth / width);
            } else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
                be = (newOpts.outHeight / height);
            } else if (w == h) {// 如果高度高的话根据宽度固定大小缩放
                be = (newOpts.outHeight / height);
            }
            if (be <= 0) {
                be = 1;// be=1表示不缩放
            }
            newOpts.inSampleSize = be;// 设置缩放比例
            newOpts.inJustDecodeBounds = false;

            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, newOpts);
            if (bitmap != null) {
                //bitmap.recycle();
                return bitmap;
            }
        } catch (Throwable err) {
            return null;
        }
        return null;
    }
}
