package org.mini.frame.photoview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mini.R;

import java.util.Collections;
import java.util.List;

/**
 * 自定义选择相册列表adapter
 *
 * @author Gassion
 */
public class MiniCustomSelectPicAdapter extends BaseAdapter {

    private Context context;
    private List<MiniPhotoBucket> dataList;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setList(List<MiniPhotoBucket> dataList) {
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        return null != dataList ? dataList.size() : 0;
    }

    @Override
    public MiniPhotoBucket getItem(int position) {
        return null != dataList ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_custom_select_pic_adapter, null);
            holder = new ViewHolder();
            holder.mItemPicImage = (ImageView) convertView.findViewById(R.id.item_select_pic_image);
            holder.mItemPicName = (TextView) convertView.findViewById(R.id.item_select_pic_name);
            holder.mItemPicCount = (TextView) convertView.findViewById(R.id.item_select_pic_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MiniPhotoBucket imageBucket = dataList.get(position);
        if (!TextUtils.isEmpty(imageBucket.bucketName)) {
            holder.mItemPicName.setText(imageBucket.bucketName);
        }
        if (null != dataList.get(position).imageList) {
            Collections.reverse(dataList.get(position).imageList);
            MiniCustomSelectPicImageLoaderUtil.isCancel=false;
            if (!TextUtils
                    .isEmpty(dataList.get(position).imageList.get(0).thumbnailPath)) {
                MiniCustomSelectPicImageLoaderUtil.loadLoaclImage(holder.mItemPicImage,
                        imageBucket.imageList.get(0).thumbnailPath);
            } else {
                MiniCustomSelectPicImageLoaderUtil.loadLoaclImage(holder.mItemPicImage,
                        imageBucket.imageList.get(0).imagePath);
            }
        }
        holder.mItemPicCount.setText("("+imageBucket.count+")");

        return convertView;
    }


    class ViewHolder {
        ImageView mItemPicImage;
        TextView mItemPicName;
        TextView mItemPicCount;
    }


}
