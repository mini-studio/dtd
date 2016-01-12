package com.mini.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.R;

/**
 * Created by Wuquancheng on 15/7/16.
 */
public class ExpandTextView extends TextView {

    private boolean mExpand;

    private ImageView mIndicator;

    private LinearLayout layout;

    public ExpandTextView(Context context) {
        super(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setIndicatorView(ImageView indicator,LinearLayout layout) {
        this.mIndicator = indicator;
        this.layout = layout;
    }

    /**
     * 展开
     */
    public void expand() {
        mExpand = true;
        setSingleLine(false);
        setTextColor(Color.parseColor("#999999"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setMinLines(getLineCount());
                setMaxLines(Integer.MAX_VALUE);
                doAnimate();
            }
        }, 100);
    }

    public void transform() {
        if (mExpand) {
            collapse();
        }
        else {
            expand();
        }
    }

    /**
     * 关闭
     */
    public void collapse() {
        mExpand = false;
        setTextColor(getResources().getColor(R.color.common_text_green));
        setSingleLine(true);
        setMinLines(1);
        setMaxLines(1);
        doAnimate();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void doAnimate() {
        ValueAnimator animator = null;
        if(mExpand) {
            animator = ObjectAnimator.ofFloat(mIndicator, "rotation", 0, 180);
        }else{
            animator = ObjectAnimator.ofFloat(mIndicator, "rotation", 180, 0);
        }
        animator.setDuration(300);
        animator.start();
    }
}
