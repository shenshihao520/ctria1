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
import cn.apputest.ctria.data.TransportCardDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 车辆道路运输证 详细信息 弹出框
 * 
 */
public class Dialog_TransportCard {
	SharedPreferences preferencesuser;
	private DBHelper helper;
	private DBManager mgr;
	private TextView Information;
	boolean IsNetWorkReady = true;
	String PlateNumber;
	String car_platenum_gua;

	public void alert_TransportCardDialog(Context context, EditText carnum,
			String car_platenum_gua) {
		// TODO Auto-generated method stub

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section2activity_lucha1_alert_transportcard_dialog,
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

		Information = (TextView) vv.findViewById(R.id.Transportcard);
		Information.setClickable(false);
		Information.setLongClickable(false);
		Information.setMovementMethod(ScrollingMovementMethod.getInstance());

		getTransportCard(carnum, car_platenum_gua);

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

	public void getTransportCard(final EditText carnum,
			final String car_platenum_gua) {

		PlateNumber = carnum.getText().toString();
		this.car_platenum_gua = car_platenum_gua;
		TransportCardDataEntity transportcard_head = new TransportCardDataEntity();
		TransportCardDataEntity transportcard_tail = new TransportCardDataEntity();
		String cariD = carnum.getText().toString();
		transportcard_head = mgr.queryTransportCard(cariD);
		transportcard_tail = mgr.queryTransportCard(car_platenum_gua);
		if (StringUtils.isNotBlank(transportcard_head.getTransportInfo())
				&& StringUtils
						.isNotBlank(transportcard_tail.getTransportInfo())) {
			Information.setText("牵引车信息：" + transportcard_head.getTransportInfo()
					+ "\n" + "挂车信息：" + transportcard_tail.getTransportInfo());
		}
		else if(!StringUtils.isNotBlank(transportcard_head.getTransportInfo()))
		{
			Information.setText("牵引车信息：" + "空"
					+ "\n" + "挂车信息：" + transportcard_tail.getTransportInfo());
		}
		else if(!StringUtils.isNotBlank(transportcard_tail.getTransportInfo()))
		{
			Information.setText("牵引车信息：" + transportcard_head.getTransportInfo()
					+ "\n" + "挂车信息：" + "空");
		}
		if(!StringUtils.isNotBlank(transportcard_head.getTransportInfo())
				&& !StringUtils
				.isNotBlank(transportcard_tail.getTransportInfo()))
		{
			Information.setText("无该车辆道路运输证信息");
		}

	}

}
