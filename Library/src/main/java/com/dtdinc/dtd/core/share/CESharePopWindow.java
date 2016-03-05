package com.dtdinc.dtd.core.share;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mini.R;

/**
 * 分享弹出框
 *
 * @author 霍永刚
 * @data 2015-8-14
 */
public class CESharePopWindow extends PopupWindow implements OnClickListener {
    private Context context;
    private ShareItemSelectedListener itemSelectedListener;

    public CESharePopWindow(Context context) {
        this.context = context;
        initViews();
    }

    public interface ShareItemSelectedListener {
        void shareItemSelected(Boolean isShareWeixin);
    }

    private void initViews() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.share_popup_main, null);
        ImageView ivWXFriend = (ImageView) contentView.findViewById(R.id.share_popup_iv_wx_friend);
        ivWXFriend.setOnClickListener(this);
        ImageView ivWXPYQ = (ImageView) contentView.findViewById(R.id.share_popup_iv_pyq);
        ivWXPYQ.setOnClickListener(this);
        Button bt_cancel = (Button) contentView.findViewById(R.id.share_popup_bt_cancel);
        RelativeLayout relative = (RelativeLayout) contentView.findViewById(R.id.share_popup_relativelayout);
        bt_cancel.setOnClickListener(this);
        relative.setOnClickListener(this);
        View line = contentView.findViewById(R.id.share_popup_view);
            line.setBackgroundColor(context.getResources().getColor(R.color.green));
        setFocusable(true);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(contentView);
    }

    public void setItemSelectedListener(ShareItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.share_popup_iv_wx_friend:
                if (itemSelectedListener != null)
                    itemSelectedListener.shareItemSelected(false);
                break;

            case R.id.share_popup_iv_pyq:
                if (itemSelectedListener != null)
                    itemSelectedListener.shareItemSelected(true);
                break;

            case R.id.share_popup_bt_cancel:

                break;
        }
        dismiss();

    }

}
