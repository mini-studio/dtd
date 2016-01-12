package org.mini.frame.toolkit;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/7/18.
 */
public class MiniPinYinUtil {

    private static MiniPinYinUtil instance;

    private MiniPinYinUtil() {
    }

    public static MiniPinYinUtil instance() {
        synchronized (MiniPinYinUtil.class) {
            if (instance == null) {
                instance = new MiniPinYinUtil();
            }
        }
        return instance;
    }

    /**
     * 获取全拼
     *
     * @param input
     * @return
     */
    public String getPinYinForAll(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                if (pinyinArray != null) {
                    stringBuffer.append(pinyinArray[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }

        }
        return stringBuffer.toString();
    }

    /**
     * 获取名字首字母
     *
     * @param input
     * @return
     */
    public String getPinYinFirstLetter(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        StringBuffer sb = new StringBuffer();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                if (pinyinArray != null) {
                    sb.append(pinyinArray[0].substring(0, 1));
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 获取名字组合
     * @param input
     * @return
     */
    public String getPinYinForGroup(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        StringBuffer sb = new StringBuffer();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                if (pinyinArray != null) {
                    if(!TextUtils.isEmpty(pinyinArray[0])) {
                        sb.append(pinyinArray[0]);
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 得到单字拼音数组
     * @param input
     * @return
     */
    public List<String> getPinYinForSingle(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        List<String> list = new ArrayList<String>();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                if (pinyinArray != null) {
                    list.add(pinyinArray[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }

        }
        return list;
    }

    public List<String> getPinYinForSearch(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        list.add(getPinYinForAll(input));
        List<String> single = getPinYinForSingle(input);
        if (single != null) {
            for (int i = 0; i < single.size(); i++) {
                list.add(single.get(i));
            }
        }
        for (int i = 1; i < input.length() - 1; i++) {
            list.add(getPinYinForGroup(input.substring(i, input.length())));
        }
        list.add(getPinYinFirstLetter(input));
        return list;
    }
}
