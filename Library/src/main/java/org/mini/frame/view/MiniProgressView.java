package org.mini.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * 感谢 霍永刚先生
 *
 * 自定义进度条显示 用以显示百分比
 */
public class MiniProgressView extends View {
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int progress, color;
    private Context context;
    private int height;
    private Float fontSize;
    private int textSpacing;

    public MiniProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initSizes();
    }

    public MiniProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initSizes();
    }

    public MiniProgressView(Context context) {
        super(context);
        this.context = context;
        initSizes();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(color);

        String text = progress + "%";
        Rect rect = new Rect();
        Paint mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(fontSize);

        RectF rectProgressBg = new RectF(0, 0, (getWidth() / 120) *  (progress > 0 ? progress : 2), getHeight());
        canvas.drawRoundRect(rectProgressBg, height, height, mRectPaint);
        canvas.drawText(text, (getWidth() / 120) * (progress > 0 ? progress : 2) + textSpacing, getHeight() + rect.centerY() / 2, mTextPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        Paint paint = new Paint();
        Rect rect = new Rect();
        paint.setTextSize(fontSize);
        paint.getTextBounds("你", 0, 1, rect);
        setMeasuredDimension(specSize, rect.height());
    }

    private void initSizes() {
        WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        height = Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics)).intValue();
        fontSize = Float.valueOf(TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, 13, displayMetrics));
        textSpacing = Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics)).intValue();
    }

    /**
     * 设置一些参数
     *
     * @param progress 进度
     * @param color    颜色值
     */
    public void setParams(int progress, int color) {
        this.progress = progress;
        this.color = color;
    }

}
