package com.mini.activity.send;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.engine.CEApi;

import org.mini.frame.annotation.Action;

/**
 * Created by Wuquancheng on 15/10/25.
 * 订单完成的主界面
 */
public class ExOrderFinishDetailActivity extends MNActivityBase {

    private PullToRefreshListView listView;
    private Button backButton;

    private PackageInfo  packageInfo;
    private ExOrderFinishDetailActivity.PackageInfoDataSourceAdapter packageInfoDataSourceAdapter;

    private CEApi api = new CEApi();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_orderfinish_detail);
        this.setTitle("完成订单");
        this.initView();
        this.loadData();
    }

    private void initView() {
        this.packageInfo = (PackageInfo)this.getIntentObject();
        listView = (PullToRefreshListView)findViewById(R.id.content_list_view);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        View header = getLayoutInflater().inflate(R.layout.activity_dispatch_header, null);
        listView.getRefreshableView().addHeaderView(header);
        packageInfoDataSourceAdapter = new PackageInfoDataSourceAdapter();
        listView.setAdapter(packageInfoDataSourceAdapter);
        this.backButton = (Button)findViewById(R.id.button_back,this);
    }

    @Action(R.id.button_back)
    public void onBackButtonTap() {
        super.back();
    }

    private void loadData() {
        this.packageInfoDataSourceAdapter.notifyDataSetChanged();
    }


    /**
     *   附近快件数据适配器
     */
    public class PackageInfoDataSourceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (packageInfo != null) {
                return 1;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return packageInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.activity_orderfinish_item, null);
                PackageInfoViewWrapper packageInfoViewWrapper = new PackageInfoViewWrapper();
                packageInfoViewWrapper.textView = (TextView)convertView.findViewById(R.id.packageInfo);
                convertView.setTag(packageInfoViewWrapper);
            }
            PackageInfoViewWrapper packageInfoViewWrapper = (PackageInfoViewWrapper)convertView.getTag();
            fillContent(position, packageInfoViewWrapper);
            return convertView;
        }

        private void fillContent(int position, PackageInfoViewWrapper viewWrapper) {
            PackageInfo packageInfo = (PackageInfo)getItem(position);
            if (packageInfo != null) {
                String content = String.format("%d、始发地：%s 发往：%s", position+1,packageInfo.getSource_city(), packageInfo.getDestination_city());
                viewWrapper.textView.setText(content);
            }
            else {
                viewWrapper.textView.setText("");
            }
        }

        private class PackageInfoViewWrapper {
            TextView textView;
        }
    }

}
