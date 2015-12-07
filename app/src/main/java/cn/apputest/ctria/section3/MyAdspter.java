package cn.apputest.ctria.section3;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.apputest.ctria.data.UploadTaskDataEntity;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen ListView 的Adapter
 * 
 */
public class MyAdspter extends BaseAdapter {

	public ArrayList<UploadTaskDataEntity> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public MyAdspter(Context context, ArrayList<UploadTaskDataEntity> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public final class Zujian {
		public TextView checkcategoryTitle;
		public TextView checkcategory;
		public TextView time;
		public TextView uploadcondition;
	}

	public void onDateChange(ArrayList<UploadTaskDataEntity> apk_list) {
		this.data = apk_list;
		this.notifyDataSetChanged();  //修改已经生成的列表
	}

	@Override
	public int getCount() {
		return data.size();
	}

	/**
	 * 获得某一位置的数据
	 */
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	/**
	 * 获得唯一标识
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UploadTaskDataEntity entity = data.get(position);
		Zujian zujian = null;
		if (convertView == null) {
			zujian = new Zujian();
			// 获得组件，实例化组件
			convertView = layoutInflater.inflate(
					R.layout.section3activity_uploadtask_list_item, null);

			zujian.checkcategoryTitle = (TextView) convertView
					.findViewById(R.id.TextView05);
			zujian.checkcategory = (TextView) convertView
					.findViewById(R.id.checkcategory);
			zujian.time = (TextView) convertView.findViewById(R.id.time);
			zujian.uploadcondition = (TextView) convertView
					.findViewById(R.id.uploadcondition);
			convertView.setTag(zujian);
		} else {
			zujian = (Zujian) convertView.getTag();
		}
		// 绑定数据
		if (!StringUtils.isNotBlank(entity.checkcategory)) {
			entity.setCheckcategory("无姓名信息");
		}
		if (entity.getCheckcategory().length() >= 7) {
			zujian.checkcategoryTitle.setText("车辆号牌");
		} else {
			zujian.checkcategoryTitle.setText("姓名");
		}
		zujian.checkcategory.setText(entity.getCheckcategory());
		zujian.time.setText(entity.getTime());
		zujian.uploadcondition.setText(entity.getUploadcondition());

		// zujian.uploadcondition.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(context, "asdasdas", Toast.LENGTH_LONG).show();
		// }
		// });
		return convertView;
	}
}
