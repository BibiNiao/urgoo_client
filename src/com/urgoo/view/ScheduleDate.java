package com.urgoo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.client.R;
import com.zw.express.tool.Util;

/**
 * Created by lijie on 2016/6/1.
 */
public class ScheduleDate extends FrameLayout implements OnClickListener {
    private TextView t;
    private ImageView img;
    private View vx;
    private String tag;
    private String json;
    private String advanceDate;
    private String type = "2";
    private boolean isChecked = false;
    private OnSheduleClickLinstener on;

    public ScheduleDate(Context context) {
        this(context, null);
    }

    public ScheduleDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        View v = LayoutInflater.from(context).inflate(R.layout.schedule_item, null);
//        LayoutParams layoutParams = new LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, (int) Util.dp2px(context, 40));
//        v.setLayoutParams(layoutParams);
//        t = (TextView) v.findViewById(R.id.time);
//        img = (ImageView) v.findViewById(R.id.checked);
//        vx = v.findViewById(R.id.line);
//        setOnClickListener(this);
//        addView(v);
    }

    public String getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(String advanceDate) {
        this.advanceDate = advanceDate;
    }

    public String getJson() {
        return String.format("{advanceDate=%s,advanceTimeId=%s}", advanceDate, tag);
    }


    public void setText(String str) {
        t.setText(str);
    }

    //变颜色
    public void setStatus() {

    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if ("2".equals(type)) {
            img.setBackgroundResource(R.drawable.xx);
        } else if ("1".equals(type)) {
            img.setBackgroundResource(R.drawable.yy);
        }
    }

    public void setLineVisibility(int visibility) {
        vx.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if (on != null) {
            isChecked = !isChecked;
            if (isChecked) {
                img.setBackgroundResource(R.drawable.oo);
            } else {
                img.setBackgroundResource(R.drawable.yy);
            }
            on.onClick(v, tag, isChecked);
        }
    }

    public void yy() {
        img.setBackgroundResource(R.drawable.yy);
    }

    public void setOnSheduleClickLinstener(OnSheduleClickLinstener on) {
        if ("1".equals(type)) {
            this.on = on;
        }
    }

    public interface OnSheduleClickLinstener {
        void onClick(View v, String tag, boolean isChecked);
    }
}
