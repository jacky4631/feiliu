package com.jiam365.flow.plugins.uc;

import java.security.MessageDigest;


public class Md5Digest {
	/**
	 * md5����
	 * @param strSrc ����ԭ��
	 * @param key ��Կ
	 * @return ���ܺ��ֵ
	 * @throws Exception
	 */
	public static String getKeyedDigest(String strSrc) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String preStr=strSrc;
		System.out.println(preStr);
		md5.update(preStr.getBytes("GB2312"));
		String result = "";
		byte[] temp;
		temp = md5.digest();
		for (int i = 0; i < temp.length; i++) {
			result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
		}
		return result.toUpperCase().substring(0, 16);
	}
	
	public static void main(String[] args) {
		String str="abc";
		
	}
}
