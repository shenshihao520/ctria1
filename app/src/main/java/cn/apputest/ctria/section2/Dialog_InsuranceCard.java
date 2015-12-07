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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.apputest.ctria.data.InsuranceCardDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 保险证详细信息 弹出框
 * 
 */
public class Dialog_InsuranceCard {
	SharedPreferences preferencesuser;
	private DBHelper helper;
	private DBManager mgr;
	private TextView Information;
	boolean IsNetWorkReady = true;
	String PlateNumber;

	public void alert_InsuranceCardDialog(Context context, EditText carnum,
			String car_platenum_gua) {
		// TODO Auto-generated method stub

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section2activity_lucha1_alert_insurancecard_dialog,
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

		Information = (TextView) vv.findViewById(R.id.Insurancecard);
		Information.setClickable(false);
		Information.setLongClickable(false);
		Information.setMovementMethod(ScrollingMovementMethod.getInstance());

		getInsuranceCard(carnum, car_platenum_gua);

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

	void getInsuranceCard(final EditText carnum, final String car_platenum_gua) {

		PlateNumber = carnum.getText().toString();

		InsuranceCardDataEntity insurancecard_head = new InsuranceCardDataEntity();
		InsuranceCardDataEntity insurancecard_Cargo = new InsuranceCardDataEntity();
		InsuranceCardDataEntity insurancecard_tail = new InsuranceCardDataEntity();
		String cariD = carnum.getText().toString();
		insurancecard_head = mgr.queryInsuranceCard(cariD);
		insurancecard_Cargo = mgr.queryInsuranceCard_Cargo(car_platenum_gua);
		insurancecard_tail = mgr.queryInsuranceCard(car_platenum_gua);
		if (StringUtils.isNotBlank(insurancecard_head.getInsuranceInfo())
				&& StringUtils.isNotBlank(insurancecard_Cargo
						.getInsuranceInfo())
				&& StringUtils
						.isNotBlank(insurancecard_tail.getInsuranceInfo())) {
			Information.setText("牵引车信息：" + insurancecard_head.getInsuranceInfo()
					+ "\n" + "货物信息：" + insurancecard_Cargo.getInsuranceInfo()
					+ "\n" + "挂车信息：" + insurancecard_tail.getInsuranceInfo());
		}
		else if(!StringUtils.isNotBlank(insurancecard_head.getInsuranceInfo())
				&& !StringUtils.isNotBlank(insurancecard_Cargo
						.getInsuranceInfo())
				&& !StringUtils
						.isNotBlank(insurancecard_tail.getInsuranceInfo()))
		{
			Information.setText("无保险证信息");
		}
		else {
			if (!StringUtils.isNotBlank(insurancecard_head.getInsuranceInfo())) {
				insurancecard_head.setInsuranceInfo("空");
			}
			if (!StringUtils.isNotBlank(insurancecard_Cargo.getInsuranceInfo())) {
				insurancecard_Cargo.setInsuranceInfo("空");
			}
			if (!StringUtils.isNotBlank(insurancecard_tail.getInsuranceInfo())) {
				insurancecard_tail.setInsuranceInfo("空");
			}
			Information.setText("牵引车信息：" + insurancecard_head.getInsuranceInfo()
					+ "\n" + "货物信息：" + insurancecard_Cargo.getInsuranceInfo()
					+ "\n" + "挂车信息：" + insurancecard_tail.getInsuranceInfo());
			
		}

	}

}
