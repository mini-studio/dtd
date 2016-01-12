package org.mini.frame.toolkit;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.mini.R;

/**
 * Created by gassion on 15/7/10.
 */
public class MiniCustomToggleButton extends View {
    private SpringSystem springSystem;
    private Spring spring;
    private float radius;
    private int onColor;
    private int offBorderColor = Color.parseColor("#dadbda");
    private int offColor = Color.parseColor("#ffffff");
    private int spotColor = Color.parseColor("#ffffff");
    private int borderColor;
    private Paint paint;
    private boolean toggleOn;
    private int borderWidth;
    private float centerY;
    private float startX;
    private float endX;
    private float spotMinX;
    private float spotMaxX;
    private int spotSize;
    private float spotX;
    private float offLineWidth;
    private RectF rect;
    private boolean defaultAnimate;
    private MiniCustomToggleButton.OnToggleChanged listener;
    SimpleSpringListener springListener;

    private MiniCustomToggleButton(Context context) {
        super(context);
            this.onColor=this.getResources().getColor(R.color.common_blue);
        this.borderColor = this.offBorderColor;
        this.toggleOn = false;
        this.borderWidth = 1;
        this.rect = new RectF();
        this.defaultAnimate = true;
        this.springListener = new SimpleSpringListener() {
            public void onSpringUpdate(Spring spring) {
                double value = spring.getCurrentValue();
                MiniCustomToggleButton.this.calculateEffect(value);
            }
        };
    }

