package org.mini.frame.toolkit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.R;

/**
 * Created by gassion on 15/4/15.
 */
public class MiniShowDialogUtils {
    public static ProgressDialog loadingDialog(Context context, String... content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.mini_loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        final ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
//        spaceshipImage.setImageResource(R.drawable.loading_animation);
//        final AnimationDrawable animationDrawable = (AnimationDrawable) spaceshipImage.getDrawable();
//        animationDrawable.start();

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.mini_loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

        if (content != null && content.length > 0) {
            tipTextView.setText(content[0]);// 设置加载信息
        } else {
            tipTextView.setText("努力加载中...");// 设置加载信息
        }
        ProgressDialog mProgressDialog = new ProgressDialog(context, R.style.mini_loading_dialog);
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);// 不可以用“返回键”取消
        mProgressDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                spaceshipImage.clearAnimation();
                //animationDrawable.stop();
            }
        });
        return mProgressDialog;
    }

    public static void setMessage(ProgressDialog progressDialog, String message) {
        TextView tipTextView = (TextView) progressDialog.findViewById(R.id.tipTextView);
        if (tipTextView != null) {
            tipTextView.setText(message);
        }
    }
}
