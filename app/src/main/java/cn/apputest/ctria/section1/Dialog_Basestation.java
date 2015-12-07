package cn.apputest.ctria.section1;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import cn.apputest.ctria.data.BasestationPassCarRecordDataEntity;
import cn.apputest.ctria.myapplication.Login;

import com.example.dcrc2.R;

import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.baidu.mapapi.model.LatLng;

/**
 * @author 作者Shihao Shen:
 * @version 创建时间：2015-11-24 下午5:25:07 类说明
 */
public class Dialog_Basestation {
    SharedPreferences preferencesuser;
    private DBHelper helper;
    private DBManager mgr;
    private ListView listView = null;
    private TextView title_textview;

    public void alert_BasestationDialog(Context context, LatLng latLng, String title) {

        final Dialog dialog = new Dialog(context, R.style.add_dialog);
        View vv = LayoutInflater.from(context).inflate(
                R.layout.section1activity_basestation, null);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(vv);
        title_textview = (TextView) vv.findViewById(R.id.S1title);
        title_textview.setText(title);
//		inti(context, vv ,latLng);
        Resources r = context.getResources();
        int x = (int) r.getDimension(R.dimen.x280);
        int y = (int) r.getDimension(R.dimen.y330);
        dialog.getWindow().setLayout(x, y);
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageButton back = (ImageButton) vv.findViewById(R.id.back_mark);

        OnClickListener backClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }

        };
        back.setOnClickListener(backClickListener);

        dialog.show();
    }

    public void inti(Context context, View vv, LatLng ll) {
        listView = (ListView) vv.findViewById(R.id.basestation_list);
        preferencesuser = context.getSharedPreferences(Login.FILE_USER,
                Context.MODE_PRIVATE);
        String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
        helper = new DBHelper(context, DBName + "_DB");
        mgr = new DBManager(helper);

        ArrayList<BasestationPassCarRecordDataEntity> list = getData(ll);
        listView.setAdapter(new Basestation_ListAdapter(context, list));

    }

    ArrayList<BasestationPassCarRecordDataEntity> getData(LatLng ll) {
        ArrayList<BasestationPassCarRecordDataEntity> bases = new ArrayList<BasestationPassCarRecordDataEntity>();

        bases = mgr.queryBasestationRecord(ll);

        return bases;

    }
}
