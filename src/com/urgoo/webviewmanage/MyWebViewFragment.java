
package com.urgoo.webviewmanage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.urgoo.ScreenManager;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.counselor.activities.CounselorList;
import com.urgoo.message.activities.ChatActivity;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.pay.activities.PaySelectActivity;

/**
 * Created by lijie on 2016/3/24.
 */
public class MyWebViewFragment extends BaseWebViewFragment {
    public static final String TAG = MyWebViewFragment.class.getName();
    private WebView webView;
    private String urlx = "http://10.203.203.129:8080/";

    private void init() {
        if (webView != null)
            webView.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
    }

    @Override
    protected boolean mShouldOverrideUrlLoading(WebView view, String url) {
        Log.d(TAG + "________", url);
        if (url.startsWith("urgoo://gotoWebView_")) {
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            //intent.putExtra(EXTRA_URL, "http://115.28.50.163:8080/urgoo/" + url.substring(url.indexOf("$_$")+3));
            intent.putExtra(EXTRA_URL, ZWConfig.URGOOURL_BASE + url.substring(url.indexOf("_") + 1));

            getActivity().startActivity(intent);
            view.stopLoading();
            return true;
        } else if (url.startsWith("urgoo://gotoConversation_")) {
            String hxID = url.substring(url.indexOf("_") + 1).toLowerCase();
            Log.d("dddd", hxID);
            startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", hxID));
            ScreenManager.getScreenManager().popOneActivity();
            view.stopLoading();
            return true;
        } else if (url.startsWith("urgoo://gotoAssessmentPage_")) {
            Log.d("gotoEdit", "....goto");
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            //intent.putExtra(EXTRA_URL, "http://115.28.50.163:8080/urgoo/" + url.substring(url.indexOf("$_$")+3));
            intent.putExtra(EXTRA_URL, ZWConfig.URGOOURL_BASE + url.substring(url.indexOf("_") + 1));
            getActivity().startActivity(intent);
            ScreenManager.getScreenManager().popTwoActivity();
            view.stopLoading();
            return true;
        } else if (url.startsWith("urgoo://gotoEdit_")) {
            Log.d("gotoEdit", "....goto");
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            //intent.putExtra(EXTRA_URL, "http://115.28.50.163:8080/urgoo/" + url.substring(url.indexOf("$_$")+3));
            intent.putExtra(EXTRA_URL, ZWConfig.URGOOURL_BASE + url.substring(url.indexOf("_") + 1));
            getActivity().startActivity(intent);
            ScreenManager.getScreenManager().popTwoActivity();
            view.stopLoading();
            return true;
        } else if (url.startsWith("urgoo://gotoOrderDtlJz")) {
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            //intent.putExtra(EXTRA_URL, "http://115.28.50.163:8080/urgoo/" + url.substring(url.indexOf("$_$")+3));
            intent.putExtra(EXTRA_URL, ZWConfig.URGOOURL_BASE + url.substring(url.indexOf("_") + 1));
            getActivity().startActivity(intent);
            //ScreenManager.getScreenManager().popTwoActivity();
            //ScreenManager.getScreenManager().popOneActivity();
            //ScreenManager.getScreenManager().popThreeActivity();
            view.stopLoading();
            return true;
        } else if (url.startsWith("urgoo://gotoSearchConsultant")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(intent);
            view.stopLoading();
        } else if (url.startsWith("urgoo://gotoOriginTask")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("nicai", "2333");
            getActivity().startActivity(intent);
            view.stopLoading();
        } else if (url.startsWith("urgoo://gotoOriginPay")) {
            Intent intent = new Intent(getActivity(), PaySelectActivity.class);
            intent.putExtra(PaySelectActivity.ORDERID, url.substring(url.indexOf("_") + 1));
            intent.putExtra(EXTRA_URL, ZWConfig.URGOOURL_BASE + url.substring(url.indexOf("_") + 1));
            getActivity().startActivity(intent);
            getActivity().finish();
            view.stopLoading();
        } else if (url.startsWith("urgoo://gotoOriginPlan")) {

            //杨德成 20160630 订单详情页面跳转规划主页
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            intent.putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_parentPlanningHtml);
            getActivity().startActivity(intent);
            ScreenManager.getScreenManager().popTwoActivity();
            view.stopLoading();
        } else if (url.startsWith("urgoo://gotoLookForGw")) {
            //寻找顾问
            getActivity().startActivity(new Intent(getActivity(), CounselorList.class));
            ScreenManager.getScreenManager().popTwoActivity();
            view.stopLoading();
        } else if (url.startsWith("urgoo://gotoConversation")) {
            // 优优
            // window.location.href ='urgoo://gotoConversation' + signLink + val;
            getActivity().startActivity(new Intent(getActivity(), ChatActivity.class)
                    .putExtra("userId", ZWConfig.ACTION_CustomerService));
            view.stopLoading();
        } else if (url.startsWith("urgoo://gotoGwDtl")) {
            //dff 16-8-3 订单跳转到顾问详情
            Intent intent = new Intent(getActivity(), CounselorActivity.class);
            intent.putExtra(CounselorActivity.COUNSELOR_ID, url.substring(url.indexOf("_") + 1));
            //intent.putExtra(EXTRA_URL, ZWConfig.URGOOURL_BASE + url.substring(url.indexOf("_") + 1));
            getActivity().startActivity(intent);
            view.stopLoading();
        } else if (
            // url.startsWith("urgoo://gotoOrderDtlJz")||
                url.startsWith("urgoo://gotoPlanDtlJz")
                        || url.startsWith("urgoo://gotoUs")
                        || url.startsWith("urgoo://gotoXyJz")
                        || url.startsWith("urgoo://gotoSchool")
                        || url.startsWith("urgoo://gotoPersonalityLabel")
                        || url.startsWith("urgoo://gotoGraduationTime")
                        || url.startsWith("urgoo://gotoAccountDtlJz")
                        || url.startsWith("urgoo://gotoOrderPay")
                        || url.startsWith("urgoo://gotoPersonalityLabel")
                        || url.startsWith("urgoo://gotoGraduationTime")
                        || url.startsWith("urgoo://gotoAimMajor")
                        || url.startsWith("urgoo://gotoAimSchool")
                        || url.startsWith("urgoo://gotoMyInfo")
                        || url.startsWith("urgoo://extracurricularJzAdd")
                        || url.startsWith("urgoo://gotoAssessmentPage")
                        || url.startsWith("urgoo://gotoAwards")
                        || url.startsWith("urgoo://gotoTaskJz")
                        || url.startsWith("urgoo://gotoPlanningStu")
                        || url.startsWith("urgoo://gotoTaskStu")
                        || url.startsWith("urgoo://gotoEditStu")
                        || url.startsWith("urgoo://gotoTestDtl")
                        || url.startsWith("urgoo://gotoTestPage")
                        || url.startsWith("urgoo://gotoTaskDetail")
                        || url.startsWith("urgoo://gotoToTest")
                        || url.startsWith("urgoo://gotoConversation")
                        || url.startsWith("urgoo://gotoExtracurricularJz")
                        || url.startsWith("urgoo://gotoFwXy")
                        || url.startsWith("urgoo://gotoPtGz")
                        || url.startsWith("urgoo://gotoPayment")) {
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            //intent.putExtra(EXTRA_URL, "http://115.28.50.163:8080/urgoo/" + url.substring(url.indexOf("$_$")+3));
            intent.putExtra(EXTRA_URL, ZWConfig.URGOOURL_BASE + url.substring(url.indexOf("_") + 1));
            getActivity().startActivity(intent);
            view.stopLoading();
            return true;
        }

       /* UiUtil.show(getActivity(), "url");
        if (url.startsWith("https://www.baidu.com")) {
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            intent.putExtra(EXTRA_URL, urlx + url.substring(url.indexOf("$_$")));
            getActivity().startActivity(intent);
            view.stopLoading();
            return true;
        }*/
        return false;
    }

    @Override
    protected void onWebViewLoadStartProtected(WebView webView) {
        this.webView = webView;
        init();
    }

    @Override
    protected void onWebViewProgressProtected(WebView webView, int progress) {

    }

    @Override
    protected void onWebViewLoadFinishedProtected(WebView webView) {

    }

    @Override
    protected void onWebViewLoadErrorProtected(WebView webView, String msg) {

    }


    class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }


}
