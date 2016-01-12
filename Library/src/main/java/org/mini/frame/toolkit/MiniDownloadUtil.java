package org.mini.frame.toolkit;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

/**
 * Created by gassion on 15/5/15.
 */
public class MiniDownloadUtil {

  public interface DownloadFileListener {
    void downLoadNotify(long currentNumber, long totalNumber);
  }

  /**
   *
   * @param urlStr 下载的链接地址
   * @param filePath 需要保存的路径
   * @param downloadFileListener 下载的回调
   * @return
   */
  public static boolean download(String urlStr, String filePath, DownloadFileListener downloadFileListener) {
    int contentLength = 0;
    try {
      URL url = new URL(urlStr);
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setConnectTimeout(1000);
      httpURLConnection.setRequestMethod("GET");
      contentLength = httpURLConnection.getContentLength();

      // 创建文件，filePath为下载文件的本地路径
      File file = new File(filePath+".temp");
      // 判断是否存在
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        // 创建该文件
        file.createNewFile();

      }
      RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
      randomAccessFile.setLength(contentLength);
      InputStream inputStream = httpURLConnection.getInputStream();
      int currentNumber = 0;
      byte[] buf = new byte[1024];
      int length;
      while ((length = inputStream.read(buf)) != -1) {
        randomAccessFile.write(buf, 0, length);
        currentNumber += length;
        downloadFileListener.downLoadNotify(currentNumber, contentLength);
      }

      inputStream.close();
      randomAccessFile.close();
      file.renameTo(new File(filePath));
    } catch (Exception e) {
      e.printStackTrace();
      File file = new File(filePath);
      if (file.exists()) {
        file.delete();
      }
      return false;
    }
    return true;
  }

  public static String getFileNameByUrl(String downFileUrl) {
      if (downFileUrl != null) {
          int index = downFileUrl.lastIndexOf("/");
          String fileName = downFileUrl.substring(index);
          return fileName;
      }
      else {
          return null;
      }
  }
}
