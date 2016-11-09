package com.urgoo.account.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.account.adapter.MyAdapter;
import com.urgoo.account.biz.AccountManager;
import com.urgoo.account.event.EditProfileEvent;
import com.urgoo.account.model.UserBean;
import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.message.activities.SysMessageActivity;
import com.urgoo.message.activities.UserMessageActivity;
import com.urgoo.net.EventCode;
import com.urgoo.profile.activities.MessageListActivity;
import com.urgoo.schedule.activites.PrecontractMyOrder;
import com.urgoo.view.MyListView;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/9/18.
 */
public class MyFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private MyListView listView;
    private MyAdapter myAdapter;
    private SimpleDraweeView sdvAvatar;
    private TextView tvNick;
    private View rlMy;
    public static final String EXTRA_USER = "extra_user";
    private UserBean userBean;
    private MessageFragmentCallback callback;
    private boolean isShowRed;
    /**
     * 系统消息红点
     */
    private int systemCount;
    /**
     * 我的消息红点
     */
    private int individualCount;
    /**
     * 预约红点
     */
    private int advanceCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_my, null);
        EventBus.getDefault().register(this);
        initViews();
        getUserInfo();
        getSelectRedCount();
        return viewContent;
    }

    public void getSelectRedCount() {
        AccountManager.getInstance(getActivity()).getSelectRedCount(this);
    }

    private void getUserInfo() {
        AccountManager.getInstance(getActivity()).getUserInfo(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSelectRedCount();
    }

    private void initViews() {
        sdvAvatar = (SimpleDraweeView) viewContent.findViewById(R.id.sdv_avatar);
        tvNick = (TextView) viewContent.findViewById(R.id.tv_nick);
        rlMy = viewContent.findViewById(R.id.rl_my);
        rlMy.setOnClickListener(this);
        listView = (MyListView) viewContent.findViewById(R.id.lv);
        myAdapter = new MyAdapter(getActivity(), systemCount, individualCount, advanceCount);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
    }

    /**
     * 成功修改用户信息后进行界面刷新
     *
     * @param event
     */
    public void onEventMainThread(EditProfileEvent event) {
        getUserInfo();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        Bundle extras = new Bundle();
        switch (i) {
            case 0:
                Util.openActivityForResult(getActivity(), UserMessageActivity.class, MessageListActivity.REQUEST_CODE_MESSAGE);
                break;
            case 1:
                Util.openActivity(getActivity(), PrecontractMyOrder.class);
                break;
            case 2:
                extras.putString(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_parentOrder);
                Util.openActivityWithBundle(getActivity(), BaseWebViewActivity.class, extras);
                break;
            case 3:
                Util.openActivityForResult(getActivity(), SysMessageActivity.class, MessageListActivity.REQUEST_CODE_MESSAGE);
                break;
//            case 4:
//                Util.openActivity(getActivity(), ActivitiesActivity.class);
//                break;
            case 4:
                Util.openActivity(getActivity(), QrcodeActivity.class);
                break;
            case 5:
//                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));

                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-061-2819"));
                startActivity(intent);

//                intent = new Intent(getActivity(), BaseWebViewActivity.class);
//                intent.putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.Action_helpJz);
//                startActivity(intent);
                break;
            case 6:
                Util.openActivity(getActivity(), SettingActivity.class);
                break;
        }
    }

    public void setMessageFragmentCallback(MessageFragmentCallback callback) {
        this.callback = callback;
    }

    private void invokeUnreadCallback() {
        if (callback != null) {
            callback.onUnreadMessageCallback(isShowRed);
        }
    }

    /**
     * 刷新未读消息数量
     *
     * @author wangsheng
     */
    public interface MessageFragmentCallback {
        void onUnreadMessageCallback(boolean isShow);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeSelectRedCount:
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body"));
                    systemCount = jsonObject.getInt("systemCount");
                    individualCount = jsonObject.getInt("individualCount");
                    advanceCount = jsonObject.getInt("advanceCount");
                    if (jsonObject.getInt("allCount") > 0) {
                        isShowRed = true;
                    } else {
                        isShowRed = false;
                    }
                    invokeUnreadCallback();
                    myAdapter.setCount(systemCount, individualCount, advanceCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeGetUserInfo:
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body")).getJSONObject("parentInfo");
                    sdvAvatar.setImageURI(Uri.parse(jsonObject.getString("userIcon")));
                    tvNick.setText(jsonObject.getString("nickName"));
                    userBean = new UserBean();
                    userBean.setUserIcon(jsonObject.getString("userIcon"));
                    userBean.setNickName(jsonObject.getString("nickName"));
                    SPManager.getInstance(getActivity()).setUserIcon(jsonObject.getString("userIcon"));
                    SPManager.getInstance(getActivity()).setUserName(jsonObject.getString("nickName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Bundle extras;
        switch (v.getId()) {
            case R.id.rl_my:
                if (userBean != null) {
                    extras = new Bundle();
                    extras.putParcelable(MyFragment.EXTRA_USER, userBean);
                    Util.openActivityWithBundle(getActivity(), EditUserProfileActivity.class, extras);
                }
                break;
        }
    }
}
