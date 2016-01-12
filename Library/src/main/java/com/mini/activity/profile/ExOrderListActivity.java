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

import org.mini.frame.http.request.MiniDataListener;

import static com.mini.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我接收的快件和我要发送的快件
 */
public class ExOrderListActivity extends MNActivityBase {

    private PullToRefreshListView listView;
    private PackageInfoWrapper  packageInfoWrapper;
    private ExOrderListActivity.PackageInfoDataSourceAdapter packageInfoDataSourceAdapter;


    private CEApi api = new CEApi();
    private int page = 1;

    /**
     * 0 我接收的快件
     * 1 我发送的快件
     */
    private int orderType = 0;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_dispatch);
        this.initView();
        if (orderType == 0) {
            this.setTitle("我接收的订单");
        }
        else {
            this.setTitle("我发出的订单");
        }
        this.loadData(1);
    }

    private void initView() {
        this.orderType = (Integer)getIntentObject();
        listView = (PullToRefreshListView)findViewById(R.id.content_list_view);
        packageInfoDataSourceAdapter = new PackageInfoDataSourceAdapter();
        listView.setAdapter(packageInfoDataSourceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo packageInfo = (PackageInfo) view.getTag(R.integer.infoKey);
                if (packageInfo != null) {
                    onClickItem(packageInfo);
                } else {
                    loadData(page + 1);
                }
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                loadData(1);
            }
        });
    }

    private void receiveData(int page, PackageInfoWrapper data, CEDataException error) {
        this.listView.onRefreshComplete();
        if (error == null) {
            this.page = page;
            if (page < 2) {
                this.packageInfoWrapper = data;
            }
            else {
                this.packageInfoWrapper.append(data);
            }
            this.packageInfoDataSourceAdapter.notifyDataSetChanged();
            if (data.getOrder() == null || data.getOrder().size() == 0) {
                showMessage("没有订单");
            }
        }
        else {
            showError(error);
        }
    }

    private void loadData(final int page) {
        if (!this.listView.isRefreshing()) {
            showWaiting();
        }
        if (this.orderType == 0) {
            api.myOrder(WHO().getPhone(), new MiniDataListener<PackageInfoWrapper>() {
                @Override
                public void onResponse(PackageInfoWrapper data, CEDataException error) {
                    dismissWaiting();
                    receiveData(page, data, error);
                }
            });
        }
        else {
            api.myPublishOrder(WHO().getPhone(), new MiniDataListener<PackageInfoWrapper>() {
                @Override
                public void onResponse(PackageInfoWrapper data, CEDataException error) {
                    dismissWaiting();
                    receiveData(page, data, error);
                }
            });
        }
    }

    private void onClickItem(PackageInfo packageInfo) {
        packageInfo.setOrderType(orderType);
        startActivityWithObject(ExPackageDetailActivity.class, packageInfo);
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
                PackageInfoViewWrapper packageInfoViewWrapper = new PackageInfoViewWrapper(convertView);
                convertView.setTag(packageInfoViewWrapper);
            }
            PackageInfoViewWrapper packageInfoViewWrapper = (PackageInfoViewWrapper)convertView.getTag();
            convertView.setTag(R.integer.infoKey, getItem(position));
            fillContent(position, packageInfoViewWrapper);
            return convertView;
        }

        private void fillContent(int position, PackageInfoViewWrapper viewWrapper) {
            PackageInfo packageInfo = (PackageInfo)getItem(position);
            viewWrapper.top_line.setVisibility(position == 0?View.VISIBLE:View.GONE);
            if (packageInfo != null) {
                viewWrapper.package_info_layout.setVisibility(View.VISIBLE);
                viewWrapper.more_data_text.setVisibility(View.GONE);
                viewWrapper.name_value_text_view.setText(packageInfo.getSource_user());
                if (orderType == 0) {
                    viewWrapper.income_value_text_view.setText(packageInfo.getIncoming());
                }
                else {
                    viewWrapper.income_value_text_view.setText(packageInfo.getPrice());
                }
                viewWrapper.status_value_text_view.setText(packageInfo.getStatusDesc(orderType));
                viewWrapper.source_time_value_text_view.setText(packageInfo.getSource_time());
                viewWrapper.source_address_value_text_view.setText(packageInfo.getSource_address());
                viewWrapper.dest_time_value_text_view.setText(packageInfo.getArrive_time());
                viewWrapper.dest_address_value_text_view.setText(packageInfo.getDestination_address());
            }
            else {
                viewWrapper.package_info_layout.setVisibility(View.GONE);
                viewWrapper.more_data_text.setVisibility(View.VISIBLE);
            }
        }

        private class PackageInfoViewWrapper {
            View package_info_layout;
            TextView name_value_text_view;
            TextView income_value_text_view;
            TextView status_value_text_view;
            TextView source_time_value_text_view;
            TextView source_address_value_text_view;
            TextView dest_time_value_text_view;
            TextView dest_address_value_text_view;
            TextView income_title;

            TextView more_data_text;

            View  top_line;
            View  bottom_line;

            public PackageInfoViewWrapper(View view) {
                this.package_info_layout = view.findViewById(R.id.package_info_layout);
                this.name_value_text_view = (TextView)view.findViewById(R.id.name_value_text_view);
                this.income_value_text_view = (TextView)view.findViewById(R.id.income_value_text_view);
                this.status_value_text_view = (TextView)view.findViewById(R.id.status_value_text_view);
                this.source_time_value_text_view = (TextView)view.findViewById(R.id.source_time_value_text_view);
                this.source_address_value_text_view = (TextView)view.findViewById(R.id.source_address_value_text_view);
                this.dest_time_value_text_view = (TextView)view.findViewById(R.id.dest_time_value_text_view);
                this.dest_address_value_text_view = (TextView)view.findViewById(R.id.dest_address_value_text_view);
                this.more_data_text = (TextView)view.findViewById(R.id.more_data_text);
                this.income_title = (TextView)view.findViewById(R.id.income_title);
                if (orderType == 0) {
                    this.income_title.setText("收入");
                }
                else {
                    this.income_title.setText("费用");
                }
                this.top_line = view.findViewById(R.id.top_line);
                this.bottom_line = view.findViewById(R.id.bottom_line);
            }
        }
    }

}
