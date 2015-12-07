package cn.apputest.ctria.section1;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.apputest.ctria.data.BasestationPassCarRecordDataEntity;

import com.example.dcrc2.R;


/**
 * @author 作者Shihao Shen:
 * @version 创建时间：2015-11-24 下午5:44:57 类说明 基站过车记录 列表适配器
 */
public class Basestation_ListAdapter extends BaseAdapter {
    ArrayList<BasestationPassCarRecordDataEntity> basesR = new ArrayList<BasestationPassCarRecordDataEntity>();
    private LayoutInflater layoutInflater;
    private Context context;

    public Basestation_ListAdapter(Context context, ArrayList<BasestationPassCarRecordDataEntity> basesR) {
        this.context = context;
        this.basesR = basesR;
        this.layoutInflater = LayoutInflater.from(context);

    }

    public final class Zujian {
        public TextView carnum;
        public TextView passtime;

    }

    public void onDateChange(ArrayList<BasestationPassCarRecordDataEntity> basesR) {
        this.basesR = basesR;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return basesR.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return basesR.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        BasestationPassCarRecordDataEntity entity = new BasestationPassCarRecordDataEntity();
        entity = basesR.get(position);
        Zujian zujian = null;
        if (convertView == null) {
            zujian = new Zujian();
            // 获得组件，实例化组件
            convertView = layoutInflater.inflate(
                    R.layout.section1activity_basestation_listitem, null);
            zujian.carnum = (TextView) convertView
                    .findViewById(R.id.carnum_basestation);
            zujian.passtime = (TextView) convertView
                    .findViewById(R.id.passtime_basestation);

            convertView.setTag(zujian);
        } else {
            zujian = (Zujian) convertView.getTag();
        }
        zujian.carnum.setText(entity.getCarnum());
        zujian.passtime.setText(entity.getPassingtime());
        return convertView;

    }

}
