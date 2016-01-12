package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mini.frame.toolkit.MiniDisplayUtil;
import org.mini.frame.toolkit.media.MiniAudioPlayer;
import org.mini.frame.toolkit.media.MiniAudioRecorder;

import org.mini.frame.log.MiniLogger;
import com.mini.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Wuquancheng on 15/4/18.
 */
public class MiniRecordingView extends RelativeLayout implements View.OnTouchListener {

    public interface MinRecordingViewListener {
        boolean onStartRecording();
        void onRecordComplete(String fileName);
    }

    String string_release_for_cancel = "松开手指，取消发送";
    String string_release_for_send = "松开 结束";
    String string_slide_for_cancel = "手指上滑，取消发送";
    String string_press_for_speak = "按住 说话";
    String string_talk_time_is_short = "说话时间太短";
    boolean bRecord =false;//是否录音
    long startTimeMillis;//开始录音的毫秒值
    long entTimeMillis;//结束录音的毫秒值

    private MinRecordingViewListener recordListener;

    private MiniAudioRecorder audioRecorder;

    private RelativeLayout contentLayout;
    private ImageView imageView;
    private TextView textView;
    private ViewGroup parentLayout;
    private MiniRecordingButton controlButton;
    private boolean outside = false;

    private Handler handler;

    public MiniRecordingView(Context context,int id) {
        super(context);
        this.setId(id);
        this.setOnTouchListener(this);
        initView();
    }

