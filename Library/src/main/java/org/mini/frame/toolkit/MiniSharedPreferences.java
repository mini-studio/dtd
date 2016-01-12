package org.mini.frame.toolkit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 公共的辅助类
 *
 * @author 霍永刚
 *
 * 2014-6-28 下午2:38:05
 */

public class MiniSharedPreferences {

    private static MiniSharedPreferences miniSharedPreferences;

    private String SHARED_ID;

    private Context context;

    private MiniSharedPreferences(Context context) {
        this.context = context;
        this.SHARED_ID = context.getPackageName();
    }

    public static MiniSharedPreferences instance(Context context) {
        synchronized (MiniSharedPreferences.class) {
            if (miniSharedPreferences == null) {
                miniSharedPreferences = new MiniSharedPreferences(context);
            }
        }
        return miniSharedPreferences;
    }

    public static MiniSharedPreferences instance() {
        return miniSharedPreferences;
    }

    private SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences  = context.getSharedPreferences(SHARED_ID, Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
        return sharedPreferences;
    }

    /***
     * 将sharedPreferences中的指定键设为指定的值
     * @param sharedKey 键值对中的key
     * @param sharedValue 指定的值,键值对中的value
     */
    public void setString(String sharedKey, String sharedValue){
        SharedPreferences sharedPreferences  = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedKey, sharedValue);
        editor.commit();
    }

    /***
     * 获取sharedPreferences中的指定键位的值
     * @param sharedKey key
     * @param defaultValue 当该键位不存在时，返回到默认值
     * @return
     */
    public String getString(String sharedKey, String defaultValue){
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(sharedKey, defaultValue);

    }

    /***
     * 将sharedPreferences中的指定键设为指定的值
     * @param sharedKey 键值对中的key
     * @param sharedValue 指定的值,键值对中的value
     */
    public void setInt(String sharedKey, int sharedValue){
        SharedPreferences sharedPreferences  = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedKey, sharedValue);
        editor.commit();
    }

    /***
     * 获取sharedPreferences中的指定键位的值
     * @param sharedKey key
     * @param defaultValue 当该键位不存在时，返回到默认值
     * @return
     */
    public int getInt(String sharedKey, int defaultValue){
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(sharedKey, defaultValue);
    }


    /***
     * 将sharedPreferences中的指定键设为指定的值
     * @param sharedKey 键值对中的key
     * @param sharedValue 指定的值,键值对中的value
     */
    public void setLong(String sharedKey, long sharedValue){
        SharedPreferences sharedPreferences  = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(sharedKey, sharedValue);
        editor.commit();
    }

    /***
     * 获取sharedPreferences中的指定键位的值
     * @param sharedKey key
     * @param defaultValue 当该键位不存在时，返回到默认值
     * @return
     */
    public long getLong(String sharedKey, long defaultValue){
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getLong(sharedKey, defaultValue);
    }

    /***
     * 将sharedPreferences中的指定键设为指定的值
     * @param sharedKey 键值对中的key
     * @param sharedValue 指定的值,键值对中的value
     */
    public void setBool(String sharedKey, boolean sharedValue){
        SharedPreferences sharedPreferences  = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedKey, sharedValue);
        editor.commit();
    }

    /***
     * 获取sharedPreferences中的指定键位的值
     * @param sharedKey key
     * @param defaultValue 当该键位不存在时，返回到默认值
     * @return
     */
    public boolean getBool(String sharedKey, boolean defaultValue){
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(sharedKey, defaultValue);

    }

    public <T> T getObject(String sharedKey, Class<T> tClass) {
        String string = getString(sharedKey, null);
        if (string != null) {
            return MiniJsonUtil.stringToObject(string, tClass);
        }
        return null;
    }

    public void setObject(String sharedKey, Object object) {
        String string = MiniJsonUtil.toJsonString(object);
        if (string != null) {
            setString(sharedKey, string);
        }
    }

    public void remove(String sharedKey) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(sharedKey);
        editor.commit();
    }
}
