package cn.apputest.ctria.section3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.service.UploadFailRecordService;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;
import com.fntech.m10.u1.rfid.reader.Manager;
/**
 * 
 * @author Shihao Shen 第三个主页面
 * 
 */
public class Section3Activity extends Activity implements OnClickListener {
	private Context context;
	RelativeLayout emergencysearch, updatedata, passwordsetting, uploadtask, logout;
	SharedPreferences preferencesuser;

	 private DBManager mgr;
	 private DBHelper helper;
	 
		private Manager manager;
		public boolean scanSuccess = false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.section3activity);

		inti();

	}

	void inti() {
		context = this.getParent();
		
//		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
//				context.MODE_PRIVATE);
//		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
//		helper = new DBHelper(context, DBName + "_DB");
//		mgr = new DBManager(helper);
		if (manager == null) {
			try {
				manager = Manager.getInstance();
			} catch (Exception e) {
				e.printStackTrace();
				// finish();
			}
		}
		
		emergencysearch = (RelativeLayout) findViewById(R.id.emergencysearch);
		updatedata = (RelativeLayout) findViewById(R.id.updatedata);
		passwordsetting = (RelativeLayout) findViewById(R.id.passwordsetting);
		uploadtask = (RelativeLayout) findViewById(R.id.uploadtask);
		logout = (RelativeLayout) findViewById(R.id.logout);

		emergencysearch.setOnClickListener(this);
		updatedata.setOnClickListener(this);
		uploadtask.setOnClickListener(this);
		passwordsetting.setOnClickListener(this);
		logout.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.emergencysearch:
			// alert_EmergencysearchDialog();
			new Emergencysearch().alert_EmergencysearchDialog(context);
			break;
		case R.id.updatedata:
			// alert_UpdatedataDialog();
			new Updatedata().alert_UpdatedataDialog(context);
			break;
		case R.id.uploadtask:
			// alert_UploadtaskDialog();
			new Uploadtask().alert_UploadtaskDialog(context);
			break;
		case R.id.passwordsetting:
			// alert_Passwordsetting();
			preferencesuser = getSharedPreferences(Login.FILE_USER,
					Section3Activity.MODE_PRIVATE);
			String username = preferencesuser.getString(Login.KEY_NAME, "1");
			new Passwordsetting().alert_Passwordsetting(context, username);

			break;
		case R.id.logout:
			// PreferenceManager.getDefaultSharedPreferences(this).edit()
			// .remove(Login.KEY_LOGIN).remove(Login.KEY_SHOWPASSWORD).commit();

			Intent intent1 = new Intent(Section3Activity.this,
					UploadFailRecordService.class);
			stopService(intent1);

			startActivity(new Intent(this, Login.class));
			finish();
			break;
		default:
			break;
		}
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// //应用的最后一个Activity关闭时应释放DB
	// mgr.closeDB();
	// }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}
}
