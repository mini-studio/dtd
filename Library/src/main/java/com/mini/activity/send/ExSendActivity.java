package com.mini.activity.send;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.activity.comm.MNCityPickerActivity;
import com.mini.app.CESystem;
import com.mini.core.api.data.City;
import com.mini.core.api.data.ImageInfo;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.data.PackageInfoWrapper;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;
import com.mini.core.kit.LocationKit;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.mini.frame.activity.base.MiniIntent;
import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.toolkit.location.MiniLocationListener;
import org.mini.frame.toolkit.location.MiniLocationManager;
import org.mini.frame.view.MiniBannerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/10/25.
 * 送件的主界面
 */
public class ExSendActivity extends MNActivityBase {

    private static final int REQ_CODE_FOR_CITY = 200;
    private MiniBannerViewPager bannerViewPager;
    private ExSendBannerAdapter bannerAdapter;
    private PullToRefreshListView listView;
    private PackageInfoDataSourceAdapter packageInfoDataSourceAdapter;
    private PackageInfoWrapper  packageInfoWrapper;

    private City city = null;

    private CEApi api = new CEApi();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_send);
        this.setTitleMidImage(R.drawable.asf_03);
        this.initView();
        setNaviLeftTitle("定位");
        this.setLeftTitleImage(R.drawable.location);
        this.loadData(1);
    }

    private void initView() {
        listView = (PullToRefreshListView)findViewById(R.id.listView);
        View header = getLayoutInflater().inflate(R.layout.activity_send_header, null);
        View viewBannerLayout = header.findViewById(R.id.viewBannerLayout);
        bannerViewPager = (MiniBannerViewPager)viewBannerLayout.findViewById(R.id.bannerViewPager);
        bannerViewPager.setRootView(viewBannerLayout);
        bannerAdapter = new ExSendBannerAdapter();
        bannerViewPager.setPagerAdapter(bannerAdapter);
        listView.getRefreshableView().addHeaderView(header);
        packageInfoDataSourceAdapter = new PackageInfoDataSourceAdapter();
        listView.setAdapter(packageInfoDataSourceAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo packageInfo = (PackageInfo) view.getTag(R.integer.infoKey);
                onSelectPackage(packageInfo);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                loadData(0);
            }
        });

    }

    private void onSelectPackage(PackageInfo packageInfo) {
        if (packageInfo != null) {
            packageInfo.setFrom(0);
            startActivityWithObject(ExPackageDetailActivity.class, packageInfo);
        }
        else {
            //加载更多
            //this.loadData(2);
        }
    }

    protected void onNaviLeftButtonAction() {
        startActivityForResult(MNCityPickerActivity.class, null, REQ_CODE_FOR_CITY);
    }

    @ActivityResult(REQ_CODE_FOR_CITY)
    public void onPickerCity(int requestCode, int resultCode, Intent data) {
        Object object = MiniIntent.getObjectFromIntent(data);
        if (object != null && object instanceof City) {
            this.setCity((City)object);
        }
    }

    private void setCity(City city) {
        this.city = city;
        this.resetNaviLeftTitle(city.getName());
        this.loadData(1);
    }

    private void receivePackageInfoWrapper(int page, PackageInfoWrapper data, CEDataException error) {
        listView.onRefreshComplete();
        if (error == null) {
            if (this.packageInfoWrapper == null || page <= 1) {
                packageInfoWrapper = data;
            }
            else {
                packageInfoWrapper.append(data);
            }
            if (data.getOrder() == null || data.getOrder().size() == 0) {
                showMessage("附近没有订单");
            }
            packageInfoDataSourceAdapter.notifyDataSetChanged();
            bannerAdapter.reset();
            bannerViewPager.setPagerAdapter(bannerAdapter);
        }
        else {
            showError(error);
        }
    }

    public void loadData(final int page) {
        if (city == null || city.isInvalid()) {
            showWaiting("正在定位当前城市");
            new LocationKit().locate(new LocationKit.LocationListener() {
                @Override
                public void locateCity(City city, String reason) {
                    dismissWaiting();
                    if (city != null) {
                        ExSendActivity.this.setCity(city);
                    }
                    else {
                        showMessage("定位失败，请确认打开了定位服务");
                    }
                }
            });
        }
        else {
            doLoadData(page);
        }
    }

    public void doLoadData(final int page) {
        if (!listView.isRefreshing()) {
            showWaiting();
        }
        api.nearbyPackageInfos(city, new MiniDataListener<PackageInfoWrapper>() {
            @Override
            public void onResponse(PackageInfoWrapper data, CEDataException error) {
                dismissWaiting();
                receivePackageInfoWrapper(page, data, error);
            }
        });
    }

    /**
     * Banner图数据适配器
     */
    private class ExSendBannerAdapter implements MiniBannerViewPager.MiniBannerViewPagerAdapter {

        List<ImageInfo> list = new ArrayList<ImageInfo>();

        public void reset() {
            list.clear();
            if (packageInfoWrapper != null) {
                List<ImageInfo> infos = packageInfoWrapper.getIndex_photo();
                if (infos != null) {
                    list.addAll(infos);
                }
            }
        }

        @Override
        public int bannerImageCount() {
            return list.size();
        }

        @Override
        public String bannerImageUrlAtIndex(int index) {
            ImageInfo imageInfo = list.get(index);
            return imageInfo.getUrl();
        }

        @Override
        public String bannerTextAtIndex(int index) {
            return null;
        }

        @Override
        public DisplayImageOptions bannerLoadOptionsAtIndex(int index) {
            return null;
        }

        @Override
        public void bannerDidSelectAtIndex(int index) {

        }
    }

    /**
     *   附近快件数据适配器
     */
    public class PackageInfoDataSourceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int count = 0;
            if (packageInfoWrapper != null && (count = packageInfoWrapper.count()) > 0) {
                return count;
            }
            return count;
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
                convertView = getLayoutInflater().inflate(R.layout.activity_send_item, null);
                PackageInfoViewWrapper packageInfoViewWrapper = new PackageInfoViewWrapper(convertView);
                convertView.setTag(packageInfoViewWrapper);
            }
            PackageInfoViewWrapper packageInfoViewWrapper = (PackageInfoViewWrapper)convertView.getTag();
            PackageInfo packageInfo = (PackageInfo)getItem(position);
            fillContent(position, packageInfoViewWrapper);
            convertView.setTag(R.integer.infoKey, packageInfo);
            return convertView;
        }

        private void fillContent(int position, PackageInfoViewWrapper viewWrapper) {
            PackageInfo packageInfo = (PackageInfo)getItem(position);
            if (packageInfo != null) {
                viewWrapper.package_info_layout.setVisibility(View.VISIBLE);
                viewWrapper.load_more.setVisibility(View.GONE);
                viewWrapper.from_city.setText(packageInfo.getSource_city());
                viewWrapper.to_city.setText(packageInfo.getDestination_city());
                viewWrapper.get_time_view.setText(packageInfo.getSource_time());
                viewWrapper.distance_view.setText(packageInfo.getDistance());
                viewWrapper.income_view.setText(packageInfo.getIncoming());
                viewWrapper.arrive_time_view.setText(packageInfo.getArrive_time());

            }
            else {
                viewWrapper.package_info_layout.setVisibility(View.GONE);
                if(getCount() > 1) {
                    viewWrapper.load_more.setVisibility(View.VISIBLE);
                }
                else {
                    viewWrapper.load_more.setVisibility(View.GONE);
                }
            }
        }

        public class PackageInfoViewWrapper {
            View load_more;
            View package_info_layout;
            TextView from_city;
            TextView to_city;
            TextView get_time_view;
            TextView distance_view;
            TextView income_view;
            TextView arrive_time_view;
            public PackageInfoViewWrapper(View convertView) {
                this.package_info_layout = convertView.findViewById(R.id.package_info_layout);
                this.load_more = convertView.findViewById(R.id.load_more);
                this.from_city = (TextView)convertView.findViewById(R.id.from_city);
                this.to_city = (TextView)convertView.findViewById(R.id.to_city);
                this.get_time_view = (TextView)convertView.findViewById(R.id.get_time_view);
                this.distance_view = (TextView)convertView.findViewById(R.id.distance_view);
                this.income_view = (TextView)convertView.findViewById(R.id.income_view);
                this.arrive_time_view = (TextView)convertView.findViewById(R.id.arrive_time_view);
            }
        }
    }
}
