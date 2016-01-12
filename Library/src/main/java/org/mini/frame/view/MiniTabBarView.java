package org.mini.frame.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.mini.R;

import java.util.ArrayList;
import java.util.List;

import static org.mini.frame.toolkit.MiniDisplayUtil.dip2px;

/**
 * Created by Wuquancheng on 15/4/5.
 */
public class MiniTabBarView extends LinearLayout implements View.OnClickListener {

    public interface MiniTabBarViewListener {
        void willSelectAtIndex(int index,MiniTabItemView item);
        void didSelectAtIndex(int index,MiniTabItemView item);
        void willDeSelectAtIndex(int index,MiniTabItemView item);
        void didDeSelectAtIndex(int index,MiniTabItemView item);
        void repeatSelectAtIndex(int index, MiniTabItemView item);
    }


    private TabHost tabHost;

    private LinearLayout tabBarLayout;
    private List<MiniTabItemView> tabItems = new ArrayList<MiniTabItemView>();
    private MiniTabBarViewListener listener;

    public MiniTabBarView(Context context, TabHost tabHost, int height) {
        super(context);
        this.tabHost = tabHost;
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(height)));
        this.initView();
    }

    public void setListener(MiniTabBarViewListener listener) {
        this.listener = listener;
    }

    private void initView() {
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundResource(R.color.mini_tab_background_color);
        tabBarLayout = new LinearLayout(this.getContext());
        tabBarLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,this.getLayoutParams().height - dip2px(0.3f)));
        tabBarLayout.setOrientation(LinearLayout.HORIZONTAL);
        super.addView(tabBarLayout);
    }

    public void addView(View view) {
        tabBarLayout.addView(view);
        if (view instanceof MiniTabItemView) {
            MiniTabItemView item = (MiniTabItemView) view;
            tabItems.add(item);
            String tag = item.getTabId();
            Class clazz = item.getActivityClazz();
            if (clazz != null) {
                tabHost.addTab(tabHost.newTabSpec(tag).setIndicator(tag).setContent(new Intent(this.getContext(), item.getActivityClazz())));
            }
            view.setOnClickListener(this);
        }
        tabBarLayout.setWeightSum(tabItems.size());
    }

    public void onClick(View v) {
        if (v instanceof MiniTabItemView) {
            MiniTabItemView item = (MiniTabItemView) v;
            Class clazz = item.getActivityClazz();
            if (clazz == null) {
                int index = tabItems.indexOf(v);
                if (listener != null) {
                    listener.willSelectAtIndex(index, item);
                }
            }
            else {
                int index = 0;
                MiniTabItemView currentItem = null;
                for (MiniTabItemView itemView : tabItems) {
                    if (itemView.isSelected()) {
                        currentItem = itemView;
                        break;
                    }
                }
                if (item.equals(currentItem)) {
                    if (listener != null) {
                        listener.repeatSelectAtIndex(index, item);
                    }
                    return;
                }
                if (clazz != null) {
                    if (currentItem != null) {
                        if (listener != null) {
                            listener.willDeSelectAtIndex(index, currentItem);
                        }
                        currentItem.setSelected(false);
                        if (listener != null) {
                            listener.didDeSelectAtIndex(index, currentItem);
                        }
                    }
                    if (!item.isSelected()) {
                        if (listener != null) {
                            listener.willSelectAtIndex(index, item);
                        }
                        item.setSelected(true);
                        tabHost.setCurrentTabByTag(item.getTabId());
                        if (listener != null) {
                            listener.didSelectAtIndex(index, item);
                        }
                    }
                    else {
                        if (listener != null) {
                            listener.repeatSelectAtIndex(index, item);
                        }
                    }
                }
            }
        }
    }

    public void setSelectedIndex(Integer index) {
        if (index >= 0 && index < tabItems.size()) {
            MiniTabItemView item = tabItems.get(index);
            this.onClick(item);
        }
    }
}
