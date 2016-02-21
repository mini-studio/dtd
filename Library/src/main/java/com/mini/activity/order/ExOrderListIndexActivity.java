package com.mini.activity.order;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;

import org.mini.frame.annotation.Action;

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
