package com.mini.activity.comm;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mini.R;
import com.mini.activity.send.ExPackageDetailActivity;
import com.mini.core.api.data.City;
import com.mini.core.api.data.ImageInfo;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.data.PackageInfoWrapper;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;
import com.mini.core.kit.LocationKit;
import com.mini.core.pay.PayConstants;
import com.mini.core.pay.PayListener;
import com.mini.core.pay.alipay.Alipay;
import com.mini.core.pay.alipay.Order;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.mini.frame.activity.base.MiniIntent;
import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.view.MiniBannerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/10/25.
 * 支付页面
 */
public class ExPayActivity extends MNActivityBase {

    private City city = null;

    private CEApi api = new CEApi();

    private PackageInfo packageInfo;

    private TextView from_city;
    private TextView to_city;
    private TextView package_name;
    private TextView package_price;
    private ImageView wx_pay;
    private ImageView ali_pay;
    // 0 微信 1 支付宝
    private int payMethod = 0;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_pay);
        this.setTitle("支付信息");
        this.packageInfo = (PackageInfo)this.getIntentObject();
        this.initView();
    }

    private void initView() {
        this.from_city = (TextView)this.findViewById(R.id.from_city);
        this.to_city = (TextView)this.findViewById(R.id.to_city);
        this.findViewById(R.id.button_pay, this);
        this.findViewById(R.id.layout_wx_pay, this);
        this.findViewById(R.id.layout_ali_pay, this);
        this.package_name = (TextView)this.findViewById(R.id.package_name);
        this.package_price = (TextView)this.findViewById(R.id.package_price);
        this.wx_pay = (ImageView)this.findViewById(R.id.wx_pay);
        this.ali_pay = (ImageView)this.findViewById(R.id.ali_pay);
        this.setPayMethod(this.wx_pay);
        this.wx_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayMethod(wx_pay);
            }
        });
        this.ali_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayMethod(ali_pay);
            }
        });
        if (this.packageInfo != null) {
            this.from_city.setText(this.packageInfo.getSource_city());
            this.to_city.setText(this.packageInfo.getDestination_city());
            this.package_name.setText(this.packageInfo.getName());
            this.package_price.setText(this.packageInfo.getPrice());
        }
    }

    private void setPayMethod(ImageView imageView) {
        imageView.setBackgroundResource(R.drawable.a);
        if (imageView == wx_pay) {
            payMethod = 0;
            imageView = ali_pay;
        }
        else {
            payMethod = 1;
            imageView = wx_pay;
        }
        imageView.setBackgroundResource(R.drawable.s);
    }

    @Action(R.id.layout_wx_pay)
    public void actionLayoutWxPayTap() {
        setPayMethod(wx_pay);
    }

    @Action(R.id.layout_ali_pay)
    public void actionLayoutAliPayTap() {
        setPayMethod(ali_pay);
    }

    @Action(R.id.button_pay)
    public void actionPay() {
        if (0 == payMethod) { //微信支付

        }
        else  { //支付宝支付
            Order order = new Order();
            order.setProductName("当天到快件订单");
            order.setProductDescription("当天到快件订单");
            order.setAmount("0.01");
            order.setTradeNO(this.packageInfo.getOrder_number());
            order.setNotifyURL("http://api.dtd.la/index.php/gindex/alipay");
            try {
                new Alipay().pay(this, order, new PayListener() {
                    @Override
                    public void onPayCompleted(int type, int result, int errcode, String desc) {
                        if (result == PayConstants.kCHPayResultSuccess) {
                            showMessage("支付成功");
                        }
                        else {
                            showMessage(desc);
                        }
                    }
                });
            }
            catch (Exception e) {
                showError(e);
            }
        }
    }

}
