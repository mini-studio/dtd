package org.mini.frame.view;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.mini.R;

/**
 * Created by admin on 2015/6/26.
 */
public class MiniImageLoadHelp {

    /**
     * 广场add
     *
     * @return
     */
    public static DisplayImageOptions circleImageAdd() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.plaza_item_add).showStubImage(R.drawable.plaza_item_add)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory().cacheOnDisc().build();
        return displayImageOptions;
    }

    /**
     * 公共的头像圆图
     *
     * @return
     */
    public static DisplayImageOptions circleImageOption() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.school_news_details_questionnaire_main_loading).showStubImage(R.drawable.school_news_details_questionnaire_main_loading)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory().cacheOnDisc().build();
        return displayImageOptions;
    }
    /**
     * 公共的头像
     *
     * @return
     */
    public static DisplayImageOptions commonAvatarImageOption() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.icon_default_100).showStubImage(R.drawable.icon_default_100)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory().cacheOnDisc().build();
        return displayImageOptions;
    }



}
