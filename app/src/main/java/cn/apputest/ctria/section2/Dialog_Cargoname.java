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
import cn.apputest.ctria.data.CargoEmergencyMethodDataEntity;
import cn.apputest.ctria.myapplication.Login;
import com.example.dcrc2.R;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;


/**
 * 
 * @author Shihao Shen 危化品详细信息弹出框
 * 
 */
public class Dialog_Cargoname {
	SharedPreferences preferencesuser;
	boolean IsNetWorkReady = true;
	String result1;
	private DBHelper helper;
	private DBManager mgr;
	private TextView Information;
	String CargoName;
	String CargoID;

	public void alert_CargonameDialog(Context context, String cargoID2) // 弹出框
	{
		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);
		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section2activity_lucha1_alert_cargoname_dialog, null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(vv);

		Resources r = context.getResources();
		int x = (int) r.getDimension(R.dimen.x320);
		int y = (int) r.getDimension(R.dimen.y350);
		dialog.getWindow().setLayout(x, y);

		dialog.getWindow().setGravity(Gravity.BOTTOM);

		Information = (TextView) vv.findViewById(R.id.CargpInformation);
		Information.setClickable(false);
		Information.setLongClickable(false);
		Information.setMovementMethod(ScrollingMovementMethod.getInstance());

		getCargo(cargoID2);

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

	CargoEmergencyMethodDataEntity alert_EmergencysearchDialog_Data(
			String emergencysearch_cargonameID) {

		CargoEmergencyMethodDataEntity cargo = new CargoEmergencyMethodDataEntity();
		cargo = mgr.queryCargoEmergency(emergencysearch_cargonameID);
		return cargo;
	}

	void getCargo(final String cargoID2) {
		CargoEmergencyMethodDataEntity cargo = new CargoEmergencyMethodDataEntity();
		cargo = alert_EmergencysearchDialog_Data(cargoID2);
		String method = mgr.queryCargoEmergencyMethod(cargoID2);
		if(StringUtils.isNotBlank(cargo.getCargoemergencymethod()))
		{
			Information.setText(cargo.getCargoemergencymethod()+method);
		}
		else
		{
			Information.setText("无危化品信息");
		}
	}

	

}
