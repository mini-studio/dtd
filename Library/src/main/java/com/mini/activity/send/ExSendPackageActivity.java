package com.mini.activity.send;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.data.PackageInfoWrapper;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我要发件的主界面
 */
public class ExSendPackageActivity extends MNActivityBase {

    private Button nextButton;
    private CEApi api = new CEApi();


    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_sendpackage);
        this.setTitle("我要发件");
        this.initView();
    }

    private void initView() {
        nextButton = (Button)this.findViewById(R.id.next_button, this);
    }

    @Action(R.id.next_button)
    public void onNextButtonTap() {
        startActivity(ExSendPackageInfoActivity.class);
    }

}
