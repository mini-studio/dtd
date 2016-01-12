package org.mini.frame.activity.base;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.annotation.PushAction;
import org.mini.frame.application.MiniApplication;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.MiniActivityManager;
import org.mini.frame.toolkit.MiniFilePath;
import org.mini.frame.toolkit.MiniKeyboardUtils;
import org.mini.frame.toolkit.MiniShowDialogUtils;

import com.mini.R;

import org.mini.frame.toolkit.MiniKeyboardListener;
import org.mini.frame.toolkit.MiniSystemHelper;
import org.mini.frame.toolkit.file.MiniFileManager;
import org.mini.frame.toolkit.media.MiniAudioPlayer;
import org.mini.frame.view.MiniActionSheetDialog;
import org.mini.frame.view.MiniRotateImageUtil;

import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/4/5.
 */
public abstract class MiniActivityBase extends Activity implements View.OnClickListener, SensorEventListener {

    protected MiniKeyboardListener listener;

    private ProgressDialog mProgressDialog;

    private final int REQUEST_SELECT_ALBUM = 8;   //  选择相册
    private final int REQUEST_SELECT_CAMERA = 9;  //  拍照

    private String mLocalHeaderName;
    private String mLocalHeaderPath;  //本地路径

    private AudioManager audioManager;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    public interface MiniPickAlbumImageListener {
        void onPickAlbumImage(String fileName);
    }

    public interface MiniPickCameraImageListener {
        void onPickCameraImage(Bitmap bitmap);
    }

    public interface MiniPickImageListener {
        void onPickImage(String file);
    }

