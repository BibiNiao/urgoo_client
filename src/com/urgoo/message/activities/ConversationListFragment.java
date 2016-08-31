package com.urgoo.message.activities;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.urgoo_Interface.ExpansionInterface;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.NetUtils;
import com.urgoo.business.BaseService;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.Constant;
import com.urgoo.client.R;
import com.urgoo.common.APPManagerTool;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.db.InviteMessgeDao;
import com.urgoo.domain.NetHeaderInfoEntity;
import com.urgoo.domain.UserInfoEntity;
import com.urgoo.profile.activities.UrgooVideoActivity;
import com.zw.express.tool.GsonTools;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class ConversationListFragment extends EaseConversationListFragment implements ExpansionInterface {


    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }

    @Override
    public void onResume() {
        super.onResume();
        //杨德成 20150504 是否显示红点
        iconSwitch();
    }
    @Override
    protected void setUpView() {
        super.setUpView();
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }
                        
                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
        //杨德成 20160429 新增头部布局
        re_Information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        //杨德成 20160505 新增回调接口
        setCallBackInterface(this);


    }

    //杨德成 20160516 重写方法
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {

            getZOOMInfo();
            iconSwitch();
        }
    }


    //杨德成20160801 获取ZOOM房间;接受或拒绝顾问端发起的视频邀请 0:用户未操作，1：接受，2：拒绝
    private void getZOOMInfo() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(getActivity()).getToken());
        //params.put("token", "p2OdthB5P+A=");
        params.put("termType", "2");
        OkHttpClientManager.postAsyn(ZWConfig.Action_getZoomRoom,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        //UiUtil.show(ZhiBodDetailActivity.this, "网络链接失败，请确定网络连接后重试！");
                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);
                            android.util.Log.d("test123",j.toString());
                            NetHeaderInfoEntity hentity= BaseService.getNetHeadInfo(j);
                            if (hentity.getCode().equals("200")) {

                                String zoomId= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("zoomId");
                                String nickname= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("nickname");
                                String pic= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("pic");
                                String zoomNo= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("zoomNo");
                                String status= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("status");
                                //notificationIntent.putExtra(MainActivity.EXTRA_TAB, 1);
                                if(!status.equals("2")){
                                    if(!APPManagerTool.isActivityRunning(getContext().getApplicationContext(),"UrgooVideoActivity")){
                                        Intent it= new Intent(getActivity(), UrgooVideoActivity.class);
                                        it.putExtra("icon", pic);
                                        it.putExtra("name", nickname);
                                        it.putExtra("zoomId", zoomId);
                                        it.putExtra("zoomNo", zoomNo);
                                        //it.putExtra("hxCode", "");
                                        startActivity(it);
                                    }
                                }

                            } else if (hentity.getCode().equals("404")) {
                                // UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());
                            }
                            if (hentity.getCode().equals("400")) {
                                //UiUtil.show(ZhiBodDetailActivity.this,hentity.getMessage());
                            }
                        } catch (JSONException e) {
                            // UiUtil.show(ZhiBodDetailActivity.this, "网络状态不佳，请刷新后重试！");
                        }
                    }
                }
                , params);
    }

    /**
     * 杨德成 20150504 是否显示红点
     */
    private  void iconSwitch(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", SPManager.getInstance(getActivity()).getToken());
        params.put("termType","2");

        OkHttpClientManager.postAsyn(ZWConfig.Action_selectInformationCount,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();

                        Toast.makeText(getActivity(),
                                "网络请求失败，请稍后重试", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String respon) {
                        //mTv.setText(u);// 注意这里是UI线程

                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message=new JSONObject(j.get("header").toString()).getString("message");
                            String count=new JSONObject(j.get("body").toString()).getString("count");
                            if(Integer.parseInt(count)>0){
                                iv_notification_icon.setVisibility(View.VISIBLE);
                            }else {
                                iv_notification_icon.setVisibility(View.GONE);
                            }
                            //String userLoginId=new JSONObject(j.get("body").toString()).getString("userLoginId");
                            //String hxid=new JSONObject(j.get("body").toString()).getString("userHxCode");
                           /* if (!RetrievePwdActivity.this.isFinishing() && pd.isShowing())
                                pd.dismiss();
                            if (code.equals("200")) {
                                Toast.makeText(RetrievePwdActivity.this,
                                        "success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RetrievePwdActivity.this, RetrievePwd2Email.class));
                                RetrievePwdActivity.this.finish();
                                overridePendingTransition(R.anim.scale_rotate,
                                        R.anim.my_alpha_action);
                                //verify_ev.setText(text);
                            } else if (code.equals("404")) {
                                Toast.makeText(RetrievePwdActivity.this,
                                        message, Toast.LENGTH_LONG).show();
                            } else if (code.equals("400")) {
                                Toast.makeText(RetrievePwdActivity.this,
                                        message, Toast.LENGTH_LONG).show();
                            }  else {
                                Toast.makeText(RetrievePwdActivity.this,
                                        new JSONObject(j.get("header").toString()).getString("message"), Toast.LENGTH_LONG).show();
                            }
*/
                        } catch (JSONException e) {
                            // TODO 自动生成的 catch 块
                          /*  if (!RetrievePwdActivity.this.isFinishing()) {
                                pd.dismiss();
                            }*/
                            e.printStackTrace();
                        }
                    }
                }

                , params);
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
         errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
          errorText.setText(R.string.the_current_network);
        }
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
    	EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
    	if (tobeDeleteCons == null) {
    	    return true;
    	}
        try {
            // 删除此会话
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        return true;
    }

    @Override
    public void SettingAvatarAndnickname(String mgs, final  TextView name, final ImageView avatar, EMConversation conversation) {
        /*Toast.makeText(getActivity(),
                "网络请求失败，请稍后重试", Toast.LENGTH_LONG).show();*/

        /* UiUtil.show(getActivity(),  mgs);
        name.setText(conversation.getUserName()+"    666");*/

        Map<String, String> maps = new HashMap<String, String>();
        maps.put("token", SPManager.getInstance(getActivity()).getToken());
        maps.put("userHxCodeString",conversation.getUserName());
        maps.put("termType","1");

        OkHttpClientManager.postAsyn(ZWConfig.Action_getAvatarAndnickname, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                //d("ProfileFragment", response);
                JSONObject jsonObject = null;
                JSONArray ja=null;
                Map<String, String> maps = new HashMap<String, String>();
                try {
                    jsonObject = new JSONObject(response).getJSONObject("body");
                    ja = jsonObject.getJSONArray("userInfoList");
                    UserInfoEntity user= GsonTools.getTargetClass(ja.getJSONObject(0).toString(), UserInfoEntity.class);
                    //name.setText(user.getEnName());
                    if(user.getRoleTyp().equals("1")){
                        //UiUtil.show(ChatActivity.this,  "正和顾问聊天");
                        //titleBar.setTitle(user.getEnName());
                        name.setText(user.getEnName());
                    }else if(user.getRoleTyp().equals("2")){
                        //UiUtil.show(ChatActivity.this,  "正和家长聊天");
                        name.setText(user.getNickName());
                    }else if(user.getRoleTyp().equals("3")){
                        name.setText(user.getCnName());
                    }
                    //ImageLoaderUtil.displayImage(ZWConfig.URGOOURL_BASE + user.getUserIcon(),avatar);
                   if(!user.getUserIcon().equals("")||user.getUserIcon()!=null){
                        imageLoadBusiness.imageLoadByNewURL(user.getUserIcon(),avatar);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, maps);
    }

    @Override
    public void SettingChatnickname(String mgs, TextView name, String conversationId) {

    }

    @Override
    public void SettingChatAvatar(String mgs, TextView name, ImageView avatar, String conversationId) {

    }

    @Override
    public void SettingChatTitle(EaseTitleBar titleBar, String conversationId) {

    }

    public void SettingChatAvatarClick(String conversationId) {

    }
}
