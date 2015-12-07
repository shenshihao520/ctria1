package com.fntech.m10.u1.rfid.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android_serialport_api.SerialPort;

import com.fntech.Loger;
import com.fntech.m10.u1.rfid.command.Command;
import com.fntech.m10.u1.rfid.utils.ArrayUtils;
import com.fntech.m10.u1.rfid.utils.Tools;

public class ReaderAndWriter implements IReaderAndWriter {

	public static final int START = 1;
	public static final int SUCCEED = 2;
	public static final int FAIL = 3;
	public static final int ERROR = 4;
	public static final int FINISH = 5;

	public static final String KEY_MSG = "msg_text";
	public static final String KEY_RAW = "raw_bytes";
	public static final String KEY_ERR = "error_code";

	public static final String MSG_IO_ERR = "IO Exception！";
	public static final String MSG_CONN_NORMAL = "Communication is normal";
	public static final String MSG_CONN_ERR = "Communication failure！";
	public static final String MSG_INTER_OPENED = "Interface open successfully！";
	public static final String MSG_INTER_CLOSED = "Interface open failure！";
	public static final String MSG_RW_READY = "Read/write device has been ready！";
	public static final String MSG_ERR_DATA = "Packet error！";

	public static final String OPEN_INTER = "open interface";
	public static final String CHK_CONN = "check connection";

	private SerialPort serialPort = null;
	private InputStream is = null;
	private OutputStream os = null;
	private static ReaderAndWriter raw;
	private int endType;

	private String LOCK = "";
	private boolean isSuspend = false;

	private String cmd = "";
	private boolean isReady = true;
	private boolean isExit = false;

	/**
	 * 单例模式
	 * 
	 * @return
	 * @throws SecurityException
	 * @throws IOException
	 */
	public static ReaderAndWriter getInstance() throws SecurityException,
			IOException {
		if (raw == null) {
			raw = new ReaderAndWriter();
		}
		return raw;
	}

	/**
	 * 私有化构造方法
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	private ReaderAndWriter() throws SecurityException, IOException {
		this.serialPort = new SerialPort(new File(DEVICE), 115200, 0);
		this.is = this.serialPort.getInputStream();
		this.os = this.serialPort.getOutputStream();
		// t.start();
	}
	byte[] temp=new byte[1024];
	@Override
	public void sendCmd(byte[] cmd) throws IOException {
		// onStart
		// sendMessage(START, this.cmd, null, null,0);
		System.out.println("命令数据包：" + Tools.Bytes2HexString(cmd, cmd.length) +"\r\n");
		// 防止并发
		// synchronized(os){
		 
		/*while(is.available()>0)
		{
			is.read(temp);
		}*/
		os.write(cmd);
		os.flush();
		System.out.print("sendCmd==\r\n");
		Loger.disk_log("write", cmd, "M10_U1");
		// }

	}

	@Override
	public void sendCmd_rn(byte[] cmd) throws IOException {
		// onStart
		// sendMessage(START, this.cmd, null, null,0);
		System.out.println("命令数据包：" + Tools.Bytes2HexString(cmd, cmd.length));
		// 防止并发
		// synchronized(os){
		byte[] cmd2=new byte[cmd.length+2] ;
		System.arraycopy(cmd, 0, cmd2, 0, cmd.length);
		cmd2[cmd2.length-2]=0x0d;
		cmd2[cmd2.length-1]=0x0a;	
		
		/*while(is.available()>0)
		{
			is.read(temp);
		}*/
		os.write(cmd2);		 
		os.flush();
		Loger.disk_log("write", cmd2, "M10_U1");
		// }

	}

	@Override
	public void sendCmd(String cmd, String[] params) throws IOException {
		byte[] cmdBytes = Command.getCmdBytes(cmd, params);
		if (cmdBytes == null || cmdBytes.length == 0) {
			return;
		}
		this.cmd = new String(cmdBytes);
		sendCmd(cmdBytes);
	} 

	int removeZero(byte[] data,int len)
	{
		byte data_temp[]=new byte[len];
		int j=0;
		for(int i=0;i<len;i++)
		{			
			if(data[i]!=0)
			{
				data_temp[j++]=data[i];
			}
		}
		if(j==len)
			return j;
		
		for(int i=0;i<j;i++)
		{
			data[i]=data_temp[i];
		}		
		return j;
	}
	@Override
	public byte[] recvData() {

		int len = 0;
		int len_total = 0;
		byte[] byteReceiver = new byte[1024];	
		List<byte[]> list = new ArrayList<byte[]>();
		try {
			while (true) {	
				len = is.read(byteReceiver);
				
				 if (len > 0)
				{
				 len= removeZero(byteReceiver,len);
				} 
				 
				if (len > 0) {
					len_total+=len;
					byte[] temp = ArrayUtils.copyArray(byteReceiver, 0, len);
					list.add(temp);
					byte[] last1 = list.get(list.size() - 1);
					byte[] last2 = null;
					if (list.size() > 1){
						last2 = list.get(list.size() - 2);
					}
					if (last1.length >= 2) {
						if (last1[last1.length - 2] == 0x24
								&& last1[last1.length - 1] == 0x3E) {
							break;
						}
					}
					else if (last1.length ==1)
					{
						if (last1[last1.length - 1] == 0x3E && (last2!=null&& last2[last2.length-1]==0x24))
						{
							break;
						}
					}
				}
			}//while (true)
			byte[] byteReturn = new byte[len_total];
			int pos=0;
			for(byte[] item : list)
			{
				System.arraycopy(item, 0, byteReturn, pos, item.length);
				pos+= item.length;
			}
			Loger.disk_log("read", byteReturn, len_total, "M10_U1");
			return byteReturn;
			//
			 
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * 设置结束符类型
	 * 
	 * @param endType
	 *            参见Command.END_1 Command.END_2
	 */
	public void setEndType(int endType) {
		if (endType < 1) {
			endType = 1;
		}

		if (endType > 2) {
			endType = 2;
		}
		this.endType = endType;
	}

	/**
	 * 获取结束符类型
	 * 
	 * @return
	 */
	public int getEndType() {
		return this.endType;
	}

	@Override
	public void setTimeout(int ms) {

	}

	@Override
	public void release() {
		try {
			isExit = true;
			is.close();
			os.close();
			serialPort.close();
			raw = null;
		} catch (Exception e) {
		} finally {
			is = null;
			os = null;
			serialPort = null;
			raw = null;
		}
	}

	/**
	 * 读写器是否正忙
	 * 
	 * @return
	 */
	public boolean isReady() {
		return isReady;
	}

	/**
	 * 暂停线程
	 */
	public void suspendWorker() {
		if (!isSuspend) {
			isSuspend = true;
		}
	}

	/**
	 * 恢复线程
	 */
	public void resumeWorker() {
		synchronized (LOCK) {
			if (isSuspend) {
				System.out.println("WorkerThread-恢复运行！");
				isSuspend = false;
				LOCK.notify();
			}
		}
	}

}
