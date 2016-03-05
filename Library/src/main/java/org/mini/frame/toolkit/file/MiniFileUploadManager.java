package org.mini.frame.toolkit.file;

import android.os.Handler;

import com.dtdinc.dtd.app.CESystem;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.MiniJsonUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/4/13.
 */
public class MiniFileUploadManager {

    private static MiniFileUploadManager instance;

    private String fileServer = CESystem.instance().fileServer();
    private String fileServerPath;

    private Handler handler = new Handler();

    private MiniFileUploadManager() {

    }

    public static MiniFileUploadManager instance() {
        synchronized (MiniFileUploadManager.class) {
            if (instance == null) {
                instance = new MiniFileUploadManager();
            }
        }
        return instance;
    }

    public void upload(final String file,final MiniDataListener<String> listener) {
        upload(file, null, listener);
    }

    public void upload(final String file,String ext, final MiniDataListener<String> listener) {
        if (file == null || file.length() ==0) {
            listener.onResponse(null,new CEDataException(400,"文件路径为空"));
            return;
        }
        if (ext == null) {
            int index = file.lastIndexOf(".");
            if (index != -1) {
                ext = file.substring(index+1);
            }
        }
        if (ext == null) {
            ext = "";
        }
        final String fileExt = ext;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] bytes = readFromFile(file);
                    final String fileId = post(bytes, 60000, 20000, fileExt);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResponse(fileId, null);
                        }
                    });
                }
                catch (Exception e) {
                    final CEDataException exception = new CEDataException(e.getMessage(), e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResponse(null, exception);
                        }
                    });

                }
            }
        }).start();
    }

    private byte[] readFromFile(String file) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        do {
            int length = dataInputStream.read(bytes, 0, 4096);
            if (length == 0) {
                break;
            }
            byteArrayOutputStream.write(bytes, 0, length);
            if (length < 4096) {
                break;
            }
        }
        while (true);
        return byteArrayOutputStream.toByteArray();
    }

    private String post(byte[] body, int readTimeout, int connectTimeout,String ext) {
        try {
            String address = fileServerPath;
            if (address == null || address.length() == 0) {
                address = fileServer + "?fid=0&project_id=20150102&ext="+ext+"&length="+body.length;
            }
            java.net.URL url = new java.net.URL(address);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type","multipart/form-data");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(readTimeout);
            connection.setConnectTimeout(connectTimeout);
            OutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body,0,body.length);
            out.flush();
            out.close();

            int code = connection.getResponseCode();
            StringBuilder stringBuilder = new StringBuilder();
            InputStream in = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"utf-8"));
            String line;
            while ((line=bufferedReader.readLine())!=null) {
                stringBuilder.append(line);
            }
            in.close();
            String response = stringBuilder.toString();
            MiniLogger.get().d(String.valueOf(response));
            if (code!=200) {
                throw new RuntimeException("service inner error ["+code+"] !");
            }
            if (fileServerPath != null && fileServerPath.length() > 0) {
                return response;
            }
            else {
                Map<String, Object> fileId = MiniJsonUtil.stringToObject(response, HashMap.class);
                return (String) fileId.get("fid");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFileServerPath() {
        return fileServerPath;
    }

    public void setFileServerPath(String fileServerPath) {
        this.fileServerPath = fileServerPath;
    }
}
