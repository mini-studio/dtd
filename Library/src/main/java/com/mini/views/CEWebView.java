package com.mini.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mini.app.CEAppConfig;

import org.mini.frame.http.request.MiniRequest;

import java.util.Map;

/**
 * Created by Wuquancheng on 15/6/12.
 */
public class CEWebView extends WebView {
    public CEWebView(Context context) {
        super(context);
        didInit();
    }

    public CEWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        didInit();
    }

    public CEWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        didInit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CEWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        didInit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CEWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        didInit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void didInit() {
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setSupportZoom(true);
        this.getSettings().setBuiltInZoomControls(true);
        this.getSettings().setDisplayZoomControls(false);

        //支持html5获取当前的位置信息
//        WebSettings webSettings = mWebView.getSettings();
        this.getSettings().setGeolocationEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDatabaseEnabled(true);
        this.getSettings().setPluginState(WebSettings.PluginState.ON);


    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
    }

    public void loadUrl(String url) {
        if (url != null && url.length() > 0) {
            if (url.startsWith(CEAppConfig.HOST()) ||
                    url.indexOf("edutao") != -1 ||
                    url.indexOf("peiyu100") != -1 ||
                    url.indexOf("peiyuapp") != -1 ||
                    url.indexOf("pyu") != -1 ||
                    url.indexOf("py100") != -1
                    ) {
                this.loadUrl(url, MiniRequest.getStandardHeaders(null));
            } else {
                super.loadUrl(url);
            }
        }
    }
}
