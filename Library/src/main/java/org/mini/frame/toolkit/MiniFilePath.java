package org.mini.frame.toolkit;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by admin on 2015/7/29.
 */
public class MiniFilePath {
    public static String SDCARD_PATH;

    /**
     * 得到缓存路径 1，如果有sd卡 就是sd空间 2，没有sd卡 就是手机系统分配空间
     *
     * @param context
     */
    public static void getPath(Context context) {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            SDCARD_PATH = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            SDCARD_PATH = context.getCacheDir().getPath();
        }
    }



    /**
     * 获取SD卡目录下相对应包名程序下的图片缓存的路径
     *
     * @param context
     * @return
     */
    public static String getDefaultImagePath(Context context) {
        getPath(context);
        if (context != null && SDCARD_PATH != null) {
            String path = SDCARD_PATH + "/" + context.getPackageName()
                    + "/image/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        }
        return null;
    }


    //判断文件是否存在
    public static boolean fileIsExists(String name) {
        File f = new File(name);
        if (!f.exists()) {
            return false;
        }
        return true;
    }



}
