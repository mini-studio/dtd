package com.dtdinc.dtd.activity.profile;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.activity.deliver.DTDFeedbackActivity;

import org.mini.frame.annotation.Action;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDSettingActivity extends MNActivityBase {
    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_setting);
        this.setTitle("系统设置");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.layout_about, this);
        this.findViewById(R.id.layout_feedback, this);
    }

    @Action(R.id.layout_about)
    public void clearCache() {
        startActivity(DTDAboutActivity.class);
    }

    @Action(R.id.layout_feedback)
    public void feedback() {
        Map map = new HashMap();
        map.put("title", "用户反馈");
        map.put("hint", "您的建议是我们不断前进的动力");
        startActivity(DTDFeedbackActivity.class,map);
    }

}
