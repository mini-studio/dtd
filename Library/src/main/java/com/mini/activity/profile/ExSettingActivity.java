package com.mini.activity.profile;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.activity.send.ExFeedbackActivity;

import org.mini.frame.annotation.Action;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class ExSettingActivity extends MNActivityBase {
    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_setting);
        this.setTitle("系统设置");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.layout_clear_cache, this);
        this.findViewById(R.id.layout_feedback, this);
    }

    @Action(R.id.layout_clear_cache)
    public void clearCache() {

    }

    @Action(R.id.layout_feedback)
    public void feedback() {
        Map map = new HashMap();
        map.put("title", "用户反馈");
        map.put("hint", "您的建议是我们不断前进的动力");
        startActivity(ExFeedbackActivity.class,map);
    }

}
