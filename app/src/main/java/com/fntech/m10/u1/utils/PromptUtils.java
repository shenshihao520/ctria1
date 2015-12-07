package com.fntech.m10.u1.utils;

import android.content.Context;
import android.widget.Toast;

public class PromptUtils {

	public static void showShortToast(Context context,String content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(Context context,String content){
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
	
}
