package com.analog.data.util;

import java.security.MessageDigest;

public class MD5Utils {
	   //小写十六位进制数字（里边的可以修改）
	   private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	   //大写十六进制数字（里边的可以修改）
	   private static final char upperCaseHexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	   
	   public static String getMd5(String s) {
	      return getMd5(s, hexDigits);
	   }
	   public static String md5UpperCase(String s) {
	      return getMd5(s, upperCaseHexDigits);
	   }
	   public static String getMd5(String s,char hexDigits[]) {
	      try {
	         byte[] btInput = s.getBytes();
	         MessageDigest mdInst = MessageDigest.getInstance("MD5");
	         mdInst.update(btInput);
	         byte[] md = mdInst.digest();
	         int j = md.length;
	         char str[] = new char[j * 2];
	         int k = 0;
	         for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexDigits[byte0 & 0xf];
	         }
	         return new String(str);
	      } catch (Exception e) {
	         e.printStackTrace();
	         return null;
	      }
	   }
	   public static void main(String[] args) {
	      System.out.println(MD5Utils.getMd5("88888888"));
	   }
}
