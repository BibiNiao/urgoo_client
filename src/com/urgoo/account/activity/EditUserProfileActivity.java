package com.urgoo.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.account.adapter.EditUserProfileAdapter;
import com.urgoo.account.biz.AccountManager;
import com.urgoo.account.event.EditProfileEvent;
import com.urgoo.account.model.UserBean;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/9/28.
 */
public class EditUserProfileActivity extends NavToolBarActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_EDIT_SCHOOL = 0;
    private SimpleDraweeView ivAvatar;
    private RelativeLayout rlAvatar;
    private ListView lv;
    private EditUserProfileAdapter adapter;
    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected View createContentView() {
        setNavTitleText("我的资料");
        View view = inflaterViewWithLayoutID(R.layout.activity_edituser, null);
        initViews(view);
        getUserData();
        return view;
    }

    private void getUserData() {
        showLoadingDialog();
        AccountManager.getInstance(this).getUserData(this);
    }

    private void initViews(View view) {
        rlAvatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);
        rlAvatar.setOnClickListener(this);
        ivAvatar = (SimpleDraweeView) view.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(this);
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Util.openActivityForResult(EditUserProfileActivity.this, SchoolActivity.class, REQUEST_CODE_EDIT_SCHOOL);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeGetUserData:
                try {
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    user = gson.fromJson(result.get("body").toString(), new TypeToken<UserBean>() {
                    }.getType());
                    if (user != null) {
                        adapter = new EditUserProfileAdapter(this, user);
                        lv.setAdapter(adapter);
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onNavLeftClick(View v) {
        EventBus.getDefault().post(new EditProfileEvent(user));
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MyFragment.EXTRA_USER, user);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onNavLeftClick(null);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

    }
}
