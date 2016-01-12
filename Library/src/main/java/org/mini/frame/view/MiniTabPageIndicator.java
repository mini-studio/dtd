package org.mini.frame.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mini.R;
import com.viewpagerindicator.TabPageIndicator;

import org.mini.frame.toolkit.MiniDisplayUtil;

/**
 * Created by Wuquancheng on 15/5/3.
 */
public class MiniTabPageIndicator extends TabPageIndicator {

  private int tabWidth = 0;

  public MiniTabPageIndicator(Context context) {
    super(context);
  }

  public MiniTabPageIndicator(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  protected TabPageIndicator.TabView createTabView() {
    return new MiniTabView(this.getContext());
  }

  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int childCount = this.tabCount();
    if (childCount == 1) {
      tabWidth = MiniDisplayUtil.getWindowWidth();
    } else if (childCount == 2) {
      tabWidth = MiniDisplayUtil.getWindowWidth() / 2;
    } else {
      tabWidth = MiniDisplayUtil.getWindowWidth() / 3;
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

  }

  class MiniTabView extends TabView {

    private ImageView badgeImageView;

    public MiniTabView(Context context) {
      super(context);
      badgeImageView = new ImageView(this.getContext());
      badgeImageView.setBackgroundResource(R.drawable.badge_bg);
      badgeImageView.setVisibility(View.GONE);
      textView.setId(R.id.text_view);
      textView.setLines(1);
        this.setBackgroundResource(R.drawable.tab_indicator);
        ColorStateList colorStateList=getResources().getColorStateList(R.color.tab_indicator_text_parents_selector);
        textView.setTextColor(colorStateList);
      int size = MiniDisplayUtil.dip2px(10);
      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
      params.addRule(RIGHT_OF, R.id.text_view);
      params.topMargin = size;
      this.addView(badgeImageView, params);
    }

    public void setBadge() {
      badgeImageView.setVisibility(View.VISIBLE);
    }

    public void removeBadge() {
      badgeImageView.setVisibility(View.GONE);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(tabWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
    }
  }

  public void setBadgeAtIndex(int index) {
    MiniTabView tabView = (MiniTabView) this.tabViewAtIndex(index);
    if (tabView != null) {
      tabView.setBadge();
    }
  }

  public void removeBadgeAtIndex(int index) {
    MiniTabView tabView = (MiniTabView) this.tabViewAtIndex(index);
    if (tabView != null) {
      tabView.removeBadge();
    }
  }


  private OnScrollChangedListener onScrollChangedListener;

  public interface OnScrollChangedListener {
    /**
     * 1就是起始位置 2.则是最后位置 3在中间位置
     *
     * @param position
     */
    void onScrollPosition(int position);
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    if (l == 0) {
      if (onScrollChangedListener != null)
        onScrollChangedListener.onScrollPosition(1);
    } else if (getScrollX() + getWidth() >= computeHorizontalScrollRange()) {
      if (onScrollChangedListener != null)
        onScrollChangedListener.onScrollPosition(2);
    }else{
      if (onScrollChangedListener != null)
        onScrollChangedListener.onScrollPosition(3);
    }
    super.onScrollChanged(l, t, oldl, oldt);
  }

  public void setOnScrollChangedListener(
          OnScrollChangedListener onScrollChangedListener) {
    this.onScrollChangedListener = onScrollChangedListener;
  }

}
