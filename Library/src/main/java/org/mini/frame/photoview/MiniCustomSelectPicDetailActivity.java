package org.mini.frame.photoview;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.define.CEConst;

import org.mini.frame.annotation.Action;
import org.mini.frame.notification.Notification;
import org.mini.frame.notification.NotificationCenter;
import org.mini.frame.toolkit.media.MiniAnimation;
import org.mini.frame.view.MiniCustomDialog;

import java.util.ArrayList;

/**
 * Created by hqh on 2015/7/13.
 */
public class MiniCustomSelectPicDetailActivity extends MNActivityBase implements AdapterView.OnItemClickListener,
    View.OnClickListener {

  private TextView mCount; // 共..张图片
  private TextView mPreview; // 预览
  private TextView mComplete; // 完成
  private GridView mGridView;
  private MiniCustomSelectPicDetailAdapter mAdapter;

  private ArrayList<MiniPhotoItem> mDataOrderList; // 按点击顺序储存图片
  private String mTitleName; // 标题名字
  private int imageLimitCount;

  private int clickPosition = 0;// 记录单张的位置

  private MiniPhotoBucket MiniPhotoBucket;

  @Override
  protected void loadView() {
    setContentView(R.layout.activity_custom_select_pic_detail);
    this.initData();
    this.initView();
    this.setTitle(mTitleName);
    this.setNaviRightTitleWithListener(getString(R.string.cancel), new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void initData() {
    mDataOrderList = new ArrayList<>();
    Intent intent = getIntent();
    if (intent != null) {
      MiniPhotoBucket = (MiniPhotoBucket) intent.getSerializableExtra(CEConst.EXTRA_IMAGE_LIST);
      mTitleName = intent.getStringExtra(CEConst.EXTRA_IMAGE_NAME);
      imageLimitCount = intent.getIntExtra(CEConst.EXTRA_IMAGE_LIMIT_COUNT, 0);
    }

  }

  private void initView() {
    NotificationCenter.defaultNotificationCenter().register(CEConst.CE_SEND_IMAGES, this);
    mCount = (TextView) findViewById(R.id.custom_select_pic_detail_count);
    mPreview = (TextView) findViewById(R.id.custom_select_pic_detail_preview);
    mComplete = (TextView) findViewById(R.id.custom_select_pic_detail_complete);
    mCount.setText(getString(R.string.pic_common) + MiniPhotoBucket.imageList.size() + getString(R.string.pic_num));

    mGridView = (GridView) findViewById(R.id.custom_select_pic_detail_grid_view);
    mAdapter = new MiniCustomSelectPicDetailAdapter();
    mGridView.setAdapter(mAdapter);
    mGridView.setOnItemClickListener(this);

    findViewById(R.id.custom_select_pic_detail_preview, this);
    findViewById(R.id.custom_select_pic_detail_complete, this);
  }


  // 预览
  @Action(R.id.custom_select_pic_detail_preview)
  public void actionPreview() {
    // 多张图片预览
    if (mDataOrderList.size() > 0) {
      Intent intent = new Intent(MiniCustomSelectPicDetailActivity.this, MiniCustomSelectPicPreviewActivity.class);
      intent.putExtra("singlePic", false);
      intent.putExtra(CEConst.EXTRA_IMAGE_LIMIT_COUNT, imageLimitCount);
      intent.putExtra("MiniPhotoItems", mDataOrderList);
      intent.putExtra("position", 0);
      startActivityForResult(intent, 35);
    }
  }

  // 完成 返回传递数据
  @Action(R.id.custom_select_pic_detail_complete)
  public void actionComplete() {
    if (mDataOrderList.size() > 0) {
      ArrayList<String> imagePics = new ArrayList<>();
      for (MiniPhotoItem picItemBean : mDataOrderList) {
        if (picItemBean.isSelected) {
          imagePics.add(picItemBean.imagePath);
        }
      }
      Notification notification = new Notification();
      notification.setInfo(CEConst.CE_RECEIVE_IMAGES_KEY, imagePics);
      notification.setKey(CEConst.CE_SEND_IMAGES);
      NotificationCenter.defaultNotificationCenter().post(notification);
      finish();
    }
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    // 单张图片预览
    this.clickPosition = position;
    ArrayList<MiniPhotoItem> picItems = new ArrayList<>();
    picItems.add(MiniPhotoBucket.imageList.get(position));
    Intent intent = new Intent(MiniCustomSelectPicDetailActivity.this, MiniCustomSelectPicPreviewActivity.class);
    intent.putExtra("singlePic", true);
    intent.putExtra(CEConst.EXTRA_IMAGE_LIMIT_COUNT, imageLimitCount);
    intent.putExtra("MiniPhotoItems", picItems);
    intent.putExtra("selectMiniPhotoItems", mDataOrderList);
    intent.putExtra("position", 0);
    startActivityForResult(intent, 34);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (resultCode) {
      case CEConst.SINGLE_PIC: // 单张图片
        MiniPhotoBucket.imageList.get(clickPosition).isSelected=data.getBooleanExtra("singlePic", false);
        if (MiniPhotoBucket.imageList.get(clickPosition).isSelected) {
          addReatDataOrderList(MiniPhotoBucket.imageList.get(clickPosition));
        } else {
          removeReatDataOrderList(MiniPhotoBucket.imageList.get(clickPosition));
        }
        updateView(clickPosition);
        break;

      case CEConst.MORE_SINGLE_PIC: // 多张图片
        ArrayList<MiniPhotoItem> MiniPhotoItems =
            (ArrayList<MiniPhotoItem>) data.getSerializableExtra("MiniPhotoItems");
        for (MiniPhotoItem MiniPhotoItem : MiniPhotoItems) {
          MiniPhotoBucket.imageList.get(MiniPhotoItem.position).isSelected=MiniPhotoItem.isSelected;

          if (MiniPhotoItem.isSelected) {
            addReatDataOrderList(MiniPhotoItem);
          } else {
            removeReatDataOrderList(MiniPhotoItem);
          }
          updateView(MiniPhotoItem.position);
        }
        break;
    }
    changeBottom();
    super.onActivityResult(requestCode, resultCode, data);
  }

  class MiniCustomSelectPicDetailAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      if (MiniPhotoBucket.imageList == null) {
        return 0;
      } else {
        return MiniPhotoBucket.imageList.size();
      }
    }

    @Override
    public Object getItem(int position) {
      return MiniPhotoBucket.imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final ViewHolder holder;
      if (convertView == null) {
        convertView =
            LayoutInflater.from(MiniCustomSelectPicDetailActivity.this).inflate(
                R.layout.view_custom_select_pic_detail_adpter, null);
        holder = new ViewHolder();
        holder.mDetailImage = (ImageView) convertView.findViewById(R.id.item_pic_detail_image);
        holder.mIsSelect = (ImageView) convertView.findViewById(R.id.item_pic_detail_is_selected);
        holder.mIsSelectLayout = (LinearLayout) convertView.findViewById(R.id.item_pic_detail_is_selected_layout);
        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }
      MiniPhotoBucket.imageList.get(position).position=position;
      if (MiniPhotoBucket.imageList.get(position).isSelected){
          holder.mIsSelect.setImageResource(R.drawable.custom_select_parent_pic_detail_check_press);
      }else{
        holder.mIsSelect.setImageResource(R.drawable.custom_select_pic_detail_check_normal);
      }

      MiniCustomSelectPicImageLoaderUtil.isCancel=false;
      if (!TextUtils.isEmpty(MiniPhotoBucket.imageList.get(position).thumbnailPath)) {
        MiniCustomSelectPicImageLoaderUtil.loadLoaclImage(holder.mDetailImage, MiniPhotoBucket.imageList.get(position).thumbnailPath);
      } else {
        MiniCustomSelectPicImageLoaderUtil.loadLoaclImage(holder.mDetailImage, MiniPhotoBucket.imageList.get(position).imagePath);
      }

      holder.mIsSelectLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (imageLimitCount != 0 && mDataOrderList.size() >= imageLimitCount) {
            if (!MiniPhotoBucket.imageList.get(position).isSelected) {
              showLimitDialog(imageLimitCount);
              return;
            }
          }
          if (!MiniPhotoBucket.imageList.get(position).isSelected) {
              holder.mIsSelect.setImageResource(R.drawable.custom_select_parent_pic_detail_check_press);
              MiniAnimation.addAnimation(holder.mIsSelect);
             MiniPhotoBucket.imageList.get(position).isSelected=true;
            addReatDataOrderList(MiniPhotoBucket.imageList.get(position));
          } else {
            holder.mIsSelect.setImageResource(R.drawable.custom_select_pic_detail_check_normal);
            MiniPhotoBucket.imageList.get(position).isSelected=false;
            removeReatDataOrderList(MiniPhotoBucket.imageList.get(position));
          }
          changeBottom();
        }
      });

      return convertView;
    }

  }

  static class ViewHolder {
    ImageView mDetailImage;
    LinearLayout mIsSelectLayout;
    ImageView mIsSelect;

  }

  /**
   * 最多选择九张图片
   *
   * @param limitCount max9
   */
  private void showLimitDialog(int limitCount) {

    MiniCustomDialog.show(MiniCustomSelectPicDetailActivity.this, getString(R.string.hint),
        getString(R.string.pic_more_select) + limitCount + "" + getString(R.string.pic_num),
        getString(R.string.I_know), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });

  }


  // 底部变化
  private void changeBottom() {
    if (mDataOrderList.size() > 0) {
      int parentColor = getResources().getColor(R.color.common_green);
      mPreview.setTextColor(parentColor);
        mPreview.setBackgroundResource(R.drawable.custom_select_image_bottom_selecter);
      mComplete.setBackgroundResource(R.drawable.custom_select_image_bottom_selecter);
    } else {
      int normalColor = getResources().getColor(R.color.common_pic_color);
      mPreview.setTextColor(normalColor);
      mComplete.setTextColor(normalColor);
      mPreview.setBackgroundResource(R.drawable.custom_select_pic_detail_image_normal);
      mComplete.setBackgroundResource(R.drawable.custom_select_pic_detail_image_normal);
    }
    mComplete.setText(getString(R.string.pic_complete) + "(" + mDataOrderList.size() + ")");
  }

  /**
   * 去除按点击顺序储存集合
   */
  private void removeReatDataOrderList(MiniPhotoItem picItem) {
    for (int i = 0; i < mDataOrderList.size(); i++) {
      if (mDataOrderList.get(i).position == picItem.position) {
        mDataOrderList.remove(i);
      }
    }

  }

  /**
   * 添加按点击顺序储存集合
   */
  private void addReatDataOrderList(MiniPhotoItem picItem) {
    for (int i = 0; i < mDataOrderList.size(); i++) {
      if (mDataOrderList.get(i).position == picItem.position) {
        return;
      }
    }
    mDataOrderList.add(picItem);
  }

  /**
   * 局部刷新数据
   *
   * @param position item的位置
   */
  private void updateView(int position) {
    MiniPhotoItem picItemBean = MiniPhotoBucket.imageList.get(position);
    // 得到第一个可显示控件的位置
    int firstVisiblePosition = mGridView.getFirstVisiblePosition();
    // 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
    if (position - firstVisiblePosition >= 0) {
      // 得到要更新的item的view
      View view = mGridView.getChildAt(position - firstVisiblePosition);
      // 从view中取得holder设置相应控件数据
      ViewHolder viewHolder = (ViewHolder) view.getTag();
      viewHolder.mDetailImage = (ImageView) view.findViewById(R.id.item_pic_detail_image);
      viewHolder.mIsSelect = (ImageView) view.findViewById(R.id.item_pic_detail_is_selected);
      if (!TextUtils.isEmpty(picItemBean.thumbnailPath)) {
        MiniCustomSelectPicImageLoaderUtil.loadLoaclImage(viewHolder.mDetailImage, picItemBean.thumbnailPath);
      } else {
        MiniCustomSelectPicImageLoaderUtil.loadLoaclImage(viewHolder.mDetailImage, picItemBean.imagePath);
      }
      if (picItemBean.isSelected) {
          viewHolder.mIsSelect.setImageResource(R.drawable.custom_select_parent_pic_detail_check_press);
      } else {
          viewHolder.mIsSelect.setImageResource(R.drawable.custom_select_pic_detail_check_normal);
      }
    }

  }


  public void onDestroy() {
    super.onDestroy();
    NotificationCenter.defaultNotificationCenter().remove(this);
  }

  @org.mini.frame.annotation.Notification(CEConst.CE_SEND_IMAGES)
  public void onReceivedSendSuccessNotification(org.mini.frame.notification.Notification notification) {
    finish();
  }
}
