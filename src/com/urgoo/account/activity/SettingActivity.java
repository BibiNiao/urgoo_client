package com.urgoo.account.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.EMCallBack;
import com.urgoo.account.adapter.SettingAdapter;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.main.activities.AboutUrgoo;
import com.urgoo.message.EaseHelper;
import com.urgoo.profile.activities.ChangePwdActivity;
import com.zw.express.tool.Util;

import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by bb on 2016/9/27.
 */
public class SettingActivity extends NavToolBarActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private SettingAdapter adapter;

    @Override
    protected View createContentView() {
        setNavTitleText("设置");
        View view = inflaterViewWithLayoutID(R.layout.activity_setting, null);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        listView = (ListView) view.findViewById(R.id.lv);
        adapter = new SettingAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void logout() {
        new AlertDialog.Builder(this).setTitle(R.string.prompt).setMessage("确认退出账号吗?").
                setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EaseHelper.getInstance().logout(false, new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                JpushUtlis.setAlias(SettingActivity.this, "", new TagAliasCallback() {
                                    @Override
                                    public void gotResult(int mI, String mS, Set<String> mSet) {
                                        android.util.Log.d("alias", "设置alias为 :  " + mS);
                                    }
                                });
                                // 重新显示登陆页面
                                SPManager.getInstance(SettingActivity.this).clearLoginInfo();
                                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {

                            }
                        });
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Util.openActivity(this, AboutUrgoo.class);
                break;
            case 1:
                logout();
                break;
        }
    }
}
