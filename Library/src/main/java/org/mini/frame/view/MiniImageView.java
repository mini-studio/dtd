package org.mini.frame.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.mini.R;

/**
 * Created by Wuquancheng on 15/4/11.
 */
public class MiniImageView extends ImageView {

    private Object info;

    public MiniImageView(Context context) {
        super(context);
    }

    public MiniImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MiniImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.BASE)
    public MiniImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url) {
        this.setImageUrl(url, null, 0);
    }

    public void setImageUrl(String url, int defaultResId) {
        this.setImageUrl(url, null, defaultResId);
    }

    public void setImageUrl(String url, DisplayImageOptions options) {
        this.setImageUrl(url, options, 0);
    }

    public void setImageUrl(String url, DisplayImageOptions options, int defaultResId) {
        if (defaultResId > 0) {
            this.setImageResource(defaultResId);
        }
        if (options == null) {
            DisplayImageOptions.Builder build =
                    new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                            .considerExifParams(true).displayer(new RoundedBitmapDisplayer(0));
            if (defaultResId > 0) {
                build.showImageOnFail(defaultResId);
            }
            options = build.build();
        }
        if (url != null && url.length() > 0) {
            ImageLoader.getInstance().displayImage(url, this, options);
        }
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    /**
     * 公共的头像圆图
     *
     * @return
     */
    public static DisplayImageOptions circleImageOption() {
        DisplayImageOptions displayImageOptions =
                new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.icon_default_100)
                        .showImageOnLoading(R.drawable.icon_default_100).showImageOnFail(R.drawable.icon_default_100)
                        .displayer(new RoundedBitmapDisplayer(8)).cacheInMemory(true).cacheOnDisk(true).build();
        return displayImageOptions;
    }

    /**
     * 公共的方图
     *
     * @return
     */
    public static DisplayImageOptions squareImageOption() {
        DisplayImageOptions displayImageOptions =
                new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.school_news_details_questionnaire_main_loading)
                        .showImageOnFail(R.drawable.school_news_details_questionnaire_main_loading)
                        .showImageOnLoading(R.drawable.school_news_details_questionnaire_main_loading)
                        .cacheInMemory(true)
                        .cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0))
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
        return displayImageOptions;
    }

    /**
     * 学校动态banner默认图
     *
     * @return
     */
    public static DisplayImageOptions squareViewPagerImageOption() {
        DisplayImageOptions displayImageOptions =
                new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.school_news_item_main_viewpager_loading)
                        .showImageOnFail(R.drawable.school_news_item_main_viewpager_loading)
                        .showImageOnLoading(R.drawable.school_news_item_main_viewpager_loading)
                        .displayer(new RoundedBitmapDisplayer(0)).cacheInMemory(true).cacheOnDisk(true).build();
        return displayImageOptions;
    }

    /**
     * 只有一张数据的时候
     *
     * @return
     */
    public static DisplayImageOptions singleImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.school_news_details_questionnaire_main_loading)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.school_news_details_questionnaire_main_loading)
                        // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.school_news_details_questionnaire_main_loading).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0))
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
        return options;

    }

    /**
     * 只有一张数据的时候
     *
     * @return
     */
    public static DisplayImageOptions listImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.school_news_list_questionnaire_main_loading)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.school_news_list_questionnaire_main_loading)
                        // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.school_news_list_questionnaire_main_loading).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0))
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
        return options;

    }

}
