package cn.apputest.ctria.section2;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
import cn.apputest.ctria.data.LatestTransportRecordDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 超重 最新货运记录 详细信息弹出框
 * 
 */
public class Dialog_weight {
	SharedPreferences preferencesuser;
	boolean IsNetWorkReady = true;
	private DBHelper helper;
	private DBManager mgr;
	private TextView Information;
	public int weight;
	String plateNumber;

	public void alert_WeightDialog(Context context, EditText carnum) {

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section2activity_lucha1_alert_weight_dialog, null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(vv);
		// dialog.getWindow().setLayout(480, 524);
		// dialog.getWindow().setLayout(720, 1050);
		Resources r = context.getResources();
		int x = (int) r.getDimension(R.dimen.x320);
		int y = (int) r.getDimension(R.dimen.y350);
		dialog.getWindow().setLayout(x, y);
		dialog.getWindow().setGravity(Gravity.BOTTOM);

		Information = (TextView) vv.findViewById(R.id.WeightInformation);
		Information.setClickable(false);
		Information.setLongClickable(false);
		Information.setMovementMethod(ScrollingMovementMethod.getInstance());

		getWeight(carnum);

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

	void getWeight(final EditText carnum) {

		plateNumber = carnum.getText().toString();
		LatestTransportRecordDataEntity latesttransportrecord = new LatestTransportRecordDataEntity();
		String cariD = carnum.getText().toString();
		latesttransportrecord = mgr.queryLatestTransportRecord(cariD);
		if(StringUtils.isNotBlank(latesttransportrecord.getTransportrecordInfo()))
		{
			Information.setText(latesttransportrecord.getTransportrecordInfo());
		}
		else 
		{
			Information.setText("无货运记录");
		}
	}

	LatestTransportRecordDataEntity parseResult(String result) {
		LatestTransportRecordDataEntity latesttransportrecord = new LatestTransportRecordDataEntity();
		try {
			JSONObject object = new JSONObject(result);
			latesttransportrecord.setPlateNumber(object
					.getString("PLATE_NUMBER"));
			latesttransportrecord.setCargoname(object.getString("CARGO_NAME"));
			latesttransportrecord.setWeight(object.getInt("WEIGHT"));
			latesttransportrecord.setPlateNumber_gua(object
					.getString("TAIL_PLATE_NUMBER"));
			latesttransportrecord.setTransportrecordInfo("准行证号:"
					+ object.getString("PERMIT_NUMBER")
					+
					// "\n"+
					// ":"+
					// object.getString("EST_TIME")+
					"\n" + "基站ID:" + object.getString("BASESTATION_ID") + "\n"
					+ "货物名称:" + object.getString("CARGO_NAME") + "\n" + ":"
					+ object.getString("REUPLOAD_TIME") + "\n" + "操作员:"
					+ object.getString("OPERATOR")

					+ "\n" + ":" + object.getString("TRANSPORT_NUMBER") + "\n"
					+ "司机ID:" + object.getString("DRIVER_ID") + "\n" + "上传时间:"
					+ object.getString("UPLOAD_TIME") + "\n" + "创建时间:"
					+ object.getString("CREATE_TIME") + "\n" + "总重量:"
					+ object.getString("WEIGHT") + "\n" + "车牌号码:"
					+ object.getString("PLATE_NUMBER") + "\n" + ":"
					+ object.getString("SUPERCARGO_ID") + "\n" + "公司ID:"
					+ object.getString("COMPANY_ID"));

			// object.getString("ID");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latesttransportrecord;
	}

}
