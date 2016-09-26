package com.urgoo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urgoo.client.R;

/**
 * 首页底部Tab组件【包含消息气泡】
 * 
 *
 */
public class TabView extends RelativeLayout {
	private Context mContext;
	private TextView tvTitle;
	private TextView tvNum;
	private ImageView ivNewIndicator;
	
	public TabView(Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.tab_view, this);
	}

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.tab_view, this);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initView();
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvNum = (TextView) findViewById(R.id.tv_num);
		ivNewIndicator = (ImageView) findViewById(R.id.iv_new_indicator);
	}
	
	/**
	 * 设置title
	 * 
	 * @param title
	 * @param drawableTop
	 */
	public void setTextAndDrawableTop(int title, int drawableTop) {
		tvTitle.setText(title);
		Drawable drawable = mContext.getResources().getDrawable(drawableTop);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		tvTitle.setCompoundDrawables(null, drawable, null, null);
	}
	
//	/**
//	 * 设置未读消息数量
//	 *
//	 * @param count
//	 *
//	 */
//	public void setUnreadCount(long count) {
//		if (count > 0) {
//			tvNum.setText(Utils.formatUnreadMessageCount(count));
//			tvNum.setVisibility(View.VISIBLE);
//		} else {
//			tvNum.setVisibility(View.GONE);
//		}
//	}
	
	/**
	 * 设置新功能标识
	 * @param display
	 */
	public void setNewIndicator(boolean display) {
		if (display) {
			ivNewIndicator.setVisibility(View.VISIBLE);
		} else {
			ivNewIndicator.setVisibility(View.GONE);
		}
	}
	
	public boolean isDisplayNewIndicator() {
		return ivNewIndicator.getVisibility() == View.VISIBLE;
	}
	
	/**
	 * 设置tab是否开启
	 * 
	 * @param enabled
	 */
	public void setTabEnable(boolean enabled) {
		setEnabled(enabled);
		tvTitle.setEnabled(enabled);
	}

}
