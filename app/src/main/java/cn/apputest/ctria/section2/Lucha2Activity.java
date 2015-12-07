package cn.apputest.ctria.section2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.apputest.ctria.data.DriverCertStatusDataEntity;
import cn.apputest.ctria.data.DriverLicenseCardDataEntity;
import cn.apputest.ctria.data.JobCertStatusDataEntity;
import cn.apputest.ctria.data.PeopleFailUploadDataEntity;
import cn.apputest.ctria.data.UploadTaskDataEntity;
import cn.apputest.ctria.myapplication.DateFormat;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.myapplication.RequestHead;
import cn.apputest.ctria.myapplication.Unicode;
import cn.apputest.ctria.myapplication.Url;
import cn.apputest.ctria.section3.Section3Activity;
import cn.apputest.ctria.service.Constants;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;
import com.fntech.m10.u1.rfid.reader.DCService;
import com.fntech.m10.u1.rfid.reader.IDCService;
import com.fntech.m10.u1.rfid.reader.InvalidParamException;
import com.fntech.m10.u1.rfid.reader.Manager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author Shihao Shen 人员路查路检 主页面
 */
public class Lucha2Activity extends Activity implements OnClickListener {
    private EditText qualificationsID;
    private EditText driverlicenseID;
    private TextView error1, error2;
    private TextView zheng1, zheng2;
    private RelativeLayout uploadrecordlucha, chancel1;
    private Context context;
    private DBHelper helper;
    private DBManager mgr;
    String drivername = null;
    boolean IsNetWorkReady = true;
    String IsJobCard = "0";
    String IsDriverLicense = "0";
    // private String name;
    private SharedPreferences preferences;
    Toast toast;
    public boolean hastoast = false;
    private Manager manager;
    public boolean scanSuccess = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section2activity_lucha2);
        // Log.i("Lucha2Activity", "Lucha2Activity");
        inti();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("InventoryActivity-onResume");
        if (manager == null) {
            try {
                manager = Manager.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
                // finish();
            }
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        manager.suspend();
    }

    public void openInterface() {
        manager.openInterface();

    }

    void inti() {

        if (manager == null) {
            try {
                manager = Manager.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
                // finish();
            }
        }
        openInterface();

        context = this.getParent();
        toast = new Toast(context);
        preferences = context.getSharedPreferences(Login.FILE_USER,
                MODE_PRIVATE);
        String DBName = preferences.getString(Login.KEY_NAME, "1");
        helper = new DBHelper(context, DBName + "_DB");
        mgr = new DBManager(helper);

        isNetworkConnected(context);
        qualificationsID = (EditText) findViewById(R.id.IDcardnum);
        driverlicenseID = (EditText) findViewById(R.id.DriverCardID);
        qualificationsID.addTextChangedListener(mTextWatcherjob);
        driverlicenseID.addTextChangedListener(mTextWatcherdriver);

        error1 = (TextView) findViewById(R.id.warning_P1);

        zheng1 = (TextView) findViewById(R.id.zheng_P1);
        zheng2 = (TextView) findViewById(R.id.zheng_P2);

        zheng1.setOnClickListener(this);
        zheng2.setOnClickListener(this);

        uploadrecordlucha = (RelativeLayout) findViewById(R.id.uploadrecordlucha);
        chancel1 = (RelativeLayout) findViewById(R.id.chancel1);

        uploadrecordlucha.setOnClickListener(this);
        chancel1.setOnClickListener(this);

        qualificationsID.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    error1.setText("");
                    zheng2.setTextColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });
        driverlicenseID.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    error1.setText("");
                    zheng1.setTextColor(getResources().getColor(R.color.black));

                }
                return false;
            }
        });
    }

    void SweepCard() {
        IDCService dcservice = new DCService();
        try {
            Set<String> scanresult = dcservice.inventory();
            if (scanresult == null || scanresult.size() == 0) {
                if (hastoast == false) {
                    hastoast = true;
                    Toast.makeText(context, "请重新扫卡", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 2000);
                }
            } else {
                scanSuccess = true;

                Iterator<String> iter = scanresult.iterator();
                String tagCode = "";
                if (iter.hasNext()) {
                    tagCode = iter.next();
                }

                Map<String, String> results = dcservice.checkDriver(tagCode);

//				System.out.println("results:" + results);

                driverlicenseID.setText(results.get("DriverID"));
                qualificationsID.setText(results.get("CertificateCardNo"));
                getdriverCertStatus();
                getjobCertStatus();
            }

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidParamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void cancelB() {
        qualificationsID.setText("");
        driverlicenseID.setText("");
        error1.setText("");
        zheng1.setTextColor(getResources().getColor(R.color.black));
        zheng2.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.uploadrecordlucha:
                uploadrecordlucha();
                break;
            case R.id.chancel1:
                cancelB();
                break;
            case R.id.zheng_P2:
                if (qualificationsID.getText().toString().equals("")) {
                    if (hastoast == false) {
                        hastoast = true;
                        Toast.makeText(context, "从业资格证为空", Toast.LENGTH_SHORT)
                                .show();
                        mHandler.postDelayed(mRunnable, 2000);
                    }
                } else {
                    new Dialog_JobCard().alert_JobCardDialog(context,
                            qualificationsID);
                }
                break;
            case R.id.zheng_P1:
                if (driverlicenseID.getText().toString().equals("")) {
                    if (hastoast == false) {
                        hastoast = true;
                        Toast.makeText(context, "驾驶证为空", Toast.LENGTH_SHORT).show();
                        mHandler.postDelayed(mRunnable, 2000);
                    }
                } else {
                    new Dialog_DriverLicenseCard().alert_DriverLicenseCardDialog(
                            context, driverlicenseID);
                }
                break;
            default:
                break;
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    };

    Handler mHandler;

    {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 3s后执行代码
                hastoast = false;
            }
        };
    }

    /**
     * 上传记录时不能 证件号码必须不为空 18位
     */
    void uploadrecordlucha() {
        if (qualificationsID.getText().toString().equals("")) {
            if (hastoast == false) {
                hastoast = true;
                Toast.makeText(context, "从业资格证为空", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(mRunnable, 2000);
            }
        } else if (driverlicenseID.getText().toString().equals("")) {
            if (hastoast == false) {
                hastoast = true;
                Toast.makeText(context, "驾驶证为空", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(mRunnable, 2000);
            }
        } else {
            if (qualificationsID.getText().toString().length() == 18
                    && driverlicenseID.getText().toString().length() == 18) {
                getDriverName();
            } else {
                if (hastoast == false) {
                    hastoast = true;
                    Toast.makeText(context, "驾驶证或从业资格证不足18位",
                            Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 2000);
                }
            }

        }
    }

    void postpersoncheckrecord() {

        Url url = new Url();
        RequestHead requesthead = new RequestHead();
        RequestParams params = new RequestParams();

        params.addHeader("userName", requesthead.getUserName());
        params.addHeader("requestDatetime", requesthead.getRequestDatetime());
        params.addHeader("appID", requesthead.getAppID());
        params.addHeader("signature", requesthead.getSignature());

        preferences = getSharedPreferences(Login.FILE_USER,
                Section3Activity.MODE_PRIVATE);
        final String username = preferences.getString(Login.KEY_NAME, "1");

        params.addBodyParameter("userID", username);
        params.addBodyParameter("baseStationID", getLocalMacAddress());
        params.addBodyParameter("status4DriverLicense",
                String.valueOf(IsDriverLicense));
        params.addBodyParameter("status4JobCert", String.valueOf(IsJobCard));
        // params.addBodyParameter("checkTime", "2008-08-08 12:12:12");

        HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
        http.send(HttpMethod.POST, url.getPersoncheckrecord(), params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println(responseInfo.result);
                        Header[] headers = responseInfo.getAllHeaders();
                        if (headers != null)
                            for (Header head : headers) {
                                System.out.println(head.getName() + ":"
                                        + head.getValue());
                            }

                        if (IsNetWorkReady) {

                            UploadTaskDataEntity entity = new UploadTaskDataEntity();
                            entity.setCheckcategory(drivername);
                            entity.setTime(new DateFormat().getDate());
                            entity.setUploadcondition("上传成功");
                            mgr.addUploadTask_People(entity);
                            if (hastoast == false) {
                                hastoast = true;
                                Toast.makeText(context, "上传成功",
                                        Toast.LENGTH_SHORT).show();
                                mHandler.postDelayed(mRunnable, 2000);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        System.out.println("onFailure");
                        IsNetWorkReady = false;
                        if (IsNetWorkReady == false) {
                            UploadTaskDataEntity entity = new UploadTaskDataEntity();
                            entity.setCheckcategory(drivername);
                            entity.setTime(new DateFormat().getDate());
                            entity.setUploadcondition("上传失败");
                            if (hastoast == false) {
                                hastoast = true;
                                Toast.makeText(context, "上传失败",
                                        Toast.LENGTH_SHORT).show();
                                mHandler.postDelayed(mRunnable, 2000);
                            }
                            mgr.addUploadTask_People(entity);

                            PeopleFailUploadDataEntity peoplefailupload = new PeopleFailUploadDataEntity();
                            peoplefailupload.setUserID(username);
                            peoplefailupload
                                    .setBaseStationID(getLocalMacAddress());
                            peoplefailupload.setStatus4DriverLicense(String
                                    .valueOf(IsDriverLicense));
                            peoplefailupload.setStatus4JobCert(String
                                    .valueOf(IsJobCard));
                            peoplefailupload.setDriverName(drivername);
                            mgr.addPeopleFailUpload(peoplefailupload);
                        }
                    }
                });
    }

    /**
     * @return 获取司机名字
     */
    String getDriverName() {
        // TODO Auto-generated method stub
        DriverLicenseCardDataEntity driverlicense = new DriverLicenseCardDataEntity();
        String cariD = driverlicenseID.getText().toString();
        driverlicense = mgr.queryDriverLicenseCard(cariD);
        drivername = driverlicense.getDrivername();
        // System.out.println(drivername);
        postpersoncheckrecord();

        return drivername;

    }

    /**
     * 获取驾驶证状态
     */
    String getdriverCertStatus() {

        DriverCertStatusDataEntity driverlicense_net = new DriverCertStatusDataEntity();
        String cariD = driverlicenseID.getText().toString();
        driverlicense_net = mgr.queryDrivercertstatus(cariD);
        if (driverlicense_net.getStatus() == null) {
            driverlicense_net.setStatus("-1");
        }
        IsDriverLicense = driverlicense_net.getStatus();

        if (driverlicense_net.getStatus().equals("1")) {
            if (StringUtils.isNotBlank(error1.getText().toString())) {
                error1.setText(error1.getText() + "/驾驶证过期");
            } else {
                error1.setText("驾驶证过期");
            }
            zheng1.setTextColor(getResources().getColor(R.color.warningtext));
        } else if (driverlicense_net.getStatus().equals("0")) {

            if (error1.getText().toString().equals("从业资格证正常")) {
                error1.setText(error1.getText() + "/驾驶证正常");
                error1.setTextColor(getResources().getColor(R.color.black));
            }
            //
            else if (!StringUtils.isNotBlank(error1.getText().toString())) {
                error1.setText("驾驶证正常");
                error1.setTextColor(getResources().getColor(R.color.black));
            }
            zheng1.setTextColor(getResources().getColor(R.color.black));

        } else if (driverlicense_net.getStatus().equals("-1")) {
            if (StringUtils.isNotBlank(error1.getText().toString())) {
                error1.setText(error1.getText() + "/驾驶证不存在");
            } else {
                error1.setText("驾驶证不存在");
            }

            zheng1.setTextColor(getResources().getColor(R.color.warningtext));
        }
        return IsDriverLicense;
    }

    /**
     * 获取从业资格证状态
     */
    String getjobCertStatus() {

        JobCertStatusDataEntity jobcard = new JobCertStatusDataEntity();
        String cariD = qualificationsID.getText().toString();
        jobcard = mgr.queryJobCertStatus(cariD);
        if (jobcard.getStatus() == null) {
            jobcard.setStatus("-1");
        }
        IsJobCard = jobcard.getStatus();
        // addlocalDB(jobcard);
        if (jobcard.getStatus().equals("1")) {
            if (StringUtils.isNotBlank(error1.getText().toString())) {
                error1.setText(error1.getText() + "/从业资格证过期");
            } else {
                error1.setText("从业资格证过期");
            }

            zheng2.setTextColor(getResources().getColor(R.color.warningtext));
        } else if (jobcard.getStatus().equals("0")) {
            if (error1.getText().toString().equals("驾驶证正常")) {
                error1.setText(error1.getText() + "/从业资格证正常");
                error1.setTextColor(getResources().getColor(R.color.black));
            } else if (!StringUtils.isNotBlank(error1.getText().toString())) {
                error1.setText("从业资格证正常");
                error1.setTextColor(getResources().getColor(R.color.black));
            }

            zheng2.setTextColor(getResources().getColor(R.color.black));

        } else if (jobcard.getStatus().equals("-1")) {
            if (StringUtils.isNotBlank(error1.getText().toString())) {
                error1.setText(error1.getText() + "/从业资格证不存在");
            } else {
                error1.setText("从业资格证不存在");
            }

            zheng2.setTextColor(getResources().getColor(R.color.warningtext));
        }
        return IsJobCard;
    }

    void SweepCardData() {
        getdriverCertStatus();
        getjobCertStatus();
    }

    /**
     * 上传日志
     */
    void postLog() {
        Url url = new Url();
        RequestHead requesthead = new RequestHead();
        RequestParams params = new RequestParams();

        params.addHeader("userName", requesthead.getUserName());
        System.out.println(requesthead.getUserName());
        params.addHeader("requestDatetime", requesthead.getRequestDatetime());
        System.out.println(requesthead.getRequestDatetime());
        params.addHeader("appID", requesthead.getAppID());
        System.out.println(requesthead.getAppID());
        params.addHeader("signature", requesthead.getSignature());
        System.out.println(requesthead.getSignature());

        SharedPreferences preferencesuser = getSharedPreferences(
                Login.FILE_USER, MODE_PRIVATE);
        String username = preferencesuser.getString(Login.KEY_NAME, "1");

        params.addBodyParameter("userName", username);
        params.addBodyParameter("deviceName", getDeviceName());
        params.addBodyParameter("deviceIP", getLocalIPAddress());
        params.addBodyParameter("deviceMac", getLocalMacAddress());
        params.addBodyParameter("appID", requesthead.getAppID());
        params.addBodyParameter("logDescription", drivername + "：上传人员路查记录");
        params.addBodyParameter("logLevel", "1");
        params.addBodyParameter("logAction", "上传");
        params.addBodyParameter("logTime", requesthead.getRequestDatetime());

        HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
        http.send(HttpMethod.POST, url.getLog(), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println(responseInfo.result);
                        Header[] headers = responseInfo.getAllHeaders();
                        for (Header head : headers) {

                            System.out.println(head.getName() + ":"
                                    + head.getValue());

                            if (head.getName().equals("resultCode")) {
                                String responseCode = head.getValue();
                                if (responseCode.equals("0")) {
                                    System.out.println("Log已上传");
                                } else {
                                    System.out.println("Log上传失败");
                                }

                            }
                            if (head.getName().equals("resultMsg")) {

                                System.out.println(head.getName()
                                        + ":"
                                        + new Unicode().unicodeToString(head
                                        .getValue()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        System.out.println("onFailure");
                    }
                });
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

    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public String getLocalIPAddress() {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            String ip = getLocalIpAddressGPRS();
            return ip;
        } else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public String getLocalIpAddressGPRS() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreferenceIpAddress","ex.toString()");
        }
        return null;
    }

    public String getDeviceName() {
        // TODO Auto-generated method stub
        String DeviceName = null;
        DeviceName = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getDeviceId();
        return DeviceName;
    }

    TextWatcher mTextWatcherjob = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            // mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = qualificationsID.getSelectionStart();
            editEnd = qualificationsID.getSelectionEnd();
            // mTextView.setText("您输入了" + temp.length() + "个字符");
            if (temp.length() == 12) {
                if (driverlicenseID.length() == 18) {
                    // SweepCardData();
                    // } else {
                    // if (hastoast == false) {
                    // hastoast = true;
                    // toast.makeText(context, "请输入正确的驾驶证号", Toast.LENGTH_SHORT)
                    // .show();
                    // mHandler.postDelayed(mRunnable, 2000);
                    // }
                    getdriverCertStatus();
                }
                getjobCertStatus();
            }
        }

    };
    TextWatcher mTextWatcherdriver = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            // mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = driverlicenseID.getSelectionStart();
            editEnd = driverlicenseID.getSelectionEnd();
            // mTextView.setText("您输入了" + temp.length() + "个字符");
            if (temp.length() == 18) {
                if (qualificationsID.length() == 12) {
                    // SweepCardData();
                    // } else {
                    // if (hastoast == false) {
                    // hastoast = true;
                    // toast.makeText(context, "请输入正确的从业资格证号",
                    // Toast.LENGTH_SHORT)
                    // .show();
                    // mHandler.postDelayed(mRunnable, 2000);
                    // }
                    getjobCertStatus();
                }
                getdriverCertStatus();
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 0) {
            if (!scanSuccess) {
                System.out.println(scanSuccess);
                SweepCard();
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == 0) {
            scanSuccess = false;
        }
        return super.onKeyUp(keyCode, event);
    }
}
