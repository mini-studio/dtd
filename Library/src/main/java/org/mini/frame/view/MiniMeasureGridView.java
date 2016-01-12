package org.mini.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by gassion on 15/4/15.
 */
public class MiniMeasureGridView extends GridView {

    public MiniMeasureGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniMeasureGridView(Context context) {
        super(context);
    }

    public MiniMeasureGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);


    }

}