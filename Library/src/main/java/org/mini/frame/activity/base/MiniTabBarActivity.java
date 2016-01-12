package org.mini.frame.activity.base;

import android.app.ActivityGroup;
import android.app.Application;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;

import org.mini.frame.application.MiniApplication;
import org.mini.frame.notification.NotificationCenter;
import org.mini.frame.toolkit.MiniActivityManager;
import org.mini.frame.view.MiniTabBarView;
import org.mini.frame.view.MiniTabItemView;

import com.mini.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/4/5.
 */
public class MiniTabBarActivity extends ActivityGroup implements MiniTabBarView.MiniTabBarViewListener {

    private TabHost tabHost;

    protected List<MiniTabItemView> itemViews;

    protected Integer defaultSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_main_tab);
        initViews();
    }

    private void initViews() {
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(this.getLocalActivityManager());
        LinearLayout tabBar = (LinearLayout) findViewById(R.id.tabbar);

        MiniTabBarView tabBarView = new MiniTabBarView(this, tabHost, tabBarHeight());
        tabBar.addView(tabBarView);
        tabBarView.setListener(this);

        itemViews = tabItems();
        if (itemViews != null && itemViews.size() > 0) {
            for (MiniTabItemView itemView : itemViews) {
                tabBarView.addView(itemView);
            }
        }

        tabBarView.setSelectedIndex(defaultSelectedIndex);
    }

    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultNotificationCenter().remove(this);
    }

    protected int tabBarHeight() {
        return 50;
    }

    protected List<MiniTabItemView> tabItems() {
        return null;
    }

    public void willSelectAtIndex(int index, MiniTabItemView item) {

    }

    public void didSelectAtIndex(int index, MiniTabItemView item) {

    }

    public void willDeSelectAtIndex(int index, MiniTabItemView item) {

    }

    public void didDeSelectAtIndex(int index, MiniTabItemView item) {

    }

    public void repeatSelectAtIndex(int index, MiniTabItemView item) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        onAfterResume();
        Application application = getApplication();
        if (application instanceof MiniApplication) {
            ((MiniApplication) application).onResume();
        }

    }

    public void onAfterResume() {
        MiniActivityManager.currentActivity = this;
    }

    @Override
    protected void onStop() {
        Application application = getApplication();
        if (application instanceof MiniApplication) {
            ((MiniApplication) application).onStop();
        }
        super.onStop();
    }

    public List<MiniTabItemView> itemViewsWithClass(Class senderClass) {
        List<MiniTabItemView> list = new ArrayList<MiniTabItemView>();
        if (itemViews != null) {
            String fullName = senderClass.getName();
            for (MiniTabItemView itemView : itemViews) {
                Class c = itemView.getActivityClazz();
                if (c != null) {
                    if (fullName.equals(c.getName())) {
                        list.add(itemView);
                    }
                }
            }
        }
        return list;
    }
}
