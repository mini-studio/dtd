package com.mini.activity.comm;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mini.R;
import com.mini.core.api.data.City;
import com.mini.core.api.data.PackageInfo;
import com.mini.core.api.engine.CEApi;
import com.mini.core.exception.CEDataException;

import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.pay.PayConstants;
import org.mini.frame.pay.PayListener;
import org.mini.frame.pay.alipay.AliPay;
import org.mini.frame.pay.alipay.AliPayParam;

import org.mini.frame.annotation.Action;
import org.mini.frame.pay.wxpay.WXPay;
import org.mini.frame.pay.wxpay.WXPayParam;

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
            showWaiting();
            api.wxPayInfo(this.packageInfo.getOrder_number(), new MiniDataListener<WXPayParam>() {
                @Override
                public void onResponse(WXPayParam data, CEDataException error) {
                    dismissWaiting();
                    if (error == null) {
                        wxPayment(data);
                    }
                    else {
                        showError(error);
                    }
                }
            });
        }
        else  { //支付宝支付
            aliPayment();
        }
    }

    /**
     * 微信支付
     * @param data
     */
    private void wxPayment(WXPayParam data) {
        WXPay.sharedInstance().pay(this, data, new PayListener() {
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

    /**
     * 阿里支付
     */
    public void aliPayment() {
        AliPayParam order = new AliPayParam();
        order.setProductName("当天到快件订单");
        order.setProductDescription("当天到快件订单("+this.packageInfo.getSource_city()+"-"+this.packageInfo.getDestination_city()+")");
        order.setAmount("0.01");
        order.setTradeNO(this.packageInfo.getOrder_number());
        order.setNotifyURL("http://api.dtd.la/index.php/gindex/alipay");
        try {
            new AliPay().pay(this, order, new PayListener() {
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
