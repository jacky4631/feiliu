package com.jiam365.flow.plugins.sswdahan;

import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	 public static String getMD5(String val) throws NoSuchAlgorithmException{    
	        MessageDigest md5 = MessageDigest.getInstance("MD5");    
	        md5.update(val.getBytes());    
	        byte[] m = md5.digest();//����     
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

	// AES BASE64加密
	public static String encrypt(String sSrc){

		String sKey = "a906449d5769fa73";
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = null;//"算法/模式/补码方式"
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("61d7ecc6aa3f6d28".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes());

			return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
