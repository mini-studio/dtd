package org.mini.frame.toolkit.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import org.mini.frame.log.MiniLogger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Wuquancheng on 15/4/19.
 */
public class MiniAudioPlayer implements MediaPlayer.OnCompletionListener {


    public interface AudioPlayListener {
        void onStart();

        void onProgress(double progress);

        void onCompletion();
    }

    private static MiniAudioPlayer audioPlayer;

    private MediaPlayer mediaPlayer;

    private AudioPlayListener playListener;

    private Handler handler = new Handler();

    private MiniAudioPlayer() {

    }

    public static MiniAudioPlayer instance() {
        synchronized (MiniAudioPlayer.class) {
            if (audioPlayer == null) {
                audioPlayer = new MiniAudioPlayer();
            }
        }
        return audioPlayer;
    }

    public void play(String path, AudioPlayListener listener) throws IOException {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            stop();
        }
        File file = new File(path);
        if (file.exists()) {
            try {
                this.playListener = listener;
                mediaPlayer.reset();
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(file.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                onStart();
            } catch (IOException e) {
                MiniLogger.get().e(e.getMessage(), e);
                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                } catch (Exception e2) {
                    MiniLogger.get().e(e.getMessage(), e2);
                }
                throw e;
            }
        }
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mediaPlayer != null) {
                    if (playListener != null) {
                        int duration = mediaPlayer.getDuration();
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        double progress = ((double) currentPosition) / duration;
                        playListener.onProgress(progress);
                    }
                }
            } catch (Exception e) {
                MiniLogger.get().e(e.getMessage(), e);
            }
            handler.postDelayed(progressRunnable, 500);
        }
    };

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            AudioPlayListener listener = playListener;
            this.playListener = null;
            if (listener != null) {
                listener.onCompletion();
            }
        }
        handler.removeCallbacks(progressRunnable);
    }

    public void onStart() {
        if (playListener != null) {
            playListener.onStart();
        }
        handler.postDelayed(progressRunnable, 500);
    }

    public void onCompletion(MediaPlayer mp) {
        mp.setOnCompletionListener(null);
        if (playListener != null) {
            playListener.onCompletion();
        }
        handler.removeCallbacks(progressRunnable);
    }

    public static long getAmrDuration(File file) throws IOException {
        if (file != null && file.exists()) {
            long duration = -1;
            int[] packedSize = {12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0};
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                long length = file.length();//文件的长度
                int pos = 6;//设置初始位置
                int frameCount = 0;//初始帧数
                int packedPos = -1;
                /////////////////////////////////////////////////////
                byte[] datas = new byte[1];//初始数据值
                while (pos <= length) {
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.read(datas, 0, 1) != 1) {
                        duration = length > 0 ? ((length - 6) / 650) : 0;
                        break;
                    }
                    packedPos = (datas[0] >> 3) & 0x0F;
                    pos += packedSize[packedPos] + 1;
                    frameCount++;
                }
                /////////////////////////////////////////////////////
                duration += frameCount * 20;//帧数*20
            } finally {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
            return (duration + 500) / 1000;
        } else {
            return 0;
        }
    }
}
