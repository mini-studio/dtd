package org.mini.frame.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.R;

import org.mini.frame.toolkit.MiniDisplayUtil;

import static org.mini.frame.toolkit.MiniDisplayUtil.dip2px;

/**
 * Created by Wuquancheng on 15/4/5.
 */
public class MiniTabItemView extends LinearLayout {

    private ImageView imageView;
    private TextView textView;
    private int normalImageId;
    private int titleId;
    private String title;
    private String tabId;
    private int selectedImageId;

    private ImageView badgeImageView;

    private LinearLayout.LayoutParams badgeViewParams;

    private Class activityClazz;

    public MiniTabItemView(Context context, int normalImageId, int selectedImageId, int titleId, Class activityClazz) {
        super(context);
        this.normalImageId = normalImageId;
        this.selectedImageId = selectedImageId;
        this.activityClazz = activityClazz;
        this.titleId = titleId;
        this.initView();
    }


    public MiniTabItemView(Context context, int normalImageId, int selectedImageId, String title, Class activityClazz) {
        super(context);
        this.normalImageId = normalImageId;
        this.selectedImageId = selectedImageId;
        this.activityClazz = activityClazz;
        this.title = title;
        this.initView();
    }


    public void setTitle(String title) {
        this.title = title;
        if (textView != null) {
            textView.setText(title);
        }
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabId() {
        return this.tabId;
    }

    private void initView() {
        this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
        this.setOrientation(LinearLayout.VERTICAL);
        imageView = new ImageView(this.getContext());
        LayoutParams imageLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageLayoutParams.topMargin = dip2px(4);
        imageLayoutParams.height = dip2px(30);
        imageLayoutParams.width = dip2px(30);
        imageLayoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageLayoutParams);
        imageView.setBackgroundResource(normalImageId);

        LayoutParams titleLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        titleLayoutParams.topMargin = -dip2px(4);
        textView = new TextView(this.getContext());
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(titleLayoutParams);
        if (title != null && title.length() > 0) {
            textView.setText(title);
        } else if (titleId > 0){
            textView.setText(titleId);
        }
        else {
            textView.setVisibility(View.GONE);
            imageLayoutParams.height = dip2px(42);
            imageLayoutParams.width = dip2px(42);
        }
        textView.setTextColor(this.getResources().getColor(R.color.mini_tab_text_default));
        this.addView(imageView);
        this.addView(textView);

        badgeImageView = new ImageView(this.getContext());
        badgeImageView.setVisibility(View.GONE);
        badgeImageView.setImageResource(R.drawable.badge_bg);
        int size = MiniDisplayUtil.dip2px(10);
        badgeViewParams = new LinearLayout.LayoutParams(size, size);
        badgeViewParams.topMargin = -4 * size;
        badgeViewParams.leftMargin = size;
        this.addView(badgeImageView, badgeViewParams);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        badgeViewParams.leftMargin = ((r - l) / 2 + dip2px(20));
    }

    public Class getActivityClazz() {
        return activityClazz;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            if (!this.isSelected()) {
                super.setSelected(selected);
                imageView.setBackgroundResource(selectedImageId);
                textView.setTextColor(this.getResources().getColor(R.color.mini_tab_text_selected));
            }
        } else {
            if (this.isSelected()) {
                super.setSelected(selected);
                imageView.setBackgroundResource(normalImageId);
                textView.setTextColor(this.getResources().getColor(R.color.mini_tab_text_default));
            }
        }
    }

    public void setBadge() {
        badgeImageView.setVisibility(View.VISIBLE);
    }

    public void removeBadge() {
        badgeImageView.setVisibility(View.GONE);
    }

}
