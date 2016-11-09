package com.urgoo.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.urgoo.client.R;


public final class ErasableEditText extends EditText implements TextWatcher {
	private Drawable mClearDrawable;

	private int clearWidth;
	private OnClearTextListener clearTextListener;
	public OnClearTextListener getClearTextListener() {
		return clearTextListener;
	}

	public void setClearTextListener(OnClearTextListener clearTextListener) {
		this.clearTextListener = clearTextListener;
	}

	public ErasableEditText(Context ctx) {
		super(ctx);
		init();
	}

	public ErasableEditText(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init();
	}

	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		if (this.isFocused() && this.isEnabled()) {
			if (clearWidth > 0) {
				int sx = (int) event.getX();
				int padding = getPaddingRight();
				if ((sx > (getWidth() - clearWidth - padding))&& (sx < (getWidth() - (padding >> 1)))) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						this.setText("");
						if(clearTextListener != null)
							clearTextListener.onClear();
					}
					return true;
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected final void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if (gainFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	@Override
	public final void onTextChanged(CharSequence s, int start, int count,
			int after) {
		setClearIconVisible(s.length() > 0);
	}

	@Override
	public final void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public final void afterTextChanged(Editable s) {

	}

	/**
	 * 初始化绘图资源
	 */
	private void init() {
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(R.drawable.ic_clear);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());

		addTextChangedListener(this);
	}

	/**
	 * 变化显示效果
	 * 
	 * @param visible
	 */
	private void setClearIconVisible(boolean visible) {
		Drawable[] drawables = getCompoundDrawables();
		if (visible) {
			drawables[2] = mClearDrawable;
			clearWidth = mClearDrawable.getIntrinsicWidth();
		} else {
			drawables[2] = null;
			clearWidth = 0;
		}
		setCompoundDrawables(drawables[0], drawables[1], drawables[2],
				drawables[3]);

	}
	
	public interface OnClearTextListener{
		void onClear();
	}
}
