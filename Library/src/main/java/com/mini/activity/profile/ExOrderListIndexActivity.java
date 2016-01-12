package com.mini.activity.profile;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.activity.send.ExPackageDetailActivity;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.data.PackageInfoWrapper;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我的快件
 */
public class ExOrderListIndexActivity extends MNActivityBase {

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_order_list_index);
        this.setTitle("我的快件");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.send_order_view, this);
        this.findViewById(R.id.receive_order_view, this);
    }

    @Action(R.id.send_order_view)
    public void actionSendOrder() {
        startActivity(ExOrderListActivity.class, 1);
    }

    @Action(R.id.receive_order_view)
    public void actionReceiveOrderView() {
        startActivity(ExOrderListActivity.class, 0);
    }

}
