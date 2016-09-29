package com.urgoo.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.account.adapter.EditUserProfileAdapter;
import com.urgoo.account.event.EditProfileEvent;
import com.urgoo.account.model.UserBean;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;

import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/9/28.
 */
public class EditUserProfileActivity extends NavToolBarActivity implements View.OnClickListener {
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
        return view;
    }

    private void initViews(View view) {
        rlAvatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);
        rlAvatar.setOnClickListener(this);
        ivAvatar = (SimpleDraweeView) view.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(this);
        lv = (ListView) view.findViewById(R.id.lv);
        adapter = new EditUserProfileAdapter(this, user);
        lv.setAdapter(adapter);
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
