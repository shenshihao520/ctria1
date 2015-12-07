/**
 * 
 */
package cn.apputest.caas.util;


import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import cn.apputest.caas.extinterface.iisiot.IRequst;
import cn.apputest.caas.extinterface.iisiot.RequestResult;
import cn.apputest.caas.extinterface.iisiot.staticinfo.VehicleApprovedQualityRequest;
import cn.apputest.caas.extinterface.iisiot.staticinfo.VehiclePlateNumberRequest;
import cn.apputest.caas.extinterface.iisiot.staticinfo.VehicleTractionMassRequest;
import cn.apputest.caas.extinterface.iisiot.staticinfo.VehicleUnladenMassRequest;
import cn.apputest.caas.extinterface.iisiot.staticinfo.VehicleVINRequest;

/** 
 * @author 作者Shihao Shen: 
 * @version 创建时间：2015-12-2 下午3:28:02 
 * 类说明 
 */
/**
 * @author Shihao Shen
 *
 *
 */
public class CarInfoGetterThread implements Runnable {

   public HashMap<String,String> resMap = new HashMap<String,String>();
   public String errorMsg="";
   public boolean isCompleted = false;
   public Handler handler;
	String tagCode;
   public CarInfoGetterThread(Handler handler, String tagCode){
	   this.handler = handler;
	   this.tagCode = tagCode;
	   
   }
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		resMap.put("tagCode", tagCode);
		try{
		//获取VIN
		VehicleVINRequest vinReq = new VehicleVINRequest(tagCode);
		RequestResult res = vinReq.execute();
		
		if(res.getErrorCode()!=0){
			errorMsg="获取VIN出错";
			isCompleted = true;
			//Toast.makeText(getActivity(), "获取VIN出错", Toast.LENGTH_SHORT).show();
			return;
		}		
		String vin = res.getResult().toString();
		resMap.put("vin", vin);
		//获取车牌号
		VehiclePlateNumberRequest pnReq = new VehiclePlateNumberRequest(
				tagCode, IRequst.TAG_CODE_TYPE);
		res = pnReq.execute();
		if(res.getErrorCode()!=0){
			errorMsg="获取车牌号出错";
			isCompleted = true;
			return;
		}		
		String plateNumber = res.getResult().toString();
		resMap.put("plateNumber", plateNumber);
		//获取核定质量
		VehicleApprovedQualityRequest vaReq = new VehicleApprovedQualityRequest(
				tagCode, IRequst.TAG_CODE_TYPE);
		res = vaReq.execute();
		if(res.getErrorCode()!=0){
			errorMsg="获取核定质量出错";
			isCompleted = true;
			return;
		}		
		String approvedQuality =  res.getResult().toString();
		resMap.put("approvedQuality", approvedQuality);
		//获取牵引总质量
		VehicleTractionMassRequest tmReq = new VehicleTractionMassRequest(
				tagCode, IRequst.TAG_CODE_TYPE);
		res = tmReq.execute();
		
		if(res.getErrorCode()!=0){
			errorMsg="获取准牵引总质量出错";
			isCompleted = true;
			return;
		}		
		String VehicleTractionMass =  res.getResult().toString();
		resMap.put("vehicleTractionMass", VehicleTractionMass);
		//获取车辆的整备质量
		VehicleUnladenMassRequest vmReq = new VehicleUnladenMassRequest(
				tagCode, IRequst.TAG_CODE_TYPE);
		res =  vmReq.execute();
		if(res.getErrorCode()!=0){
			errorMsg="获取车辆的整备质量出错";
			isCompleted = true;
			return;
		}
		String VehicleUnladenMass =  res.getResult().toString();
		resMap.put("vehicleUnladenMass", VehicleUnladenMass);
		}catch(Exception e )
		{
			e.printStackTrace();
		}
		finally{
			Message msg = new Message();
			msg.what=1;
			msg.obj = resMap;
			handler.sendMessage(msg);
			
			Message msg2 = new Message();
			msg2.what=2;
			msg2.obj = errorMsg;
			handler.sendMessage(msg2);
		}
	

	}

}
