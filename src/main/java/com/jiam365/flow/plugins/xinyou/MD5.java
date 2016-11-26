package com.jiam365.flow.plugins.xinyou;

import java.io.UnsupportedEncodingException;
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

}
