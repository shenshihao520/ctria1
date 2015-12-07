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
import cn.apputest.ctria.data.PermitCardDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 准行证详细信息实体
 * 
 */
public class Dialog_PermitCard {

	public static final String FLAG_Permit = "FLAG_Permit";
	SharedPreferences preferencesuser;
	boolean IsNetWorkReady = true;
	private DBHelper helper;
	private DBManager mgr;
	private TextView Information;
	public static final String KEY_FLAG = "flag";
	String flag = "0";
	Context contextAlawys;
	String PlateNumber;

	public void alert_PermitCardDialog(Context context, EditText carnum) {
		contextAlawys = context;

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		// preferencesuser.getInt("DataVerSion", 1);

		helper = new DBHelper(context, DBName + "_DB");

		mgr = new DBManager(helper);

		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section2activity_lucha1_alert_permitcard_dialog, null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(vv);
		// dialog.getWindow().setLayout(480, 524);
		// dialog.getWindow().setLayout(720, 1050);
		Resources r = context.getResources();
		int x = (int) r.getDimension(R.dimen.x320);
		int y = (int) r.getDimension(R.dimen.y350);
		dialog.getWindow().setLayout(x, y);
		dialog.getWindow().setGravity(Gravity.BOTTOM);

		Information = (TextView) vv.findViewById(R.id.PermitCard);
		Information.setClickable(false);
		Information.setLongClickable(false);
		Information.setMovementMethod(ScrollingMovementMethod.getInstance());

		getPermitCard(carnum);

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

	void getPermitCard(final EditText carnum) {

		PermitCardDataEntity permitcard = new PermitCardDataEntity();
		String cariD = carnum.getText().toString();
		permitcard = mgr.queryPermitCard(cariD);
		if (StringUtils.isNotBlank(permitcard.getPermitInfo())) {
			Information.setText(permitcard.getPermitInfo());
		}
		else
		{
			Information.setText("无该车辆准行证信息");
		}
	}

}
