package com.mage.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class myMD5 {
	public static String myMd5(String content) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(content.getBytes("UTF8"));
			byte s[] = m.digest();
			String result = "";
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	public static void main(String []args) {
		System.out.println(myMD5.myMd5(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"nihaoya"));
	}
}
