package org.mini.frame.photoview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.mini.frame.photo.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/7/14.
 */
public class MiniPreviewViewPagerAdapter extends PagerAdapter{

    private List<PhotoView> mAllPagerViews;
    private ArrayList<MiniPhotoItem> mImageItems;
    private Context context;

    public MiniPreviewViewPagerAdapter(List<PhotoView> mAllPagerViews, ArrayList<MiniPhotoItem> mImageItems) {

        this.mAllPagerViews = mAllPagerViews;
        this.mImageItems = mImageItems;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mAllPagerViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setBackgroundColor(Color.BLACK);
        mAllPagerViews.remove(position);
        mAllPagerViews.add(position, photoView);
        container.addView(photoView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        String url = mImageItems.get(position).imagePath;
        MiniCustomSelectPicImageLoaderUtil.isCancel=true;
        MiniCustomSelectPicImageLoaderUtil.loadLoaclImage(photoView, url);
        return photoView;
    }


}
