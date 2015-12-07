package cn.apputest.ctria.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.myapplication.Url;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

/**
 * @author 作者Shihao Shen:
 * @version 创建时间：2015-10-21 上午11:32:59 类说明 轮询数据更新类
 */
public class UpdateDataService extends Service {
	private SharedPreferences preferences;
	private DBHelper helper;
	private MyThread myThread;
	private DBManager mgr;
	private boolean flag = true;
	private Context context;
	int VersionCode = 0;
	String sqls;
	boolean ishassqls = false;

	// private static final int POLL_INTERVAL = 1000 * 60;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		preferences = context.getSharedPreferences(Login.FILE_USER,
				MODE_PRIVATE);
		String DBName = preferences.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		System.out.println("创建了更新数据库服务");
		System.out.println("service");
//		new Handler().post(runnable);
	
		
		//
		// this.myThread = new MyThread();
		// this.myThread.start();
//		updatedata();
	
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("开始了更新数据库服务");
		updatedata();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		this.flag = false;
		super.onDestroy();
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			updatedata();
		}
	};
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager
				.getRunningServices(Constants.RETRIVE_SERVICE_COUNT);

		if (null == serviceInfos || serviceInfos.size() < 1) {
			return false;
		}

		for (int i = 0; i < serviceInfos.size(); i++) {
			if (serviceInfos.get(i).service.getClassName().contains(className)) {
				isRunning = true;
				break;
			}
		}
		Log.i("ServiceUtil-AlarmManager", className + " isRunning =  "
				+ isRunning);
		return isRunning;
	}

	public static void setServiceAlarm(Context context) {
		Log.i("ServiceUtil-AlarmManager", "invokeTimerPOIService wac called..");
		PendingIntent alarmSender = null;
		Intent startIntent = new Intent(context, UpdateDataService.class);
		startIntent.setAction(Constants.POI_SERVICE_ACTION_updatedata);
		try {
			alarmSender = PendingIntent.getService(context, 0, startIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
		} catch (Exception e) {
			Log.i("ServiceUtil-AlarmManager", "failed to start " + e.toString());
		}
		AlarmManager am = (AlarmManager) context
				.getSystemService(Activity.ALARM_SERVICE);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), Constants.ELAPSED_TIME_D,
				alarmSender);
	}

	public void updatedata() {
		String version_num = preferences.getString("DataVerSion", "0");
		if (version_num.equals("0")) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("DataVerSion", "0");
			editor.commit();
		}
		getSQL(version_num);
//	
//		Intent it = new Intent();
//		it.setAction("loading complate");
//		context.sendBroadcast(it);
		
//		stopSelf();
	}

	private class MyThread extends Thread {
		@Override
		public void run() {

			while (flag) {

				try {
					// 每个10分钟向服务器发送一次请求
					Thread.sleep(6000);
					System.out.println("发送请求DATA");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String version_num = preferences.getString("DataVerSion", "0");
				if (version_num.equals("0")) {
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("DataVerSion", "0");
					editor.commit();
				}
				getSQL(version_num);
				if (StringUtils.isNotBlank(sqls)) {
					String[] sqlss = sqls.split(";");
					String dataindex = sqlss[0];

					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("DataVerSion", dataindex);
					editor.commit();
					mgr.dosql(sqlss);

				}
			}
		}
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = Base64.decode(
					new String(compressedStr.getBytes(), "UTF-8").getBytes(),
					Base64.DEFAULT);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}

	private String getSQL(final String version_num) {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Url url = new Url();
 
				HttpClient client = new DefaultHttpClient(); // 连接
				HttpGet httpget = new HttpGet(url.getDateversioninfo()
						+ version_num);
				HttpResponse response;
				String sbuf = null;
				try {
					response = client.execute(httpget); // 得到数据
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpentity = response.getEntity();
						if (httpentity != null) {
							BufferedReader br = new BufferedReader(
									new InputStreamReader(
											httpentity.getContent(), "utf-8"));
							sbuf = br.readLine(); // 读出来
							sqls = unzip(sbuf);
							if (StringUtils.isNotBlank(sqls)) {
								String[] sqlss = sqls.split(";");
								String dataindex = sqlss[0];

								SharedPreferences.Editor editor = preferences.edit();
								editor.putString("DataVerSion", dataindex);
								editor.commit();
								mgr.dosql(sqlss);

							}
						} else {
//							Toast.makeText(context, "", Toast.LENGTH_SHORT)
//									.show();
							System.out.println("空");
						}
					}
					else {
//						Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
						System.out.println("网络异常");
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();  
				}
			}
		});
		thread.start();
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return sqls;

	}

}
