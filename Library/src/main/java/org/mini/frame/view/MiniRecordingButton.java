package org.mini.frame.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Wuquancheng on 15/6/16.
 */
public class MiniRecordingButton extends Button {

    public interface MiniRecordingButtonStatusAdapter {
        void onMoveOut();
        void onMoveIn();
        void onTouchUp();
    }


    private MiniRecordingButtonStatusAdapter statusAdapter;

    public MiniRecordingButton(Context context) {
        super(context);
    }

    public MiniRecordingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniRecordingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MiniRecordingButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void moveOut() {
        if (this.statusAdapter != null) {
            this.statusAdapter.onMoveOut();
        }
    }

    public void moveIn() {
        if (this.statusAdapter != null) {
            this.statusAdapter.onMoveIn();
        }
    }

    public void touchUp() {
        if (this.statusAdapter != null){
            this.statusAdapter.onTouchUp();
        }
    }

    public void setStatusAdapter(MiniRecordingButtonStatusAdapter statusAdapter) {
        this.statusAdapter = statusAdapter;
    }
}
