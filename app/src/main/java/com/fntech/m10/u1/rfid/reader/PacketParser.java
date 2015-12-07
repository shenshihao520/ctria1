package com.fntech.m10.u1.rfid.reader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

import com.fntech.m10.u1.rfid.utils.CheckUtils;
import com.fntech.m10.u1.rfid.utils.LogUtils;
import com.fntech.m10.u1.rfid.utils.StringUtils;
import com.fntech.m10.u1.rfid.utils.Tools;

/**
 * 包解析类
 * @author fnsoft
 */
public class PacketParser {
	
	private static final String path = Environment.getExternalStorageDirectory().getPath()+File.separator+"u1_log.txt";
	
	public static final String HEAD_OK = "ok";
	public static final String HEAD_ERR = "err";
	public static final String HEAD_ERR_TAG = "err_tag";
	public static final String HEAD_END = "end";
	public static final String CARROT_MARK = "^";
	
	private byte[] packet;
	private String text;
	private String[] emcTag;
	private boolean isOpen = false;
	
	private Map<String,byte[]> map = null;
	
	public PacketParser(){
	}
	
	/**
	 * @param packet  响应字节数组
	 */
	public PacketParser(byte[] packet) {
		if(packet != null && packet.length > 0){
			this.packet = packet;
			this.text = new String(packet);
			printAndLog();
			
		}else{
			this.text = null;
			this.packet = null;
		}
	}
	
	/**
	 * 直接传入text进行解析
	 * @param text
	 */
	public PacketParser(String text){
		this.text = text;
		if(text!=null){
			this.packet = text.getBytes();
		}else{
			this.packet = null;
		}
		printAndLog();
	}
	
	private void printAndLog(){
		
		String hex_str = Tools.Bytes2HexString(this.packet, this.packet.length);
		String str1 ="PacketParser-byte[]:"+hex_str;
		String str2 ="PacketParser-String:"+this.text;
		
		String content = str1+"\n"+str2+"\n";
		
		//记录日志
		LogUtils.log2File(path, content, true);
		
		//打印到控制台
		System.out.println(str1);
		System.out.println(str2);
	}
	
	/**
	 * 获取包数据
	 * @return  数据包字节数组
	 */
	public byte[] getPacket(){
		return this.packet;
	}
	
	/**
	 * 获取数据包数据转换成的字符串
	 * @return
	 */
	public String getText(){
		return this.text;
	}
	
	/**
	 * 检查包是否合法
	 * @return 检查合法则返回true
	 */
	public boolean checkPacket(){
		if(StringUtils.isBlank(this.text) 
				|| this.packet == null 
				|| this.packet.length==0){
			return false;
		}
		
		//如果返回数据以ok,err,end开头或者里面包含"^"，或者数据是合法的16进制字符串，则包是合法的
		return checkStart()
				|| isBadCmd()
				|| CheckUtils.isValidHexString(text)
				|| isValid();

	}
	
	/**
	 * 是否以$符号结尾
	 * @return
	 */
	public boolean isContain$(){
		return text.endsWith("$>");
	}
	
	/**
	 * 以$>分割字符串并返回字符数组
	 * @return
	 */
	public String [] getStringBuf(){
		return text.split("$>");
	}
	
	/**
	 * 包是否合法
	 */
	public boolean isValid(){
		String pattern = "[0-9a-zA-Z=,]+";
		return text.matches(pattern);
	}
	
	/**
	 * 检查头
	 * @return
	 */
	private boolean checkStart(){
		return text.startsWith(HEAD_OK)
				|| text.startsWith(HEAD_ERR)
				|| text.startsWith(HEAD_END);
	}
	
		
	/**
	 * 数据是否以ok开头
	 * @return
	 */
	public boolean isOK(){
		return text.startsWith(HEAD_OK);
	}
	
	/**
	 * 数据是否以err开头
	 * @return
	 */
	public boolean isError(){
		return text.startsWith(HEAD_ERR);
	}
	
	/**
	 * 数据是否以end开头
	 * @return
	 */
	public boolean isEnd(){
		return text.startsWith(HEAD_END);
	}
	
	/**
	 * 数据是否有命令或参数错误 响应数据中包含"^"
	 * @return
	 */
	public boolean isBadCmd(){
		return text.contains(CARROT_MARK);
	}
	
	/**
	 * 获取ok,value中的value
	 * @return
	 */
	public String getOKValue(){
		if(isOK()){
			int index = text.indexOf(",");
			if(index!=-1){
				String value = text.substring(index+1);
			}
		}
		return null;
	}
	
	/**
	 * 获取错误码
	 * @return
	 */
	public String getErrorCode(){
		if(isError()){
			int index = text.indexOf("=");
			String errcode = text.substring(index+1, index+2);
			return errcode;
		}
		return null;
	}
	
	/**
	 * 获取结束码
	 * @return
	 */
	public String getEndCode(){
		if(isEnd()){
			int index = text.indexOf("=");
			String endcode = text.substring(index+1, index+2);
			return endcode;
		}
		return null;
	}
	
	/**
	 * 所有读到的数据都在这里进行解析
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public Map analysisData(byte[] data){
		Map map = new HashMap<String, String>();
		this.packet = data;
		this.text = new String(data);				
		 if (data != null && data.length > 0) {
			if(text.startsWith("\r\n$>") || 
				text.startsWith("$>$>$>$>")){
					map.put("msg", "true");
				}else{
						String[] strBuf = text.split("\r\n");
						for(int i = 0;i<strBuf.length;i++){
							if (isOK()) {
								map.put("msg", text);
							} else if (isError()) {
								map.put("msg", getErrorCode());
							} else if (isEnd()) {
								map.put("msg",text);
							} else if (isBadCmd()) {
								map.put("msg",text);
							} else {
								map.put("msg",text);
							}
						}
				}
			}
		 return map;
	}
}
