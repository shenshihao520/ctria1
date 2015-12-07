package cn.apputest.ctria.service;

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import cn.apputest.ctria.data.VersionInfo;
import cn.apputest.ctria.myapplication.RequestHead;
import cn.apputest.ctria.myapplication.Url;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author 作者Shihao Shen:
 * @version 创建时间：2015-11-4 下午5:58:09 类说明  版本更新
 */
public class UpdateVersionService {
	private ProgressDialog pBar;
	Context context;
	String NowVersion;
	String LastestVersion;	
	Url url = new Url();
	public void CheckUpdate(Context context) {
		this.context = context;

		Toast.makeText(context, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
		// 自动检查有没有新版本 如果有新版本就提示更新
		new Thread() {
			public void run() {
				try {
					getLastestVersion();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			if (isNeedUpdate()) { // 在下面的代码段
				showUpdateDialog(); // 下面的代码段
			}
		}
	};

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("请升级APP至版本" + LastestVersion);
//		builder.setMessage(info.getDescription());
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					
					downFile(url.getDownload()); // 在下面的代码段
				} else {
					 Toast.makeText(context,
					 "SD卡不可用，请插入SD卡",Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.create().show();
	}

	private boolean isNeedUpdate() {

		if (("V"+NowVersion).equals(LastestVersion) ) {
			Toast.makeText(context, "不需要更新", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			Toast.makeText(context, "需要更新", Toast.LENGTH_SHORT)
					.show();
			return true;
		}
	}

	void downFile(final String url) {
		pBar = new ProgressDialog(context); // 进度条，在下载的时候实时更新进度，提高用户友好度
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgress(0);
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try { 
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength(); // 获取文件大小
					pBar.setMax(length); // 设置进度条的总长度
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"ctria"+LastestVersion+".apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[100]; // 这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一
													// 下就下载完了，看不出progressbar的效果。
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							pBar.setProgress(process); // 这里就是关键的实时更新进度了！
						}

					}
					if (fileOutputStream != null) {
						fileOutputStream.flush();
					}
				
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
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
				pBar.cancel();
				update();
			}
		});
	}

	// 安装文件，一般固定写法
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "Test.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	void getLastestVersion() {
		Url url = new Url();
		System.out.println("==" + url.getVersoninfo());
		RequestHead requesthead = new RequestHead();
		RequestParams params = new RequestParams();

		params.addHeader("userName", requesthead.getUserName());
		params.addHeader("requestDatetime", requesthead.getRequestDatetime());
		params.addHeader("appID", requesthead.getAppID());
		params.addHeader("signature", requesthead.getSignature());

		params.addQueryStringParameter("appID", requesthead.getAppID());

		HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
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
						
						LastestVersion = info.getVerionNumber();
						NowVersion = getVersion();
						
						handler1.sendEmptyMessage(0);
				
						// handler1.sendEmptyMessage(0);

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
}
