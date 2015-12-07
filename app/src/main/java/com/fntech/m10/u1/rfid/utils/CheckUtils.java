package com.fntech.m10.u1.rfid.utils;



public class CheckUtils {
		/**
		 * 检查是否是二进制数
		 * @param binstr
		 * @param len
		 * @return
		 */
		public static boolean checkBin(String binstr,int len){
			
			if(binstr == null || binstr.equals(""))
			{
				return false;
			}
			
			if(binstr.length() != len)
			{
				return false;
			}
			
			String pattern = "^[01]{"+len+"}$";
			return binstr.matches(pattern);
		}
		
		/**
		 * 检查是否是16进制数
		 * @param str
		 * @return
		 */
		public static boolean isValidHexString(String str){
			if(StringUtils.isBlank(str)){
				return false;
			}
			String pattern = "[0-9a-fA-F]+";
			return str.matches(pattern);
		}
}
