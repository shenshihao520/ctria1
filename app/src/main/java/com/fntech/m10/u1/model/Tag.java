package com.fntech.m10.u1.model;
/**
 * 
 * @author fn-software
 *
 */
public class Tag {
	/**
	 * ID
	 */
	public String id;
	
	/**
	 * epc
	 */
	public String epc;
	
	/**
	 * count
	 */
	public String count;

	public Tag(){}
	
	public Tag(String id, String epc, String count) {
		super();
		this.id = id;
		this.epc = epc;
		this.count = count;
	}
	
	
}
