package com.mini.activity.mess;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class ExMessActivity extends MNActivityBase {
    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_mess);
        this.setTitle("消息");
        this.hiddenLeftBackButton();
    }
}
