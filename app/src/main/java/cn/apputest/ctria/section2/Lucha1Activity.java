package cn.apputest.ctria.section2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.apputest.ctria.data.CarBasicInfoDataEntity;
import cn.apputest.ctria.data.CargoEmergencyMethodDataEntity;
import cn.apputest.ctria.data.CarsFailUploadDataEntity;
import cn.apputest.ctria.data.LatestTransportRecordDataEntity;
import cn.apputest.ctria.data.UploadTaskDataEntity;
import cn.apputest.ctria.myapplication.DateFormat;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.myapplication.RequestHead;
import cn.apputest.ctria.myapplication.Unicode;
import cn.apputest.ctria.myapplication.Url;
import cn.apputest.ctria.section3.Section3Activity;
import cn.apputest.ctria.service.Constants;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;
import com.fntech.m10.u1.rfid.reader.DCService;
import com.fntech.m10.u1.rfid.reader.IDCService;
import com.fntech.m10.u1.rfid.reader.InvalidParamException;
import com.fntech.m10.u1.rfid.reader.Manager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 
 * @author Shihao Shen 车辆路查路检 主页面
 * 
 */

public class Lucha1Activity extends Activity implements OnClickListener {
	private TextView accessalertTextView;
	private Context context;
	private EditText carnum;
	private TextView giftname, weight;
	private TextView errorT;
	private TextView zheng2, zheng3, zheng4, zheng5;
	private RelativeLayout chancel1; // 取消的英文写错了
	private Superbutton uploadrecordlucha;
	private DBHelper helper;
	private DBManager mgr;
	private String car_platenum;
	private String car_platenum_gua;
	String CargoID;
	boolean IsNetWorkReady = true;
	double Weight2;
	double weightW = 0;
	int IsPermitCard = 0;
	int IsInsuranceCard_Head = 0;
	int IsInsuranceCard_Cargo = 0;
	int IsInsuranceCard_Tail = 0;
	int IsSpecialEquipment = 0;
	int IsTransportCard_Head = 0;
	int IsTransportCard_Tail = 0;

