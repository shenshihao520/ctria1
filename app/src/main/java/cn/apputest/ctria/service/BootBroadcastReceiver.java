package cn.apputest.ctria.service;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
/**
 * 
 * @author Shihao Shi
 *	定时检查  数据更新 及 失败重传的服务 是否在启动
 *
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        /**
         * 当出现以下action触发的时候
         */
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.i("BootBroadcastReceiver", "BroadcastReceiver onReceive here.... ");
            Handler handler = new Handler(Looper.getMainLooper());
            //after reboot the device,about 2 minutes later,upload the POI info to server
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                	if(!UpdateDataService.isServiceRunning(mContext,Constants.POI_SERVICE_updatedata)){
                    	UpdateDataService.setServiceAlarm(mContext);
                    }
                    if(!UploadFailRecordService.isServiceRunning(mContext,Constants.POI_SERVICE)){
                    	UploadFailRecordService.setServiceAlarm(mContext);
                    }
                    
                }
            }, Constants.BROADCAST_ELAPSED_TIME_DELAY);
        }
    }
}

