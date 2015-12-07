package cn.apputest.ctria.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import cn.apputest.ctria.data.CarsFailUploadDataEntity;
import cn.apputest.ctria.data.PeopleFailUploadDataEntity;
import cn.apputest.ctria.data.UploadTaskDataEntity;
import cn.apputest.ctria.myapplication.DateFormat;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.myapplication.RequestHead;
import cn.apputest.ctria.myapplication.Url;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
/**
 * 失败重传服务类
 */
public class UploadFailRecordService extends Service {
	SharedPreferences preferencesuser;
	private MyThread myThread;
	private boolean flag = true;
	private DBHelper helper;
	private DBManager mgr;
	private Context context;
	boolean IsOK;
	private static final int POLL_INTERVAL = 1000 * 6;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		context = this;

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		System.out.println("创建了失败重传服务");
		System.out.println("service");
		
//		uploadrecord();
//		this.myThread = new MyThread();
//		this.myThread.start();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("开始了失败重传服务");
		uploadrecord();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		this.flag = false;
		super.onDestroy();
	}
	  public static boolean isServiceRunning(Context context, String className) {
	        boolean isRunning = false;
	        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(Constants.RETRIVE_SERVICE_COUNT);

	        if(null == serviceInfos || serviceInfos.size() < 1) {
	            return false;
	        }

	        for(int i = 0; i < serviceInfos.size(); i++) {
	            if(serviceInfos.get(i).service.getClassName().contains(className)) {
	                isRunning = true;
	                break;
	            }
	        }
	        Log.i("ServiceUtil-AlarmManager", className + " isRunning =  " + isRunning);
	        return isRunning;
	    }

	public static void setServiceAlarm(Context context)
    {
		   Log.i("ServiceUtil-AlarmManager", "invokeTimerPOIService wac called.." );
	        PendingIntent alarmSender = null;
	        Intent startIntent = new Intent(context, UploadFailRecordService.class);
	        startIntent.setAction(Constants.POI_SERVICE_ACTION);
	        try {
	            alarmSender = PendingIntent.getService(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	        } catch (Exception e) {
	            Log.i("ServiceUtil-AlarmManager", "failed to start " + e.toString());
	        }
	        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
	        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.ELAPSED_TIME_F, alarmSender);
    }
	public void uploadrecord()
	{
		Cursor cars = mgr.queryTheCursorCarsFailUpload();
		List<CarsFailUploadDataEntity> list_c = new ArrayList<CarsFailUploadDataEntity>();

		if (cars.getCount() != 0) {
			System.out.println("有上传失败的车辆信息需要重传");
			while (cars.moveToNext()) {
				CarsFailUploadDataEntity carsfailupload = new CarsFailUploadDataEntity();
				carsfailupload.setUserID(cars.getString(cars
						.getColumnIndex("userID")));
				carsfailupload.setBaseStationID(cars.getString(cars
						.getColumnIndex("baseStationID")));
				carsfailupload.setIsOverWeight(cars.getString(cars
						.getColumnIndex("isOverWeight")));
				carsfailupload
						.setStatus4PermitRunCert(cars.getString(cars
								.getColumnIndex("status4PermitRunCert")));
				carsfailupload
						.setStatus4TransportCert_Head(cars.getString(cars
								.getColumnIndex("status4TransportCert_Head")));
				carsfailupload
						.setStatus4TransportCert_Tail(cars.getString(cars
								.getColumnIndex("status4TransportCert_Tail")));
				carsfailupload
						.setStatus4InsuranceCert_Head(cars.getString(cars
								.getColumnIndex("status4InsuranceCert_Head")));
				carsfailupload
						.setStatus4InsuranceCert_Cargo(cars.getString(cars
								.getColumnIndex("status4InsuranceCert_Cargo")));
				carsfailupload
						.setStatus4InsuranceCert_Tail(cars.getString(cars
								.getColumnIndex("status4InsuranceCert_Tail")));
				carsfailupload
						.setStatus4SpecialEquipUsage(cars.getString(cars
								.getColumnIndex("status4SpecialEquipUsage")));
				carsfailupload.setPlateNumber(cars.getString(cars
						.getColumnIndex("plateNumber")));
				carsfailupload.setID(cars.getInt(cars
						.getColumnIndex("ID")));
				list_c.add(carsfailupload);
			}
			cars.close();
			for (CarsFailUploadDataEntity carsFailUploadDataEntity : list_c) {
				postcarcheckrecord(carsFailUploadDataEntity);
				if (IsOK) {
					mgr.DeleteCarsFailUpload(carsFailUploadDataEntity
							.getID());
					System.out.println("上传成功");
				}
			}
		} else {
			System.out.println("没有上传失败的车辆信息需要重传");
		}
		/**
		 * 重传人员记录
		 */

		Cursor people = mgr.queryTheCursorPeopleFailUpload();
		List<PeopleFailUploadDataEntity> list_p = new ArrayList<PeopleFailUploadDataEntity>();

		if (people.getCount() != 0) {
			System.out.println("有上传失败的人员信息需要重传");
			while (people.moveToNext()) {
				PeopleFailUploadDataEntity peoplefailupload = new PeopleFailUploadDataEntity();
				peoplefailupload.setUserID(people.getString(people
						.getColumnIndex("userID")));
				peoplefailupload.setBaseStationID(people
						.getString(people
								.getColumnIndex("baseStationID")));
				peoplefailupload
						.setStatus4DriverLicense(people.getString(people
								.getColumnIndex("status4DriverLicense")));
				peoplefailupload.setStatus4JobCert(people
						.getString(people
								.getColumnIndex("status4JobCert")));
				peoplefailupload.setID(people.getInt(people
						.getColumnIndex("ID")));
				peoplefailupload.setDriverName(people.getString(people
						.getColumnIndex("driverName")));
				list_p.add(peoplefailupload);
			}
			people.close();
			for (PeopleFailUploadDataEntity peopleFailUploadDataEntity : list_p) {
				postpersoncheckrecord(peopleFailUploadDataEntity);
				if (IsOK) {

					mgr.DeletePeopleFailUpload(peopleFailUploadDataEntity
							.getID());
					System.out.println("上传成功");
				}
			}

		} else {
			System.out.println("没有上传失败的人员信息需要重传");
		}
//		stopSelf();
	}
	private class MyThread extends Thread {
		@Override
		public void run() {

			while (flag) {

				try {
					// 每个1分钟向服务器发送一次请求
					Thread.sleep(60000);
					System.out.println("发送请求");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/**
				 * 重传车辆记录
				 */

				Cursor cars = mgr.queryTheCursorCarsFailUpload();
				List<CarsFailUploadDataEntity> list_c = new ArrayList<CarsFailUploadDataEntity>();

				if (cars.getCount() != 0) {
					System.out.println("有上传失败的车辆信息需要重传");
					while (cars.moveToNext()) {
						CarsFailUploadDataEntity carsfailupload = new CarsFailUploadDataEntity();
						carsfailupload.setUserID(cars.getString(cars
								.getColumnIndex("userID")));
						carsfailupload.setBaseStationID(cars.getString(cars
								.getColumnIndex("baseStationID")));
						carsfailupload.setIsOverWeight(cars.getString(cars
								.getColumnIndex("isOverWeight")));
						carsfailupload
								.setStatus4PermitRunCert(cars.getString(cars
										.getColumnIndex("status4PermitRunCert")));
						carsfailupload
								.setStatus4TransportCert_Head(cars.getString(cars
										.getColumnIndex("status4TransportCert_Head")));
						carsfailupload
								.setStatus4TransportCert_Tail(cars.getString(cars
										.getColumnIndex("status4TransportCert_Tail")));
						carsfailupload
								.setStatus4InsuranceCert_Head(cars.getString(cars
										.getColumnIndex("status4InsuranceCert_Head")));
						carsfailupload
								.setStatus4InsuranceCert_Cargo(cars.getString(cars
										.getColumnIndex("status4InsuranceCert_Cargo")));
						carsfailupload
								.setStatus4InsuranceCert_Tail(cars.getString(cars
										.getColumnIndex("status4InsuranceCert_Tail")));
						carsfailupload
								.setStatus4SpecialEquipUsage(cars.getString(cars
										.getColumnIndex("status4SpecialEquipUsage")));
						carsfailupload.setPlateNumber(cars.getString(cars
								.getColumnIndex("plateNumber")));
						carsfailupload.setID(cars.getInt(cars
								.getColumnIndex("ID")));
						list_c.add(carsfailupload);
					}
					cars.close();
					for (CarsFailUploadDataEntity carsFailUploadDataEntity : list_c) {
						postcarcheckrecord(carsFailUploadDataEntity);
						if (IsOK) {
							mgr.DeleteCarsFailUpload(carsFailUploadDataEntity
									.getID());
							System.out.println("上传成功");
						}
					}
				} else {
					System.out.println("没有上传失败的车辆信息需要重传");
				}
				/**
				 * 重传人员记录
				 */

				Cursor people = mgr.queryTheCursorPeopleFailUpload();
				List<PeopleFailUploadDataEntity> list_p = new ArrayList<PeopleFailUploadDataEntity>();

				if (people.getCount() != 0) {
					System.out.println("有上传失败的人员信息需要重传");
					while (people.moveToNext()) {
						PeopleFailUploadDataEntity peoplefailupload = new PeopleFailUploadDataEntity();
						peoplefailupload.setUserID(people.getString(people
								.getColumnIndex("userID")));
						peoplefailupload.setBaseStationID(people
								.getString(people
										.getColumnIndex("baseStationID")));
						peoplefailupload
								.setStatus4DriverLicense(people.getString(people
										.getColumnIndex("status4DriverLicense")));
						peoplefailupload.setStatus4JobCert(people
								.getString(people
										.getColumnIndex("status4JobCert")));
						peoplefailupload.setID(people.getInt(people
								.getColumnIndex("ID")));
						peoplefailupload.setDriverName(people.getString(people
								.getColumnIndex("driverName")));
						list_p.add(peoplefailupload);
					}
					people.close();
					for (PeopleFailUploadDataEntity peopleFailUploadDataEntity : list_p) {
						postpersoncheckrecord(peopleFailUploadDataEntity);
						if (IsOK) {

							mgr.DeletePeopleFailUpload(peopleFailUploadDataEntity
									.getID());
							System.out.println("上传成功");
						}
					}

				} else {
					System.out.println("没有上传失败的人员信息需要重传");
				}
			}
		}
	}

	boolean postcarcheckrecord(final CarsFailUploadDataEntity carsFailUpload) {

		Url url = new Url();
		RequestHead requesthead = new RequestHead();
		RequestParams params = new RequestParams();

		params.addHeader("userName", requesthead.getUserName());
		params.addHeader("requestDatetime", requesthead.getRequestDatetime());
		params.addHeader("appID", requesthead.getAppID());
		params.addHeader("signature", requesthead.getSignature());

		params.addBodyParameter("userID", carsFailUpload.getUserID());
		params.addBodyParameter("baseStationID",
				carsFailUpload.getBaseStationID());
		params.addBodyParameter("isOverWeight",
				carsFailUpload.getIsOverWeight());
		params.addBodyParameter("Status4PermitRunCert",
				carsFailUpload.getStatus4PermitRunCert());
		params.addBodyParameter("status4TransportCertHead",
				carsFailUpload.getStatus4TransportCert_Head());
		params.addBodyParameter("status4TransportCertTail",
				carsFailUpload.getStatus4TransportCert_Tail());
		params.addBodyParameter("status4InsuranceCertHead",
				carsFailUpload.getStatus4InsuranceCert_Head());
		params.addBodyParameter("status4InsuranceCertCargo",
				carsFailUpload.getStatus4InsuranceCert_Cargo());
		params.addBodyParameter("status4InsuranceCertTail",
				carsFailUpload.getStatus4InsuranceCert_Tail());
		params.addBodyParameter("status4SpecialEquipUsage",
				carsFailUpload.getStatus4SpecialEquipUsage());
		// params.addBodyParameter("checkTime", "2008-08-08 12:12:12");

		HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
		http.send(HttpMethod.POST, url.getCarcheckrecord(), params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println(responseInfo.result);
						Header[] headers = responseInfo.getAllHeaders();
						if (headers != null)
							for (Header head : headers) {
								System.out.println(head.getName() + ":"
										+ head.getValue());
							}

						UploadTaskDataEntity entity = new UploadTaskDataEntity();
						entity.setCheckcategory(carsFailUpload.getPlateNumber());
						entity.setTime(new DateFormat().getDate());
						entity.setUploadcondition("上传成功");
						mgr.addUploadTask_Cars(entity);
						IsOK = true;
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// System.out.println("onFailure");
						// UploadTaskDataEntity entity = new
						// UploadTaskDataEntity();
						// entity.setCheckcategory(carnum.getText().toString());
						// entity.setTime(new DateFormat().getDate());
						// entity.setUploadcondition("上传失败");
						// mgr.addUploadTask_Cars(entity);
						IsOK = false;
					}

				});
		return IsOK;
	}

	boolean postpersoncheckrecord(
			final PeopleFailUploadDataEntity peopleFailUpload) {

		Url url = new Url();
		RequestHead requesthead = new RequestHead();
		RequestParams params = new RequestParams();

		params.addHeader("userName", requesthead.getUserName());
		params.addHeader("requestDatetime", requesthead.getRequestDatetime());
		params.addHeader("appID", requesthead.getAppID());
		params.addHeader("signature", requesthead.getSignature());
		params.addBodyParameter("userID", peopleFailUpload.getUserID());
		params.addBodyParameter("baseStationID",
				peopleFailUpload.getBaseStationID());
		params.addBodyParameter("status4DriverLicense",
				peopleFailUpload.getStatus4DriverLicense());
		params.addBodyParameter("status4JobCert",
				peopleFailUpload.getStatus4JobCert());
		// params.addBodyParameter("checkTime", "2008-08-08 12:12:12");

		HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
		http.send(HttpMethod.POST, url.getPersoncheckrecord(), params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println(responseInfo.result);
						Header[] headers = responseInfo.getAllHeaders();
						if (headers != null)
							for (Header head : headers) {
								System.out.println(head.getName() + ":"
										+ head.getValue());
							}

						UploadTaskDataEntity entity = new UploadTaskDataEntity();
						entity.setCheckcategory(peopleFailUpload
								.getDriverName());
						entity.setTime(new DateFormat().getDate());
						entity.setUploadcondition("上传成功");
						mgr.addUploadTask_People(entity);
						IsOK = true;
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// System.out.println("onFailure");
						// UploadTaskDataEntity entity = new
						// UploadTaskDataEntity();
						// entity.setCheckcategory(peopleFailUpload.getDriverName());
						// entity.setTime(new DateFormat().getDate());
						// entity.setUploadcondition("上传失败");
						// mgr.addUploadTask_People(entity);
						IsOK = false;
					}

				});
		return IsOK;
	}

}
