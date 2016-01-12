package org.mini.frame.photoview;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.define.CEConst;

import org.mini.frame.annotation.Action;
import org.mini.frame.notification.Notification;
import org.mini.frame.notification.NotificationCenter;
import org.mini.frame.photo.HackyViewPager;
import org.mini.frame.photo.PhotoView;
import org.mini.frame.view.MiniCustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hqh on 2015/7/13. 自定义选择图片预览界面
 */
public class MiniCustomSelectPicPreviewActivity extends MNActivityBase implements ViewPager.OnPageChangeListener {

    private ImageView mSelectImage; //
    private LinearLayout mCenterLayout;
    private TextView mCurrentNum;
    private TextView mTotalNum;
    private TextView mBottomTotalCount;
    private TextView mBottomComplete;
    private HackyViewPager mViewPager;
    private MiniPreviewViewPagerAdapter mAdapter;


    private PhotoView mPhotoView;
    private List<PhotoView> mAllPagerViews;

    private ArrayList<MiniPhotoItem> mMiniPhotoItems;
    private int mCurrentPager;
    private boolean bSinglePic; // 单张多张图片的标示
    private int imageLimitCount; // 限制数量
    private boolean bSinglePicSelect; // 单张图片是否选中
    private int previewPicPosition = 0; // 预览图片的位置
    private int selectPicCount = 0; // 记录选中的数量
    private ArrayList<MiniPhotoItem> mSelectMiniPhotoItems;


    @Override
    protected void loadView() {
        setContentView(R.layout.activity_custom_select_pic_preview);
        initView();
        initData();
    }

    private void initView() {

        mSelectImage = (ImageView) findViewById(R.id.custom_select_pic_preview_check);
        mCenterLayout = (LinearLayout) findViewById(R.id.custom_select_pic_preview_center_layout);
        mCurrentNum = (TextView) findViewById(R.id.custom_select_pic_preview_current);
        mTotalNum = (TextView) findViewById(R.id.custom_select_pic_preview_total);
        mBottomTotalCount = (TextView) findViewById(R.id.custom_select_pic_preview_count);
        mBottomComplete = (TextView) findViewById(R.id.custom_select_pic_preview_complete);
        mViewPager = (HackyViewPager) findViewById(R.id.custom_select_pic_preview_viewpager);

        findViewById(R.id.custom_select_pic_preview_left_layout, this);
        findViewById(R.id.custom_select_pic_preview_check, this);
        findViewById(R.id.custom_select_pic_preview_bottom_layout, this);

    }

    private void initData() {
        mAllPagerViews = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            mMiniPhotoItems = (ArrayList<MiniPhotoItem>) intent.getSerializableExtra("MiniPhotoItems");
            mCurrentPager = intent.getIntExtra("position", 0);
            bSinglePic = intent.getBooleanExtra("singlePic", false);
            imageLimitCount = intent.getIntExtra(CEConst.EXTRA_IMAGE_LIMIT_COUNT, 0);
            this.bSinglePicSelect = mMiniPhotoItems.get(previewPicPosition).isSelected;
        }

        if (bSinglePic) {
            mCenterLayout.setVisibility(View.INVISIBLE);
            mSelectMiniPhotoItems = (ArrayList<MiniPhotoItem>) intent.getSerializableExtra("selectMiniPhotoItems");
            mPhotoView = new PhotoView(this);
            mAllPagerViews.add(mPhotoView);
            this.selectPicCount = mSelectMiniPhotoItems.size();
        } else {
            mCenterLayout.setVisibility(View.VISIBLE);
            // 处理多张图片
            for (int i = 0; i < mMiniPhotoItems.size(); i++) {
                mPhotoView = new PhotoView(this);
                mAllPagerViews.add(mPhotoView);
            }
            this.selectPicCount = mMiniPhotoItems.size();
        }
        updateBottomData(true);

