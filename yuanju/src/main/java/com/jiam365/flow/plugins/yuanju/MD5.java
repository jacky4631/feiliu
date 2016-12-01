package com.jiam365.flow.plugins.yuanju;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MD5 {
	
	 /*@param key 接入密钥
	    *@param timestamp 时间戳
	    *@param account客户账户
	    *@return 加密后的签名值
	    */
	    public static String sign (String key, String timestamp, String account) {
	    	
	       String[] arr = new String[]{key, timestamp, account };
	       Arrays.sort(arr);
	       StringBuilder content = new StringBuilder();
	       for (int i = 0; i < arr.length; i++) {
	          content.append(arr[i]);
	       }
	       String signature = null;
	       try {
	          MessageDigest md = MessageDigest.getInstance("SHA-1");
	          byte[] digest = md.digest(content.toString().getBytes("utf-8"));
	          signature =toHexString(digest);
	       } catch (NoSuchAlgorithmException e) {
	          e.printStackTrace();
	       } catch (UnsupportedEncodingException e) {
	          e.printStackTrace();
	       }
	       return signature;
	    }

	public static String SHA1 (String key) {

		String[] arr = new String[]{key};
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		String signature = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes("utf-8"));
			signature =toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return signature;
	}

	    public static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	                'a', 'b', 'c', 'd', 'e', 'f'};
	    public static String toHexString(byte[] bytes) {
	       StringBuilder sb = new StringBuilder(bytes.length * 2);
	       for (int i = 0; i < bytes.length; i++) {
	          sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >>> 4]);
	          sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
	       }
	       return sb.toString();
	    }

	public static String getMD5(String val) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] m = md5.digest();
		return getString(m);
	}
	private static String getString(byte[] b){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < b.length; i ++){
			sb.append(b[i]);
		}
		return sb.toString();
	}

	public static  String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

}
