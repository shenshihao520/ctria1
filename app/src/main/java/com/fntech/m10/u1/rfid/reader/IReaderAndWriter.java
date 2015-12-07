package com.fntech.m10.u1.rfid.reader;

import java.io.IOException;

public interface IReaderAndWriter {

	/**
	 * 串口设备
	 */
	String DEVICE = "/dev/ttySAC2";
	
	
	void sendCmd_rn(byte[] cmd) throws IOException;//,byte[] byteReceiver
	/**
	 * 发送命令字节数组，向IO流写数据
	 * @param cmd  命令字节数组
	 * @return     如果发生异常则发送失败，返回false
	 * @throws IOException 
	 */
	void sendCmd(byte[] cmd) throws IOException;//,byte[] byteReceiver
	
	/**
	 * 发送命令字节数组
	 * @param cmd     命令字节数组
	 *//*
	public boolean sendCmd(byte[] cmd);*/
	
	/**
	 * 发送命令
	 * @param cmd     命令
	 * @param params  参数
	 * @throws IOException 
	 */
	void sendCmd(String cmd, String[] params) throws IOException;
	
	/**
	 * 读串口数据，读IO流
	 */
	byte[] recvData();
	
	/**
	 * 设置超时
	 * @param ms  超时时间
	 */
	void setTimeout(int ms);
	
	/**
	 * 释放资源，关闭流
	 */
	void release();
}
