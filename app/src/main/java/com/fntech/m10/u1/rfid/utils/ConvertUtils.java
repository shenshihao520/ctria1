package com.fntech.m10.u1.rfid.utils;

public class ConvertUtils {
	
	/**
	 * @param binstr
	 * @return
	 */
	public static int getBinFromString(String binstr){
		
		if(!CheckUtils.checkBin(binstr, binstr.length()))
		{
			throw new RuntimeException("参数错误！");
		}
		
		int result = 0x00;
		byte[] bytes = binstr.getBytes();
		for (byte b : bytes) {
			result = result <<1;
			result |= (b-0x30);
		}
		return result;
	}
	
	/**
	 * @param value
	 * @param length
	 * @return
	 */
	public static String getBinString(int value,int length){
		String binaryString = Integer.toBinaryString(value);
		if(length > binaryString.length())
		{
			String tmp = "";
			for(int i=0;i<length-binaryString.length();i++)
			{
				tmp = tmp+"0";
			}
			binaryString = tmp+binaryString;
		}
		
		if(length < binaryString.length())
		{
			binaryString = binaryString.substring(binaryString.length()-length);
		}
		return binaryString;
	}
	
}
