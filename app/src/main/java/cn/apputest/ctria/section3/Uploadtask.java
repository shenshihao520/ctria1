package cn.apputest.ctria.section3;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import cn.apputest.ctria.data.UploadTaskDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.section3.LoadListView.ILoadListener;
import cn.apputest.ctria.section3.LoadListView.IReflashLisener;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;
/**
 * 
 * @author Shihao Shen 上传任务 弹出框 可以显示 路查记录的上传记录
 * 
 */
public class Uploadtask {
	MyAdspter adapter;
	LoadListView listview;
	ArrayList<UploadTaskDataEntity> apk_list = new ArrayList<UploadTaskDataEntity>();
	int listitemlast = 0;
	int listitemID = 0;
	private DBManager mgr;
	private DBHelper helper;
	Context context;
	private CheckBox checkbox1, checkbox2;
	int flag = 0;
	SharedPreferences preferencesuser;

	void alert_UploadtaskDialog_inti(final View vv) {
		checkbox1 = (CheckBox) vv.findViewById(R.id.checkBox1);
		checkbox2 = (CheckBox) vv.findViewById(R.id.checkBox2);
		checkbox1.setChecked(true);
		checkbox2.setChecked(false);
		listview = (LoadListView) vv.findViewById(R.id.lv);
		Listgoon listgoon = new Listgoon();
		listgoon.showListView(listgoon.getData(flag));
		checkbox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					apk_list = null;
					flag = 0;
					checkbox2.setChecked(false);
					listview = (LoadListView) vv.findViewById(R.id.lv);
					Listgoon listgoon = new Listgoon();
					listgoon.showListView(listgoon.getData(flag));
				}
			}
		});
		checkbox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					apk_list = null;
					flag = 1;
					checkbox1.setChecked(false);
					listview = (LoadListView) vv.findViewById(R.id.lv);
					Listgoon listgoon = new Listgoon();
					listgoon.showListView(listgoon.getData(flag));
				}
			}
		});

	}

	class Listgoon implements ILoadListener, IReflashLisener {
		private void showListView(ArrayList<UploadTaskDataEntity> apk_list) {
			if (adapter == null) {

				listview.setInterface(this, this);
				adapter = new MyAdspter(context, apk_list);
				listview.setAdapter(adapter);

			} else {
				listitemlast = listview.getCount();
				listview.setInterface(this, this);
				adapter = new MyAdspter(context, apk_list);
				listview.setAdapter(adapter);
				listview.setSelection(listitemlast - 1);

			}
		}

		private void showListViewUpdate(ArrayList<UploadTaskDataEntity> apk_list) {
			// TODO Auto-generated method stub
			if (adapter == null) {

				listview.setInterface(this, this);
				adapter = new MyAdspter(context, apk_list);
				listview.setAdapter(adapter);

			} else {
				listitemlast = listview.getCount();
				listview.setInterface(this, this);
				adapter = new MyAdspter(context, apk_list);
				listview.setAdapter(adapter);
			}
		}

		private ArrayList<UploadTaskDataEntity> getData(int flag) {
			if (flag == 0) {
				listitemID = 0;
				ArrayList<UploadTaskDataEntity> tasks = mgr
						.queryUploadTask_Cars(0);
				for (UploadTaskDataEntity task : tasks) {
					listitemID = task.getTaskID();
					break;
				}
				listitemID = listitemID + 10;
				apk_list = tasks;
			} else {
				listitemID = 0;
				ArrayList<UploadTaskDataEntity> tasks = mgr
						.queryUploadTask_People(0);
				for (UploadTaskDataEntity task : tasks) {
					listitemID = task.getTaskID();
					break;
				}
				listitemID = listitemID + 10;
				apk_list = tasks;
			}
			return apk_list;
		}

		private ArrayList<UploadTaskDataEntity> getReFlashData() {
			// TODO Auto-generated method stub
			if (flag == 0) {
				listitemID = 0;
				ArrayList<UploadTaskDataEntity> tasks = mgr
						.queryUploadTask_Cars(0);
				for (UploadTaskDataEntity task : tasks) {
					listitemID = task.getTaskID();
					break;
				}
				listitemID = listitemID + 10;
				apk_list = tasks;
			} else {
				listitemID = 0;
				ArrayList<UploadTaskDataEntity> tasks = mgr
						.queryUploadTask_People(0);
				for (UploadTaskDataEntity task : tasks) {
					listitemID = task.getTaskID();
					break;
				}
				listitemID = listitemID + 10;
				apk_list = tasks;
			}
			return apk_list;
		}

		private ArrayList<UploadTaskDataEntity> getLoadData(int flag) {

			if (flag == 0) {
				listitemlast = listview.getCount()-2;
				ArrayList<UploadTaskDataEntity> tasks = mgr
						.queryUploadTask_Cars(listitemlast);
				for (UploadTaskDataEntity task : tasks) {
					UploadTaskDataEntity entity = new UploadTaskDataEntity();

					entity.setCheckcategory(task.getCheckcategory());

					entity.setTime(task.getTime());
					entity.setUploadcondition(task.getUploadcondition());

					entity.setTaskID(listitemID);
					apk_list.add(entity);
				}
				if (!tasks.isEmpty()) {
					listitemID = tasks.get(0).getTaskID();
					listitemID = listitemID + 10;
				}

			} else {
				listitemlast = listview.getCount()-2;
				
				ArrayList<UploadTaskDataEntity> tasks = mgr
						.queryUploadTask_People(listitemlast);
				for (UploadTaskDataEntity task : tasks) {
					UploadTaskDataEntity entity = new UploadTaskDataEntity();

					entity.setCheckcategory(task.getCheckcategory());

					entity.setTime(task.getTime());
					entity.setUploadcondition(task.getUploadcondition());

					entity.setTaskID(listitemID);
					apk_list.add(entity);
				}
				if (!tasks.isEmpty()) {
					listitemID = tasks.get(0).getTaskID();
					listitemID = listitemID + 10;
				}
			}
			return apk_list;
		}

		@Override
		public void onLoad() {
			// TODO Auto-generated method stub
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 获取更多数据

					apk_list = getLoadData(flag);
					// 更新listview显示；
					showListView(apk_list);
					// 通知listview加载完毕
					listview.loadComplete();
				}
			}, 2000);
		}

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

	}

	public void alert_UploadtaskDialog(Context context) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context, R.style.add_dialog);
		View vv = LayoutInflater.from(context).inflate(
				R.layout.section3activity_uploadtask, null);
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

		preferencesuser = context.getSharedPreferences(Login.FILE_USER,
				Context.MODE_PRIVATE);
		String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
		helper = new DBHelper(context, DBName + "_DB");
		mgr = new DBManager(helper);

		alert_UploadtaskDialog_inti(vv);
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

}
