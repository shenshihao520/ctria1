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
import cn.apputest.ctria.data.JobCardDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;
import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 从业资格证 弹出框
 * 
 */
public class Dialog_JobCard {
	SharedPreferences preferencesuser;
	private DBHelper helper;
	private DBManager mgr;
	private TextView Information;
	boolean IsNetWorkReady = true;
	Context contextAlawys;
	String certNumber;

	public void alert_JobCardDialog(Context context, EditText carnum) {
		// TODO Auto-generated method stub

		contextAlawys = context;

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section2activity_lucha2_alert_jobcard_dialog, null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(vv);
		// dialog.getWindow().setLayout(480, 524);
		// dialog.getWindow().setLayout(720, 1050);
		Resources r = context.getResources();
		int x = (int) r.getDimension(R.dimen.x320);
		int y = (int) r.getDimension(R.dimen.y350);
		dialog.getWindow().setLayout(x, y);
		dialog.getWindow().setGravity(Gravity.BOTTOM);

		Information = (TextView) vv.findViewById(R.id.Jobcard);
		Information.setClickable(false);
		Information.setLongClickable(false);
		Information.setMovementMethod(ScrollingMovementMethod.getInstance());

		getQualificationCard(carnum);

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

	void getQualificationCard(final EditText carnum) {

		certNumber = carnum.getText().toString();
		JobCardDataEntity jobcard1 = new JobCardDataEntity();
		String cariD = carnum.getText().toString();
		jobcard1 = mgr.queryJobCard(cariD);
		if(StringUtils.isNotBlank(jobcard1.getJobInfo()))
		{
		Information.setText(jobcard1.getJobInfo());
		}
		else
		{
			Information.setText("无从业资格证信息");
		}
	}

}
