package org.mini.frame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.R;

/**
 * Created by Wuquancheng on 15/5/3.
 */
public class MiniImageButton extends LinearLayout {

    private ImageView imageView;

    private ImageView badgeImageView;

    private TextView textView;

    public MiniImageButton(Context context) {
        super(context);
        this.initViews();
    }

    public MiniImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViews();
        this.initAttrs(attrs);
    }

    private void initViews() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.mini_view_image_button,null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        this.addView(view,params);
        imageView = (ImageView)view.findViewById(R.id.button_image);
        badgeImageView = (ImageView)view.findViewById(R.id.class_message_badge);
        textView = (TextView)view.findViewById(R.id.button_title);
        badgeImageView.setVisibility(View.GONE);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MiniImageButton);
        String text = typedArray.getString(R.styleable.MiniImageButton_text);
        int imageId = typedArray.getResourceId(R.styleable.MiniImageButton_image_src_id, 0);
        imageView.setImageResource(imageId);
        textView.setText(text);
    }

    public void setBadge() {
        badgeImageView.setVisibility(View.VISIBLE);
    }

    public void removeBadge() {
        badgeImageView.setVisibility(View.GONE);
    }
}
