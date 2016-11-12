package com.urgoo.live.view;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.urgoo.client.R;
import com.zw.express.tool.Util;

/**
 * Created by bb on 2016/10/14.
 */

public class CommentInputBox extends RelativeLayout {
    // 输入框
    private EditText etInput;
    // 发送按钮
    private Button btnSend;

    private Context context;

    private CommentInputToolBoxListener listener;
    /**
     * 评论对象的id
     */
    private String replyTarget;

    public CommentInputBox(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.comment_input_tool_box,
                this);
    }

    public CommentInputBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.comment_input_tool_box,
                this);
    }

    public CommentInputBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.comment_input_tool_box,
                this);
    }

    /**
     * 设置输入框的提示文本
     *
     * @return
     */
    public void setInputHint(CharSequence hint) {
        this.etInput.setHint(hint);
    }

    /**
     * 获取输入框的提示文本
     *
     * @return
     */
    public CharSequence getInputHint() {
        return this.etInput.getHint();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.initView();
    }

    private void initView() {
        etInput = (EditText) findViewById(R.id.et_input);
        etInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !"".equals(s.toString().trim())) {
                    btnSend.setEnabled(true);
                } else {
                    btnSend.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSend(etInput.getText().toString().trim());
                }
            }
        });
    }

    private final KeyEvent keyEventDelete = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);

    public void setCommentInputToolBoxListener(CommentInputToolBoxListener listener) {
        this.listener = listener;
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     *
     * @param context
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            IBinder token = activity.getCurrentFocus().getWindowToken();
            if (token != null) {
                imm.showSoftInputFromInputMethod(token, 0);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void setReplyTarget(String replyTarget) {
        this.replyTarget = replyTarget;
    }

    public String getReplyTarget() {
        return replyTarget;
    }

    public void loseFocus() {
        hideKeyboard(context);
        requestFocus();
    }

    public void showReplyInputToolBox() {
        etInput.requestFocus();
        Util.popSoftKeyBoard(context, etInput);
    }

    public void resetToolBox() {
        setReplyTarget("0");
        this.etInput.setText("");
    }
}