	int IsOverWeight = 0;
	private SharedPreferences preferences;
	public static final String KEY_FLAG = "flag";
	public static final String FLAG_Permit = "FLAG_Permit";
	Toast toast;
	public boolean hastoast = false;
	private Manager manager;
	public boolean scanSuccess = false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.section2activity_lucha1);
		// Log.i("Lucha1Activity", "Lucha1Activity");
		// preferences = getSharedPreferences(KEY_FLAG, MODE_PRIVATE);
		inti();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("InventoryActivity-onResume");
		if (manager == null) {
			try {
				manager = Manager.getInstance();
			} catch (Exception e) {
				e.printStackTrace();
				// finish();
			}
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		manager.suspend();
	}

	public void openInterface() {
		manager.openInterface();

	}

	void inti() {

		if (manager == null) {
			try {
				manager = Manager.getInstance();
			} catch (Exception e) {
				e.printStackTrace();
				// finish();
			}
		}
		openInterface();

		context = this.getParent();
		toast = new Toast(context);

		preferences = context.getSharedPreferences(Login.FILE_USER,
				MODE_PRIVATE);
		String DBName = preferences.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		isNetworkConnected(context);

		carnum = (EditText) findViewById(R.id.carnum);
		giftname = (TextView) findViewById(R.id.giftname);
		weight = (TextView) findViewById(R.id.weight);

		errorT = (TextView) findViewById(R.id.warning);

		accessalertTextView = (TextView) findViewById(R.id.zheng1);
		zheng2 = (TextView) findViewById(R.id.zheng2);
		zheng3 = (TextView) findViewById(R.id.zheng3);
		zheng4 = (TextView) findViewById(R.id.zheng4);
		zheng5 = (TextView) findViewById(R.id.zheng5);
		accessalertTextView.setOnClickListener(this);
		zheng2.setOnClickListener(this);
		zheng3.setOnClickListener(this);
		zheng4.setOnClickListener(this);
		zheng5.setOnClickListener(this);
		uploadrecordlucha = (Superbutton) findViewById(R.id.uploadrecordlucha);
		chancel1 = (RelativeLayout) findViewById(R.id.chancel1);
		giftname.setOnClickListener(this);
		weight.setOnClickListener(this);
		uploadrecordlucha.setOnClickListener(this);
		chancel1.setOnClickListener(this);
		carnum.addTextChangedListener(mTextWatcher);
		carnum.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_DEL) {

					cancelBB();

				}
				return false;
			}
		});
	}

	void SweepCard() {
		IDCService dcservice = new DCService();
		try {
			Set<String> scanresult = dcservice.inventory();
			if (scanresult == null || scanresult.size() == 0) {

			} else {
				scanSuccess = true;
				
				Iterator<String> iter = scanresult.iterator();
				String tagCode = "";
				if (iter.hasNext()) {
					tagCode = iter.next();
				}
				Map<String, String> mapResults = dcservice.check(tagCode);

				carnum.setText(mgr.transPlateNumber(tagCode));
				// carnum.setText("123567");
				CargoEmergencyMethodDataEntity ProductName = mgr
						.queryCargoEmergencyName(mapResults.get("ProductNo"));
				giftname.setText(ProductName.getCargoname());
				CargoID = mapResults.get("ProductNo");
				String weightfornow = mapResults.get("EnterpriseWeight");
				weight.setText(weightfornow);
			}
			SweepCardData();

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void cancelB() {
		carnum.setText("");
		giftname.setText("");
		weight.setText("");
		errorT.setText("");
		accessalertTextView
				.setTextColor(getResources().getColor(R.color.black));
		zheng2.setTextColor(getResources().getColor(R.color.black));
		zheng3.setTextColor(getResources().getColor(R.color.black));
		zheng4.setTextColor(getResources().getColor(R.color.black));
		zheng5.setTextColor(getResources().getColor(R.color.black));
	}

	void cancelBB() {
		giftname.setText("");
		weight.setText("");
		errorT.setText("");
		accessalertTextView
				.setTextColor(getResources().getColor(R.color.black));
		zheng2.setTextColor(getResources().getColor(R.color.black));
		zheng3.setTextColor(getResources().getColor(R.color.black));
		zheng4.setTextColor(getResources().getColor(R.color.black));
		zheng5.setTextColor(getResources().getColor(R.color.black));
	}

	public void onClick(View arg0) {

		switch (arg0.getId()) {

		case R.id.zheng1:
			if (carnum.getText().toString().equals("")) {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "车牌号码为空", Toast.LENGTH_SHORT)
							.show();
					mHandler.postDelayed(mRunnable, 2000);
				}

			} else {
				new Dialog_PermitCard().alert_PermitCardDialog(context, carnum);
			}
			break;

		case R.id.zheng2:
			if (carnum.getText().toString().equals("")) {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "车牌号码为空", Toast.LENGTH_SHORT)
							.show();
					mHandler.postDelayed(mRunnable, 2000);
				}
			} else {
				new Dialog_TransportCard().alert_TransportCardDialog(context,
						carnum, car_platenum_gua);
			}
			break;
		case R.id.zheng3:
			if (carnum.getText().toString().equals("")) {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "车牌号码为空", Toast.LENGTH_SHORT)
							.show();
					mHandler.postDelayed(mRunnable, 2000);
				}
			} else {
				new Dialog_InsuranceCard().alert_InsuranceCardDialog(context,
						carnum, car_platenum_gua);
			}
			break;
		case R.id.zheng4:

			break;
		case R.id.zheng5:
			if (carnum.getText().toString().equals("")) {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "车牌号码为空", Toast.LENGTH_SHORT)
							.show();
					mHandler.postDelayed(mRunnable, 2000);
				}
			} else {
				new Dialog_SpecialEquipment().alert_SpecialEquipmentDialog(
						context, car_platenum_gua);
			}
			break;
		case R.id.giftname:
			if (!StringUtils.isNotBlank(giftname.getText().toString())) {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "请先扫卡或输入正确的车牌号码",
							Toast.LENGTH_SHORT).show();
					mHandler.postDelayed(mRunnable, 2000);
				}
			} else if (!StringUtils.isNotBlank(CargoID)) {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "没有该货物的信息", Toast.LENGTH_SHORT)
							.show();
					mHandler.postDelayed(mRunnable, 2000);
				}
			} else {
				new Dialog_Cargoname().alert_CargonameDialog(context, CargoID);
			}

			break;
		case R.id.weight:

			if (Weight2 == 0) {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "请先扫卡或输入正确的车牌号码",
							Toast.LENGTH_SHORT).show();
					mHandler.postDelayed(mRunnable, 2000);
				}
			} else {
				new Dialog_weight().alert_WeightDialog(context, carnum);
			}
			break;
		case R.id.uploadrecordlucha:
			uploadrecordlucha();
			break;
		case R.id.chancel1:
			cancelB();
			break;
		default:
			break;
		}
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(1);
		}
	};

	Handler mHandler;

	{
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 3s后执行代码
				hastoast = false;
			}
		};
	}

	void uploadrecordlucha() {
		if (carnum.getText().toString().equals("")) {
			if (hastoast == false) {
				hastoast = true;
				Toast.makeText(context, "车牌号码为空", Toast.LENGTH_SHORT).show();
				mHandler.postDelayed(mRunnable, 2000);
			}
		} else {
			if (carnum.getText().toString().length() == 7) {
				postcarcheckrecord();
				postLog();
			} else {
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "车牌号码不足7位", Toast.LENGTH_SHORT)
							.show();
					mHandler.postDelayed(mRunnable, 2000);
				}
			}
		}
	}

	// 上次车辆路查记录
	void postcarcheckrecord() {

		Url url = new Url();
		RequestHead requesthead = new RequestHead();
		RequestParams params = new RequestParams();

		params.addHeader("userName", requesthead.getUserName());
		params.addHeader("requestDatetime", requesthead.getRequestDatetime());
		params.addHeader("appID", requesthead.getAppID());
		params.addHeader("signature", requesthead.getSignature());

		// String permitflag = preferences.getString(FLAG_Permit, "");
		preferences = getSharedPreferences(Login.FILE_USER,
				Section3Activity.MODE_PRIVATE);
		final String username = preferences.getString(Login.KEY_NAME, "1");
		params.addBodyParameter("userID", username);
		params.addBodyParameter("baseStationID", getLocalMacAddress());
		params.addBodyParameter("isOverWeight", String.valueOf(IsOverWeight));
		params.addBodyParameter("Status4PermitRunCert",
				String.valueOf(IsPermitCard));
		params.addBodyParameter("status4TransportCertHead",
				String.valueOf(IsTransportCard_Head));
		params.addBodyParameter("status4TransportCertTail",
				String.valueOf(IsTransportCard_Tail));
		params.addBodyParameter("status4InsuranceCertHead",
				String.valueOf(IsInsuranceCard_Head));
		params.addBodyParameter("status4InsuranceCertCargo",
				String.valueOf(IsInsuranceCard_Cargo));
		params.addBodyParameter("status4InsuranceCertTail",
				String.valueOf(IsInsuranceCard_Tail));
		params.addBodyParameter("status4SpecialEquipUsage",
				String.valueOf(IsSpecialEquipment));
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
						if (IsNetWorkReady) {
							UploadTaskDataEntity entity = new UploadTaskDataEntity();
							entity.setCheckcategory(carnum.getText().toString()
									.toUpperCase());
							entity.setTime(new DateFormat().getDate());
							entity.setUploadcondition("上传成功");
							mgr.addUploadTask_Cars(entity);
							if (hastoast == false) {
								hastoast = true;
								Toast.makeText(context, "上传成功",
										Toast.LENGTH_SHORT).show();
								mHandler.postDelayed(mRunnable, 2000);
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						IsNetWorkReady = false;
						if (IsNetWorkReady == false) {
							UploadTaskDataEntity entity = new UploadTaskDataEntity();
							entity.setCheckcategory(carnum.getText().toString());
							entity.setTime(new DateFormat().getDate());
							entity.setUploadcondition("上传失败");
							mgr.addUploadTask_Cars(entity);
							if (hastoast == false) {
								hastoast = true;
								Toast.makeText(context, "上传失败",
										Toast.LENGTH_SHORT).show();
								mHandler.postDelayed(mRunnable, 2000);
							}
							CarsFailUploadDataEntity carsfailupload = new CarsFailUploadDataEntity();
							carsfailupload.setUserID(username);
							carsfailupload
									.setBaseStationID(getLocalMacAddress());
							carsfailupload.setIsOverWeight(String
									.valueOf(IsOverWeight));
							carsfailupload.setStatus4PermitRunCert(String
									.valueOf(IsPermitCard));
							carsfailupload.setStatus4TransportCert_Head(String
									.valueOf(IsTransportCard_Head));
							carsfailupload.setStatus4TransportCert_Tail(String
									.valueOf(IsTransportCard_Tail));
							carsfailupload.setStatus4InsuranceCert_Head(String
									.valueOf(IsInsuranceCard_Head));
							carsfailupload.setStatus4InsuranceCert_Cargo(String
									.valueOf(IsInsuranceCard_Cargo));
							carsfailupload.setStatus4InsuranceCert_Tail(String
									.valueOf(IsInsuranceCard_Tail));
							carsfailupload.setStatus4SpecialEquipUsage(String
									.valueOf(IsSpecialEquipment));
							carsfailupload.setPlateNumber(carnum.getText()
									.toString());

							mgr.addCarsFailUpload(carsfailupload);
						}
					}
				});
	}

	// 获取车辆基本信息
	void getCarbasicinfo() {
		CarBasicInfoDataEntity carbasicinfo = new CarBasicInfoDataEntity();
		String cariD = carnum.getText().toString();
		carbasicinfo = mgr.queryTheCursorCarBasicInfo(cariD, car_platenum_gua);

		IsInsuranceCard_Head = carbasicinfo.getStatus4InsuranceCertHead();
		IsInsuranceCard_Cargo = carbasicinfo.getStatus4InsuranceCertCargo();
		IsInsuranceCard_Tail = carbasicinfo.getStatus4InsuranceCertTail();
		IsPermitCard = carbasicinfo.getStatus4PermitRunCert();
		IsSpecialEquipment = carbasicinfo.getStatus4SEquipCheck();
		IsTransportCard_Head = carbasicinfo.getStatus4TransportCertHead();
		IsTransportCard_Tail = carbasicinfo.getStatus4TransportCertTail();

		if (carbasicinfo.getTraction_quality() != 0) {

			// 有挂车
			if (Weight2 == 0) {
				errorT.setText("");
			} else {
				// try {
				// carbasicinfo.getMaxweight();
				// } catch (Exception e) {
				// carbasicinfo.setMaxweight(0);
				// }

				if (!IsOverWeight(
						Weight2,
						carbasicinfo.getReorganize_quality()
								+ carbasicinfo.getMaxweight_tail())) {
					errorT.setText("正常载重");
					errorT.setTextColor(getResources().getColor(R.color.black));
					// weightaImageButton.isShown();
				} else {
					IsOverWeight = 1;
					double a = (Weight2 - carbasicinfo.getReorganize_quality() - carbasicinfo
							.getMaxweight_tail())
							/ carbasicinfo.getCheck_quality_tail() * 100;
					DecimalFormat df = new DecimalFormat("######0.0");

					errorT.setText("超重 " + df.format(a) + "%");
					errorT.setTextColor(getResources().getColor(
							R.color.warningtext));
				}
			}
		} else {
			// 无挂车
			if (Weight2 == 0) {
				errorT.setText("");
			} else {
				// try {
				// carbasicinfo.getMaxweight();
				// } catch (Exception e) {
				// carbasicinfo.setMaxweight(0);
				// }
				if (!IsOverWeight(Weight2, carbasicinfo.getMaxweight())) {
					errorT.setText("正常载重");
					errorT.setTextColor(getResources().getColor(R.color.black));
					// weightaImageButton.isShown();
				} else {
					IsOverWeight = 1;
					double a = (Weight2 - carbasicinfo.getMaxweight())
							/ carbasicinfo.getCheck_quality() * 100;
					DecimalFormat df = new DecimalFormat("######0.0");

					errorT.setText("超重 " + df.format(a) + "%");
					errorT.setTextColor(getResources().getColor(
							R.color.warningtext));
				}
			}

		}

		if (IsInsuranceCard_Head == 1 || IsInsuranceCard_Cargo == 1
				|| IsInsuranceCard_Tail == 1) {
			zheng3.setTextColor(getResources().getColor(R.color.warningtext));
		} else if (IsInsuranceCard_Head == 0 && IsInsuranceCard_Cargo == 0
				&& IsInsuranceCard_Tail == 0) {
			zheng3.setTextColor(getResources().getColor(R.color.black));
		} else if (IsInsuranceCard_Head == -1 || IsInsuranceCard_Cargo == -1
				|| IsInsuranceCard_Tail == -1) {
			zheng3.setTextColor(getResources().getColor(R.color.warningtext));
		}

		if (IsPermitCard == 1) {
			accessalertTextView.setTextColor(getResources().getColor(
					R.color.warningtext));
		} else if (IsPermitCard == 0) {
			accessalertTextView.setTextColor(getResources().getColor(
					R.color.black));
		} else if (IsPermitCard == -1) {
			accessalertTextView.setTextColor(getResources().getColor(
					R.color.warningtext));
		}
		if (IsSpecialEquipment == 1) {
			zheng5.setTextColor(getResources().getColor(R.color.warningtext));
		} else if (IsSpecialEquipment == 0) {
			zheng5.setTextColor(getResources().getColor(R.color.black));
		} else if (IsSpecialEquipment == -1) {
			zheng5.setTextColor(getResources().getColor(R.color.warningtext));
		}
		if (IsTransportCard_Head == 1 || IsTransportCard_Tail == 1) {
			zheng2.setTextColor(getResources().getColor(R.color.warningtext));
		} else if (IsTransportCard_Head == 0 && IsTransportCard_Tail == 0) {
			zheng2.setTextColor(getResources().getColor(R.color.black));
		} else if (IsTransportCard_Head == -1 || IsTransportCard_Tail == -1) {
			zheng2.setTextColor(getResources().getColor(R.color.warningtext));
		}
	}

	// 获取车辆重量信息
	void getWeight(final EditText carnum) {
		LatestTransportRecordDataEntity latesttransportrecord = new LatestTransportRecordDataEntity();
		String cariD = carnum.getText().toString();
		if (StringUtils.isNotBlank(cariD)) {

			latesttransportrecord = mgr.queryLatestTransportRecord(cariD);
			car_platenum_gua = latesttransportrecord.getPlateNumber_gua();

			if (StringUtils.isNotBlank(weight.getText().toString().trim())) {
				Weight2 = Double
						.parseDouble(weight.getText().toString().trim());
				getCarbasicinfo();
			} else {

				if (latesttransportrecord.getWeight() != null) {

					Weight2 = latesttransportrecord.getWeight();
					weight.setText("  " + Weight2);
					CargoID = mgr.queryCargoEmergencyID(latesttransportrecord
							.getCargoname());
					giftname.setText("  "
							+ latesttransportrecord.getCargoname());
					getCarbasicinfo();
				} else {
					if (hastoast == false) {
						hastoast = true;
						Toast.makeText(context, "没有该车辆号牌信息", Toast.LENGTH_SHORT)
								.show();
						mHandler.postDelayed(mRunnable, 2000);
					}
					Weight2 = 0;
					getCarbasicinfo();// ///////////////////////////////
					weight.setText("");
					// CargoID
					// =mgr.queryCargoEmergencyID(latesttransportrecord.getCargoname());
					giftname.setText("");
				}
			}
		} else {
			if (hastoast == false) {
				hastoast = true;
				Toast.makeText(context, "请重新扫卡", Toast.LENGTH_SHORT).show();
				mHandler.postDelayed(mRunnable, 2000);
			}

		}

	}

	// ///
	LatestTransportRecordDataEntity parseResultW(String result) {
		LatestTransportRecordDataEntity latesttransportrecord = new LatestTransportRecordDataEntity();
		try {
			JSONObject object = new JSONObject(result);
			latesttransportrecord.setPlateNumber(object
					.getString("PLATE_NUMBER"));
			latesttransportrecord.setCargoname(object.getString("CARGO_NAME"));
			latesttransportrecord.setWeight(object.getInt("WEIGHT"));
			latesttransportrecord.setPlateNumber_gua(object
					.getString("TAIL_PLATE_NUMBER"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latesttransportrecord;
	}

	void SweepCardData() {
		getWeight(carnum);
		// CargoID = "0cc175b9c0f1b6a831c399e269772661";
		// giftname.setText(" "+mgr.queryCargoEmergencyName(CargoID).getCargoname());

	}

	/**
	 * 
	 * @param Weight2
	 *            车辆重量
	 * @param MaxWeight
	 *            最大重量
	 * @return 是否超重
	 */
	boolean IsOverWeight(double Weight2, double MaxWeight) {
		boolean IsOverWeight = false;
		if (Weight2 <= MaxWeight) {
			return IsOverWeight;
		} else {
			IsOverWeight = true;
			return IsOverWeight;
		}

	}

	/**
	 * 上传日志
	 */
	void postLog() {
		Url url = new Url();
		RequestHead requesthead = new RequestHead();
		RequestParams params = new RequestParams();

		params.addHeader("userName", requesthead.getUserName());
		System.out.println(requesthead.getUserName());
		params.addHeader("requestDatetime", requesthead.getRequestDatetime());
		System.out.println(requesthead.getRequestDatetime());
		params.addHeader("appID", requesthead.getAppID());
		System.out.println(requesthead.getAppID());
		params.addHeader("signature", requesthead.getSignature());
		System.out.println(requesthead.getSignature());

		SharedPreferences preferencesuser = getSharedPreferences(
				Login.FILE_USER, MODE_PRIVATE);
		String username = preferencesuser.getString(Login.KEY_NAME, "1");

		params.addBodyParameter("userName", username);
		params.addBodyParameter("deviceName", getDeviceName());
		params.addBodyParameter("deviceIP", getLocalIPAddress());
		params.addBodyParameter("deviceMac", getLocalMacAddress());
		params.addBodyParameter("appID", requesthead.getAppID());
		params.addBodyParameter("logDescription", carnum.getText().toString()
				+ "：上传车辆路查记录");
		params.addBodyParameter("logLevel", "1");
		params.addBodyParameter("logAction", "上传");
		params.addBodyParameter("logTime", requesthead.getRequestDatetime());

		HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
		http.send(HttpMethod.POST, url.getLog(), params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println(responseInfo.result);
						Header[] headers = responseInfo.getAllHeaders();
						for (Header head : headers) {

							System.out.println(head.getName() + ":"
									+ head.getValue());

							if (head.getName().equals("resultCode")) {
								String responseCode = head.getValue();
								if (responseCode.equals("0")) {
									System.out.println("Log已上传");
								} else {
									System.out.println("Log上传失败");
								}

							}
							if (head.getName().equals("resultMsg")) {

								System.out.println(head.getName()
										+ ":"
										+ new Unicode().unicodeToString(head
												.getValue()));
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
					}
				});
	}

	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				IsNetWorkReady = true;
				return mNetworkInfo.isAvailable();
			}
		}
		IsNetWorkReady = false;
		return false;
	}

	public String getLocalIPAddress() {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			String ip = getLocalIpAddressGPRS();
			return ip;
		} else {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			String ip = intToIp(ipAddress);
			return ip;
		}
	}

	private String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	public String getLocalIpAddressGPRS() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	public String getDeviceName() {
		// TODO Auto-generated method stub
		String DeviceName = null;
		DeviceName = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();
		return DeviceName;
	}

	/**
	 * 编辑框监听
	 */
	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = carnum.getSelectionStart();
			editEnd = carnum.getSelectionEnd();
			// mTextView.setText("您输入了" + temp.length() + "个字符");
			if (temp.length() == 7) {
				SweepCardData();
			}
			if (temp.length() == 0) {
				cancelBB();
			}
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mgr.closeDB();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 0) {
			if(!scanSuccess){
				System.out.println(scanSuccess);
				SweepCard();
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == 0) {
			scanSuccess = false;
		}
		return super.onKeyUp(keyCode, event);
	}
}
