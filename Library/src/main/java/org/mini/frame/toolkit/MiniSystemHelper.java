package org.mini.frame.toolkit;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.R;

import org.mini.frame.view.MiniCustomDialog;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wuquancheng on 15/5/10.
 */
public class MiniSystemHelper {

    public static void copyText(String text, Context context) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(text);
    }

    public static String getFromClip(Context context) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clip.hasText()) {
            return clip.getText().toString();
        } else {
            return null;
        }
    }

    public static void directCall(final String mobile, final Context activity) {
        if (mobile == null || mobile.length() == 0) {
            Toast.makeText(activity, activity.getResources().getString(R.string.person_detail_tel_null), Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = mobile.toLowerCase();
        if (!phone.startsWith("tel:")) {
            phone = "tel:" + mobile;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phone));
        activity.startActivity(intent);
    }

    public static void call(final String mobile, final Context activity) {
        if (mobile == null || mobile.length() == 0) {
            Toast.makeText(activity, activity.getResources().getString(R.string.person_detail_tel_null), Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = mobile.toLowerCase();
        String confirm = phone;
        if (!phone.startsWith("tel:")) {
            phone = "tel:" + mobile;
        }
        else {
            confirm = phone.substring(4);
        }
        final String callMobile = phone;
        MiniCustomDialog.show(activity, activity.getResources().getString(R.string.whether_call_phone), confirm, activity.getResources().getString(R.string.person_detail_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, activity.getResources().getString(R.string.person_detail_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(callMobile));
                activity.startActivity(intent);
            }
        });
    }


    /**
     * 打开安装apk
     *
     * @param filePath apk安装路径
     */
    public static void installApk(Context context, String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    /**
     * @param dpValue
     * @return dip 转换成px
     */
    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 解决textview 有数字、小数点、字母出现的排版换行问题
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static boolean isWtaiTypeUrl(String url) {
        String utf8Url = null;
        try {
            utf8Url = new String(url.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (utf8Url != null && utf8Url.startsWith("tel:")) {
            return true;
        }
        return false;
    }

    public static boolean handleTellTypeUrl(Context context, String url) {
        String mobile = url.substring("tel:".length());
        call(mobile, context);
        return true;
    }

    public static boolean handleWtaiTypeEmailUrl(Context context, String url) {
        Intent intent;

        intent = new Intent();
        intent.setData(Uri.parse("mailto:"));

        // perform generic parsing of the URI to turn it into an Intent.
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.w("Browser", "No resolveActivity " + url);
            return false;
        }
        return true;
    }


    public static boolean isItemWtaiTypeUrl(String url) {
        String utf8Url = null;
        try {
            utf8Url = new String(url.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (utf8Url != null && utf8Url.startsWith("wtai://wp/mc;")) {
            return true;
        }
        return false;
    }


    public static boolean handleWtaiTypeUrl(Context context, String url) {
        Intent intent;
        // perform generic parsing of the URI to turn it into an Intent.
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebView.SCHEME_TEL
                + url.substring(("wtai://wp/mc;").length())));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.w("Browser", "No resolveActivity " + url);
            return false;
        }
        return true;
    }

    //
    public static String transOrderNum(int order) {
        float or = 0;
        if (order >= 10000) {
            or = order / 10000f;
            or = (float) (Math.round(or * 100)) / 100;
            String str = String.valueOf(or);
            if (str.contains(".0")) {
                str = str.substring(0, str.length() - 2);
            } else if (str.contains(".00")) {
                str = str.substring(0, str.length() - 4);
            }
            return str + "万";
        } else {
            return order + "";
        }
    }

    // 姓名的表达式
    public static boolean isUserName(String userName) {
        // 姓名限制10字符，可输入中文、英文、及 “·”
//		String telRegex = "[\u4e00-\u9fa5a-zA-Z-·\u0020]*";
        String telRegex = "[\u4e00-\u9fa5 a-zA-Z•·.]{1,10}";
        return userName.matches(telRegex);
    }

    // 昵称的表达式
    public static boolean isNickName(String nickName) {
        // 姓名限制10字符，可输入中文、英文、数字、及 “·”
        String telRegex = "[\u4e00-\u9fa5a-zA-Z-·-、-\\w.]*";
        return nickName.matches(telRegex);
    }


    public static String schoolTransOrderNum(int order) {
        DecimalFormat df = new DecimalFormat("#.00");
        float or = 0;
        String str = "";
        float transUnit = 10000F;
        if (order >= transUnit && order < transUnit * 100) {
            or = order / transUnit;
            str = df.format(or);
            if (df.format(or).contains(".00"))
                str = str.substring(0, df.format(or).length() - 3);
            else if ("0".equals(str.substring(df.format(or).length() - 1)) ? true : false) {
                str = str.substring(0, df.format(or).length() - 1);
            }
            return str + "万";
        } else if (order >= transUnit * 100) {
            return "100万+";
        } else {
            return order + "";
        }

    }


    /**
     * 姓名超过4个字后面显示....
     *
     * @param name
     * @return
     */
    public static String formatName(String name) {
        if (name.length() > 4) {
            name = name.substring(0, 4) + "...";
        }
        return name;
    }

    /**
     * 搜索出来的关键字变颜色
     *
     * @param filter
     * @param name
     * @return
     */
    public static SpannableString filterName(String filter, String name) {
        SpannableString s = new SpannableString(name);
        if (filter != null) {
            Pattern p = Pattern.compile(filter);
            Matcher m = p.matcher(s);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                s.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return s;
    }


    /**
     * 某个程序是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            PackageManager info = context.getPackageManager();
            info.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //切换孩子
    public static void setRightTitleImage(Context context, TextView rightTitle) {

    }
}
