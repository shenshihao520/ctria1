package cn.apputest.ctria.myapplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.apputest.ctria.data.UserInformationDataEntity;
import cn.apputest.ctria.section1.Section1Activity;
import cn.apputest.ctria.service.Constants;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.example.dcrc2.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author shenshihao 登录
 */
public class Login extends Activity {
    private AutoCompleteTextView editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;
    String responseCode = "0";
    private String username;
    private String password;
    private DBHelper helper;
    private DBManager mgr;
    private DBHelper helper_V;
    private DBManager mgr_V;
    private Context context;
    public static final String KEY_LOGIN = "login";
    public static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    public static final String FILE_USER = "data";
    public static final String KEY_SHOWPASSWORD = "showpassword";
    private SharedPreferences preferences;
    boolean IsNetWorkReady = true;
    String DBName;
    Toast toast;
    boolean hastoast = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(FILE_USER, MODE_PRIVATE);


        initView();

    }

    // 初始化
    private void initView() {

        context = this;
        toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 50);
        isNetworkConnected(context);

        editTextUsername = (AutoCompleteTextView) findViewById(R.id.edittext_user_name);
        editTextPassword = (EditText) findViewById(R.id.edittext_user_password);
        loginButton = (Button) findViewById(R.id.button_login);
        editTextUsername.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initAutoComplete("history", editTextUsername);
            }
        });
        //
        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // version_num = preferences.getInt("DataVerSion",0);

                DBName = editTextUsername.getText().toString();
                helper = new DBHelper(context, DBName + "_DB");
                mgr = new DBManager(helper);

