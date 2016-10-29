package com.urgoo.account.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.account.adapter.EditUserProfileAdapter;
import com.urgoo.account.event.EditProfileEvent;
import com.urgoo.account.model.UserBean;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.zw.express.tool.PickUtils;
import com.zw.express.tool.Util;
import com.zw.express.tool.log.Log;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bb on 2016/9/28.
 */
public class EditUserProfileActivity extends NavToolBarActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_EDIT_SCHOOL = 1;
    public static final int REQUEST_CODE_EDIT_NAME = 110;
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
//        getUserData();
        return view;
    }

//    private void getUserData() {
//        showLoadingDialog();
//        AccountManager.getInstance(this).getUserData(this);
//    }

    private void initViews(View view) {
        user = getIntent().getParcelableExtra(MyFragment.EXTRA_USER);
        rlAvatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);
        rlAvatar.setOnClickListener(this);
        ivAvatar = (SimpleDraweeView) view.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(this);
        ivAvatar.setImageURI(Uri.parse(user.getUserIcon()));
        lv = (ListView) view.findViewById(R.id.lv);
        adapter = new EditUserProfileAdapter(this, user);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Util.openActivityForResult(EditUserProfileActivity.this, EditNickNameActivity.class, REQUEST_CODE_EDIT_NAME);
                        break;
                    case 1:
                        String strURL = ZWConfig.URL_requestEditMy + "?token=" + spManager.getToken();
                        startActivity(new Intent(EditUserProfileActivity.this, BaseWebViewActivity.class).putExtra(BaseWebViewActivity.EXTRA_URL, strURL));
                        break;
                }
            }
        });
    }

    @Override
    protected void onNavLeftClick(View v) {
        EventBus.getDefault().post(new EditProfileEvent(user));
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
        switch (v.getId()) {
            case R.id.rl_avatar:
                PickUtils.doPickPhotoAction(this);
                break;
            case R.id.iv_avatar:
                PickUtils.doPickPhotoAction(this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT_NAME:
                    user.setNickName(spManager.getNickName());
                    adapter.notifyDataSetChanged();
                    break;
                case PickUtils.PHOTO_PICKED_WITH_DATA: // 调用Gallery返回的
                    PickUtils.doCropPhoto(this, data.getData());
                    Log.d("图片路径", String.valueOf(data.getData()));
                    break;
                case PickUtils.CAMERA_WITH_DATA: // 照相机程序返回的
                    PickUtils.rotatePhotoAndSave(ZWConfig.pickPicture);
                    PickUtils.doCropPhoto(this, Uri.fromFile(new File(ZWConfig.pickPicture)));  //如果不裁剪及不需要调用该方法
                    break;
                case PickUtils.PHOTO_CROP: //图片裁剪操作
                    showLoadingDialog();
                    OkHttpClient client = new OkHttpClient();
                    // mImgUrls为存放图片的url集合
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), new File(ZWConfig.tempPicture.getPath()));
                    builder.addFormDataPart("userIconFile", ZWConfig.tempPicture.getPath(), fileBody);
                    builder.addFormDataPart("token", spManager.getToken());
                    //构建请求体
                    RequestBody requestBody = builder.build();
                    //构建请求
                    Request request = new Request.Builder()
                            .url(ZWConfig.ACTION_updateUserIcon)//地址
                            .post(requestBody)//添加请求体
                            .build();
                    //发送异步请求，同步会报错，Android4.0以后禁止在主线程中进行耗时操作
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            dismissLoadingDialog();
                            showToastSafe("修改失败请重新上传");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            dismissLoadingDialog();
                            showToastSafe("头像修改成功");
                        }
                    });
                    ivAvatar.setImageURI(ZWConfig.tempPicture);
                    user.setUserIcon(ZWConfig.tempPicture.getPath());
                    break;
            }
        }
    }
}
