package org.mini.frame.toolkit;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 解密aes
 * 
 * @author pdy
 * 
 */
public class MiniAESUtils {
	 private static byte[] hex2Bin(String src) {
	        if (src.length() < 1)
	            return null;
	        byte[] encrypted = new byte[src.length() / 2];
	        for (int i = 0; i < src.length() / 2; i++) {
	            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
	            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
	            encrypted[i] = (byte) (high * 16 + low);
	        }
	        return encrypted;
	    }
	    private static String byte2hexString(byte buf[]) {
	        StringBuffer strBuffer = new StringBuffer(buf.length * 2);
	        int i;
	        for( i=0;i<buf.length;i++){
	            strBuffer.append(Integer.toString((buf[i] >> 4) & 0xf, 16) + Integer.toString(buf[i] & 0xf, 16));
	        }
	        return strBuffer.toString();
	    }

	    public static String aesEncrypt(String in, String key) throws Exception  {
	        byte[] bytIn= in.getBytes();
	        SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
	        byte[] bytOut = cipher.doFinal(bytIn);
	        String hex = byte2hexString(bytOut);
	        return hex;
	    }

	    public static String aesDecrypt(String hex,String key) throws Exception {
	        byte[] bytIn= hex2Bin(hex);
	        SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
	        byte[] bytOut = cipher.doFinal(bytIn);
	        String rr = new String(bytOut);
	        return rr;
	    }

	    public static String md5(String str) throws Exception {
	        if (str == null) {
	            return null;
	        }
	        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        messageDigest.update(str.getBytes());
	        return byte2hexString(messageDigest.digest());
	    }


	    public static void main(String[] args) throws Exception {
	        System.out.println(aesEncrypt("uid=289437832&obj_uid=8wuerueh&time=9800898989", "printStackTrace4"));
	        System.out.println(aesDecrypt("d094cd593ce3e6b3dc53a80782bca09009abcf4cbddb0ad9b5924d2d322160ce8a0f105e5c5f9544014f9092a9be805d", "printStackTrace4"));
	        System.out.println( md5("yubo"));
	    }
}