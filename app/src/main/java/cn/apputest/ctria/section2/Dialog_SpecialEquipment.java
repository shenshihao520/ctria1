package cn.apputest.ctria.section2;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.apputest.ctria.data.SpecialEquipUsageDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 特征设备使用登记证明详细信息 弹出框
 * 
 */
public class Dialog_SpecialEquipment {
	SharedPreferences preferencesuser;
	private DBHelper helper;
	private DBManager mgr;
	private TextView Information;
	boolean IsNetWorkReady = true;
	String PlateNumber;

	public void alert_SpecialEquipmentDialog(Context context,
			String car_platenum_gua) {
		// TODO Auto-generated method stub

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		//
		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater
				.from(context)
				.inflate(
						R.layout.section2activity_lucha1_alert_specialequipusage_dialog,
						null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(vv);
		// dialog.getWindow().setLayout(480, 524);
		// dialog.getWindow().setLayout(720, 1050);
		Resources r = context.getResources();
		int x = (int) r.getDimension(R.dimen.x320);
		int y = (int) r.getDimension(R.dimen.y350);
		dialog.getWindow().setLayout(x, y);
		dialog.getWindow().setGravity(Gravity.BOTTOM);

		Information = (TextView) vv.findViewById(R.id.Specialequipusage);
		Information.setClickable(false);
		Information.setLongClickable(false);
		Information.setMovementMethod(ScrollingMovementMethod.getInstance());

		getSpecialEquipment(car_platenum_gua);

		ImageButton backButton = (ImageButton) vv
				.findViewById(R.id.backImageButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					dialog.dismiss();
			}
		});
		dialog.show();
	}

	void getSpecialEquipment(final String car_platenum_gua) {

		PlateNumber = car_platenum_gua;
		SpecialEquipUsageDataEntity specialequipusage = new SpecialEquipUsageDataEntity();
		String cariD = car_platenum_gua;
		specialequipusage = mgr.querySpecialEquipUsage(cariD);
		if (StringUtils.isNotBlank(specialequipusage.getSpecialequipmentInfo())) {
			Information.setText(specialequipusage.getSpecialequipmentInfo());
		} else {
			Information.setText("无该车辆特种道路运输证信息");
		}

	}

}
