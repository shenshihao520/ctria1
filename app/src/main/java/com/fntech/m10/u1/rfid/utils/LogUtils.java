package com.fntech.m10.u1.rfid.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogUtils {

	public static final boolean log2File(String path,String content,boolean isAppend){
		if(content == null || path == null){
			return false;
		}
		File file = new File(path);
		
		if(!file.exists()){
			try {
				boolean isCreated = file.createNewFile();
				if(!isCreated){
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		if(file.isDirectory()){
			return false;
		}
		
		if(!file.canWrite()){
			return false;
		}
		
		try {
			FileWriter writer = new FileWriter(file,isAppend);
			writer.write(content);
			writer.flush();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
}
