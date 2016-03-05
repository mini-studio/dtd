package com.dtdinc.dtd.core.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mini.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.mini.frame.toolkit.MiniImageBitmapUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 和微信相关的操作
 *
 * @author Gassion
 */
public class CEWeiXinShareUtils {

    private static IWXAPI iwxapi;

    /**
     * 注册客户端到微信
     *
     * @param context
     * @return 是否注册成功
     */
    public static boolean registerWeixin(Context context) {
        boolean isSuccess;
            iwxapi = WXAPIFactory.createWXAPI(context, CEWeiXinShareParams.PARENTS_APP_ID, false);
            isSuccess = iwxapi.registerApp(CEWeiXinShareParams.PARENTS_APP_ID);
        return isSuccess;
    }

    /**
     * 是否已经安装微信
     *
     * @param context
     * @return
     */
    public static boolean isInstalled(Context context) {
        if (iwxapi == null)
            registerWeixin(context);
        return iwxapi.isWXAppInstalled();
    }

    /**
     * 启动微信
     *
     * @param context
     * @return 返回是否启动成功
     */
    public static boolean startWeixin(Context context) {
        boolean isSuccess = false;
        if (iwxapi == null)
            isSuccess = registerWeixin(context);
        if (isSuccess) {
            iwxapi.openWXApp();
            return true;
        } else
            return isSuccess;
    }

    /**
     * 发送网页
     *
     * @param url     网页地址
     * @param context
     */
    public static void sendWebPage(String url, Context context, String title, String description, File imageFile, String... otherInfo) {
        if (title == null || title.length() == 0) {
            title = "";
        }
        if (description == null || description.length() == 0) {
            description = "";
        }
        if (description.length() > 50) {
            description = description.substring(0, 47) + "...";
        }
        if (iwxapi == null) {
            registerWeixin(context);
        }
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = url;
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        setSharedImage(context, wxMediaMessage, imageFile);
        wxMediaMessage.mediaObject = wxWebpageObject;
        wxMediaMessage.description = description;
        wxMediaMessage.title = title;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = wxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }


    /**
     * 分享网页到朋友圈
     *
     * @param url
     * @param context
     */
    public static void sendWebPage_circle(String url, Context context, String title, String description, File imageFile, String... otherInfo) {
        if (title == null || title.length() == 0) {
            title = "";
        }
        if (description == null || description.length() == 0) {
            description = "";
        }
        if (description.length() > 50) {
            description = description.substring(0, 47) + "...";
        }
        if (iwxapi == null) {
            registerWeixin(context);
        }
        WXWebpageObject imgObj = new WXWebpageObject();
        imgObj.webpageUrl = url;
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        setSharedImage(context, wxMediaMessage, imageFile);
        wxMediaMessage.mediaObject = imgObj;
        wxMediaMessage.title = title;
        wxMediaMessage.description = description;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "" + System.currentTimeMillis();
        req.message = wxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    public static void setSharedImage(Context context,WXMediaMessage wxMediaMessage, File imageFile ) {
        Bitmap bitmap = null;
        if (imageFile != null) {
            bitmap = MiniImageBitmapUtil.getImage(imageFile.getAbsolutePath(), 100, 100);
        }
        if (bitmap != null) {
            wxMediaMessage.setThumbImage(bitmap);
        }
        if ( wxMediaMessage.thumbData == null) {
            wxMediaMessage.thumbData = Bitmap2Bytes(context);
        }
    }
    /**
     * bitmap 转换 bytes
     *
     * @param context
     * @return
     */
    public static byte[] Bitmap2Bytes(Context context) {
        Bitmap thumb;
            thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        return Bitmap2Bytes(thumb);
    }

    public static byte[] Bitmap2Bytes(Bitmap thumb) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            thumb.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        }
        catch (Exception e) {
            return null;
        }
        finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                }
                catch (Exception e) {
                }
            }
        }
    }

}
