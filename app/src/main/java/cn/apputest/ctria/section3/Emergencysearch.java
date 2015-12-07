package cn.apputest.ctria.section3;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.apputest.ctria.data.CargoEmergencyMethodDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;
import com.example.dcrc2.R;
import com.fntech.m10.u1.rfid.reader.DCService;
import com.fntech.m10.u1.rfid.reader.IDCService;
import com.fntech.m10.u1.rfid.reader.InvalidParamException;
import com.fntech.m10.u1.rfid.reader.Manager;

/**
 * 
 * @author Shihao Shen 应急查询弹出框
 * 
 */
public class Emergencysearch {
	String responseCode = "0";
	Button emergencysearch, updatedata, passwordsetting, uploadtask, logout;
	String result1;
	RelativeLayout emergencysearch_sureButton, emergencysearch_cancelButton;
	EditText emergencysearch_cargoname, emergencysearch_tasknum;
	TextView emergencysearchTextView;
	boolean IsNetWorkReady = true;
	private DBManager mgr;
	private DBHelper helper;
	SharedPreferences preferencesuser;
	private Context context;
	private Manager manager;
	public boolean scanSuccess = false;
	Toast toast;
	public boolean hastoast = false;
	void alert_EmergencysearchDialog_inti(View vv) {
		emergencysearch_sureButton = (RelativeLayout) vv
				.findViewById(R.id.S3sure);
		emergencysearch_cancelButton = (RelativeLayout) vv
				.findViewById(R.id.S3chancel1);
		emergencysearch_cargoname = (EditText) vv
				.findViewById(R.id.S3cargoname);
		// emergencysearch_tasknum = (EditText)vv.findViewById(R.id.S3tasknum);

		emergencysearchTextView = (TextView) vv
				.findViewById(R.id.emergencysearchTextView);
		emergencysearchTextView.setClickable(false);
		emergencysearchTextView.setLongClickable(false);
		emergencysearchTextView.setMovementMethod(ScrollingMovementMethod
				.getInstance());

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		emergencysearch_sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (emergencysearch_cargoname.getText().toString().equals("")) {
					Toast.makeText(context, "货物名称为空", Toast.LENGTH_LONG).show();
				} else {
					// CargoEmergencyMethodDataEntity c = new
					// CargoEmergencyMethodDataEntity();
					String emergencysearch_cargonames = emergencysearch_cargoname
							.getText().toString();
					String emergencysearch_cargonameID = mgr
							.queryCargoEmergencyID(emergencysearch_cargonames);
					// System.out.println(emergencysearch_cargonameID);
					get(emergencysearch_cargonameID);
				}

			}
		});

	}

	CargoEmergencyMethodDataEntity alert_EmergencysearchDialog_Data(
			String emergencysearch_cargonameID) {

		CargoEmergencyMethodDataEntity cargo = new CargoEmergencyMethodDataEntity();
		cargo = mgr.queryCargoEmergency(emergencysearch_cargonameID);
		return cargo;
	}

	public void openInterface() {
		manager.openInterface();

	}

	public void alert_EmergencysearchDialog(Context context) {
		if (manager == null) {
			try {
				manager = Manager.getInstance();
			} catch (Exception e) {
				e.printStackTrace();
				// finish();
			}
		}
		toast = new Toast(context);
		openInterface();
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section3activity_emergencysearch, null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(vv);
		// dialog.getWindow().setLayout(720, 1100);
		// dialog.getWindow().setLayout(480, 570);
		Resources r = context.getResources();
		int x = (int) r.getDimension(R.dimen.x320);
		int y = (int) r.getDimension(R.dimen.y385);
		dialog.getWindow().setLayout(x, y);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		this.context = context;
		alert_EmergencysearchDialog_inti(vv);

		emergencysearch_cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					dialog.dismiss();
			}
		});

		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub

				if (keyCode == 0) {
					if (!scanSuccess) {
						System.out.println(scanSuccess);
						SweepCard();
						scanSuccess = true;
					}
				}

				return false;
			}

		});

		dialog.show();
	}

	void get(final String emergencysearch_cargonameID) {
		CargoEmergencyMethodDataEntity cargo = new CargoEmergencyMethodDataEntity();
		cargo = alert_EmergencysearchDialog_Data(emergencysearch_cargonameID);
		String method = mgr
				.queryCargoEmergencyMethod(emergencysearch_cargonameID);
		emergencysearch_cargoname.setText(cargo.getCargoname());
		emergencysearchTextView.setText(cargo.getCargoemergencymethod()
				+ method);
	}

	void SweepCard() {
		IDCService dcservice = new DCService();
		try {
			Set<String> scanresult = dcservice.inventory();
			if (scanresult == null || scanresult.size() == 0) {
				
				if (hastoast == false) {
					hastoast = true;
					Toast.makeText(context, "请重新扫卡", Toast.LENGTH_SHORT).show();
					mHandler.postDelayed(mRunnable, 2000);
				}
				scanSuccess = false;

			} else {

				Iterator<String> iter = scanresult.iterator();
				String tagCode = "";
				if (iter.hasNext()) {
					tagCode = iter.next();
				}
				Map<String, String> mapResults = dcservice.check(tagCode);

				CargoEmergencyMethodDataEntity ProductName = mgr
						.queryCargoEmergencyName(mapResults.get("ProductNo"));
				emergencysearch_cargoname.setText(ProductName.getCargoname());

				if (emergencysearch_cargoname.getText().toString().equals("")) {
					Toast.makeText(context, "货物名称为空", Toast.LENGTH_LONG).show();
				} else {
					get(mapResults.get("ProductNo"));
				}
				mHandler.postDelayed(scanRunnable, 5000);
			}

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

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 3s后执行代码
			if (msg.what == 1) {
				scanSuccess = false;
			}
			else
			{
				hastoast = false;
			}
		}
	};
	private Runnable scanRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(1);
		}
	};
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(2);
		}
	};

}
