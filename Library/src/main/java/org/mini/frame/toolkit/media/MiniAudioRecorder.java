package org.mini.frame.toolkit.media;

import android.content.Context;
import android.media.MediaRecorder;

import org.mini.frame.toolkit.file.MiniFileManager;

import java.io.File;
import java.io.IOException;

public class MiniAudioRecorder {

    private static int SAMPLE_RATE_IN_HZ = 8000;
    private MediaRecorder recorder = null;
    private String fileName = null;
    Context context;
    public MiniAudioRecorder(Context context) {
        this.context = context;
    }

    /**
     * 开始录音
     *
     * @throws IOException
     */
    public void start() throws IOException {
        String name = System.currentTimeMillis() + ".amr";
        fileName =  MiniFileManager.getAppFilePath(context, "audio") +"/" + name;
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted,It is  " + state + ".");
        }
        File directory = new File(fileName).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created");
        }
        if (recorder == null) {
            recorder = new MediaRecorder();
        }
        else {
            recorder.reset();
        }
        recorder.setOutputFile(fileName);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
        recorder.prepare();
        recorder.start();
    }

    public void stop() throws IllegalStateException {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (Exception e) {
               e.printStackTrace();
            }
            release();
        }
    }

    public void release() {
        if (recorder != null) {
            try {
                recorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            recorder = null;
        }
    }

    public double getAmplitude() {
        if (recorder != null) {
            return (recorder.getMaxAmplitude()/2700);
        }
        else {
            return 0;
        }
    }

    public void clearFile() {
        if (fileName != null) {
            File file = new File(fileName);
            file.delete();
        }
        fileName = null;
    }

    public String getFileName() {
        return fileName;
    }
}