    public MiniRecordingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MiniDisplayUtil.dip2px(150), MiniDisplayUtil.dip2px(150));
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_VERTICAL);
        contentLayout = new RelativeLayout(this.getContext());
        contentLayout.setBackgroundResource(R.drawable.chat_recording_view_background);
        this.addView(contentLayout, params);

        imageView = new ImageView(this.getContext());
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(MiniDisplayUtil.dip2px(75), MiniDisplayUtil.dip2px(75));
        relativeParams.topMargin = MiniDisplayUtil.dip2px(20);
        relativeParams.addRule(CENTER_HORIZONTAL);
        imageView.setImageResource(R.drawable.amp1);
        contentLayout.addView(imageView, relativeParams);

        textView = new TextView(this.getContext());
        textView.setText(string_slide_for_cancel);
        textView.setTextColor(getResources().getColor(R.color.common_white));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setLines(1);
        textView.setTextSize(14);
        relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,MiniDisplayUtil.dip2px(20));
        relativeParams.addRule(ALIGN_PARENT_BOTTOM);
        int margin = MiniDisplayUtil.dip2px(5);
        relativeParams.setMargins(margin,0,margin,0);
        relativeParams.bottomMargin = MiniDisplayUtil.dip2px(20);
        contentLayout.addView(textView, relativeParams);

        controlButton = new MiniRecordingButton(this.getContext());
        controlButton.setText(string_press_for_speak);
        controlButton.setGravity(Gravity.CENTER);
        controlButton.setOnTouchListener(this);


        handler = new Handler();
    }

    public MiniRecordingButton getControlButton() {
        return controlButton;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.equals(controlButton)) {
            bRecord =false;
            switch(event.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        controlButton.setText(string_release_for_cancel);
                        imageView.setImageResource(R.drawable.voice_cancel);
                        controlButton.moveOut();
                        textView.setText(string_release_for_cancel);
                        textView.setBackgroundResource(R.drawable.voice_cancle_background);
                        outside = true;
                    } else {
                        controlButton.moveIn();
                        imageView.setImageResource(R.drawable.amp1);
                        controlButton.setText(string_release_for_send);
                        textView.setText(string_slide_for_cancel);
                        textView.setBackgroundColor(Color.TRANSPARENT);
                        outside = false;
                    }
                    break;
                }
                case MotionEvent.ACTION_DOWN: {
                    startTimeMillis = System.currentTimeMillis();
                    startRecording();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    controlButton.touchUp();
                    controlButton.setText(string_press_for_speak);
                    if (timingSend){
                        hideRecordingView();
                    }
                    break;
                }

                case MotionEvent.ACTION_CANCEL:
                    bRecord =true;
                    controlButton.setText(string_press_for_speak);
                    controlButton.touchUp();
                    if (timingSend){
                        hideRecordingView();
                    }
                    break;

            }
        }
        return true;
    }

    public void setParentLayout(ViewGroup parentLayout) {
        this.parentLayout = parentLayout;
    }

    private final int updateAmpInterval = 100;

    public Runnable updateAmpRunnable = new Runnable() {
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();

        public void run() {
            if (!outside) {
                if (map.size() == 0) {
                    map.put(0,R.drawable.amp1);
                    map.put(1,R.drawable.amp2);
                    map.put(2,R.drawable.amp3);
                    map.put(3,R.drawable.amp4);
                    map.put(4,R.drawable.amp5);
                }
                Integer amp = (int)getAmplitude()/3;
                Integer id = map.get(amp);
                if (id == null) {
                    id = R.drawable.amp5;
                }
                imageView.setImageResource(id);
            }
            handler.postDelayed(updateAmpRunnable, updateAmpInterval);
        }
    };

    private void showRecordingView() {
        if (this.getParent() != null) {
            this.parentLayout.removeView(this);
        }
        textView.setText(string_slide_for_cancel);
        if (parentLayout instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            this.parentLayout.addView(this, params);
        }
        else {
            if (parentLayout instanceof LinearLayout) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                this.parentLayout.addView(this, params);
            }
        }

    }

    private void hideRecordingView() {
        timingSend=!timingSend;
        entTimeMillis = System.currentTimeMillis();
        handler.removeCallbacks(isTimingRunnable);
        if (this.getParent() != null) {
            this.parentLayout.removeView(this);
        }
        controlButton.setText(string_press_for_speak);
        recordComplete();
    }

    public void setRecordListener(MinRecordingViewListener recordListener) {
        this.recordListener = recordListener;
    }

    public void stopRecording() {
        if (audioRecorder != null) {
            audioRecorder.stop();
        }
    }

    public void recordComplete() {
        if (audioRecorder != null) {
            try {
                audioRecorder.stop();
                if (!outside) {
                    if (!bRecord){
                        long seconds=(entTimeMillis-startTimeMillis)/1000;
                        if (recordListener != null && seconds>=1) {
                            long amrDuration = MiniAudioPlayer.getAmrDuration(new File(audioRecorder.getFileName()));
                            if (amrDuration>0)
                                recordListener.onRecordComplete(audioRecorder.getFileName());
                        }else{
                            Toast.makeText(this.getContext(),string_talk_time_is_short, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    audioRecorder.clearFile();
                }
            }
            catch (Exception e) {
                MiniLogger.get().e(e.getMessage(),e);
                showError("录音状态错误");
            }
            finally {
                handler.removeCallbacks(updateAmpRunnable);
            }
        }
    }

    public void startRecording() {
        if (recordListener != null) {
            if (recordListener.onStartRecording()) {
                try {
                    startRecord();
                    showRecordingView();
                    controlButton.setText(string_release_for_send);
                }
                catch (IOException e) {
                    MiniLogger.get().e(e.getMessage(), e);
                    showError("没有发现存储卡或文件路径不存在");
                }
            }
        }
    }

    private final int timingInterval = 1000;
    private int timingSeconds=0;//定时的秒数
    private int recordMaxSeconds=5*60-2;//允许录制的最大时间
    private boolean timingSend=false;//定时发送

    public Runnable isTimingRunnable = new Runnable() {

        @Override
        public void run() {
            timingSeconds++;
            if (timingSeconds==recordMaxSeconds){
                hideRecordingView();
            }else{
                handler.postDelayed(this, timingInterval);
            }

        }
    };


    private void startRecord() throws IOException {
        if (audioRecorder == null) {
            audioRecorder = new MiniAudioRecorder(this.getContext());
        }
        audioRecorder.start();
        timingSeconds = 0;
        timingSend=!timingSend;
        handler.postDelayed(isTimingRunnable, timingInterval);
        handler.postDelayed(updateAmpRunnable, updateAmpInterval);
    }

    private double getAmplitude() {
        if (audioRecorder != null) {
            return audioRecorder.getAmplitude();
        }
        else {
            return 0;
        }
    }

    private void showError(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
