package org.mini.frame.view;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.MiniDisplayUtil;
import org.mini.frame.toolkit.file.MiniFileDownloadManager;
import org.mini.frame.toolkit.media.MiniAudioPlayer;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.R;
import com.mini.core.exception.CEDataException;

/**
 * Created by Wuquancheng on 15/4/19.
 */
public class MiniAudioView extends RelativeLayout implements View.OnClickListener {


  public enum MiniAudioArrowDirection {
    Normal, Left, Right
  }

  private String fileName;

  private String url;

  private int audioTime;

  private RelativeLayout contentLayout;

  private ImageView voiceImageView;
  private RelativeLayout.LayoutParams voiceImageViewLayoutParams;
  private TextView textView;
  private RelativeLayout.LayoutParams layoutParamsText;
  private RelativeLayout.LayoutParams layoutParams;
  private RelativeLayout.LayoutParams allLayoutParams;

  private int imageViewMargin = MiniDisplayUtil.dip2px(10);

  private byte imageIndex = 3;

  private boolean playing = false;

  private Map<Byte, Integer> imageMap = new HashMap<>();

  public MiniAudioView(Context context) {
    super(context);
    this.initView();
  }

  public MiniAudioView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.initView();
  }

  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (playing) {
      MiniAudioPlayer.instance().stop();
    }
  }

  private void initView() {
    this.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    RelativeLayout relayout = new RelativeLayout(this.getContext());
    allLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    relayout.setLayoutParams(allLayoutParams);

    contentLayout = new RelativeLayout(this.getContext());
    contentLayout.setBackgroundResource(R.drawable.chat_right_bg_normal);
    layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    relayout.addView(contentLayout, layoutParams);

    textView = new TextView(this.getContext());
    layoutParamsText = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    layoutParamsText.addRule(RelativeLayout.CENTER_VERTICAL);
    relayout.addView(textView, layoutParamsText);

    voiceImageView = new ImageView(this.getContext());
    voiceImageView.setImageResource(R.drawable.chat_voice_right_3_normal);
    int imageSize = MiniDisplayUtil.dip2px(30);
    voiceImageViewLayoutParams = new RelativeLayout.LayoutParams(imageSize, imageSize);
    voiceImageViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    relayout.addView(voiceImageView, voiceImageViewLayoutParams);

    this.addView(relayout);

    this.setOnClickListener(this);
  }

  public void setArrowDirection(MiniAudioArrowDirection arrowDirection) {
    if (arrowDirection.equals(MiniAudioArrowDirection.Normal)) {
      contentLayout.setBackgroundResource(R.drawable.chat_text_normal);
      voiceImageView.setImageResource(R.drawable.chat_voice_left_3_normal);
      allLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      layoutParamsText.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      voiceImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      voiceImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
      voiceImageViewLayoutParams.leftMargin = imageViewMargin;
      imageMap.put((byte) 1, R.drawable.chat_voice_left_1_normal);
      imageMap.put((byte) 2, R.drawable.chat_voice_left_2_normal);
      imageMap.put((byte) 3, R.drawable.chat_voice_left_3_normal);
    } else if (arrowDirection.equals(MiniAudioArrowDirection.Left)) {
      contentLayout.setBackgroundResource(R.drawable.chat_left_bg_selector);
      allLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      voiceImageView.setImageResource(R.drawable.chat_voice_left_3_normal);
      voiceImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
      voiceImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      layoutParamsText.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      voiceImageViewLayoutParams.leftMargin = imageViewMargin;
      imageMap.put((byte) 1, R.drawable.chat_voice_left_1_normal);
      imageMap.put((byte) 2, R.drawable.chat_voice_left_2_normal);
      imageMap.put((byte) 3, R.drawable.chat_voice_left_3_normal);
    } else if (arrowDirection.equals(MiniAudioArrowDirection.Right)) {
        contentLayout.setBackgroundResource(R.drawable.chat_right_bg_selector);
      allLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      voiceImageView.setImageResource(R.drawable.chat_voice_right_3_normal);
      voiceImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      voiceImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
      layoutParamsText.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      voiceImageViewLayoutParams.rightMargin = imageViewMargin;
      imageMap.put((byte) 1, R.drawable.chat_voice_right_1_normal);
      imageMap.put((byte) 2, R.drawable.chat_voice_right_2_normal);
      imageMap.put((byte) 3, R.drawable.chat_voice_right_3_normal);
    }
  }


  /**
   * 设置动态的语言已经背景以及语言显示的时长颜色值
   */
  public void setFeedVoiceBg() {
    imageMap.clear();
      textView.setTextColor(getResources().getColor(R.color.common_green));
      contentLayout.setBackgroundResource(R.drawable.chat_left_bg_yellow_green_normal);
    voiceImageView.setImageResource(R.drawable.voice_left_white_3_normal);
    imageMap.put((byte) 1, R.drawable.voice_left_white_1_normal);
    imageMap.put((byte) 2, R.drawable.voice_left_white_2_normal);
    imageMap.put((byte) 3, R.drawable.voice_left_white_3_normal);
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;

    if (!TextUtils.isEmpty(fileName)) {
      try {
        long amrDuration = MiniAudioPlayer.getAmrDuration(new File(fileName));
        int time = audioTime > 0 ? audioTime : (int) amrDuration;
        setVoiceShowLength(time);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
    if (url != null) {
      MiniFileDownloadManager.instance().request(this.getUrl(), new MiniDataListener<String>() {
        @Override
        public void onResponse(String data, CEDataException error) {
          if (error == null) {
            setFileName(data);
          } else {
            MiniLogger.get().e(error.getMessage(), error);
          }
        }
      });
    }
  }

  public void setUrlAndTime(String url, int audioTime) {
    this.url = url;
    this.audioTime = audioTime;
    setVoiceShowLength(audioTime);
    if (url != null) {
      MiniFileDownloadManager.instance().request(this.getUrl(), new MiniDataListener<String>() {
        @Override
        public void onResponse(String data, CEDataException error) {
          if (error == null) {
            fileName = data;
          } else {
            MiniLogger.get().e(error.getMessage(), error);
          }
        }
      });
    }
  }

  public void setVoiceShowLength(int audioTime) {
    if (audioTime > 0)
      textView.setText(audioTime + " ''");
    else {
      int audioDuration = audioTime > 300 ? 300 : (int) audioTime;
      textView.setText(audioDuration + " ''");
    }
    layoutParams.width = MiniDisplayUtil.dip2px(65 + audioTime / 2);
    if (audioTime >= 100) {
      allLayoutParams.width = MiniDisplayUtil.dip2px(65 + audioTime / 2) + MiniDisplayUtil.dip2px(40);
    } else {
      allLayoutParams.width = MiniDisplayUtil.dip2px(65 + audioTime / 2) + MiniDisplayUtil.dip2px(30);
    }
  }

  public void play() {
    imageIndex = 3;
    if (this.fileName != null) {
      try {
        MiniAudioPlayer.instance().play(this.fileName, new MiniAudioPlayer.AudioPlayListener() {
          public void onStart() {
            imageIndex = 1;
            playing = true;
            updateProgress(true);
            MiniLogger.get().d("start...");
          }

          public void onProgress(double progress) {
            MiniLogger.get().d("progress ..." + progress);
            updateProgress(true);
          }

          public void onCompletion() {
            imageIndex = 3;
            playing = false;
            updateProgress(false);
            MiniLogger.get().d("onCompletion ...");
          }
        });
      } catch (Exception e) {
        MiniLogger.get().e(e.getMessage(), e);
      }
    }
  }

  private void updateProgress(boolean changeIndex) {
    Integer resId = imageMap.get(imageIndex);
    if (resId != null) {
      voiceImageView.setImageResource(resId);
      voiceImageView.invalidate();
    }
    if (changeIndex) {
      imageIndex++;
      if (imageIndex > 3) {
        imageIndex = 1;
      }
    }
  }

  @Override
  public void onClick(View v) {
    if (playing) {
      MiniAudioPlayer.instance().stop();
    } else {
      play();
    }
  }

}
