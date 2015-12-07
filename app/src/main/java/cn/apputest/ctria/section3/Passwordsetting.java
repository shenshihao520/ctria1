package cn.apputest.ctria.section3;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.apputest.ctria.myapplication.MD5;
import cn.apputest.ctria.myapplication.RequestHead;
import cn.apputest.ctria.myapplication.Unicode;
import cn.apputest.ctria.myapplication.Url;
import cn.apputest.ctria.service.Constants;

import com.example.dcrc2.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 
 * @author Shihao Shen 密码设置弹出框
 * 
 */
public class Passwordsetting {
	EditText passwordsetting_frontpassword, passwordsetting_newpassword,
			passwordsetting_passwordagain;
	RelativeLayout passwordsetting_sureButton, passwordsetting_cancelButton;
	SharedPreferences preferencesuser;
	boolean IsNetWorkReady = true;
	Context context;
	Toast toast;
	public boolean hastoast = false;
	
	void alert_Passwordsetting_inti(View vv, final String username) {
		passwordsetting_frontpassword = (EditText) vv
				.findViewById(R.id.S3frontpassword);
		passwordsetting_newpassword = (EditText) vv
				.findViewById(R.id.S3newpassword);
		passwordsetting_passwordagain = (EditText) vv
				.findViewById(R.id.S3againpassword);
		passwordsetting_sureButton = (RelativeLayout) vv.findViewById(R.id.S3sure);
		passwordsetting_cancelButton = (RelativeLayout) vv
				.findViewById(R.id.S3chancel1);

		passwordsetting_sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((StringUtils.isNotBlank(passwordsetting_frontpassword.getText().toString()))
						&&(StringUtils.isNotBlank(passwordsetting_newpassword.getText().toString()))
						&&(StringUtils.isNotBlank(passwordsetting_passwordagain.getText().toString())))
				{
					if(passwordsetting_newpassword.getText().toString().equals(passwordsetting_passwordagain.getText().toString()))
					{
						if(passwordsetting_frontpassword.getText().toString().equals(passwordsetting_newpassword.getText().toString()))
						{
							if (hastoast == false) {
								hastoast = true;
								Toast.makeText(context, "新密码不能和原密码一致", Toast.LENGTH_SHORT).show();
								mHandler.postDelayed(mRunnable, 2000);
							}
							
						}
						else
						{
							post(passwordsetting_frontpassword,
									passwordsetting_newpassword, username);
						}
						
					}
					else
					{
						
						if (hastoast == false) {
							hastoast = true;
							Toast.makeText(context, "俩次密码输入不一致", Toast.LENGTH_SHORT).show();
							mHandler.postDelayed(mRunnable, 2000);
						}
						
					}
				}
				else
				{
					
					if (hastoast == false) {
						hastoast = true;
						Toast.makeText(context, "请您填写完整密码信息", Toast.LENGTH_SHORT).show();
						mHandler.postDelayed(mRunnable, 2000);
					}
				}
				
				
			}
		});
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
				// 2s后执行代码
				hastoast = false;
			}
		};
	}

	public void alert_Passwordsetting(Context context, String username) {
		// TODO Auto-generated method stub
		isNetworkConnected(context);
		this.context = context;
		toast = new Toast(context);
		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section3activity_passwordsetting, null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(vv);
		// dialog.getWindow().setLayout(720, 1100);
		Resources r = context.getResources();
		int x = (int) r.getDimension(R.dimen.x320);
		int y = (int) r.getDimension(R.dimen.y385);
		dialog.getWindow().setLayout(x, y);
		// dialog.getWindow().setLayout(480, 570);
		dialog.getWindow().setGravity(Gravity.BOTTOM);

		alert_Passwordsetting_inti(vv, username);

		passwordsetting_cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					dialog.dismiss();
			}
		});

		dialog.show();

	}

	void post(EditText passwordsetting_frontpassword2,
			EditText passwordsetting_newpassword2, String username) {

		Url url = new Url();
		RequestHead requesthead = new RequestHead();
		RequestParams params = new RequestParams();

		params.addHeader("userName", requesthead.getUserName());
		params.addHeader("requestDatetime", requesthead.getRequestDatetime()); 
		params.addHeader("appID", requesthead.getAppID());
		params.addHeader("signature", requesthead.getSignature());

		params.addBodyParameter("userName", username);
		params.addBodyParameter("oldPassword",
				MD5.md5(passwordsetting_frontpassword2.getText().toString()));
		params.addBodyParameter("newPassword",
				MD5.md5(passwordsetting_newpassword2.getText().toString()));

		HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
		http.send(HttpMethod.POST, url.modifypwd, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println(responseInfo.result);
						
						Header[] headers = responseInfo.getAllHeaders();
						for (Header head : headers) {

							System.out.println(head.getName() + ":"
									+ head.getValue());

							if (head.getName().equals("resultCode")) 
							{
								if(head.getValue().equals("000020"))
								{
									
									if (hastoast == false) {
										hastoast = true;
										Toast.makeText(context, "原密码不正确", Toast.LENGTH_SHORT).show();
										mHandler.postDelayed(mRunnable, 2000);
									}
								}
								else
								{
									if (hastoast == false) {
										hastoast = true;
										Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
										mHandler.postDelayed(mRunnable, 2000);
									}
									
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
						
						if (hastoast == false) {
							hastoast = true;
							Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT)
							.show();
							mHandler.postDelayed(mRunnable, 2000);
						}
						
					}
				});
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
}
