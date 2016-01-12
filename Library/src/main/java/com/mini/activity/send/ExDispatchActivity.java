package com.mini.activity.send;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.data.PackageInfoWrapper;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;

import org.mini.frame.http.request.MiniDataListener;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我要送件的主界面
 */
public class ExDispatchActivity extends MNActivityBase {

    private PullToRefreshListView listView;
    private PackageInfoWrapper  packageInfoWrapper;
    private ExDispatchActivity.PackageInfoDataSourceAdapter packageInfoDataSourceAdapter;

    private TextView locationTextView;

    private CEApi api = new CEApi();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_dispatch);
        this.setTitle("我要送件");
        this.initView();
        this.loadData();
    }

    private void initView() {
        this.locationTextView = (TextView)this.findViewById(R.id.location_dispatch_view);
        listView = (PullToRefreshListView)findViewById(R.id.content_list_view);
        View header = getLayoutInflater().inflate(R.layout.activity_dispatch_header, null);
        listView.getRefreshableView().addHeaderView(header);
        packageInfoDataSourceAdapter = new PackageInfoDataSourceAdapter();
        listView.setAdapter(packageInfoDataSourceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo packageInfo = (PackageInfo)view.getTag(R.integer.infoKey);
                onClickItem(packageInfo);
            }
        });
    }

    private void receiveData(PackageInfoWrapper data, CEDataException error) {
        if (error == null) {
            this.packageInfoWrapper = data;
            this.packageInfoDataSourceAdapter.notifyDataSetChanged();
        }
        else {
            showError(error);
        }
    }

    private void loadData() {
        this.locationTextView.setText("北京");
//        api.dispatchPackageInfos(null, new MiniDataListener<PackageInfoWrapper>() {
//            @Override
//            public void onResponse(PackageInfoWrapper data, CEDataException error) {
//                receiveData(data, error);
//            }
//        });
    }

    private void onClickItem(PackageInfo packageInfo) {
        //startActivityWithObject(ExDispatchDetailActivity.class, packageInfo);
        startActivityWithObject(ExOrderDetailActivity.class, packageInfo);
    }

    /**
     *   附近快件数据适配器
     */
    public class PackageInfoDataSourceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (packageInfoWrapper != null) {
                return packageInfoWrapper.count();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (packageInfoWrapper != null) {
                return packageInfoWrapper.getPackageInfo(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.activity_dispatch_item, null);
                PackageInfoViewWrapper packageInfoViewWrapper = new PackageInfoViewWrapper();
                packageInfoViewWrapper.textView = (TextView)convertView.findViewById(R.id.packageInfo);
                convertView.setTag(packageInfoViewWrapper);
            }
            PackageInfoViewWrapper packageInfoViewWrapper = (PackageInfoViewWrapper)convertView.getTag();
            convertView.setTag(R.integer.infoKey, getItem(position));
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
