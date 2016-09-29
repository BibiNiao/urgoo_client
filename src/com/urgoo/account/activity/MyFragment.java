package com.urgoo.account.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.account.adapter.MyAdapter;
import com.urgoo.account.biz.AccountManager;
import com.urgoo.base.BaseFragment;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.net.EventCode;
import com.urgoo.profile.activities.MessageActivity;
import com.urgoo.profile.activities.MessageListActivity;
import com.urgoo.view.MyListView;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.activity_my, null);
        initViews();
        getUserInfo();
        return viewContent;
    }

    private void getUserInfo() {
        AccountManager.getInstance(getActivity()).getUserInfo(this);
    }

    private void initViews() {
        sdvAvatar = (SimpleDraweeView) viewContent.findViewById(R.id.sdv_avatar);
        tvNick = (TextView) viewContent.findViewById(R.id.tv_nick);
        rlMy = viewContent.findViewById(R.id.rl_my);
        rlMy.setOnClickListener(this);
        listView = (MyListView) viewContent.findViewById(R.id.lv);
        myAdapter = new MyAdapter(getActivity());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        Bundle extras = new Bundle();
        switch (i) {
            case 0:
                extras.putInt(MessageListActivity.MESSAGE_TYPE, 2);
                Util.openActivityForResultWithBundle(getActivity(), MessageListActivity.class, extras,MessageListActivity.REQUEST_CODE_MESSAGE);
                break;
            case 2:
                extras.putString(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_parentOrder);
                Util.openActivityWithBundle(getActivity(), BaseWebViewActivity.class, extras);
                break;
            case 3:
                extras.putInt(MessageListActivity.MESSAGE_TYPE, 1);
                Util.openActivityForResultWithBundle(getActivity(), MessageListActivity.class, extras, MessageListActivity.REQUEST_CODE_MESSAGE);
                break;
            case 4:
                Util.openActivity(getActivity(), ActivitiesActivity.class);
                break;
            case 5:
                Util.openActivity(getActivity(), QrcodeActivity.class);
                break;
            case 6:
                intent = new Intent(getActivity(), BaseWebViewActivity.class);
                intent.putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.Action_helpJz);
                startActivity(intent);
                break;
            case 7:
                Util.openActivity(getActivity(), SettingActivity.class);
                break;
        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeGetUserInfo:
                try {
                    JSONObject jsonObject = new JSONObject(result.getString("body")).getJSONObject("parentInfo");
                    sdvAvatar.setImageURI(Uri.parse(jsonObject.getString("userIcon")));
                    tvNick.setText(jsonObject.getString("nickName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my:
//                extras = new Bundle();
//                extras.putParcelable(MyFragment.EXTRA_USER, userBean);
                Util.openActivity(getActivity(), EditUserProfileActivity.class);
                break;
        }
    }
}
