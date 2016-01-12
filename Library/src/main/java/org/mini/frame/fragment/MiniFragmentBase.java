package org.mini.frame.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mini.app.CEApplication;

import org.mini.frame.activity.base.MiniActivityBase;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.log.MiniLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Wuquancheng on 15/4/7.
 */
@SuppressLint("ValidFragment")
public class MiniFragmentBase extends Fragment{

    public MiniFragmentBase() {
    }

    public MiniFragmentBase(Bundle bundle) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void showErrorMessage(Exception error) {
        Toast.makeText(CEApplication.instance().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    protected <T extends Annotation> Method findMethodForActionId(int id, Class<T> clazz) {
        return MiniActivityBase.findMethodForActionId(this.getClass(), id, clazz);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Method method = findMethodForActionId(requestCode, ActivityResult.class);
        if (method != null) {
            try {
                method.invoke(this,requestCode,resultCode,data);
            } catch (Exception e) {
                MiniLogger.get().e("",e);
            }
            return;
        }
    }
}
