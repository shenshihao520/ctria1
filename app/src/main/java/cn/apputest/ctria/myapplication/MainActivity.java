package cn.apputest.ctria.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import cn.apputest.ctria.section1.Section1Activity;
import cn.apputest.ctria.section2.Section2Activity;
import cn.apputest.ctria.section3.Section3Activity;
import cn.apputest.ctria.section4.PushService;
import cn.apputest.ctria.section4.Section4Activity;
import cn.apputest.ctria.service.UpdateDataService;
import cn.apputest.ctria.service.UpdateVersionService;
import cn.apputest.ctria.service.UploadFailRecordService;

import com.example.dcrc2.R;
import com.fntech.m10.gpio.M10_GPIO;
import com.fntech.m10.u1.rfid.reader.Manager;


/**
 * 主函数 囊括4个Activity（Tab表签）
 */
public class MainActivity extends Activity {
    List<View> listViews;
    Context context = null;
    LocalActivityManager manager = null;
    TabHost tabHost = null;
    String username = null;
    private String mDeviceID;
    private ProgressDialog pBar;
    SharedPreferences preferencesuser;
    TextView weidu_text;
    Handler anewhandler;
    boolean havemessages = true;
    Bundle savedInstanceState;
    private Manager managerSweap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.homemain);
        // username = QuairData("User_DB","userinformation","1",aname);
        context = MainActivity.this;

        M10_GPIO.U1_PowerOn();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("a message is coming"); // 为BroadcastReceiver指定action，即要监听的消息名字。
//		intentFilter.addAction("loading complate");
        registerReceiver(br, intentFilter); // 注册监听


        PushService pu = new PushService();
        pu.onCreate(context);
        preferencesuser = context.getSharedPreferences(Login.FILE_USER,
                Context.MODE_PRIVATE);

        mDeviceID = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        inti(savedInstanceState);
        UploadFailRecordService.setServiceAlarm(context);

        UpdateDataService.setServiceAlarm(context);


    }

    void inti(Bundle savedInstanceState) {
        UpdateVersionService up = new UpdateVersionService();
        up.CheckUpdate(context);
        // 定放一个放view的list，用于存放viewPager用到的view
        listViews = new ArrayList<View>();

        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);

        Intent i1 = new Intent(context, Section1Activity.class);
        listViews.add(getView("A", i1));
        Intent i2 = new Intent(context, Section2Activity.class);
        listViews.add(getView("B", i2));
        Intent i3 = new Intent(context, Section3Activity.class);
        listViews.add(getView("C", i3));
        Intent i4 = new Intent(context, Section4Activity.class);
        listViews.add(getView("D", i4));

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.setup(manager);

        // 这儿主要是自定义一下tabhost中的tab的样式
        View tabIndicator1 = LayoutInflater.from(this).inflate(
                R.layout.hometab_1, null);
        TextView tvTab1 = (TextView) tabIndicator1.findViewById(R.id.tv);
        tvTab1.setText("定位");

        View tabIndicator2 = LayoutInflater.from(this).inflate(
                R.layout.hometab_2, null);
        TextView tvTab2 = (TextView) tabIndicator2.findViewById(R.id.tv);
        tvTab2.setText("路查");

        View tabIndicator3 = LayoutInflater.from(this).inflate(
                R.layout.hometab_3, null);
        TextView tvTab3 = (TextView) tabIndicator3.findViewById(R.id.tv);
        tvTab3.setText("综合");

        View tabIndicator4 = LayoutInflater.from(this).inflate(
                R.layout.hometab_4, null);
        TextView tvTab4 = (TextView) tabIndicator4.findViewById(R.id.tv);
        tvTab4.setText("消息");
        weidu_text = (TextView) tabIndicator4.findViewById(R.id.wx_textview);
        SharedPreferences.Editor editor = preferencesuser.edit();
        editor.putInt("weidu", 0);
        editor.commit();

        int weidu = preferencesuser.getInt("weidu", 0);
        if (weidu == 0) {
            weidu_text.setVisibility(View.INVISIBLE);
        } else {
            String a = String.valueOf(weidu);
            weidu_text.setText(a);
            weidu_text.setVisibility(View.VISIBLE);
        }

        // Intent intent = new Intent(context, EmptyActivity.class);
        // 注意这儿Intent中的activity不能是自身,所以我弄了个空的acitivity
        // tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator1)
        // .setContent(i1));
        tabHost.addTab(tabHost
                .newTabSpec("A")
                .setIndicator(tabIndicator1)
                .setContent(
                        new Intent(this, Section1Activity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator2)
                .setContent(new Intent(this, Section2Activity.class)));
        // .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabHost.addTab(tabHost.newTabSpec("C").setIndicator(tabIndicator3)
                .setContent(new Intent(this, Section3Activity.class)));
        // .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabHost.addTab(tabHost
                .newTabSpec("D")
                .setIndicator(tabIndicator4)
                .setContent(
                        new Intent(this, Section4Activity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        // tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator2)
        // .setContent(i2));
        // tabHost.addTab(tabHost.newTabSpec("C").setIndicator(tabIndicator3)
        // .setContent(i3));
        // tabHost.addTab(tabHost.newTabSpec("D").setIndicator(tabIndicator4)
        // .setContent(i4));

        // 点击tabhost中的tab时，要切换下面的viewPager
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if ("A".equals(tabId)) {
                    tabHost.setCurrentTab(0);
                    havemessages = true;
                }
                if ("B".equals(tabId)) {

                    tabHost.setCurrentTab(1);
                    havemessages = true;
                }
                if ("C".equals(tabId)) {
                    tabHost.setCurrentTab(2);
                    havemessages = true;
                }
                if ("D".equals(tabId)) {
                    havemessages = false;
                    // preferencesuser =
                    // context.getSharedPreferences(Login.FILE_USER,
                    // Context.MODE_PRIVATE);
                    // int weidu = preferencesuser.getInt("weidu", 1);
                    weidu_text.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = preferencesuser.edit();
                    editor.putInt("weidu", 0);
                    editor.commit();

                    tabHost.setCurrentTab(3);
                }
            }
        });
        anewhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 3s后执行代码
                if (msg.what == 1) {
                    if (havemessages == true) {
                        int weidu = preferencesuser.getInt("weidu", 1);
                        String a = String.valueOf(weidu);
                        weidu_text.setText(a);
                        weidu_text.setVisibility(View.VISIBLE);
                    }

                }
            }
        };
    }

    /**
     * 接收新消息时提醒界面  改变ui
     */
    BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if ("a message is coming".equals(action)) // 判断是否接到电池变换消息
            {
                // 处理内容
                anewhandler.sendEmptyMessage(1);
            }
//			if("loading complate".equals(action))
//			{
//
//			}
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("InventoryActivity-onResume");
        if (managerSweap == null) {
            try {
                managerSweap = Manager.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }
        //manager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("InventoryActivity-onPause");
//			managerSweap.suspend();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        managerSweap.release();
        M10_GPIO.U1_PowerOff();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

}
