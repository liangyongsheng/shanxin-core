package com.shanxin.core.util;

import java.security.MessageDigest;

public class MyAlgorithmUtils {
	/**
	 * md5 digest algorithm
	 * 
	 * @param md5
	 * @return
	 */
	public static String MD5(String inputStr) {
		if (inputStr == null || inputStr.equals(""))
			return null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(inputStr.getBytes());
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < array.length; ++i)
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));

			return sb.toString();
		} catch (Exception e) {
		}
		return null;
	}

}
