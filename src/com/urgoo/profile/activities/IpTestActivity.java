package com.urgoo.profile.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.zw.express.tool.UiUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.zoom.sdk.MeetingService;

/**
 * Created by Administrator on 2016/5/17.
 */
public class IpTestActivity extends ActivityBase  {

    private final static String TAG = "Zoom SDK Example";

    private EditText mEdtMeetingNo;
    private EditText mEdtMeetingPassword;

    private final static int STYPE = MeetingService.USER_TYPE_ZOOM;
    private final static String DISPLAY_NAME = "ZoomUS SDK";


    private EditText CurrentMeetingNo;

    private Button submit_btn;

    private  EditText edtport,edt_ip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ipseting_layout);

        back(R.id.back, this);
        initView();

    }


    @Override
    public void initView() {


        edtport=(EditText)findViewById(R.id.edtport);
        edt_ip=(EditText)findViewById(R.id.edt_ip);

        submit_btn=(Button)findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipStr= edt_ip.getText().toString();
                String portStr= edtport.getText().toString();

                if(isIP(ipStr)){
                    if(portStr.equals("")||portStr==null){
                        //ZWConfig.URGOOURL_BASE="http://"+ipStr+"/urgoo/";
                    }else{
                        if(portStr.length()==4){
                            //ZWConfig.URGOOURL_BASE="http://"+ipStr+":"+portStr+"/urgoo/";
                        }else{
                            UiUtil.show(IpTestActivity.this,"请输入四位端口号");
                        }

                        UiUtil.show(IpTestActivity.this,"设置成功");
                        finish();


                    }
                    UiUtil.show(IpTestActivity.this,"设置成功");
                    finish();
                    Log.d("baseURL","URGOOURL_BASE:"+ZWConfig.URGOOURL_BASE);
                }else{
                    UiUtil.show(IpTestActivity.this,"请输入合法的IP地址");
                }


            }
        });

    }

    public boolean isIP(String addr)
    {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }
}


