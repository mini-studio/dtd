package org.mini.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mini.R;

/**
 * Created by gassion on 15/6/18.
 */
public class MiniLoadingRelativeLayout extends RelativeLayout implements View.OnClickListener {


  private ImageView ivShow;
  private OnclickTypeListener listener;
  private RelativeLayout contentLayout;
  private SelectType selectType;
  private PullToRefreshListView pullToRefreshListView;
  private LinearLayout linearLayout;

  public interface OnclickTypeListener {
    void onClick();
  }

  public enum SelectType {
    /**
     * 没有数据
     */
    type_no_data,
    /**
     * 重新加载
     */
    type_reload,
    /**
     * 正常数据
     */
    type_data,
    /**
     * 私信列表无私信内容
     */
    type_chat_no_data
  }

  public MiniLoadingRelativeLayout(Context context) {
    super(context);
    this.initView();
  }

  public MiniLoadingRelativeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.initView();
  }

  private void initView() {
    LayoutParams layoutParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    contentLayout = new RelativeLayout(this.getContext());
    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
    ivShow = new ImageView(getContext());
    contentLayout.addView(ivShow, layoutParams1);
    LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    this.addView(contentLayout, layoutParams);
  }

  public void setSelectType(SelectType selectType) {
    this.selectType = selectType;
    if (selectType == SelectType.type_no_data) {
      contentLayout.setVisibility(View.VISIBLE);
        ivShow.setBackgroundResource(R.drawable.common_page_no_data);
    } else if (selectType == SelectType.type_reload) {
      contentLayout.setVisibility(View.VISIBLE);
        ivShow.setBackgroundResource(R.drawable.common_page_error_icom);
      for (int i = 0; i < getChildCount(); i++) {
        View childAt = getChildAt(i);
        if (childAt instanceof PullToRefreshListView) {
          pullToRefreshListView = (PullToRefreshListView) childAt;
          pullToRefreshListView.setVisibility(View.GONE);
        }
        if (childAt instanceof LinearLayout) {
          linearLayout = (LinearLayout) childAt;
          linearLayout.setVisibility(View.GONE);
        }
      }
      contentLayout.setOnClickListener(this);
    } else if (selectType == SelectType.type_data){
      if (pullToRefreshListView != null)
        pullToRefreshListView.setVisibility(View.VISIBLE);
      if (linearLayout != null)
        linearLayout.setVisibility(View.VISIBLE);
      contentLayout.setVisibility(View.GONE);
    }else if (selectType == SelectType.type_chat_no_data){
      contentLayout.setVisibility(View.VISIBLE);
      ivShow.setBackgroundResource(R.drawable.common_chat_page_no_data);
    }
  }

  public void setOnclick(OnclickTypeListener listener) {
    this.listener = listener;

  }

  @Override
  public void onClick(View v) {
    // 只有服务器返回错误的时候生效
    if (listener != null && selectType == SelectType.type_reload) {
      listener.onClick();
    }
  }


}
