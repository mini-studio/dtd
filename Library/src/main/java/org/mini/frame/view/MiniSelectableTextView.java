package org.mini.frame.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Wuquancheng on 15/6/24.
 */
public class MiniSelectableTextView extends EditText {


    private int off = 0;
    private boolean selecting = false;
    public MiniSelectableTextView(Context context) {
        super(context);
        init();
    }

    public MiniSelectableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MiniSelectableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MiniSelectableTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {
        this.setAutoLinkMask(Linkify.ALL);
        this.setTextIsSelectable(true);
        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        this.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        this.setMovementMethod(LinkMovementMethod.getInstance());
        this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                showSelectionModifierCursorController();
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    private void showSelectionModifierCursorController(){
        try {
            Field mEditor = TextView.class.getDeclaredField("mEditor");
            mEditor.setAccessible(true);
            Object object= mEditor.get(this);
            Class mClass=Class.forName("android.widget.Editor");
            Method method=mClass.getDeclaredMethod("getSelectionController");//取得方法  getSelectionController
            method.setAccessible(true);//取消访问私有方法的合法性检查
            Object resultObject=method.invoke(object);//调用方法，返回SelectionModifierCursorController类的实例
            Method show=resultObject.getClass().getDeclaredMethod("show");//查找 SelectionModifierCursorController类中的show方法
            show.invoke(resultObject);//执行SelectionModifierCursorController类的实例的show方法
            selecting = true;
            showActionMenu();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideSelectionModifierCursorController() {
        try {
            Field mEditor = TextView.class.getDeclaredField("mEditor");
            mEditor.setAccessible(true);
            Object object = mEditor.get(this);
            Class mClass = Class.forName("android.widget.Editor");
            Method method = mClass.getDeclaredMethod("getSelectionController");//取得方法  getSelectionController
            method.setAccessible(true);//取消访问私有方法的合法性检查
            Object resultObject = method.invoke(object);//调用方法，返回SelectionModifierCursorController类的实例
            Method show = resultObject.getClass().getDeclaredMethod("hide");//查找 SelectionModifierCursorController类中的show方法
            show.invoke(resultObject);//执行SelectionModifierCursorController类的实例的show方法
            selecting = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showActionMenu() {

    }

    private void hideActionMenu() {

    }

    protected boolean getDefaultEditable() {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if (selecting) {
                    hideSelectionModifierCursorController();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (selecting) {
                    Layout layout = getLayout();
                    if (hasSelection()) {
                        int start = getSelectionStart();
                        int end = getSelectionEnd();
                        Selection.setSelection(getEditableText(), start, end);
                    }
                    else {
                        int line = layout.getLineForVertical(getScrollY()+(int)event.getY());
                        int curOff = layout.getOffsetForHorizontal(line, (int)event.getX());
                        Selection.setSelection(getEditableText(), off, curOff);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }
}
