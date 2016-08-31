package com.urgoo.webviewmanage;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.urgoo.ScreenManager;
import com.urgoo.client.R;
import com.urgoo.webviewmanage.BaseWebViewFragment.WebViewLinstener;


/**
 * Created by lijie on 2016/3/24.
 */
@SuppressLint("NewApi")
public class BaseWebViewActivity extends   FragmentActivity  implements WebViewLinstener {
    private BaseWebViewFragment baseWebViewFragment;
    public static final String EXTRA_URL = "EXTRA_URL";
    private WebView webView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_webview_activity);
        ScreenManager.getScreenManager().pushActivity(this);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        String a= getIntent().getStringExtra(EXTRA_URL);
        baseWebViewFragment = new MyWebViewFragment();
        if (getIntent().hasExtra(EXTRA_URL) && !TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_URL))) {
            Bundle b = new Bundle();
            b.putString(BaseWebViewFragment.EXTRA_URL, getIntent().getStringExtra(EXTRA_URL));
            baseWebViewFragment.setArguments(b);
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, baseWebViewFragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenManager.getScreenManager().popActivity(this);
    }

    @Override
    public void onWebViewLoadStart(WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onWebViewProgress(WebView webView, int progress) {    }

    @Override
    public void onWebViewReceivedTitle(WebView webView, String title) {    }

    @Override
    public void onWebViewLoadFinished(WebView webView) {    }

    @Override
    public void onWebViewLoadError(WebView webView, String msg) {    }
}
