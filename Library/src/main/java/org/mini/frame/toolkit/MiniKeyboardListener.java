package org.mini.frame.toolkit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class MiniKeyboardListener extends RelativeLayout {

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;

    private boolean mHasInit = false;
    private boolean mHasKeyboard = false;
    private int mHeight;

    private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;

    public interface IOnKeyboardStateChangedListener {
        void onKeyboardStateChanged(int state);
    }

    public MiniKeyboardListener(Context context) {
        super(context);
    }

    public MiniKeyboardListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniKeyboardListener(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnKeyboardStateChangedListener(IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {
        this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
            if (onKeyboardStateChangedListener != null) {
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_INIT);
            }
        } else {
            mHeight = mHeight < b ? b : mHeight;
        }

        if (mHasInit && mHeight > b) {
            if (onKeyboardStateChangedListener != null) {
                if (!mHasKeyboard) {
                    onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
                }
            }
            mHasKeyboard = true;
        }
        if (mHasInit && mHasKeyboard && mHeight == b) {
            if (onKeyboardStateChangedListener != null) {
                if (mHasKeyboard) {
                    onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
                }
            }
            mHasKeyboard = false;
        }
    }


}