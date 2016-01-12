package org.mini.frame.toolkit.media;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by admin on 2015/7/13.
 */
public class MiniAnimation {

    // 选择图片动画
    public static void addAnimation(View view) {
        float[] values = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f,
                1.0f };
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", values), ObjectAnimator.ofFloat(view, "scaleY", values));
        set.setDuration(150);
        set.start();
    }
}
