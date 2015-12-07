package com.fntech.m10.u1.rfid.reader;

import com.fntech.m10.u1.model.Message;


public interface IManager {
	
	
	/**
	 * Open Interface 
	 * @return   Return true Successful, false Failure
	 */
	boolean openInterface();
	
	/**
	 * 
	 * @return
	 */
	String checkConnectivity();
	
	/**
	 * Inventory
	 * @param f_s set to 1 to stop automatically after a tag is inventoried
	 * @param f_m set to 3 to query selected tags by select mask,to 0 or 1 with select mask
	 * @param to  the operation timeout value in millisecond
	 * @param handler
	 */
	/**
	 *  Return Inventory Tag String
	 * @return
	 */
	String inventory();
	
	/**
	 * 
	 * @return
	 */
	String cmdInventory();
	
	/**
	 * Return true Stop Successful,false Failure
	 * @return
	 */
	boolean stopOperation();
	
	/**
	 *  Get version  Return Version String
	 * @return
	 */
	String getVersion();
	
	/**
	 * 
	 * @return
	 */
	String setDefaultParameter();
	
	/**
	 * Inventory Parameter
	 * @param session  session value for query command
	 * @param q        q value for query command
	 * @param m_ab     taregt value for query command,set to 0 for target A
	 * @return         Return inventory Tag String
	 */
	String inventoryParameter(int session, int q, int m_ab);
	
	/**
	 * Getting parameter
	 * @param cmd  command to get parameters
	 * @param p    parameter of cmd
	 * @return     String
	 */
	String getParameter(String cmd, String p);
	
	/**
	 * Select Mask
	 * @param n         the index of mask table
	 * @param bits      number of bits of the select mask pattern 
	 * @param mem       Memory Bank ID,0=RESERVED,1=EPC,2=TID,3=USER
	 * @param b_offset  bit offset of the memory bank
	 * @param pattern   mask pattern HEXA_String
	 * @param target    target flag
	 * @param action    flag setting option
	 * @return          Return OK Successful
	 */
	Message selectMask(int n, int bits, int mem, int b_offset, String pattern, int target, int action);
	
	/**
	 * Setting Tx Power
	 * @param a must be 0 or negative integer
	 * @return  Return Ok Successful
	 */
	String setTxPower(int a);
	
	/**
	 * Get Max power
	 * @return Return MaxPower String
	 */
	String getMaxPower();
	
	/**
	 * Setting Tx cycle
	 * @param on    transmission interval in msec
	 * @param off   wait interval in msec
	 * @return   String
	 */
	String setTxCycle(int on, int off);
	
	/**
	 * change channel state
	 * @param channel  channel number
	 * @param f_e      set to 1 to use this channel,to 0 not to use
	 * @return    Return OK Successful
	 */
	String changeChannelState(int channel, int f_e);
	
	/**
	 * Setting Country
	 * @param code  id code for a region to work
	 * @return   String
	 */
	String setCountry(int code);
	
	/**
	 * Getting Country Capability
	 *  @return   String
	 */
	String getCountryCapability();
	
	/**
	 * Reading tag memory
	 * @param w_count  numbers of words(2 bytes) to read
	 * @param mem      Memory Bank ID
	 * @param w_offset word offset of the memory bank to read
	 * @param acs_pwd  access password
	 * @param f_s      same as inventory command
	 * @param f_m      same as inventory command
	 * @param to       same as inventory command
	 * @return         return Tag String
	 */
	Message readRagMemory(int w_count, int mem, int w_offset, long acs_pwd);
	
	/**
	 * Writting Tag Memory
	 * @param w_count   number of words to write
	 * @param mem       Memory Bank ID
	 * @param w_offset  word offset of the memory bank to write
	 * @param data      data to write in HEXA_String
	 * @param acs_pwd   access password
	 * @param f_s       same as inventory command
	 * @param f_m       same as inventory command
	 * @param to        same as inventory command
	 * @return          return OK Successful
	 */
	Message writeTagMemory(int w_count, int mem, int w_offset, String data, long acs_pwd);
	
	/**
	 * Killing Tag
	 * @param kill_pwd  kill password in the tag to kill cannot be 0
	 * @param f_s       same as inventory command
	 * @param f_m       same as inventory command
	 * @param to        same as inventory command
	 * @return          String
	 */
	boolean killTag(long kill_pwd);
	
	/**
	 * Locking tag memory
	 * @param user       LOCK state of the USER Memory
	 * @param tid        LOCK state of the TID Memory
	 * @param epc        LOCK state of the PC/EPC Memory
	 * @param acs_pwd    LOCK state of the access password
	 * @param kill_pwd   LOCK state of the kill password
	 * @param ACS_PWD    access password of the tag
	 * @param f_S        same as inventory command
	 * @param f_m        same as inventory command
	 * @param to         same as inventory command
	 * @return           String
	 */
	boolean lockTagMemory(int user, int tid, int epc, int acs_pwd, int kill_pwd, long ACS_PWD);
	
	/**
	 * Set Lock tag memory state permanently
	 * @param mem_id   ID of the Memory Bank of password to fix lock state,0=USER,1=TID,2=EPC,3=access password,4=kill password
	 * @param f_l      lock state to fix,1=permanently lock,0=permanently unlock
	 * @param ACS_PWD  access password
	 * @param f_s      same as inventory command
	 * @param f_m      same as inventory command
	 * @param to       same as inventory command
	 * 
	 */
	void setLockStatePerm(int mem_id, int f_l, int ACS_PWD, int f_s, int f_m, int to);
	
	/**
	 * Pause Tx
	 * 
	 */
	void pauseTX();
	
	/**
	 * Heart Beat
	 * @param value set to 0 to stop heart beat handshake or set the interval ms
	 * 
	 */
	void heartBeat(int value);
	
	/**
	 * status report
	 * @param f_link set to 1 to make reader report when link state changes
	 * 
	 */
	void statusReport(int f_link);
	
	/**
	 * Inventory reporting format
	 * @param f_time  set to 1 to get inventoried time
	 * @param f_rssi  set to 1 to get rssi of the tag response
	 * 
	 */
	void setInventoryReportFormat(int f_time, int f_rssi);
	
	/**
	 * System time
	 * @param val     current time to set in msec since 1970.1.1
	 * 
	 */
	void setSystemTime(long val);
	
	/**
	 * Dislink
	 * 
	 */
	void disLink();
	
	/**
	 * 释放资源
	 */
	void release();
	
}