    MiniPickAlbumImageListener pickAlbumImageListener;
    MiniPickCameraImageListener pickCameraImageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadView();
        didLoadView();
        setNaivLeftBackAction();
        registerSensor();
    }

    protected void onPause() {
        super.onPause();
        MiniAudioPlayer.instance().stop();
        mSensorManager.unregisterListener(this);
    }

    protected abstract void loadView();

    protected void didLoadView() {

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

    protected void setNaivLeftBackAction() {
        View view = this.findViewById(R.id.app_title_back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MiniKeyboardUtils.hideKeyboard(v);
                    onNaviLeftButtonAction();
                }
            });
        }
    }

    protected void setNaviRightStringAction(String title) {
        View view = this.findViewById(R.id.app_title_right, this);
        view.setVisibility(View.VISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.app_right_title);
        textView.setText(title);
        textView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.app_right_image).setVisibility(View.GONE);
    }

    protected void setNaviRightImageAction(int resId) {
        View view = this.findViewById(R.id.app_title_right, this);
        view.setVisibility(View.VISIBLE);
        view.findViewById(R.id.app_right_title).setVisibility(View.INVISIBLE);
        ImageView imageView = (ImageView) view.findViewById(R.id.app_right_image);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(resId);
    }

    public void onBackPressed() {
        willBack();
        super.onBackPressed();
    }

    protected void back() {
        willBack();
        finish();
    }

    protected void willBack() {

    }

    public void setTitle(String title) {
        View view = this.findViewById(R.id.app_center_title);
        if (view != null && view instanceof TextView) {
            ((TextView) view).setText(title);
            view.setVisibility(View.VISIBLE);
        }
        ImageView imageView = (ImageView)this.findViewById(R.id.title_mid_image_view);
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setTitleMidImage(int resId) {
        ImageView imageView = (ImageView)this.findViewById(R.id.title_mid_image_view);
        if (imageView != null) {
            imageView.setImageResource(resId);
            View view = this.findViewById(R.id.app_center_title);
            view.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public String getActivityTitle() {
        View view = this.findViewById(R.id.app_center_title);
        if (view != null && view instanceof TextView) {
            CharSequence charSequence = ((TextView) view).getText();
            if (charSequence != null && charSequence.length() > 0) {
                return charSequence.toString();
            }
        }
        return null;
    }

    public void setTitle(int resId) {
        View view = this.findViewById(R.id.app_center_title);
        if (view != null && view instanceof TextView) {
            String title = getResources().getString(resId);
            ((TextView) view).setText(title);
        }
    }

    public void hideRightButton() {
        View view = this.findViewById(R.id.app_right_title, this);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public void hideRightLayoutButton() {
        View view = this.findViewById(R.id.app_title_right, this);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public void setNaviRightTitleWithListener(String title, View.OnClickListener listener) {
        View view = setNaviRightTitle(title);
        view.setOnClickListener(listener);
        findViewById(R.id.app_title_right).setOnClickListener(listener);
    }

    public void setNaviRightImageWithListener(int resId, View.OnClickListener listener) {
        findViewById(R.id.app_right_title).setVisibility(View.GONE);
        ImageView imageView = (ImageView) findViewById(R.id.app_right_image);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(resId);
        View view = findViewById(R.id.app_title_right);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(listener);
    }

    public View setNaviRightTitle(String title) {
        View rightView = this.findViewById(R.id.app_title_right, this);
        rightView.setVisibility(View.VISIBLE);
        TextView view = (TextView) this.findViewById(R.id.app_right_title);
        view.setTextColor(getResources().getColor(R.color.common_green));
        view.setVisibility(View.VISIBLE);
        findViewById(R.id.app_right_image).setVisibility(View.GONE);
        view.setText(title);
        view.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(layoutParams);
        return view;
    }

    public void resetNaviLeftTitle(String title) {
        TextView view = (TextView) this.findViewById(R.id.app_title_left);
        view.setText(title);
    }

    public View setNaviLeftTitle(String title) {
        View leftView = this.findViewById(R.id.app_title_back, this);
        leftView.setVisibility(View.VISIBLE);
        TextView view = (TextView) this.findViewById(R.id.app_title_left);
        view.setVisibility(View.VISIBLE);
        findViewById(R.id.app_left_image).setVisibility(View.GONE);
        view.setText(title);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    public void setLeftTitleImage(int id) {
        ImageView imageView = (ImageView)this.findViewById(R.id.app_left_title_image);
        if (imageView != null) {
            imageView.setImageResource(id);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    protected void onNaviRightAction() {
    }

    @Action(R.id.app_title_right)
    protected void naviRightAction() {
        onNaviRightAction();
    }


    @Action(R.id.app_title_back)
    protected void naviBackAction() {
    }

    protected void onNaviLeftButtonAction() {
        back();
    }

    protected void hiddenLeftBackButton() {
        View view = this.findViewById(R.id.app_title_back);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    protected void setLeftBackground(int imagePath) {
        View view = this.findViewById(R.id.app_left_image);
        if (view != null) {
            view.setBackgroundResource(imagePath);
        }
    }

    public void showError(Exception e) {
        String message = e.getMessage();
        if (message == null) {
            message = e.toString();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    protected void showMessage(int resId) {
        Toast.makeText(this, getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected <T> T getIntentData() {
        return (T) getIntentObject();
    }

    protected <T> T getIntentValue(String key) {
        return (T) this.getIntent().getSerializableExtra(key);
    }

    protected Object getIntentObject() {
        Intent intent = this.getIntent();
        return MiniIntent.getObjectFromIntent(intent);
    }

    protected void startActivity(Class<?> clazz, Object data) {
        startActivityWithObject(clazz, data);
    }

    protected void startActivity(Class<?> clazz) {
        startActivityWithObject(clazz, null);
    }

    protected void startActivityWithObject(Class<?> clazz, Object data) {
        MiniIntent intent = new MiniIntent(this, clazz);
        if (data != null) {
            intent.setObject(data);
        }
        this.startActivity(intent);
    }

    protected String filePathForUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        return cursor.getString(columnIndex);
    }

    public void startActivityForResult(Class<?> clazz, Object data, int requestCode) {
        MiniIntent intent = new MiniIntent(this, clazz);
        intent.setObject(data);
        startActivityForResult(intent, requestCode);
    }

    protected void pickImage(final MiniPickImageListener imageListener) {
        List<String> subjects = new ArrayList<String>();
        subjects.add("从相册选择");
        subjects.add("拍照");
        final MiniActivityBase activityBase = this;
        new MiniActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true)
                .addSheetItem(subjects, new MiniActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 0) {
                            pickImageFromAlbum(new MiniPickAlbumImageListener() {
                                @Override
                                public void onPickAlbumImage(String fileName) {
                                    imageListener.onPickImage(fileName);
                                }
                            });
                        } else if (which == 1) {
                            pickImageFromCamera(new MiniPickCameraImageListener() {
                                @Override
                                public void onPickCameraImage(Bitmap bitmap) {
                                    try {
                                        String fname = MiniFileManager.saveBitmap(activityBase, bitmap);
                                        imageListener.onPickImage(fname);
                                    }
                                    catch (Exception e) {
                                        showMessage("保存图片失败");
                                    }
                                }
                            });
                        }
                    }
                }).show();
    }

    public void pickImageFromAlbum(MiniPickAlbumImageListener listener) {
        this.pickAlbumImageListener = listener;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_SELECT_ALBUM);
    }


    public void pickImageFromCamera(MiniPickCameraImageListener listener) {
        this.pickCameraImageListener = listener;
        mLocalHeaderName = System.currentTimeMillis() + ".jpg";
        mLocalHeaderPath = MiniFilePath.getDefaultImagePath(getBaseContext());
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(mLocalHeaderPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File out = new File(mLocalHeaderPath, mLocalHeaderName);
            Uri uri = Uri.fromFile(out);
            // 指定调用相机拍照后照片的储存路径
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, REQUEST_SELECT_CAMERA);
        } else {
            showMessage("请确认已经插入SD卡");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Method method = findMethodForActionId(requestCode, ActivityResult.class);
        if (method != null) {
            try {
                method.invoke(this, requestCode, resultCode, data);
            } catch (Exception e) {
                MiniLogger.get().e("", e);
            }
            return;
        }
    }

    @ActivityResult(REQUEST_SELECT_ALBUM)
    public void activityResultAlbum(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                if (uri.toString().contains("content:")) {
                    if (pickAlbumImageListener != null) {
                        String path = filePathForUri(uri);
                        if (path != null) {
                            pickAlbumImageListener.onPickAlbumImage(path);
                        } else {
                            ContentResolver cr = this.getContentResolver();
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                                if (bitmap != null) {
                                    String fname = MiniFileManager.saveBitmap(this, bitmap);
                                    if (fname != null) {
                                        pickAlbumImageListener.onPickAlbumImage(fname);
                                    }
                                }
                            } catch (Exception e) {
                                MiniLogger.get().e(e);
                            }
                        }
                    }
                } else {
                    try {
                        File file = new File(new URI(uri.toString()));
                        String fileAbsolutePath = file.getAbsolutePath();
                        pickAlbumImageListener.onPickAlbumImage(fileAbsolutePath);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @ActivityResult(REQUEST_SELECT_CAMERA)
    public void activityResultCamera(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                File out = new File(mLocalHeaderPath, mLocalHeaderName);
                Uri uri = Uri.fromFile(out);

                if (uri != null) {
                    if (out.exists()) {
                        //解决三星手机拍照旋转问题
                        String FilePath = out.getAbsolutePath();
                        byte[] imageBytes = MiniRotateImageUtil.getImageBytes(FilePath);

                        Bitmap rotateBitmap = Bytes2Bitmap(imageBytes);

                        if (rotateBitmap != null)
                            pickCameraImageListener.onPickCameraImage(rotateBitmap);
                        else {
                            showMessage("获取图片失败，请重试！");
                        }
                    }
                } else {
                    showMessage("获取图片失败，请重试！");
                }


            } catch (Exception e) {
                showMessage(e.toString());
                e.printStackTrace();
            }
        }
    }


    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    protected void onKeyboardShow() {

    }

    protected void onKeyboardHide() {

    }

    protected void inKeyboardInit() {

    }

    protected void addKeyboardListener(ViewGroup contentView) {
        addKeyboardListener(contentView, contentView.getId());
    }

    protected void addKeyboardListener(ViewGroup contentView, int resId) {
        if (listener == null) {
            listener = new MiniKeyboardListener(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, resId);
            contentView.addView(listener, params);
            listener.setOnKeyboardStateChangedListener(new MiniKeyboardListener.IOnKeyboardStateChangedListener() {
                @Override
                public void onKeyboardStateChanged(int state) {
                    switch (state) {
                        case MiniKeyboardListener.KEYBOARD_STATE_SHOW: {
                            onKeyboardShow();
                            break;
                        }
                        case MiniKeyboardListener.KEYBOARD_STATE_HIDE: {
                            onKeyboardHide();
                            break;
                        }
                        case MiniKeyboardListener.KEYBOARD_STATE_INIT: {
                            inKeyboardInit();
                            break;
                        }
                    }
                }
            });
        }
    }

    // 显示虚拟键盘
    public void showKeyboard(View v) {
        MiniKeyboardUtils.showKeyboard(v);
    }

    // 隐藏虚拟键盘
    public void hideKeyboard(View v) {
        MiniKeyboardUtils.hideKeyboard(v);
    }

    public void showWaiting(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = MiniShowDialogUtils.loadingDialog(this);
        } else {
            mProgressDialog.show();
        }
        if (text != null) {
            MiniShowDialogUtils.setMessage(mProgressDialog, text);
        }
        else {
            MiniShowDialogUtils.setMessage(mProgressDialog, "正在加载数据");
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

    public static <T extends Annotation> Method findMethodForActionId(Class objClazz, int id, Class<T> clazz) {
        while (!objClazz.equals(Object.class)) {
            Method[] methods = objClazz.getDeclaredMethods();
            for (Method method : methods) {
                T action = method.getAnnotation(clazz);
                if (action != null) {
                    if ((action instanceof Action && ((Action) action).value() == id) ||
                            (action instanceof ActivityResult && ((ActivityResult) action).value() == id) ||
                            (action instanceof PushAction && ((PushAction) action).value() == id)) {
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }
                        return method;
                    }
                }
            }
            objClazz = objClazz.getSuperclass();
        }

        return null;
    }

    protected <T extends Annotation> Method findMethodForActionId(int id, Class<T> clazz) {
        return findMethodForActionId(this.getClass(), id, clazz);
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


    @Override
    protected void onResume() {
        super.onResume();
        afterResume();
        Application application = getApplication();
        if (application instanceof MiniApplication) {
            ((MiniApplication) application).onResume();
        }
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void afterResume() {
        MiniActivityManager.currentActivity = this;
    }

    @Override
    protected void onStop() {
        Application application = getApplication();
        if (application instanceof MiniApplication) {
            ((MiniApplication) application).onStop();
        }
        super.onStop();
    }


    private void registerSensor() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float range = event.values[0];
        if (range >= mSensor.getMaximumRange()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }


    private static Handler tipHandler = new Handler();

    private static synchronized void doShowTip(Activity activity, String message) {
        if (!TextUtils.isEmpty(message)) {
            final LinearLayout layoutInfo = (LinearLayout) activity.findViewById(R.id.title_layout_info);
            if (layoutInfo != null) {
                final TextView tvInfo = (TextView) activity.findViewById(R.id.title_tv_info);
                if (tvInfo != null) {
                    layoutInfo.setBackgroundResource(R.drawable.action_parents_center_info);
                    tvInfo.setTextColor(activity.getResources().getColor(R.color.parents_center_info));
                    tvInfo.setText(message);
                    layoutInfo.setVisibility(View.VISIBLE);
                    setStartAnim(activity, layoutInfo);
                    tipHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layoutInfo.setVisibility(View.GONE);
                            tipHandler.removeCallbacks(this);
                        }
                    }, 1000);
                }
            }
        }
    }

    protected boolean willShowTip(Object src, String message) {
        return true;
    }

    protected void showTip(Object src, String message) {
        if (willShowTip(src, message)) {
            if (MiniActivityManager.currentActivity != null && !MiniActivityManager.currentActivity.equals(this)) {
                if (MiniActivityManager.currentActivity instanceof Activity) {
                    doShowTip((Activity) MiniActivityManager.currentActivity, message);
                }
            }
        }
    }

    private static void setStartAnim(Activity activity, View view) {
        TranslateAnimation animation = new TranslateAnimation(0, 0,
                MiniSystemHelper.dip2px(-40, activity), MiniSystemHelper.dip2px(40, activity));
        animation.setDuration(700);
        animation.setRepeatCount(1);
        animation.setStartOffset(1000);
        animation.setRepeatMode(Animation.REVERSE);
        view.setAnimation(animation);
    }
}
