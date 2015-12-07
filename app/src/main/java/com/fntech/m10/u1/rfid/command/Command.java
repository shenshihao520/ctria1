package com.fntech.m10.u1.rfid.command;

import com.fntech.m10.u1.rfid.utils.StringUtils;

public class Command {
	
	public static final int END_TYPE_1 = 1;
	public static final int END_TYPE_2 = 2;
	
	//结束符 \r
	//public static final byte[] END_1 = {0x0D};
	//结束符 \r\n
	public static final byte[] END_2 = {0x0D,0x0A};
	
	//public static final byte[] NULL_1 = END_1;
	public static final byte[] NULL_2 = END_2;
	
	/**
	 * open interface
	 */
	//public static final byte[] OPEN_INTERFACE_1 = {0x0D,0x0D,0x0D,0x0D,0x0D,0x0D,0x0D,0x0D};
	public static final byte[] OPEN_INTERFACE_2 = {0x0D,0x0A,0x0D,0x0A,0x0D,0x0A};
	
	/**
	 * 提示符 prompt "$>" is send if the reader is ready
	 */
	public static final String PROMPT = "$>";
	
	/**
	 * cmd: inventory
	 */
	public static final String CMD_INVENTORY = "i";
	
	/**
	 * cmd: stop operation
	 */
	public static final String CMD_STOP = "s";
	
	/**
	 * cmd: get version
	 */
	public static final String CMD_GET_VERSION = "ver";
	
	/**
	 * cmd:set default parameter
	 */
	public static final String CMD_SET_DEFAULT_PARAMETER = "default";
	
	/**
	 * cmd: set inventory parameter
	 */
	public static final String CMD_INVENTORY_PARAMETER = "iparam";
	
	/**
	 * cmd: get parameter
	 */
	public static final String CMD_GET_PARAMETER = "g";
	
	/**
	 * cmd: select mask
	 */
	public static final String CMD_SELECT_MASK = "m";
	
	/**
	 * cmd: set tx power
	 */
	public static final String CMD_SET_TX_POWER = "txp";
	
	/**
	 * cmd: get max power
	 */
	public static final String CMD_GET_MAX_POWER = "maxp";
	
	/**
	 * cmd: set tx cycle
	 */
	public static final String CMD_SET_TX_CYCLE = "txc";
	
	/**
	 * cmd: change channel state
	 */
	public static final String CMD_CHANGE_CHANNEL_STATE = "chs";
	
	/**
	 * cmd: set countory
	 */
	public static final String CMD_SET_COUNTRY = "cc";
	
	/**
	 * cmd: get countory capability
	 */
	public static final String CMD_GET_COUNTRY_CAPABILITY = "ccap";
	
	/**
	 * cmd: read tag memory
	 */
	public static final String CMD_READ_RAG_MEMORY = "r";
	
	/**
	 * cmd: write tag memory
	 */
	public static final String CMD_WRITE_TAG_MEMORY = "w";
	
	/**
	 * cmd: kill tag
	 */
	public static final String CMD_KILL_TAG = "kill";
	
	/**
	 * cmd: lock tag memory
	 */
	public static final String CMD_LOCK_TAG_MEMORY = "lock";
	
	/**
	 * cmd: set lock tag memoty state permanently
	 */
	public static final String CMD_SET_LOCK_STATE_PERM = "lockperm";
	
	/**
	 * cmd: pause tx
	 */
	public static final String CMD_PAUSE_TX = "pause";
	
	/**
	 * cmd: heart beat
	 */
	public static final String CMD_HEART_BEAT = "online";
	
	/**
	 * cmd: status report
	 */
	public static final String CMD_STATUS_REPORT = "alert";
	
	/**
	 * cmd: inventory report format
	 */
	public static final String CMD_INVENTORY_REPORT_FORMAT = "ireport";
	
	/**
	 * cmd: system time
	 */
	public static final String CMD_SYSTEM_TIME = "time";
	
	/**
	 * cmd: dislink
	 */
	public static final String CMD_DISLINK = "bye";
	
	/**
	 * R900 Controls
	 */
	public static final String CMD_UPLOAD_TAG_DATA = "br.upl";
	public static final String CMD_CLEAR_TAG_DATA = "br.clrlist";
	public static final String CMD_ALERT_READER_STATUS = "br.alert";
	public static final String CMD_GET_STATUS_WORD= "br.sta";
	public static final String CMD_SET_BUZZER_VOLUME = "br.vol";
	public static final String CMD_BEEP = "br.beep";
	public static final String CMD_SET_AUTO_POWER_OFF_DELAY = "br.autooff";
	public static final String CMD_GET_BATTERY_LEVEL = "br.batt";
	public static final String CMD_REPORT_BATTERY_STATE = "br.reportbatt";
	public static final String CMD_TURN_OFF = "br.off";
	public static final String CMD_CONFIG_BLUETOOTH = "br.bt.config";
	public static final String CMD_GET_BLUETOOTH_MAC_ADDR = "br.bt.mac";
	
	/**
	 * 根据命令和参数转换为字节数组
	 * @param cmd         命令
	 * @param params      参数
	 * @param endType  	      结束符类型
	 * @return            转换后的字节数组
	 */
	public static byte[] getCmdBytes(String cmd,String[] params){//,int endType
		if(StringUtils.isBlank(cmd)){
			return null;
		}
		
		/*if(endType<1){
			endType = 1;
		}
		
		if(endType >2){
			endType = 2;
		}*/
		
		StringBuilder sb = new StringBuilder(cmd);
		
		if(params != null && params.length>0){
			for(int i=0;i<params.length;i++){
				if(params[i]!=null)
				{
					sb.append(",");
					sb.append(params[i]);
				}else if(params[i] != null && params[i].equals(""))
				{
					sb.append(",");
				}
			}
		}
		
		String cmdStr = sb.toString();
		System.out.println("命令："+cmdStr);
		byte[] tmpBytes = cmdStr.getBytes();
		
		byte[] cmdBytes = new byte[tmpBytes.length+2];//+endType
		
		for(int i=0;i<tmpBytes.length;i++){
			cmdBytes[i] = tmpBytes[i];
		}
		
		/*if(endType == 1){
			cmdBytes[tmpBytes.length] = END_1[0];
		}else{*/
			cmdBytes[tmpBytes.length] =       END_2[0];
			cmdBytes[tmpBytes.length + 1] = END_2[1];
		//}
		
		return cmdBytes;
	}
	
}
