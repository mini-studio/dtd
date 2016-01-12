package com.mini.activity.comm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.R;
import com.mini.views.CEWebView;

import org.mini.frame.activity.base.MiniIntent;
import org.mini.frame.annotation.Action;
import org.mini.frame.toolkit.MiniSystemHelper;
import org.mini.frame.toolkit.location.MiniLocationListener;
import org.mini.frame.toolkit.location.MiniLocationManager;
import org.mini.frame.view.MiniCustomDialog;

import java.io.File;

/**
 */
public class MNWebViewActivity extends MNActivityBase implements MiniLocationListener {

    private final String NEWTAB = "newtab";
    private final String CHEDU_LOCATION_SCHEME = "chedu-location";
    private String locationUrl;//定位截获的url
    private CEWebView mWebView;
    private ProgressBar mProgressBar;
    private TextView mActionRightText;
    private RelativeLayout mActionRightLayout;
    private Handler mHandler = new Handler();
    private boolean bFinish = true;
    private CEWebViewObject webViewObject = null;

    private String close = "关闭";

    public static class CEWebViewObject {
        private String url;
        private String title;
        private boolean bDifference;

        public String getUrl() {
            if (url != null && !url.startsWith("http")) {
                return "http://" + url;
            } else {
                return url;
            }
        }
    }

    public static void open(Context from, String url) {
        open(from, url, null, true);
    }

    /**
     * @param from        上下文
     * @param url         路径
     * @param title       标题
     * @param bDifference true actionBar右上角无文字，false有文字
     */
    public static void open(Context from, String url, String title, boolean bDifference) {
        clearWebViewCache(from);
        CEWebViewObject viewObject = new CEWebViewObject();
        viewObject.url = url;
        viewObject.title = title;
        viewObject.bDifference = bDifference;
        MiniIntent miniIntent = new MiniIntent();
        miniIntent.setClass(from, MNWebViewActivity.class);
        miniIntent.setObject(viewObject);
        from.startActivity(miniIntent);
    }

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_web_view);
        initView();
        this.loadData();
    }

    private void initView() {
        mActionRightText = (TextView) findViewById(R.id.app_right_title);
        mActionRightText.setTextColor(getResources().getColor(R.color.parents_center_info));
        mActionRightLayout = (RelativeLayout) findViewById(R.id.app_title_right);
        mActionRightText.setVisibility(View.VISIBLE);
        mActionRightLayout.setVisibility(View.VISIBLE);
        mWebView = (CEWebView) findViewById(R.id.item_detail_activity_webview);
        mWebView.setWebChromeClient(new CEWebChromeClient());
        mWebView.setWebViewClient(new CEWebViewClient());
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mProgressBar = (ProgressBar) findViewById(R.id.item_detail_activity_progressbar);
        mProgressBar.setMax(100);
        findViewById(R.id.app_title_back, this);
        findViewById(R.id.app_title_right, this);
        mActionRightText.setText(close);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String type,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    private void loadData() {
        Object viewObject = getIntentObject();
        if (viewObject != null && viewObject instanceof CEWebViewObject) {
            this.webViewObject = (CEWebViewObject) viewObject;
            if (this.webViewObject.title != null) {
                this.setTitle(this.webViewObject.title);
            }
            if (this.webViewObject.url != null) {
                this.mWebView.loadUrl(this.webViewObject.getUrl());
            } else {
                this.showMessage(R.string.invalid_web_address);
            }
        } else {
            this.showMessage(R.string.invalid_web_address);
        }
    }

    private void handleRightButton() {
        setRightTextColor(mActionRightText);
        if (bFinish) {
            mActionRightText.setText("关闭");
        } else {
            mActionRightText.setText("回首页");
        }
    }

    private void setRightTextColor(TextView text) {
        text.setTextColor(getResources().getColor(R.color.common_green));
    }

    @Override
    protected void back() {
        if (mWebView.canGoBack() && !bFinish) {
            mWebView.goBack();
        } else if (mWebView.canGoBack() && this.webViewObject.bDifference) {
            mWebView.goBack();
        } else {
            pausePlayVideo();
            finish();
        }
    }

    @Action(R.id.app_title_right)
    public void actionRightLayout() {
        if (close.equals(mActionRightText.getText().toString())) {
            pausePlayVideo();
            finish();
            return;
        }
        if (!this.webViewObject.bDifference) {
            if (bFinish) {
                pausePlayVideo();
                finish();
            } else {
                mWebView.loadUrl(webViewObject.url);
            }
        }
    }

    class CEWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }


        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    class CEWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 获取位置信息
            if (url.indexOf(CHEDU_LOCATION_SCHEME) == 0) {
                MiniLocationManager.shard().addListener(MNWebViewActivity.this);
                MiniLocationManager.shard().start(MNWebViewActivity.this);
                locationUrl=url;
            }
           else if (!webViewObject.bDifference) {
                bFinish = false;
                handleRightButton();
                if (url.startsWith(NEWTAB)) {
                    String httpUrl = url.replaceFirst(NEWTAB, "http");
                    mWebView.loadUrl(httpUrl);
                } else if (MiniSystemHelper.isWtaiTypeUrl(url)) {
                    if (url.startsWith("tel:")) {
                        MiniSystemHelper.handleTellTypeUrl(MNWebViewActivity.this, url);
                    } else {
                        MiniSystemHelper.handleWtaiTypeEmailUrl(MNWebViewActivity.this, url);
                    }
                } else {
                    view.loadUrl(url);
                }
            } else {
                if (MiniSystemHelper.isItemWtaiTypeUrl(url)) {
                    MiniSystemHelper.handleWtaiTypeUrl(MNWebViewActivity.this, url);
                } else if (MiniSystemHelper.isWtaiTypeUrl(url)) {
                    if (url.startsWith("tel:")) {
                        MiniSystemHelper.handleTellTypeUrl(MNWebViewActivity.this, url);
                    } else {
                        MiniSystemHelper.handleWtaiTypeEmailUrl(MNWebViewActivity.this, url);
                    }
                } else {
                    view.loadUrl(url);
                }
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack() && !bFinish) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
        } else if (mWebView.canGoBack() && this.webViewObject.bDifference) {
            mWebView.goBack();
        } else {
            pausePlayVideo();
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayVideo();
    }

    @Override
    public void chLocationManagerDidGetLatAndLon() {
        if (MiniLocationManager.shard().isGetLatAndLon()) {
            //填写定位信息回传服务器
            String newUrl=locationUrl+"?lon="+MiniLocationManager.shard().longitude()+"&lat="+MiniLocationManager.shard().latitude();
            mWebView.loadUrl(newUrl);
            //取消定位
            MiniLocationManager.shard().removeListener(MNWebViewActivity.this);
            MiniLocationManager.shard().stop();
        } else {
            MiniCustomDialog.show(this, "提示", "请在设置中开启定位功能，否则无法进行签到操作。", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, "设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 0);
                }
            });
        }
    }

    private static void clearWebViewCache(Context from) {
        try {
            from.deleteDatabase("webview.db");
        } catch (Exception e) {
        }
        try {
            from.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
        }
        clearCacheFolder(from.getCacheDir(), System.currentTimeMillis());
    }

    private static int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }


    /**
     * 暂停播放视频
     */
    private void pausePlayVideo() {
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
