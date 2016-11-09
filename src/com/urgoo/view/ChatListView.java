package com.urgoo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/7/20.
 */
public class ChatListView extends ListView {
    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    public ChatListView(Context context) {
        // TODO Auto-generated method stub
        super(context);
    }

    public ChatListView(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }

    public ChatListView(Context context, AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return mGestureDetector.onTouchEvent(ev);

    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //判断是否上下滑动
            if (Math.abs(distanceY) / Math.abs(distanceX) > 2) {
                return true;//截获事件
            }
            return false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