//				
//				SharedPreferences.Editor editor = preferences
//						.edit();
//				editor.putString(KEY_NAME, "admin");
//				editor.putString(KEY_PASSWORD, "1");
//				editor.commit();
//				DBName = "admin";
//				
//				UserInformationDataEntity userinformation = new UserInformationDataEntity();
//				userinformation.setUsername("admin");
//				userinformation.setPassword("1");
//				mgr.addUserInformation(userinformation);

                confirm();

            }
        });
    }

    /**
     * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */
    private void initAutoComplete(String field,
                                  AutoCompleteTextView autoCompleteTextView) {
        SharedPreferences sp = getSharedPreferences("network_url", 0);
        String longhistory = sp.getString("history", "");
        String[] histories = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, histories);
        // 只保留最近的50条的记录
        if (histories.length > 50) {
            String[] newHistories = new String[50];
            System.arraycopy(histories, 0, newHistories, 0, 50);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, newHistories);
        }
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView
                .setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        AutoCompleteTextView view = (AutoCompleteTextView) v;
                        if (hasFocus) {
                            view.showDropDown();
                        }
                    }
                });
    }

    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */
    private void saveHistory(String field,
                             AutoCompleteTextView autoCompleteTextView) {
        String text = autoCompleteTextView.getText().toString();
        SharedPreferences sp = getSharedPreferences("network_url", 0);
        String longhistory = sp.getString(field, "");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }

    protected void onStart() {
        super.onStart();
    }

    // 程序暂停时保存用户数据到preferences
    @Override
    protected void onPause() {
        super.onPause();
        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();

    }

    // 验证用户名和密码的正确性
    void confirm() {
        isNetworkConnected(context);
        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();
        if (IsNetWorkReady) {
            post();
            postLog();
            // new LogControl().postLog(username,1);
        } else {
            UserInformationDataEntity user = new UserInformationDataEntity();
            System.out.println(username);
            user = mgr.queryUserInformation(username);

            if (user.getPassword() == null) {

                if (hastoast == false) {
                    hastoast = true;
                    toast = Toast.makeText(context, "第一次登录不能没有网络或用户名错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 205);
                    toast.show();
                    mHandler.postDelayed(mRunnable, 2050);
                }
            } else {
                if (user.getPassword().equals(password)) {
                    saveHistory("history", editTextUsername);
                    jump();
                } else {
                    if (hastoast == false) {
                        hastoast = true;


                        toast = Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 205);
                        toast.show();
                        mHandler.postDelayed(mRunnable, 2050);
                    }
                }

            }
        }

    }

    /**
     * 解决Toast的队列积攒过多问题
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    };

    private Handler mHandler;

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

    // 优先上传的密码和用户名到服务器验证，无网络则先从本地取
    void post() {
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

        params.addBodyParameter("userName", username);
        params.addBodyParameter("password", MD5.md5(password));

        HttpUtils http = new HttpUtils(Constants.TIMR_OUT);
        http.send(HttpMethod.POST, url.login, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println(responseInfo.result);
                        // saveHistory("history", editTextUsername);
                        Header[] headers = responseInfo.getAllHeaders();
                        for (Header head : headers) {

                            System.out.println(head.getName() + ":"
                                    + head.getValue());

                            if (head.getName().equals("resultCode")) {
                                responseCode = head.getValue();
                                if (responseCode.equals("0")) {
                                    UserInformationDataEntity userinformation = new UserInformationDataEntity();

                                    userinformation.setUsername(username);
                                    userinformation.setPassword(password);

                                    UserInformationDataEntity queryEntity = new UserInformationDataEntity();
                                    queryEntity = mgr
                                            .queryUserInformation(username);
                                    if (queryEntity.getUsername() == null) {
                                        mgr.addUserInformation(userinformation);
                                    } else if (queryEntity
                                            .equals(userinformation) == false) {
                                        mgr.updateUserInformation(userinformation);
                                    }

                                    SharedPreferences.Editor editor = preferences
                                            .edit();
                                    editor.putString(KEY_NAME, username);
                                    editor.putString(KEY_PASSWORD, password);
                                    editor.commit();

                                    // Intent intent = new Intent();
                                    // intent.setClass(Login.this,
                                    // MainActivity.class);
                                    // startActivity(intent);
                                    saveHistory("history", editTextUsername);
                                    jump();

                                } else {
                                    if (hastoast == false) {
                                        hastoast = true;

                                        toast = Toast.makeText(context, "用户名或密码错误",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.TOP, 0, 205);
                                        toast.show();
                                        mHandler.postDelayed(mRunnable, 2050);
                                    }
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
                        UserInformationDataEntity user = new UserInformationDataEntity();
                        System.out.println(username);
                        user = mgr.queryUserInformation(username);

                        Intent intent2 = new Intent(Login.this, Section1Activity.class);
                        startActivity(intent2);


                        if (user.getPassword() == null) {

                            if (hastoast == false) {
                                hastoast = true;
                                toast = Toast.makeText(context, "第一次登录或用户名错误",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 205);
                                toast.show();
                                mHandler.postDelayed(mRunnable, 2050);
                            }
                        } else {
                            if (user.getPassword().equals(password)) {
                                Intent intent = new Intent();
                                intent.setClass(Login.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                if (hastoast == false) {
                                    hastoast = true;
                                    toast = Toast.makeText(context, "用户名或密码错误",
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP, 0, 205);
                                    toast.show();
                                    mHandler.postDelayed(mRunnable, 2050);
                                }
                            }

                        }
                    }
                });
    }

    /**
     * 跳转方法
     */
    void jump() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        mgr.closeDB();
        startActivity(intent);
        finish();
    }

    // 上传Log日志
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

        params.addBodyParameter("userName", username);
        params.addBodyParameter("deviceName", getDeviceName());
        params.addBodyParameter("deviceIP", getLocalIPAddress());
        params.addBodyParameter("deviceMac", getLocalMacAddress());
        params.addBodyParameter("appID", requesthead.getAppID());
        params.addBodyParameter("logDescription", username + "：登录");
        params.addBodyParameter("logLevel", "1");
        params.addBodyParameter("logAction", "登录");
        params.addBodyParameter("logTime", requesthead.getRequestDatetime());

        HttpUtils http = new HttpUtils();
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

    /**
     * 检查网络状况
     *
     * @return
     */
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

    /**
     * 获取wifi基站MAC
     *
     * @return
     */
    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取当前WIFI IP
     *
     * @return
     */
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

    /**
     * 拼IP用
     *
     * @return
     */
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
            Log.e("WifiPreference IpAddress", ex.toString());
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//		mgr.closeDB();
        finish();
    }
}
