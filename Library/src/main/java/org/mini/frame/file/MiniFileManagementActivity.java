package org.mini.frame.file;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;

import org.mini.frame.toolkit.file.MiniFileBrowser;
import org.mini.frame.toolkit.file.MiniFileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gassion on 15/8/3.
 */
public class MiniFileManagementActivity extends MNActivityBase implements AdapterView.OnItemClickListener {

    public static final String DOWNLOAD_PATH = "DOWNLOAD_PATH";
    private MiniFileManagementAdapter ceFileManagementAdapter;
    private ListView listView;
    private List<Map<String, Object>> list = null;
    private Map<String, Integer> imageMap = null;
    private final String sParent = "..";
    private final String sFolder = ".";
    private final String sEmpty = "";
    private String sRootPath;
    // 需要显示文件的路径地址
    private String sRoot;
    private String path;
    // 需要显示的文件类型
    private String suffix;

    private static int FileTypeRar = 1;
    private static int FileTypeZip = 2;
    private int mFileTypeCompress = 0;

    private Handler handler = new Handler();

    public static void open(String fileName, Context from) {
        Intent intent = new Intent(from, MiniFileManagementActivity.class);
        intent.putExtra(MiniFileManagementActivity.DOWNLOAD_PATH, fileName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        from.startActivity(intent);
    }

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_file_management);
        this.setTitle("文件管理");
        String downloadPath = getIntent().getStringExtra(DOWNLOAD_PATH);
        if (!TextUtils.isEmpty(downloadPath)){
            this.sRoot = downloadPath;
            this.path = downloadPath;
        } else{
            showMessage("文件打开失败，请重试！");
            finish();
        }
        this.suffix = getFileType();
        imageMap = new HashMap<>();
        imageMap.put(sRoot, R.drawable.file_management_root); // 根目录图标
        imageMap.put(sParent, R.drawable.ffile_management_folder_up); // 返回上一层的图标
        imageMap.put(sFolder, R.drawable.file_management_folder); // 文件夹图标
        imageMap.put("jpg", R.drawable.ffile_management_pic); // wav文件图标
        imageMap.put("png", R.drawable.ffile_management_pic); // wav文件图标
        imageMap.put("wav", R.drawable.file_management_file);
        imageMap.put("amr", R.drawable.file_management_file);
        imageMap.put("doc", R.drawable.file_management_doc);
        imageMap.put("docx", R.drawable.file_management_doc);
        imageMap.put("xls", R.drawable.file_management_excel);
        imageMap.put("xlsx", R.drawable.file_management_excel);
        imageMap.put("zip", R.drawable.ffile_management_rar);
        imageMap.put("rar", R.drawable.ffile_management_rar);
        imageMap.put("txt", R.drawable.file_management_txt);
        imageMap.put(sEmpty, R.drawable.file_management_root);
        ceFileManagementAdapter = new MiniFileManagementAdapter();
        ceFileManagementAdapter.setContext(this);
        listView = (ListView) findViewById(R.id.file_management_list_view);
        listView.setAdapter(ceFileManagementAdapter);
        listView.setOnItemClickListener(this);
    }

    public void didLoadView() {
        super.didLoadView();
        int lastPoint = sRoot.lastIndexOf(".");
        if (lastPoint != -1) {
            String ext = sRoot.substring(lastPoint).toLowerCase();
            boolean isRar = ext.equals(".rar");
            if (isRar) {
                mFileTypeCompress = FileTypeRar;
            } else {
                boolean isZip = ext.equals(".zip");
                if (isZip) {
                    mFileTypeCompress = FileTypeZip;
                }
            }
        }
        if (mFileTypeCompress == FileTypeRar || mFileTypeCompress == FileTypeZip) {
            String dirPath = sRoot.substring(0,lastPoint);
            this.sRootPath = dirPath;
            unCompress(sRoot, dirPath);
        }
        else {
            MiniFileBrowser fileBrowser = new MiniFileBrowser(this);
            fileBrowser.openFile(new File(sRoot));
            this.finish();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (imageMap != null) {
            imageMap.clear();
            imageMap = null;
        }
        if (list != null) {
            list.clear();
            list = null;
        }
        if (ceFileManagementAdapter != null) {
            ceFileManagementAdapter = null;
        }
    }

    private void unCompress(final String fileName, final String destFilePath) {
        showWaiting();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mFileTypeCompress == FileTypeRar || mFileTypeCompress == FileTypeZip) {
                        MiniFileManager.unCompress(fileName, destFilePath);
                        unCompressSuccess(destFilePath);
                    }
                    dismissWaiting();
                } catch (Exception e) {
                    dismissWaiting();
                    final Exception fe = e;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showMessage("文件格式错误");
                        }
                    });
                }
                finally {
                    dismissWaiting();
                }
            }
        }).start();
    }

    private void unCompressSuccess(String destFilePath) {
        this.path = destFilePath;
        handler.post(new Runnable() {
            @Override
            public void run() {
                ceFileManagementAdapter.setData(getData());
                ceFileManagementAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取改文件夹下的所有文件和目录
     *
     * @return
     */
    public List<Map<String, Object>> getData() {
        // 刷新文件列表
        File[] files;
        try {
            files = new File(path).listFiles();
        } catch (Exception e) {
            files = null;
        }
        if (files == null) {
            return new ArrayList<>();
        }
        list = new ArrayList<>(files.length);

        // 用来先保存文件夹和文件夹的两个列表
        ArrayList<Map<String, Object>> folders = new ArrayList<>();
        ArrayList<Map<String, Object>> allFiles = new ArrayList<>();

        if (!this.path.equals(sRootPath)) {
            // 上一层目录
            Map<String, Object> map = new HashMap<>();
            map.put(MiniFileManagementAdapter.fileName, sParent);
            map.put(MiniFileManagementAdapter.filePath, path);
            map.put(MiniFileManagementAdapter.fileIcon, getImageId(sParent));
            list.add(map);
        }

        for (File file : files) {
            if (file.isDirectory() && file.listFiles() != null) {
                // 添加文件夹
                Map<String, Object> map = new HashMap<>();
                map.put(MiniFileManagementAdapter.fileName, file.getName());
                map.put(MiniFileManagementAdapter.filePath, file.getPath());
                map.put(MiniFileManagementAdapter.fileIcon, getImageId(sFolder));
                folders.add(map);
            } else if (file.isFile()) {
                // 添加文件
                String sf = getSuffix(file.getName()).toLowerCase();
                if (suffix == null || suffix.length() == 0 || (sf.length() > 0 && suffix.indexOf("." + sf + ";") >= 0)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(MiniFileManagementAdapter.fileName, file.getName());
                    map.put(MiniFileManagementAdapter.filePath, file.getPath());
                    map.put(MiniFileManagementAdapter.fileIcon, getImageId(sf));
                    allFiles.add(map);
                }
            }
        }

        list.addAll(folders); // 先添加文件夹，确保文件夹显示在上面
        list.addAll(allFiles); // 再添加文件

        return list;
    }

    private int getImageId(String s) {
        if (imageMap == null) {
            return 0;
        } else if (imageMap.containsKey(s)) {
            return imageMap.get(s);
        } else if (imageMap.containsKey(sEmpty)) {
            return imageMap.get(sEmpty);
        } else {
            return 0;
        }
    }

    private String getSuffix(String filename) {
        int dix = filename.lastIndexOf('.');
        if (dix < 0) {
            return "";
        } else {
            return filename.substring(dix + 1);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 条目选择
        final String pt = (String) list.get(position).get("path");
        String fn = (String) list.get(position).get("name");
        if (fn.equals(sRoot) || fn.equals(sParent)) {
            // 如果是更目录或者上一层
            File fl = new File(pt);
            String ppt = fl.getParent();
            if (ppt != null) {
                // 返回上一层
                path = ppt;
            } else {
                // 返回更目录
                path = sRoot;
            }
        }
        else {
            File fl = new File(pt);
            if (fl.isFile()) {
                String lowerName = pt.toLowerCase();
                if (lowerName.endsWith(".rar") || lowerName.endsWith(".zip")) {
                    showWaiting("解压中…");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MiniFileManager.unCompress(pt, path);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissWaiting();
                                        ceFileManagementAdapter.setData(getData());
                                        ceFileManagementAdapter.notifyDataSetChanged();
                                        listView.setSelection(0);
                                    }
                                });
                            } catch (Exception e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissWaiting();
                                        showMessage("文件没有解压成功,请稍后重试！");
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    // 调用系统内部的资源打开
                    showMessage("文件正在打开中,请稍后!");
                    MiniFileBrowser fileBrowser = new MiniFileBrowser(this);
                    fileBrowser.openFile(new File(pt));
                }
                return;
            }
            else if (fl.isDirectory()) {
                path = pt;
            }
        }
        ceFileManagementAdapter.setData(getData());
        ceFileManagementAdapter.notifyDataSetChanged();
    }

    private String getFileType() {
        String fileTypeName =
                ".3gp;.apk;.asf;.avi;.bin;.bmp;.c;.class;.conf;.cpp;.doc;.docx;.xls;.java;.png;.txt"
                        + ".xlsx;.exe;.gif;.gtar;.gz;.h;.htm;.html;.jar;.js;.m4a;.m4b;.m4p;.rar;"
                        + ".m4u;.mp3;.mpe;.mpga;.pdf;.ppt;.pptx;.rc;.tar;.wav;.wps;.zip;.jpg;.jpeg;.amr;.xml;";
        return fileTypeName;
    }


}
