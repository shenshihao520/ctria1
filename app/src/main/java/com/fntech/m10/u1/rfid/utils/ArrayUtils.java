package com.fntech.m10.u1.rfid.utils;


public class ArrayUtils {
	
	/**
	 * 从源数组中拷贝指定偏移和长度的数组
	 * @param source 源数组
	 * @param offset 偏移位置
	 * @param len    长度
	 * @return       新数组
	 */
	public static byte[] copyArray(byte[] source,int offset,int len)
	{
		if(source  == null)
		{
			throw new RuntimeException("参数错误！");
		}
		
		if(offset + len > source.length)
		{
			throw new RuntimeException("参数错误！");
		}
		
		byte[] des = new byte[len];
		for(int i=offset,j=0;j<len;i++,j++)
		{
			des[j] = source[i];
		}
		return des;
	}
}
