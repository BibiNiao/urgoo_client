package com.urgoo.webviewmanage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urgoo.account.activity.HomeActivity;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;
import com.urgoo.message.activities.MainActivity;
import com.zw.express.tool.NetWorkUtil;
import com.zw.express.tool.log.Log;

import java.io.IOException;

/**
 * Created by lijie on 2016/3/24.
 */
@SuppressLint("NewApi")
public abstract class BaseWebViewFragment extends Fragment {

    public static final String EXTRA_URL = "EXTRA_URL";
    private View rootView;
    private WebView webView;
    private WebViewLinstener webViewLinstener;
    private String url = "";
    TextView tv_title;
    private String title;

    private ProgressBar pb;
    private RelativeLayout net;
    private Button refresh;

    private LinearLayout back, activity_selectimg_send;

    public void setWebViewLinstener(WebViewLinstener webViewLinstener) {
        this.webViewLinstener = webViewLinstener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.web_view_fragment, null);
        webView = (WebView) rootView.findViewById(R.id.webView);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        refresh = (Button) rootView.findViewById(R.id.refresh);
        net = (RelativeLayout) rootView.findViewById(R.id.net);
        pb = (ProgressBar) rootView.findViewById(R.id.rb);
        back = (LinearLayout) rootView.findViewById(R.id.back);
        activity_selectimg_send = (LinearLayout) rootView.findViewById(R.id.activity_selectimg_send);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });
        activity_selectimg_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                net.setVisibility(View.GONE);
                reLoad();
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        if (getArguments() != null || TextUtils.isEmpty(url))
            this.url = getArguments().getString(EXTRA_URL);

        if (!TextUtils.isEmpty(url)) {
            if (url.contains("?")) {
                Log.d("BaseWebViewFragment", url + "&token=" + SPManager.getInstance(getActivity()).getToken());
                url = url + "&token=" + SPManager.getInstance(getActivity()).getToken() + "&termType=2";
                loadUrl(url);
            } else if (url.contains("nosign")) {
                getBack().setVisibility(View.GONE);
                getHome().setVisibility(View.GONE);
                loadUrl(url + "?termType=2");
            } else {
                Log.d("BaseWebViewFragment", url + "?token=" + SPManager.getInstance(getActivity()).getToken());
                url = url + "?token=" + SPManager.getInstance(getActivity()).getToken() + "&termType=2";
                loadUrl(url);
            }

        }
    }

    private void startLogin() {
        getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
        if (getActivity() instanceof BaseWebViewActivity) {
            getActivity().finish();
        }
    }

    protected LinearLayout getBack() {
        return back;
    }

    protected LinearLayout getHome() {
        return activity_selectimg_send;
    }

    private void loadUrl(String url) {
        if (!NetWorkUtil.isNetwokConnect(getActivity())) {
            net.setVisibility(View.VISIBLE);
            return;
        }
        net.setVisibility(View.GONE);
        if (webView != null) {

            //杨德成 20160804 添加growingio  h5页面采集
            webView.setWebChromeClient(new MyWebChromeClient());

            Log.d("url", url);
            webView.loadUrl(url);
            webView.setWebViewClient(new MyWebViewClient());//一定要放webView.loadUrl后面
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void reLoad() {
        if (TextUtils.isEmpty(url) || webView == null) {
            return;
        }

        loadUrl(url);
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {


            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

            } else {
                if (View.INVISIBLE == pb.getVisibility()) {
                    pb.setVisibility(View.VISIBLE);
                }
                pb.setProgress(newProgress);
            }

            super.onProgressChanged(view, newProgress);
            if (webViewLinstener != null)
                webViewLinstener.onWebViewProgress(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (webViewLinstener != null)
                webViewLinstener.onWebViewReceivedTitle(view, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }


    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mShouldOverrideUrlLoading(view, url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            String titleStr = view.getTitle();
            if (titleStr == null || titleStr.equals("")) {

            } else {
                tv_title.setText(titleStr);
                setTitle(titleStr.trim());
            }

            pageFinished(view, url);

        }


        WebResourceResponse minJs, bundleJs, minCss;

        private WebResourceResponse getCustomWebResourceResponse(String js) {
            try {
                String str = js.endsWith(".js") ? "text/javascript" : "text/css";
                return new WebResourceResponse(str, "utf-8",
                        getActivity().getAssets().open(js));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (url.contains("ionic.bundle.min.js") && minJs == null) {
                minJs = getCustomWebResourceResponse("ionic.bundle.min.js");
                return minJs;
            } else if (url.contains("ionic.bundle.js") && bundleJs == null) {
                bundleJs = getCustomWebResourceResponse("ionic.bundle.js");
                return bundleJs;
            } else if (url.contains("ionic.min.css")) {
                minCss = getCustomWebResourceResponse("ionic.min.css");
                return minCss;
            }
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (webViewLinstener != null)
                webViewLinstener.onWebViewLoadError(view, description);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pageStart(view, url, favicon);
        }

    }

    private void pageStart(WebView view, String url, Bitmap favicon) {
        if (webViewLinstener != null)
            webViewLinstener.onWebViewLoadStart(webView);
    }

    private void pageFinished(WebView view, String url) {
        if (webViewLinstener != null)
            webViewLinstener.onWebViewLoadFinished(webView);
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
        Log.d("..resume", getTitle() + "xxxx" + url);
        if ("我的资料".equals(getTitle())) {
            Log.d("..我的资料", getTitle() + "xxxx" + url);
            reLoad();
        }
        if ("编辑资料".equals(getTitle())) {
            reLoad();
        }
    }

    abstract protected boolean mShouldOverrideUrlLoading(WebView view, String url);

    abstract protected void onWebViewLoadStartProtected(WebView webView);

    abstract protected void onWebViewProgressProtected(WebView webView, int progress);

    abstract protected void onWebViewLoadFinishedProtected(WebView webView);

    abstract protected void onWebViewLoadErrorProtected(WebView webView, String msg);


    public interface WebViewLinstener {
        public void onWebViewLoadStart(WebView webView);

        public void onWebViewProgress(WebView webView, int progress);

        public void onWebViewReceivedTitle(WebView webView, String title);

        public void onWebViewLoadFinished(WebView webView);

        public void onWebViewLoadError(WebView webView, String msg);
    }

}
