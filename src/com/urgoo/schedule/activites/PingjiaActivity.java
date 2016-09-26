package com.urgoo.schedule.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class PingjiaActivity extends BaseActivity {
    private String advanceId;
    private String TAG = "cancell";
    private String str_EditText = "未填写";
    private String str;
    private LinearLayout LinLyout_schedule_back;
    private RatingBar ratingBar_z1_score;
    private EditText EditTexts;
    private float mRating;
    private Button but_ppw_cancel_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingjia);
        Intent mIntent = getIntent();
        if (mIntent.getStringExtra("advanceId") != null) {
            advanceId = mIntent.getStringExtra("advanceId");
        } else {
            Toast.makeText(PingjiaActivity.this, "网络繁忙，请稍后2秒再试！", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "  传参错误  ");
        }
        initviews();

        ratingBar_z1_score.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mRating = ratingBar_z1_score.getRating();
            }
        });

        EditTexts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                str=s.toString();
            }
        });

        LinLyout_schedule_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        but_ppw_cancel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });

    }


    private void getdata() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", spManager.getToken());
        params.put("advanceId",advanceId);
        params.put("star", ((int)mRating)+"");
        if(str!=null){
            params.put("comment", str);
        }else
            params.put("comment", str_EditText);
        OkHttpClientManager.postAsyn(ZWConfig.URL_advanceComment2
                , new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject j = new JSONObject(response);
                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            if (code.equals("200")) {
                                UiUtil.show(PingjiaActivity.this, "评价成功");
                                startActivity(new Intent(PingjiaActivity.this,PrecontractMyOrder.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            UiUtil.show(PingjiaActivity.this, "评价失败");
                        }
                    }
                }, params);
       }
    private void initviews() {
        ratingBar_z1_score=(RatingBar)findViewById(R.id.ratingBar_z1_score);
        LinLyout_schedule_back=(LinearLayout)findViewById(R.id.LinLyout_schedule_back);
        EditTexts=(EditText)findViewById(R.id.EditTexts);
        but_ppw_cancel_submit=(Button)findViewById(R.id.but_ppw_cancel_submit);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
