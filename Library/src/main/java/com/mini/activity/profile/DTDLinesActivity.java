package com.mini.activity.profile;

import android.content.Intent;
import android.widget.TextView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.data.User;
import com.mini.core.model.LineInfo;

import org.mini.frame.activity.base.MiniIntent;
import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDLinesActivity extends MNActivityBase {
    private TextView line_a_view;
    private TextView line_b_view;
    private TextView line_c_view;

    private String linea;
    private String lineb;
    private String linec;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_lines);
        this.setTitle("常用线路");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.line_a_layout, this);
        this.findViewById(R.id.line_b_layout, this);
        this.findViewById(R.id.line_c_layout, this);
        this.line_a_view = (TextView)this.findViewById(R.id.line_a_view);
        this.line_b_view = (TextView)this.findViewById(R.id.line_b_view);
        this.line_c_view = (TextView)this.findViewById(R.id.line_c_view);
    }

    public void afterResume() {
        loadData();
    }

    private void loadData() {
        User user = WHO();
        if (user.getLinea() != null) {
            this.line_a_view.setText(user.getLinea());
        }
        if (user.getLineb() != null) {
            this.line_b_view.setText(user.getLineb());
        }
        if (user.getLinec() != null) {
            this.line_c_view.setText(user.getLinec());
        }
    }

    /**
     * 设置C线路
     */
    @Action(R.id.line_c_layout)
    public void actionForUploadIdCard() {
        LineInfo lineInfo = new LineInfo();
        lineInfo.setName("C线路");
        startActivityForResult(DTDLinesSetActivity.class, lineInfo, 300);
    }

    @ActivityResult(300)
    public void onSetLineC(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            User user = WHO();
            LineInfo lineInfo = (LineInfo)MiniIntent.getObjectFromIntent(intent);
            String line = lineInfo.toString();
            user.setLinec(line);
            this.line_c_view.setText(line);
        }
    }

    /**
     * 设置b线路
     */
    @Action(R.id.line_b_layout)
    public void actionForUploadBankCard() {
        LineInfo lineInfo = new LineInfo();
        lineInfo.setName("B线路");
        startActivityForResult(DTDLinesSetActivity.class, lineInfo, 200);
    }

    @ActivityResult(200)
    public void onSetLineB(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            User user = WHO();
            LineInfo lineInfo = (LineInfo)MiniIntent.getObjectFromIntent(intent);
            String line = lineInfo.toString();
            user.setLineb(line);
            this.line_b_view.setText(line);
        }
    }

    /**
     * 设置a线路
     */
    @Action(R.id.line_a_layout)
    public void actionForLineLayout() {
        LineInfo lineInfo = new LineInfo();
        lineInfo.setName("A线路");
        startActivityForResult(DTDLinesSetActivity.class, lineInfo, 100);
    }

    @ActivityResult(100)
    public void onSetLineA(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            User user = WHO();
            LineInfo lineInfo = (LineInfo)MiniIntent.getObjectFromIntent(intent);
            String line = lineInfo.toString();
            user.setLinea(line);
            this.line_a_view.setText(line);
        }
    }
}
