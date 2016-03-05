package com.dtdinc.dtd.activity.comm;

import android.app.KeyguardManager;
import android.os.Handler;

import com.dtdinc.dtd.core.define.CEConst;
import org.mini.frame.toolkit.MiniActivityManager;

/**
 * Created by Wuquancheng on 15/5/5.
 */
public class MNPushRouteActivity extends MNActivityBase {

    //收到PUSH当时app的运行状态
    MiniActivityManager.APP_STATUS appStatus;

    private Handler handler = new Handler();

    private boolean keyguardFlag;    //锁屏状态 如果flag为true，表示有两种状态：a、屏幕是黑的 b、目前正处于解锁状态 。如果flag为false，表示目前未锁屏

    protected void registerNotification() {
    }

    public void loadView() {
        appStatus = (MiniActivityManager.APP_STATUS) getIntent().getSerializableExtra(CEConst.APP_STATUS);
        //锁屏状态
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(this.KEYGUARD_SERVICE);
        keyguardFlag = keyguardManager.inKeyguardRestrictedInputMode();
        final String message = getIntent().getStringExtra(CEConst.APP_REMOTE_MESSAGE);
    }
}
