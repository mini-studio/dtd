package org.mini.frame.activity.base;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.R;

import org.mini.frame.annotation.Action;
import org.mini.frame.application.MiniApplication;
import org.mini.frame.toolkit.MiniActivityManager;
import org.mini.frame.toolkit.MiniKeyboardUtils;
import org.mini.frame.toolkit.MiniShowDialogUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Wuquancheng on 15/4/5.
 */
public abstract class MiniFragmentActivityBase extends FragmentActivity implements View.OnClickListener{

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadView();
        View view = this.findViewById(R.id.app_title_back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    MiniKeyboardUtils.hideKeyboard();
                }
            });
        }
    }

    protected abstract void loadView();

    public void setTitle(String title) {
        View view = this.findViewById(R.id.app_center_title);
        if (view != null && view instanceof TextView) {
            ((TextView)view).setText(title);
        }
    }

    public void setTitle(int resId) {
        View view = this.findViewById(R.id.app_center_title);
        if (view != null && view instanceof TextView) {
            ((TextView)view).setText(getResources().getString(resId));
        }
    }

    protected void hiddenLeftBackButton() {
        View view = this.findViewById(R.id.app_title_back);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    protected void showError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    protected void showMessage(String string) {
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }


    public void showWaiting(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = MiniShowDialogUtils.loadingDialog(this);
        }
        else {
            mProgressDialog.show();
        }
        if (text != null) {
            MiniShowDialogUtils.setMessage(mProgressDialog, text);
        }
    }
    public void showWaiting() {
        showWaiting(null);
    }

    public void dismissWaiting() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void setWaitingText(String text) {
        if (text != null) {
            if (mProgressDialog == null) {
                mProgressDialog = MiniShowDialogUtils.loadingDialog(this, text);
            } else {
                MiniShowDialogUtils.setMessage(mProgressDialog, text);
            }
        }
    }

    protected Object getIntentObject() {
        Intent intent = this.getIntent();
        return MiniIntent.getObjectFromIntent(intent);
    }

    protected void setNaviRightStringAction(String title) {
        View view = this.findViewById(R.id.app_title_right, this);
        view.setVisibility(View.VISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.app_right_title);
        textView.setText(title);
            textView.setTextColor(getResources().getColor(R.color.common_green));
        textView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.app_right_image).setVisibility(View.GONE);
    }

    protected View findViewById(int id, View.OnClickListener listener) {
        View view = findViewById(id);
        if (view != null) {
            if (listener != null) {
                view.setOnClickListener(listener);
            }
        }
        return view;
    }

    public void onClick(View v) {
        int id = v.getId();
        try {
            Method method = findMethodForActionId(id, Action.class);
            if (method != null) {
                method.invoke(this);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Annotation> Method findMethodForActionId(int id, Class<T> clazz) {
        return MiniActivityBase.findMethodForActionId(this.getClass(), id, clazz);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MiniActivityManager.currentActivity = this;
        Application application = getApplication();
        if (application instanceof MiniApplication) {
            ((MiniApplication)application).onResume();
        }
    }

    @Override
    protected void onStop() {
        Application application = getApplication();
        if (application instanceof MiniApplication) {
            ((MiniApplication)application).onStop();
        }
        super.onStop();
    }
}
