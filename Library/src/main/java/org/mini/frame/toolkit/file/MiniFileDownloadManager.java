package org.mini.frame.toolkit.file;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.mini.core.exception.CEDataException;

import org.mini.frame.file.MiniFileManagementActivity;
import org.mini.frame.http.request.MiniDataListener;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Wuquancheng on 15/4/21.
 */
public class MiniFileDownloadManager {

    private static MiniFileDownloadManager instance;

    private Handler handler = new Handler();

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private Context context;

    private Map<String,DownloadTask> downloadTaskMap = new Hashtable<String,DownloadTask>();

    public interface MiniFileDownloadStatusListener {
        void onIdle();
        void onDownloading();
        void onDownloaded();
    }

    private MiniFileDownloadManager(Context context) {
        this.context = context;
    }

    private void shutdown() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    public static void destroy() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
    }

    public static MiniFileDownloadManager instance(Context context) {
        synchronized (MiniFileDownloadManager.class) {
            if (instance == null) {
                instance = new MiniFileDownloadManager(context);
            }
        }
        return instance;
    }

    public static MiniFileDownloadManager instance() {
        return instance;
    }

    public synchronized void request(String url, MiniDataListener<String> listener) {
        String fileName = MiniFileManager.existFileForUrl(context, url);
        if (fileName != null) {
            listener.onResponse(fileName,null);
        }
        else {
            DownloadTask task = downloadTaskMap.get(url);
            if (task != null) {
                task.addListener(listener);
            }
            else {
                DownloadTask downloadTask = new DownloadTask(url, listener);
                downloadTaskMap.put(url, downloadTask);
                executorService.execute(downloadTask);
            }
        }
    }

    private synchronized void fishedDownloadTask(String url) {
        downloadTaskMap.remove(url);
    }

    public synchronized void attach(View view, final String url, final MiniDataListener<String> listener, final MiniFileDownloadStatusListener statusListener) {
        DownloadTask task = downloadTaskMap.get(url);
        if (task != null) {
            task.addListener(listener);
            statusListener.onDownloading();
        }
        else {
            String fileName = MiniFileManager.existFileForUrl(context, url);
            if (fileName != null) {
                statusListener.onDownloaded();
            }
            else {
                statusListener.onIdle();
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = MiniFileManager.existFileForUrl(context, url);
                if (TextUtils.isEmpty(fileName)) {
                    statusListener.onDownloading();
                    MiniFileDownloadManager.instance().request(url, listener);
                } else {
                    if (!TextUtils.isEmpty(fileName)) {
                        MiniFileManagementActivity.open(fileName, context);
                    }
                }
            }
        });
    }

    private class DownloadTask implements Runnable {
        private String address;
        private List<MiniDataListener<String>> listeners = new ArrayList<MiniDataListener<String>>();
        private String fileName;
        private String tmpFileName;

        public DownloadTask(String url, MiniDataListener<String> listener) {
            this.address = url;
            this.listeners.add(listener);
            this.fileName = MiniFileManager.fileForUrl(context, address);
            this.tmpFileName = this.fileName + "_tmp";
        }

        public void addListener(MiniDataListener<String> listener) {
            this.listeners.add(listener);
        }

        public void run() {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                java.net.URL url = new java.net.URL(address);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int code = connection.getResponseCode();

                if (code == 200) {
                    MiniFileManager.createFile(this.tmpFileName);
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.tmpFileName));
                    inputStream = connection.getInputStream();
                    byte[] cache = new byte[2048];
                    int len;
                    while ( (len = inputStream.read(cache,0,cache.length)) != -1) {
                        bufferedOutputStream.write(cache, 0 , len);
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedOutputStream = null;
                    inputStream.close();
                    inputStream = null;
                    MiniFileManager.rename(this.tmpFileName, fileName);
                    final String fname = fileName;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (MiniDataListener listener : listeners) {
                                listener.onResponse(fname, null);
                            }
                        }
                    });
                }
                else {
                    final String c = String.valueOf(code);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            CEDataException exception = new CEDataException(-1,c);
                            for (MiniDataListener listener : listeners) {
                                listener.onResponse(null, exception);
                            }
                        }
                    });
                }
            }
            catch (Exception e) {
                final CEDataException dataException = new CEDataException(e.getMessage(),e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (MiniDataListener listener : listeners) {
                            listener.onResponse(null, dataException);
                        }
                    }
                });
            }
            finally {
                try {
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();

                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                catch (Exception e) {

                }
                fishedDownloadTask(address);
            }
        }
    }
}


