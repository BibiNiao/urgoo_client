package com.urgoo.account.activity;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.urgoo.account.biz.AccountManager;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.zw.express.tool.Util;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by bb on 2016/9/29.
 */
public class EditNickNameActivity extends NavToolBarActivity {
    private EditText etContent;
    private String nickName;

    @Override
    protected View createContentView() {
        setNavTitleText("修改昵称");
        View view = inflaterViewWithLayoutID(R.layout.activity_edit_name, null);
        initViews(view);
        return view;
    }

    @Override
    protected int getOptionMenuRes() {
        return R.menu.text_menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            submit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(View view) {
        etContent = (EditText) view.findViewById(R.id.et_content);
        etContent.setText(spManager.getNickName());
        etContent.setSelection(etContent.getText().toString().length());
    }

    private void submit() {
        if (Util.isEmpty(etContent.getText().toString().trim())) {
            showToastSafe("昵称不能为空");
        } else if (Util.getMixedTextlength(etContent.getText().toString().trim()) > 20) {
            showToastSafe("昵称太长啦");
        } else {
            nickName = etContent.getText().toString();
            updateNickName();
        }
    }

    private void updateNickName() {
        showLoadingDialog();
        AccountManager.getInstance(this).updateNickName(nickName, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeUpdateNickName:
                showToastSafe("修改成功");
                spManager.setNickName(nickName);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
