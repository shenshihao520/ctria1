package cn.apputest.ctria.section4;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import cn.apputest.ctria.data.MessageDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

/**
 * @author 作者Shihao Shen:
 * @version 创建时间：2015-11-3 下午3:16:55 类说明 消息推送 mqqt 长连接
 */
public class PushService {
	SharedPreferences preferencesuser;
	private DBManager mgr;
	private DBHelper helper;
	private Context context;
	private String host = "tcp://111.198.72.101:1883";
	private String userName = "";
	private String passWord = "";

	private Handler handler;

	private MqttClient client;

	private String myTopic = "zhongxin";

	private MqttConnectOptions options;

	private ScheduledExecutorService scheduler;
	MediaPlayer player = new MediaPlayer();
	int flag_sound = 0;

	public void onCreate(final Context context) {
		// TODO Auto-generated method stub

		this.context = context;
		preferencesuser = context.getSharedPreferences(
				Login.FILE_USER, Context.MODE_PRIVATE);
		System.out.println("创建推送服务");
		init();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					Log.i("Push", (String) msg.obj);

					try {
						soundRing(context);
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalStateException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					Log.i("Push", "-----------------------------");
//					preferencesuser = context.getSharedPreferences(
//							Login.FILE_USER, Context.MODE_PRIVATE);
					String DBName = preferencesuser.getString(Login.KEY_NAME,
							"1");
					helper = new DBHelper(context, DBName + "_DB");
					mgr = new DBManager(helper);
					JSONObject object;
					String title = null;
					String comtent = null;
					try {
						object = new JSONObject((String) msg.obj);
						title = object.getString("TITLE");
						comtent = object.getString("COMTENT");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					MessageDataEntity message = new MessageDataEntity();
					message.setMessageTitle(title);
					message.setMessage(comtent);
					mgr.addMessage(message);

					Message msg55 = new Message();
					
				
					int weidu = preferencesuser.getInt("weidu", 1);
					
					msg55.what = 5;
					msg55.obj = weidu;
					mHandler.sendMessage(msg55);

				} else if (msg.what == 2) {
					Log.i("Push", "连接成功");
					try {
						client.subscribe(myTopic, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg.what == 3) {
					Log.i("Push", "连接失败，系统正在重连");
				}
			}
		};

		startReconnect();

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 3s后执行代码
			if (msg.what == 5) {
				int i = (Integer) msg.obj+1;
				SharedPreferences.Editor editor = preferencesuser.edit();
				editor.putInt("weidu", i);
				editor.commit();

				Intent it = new Intent();
				it.setAction("a message is coming");
				context.sendBroadcast(it);
			}
		}
	};

	private void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!client.isConnected()) {
					connect();
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	private void init() {
		try {
			String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
			// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(host, android_id, null);
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(true);
			// // 设置连接的用户名
			// options.setUserName(userName);
			// // 设置连接的密码
			// options.setPassword(passWord.toCharArray());
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(10);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(20);
			// 设置回调
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable cause) {
					// 连接丢失后，一般在这里面进行重连
					System.out.println("connectionLost----------");
				}

				@Override
				public void deliveryComplete(MqttDeliveryToken arg0) {
					// TODO Auto-generated method stub
					System.out.println("deliveryComplete---------"
							+ arg0.isComplete());
				}

				@Override
				public void messageArrived(MqttTopic topicName,
						MqttMessage message) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("messageArrived----------");
					Message msg = new Message();
					msg.what = 1;
					msg.obj = message.toString();
					handler.sendMessage(msg);
				}
			});
			// connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client.connect(options);
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	private void soundRing(Context context) throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {

		MediaPlayer mp = new MediaPlayer();
		mp.reset();
		mp.setDataSource(context, RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		mp.prepare();
		mp.start();

	}

	// @Override
	// public void onDestroy() {
	// super.onDestroy();
	// try {
	// scheduler.shutdown();
	// client.disconnect();
	// } catch (MqttException e) {
	// e.printStackTrace();
	// }
	// }

}
