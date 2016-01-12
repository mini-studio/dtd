package com.mini.activity.main;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.mini.R;
import com.mini.activity.mess.ExMessActivity;
import com.mini.activity.profile.ExProfileActivity;
import com.mini.activity.post.ExPostActivity;
import com.mini.activity.send.ExSendActivity;
import com.mini.core.api.data.City;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;
import com.mini.core.inter.CETabActivityInter;

import org.mini.frame.activity.base.MiniTabBarActivity;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.notification.NotificationCenter;
import org.mini.frame.view.MiniTabItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MNMainTabActivity extends MiniTabBarActivity {

    long waitTime = 2000;
    long touchTime = 0;
    private Map<String, MiniTabItemView> tabViewMap = new HashMap<String, MiniTabItemView>();
    public static MNMainTabActivity tabActivity = null;

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) == Intent.FLAG_ACTIVITY_CLEAR_TOP) {
            this.startActivity(new Intent(this.getApplicationContext(), MNSigninActivity.class));
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabActivity = this;
        registerNotification();
        init();
    }

    private void init() {

    }

    public void onDestroy() {
        NotificationCenter.defaultNotificationCenter().remove(this);
        super.onDestroy();
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        //清除通知栏
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        super.onPostResume();
    }

    protected List<MiniTabItemView> tabItems() {
        List<MiniTabItemView> list = new ArrayList<MiniTabItemView>(0);
        MiniTabItemView itemView = new MiniTabItemView(
                this, R.drawable.send_normal,
                R.drawable.send_selected,
                null, ExSendActivity.class);
        itemView.setTabId("1");
        list.add(itemView);
        itemView = new MiniTabItemView(
                this, R.drawable.dispatch_normal,
                R.drawable.dispatch_selected,
                null, ExPostActivity.class);
        itemView.setTabId("2");
        list.add(itemView);
        itemView = new MiniTabItemView(
                this, R.drawable.me_normal,
                R.drawable.me_selected,
                null, ExProfileActivity.class);
        itemView.setTabId("4");
        list.add(itemView);
        return list;
    }

    protected void registerNotification() {
    }


    protected void removeBadge(String tabId) {

    }

    protected void setBadgeWithTabId(String tabId) {
        MiniTabItemView view = tabViewMap.get(tabId);
        if (view != null) {
            view.setBadge();
        }
    }

    public void repeatSelectAtIndex(int index, MiniTabItemView item) {
        Activity activity = this.getLocalActivityManager().getCurrentActivity();
        if (activity instanceof CETabActivityInter) {
            ((CETabActivityInter) activity).onRepeatSelectTab();
        }
    }

    public void willSelectAtIndex(int index, MiniTabItemView item) {

    }

    public void willDeSelectAtIndex(int index, MiniTabItemView item) {
        Activity activity = this.getLocalActivityManager().getCurrentActivity();
        if (activity instanceof CETabActivityInter) {
            refreshTabItemViewBadge(item, (CETabActivityInter) activity);
        }
    }

    public void didSelectAtIndex(int index, MiniTabItemView item) {
        Activity activity = this.getLocalActivityManager().getCurrentActivity();
        if (activity instanceof CETabActivityInter) {
            ((CETabActivityInter) activity).onSelectedTab();
            if (item.getTabId().equals("299")) {
                removeBadge("299");
            }
        }
    }

    public void refreshTabItemViewBadge(Class clazz) {
        List<MiniTabItemView> itemViews = itemViewsWithClass(clazz);
        if (itemViews != null && itemViews.size() > 0) {
            for (MiniTabItemView view : itemViews) {
                Activity activity = this.getLocalActivityManager().getActivity(view.getTabId());
                if (activity != null && activity instanceof CETabActivityInter) {
                    refreshTabItemViewBadge(view, (CETabActivityInter) activity);
                }
            }
        }
    }

    public void refreshTabItemViewBadge(MiniTabItemView item,CETabActivityInter activity) {
        String tableId = item.getTabId();
        int count = activity.countOfUnReadItem();
        if (count > 0) {
            if (tableId != null) {
                setBadgeWithTabId(tableId);
            }
        } else {
            removeBadge(tableId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void finish() {

        super.finish();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
            }
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public void onAfterResume() {

    }

}
