package cn.apputest.ctria.section3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.apputest.ctria.data.VersionInfo;
import cn.apputest.ctria.myapplication.RequestHead;
import cn.apputest.ctria.myapplication.Url;
import cn.apputest.ctria.section2.Superbutton;

import com.example.dcrc2.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 
 * @author Shihao Shen 关于
 * 
 */
public class Updatedata {
	boolean IsNetWorkReady = true;
	TextView updatedata_S3versionID, updatedata_S3latestversionID,
			updatedata_condition, updatedata_conditionnum;
	ProgressBar progressBar1;
	Button updatedata_startupdateButton;
	ImageButton updatedata_cancelButton;
	Context context;
	String nowversion;
	String latestversion;
	boolean Isneedupdate = false;
	Superbutton sup;
	void alert_UpdatedataDialog_inti(View vv) {
		// isNetworkConnected(context);
		updatedata_S3versionID = (TextView) vv.findViewById(R.id.S3versionID);
		updatedata_S3latestversionID = (TextView) vv
				.findViewById(R.id.S3latestversionID);
//		sup = (Superbutton)vv.findViewById(R.id.sup);
//		sup.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			Toast.makeText(context, "!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();	
//			}
//		});
//		try {
//			downFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			// if (isNeedUpdate()) { //在下面的代码段
			// showUpdateDialog(); //下面的代码段
			// }
		}
	};

	private void downFile() throws IOException {
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://192.168.0.80/MqttWebSocket/ctria.apk");
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength(); // 获取文件大小
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"Test.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[10]; // 这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一
													// 下就下载完了，看不出progressbar的效果。
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
						}
						System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
					}
					if (fileOutputStream != null) {
						fileOutputStream.flush();
					}
				
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					update();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}

	void down() {
		handler1.post(new Runnable() {
			public void run() {
				// progressBar1.cancel();
				update();
			}
		});
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "Test.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public void alert_UpdatedataDialog(Context context) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section3activity_updatedata, null);
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
		alert_UpdatedataDialog_inti(vv);
		updatedata_cancelButton = (ImageButton) vv
				.findViewById(R.id.S3chancel1);

		updatedata_cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					dialog.dismiss();
			}
		});

		get();
		dialog.show();
	}

	void get() {
		Url url = new Url();
		System.out.println("==" + url.getVersoninfo());
		RequestHead requesthead = new RequestHead();
		RequestParams params = new RequestParams();

		params.addHeader("userName", requesthead.getUserName());
		params.addHeader("requestDatetime", requesthead.getRequestDatetime());
		params.addHeader("appID", requesthead.getAppID());
		params.addHeader("signature", requesthead.getSignature());

		params.addQueryStringParameter("appID", requesthead.getAppID());

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, url.getVersoninfo(), params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String xufang = null;
						System.out.println(responseInfo.result);

						Header[] headers = responseInfo.getAllHeaders();
						if (headers != null)
							for (Header head : headers) {

								System.out.println(head.getName() + ":"
										+ head.getValue());

							}

						JSONObject object;
						VersionInfo info = new VersionInfo();
						try {

							object = new JSONObject(responseInfo.result);
							info.setVerionNumber(object.getString("verionName"));
							info.setVersionCode(object.getString("versionCode"));
							// info.setReleaseNote(object.getString("releaseNote"));
							// info.setReleaseDate(object.getString("releaseDate"));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						nowversion = info.getVerionNumber();
						latestversion = "V" + getVersion();
						updatedata_S3latestversionID.setText(nowversion);
						updatedata_S3versionID.setText(latestversion);
						// handler1.sendEmptyMessage(0);
						if (isNeedUpdate(nowversion, latestversion)) {
							Toast.makeText(context, "需要更新", Toast.LENGTH_SHORT)
									.show();
							Isneedupdate = true;

						} else {
							Toast.makeText(context, "不需要更新", Toast.LENGTH_SHORT)
									.show();
							Isneedupdate = false;
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	private String getVersion() {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

	private boolean isNeedUpdate(String nowversion, String latestversion) {

		return !latestversion.equals(nowversion);
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
