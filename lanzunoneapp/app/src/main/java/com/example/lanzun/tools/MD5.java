package com.example.lanzun.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static String getHashString(MessageDigest paramMessageDigest) {
		StringBuilder localStringBuilder = new StringBuilder();
		byte[] arrayOfByte = paramMessageDigest.digest();
		int i = arrayOfByte.length;
		for (int j = 0;; j++) {
			if (j >= i)
				return localStringBuilder.toString();
			int k = arrayOfByte[j];
			localStringBuilder.append(Integer.toHexString(0xF & k >> 4));
			localStringBuilder.append(Integer.toHexString(k & 0xF));
		}
	}

	public static String getMD5(String paramString) {
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			String str = getHashString(localMessageDigest);
			return str;
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
		}
		return null;
	}
}
