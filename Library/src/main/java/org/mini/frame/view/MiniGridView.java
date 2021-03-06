package org.mini.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by huangqihua on 2015/6/8.
 */
public class MiniGridView extends GridView {
    public MiniGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniGridView(Context context) {
        super(context);
    }

    public MiniGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
