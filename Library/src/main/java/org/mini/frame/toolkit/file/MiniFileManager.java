package org.mini.frame.toolkit.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import org.mini.frame.toolkit.MiniFramework;
import org.mini.frame.toolkit.MiniImageBitmapUtil;
import org.mini.frame.toolkit.MiniMd5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * Created by Wuquancheng on 15/4/19.
 */
public class MiniFileManager {

    public static void clearCache(Context context) {
        if (context != null) {
            String sdcardPath = getSdcardPath(context);
            String path = sdcardPath + File.separator + MiniFramework.appShortName;
            File file = new File(path);
            delete(file);
        }
    }

    //删除所有的文件夹以及文件
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static String getSdcardPath(Context context) {
        String sdcardPath;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            sdcardPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdcardPath = context.getCacheDir().getPath();
        }
        return sdcardPath;
    }

    public static String getAppFilePath(Context context, String directoryPath) {
        String sdcardPath = getSdcardPath(context);
        if (context != null) {
            String path = sdcardPath + File.separator + MiniFramework.appShortName + File.separator + directoryPath;
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            return path;
        } else {
            return sdcardPath;
        }
    }

    public static void saveDownloadFile(byte[] data, String name) throws Exception {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data, 0, data.length);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void rename(String src, String obj) {
        File file = new File(src);
        if (file.exists()) {
            File objFile = new File(obj);
            if (objFile.exists()) {
                objFile.delete();
            }
            file.renameTo(objFile);
        }
    }

    public static void createFile(String name) throws Exception {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    public static String existFileForUrl(Context context, String url) {
        String fname = fileForUrl(context, url);
        if (fname != null) {
            File file = new File(fname);
            if (file.exists()) {
                return fname;
            }
        }
        return null;
    }

    public static String existDirForUrl(Context context, String url) {
        String fname = fileForUrl(context, url);
        if (!TextUtils.isEmpty(fname)) {
            String dirPath = fname.substring(0, fname.lastIndexOf("."));
            File file = new File(dirPath);
            if (file.isDirectory()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    public static String fileForUrl(Context context, String url) {
        String fileName = MiniMd5.md5String(url);
        int index = url.lastIndexOf(".");
        if (index != -1) {
            String ext = url.substring(index + 1);
            fileName = fileName + "." + ext;
        }
        fileName = getAppFilePath(context, "cache/download") + File.separator + fileName;
        return fileName;
    }

    public static String cacheSdPath(Context context) {
        return getAppFilePath(context, "cache/download");
    }


    public static String saveBitmap(Context context, Bitmap bitmap) throws Exception {
        String fileName = getAppFilePath(context, "cache/bitmap") + File.separator + System.currentTimeMillis() + ".jpg";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            return fileName;
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static Bitmap getImage(String fileName) {
        return MiniImageBitmapUtil.getDiskBitmap(fileName);
    }


    public static String codeString(String fileName) throws Exception{
        File file = new File(fileName);
        if(file==null || !file.exists()){
            throw new RuntimeException("文件不存在");
        }
        BufferedInputStream bin = new BufferedInputStream( new FileInputStream(file));
        try {
            int p = (bin.read() << 8) + bin.read();
            String code;
            //其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                case 0x5c75:
                    code = "ANSI|ASCII";
                    break;
                default:
                    code = "GBK";
            }
            return code;
        }
        finally {
            if (bin != null) {
                bin.close();
            }
        }
    }

    private static void unZip(String srcFile, String destDir){
        int BUFFER = 2048;
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry entry;
        try {
            BufferedOutputStream dest;
            fis = new FileInputStream(srcFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.toString().endsWith("/")) {
                    try {
                        byte[] data = new byte[BUFFER];
                        String strEntry = new String(entry.getName().getBytes(),"utf-8");
                        File entryFile = new File(destDir + "/" + strEntry);
                        File entryDir = new File(entryFile.getParent());
                        if (!entryDir.exists()) {
                            entryDir.mkdirs();
                        }
                        FileOutputStream fos = new FileOutputStream(entryFile);
                        dest = new BufferedOutputStream(fos, BUFFER);
                        int count;
                        while ((count = zis.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, count);
                        }
                        dest.flush();
                        dest.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    try  {
                        String strEntry = new String(entry.getName().getBytes(),"utf-8");
                        File entryFile = new File(destDir + "/" + strEntry);
                        if (!entryFile.exists())
                            entryFile.mkdirs();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception cwj) {
            throw new RuntimeException(cwj);
        }
        finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            }catch (Exception e) {

            }
            try {
                if (zis != null) {
                    zis.close();
                    zis = null;
                }
            }
            catch (Exception e) {

            }
            entry = null;
        }
    }

    private static void unRar(String srcFile, String destDir) throws RuntimeException {
        Archive a = null;
        FileOutputStream fileOutputStream = null;
        try {
            a = new Archive(new File(srcFile));
            FileHeader fh = a.nextFileHeader();
            while (fh != null) {
                if (fh.getFileNameString().trim().toString().indexOf(".") == -1) {
                    String compressFileName = fh.getFileNameW().trim();
                    String destDirName = "";
                    compressFileName = compressFileName.replaceAll("\\\\", "/");
                    destDirName = destDir + "/" + compressFileName;
                    File dir = new File(destDirName);
                    if ((!dir.exists()) || (!dir.isDirectory())) {
                        dir.mkdirs();
                    }
                }
                if (!fh.isDirectory()) {
                    String compressFileName = fh.getFileNameW().trim();
                    compressFileName = compressFileName.replaceAll("\\\\", "/");
                    String destFileName = destDir + "/" + compressFileName;
                    String destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                    File dir = new File(destDirName);
                    if ((!dir.exists()) || (!dir.isDirectory())) {
                        dir.mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fileOutputStream);
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
                fh = a.nextFileHeader();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    fileOutputStream = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (a != null) {
                try {
                    a.close();
                    a = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void unCompress(String srcFile, String destDir) throws Exception {
        File dir = new File(destDir);
        if (dir.exists() && dir.isDirectory()) {
            if (System.currentTimeMillis() -  dir.lastModified() > 3600000) {
                MiniFileManager.delete(dir);
            }
            else {
                return;
            }
        }
        try {
            if (srcFile.endsWith(".rar")) {
                unRar(srcFile, destDir);
            } else if (srcFile.endsWith(".zip")) {
                unZip(srcFile, destDir);
            }
        }
        catch (Exception e) {
            File dest = new File(destDir);
            if (dest.exists()) {
                MiniFileManager.delete(dest);
            }
            throw e;
        }
    }
}