    public MiniCustomToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
            this.onColor=this.getResources().getColor(R.color.common_blue);
        this.borderColor = this.offBorderColor;
        this.toggleOn = false;
        this.borderWidth = 1;
        this.rect = new RectF();
        this.defaultAnimate = true;
        this.springListener = new SimpleSpringListener() {
            public void onSpringUpdate(Spring spring) {
                double value = spring.getCurrentValue();
                MiniCustomToggleButton.this.calculateEffect(value);
            }
        };
        this.setup(attrs);
    }

    public MiniCustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
            this.onColor=this.getResources().getColor(R.color.common_blue);
        this.borderColor = this.offBorderColor;
        this.toggleOn = false;
        this.borderWidth = 1;
        this.rect = new RectF();
        this.defaultAnimate = true;
        this.springListener = new SimpleSpringListener() {
            public void onSpringUpdate(Spring spring) {
                double value = spring.getCurrentValue();
                MiniCustomToggleButton.this.calculateEffect(value);
            }
        };
        this.setup(attrs);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.spring.removeListener(this.springListener);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.spring.addListener(this.springListener);
    }

    public void setup(AttributeSet attrs) {
        this.paint = new Paint(1);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.springSystem = SpringSystem.create();
        this.spring = this.springSystem.createSpring();
        this.spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(50.0D, 7.0D));
        this.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                MiniCustomToggleButton.this.toggle(MiniCustomToggleButton.this.defaultAnimate);
            }
        });
        TypedArray typedArray = this.getContext().obtainStyledAttributes(attrs, com.zcw.togglebutton.R.styleable.ToggleButton);
        this.offBorderColor = typedArray.getColor(com.zcw.togglebutton.R.styleable.ToggleButton_offBorderColor, this.offBorderColor);
        this.onColor = typedArray.getColor(com.zcw.togglebutton.R.styleable.ToggleButton_onColor, this.onColor);
        this.spotColor = typedArray.getColor(com.zcw.togglebutton.R.styleable.ToggleButton_spotColor, this.spotColor);
        this.offColor = typedArray.getColor(com.zcw.togglebutton.R.styleable.ToggleButton_offColor, this.offColor);
        this.borderWidth = typedArray.getDimensionPixelSize(com.zcw.togglebutton.R.styleable.ToggleButton_borderWidth, this.borderWidth);
        this.defaultAnimate = typedArray.getBoolean(com.zcw.togglebutton.R.styleable.ToggleButton_animate, this.defaultAnimate);
        typedArray.recycle();
        this.borderColor = this.offBorderColor;
    }

    public void toggle() {
        this.toggle(true);
    }

    public void toggle(boolean animate) {
        this.toggleOn = !this.toggleOn;
        this.takeEffect(animate);
        if(this.listener != null) {
            this.listener.onToggle(this.toggleOn);
        }

    }

    public void toggleOn() {
        this.setToggleOn();
        if(this.listener != null) {
            this.listener.onToggle(this.toggleOn);
        }

    }

    public void toggleOff() {
        this.setToggleOff();
        if(this.listener != null) {
            this.listener.onToggle(this.toggleOn);
        }

    }

    public void setToggleOn() {
        this.setToggleOn(true);
    }

    public void setToggleOn(boolean animate) {
        this.toggleOn = true;
        this.takeEffect(animate);
    }

    public void setToggleOff() {
        this.setToggleOff(true);
    }

    public void setToggleOff(boolean animate) {
        this.toggleOn = false;
        this.takeEffect(animate);
    }

    private void takeEffect(boolean animate) {
        if(animate) {
            this.spring.setEndValue(this.toggleOn?1.0D:0.0D);
        } else {
            this.calculateEffect(this.toggleOn?1.0D:0.0D);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Resources r = Resources.getSystem();
        if(widthMode == 0 || widthMode == -2147483648) {
            widthSize = (int) TypedValue.applyDimension(1, 50.0F, r.getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, 1073741824);
        }

        if(heightSize == 0 || heightSize == -2147483648) {
            heightSize = (int)TypedValue.applyDimension(1, 30.0F, r.getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, 1073741824);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = this.getWidth();
        int height = this.getHeight();
        this.radius = (float)Math.min(width, height) * 0.5F;
        this.centerY = this.radius;
        this.startX = this.radius;
        this.endX = (float)width - this.radius;
        this.spotMinX = this.startX + (float)this.borderWidth;
        this.spotMaxX = this.endX - (float)this.borderWidth;
        this.spotSize = height - 4 * this.borderWidth;
        this.spotX = this.toggleOn?this.spotMaxX:this.spotMinX;
        this.offLineWidth = 0.0F;
    }

    private int clamp(int value, int low, int high) {
        return Math.min(Math.max(value, low), high);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.rect.set(0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight());
        this.paint.setColor(this.borderColor);
        canvas.drawRoundRect(this.rect, this.radius, this.radius, this.paint);
        float spotR;
        if(this.offLineWidth > 0.0F) {
            spotR = this.offLineWidth * 0.5F;
            this.rect.set(this.spotX - spotR, this.centerY - spotR, this.endX + spotR, this.centerY + spotR);
            this.paint.setColor(this.offColor);
            canvas.drawRoundRect(this.rect, spotR, spotR, this.paint);
        }

        this.rect.set(this.spotX - 1.0F - this.radius, this.centerY - this.radius, this.spotX + 1.1F + this.radius, this.centerY + this.radius);
        this.paint.setColor(this.borderColor);
        canvas.drawRoundRect(this.rect, this.radius, this.radius, this.paint);
        spotR = (float)this.spotSize * 0.5F;
        this.rect.set(this.spotX - spotR, this.centerY - spotR, this.spotX + spotR, this.centerY + spotR);
        this.paint.setColor(this.spotColor);
        canvas.drawRoundRect(this.rect, spotR, spotR, this.paint);
    }

    private void calculateEffect(double value) {
        float mapToggleX = (float) SpringUtil.mapValueFromRangeToRange(value, 0.0D, 1.0D, (double) this.spotMinX, (double) this.spotMaxX);
        this.spotX = mapToggleX;
        float mapOffLineWidth = (float)SpringUtil.mapValueFromRangeToRange(1.0D - value, 0.0D, 1.0D, 10.0D, (double)this.spotSize);
        this.offLineWidth = mapOffLineWidth;
        int fb = Color.blue(this.onColor);
        int fr = Color.red(this.onColor);
        int fg = Color.green(this.onColor);
        int tb = Color.blue(this.offBorderColor);
        int tr = Color.red(this.offBorderColor);
        int tg = Color.green(this.offBorderColor);
        int sb = (int)SpringUtil.mapValueFromRangeToRange(1.0D - value, 0.0D, 1.0D, (double)fb, (double)tb);
        int sr = (int)SpringUtil.mapValueFromRangeToRange(1.0D - value, 0.0D, 1.0D, (double)fr, (double)tr);
        int sg = (int)SpringUtil.mapValueFromRangeToRange(1.0D - value, 0.0D, 1.0D, (double)fg, (double)tg);
        sb = this.clamp(sb, 0, 255);
        sr = this.clamp(sr, 0, 255);
        sg = this.clamp(sg, 0, 255);
        this.borderColor = Color.rgb(sr, sg, sb);
        this.postInvalidate();
    }

    public void setOnToggleChanged(MiniCustomToggleButton.OnToggleChanged onToggleChanged) {
        this.listener = onToggleChanged;
    }

    public boolean isAnimate() {
        return this.defaultAnimate;
    }

    public void setAnimate(boolean animate) {
        this.defaultAnimate = animate;
    }

    public interface OnToggleChanged {
        void onToggle(boolean var1);
    }
}

