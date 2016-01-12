package org.mini.frame.toolkit;

import java.text.DecimalFormat;

/**
 * Created by Wuquancheng on 15/4/3.
 */
public class MiniFormatFileSizeUtil {

    public static String getFormatFileSize(int fileCount) {
        DecimalFormat df = new DecimalFormat("#0.0");

        if (fileCount == 0) {
            return "0B";
        }

        String fileSizeString;

        if (fileCount < 1024) {
            fileSizeString = df.format((double) fileCount) + "B";
        } else if (fileCount < 1048576) {
            fileSizeString = df.format((double) fileCount / 1024) + "K";
        } else if (fileCount < 1073741824) {
            fileSizeString = df.format((double) fileCount / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileCount / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
