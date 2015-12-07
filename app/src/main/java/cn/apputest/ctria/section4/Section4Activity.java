package cn.apputest.ctria.section4;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import cn.apputest.ctria.data.MessageDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.section4.LoadListView.ILoadListener;
import cn.apputest.ctria.section4.LoadListView.IReflashLisener;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;
/**
 * 
 * @author Shihao Shen
 *	消息界面
 *
 */
public class Section4Activity extends Activity {
	MyAdspter adapter;
	LoadListView listview;
	ArrayList<MessageDataEntity> apk_list = new ArrayList<MessageDataEntity>();
	int listitemlast = 0;
	int listitemID = 0;
	private DBManager mgr;
	private DBHelper helper;
	Context context;
	ILoadListener iLoadListener;
	IReflashLisener iReflashLisener;
	int flag = 0;
	SharedPreferences preferencesuser;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.section4activity);

		alert_UploadtaskDialog_inti();
	}

	

	void alert_UploadtaskDialog_inti() {
		iLoadListener = new ILoadListener() {

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 获取更多数据

						apk_list = getLoadData();
						// 更新listview显示；
						showListViewLoad(apk_list);
						// 通知listview加载完毕 
						listview.loadComplete();
					}
				}, 2000);
			}
		};
		iReflashLisener = new IReflashLisener() {

			@Override
			public void onReflash() {
				// TODO Auto-generated method stub
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 获取更多数据
						getReFlashData();
						// 更新listview显示；
						showListViewUpdate(apk_list);
						// 通知listview加载完毕
						listview.reflashComplete();
					}
				}, 2000);

			}
		};
		preferencesuser = getSharedPreferences(Login.FILE_USER, MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(this, DBName + "_DB");
		mgr = new DBManager(helper);

		listview = (LoadListView) findViewById(R.id.lv_4);
		showListViewLoad(getData());
	}

	private void showListViewLoad(ArrayList<MessageDataEntity> apk_list) {
		if (adapter == null) {
			listview.setInterface(iLoadListener, iReflashLisener);
			adapter = new MyAdspter(context, apk_list);
			listview.setAdapter(adapter);

		} else {
			listitemlast = listview.getCount();
			listview.setInterface(iLoadListener, iReflashLisener);
			adapter = new MyAdspter(context, apk_list);
			listview.setAdapter(adapter);
			listview.setSelection(listitemlast - 1);

		}
	}

	private void showListViewUpdate(ArrayList<MessageDataEntity> apk_list) {
		if (adapter == null) {
			listview.setInterface(iLoadListener, iReflashLisener);
			adapter = new MyAdspter(context, apk_list);
			listview.setAdapter(adapter);

		} else {
//			listitemlast = listview.getCount();
			listview.setInterface(iLoadListener, iReflashLisener);
			adapter = new MyAdspter(context, apk_list);
			listview.setAdapter(adapter);

		}
	}

	private ArrayList<MessageDataEntity> getData() {

		listitemID = 0;
		ArrayList<MessageDataEntity> messages = mgr.queryMessage(0);
		for (MessageDataEntity message : messages) {
			listitemID = message.getID();
			break;
		}
		listitemID = listitemID + 10;
		apk_list = messages;

		return apk_list;
	}

	private ArrayList<MessageDataEntity> getLoadData() {
		
		listitemlast = listview.getCount()-2;
		ArrayList<MessageDataEntity> messages = mgr.queryMessage(listitemlast);
		for (MessageDataEntity message : messages) {
			MessageDataEntity entity = new MessageDataEntity();

			entity.setMessageTitle(message.getMessageTitle());
			entity.setMessage(message.getMessage());

//			entity.setID(listitemID);
			apk_list.add(entity);
		}
//		if (!messages.isEmpty()) {
//			listitemID = messages.get(0).getID();
//			listitemID = listitemID + 10;
//		}

		return apk_list;
	}

	private ArrayList<MessageDataEntity> getReFlashData() {

		listitemID = 0;
		ArrayList<MessageDataEntity> messages = mgr.queryMessage(0);
		for (MessageDataEntity message : messages) {
			listitemID = message.getID();
			break;
		}
		listitemID = listitemID + 10;
		apk_list = messages;
		return apk_list;
	}
	 @Override
	 protected void onDestroy() {
	 super.onDestroy();
	 //应用的最后一个Activity关闭时应释放DB
	 mgr.closeDB();
	 }
}
