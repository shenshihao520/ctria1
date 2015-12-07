package com.fntech.m10.u1.rfid.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

//import com.fntech.m10.u1.R;


public class Tools {

		/**
		 * byte[]转String
		 * @param b
		 * @param size
		 * @return
		 */
		@SuppressLint("DefaultLocale")
		public static String Bytes2HexString(byte[] b, int size) {
		    String ret = "";
		    for (int i = 0; i < size; i++) {
		      String hex = Integer.toHexString(b[i] & 0xFF);
		      if (hex.length() == 1) {
		        hex = "0" + hex;
		      }
		      ret += hex.toUpperCase();
		    }
		    return ret;
		  }
		
		public static byte uniteBytes(byte src0, byte src1) {
		    byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
		    _b0 = (byte)(_b0 << 4);
		    byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
		    byte ret = (byte)(_b0 ^ _b1);
		    return ret;
		  }
		
		/**
		 * String转byte[]
		 * @param src
		 * @return
		 */
		public static byte[] HexString2Bytes(String src) {
			int len = src.length() / 2;
			byte[] ret = new byte[len];
			byte[] tmp = src.getBytes();

			for (int i = 0; i < len; i++) {
				ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
			}
			return ret;
		}
	 
		
		/**
		 * byte[]转int,低位在前， 高位在后
		 * @param bytes
		 * @return
		 */
		public static int bytesToInt(byte[] bytes)
		{
			int addr = bytes[0] & 0xFF;
			addr |= ((bytes[1] << 8) & 0xFF00);
			addr |= ((bytes[2] << 16) & 0xFF0000);
			addr |= ((bytes[3] << 24) & 0xFF000000);
			return addr;

		}
		/**
		 * byte[]转int,高位在前，地位在后
		 * @param bytes
		 * @return
		 */
		public static int bytesToInt2(byte[] bytes)
		{
			int addr =0;
			  for(int i=bytes.length-1,j=0;i>=0;i--,j++)
			 {
				 addr |= (bytes[i] << (j*8)) & (0xFF<<(j*8));
			 }  
			return addr;

		}
		/**
		 * byte[]转int,高位在前，地位在后
		 * @param bytes
		 * @return
		 */
		public static long bytesToLong2(byte[] bytes)
		{
			long addr =0;
			  for(int i=bytes.length-1,j=0;i>=0;i--,j++)
			 {
				 addr |= (bytes[i] << (j*8)) & (0xFFL<<(j*8));
			 }  
			return addr;

		}

		/**
		 * int转byte[]
		 * @param i
		 * @return
		 */
		public static byte[] intToByte(int i)
		{
			byte[] abyte0 = new byte[4];
			abyte0[0] = (byte) (0xff & i);
			abyte0[1] = (byte) ((0xff00 & i) >> 8);
			abyte0[2] = (byte) ((0xff0000 & i) >> 16);
			abyte0[3] = (byte) ((0xff000000 & i) >> 24);
			return abyte0;
		}
		
		/**
		 * 获取时间
		 * @return
		 */
		@SuppressLint("SimpleDateFormat")
		public static String getTime(){
			String model = "yyyy-MM-dd HH:mm:ss";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat(model);		
			String dateTime = format.format(date);
			return  dateTime;
		}
		
		/**
		 * 播放音频
		 * @param context
		 */
		public static void playMedia(Context context){
//			System.out.println("media player");
/*			MediaPlayer player = MediaPlayer.create(context, R.raw.msg);
			if(player.isPlaying()){
//				player.release();
//				player.reset();
//				player = MediaPlayer.create(context, R.raw.msg);
				return;
			}
			try {
//				player.prepare();
				player.start();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				player.release();
			} 	*/	
		}

		private static SoundPool soundPool;
		private static HashMap<Integer, Integer> soundPoolMap; 

		@SuppressLint("UseSparseArrays")
		@SuppressWarnings("deprecation")
		public static void initSound(Context context) {
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
//			soundPoolMap = new HashMap<Integer, Integer>();
//			soundPoolMap.put(1, soundPool.load(context, R.raw.noxiaqi, 1));
//			soundPoolMap.put(2, soundPool.load(context, R.raw.dong, 1)); 
//			soundPoolMap.put(4, soundPool.load(context, R.raw.win, 1)); 
//			soundPoolMap.put(5, soundPool.load(context, R.raw.loss, 1)); 
//			soundPoolMap.put(6, soundPool.load(context, R.raw.msg, 1));
		}
		
		/**
		 * 播放音频
		 * @param context
		 * @param sound
		 * @param loop
		 */
		public static void playSound(Context context, int sound, int loop) {
			AudioManager mgr = (AudioManager) context 
					.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;
			soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
		}		
		
		/**
		 * 计算CRC
		 * @param cmd
		 * @param pos
		 * @param len
		 * @return
		 */
		public static  byte getCRC(byte[] cmd,int pos, int len) {
			byte crc = (byte) 0x00;
			for (int i = pos; i < len - 1; i++) {
				crc += cmd[i];
			}
			return crc;
		}
		
        /** 
         * 字符串转换成十六进制字符串
         */  
        public static String strHexStr(String str) {  
            char[] chars = "0123456789ABCDEF".toCharArray();  
            StringBuilder sb = new StringBuilder("");
            byte[] bs = str.getBytes();  
            int bit;  
            for (int i = 0; i < bs.length; i++) {  
                bit = (bs[i] & 0x0f0) >> 4;  
                sb.append(chars[bit]);  
                bit = bs[i] & 0x0f;  
                sb.append(chars[bit]);  
            }  
            return sb.toString();  
        } 


}
