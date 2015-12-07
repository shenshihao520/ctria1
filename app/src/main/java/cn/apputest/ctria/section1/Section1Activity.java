package cn.apputest.ctria.section1;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ZoomControls;

import cn.apputest.ctria.data.BasestationDataEntity;
import cn.apputest.ctria.myapplication.Login;
import cn.apputest.ctria.sql.DBHelper;
import cn.apputest.ctria.sql.DBManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.dcrc2.R;

/**
 * @author Shihao Shen 定位地图方法实现类
 */
public class Section1Activity extends Activity {

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    public NearbyInfoListener myRadarListener_N = new NearbyInfoListener();
    // public AutoupLoad autoload = new AutoupLoad();
    MapView mMapView;
    BaiduMap mBaiduMap;
    Context context;
    // UI相关
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton, nextPage;
    boolean isFirstLoc = true;// 是否首次定位
    RadarSearchManager mManager;
    BitmapDescriptor ff3;
    BitmapDescriptor ff4;
    BitmapDescriptor ff5;
    int indexPage = 0;
    int totalPage = 0;
    LatLng pt;
    private DBHelper helper;
    private DBManager mgr;
    SharedPreferences preferencesuser;
    ArrayList<BasestationDataEntity> bases;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        preferencesuser = context.getSharedPreferences(Login.FILE_USER,
                MODE_PRIVATE);
        String DBName = preferencesuser.getString(Login.KEY_NAME, "1");
        helper = new DBHelper(context, DBName + "_DB");
        mgr = new DBManager(helper);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.section1activity);
        ff3 = BitmapDescriptorFactory.fromResource(R.drawable.seek_bar_thumb); // 基站图标
        ff5 = BitmapDescriptorFactory.fromResource(R.drawable.seekmb); // 附近用户图标
        requestLocButton = (Button) findViewById(R.id.button1);
        // nextPage = (Button)findViewById(R.id.button2);

        mManager = RadarSearchManager.getInstance();
        // 地图初始化
        SDKInitializer.initialize(this);
        mMapView = (MapView) findViewById(R.id.bmapView);

        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        // 去掉放大缩小按钮
        mMapView.showZoomControls(false);

        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mBaiduMap.setOnMapClickListener(new OnMapClickListener() { // 监听地图手势动作

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng latLng) { // 当点击地图任意其他地方时弹出框消失

                //
                // mBaiduMap.hideInfoWindow();
            }

        });
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() { // 监听地图上的Marker

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                // arg0.getPosition(); // 点击时显示过车记录
                displayInfoWindow(arg0.getPosition(), arg0.getTitle());

                return false;
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() { // 监听地图状态变化

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChange(MapStatus arg0) { // 滑动地图时
                // TODO Auto-generated method stub
                // mBaiduMap.hideInfoWindow();
            }
        });
        // mManager.startUploadAuto(autoload, 5000);
        OnClickListener btnClickListener = new OnClickListener() { // 点击定位按钮

            @Override
            public void onClick(View v) {
                clear(); // 清理上次的记录 标记 例如附近的人
                BDLocation location = mLocClient.getLastKnownLocation();
                // TODO Auto-generated method stub

                if (location != null) {
                    LatLng ll = new LatLng(location.getLatitude(), // 获取当前的位置
                            location.getLongitude());
                    System.out.println(ll + "++++++++++++++++++++++++++++");
                    pt = ll;
                    MapStatus mMapStatus = new MapStatus.Builder().target(ll) // 定义一个Map的状态变化
                            .zoom(18).build();
                    // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                            .newMapStatus(mMapStatus);
                    // 改变地图状态
                    mBaiduMap.animateMapStatus(mMapStatusUpdate);
                    uploadLoction(ll);
                    indexPage = 0;
                    serachNearBy(ll);
                    Mark();
                } else {
                    Toast.makeText(context, "目前无法定位", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };
        OnClickListener btnClickListener2 = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Mark();
                // addTextOptions();
                // clear();
                // indexPage++;
                // BDLocation location = mLocClient.getLastKnownLocation();
                // // TODO Auto-generated method stub
                //
                // LatLng ll = new LatLng(location.getLatitude(),
                // location.getLongitude());
                // pt = ll;
                // MapStatus mMapStatus = new MapStatus.Builder().target(ll)
                // .zoom(18).build();
                // // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                // MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                // .newMapStatus(mMapStatus);
                // // 改变地图状态
                // mBaiduMap.animateMapStatus(mMapStatusUpdate);
                // uploadLoction(ll);

                // serachNearBy(ll);

            }

        };
        requestLocButton.setOnClickListener(btnClickListener);
        // nextPage.setOnClickListener(btnClickListener2);

    }

    /**
     * marker
     */
    public void Mark() {
        bases = mgr.queryBasestation();

        for (int i = 0; i < bases.size(); i++) {

            MarkerOptions option = new MarkerOptions().icon(ff3).position(
                    bases.get(i).getLatlng());
            option.title(bases.get(i).getName());
            mBaiduMap.addOverlay(option);

        }
    }

    /*
     * 弹出框覆盖物
     */
    private void displayInfoWindow(final LatLng latLng, String title) {

        if (!title.equals("app")) {
            new Dialog_Basestation().alert_BasestationDialog(context, latLng, title);
        }
        // 创建infowindow展示的view
        // Button btn = new Button(getApplicationContext());
        // btn.setBackgroundResource(R.drawable.puper);
        // LatLng lat1 = new LatLng(39.78989263896404, 116.50377479075914);
        // //根据不同位置展示信息
        // LatLng lat2 = new LatLng(39.79125096836994, 116.50200512890446);
        // if (latLng.latitude == lat1.latitude
        // && latLng.longitude == lat1.longitude) {
        // btn.setText("有一辆车过了");
        // } else if (latLng.latitude == lat2.latitude
        // && latLng.longitude == lat2.longitude) {
        // btn.setText("有好多车过了");
        // }

        // infowindow点击事件
        // OnInfoWindowClickListener infoWindowClickListener = new
        // OnInfoWindowClickListener() { // 点击弹框
        // // 点击位置的详细信息
        // @Override
        // public void onInfoWindowClick() {
        // // reverseGeoCode(latLng);
        // // 隐藏InfoWindow
        // // mBaiduMap.hideInfoWindow();
        // }
        // };
        // 创建infowindow
        // InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47,
        // backClickListener);
        // InfoWindow infoWindow = new InfoWindow(vv, latLng, -47);
        //
        // // 显示InfoWindow
        // mBaiduMap.showInfoWindow(infoWindow);

    }

    /**
     * 反地理编码
     */
    public void reverseGeoCode(LatLng latLng) {
        GeoCoder geoCoder = GeoCoder.newInstance();
        //
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    Toast.makeText(Section1Activity.this, "抱歉，未能找到结果",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Section1Activity.this,
                            "位置：" + result.getAddress(), Toast.LENGTH_LONG)
                            .show();
                }
            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        //
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        // 释放地理编码检索实例
        // geoCoder.destroy();
    }

    /**
     * 添加文字覆盖物
     */
    private void addTextOptions() {
        mBaiduMap.clear();
        LatLng latLng = new LatLng(39, 40);
        TextOptions textOptions = new TextOptions();
        textOptions.bgColor(0xAAFFFF00) // 設置文字覆蓋物背景顏色
                .fontSize(28) // 设置字体大小
                .fontColor(0xFFFF00FF)// 设置字体颜色
                .text("我在这里啊！！！！") // 文字内容
                .rotate(-30) // 设置文字的旋转角度
                .position(latLng);// 设置位置
        mBaiduMap.addOverlay(textOptions);
    }

    /**
     * 雷达周边
     *
     * @return
     */
    public void uploadLoction(LatLng ll) {
        String a = getDeviceName();
        // 周边雷达设置监听
        mManager.addNearbyInfoListener(myRadarListener_N);
        // 周边雷达设置用户身份标识，id为空默认是设备标识
        mManager.setUserID(null);
        // 上传位置
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = "shen";
        info.pt = ll;
        mManager.uploadInfoRequest(info);
        // 监听上传结果

    }

    // public class AutoupLoad implements RadarUploadInfoCallback
    // {
    //
    // @Override
    // public RadarUploadInfo onUploadInfoCallback() {
    // // TODO Auto-generated method stub
    // RadarUploadInfo info = new RadarUploadInfo();
    // System.out.println("zidong");
    // System.out.println(pt);
    // info.comments ="haha";
    // info.pt = pt;
    // return info;
    // }
    //
    // }
    public class NearbyInfoListener implements RadarSearchListener { // 触发接口

        @Override
        public void onGetClearInfoState(RadarSearchError arg0) {
            // TODO Auto-generated method stub
            if (arg0 == RadarSearchError.RADAR_NO_ERROR) {
                // 清除成功
                Toast.makeText(context, "清除位置成功", Toast.LENGTH_SHORT).show();
            } else {
                // 清除失败
                Toast.makeText(context, "清除位置失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onGetNearbyInfoList(RadarNearbyResult arg0,
                                        RadarSearchError arg1) {
            // TODO Auto-generated method stub
            if (arg1 == RadarSearchError.RADAR_NO_ERROR) {
                Toast.makeText(context, "查询周边成功", Toast.LENGTH_SHORT).show();
                // 获取成功，处理数据
                totalPage = arg0.pageNum;
                parseResultToMap(arg0);

            } else {
                // 获取失败
                System.out.println(arg1 + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(arg0 + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Toast.makeText(context, "查询周边失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onGetUploadState(RadarSearchError arg0) {
            // TODO Auto-generated method stub
            if (arg0 == RadarSearchError.RADAR_NO_ERROR) {
                // 上传成功
                Toast.makeText(context, "单次上传位置成功", Toast.LENGTH_SHORT).show();
            } else {
                // 上传失败
                Toast.makeText(context, "单次上传位置失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 检查周边
     */
    public void serachNearBy(LatLng ll) {
        if (indexPage == 0) {

            System.out.println("查询周边");
            RadarNearbySearchOption option = new RadarNearbySearchOption()
                    .centerPt(ll).pageNum(indexPage).radius(5000).pageCapacity(100);  //疑点

            // 发起查询请求
            mManager.nearbyInfoRequest(option);
        } // /记住这里可以添加好几页 目前的方法是 for循环添加 需要知道一共有多少页
    }

    /**
     * 清楚周边信息
     */
    public void clear() {
        mManager.clearUserInfo();
    }

    /**
     * 更新地图 按照上次时间大于一小时不显示 距离小于不显示
     */
    public void parseResultToMap(RadarNearbyResult res) {
        // mBaiduMap.clear();
        // String a = getDeviceName();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,
                calendar.get(Calendar.HOUR_OF_DAY) - 1);

        if (res != null && res.infoList != null && res.infoList.size() > 0) {
            for (int i = 0; i < res.infoList.size(); i++) {
                if (res.infoList.get(i).distance > 1
                        && res.infoList.get(i).timeStamp.after(calendar
                        .getTime())) {
                    // if(res.infoList.get(i).timeStamp.after(date))
                    MarkerOptions option = new MarkerOptions().icon(ff5)
                            .position(res.infoList.get(i).pt);
                    option.title("app");
                    Bundle des = new Bundle();
                    if (res.infoList.get(i).comments == null
                            || res.infoList.get(i).comments.equals("")) {
                        des.putString("des", "没有备注");
                    } else {
                        des.putString("des", res.infoList.get(i).comments);
                    }
                    option.extraInfo(des);
                    mBaiduMap.addOverlay(option); // 添加附近的人
                }
                // System.out.println(calendar.getTime());

            }
        } else {
            //
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(5000)
                            // accuracy 蓝圈
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;

        mgr.closeDB();
    }

    public String getDeviceName() {
        // TODO Auto-generated method stub
        String DeviceName = null;
        DeviceName = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getDeviceId();
        return DeviceName;
    }
}
