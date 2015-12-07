package cn.apputest.ctria.section4;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.apputest.ctria.data.MessageDataEntity;
import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen ListView 的Adapter
 * 
 */
public class MyAdspter extends BaseAdapter {

	public ArrayList<MessageDataEntity> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public MyAdspter(Context context, ArrayList<MessageDataEntity> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public final class Zujian {
		public TextView MessageTitle;
		public TextView Message;

	}

	public void onDateChange(ArrayList<MessageDataEntity> apk_list) {
		this.data = apk_list;
		this.notifyDataSetChanged();
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
		MessageDataEntity entity = data.get(position);
		Zujian zujian = null;
		if (convertView == null) {
			zujian = new Zujian();
			// 获得组件，实例化组件
			convertView = layoutInflater.inflate(
					R.layout.section4activity_message_list_item, null);

			zujian.MessageTitle = (TextView) convertView
					.findViewById(R.id.S4MessageTitle);
			zujian.Message = (TextView) convertView
					.findViewById(R.id.message_T);

			convertView.setTag(zujian);
		} else {
			zujian = (Zujian) convertView.getTag();
		}
		// 绑定数据

		zujian.MessageTitle.setText(entity.getMessageTitle());
		zujian.Message.setText(entity.getMessage());

		return convertView;
	}

}