        mAdapter = new MiniPreviewViewPagerAdapter(mAllPagerViews, mMiniPhotoItems);
        mAdapter.setContext(MiniCustomSelectPicPreviewActivity.this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPager);
        mTotalNum.setText(mMiniPhotoItems.size() + "");
        mCurrentNum.setText((mCurrentPager + 1) + "");
        mViewPager.setOnPageChangeListener(this);
    }

    // 左侧返回关闭
    @Action(R.id.custom_select_pic_preview_left_layout)
    public void actionLeft() {
        Intent intent = getIntent();
        if (bSinglePic) {
            intent.putExtra("singlePic", mMiniPhotoItems.get(0).isSelected);
            setResult(CEConst.SINGLE_PIC, intent);
        } else {
            intent.putExtra("MiniPhotoItems", mMiniPhotoItems);
            setResult(CEConst.MORE_SINGLE_PIC, intent);
        }
        finish();
        overridePendingTransition(R.anim.class_push_bottom_in, R.anim.class_push_bottom_out);
    }

    // 顶部右侧图片选中状态
    @Action(R.id.custom_select_pic_preview_check)
    public void actionSelectImage() {
        if (bSinglePic) {
            // 单张图片
            if (mMiniPhotoItems.get(0).isSelected) {
                selectPicCount--;
                mMiniPhotoItems.get(0).isSelected = false;
            } else {
                selectPicCount++;
                if (selectPicCount > imageLimitCount) {
                    selectPicCount--;
                    showLimitDialog(imageLimitCount);
                } else {
                    mMiniPhotoItems.get(0).isSelected = true;
                }
            }
            updateBottomData(false);
        } else {
            // 多张图片
            if (mMiniPhotoItems.get(previewPicPosition).isSelected) {
                selectPicCount--;
                mMiniPhotoItems.get(previewPicPosition).isSelected = false;
            } else {
                selectPicCount++;
                mMiniPhotoItems.get(previewPicPosition).isSelected = true;
            }
            updateBottomData(false);
        }
    }


    @Action(R.id.custom_select_pic_preview_bottom_layout)
    public void actionBottomLayout() {
        ArrayList<String> imagePics = null;
        if (bSinglePic) {
            // 单张图片
            if (bSinglePicSelect) {
                imagePics = new ArrayList<String>();
                for (int i = 0; i < mSelectMiniPhotoItems.size(); i++) {
                    if (mSelectMiniPhotoItems.get(i).position == mMiniPhotoItems.get(0).position) {
                        mSelectMiniPhotoItems.get(i).isSelected = (mMiniPhotoItems.get(0).isSelected);
                    }
                    if (mSelectMiniPhotoItems.get(i).isSelected)
                        imagePics.add(mSelectMiniPhotoItems.get(i).imagePath);

                }
            } else {
                imagePics = new ArrayList<String>();
                if (!mSelectMiniPhotoItems.isEmpty()) {
                    mSelectMiniPhotoItems.add(mMiniPhotoItems.get(0));
                    for (MiniPhotoItem MiniPhotoItem : mSelectMiniPhotoItems) {
                        if (MiniPhotoItem.isSelected)
                            imagePics.add(MiniPhotoItem.imagePath);
                    }
                } else {
                    imagePics.add(mMiniPhotoItems.get(0).imagePath);
                }
            }
        } else {
            // 多张图片
            if (selectPicCount > 0) {
                imagePics = new ArrayList<>();
                for (MiniPhotoItem MiniPhotoItem : mMiniPhotoItems) {
                    if (MiniPhotoItem.isSelected)
                        imagePics.add(MiniPhotoItem.imagePath);
                }
            }
        }
        Notification notification = new Notification();
        notification.setInfo(CEConst.CE_RECEIVE_IMAGES_KEY, imagePics);
        notification.setKey(CEConst.CE_SEND_IMAGES);
        NotificationCenter.defaultNotificationCenter().post(notification);
        finish();
    }

    /**
     * 显示底部更新的数量
     *
     * @param bInitTop 是否是初始化顶部
     */
    private void updateBottomData(boolean bInitTop) {
        if (bInitTop) {
            changeSelectImage();
            updateBottomText();
            if (bSinglePicSelect) {
                changeSelectImage();
            } else {
                mSelectImage.setBackgroundResource(R.drawable.custom_select_pic_detail_check_normal);
            }
        } else {
            if (bSinglePic) {
                updateBottomText();
            } else {
                if (selectPicCount > 0) {
                    changeSelectTextColor();
                    mBottomTotalCount.setVisibility(View.VISIBLE);
                        mBottomTotalCount.setBackgroundResource(R.drawable.custom_select_parent_pic_preview_count_bg);
                    mBottomTotalCount.setText(selectPicCount + "");
                } else {
                    mBottomComplete.setTextColor(getResources().getColor(R.color.common_pic_color));
                    mBottomTotalCount.setVisibility(View.INVISIBLE);
                }
            }
            if (mMiniPhotoItems.get(previewPicPosition).isSelected) {
                changeSelectImage();
            } else {
                mSelectImage.setBackgroundResource(R.drawable.custom_select_pic_detail_check_normal);
            }
        }
    }

    private void updateBottomText() {
        changeSelectTextColor();
        if (selectPicCount == 0) {
            mBottomTotalCount.setVisibility(View.INVISIBLE);
        } else {
            mBottomTotalCount.setVisibility(View.VISIBLE);
                mBottomTotalCount.setBackgroundResource(R.drawable.custom_select_parent_pic_preview_count_bg);
            mBottomTotalCount.setText(selectPicCount + "");
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        this.previewPicPosition = i;
        if (mMiniPhotoItems.get(i).isSelected) {
            changeSelectImage();
        } else {
            mSelectImage.setBackgroundResource(R.drawable.custom_select_pic_detail_check_normal);
        }
        mCurrentNum.setText((i + 1) + "");
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onBackPressed() {
        actionLeft();
        super.onBackPressed();
    }

    /**
     * 最多选择九张图片
     *
     * @param limitCount max9
     */
    private void showLimitDialog(int limitCount) {

        MiniCustomDialog.show(MiniCustomSelectPicPreviewActivity.this, getString(R.string.hint),
                getString(R.string.pic_more_select) + limitCount + "" + getString(R.string.pic_num),
                getString(R.string.I_know), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    // 判断老师版和家长版选中的图片背景
    private void changeSelectImage() {
            mSelectImage.setBackgroundResource(R.drawable.custom_select_parent_pic_detail_check_press);
    }

    // 判断老师版和家长版字体颜色
    private void changeSelectTextColor() {
            mBottomComplete.setTextColor(getResources().getColor(R.color.common_green));
    }
}
