package cn.apputest.ctria.sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cn.apputest.ctria.data.BasestationDataEntity;
import cn.apputest.ctria.data.BasestationPassCarRecordDataEntity;
import cn.apputest.ctria.data.CarBasicInfoDataEntity;
import cn.apputest.ctria.data.CargoEmergencyMethodDataEntity;
import cn.apputest.ctria.data.CarsFailUploadDataEntity;
import cn.apputest.ctria.data.DataVersionInfo;
import cn.apputest.ctria.data.DriverCertStatusDataEntity;
import cn.apputest.ctria.data.DriverLicenseCardDataEntity;
import cn.apputest.ctria.data.InsuranceCardDataEntity;
import cn.apputest.ctria.data.JobCardDataEntity;
import cn.apputest.ctria.data.JobCertStatusDataEntity;
import cn.apputest.ctria.data.LatestTransportRecordDataEntity;
import cn.apputest.ctria.data.MessageDataEntity;
import cn.apputest.ctria.data.PeopleFailUploadDataEntity;
import cn.apputest.ctria.data.PermitCardDataEntity;
import cn.apputest.ctria.data.SpecialEquipUsageDataEntity;
import cn.apputest.ctria.data.TransportCardDataEntity;
import cn.apputest.ctria.data.UploadTaskDataEntity;
import cn.apputest.ctria.data.UserInformationDataEntity;
import cn.apputest.ctria.myapplication.DateFormat;

import com.baidu.mapapi.model.LatLng;

/**
 * 
 * @author Shihao Shen 数据库操作类
 * 
 */
public class DBManager {
	
	private SQLiteDatabase db;
//	private static DBManager dbmanager = new DBManager();
//	
//	private DBManager (){}
//	
//	public static DBManager getInstance()
//	{
//		
//		return dbmanager;
//	}
	
	public DBManager(DBHelper helper) {

		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
		System.out.println("creat database");
	}

	// 上传任务管理
	public void addUploadTask_Cars(UploadTaskDataEntity task) {
		System.out.println("add database");
		db.execSQL(
				"INSERT INTO UploadTask_Cars VALUES(null, ?, ?, ?)",
				new Object[] {

				task.getCheckcategory(), task.getTime(),
						task.getUploadcondition() });
	}

	public ArrayList<UploadTaskDataEntity> queryUploadTask_Cars(int listitemID) {
		ArrayList<UploadTaskDataEntity> tasks = new ArrayList<UploadTaskDataEntity>();
		Cursor c = queryTheCursorUploadTask_Cars(listitemID);
		while (c.moveToNext()) {
			UploadTaskDataEntity task = new UploadTaskDataEntity();
			task.setTaskID(c.getInt(c.getColumnIndex("taskID")));

			task.setCheckcategory(c.getString(c.getColumnIndex("checkcategory")));
			task.setTime(c.getString(c.getColumnIndex("time")));
			task.setUploadcondition(c.getString(c
					.getColumnIndex("uploadcondition")));

			tasks.add(task);
		}
		c.close();
		return tasks;
	}

	public Cursor queryTheCursorUploadTask_Cars(int listitemID) {
		// System.out.println("query database");
		// int listitemID1 = listitemID+1;
		// int listitemID2 = listitemID+10;
		Cursor c = db.rawQuery(
				"SELECT * FROM UploadTask_Cars order by time desc limit 10 offset "
						+ listitemID, null);
		return c;
	}

	public void addUploadTask_People(UploadTaskDataEntity task) {
		System.out.println("add database");
		db.execSQL(
				"INSERT INTO UploadTask_People VALUES(null, ?, ?, ?)",
				new Object[] {

				task.getCheckcategory(), task.getTime(),
						task.getUploadcondition() });
	}

	public ArrayList<UploadTaskDataEntity> queryUploadTask_People(int listitemID) {
		ArrayList<UploadTaskDataEntity> tasks = new ArrayList<UploadTaskDataEntity>();
		Cursor c = queryTheCursorUploadTask_People(listitemID);
		while (c.moveToNext()) {
			UploadTaskDataEntity task = new UploadTaskDataEntity();
			task.setTaskID(c.getInt(c.getColumnIndex("taskID")));

			task.setCheckcategory(c.getString(c.getColumnIndex("checkcategory")));
			task.setTime(c.getString(c.getColumnIndex("time")));
			task.setUploadcondition(c.getString(c
					.getColumnIndex("uploadcondition")));

			tasks.add(task);
		}
		c.close();
		return tasks;
	}

	public Cursor queryTheCursorUploadTask_People(int listitemID) {
		// System.out.println("query database");
		// int listitemID1 = listitemID+1;
		// int listitemID2 = listitemID+10;
		Cursor c = db.rawQuery(
				"SELECT * FROM UploadTask_People order by time desc limit 10 offset "
						+ listitemID, null);
		return c;
	}

	// 消息
	public void addMessage(MessageDataEntity message) {
		System.out.println("add database");
		db.execSQL("INSERT INTO Message VALUES(null, ?, ?)", new Object[] {
				message.getMessageTitle(), message.getMessage() });

	}

	public ArrayList<MessageDataEntity> queryMessage(int listitemID) {
		ArrayList<MessageDataEntity> messages = new ArrayList<MessageDataEntity>();
		Cursor c = queryTheCursorMessage(listitemID);
		while (c.moveToNext()) {
			MessageDataEntity message = new MessageDataEntity();
			message.setMessageTitle(c.getString(c
					.getColumnIndex("MessageTitle")));
			message.setID(c.getInt(c.getColumnIndex("ID")));
			message.setMessage(c.getString(c.getColumnIndex("Message")));

			messages.add(message);
		}
		c.close();
		return messages;
	}

	public Cursor queryTheCursorMessage(int listitemID) {

		Cursor c = db.rawQuery(
				"SELECT * FROM Message order by ID desc limit 10 offset "
						+ listitemID, null);
		return c;
	}

	// 危化品紧急处理方案
	public CargoEmergencyMethodDataEntity queryCargoEmergency(String CargoNameID) {

		Cursor c = queryTheCursorCargoEmergency(CargoNameID);
		CargoEmergencyMethodDataEntity cargo = new CargoEmergencyMethodDataEntity();
		while (c.moveToNext()) {
			cargo.setCargonameID(c.getString(c.getColumnIndex("ID")));
			cargo.setCargoname(c.getString(c.getColumnIndex("NAME")));
			cargo.setCargoemergencymethod("\n" + "危化品名称:"
					+ c.getString(c.getColumnIndex("NAME")) + "\n" + "别名:"
					+ c.getString(c.getColumnIndex("ALIAS_NAME")) + "\n"
					+ "英文名:" + c.getString(c.getColumnIndex("ENGLISH_NAME"))
					+ "\n" + "CAS号:"
					+ c.getString(c.getColumnIndex("CAS_NUMBER")) + "\n"
					+ "种类:" + c.getString(c.getColumnIndex("CATEGORY")) + "\n"
					+ "种类编码:" + c.getString(c.getColumnIndex("CATEGORY_CODE"))
					+ "\n");
		}
		c.close();

		return cargo;
	}

	public Cursor queryTheCursorCargoEmergency(String CargoNameID) {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM or_dangerchemical where ID='"
				+ CargoNameID + "'", null);
		return c;
	}

	public String queryCargoEmergencyMethod(String CargoNameID) {

		Cursor c = queryTheCursorCargoEmergencyMethod(CargoNameID);
		String cargo = null;
		while (c.moveToNext()) {

			cargo =  "特别警示:"
					+ c.getString(c.getColumnIndex("SPECIAL_MSG")) + 
					"\n"
					+ "危害信息:" 
					+ c.getString(c.getColumnIndex("HAZARD_INFO"))
					+ "\n" + "安全措施:"
					+ c.getString(c.getColumnIndex("SAFETY_MEASURES")) + "\n"
					+ "应急处置原则:"
					+ c.getString(c.getColumnIndex("EMERGENCY_DISPOSAL"))
					+ "\n" + "创建时间:"
					+ c.getString(c.getColumnIndex("CREATE_TIME")) + "\n";

		}
		c.close();

		return cargo;
	}

	public Cursor queryTheCursorCargoEmergencyMethod(String CargoNameID) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM or_company_dangerchemical where ID='"
						+ CargoNameID + "'", null);
		return c;
	}

	// 通过名字获取危化品信息
	public CargoEmergencyMethodDataEntity queryCargoEmergencyfromName(
			String CargoName) {

		Cursor c = queryTheCursorCargoEmergency(CargoName);
		CargoEmergencyMethodDataEntity cargo = new CargoEmergencyMethodDataEntity();
		while (c.moveToNext()) {
			cargo.setCargonameID(c.getString(c.getColumnIndex("ID")));
			cargo.setCargoname(c.getString(c.getColumnIndex("NAME")));
			cargo.setCargoemergencymethod("\n" + "危化品名称:"
					+ c.getString(c.getColumnIndex("NAME")) + "\n" + "别名:"
					+ c.getString(c.getColumnIndex("ALIAS_NAME")) + "\n"
					+ "英文名:" + c.getString(c.getColumnIndex("ENGLISH_NAME"))
					+ "\n" + "CAS号:"
					+ c.getString(c.getColumnIndex("CAS_NUMBER")) + "\n"
					+ "种类:" + c.getString(c.getColumnIndex("CATEGORY")) + "\n"
					+ "种类编码:" + c.getString(c.getColumnIndex("CATEGORY_CODE"))
					+ "\n");
		}
		c.close();

		return cargo;
	}

	public Cursor queryTheCursorCargoEmergencyfromName(String CargoName) {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM or_dangerchemical where NAME='"
				+ CargoName + "'", null);
		return c;
	}

	// 获取货品ID
	public String queryCargoEmergencyID(String CargoName) {
		String id = null;
		Cursor c = queryTheCursorCargoEmergencyID(CargoName);
		while (c.moveToNext()) {
			id = c.getString(c.getColumnIndex("ID"));
		}
		c.close();
		return id;
	}

	public Cursor queryTheCursorCargoEmergencyID(String CargoName) {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT ID FROM or_dangerchemical where NAME='"
				+ CargoName + "'", null);
		return c;
	}

	// 获取货品名称
	public CargoEmergencyMethodDataEntity queryCargoEmergencyName(
			String CargoNameID) {

		Cursor c = queryTheCursorCargoEmergencyName(CargoNameID);
		CargoEmergencyMethodDataEntity cargo = new CargoEmergencyMethodDataEntity();
		while (c.moveToNext()) {
			cargo.setCargoname(c.getString(c.getColumnIndex("NAME")));
		}
		c.close();
		return cargo;
	}

	public Cursor queryTheCursorCargoEmergencyName(String CargoNameID) {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT NAME FROM or_dangerchemical where ID='"
				+ CargoNameID + "'", null);
		return c;
	}
	/**
	 * 获取基站信息
	 * 
	 * @return
	 */
	public ArrayList<BasestationDataEntity> queryBasestation() {

		Cursor c = queryTheCursorBasestation();
		ArrayList<BasestationDataEntity> bases = new ArrayList<BasestationDataEntity>();
		while (c.moveToNext()) {
			BasestationDataEntity base = new BasestationDataEntity();
			
			base.setName(c.getString(c.getColumnIndex("NAME")));
			LatLng l = new LatLng(c.getDouble(c.getColumnIndex("ALTITUDE")), c.getDouble(c.getColumnIndex("LATITUDE")));
			base.setIsbasestation(true);
			base.setLatlng(l);
			bases.add(base);
		}
		c.close();

		return bases;
	}

	public Cursor queryTheCursorBasestation() {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM or_basestation_fix", null);
		return c;
	}
	/**
	 * 
	 * 基站过车记录
	 * @return
	 */
	public ArrayList<BasestationPassCarRecordDataEntity> queryBasestationRecord(LatLng ll) {

		Cursor c = queryTheCursorBasestationRecord();
		ArrayList<BasestationPassCarRecordDataEntity> bases = new ArrayList<BasestationPassCarRecordDataEntity>();
		while (c.moveToNext()) {
			BasestationPassCarRecordDataEntity base = new BasestationPassCarRecordDataEntity();

			bases.add(base);
		}
		c.close();

		return bases;
	}

	public Cursor queryTheCursorBasestationRecord() {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM or_basestation_fix", null); ///gai!!!!!!!!!!!!!
		return c;
	}
	
	// 准行证详细信息

	public PermitCardDataEntity queryPermitCard(String PlateNumber) {

		Cursor c = queryTheCursorPermitCard(PlateNumber);
		PermitCardDataEntity permitcard = new PermitCardDataEntity();
		while (c.moveToNext()) {
			String forbid_run_time = c.getString(c
					.getColumnIndex("FORBID_RUN_TIME"));
			String a[] = forbid_run_time.split(",");
			String b = "";
			for (int i = 0; i < a.length; i++) {
				if ((i + 1) % 2 != 0) {
					b = b + a[i] + "-";
				} else {
					b = b + a[i] + ",\n";
				}
			}
			permitcard.setPlateNumber(c.getString(c
					.getColumnIndex("PLATE_NUMBER")));

			permitcard.setEndDate(c.getString(c.getColumnIndex("EXPIRE_DATE")));
			permitcard
					.setPermitInfo("准行证号："
							+ c.getString(c.getColumnIndex("CERT_NUMBER"))
							+ "\n"
							+ "准行车牌照："
							+ c.getString(c.getColumnIndex("PLATE_NUMBER"))
							+ "\n"
							+ "有效期："
							+ c.getString(c.getColumnIndex("EXPIRE_DATE"))
							+ "\n"
							+ "行驶路线："
							+ "\n"
							+ c.getString(c.getColumnIndex("PERMIT_LINES"))
							+ "\n"
							+ "禁行时间："
							+ "\n"
							+ b
							+ "准行类型:"
							+ queryType(c.getString(c
									.getColumnIndex("PERMIT_RUN_TYPE"))));
		}
		c.close();
		return permitcard;
	}

	public Cursor queryTheCursorPermitCard(String PlateNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM or_permit_run_cert where PLATE_NUMBER='"
						+ PlateNumber + "' COLLATE NOCASE", null);
		return c;
	}

	/**
	 * 

	 */
	public String queryType(String typenum) {
		String type = null;
		Cursor c = db.rawQuery(
				"SELECT NAME FROM sys_dictionaries where ZD_ID='" + typenum
						+ "'", null);
		while (c.moveToNext()) {
			type = c.getString(c.getColumnIndex("NAME"));
		}
		return type;

	}

	public String queryType(int typenum) {
		String type = null;
		Cursor c = db.rawQuery(
				"SELECT NAME FROM sys_dictionaries where ZD_ID='" + typenum
						+ "'", null);
		while (c.moveToNext()) {
			type = c.getString(c.getColumnIndex("NAME"));
		}
		return type;

	}

	// 车辆道路运输证

	public TransportCardDataEntity queryTransportCard(String PlateNumber) {

		Cursor c = queryTheCursorTransportCard(PlateNumber);
		TransportCardDataEntity transport = new TransportCardDataEntity();
		while (c.moveToNext()) {

			transport.setPlateNumber(c.getString(c
					.getColumnIndex("PLATE_NUMBER")));
			transport.setEndDate(c.getString(c.getColumnIndex("EXPIRE_DATE")));
			transport.setTransportInfo("\n" + "道路运输证号:"
					+ c.getString(c.getColumnIndex("CERT_NUMBER")) + "\n"
					+ "业主名称:" + c.getString(c.getColumnIndex("COMPANY")) + "\n"
					+ "地址:" + c.getString(c.getColumnIndex("ADDRESS")) + "\n"
					+ "车辆号码:" + c.getString(c.getColumnIndex("PLATE_NUMBER"))
					+ "\n" + "经营许可证号:"
					+ c.getString(c.getColumnIndex("BUS_LICENSE_NUMBER"))
					+ "\n" + "经济类型:"
					+ c.getString(c.getColumnIndex("ECONOMY_TYPE")) + "\n"
					+ "车辆类型:" + c.getString(c.getColumnIndex("CAR_TYPE"))
					+ "\n" + "顿（座）位:"
					+ c.getInt(c.getColumnIndex("MAX_LOAD_WEIGHT")) + "\n"
					+ "车辆尺寸：长:" + c.getInt(c.getColumnIndex("CAR_SIZE_L"))
					+ "\n" + "车辆尺寸：宽"
					+ c.getInt(c.getColumnIndex("CAR_SIZE_W")) + "\n"
					+ "车辆尺寸：高:" + c.getInt(c.getColumnIndex("CAR_SIZE_H"))
					+ "\n" + "经营范围:"
					+ c.getString(c.getColumnIndex("BUSSINESS_SCOPE")) + "\n"
					+ "发证机关:" + c.getString(c.getColumnIndex("ISSUE_ORG"))
					+ "\n" + "发证日期:"
					+ c.getString(c.getColumnIndex("ISSUE_DATE")) + "\n"
					+ "备注:" + c.getString(c.getColumnIndex("REMARK")) + "\n"
					+ "车辆唯一标示:" + c.getString(c.getColumnIndex("VIN")) + "\n"
					+ "有效期:" + c.getString(c.getColumnIndex("EXPIRE_DATE"))
					+ "\n" + "\n");

		}
		c.close();
		return transport;
	}

	public Cursor queryTheCursorTransportCard(String PlateNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM or_transport_cert where PLATE_NUMBER='"
						+ PlateNumber + "' COLLATE NOCASE", null);

		return c;
	}

	// 车辆保险证

	public InsuranceCardDataEntity queryInsuranceCard(String PlateNumber) {

		Cursor c = queryTheCursorInsuranceCard(PlateNumber);
		InsuranceCardDataEntity insurance = new InsuranceCardDataEntity();
		while (c.moveToNext()) {
			String endTime = c.getString(c.getColumnIndex("INSURANCE_DATE"));
			if (StringUtils.isNotBlank(endTime)) {
				String a[] = endTime.split(",");
				endTime = a[1];
			}
			insurance
					.setPlateNumber(c.getString(c.getColumnIndex("CAR_NUMBER")));
			insurance
					.setEndDate(c.getString(c.getColumnIndex("INSURANCE_DATE")));
			insurance.setInsuranceInfo("\n" + "保单号："
					+ c.getString(c.getColumnIndex("INSURANCE_NUMBER")) + "\n"
					+ "保险公司："
					+ c.getString(c.getColumnIndex("NSURANCE_COMPANY")) + "\n"
					+ "被保险人名称:"
					+ c.getString(c.getColumnIndex("INSURANCE_PEOPLE")) + "\n"
					+ "车牌号码：" + c.getString(c.getColumnIndex("CAR_NUMBER"))
					+ "\n" + "厂牌型号："
					+ c.getString(c.getColumnIndex("FACTORY_MODEL")) + "\n"
					+ "发动机号码：" + c.getString(c.getColumnIndex("ENGINE_NUMBER"))
					+ "\n" + "车架号：" + c.getString(c.getColumnIndex("VIN"))
					+ "\n" + "使用性质："
					+ queryType(c.getInt(c.getColumnIndex("EMPLOY_NATURE")))
					+ "\n" + "核定载客："
					+ c.getString(c.getColumnIndex("PASSENGERS_NUMBER")) + "\n"
					+ "指定驾驶人：" + c.getString(c.getColumnIndex("DRIVER")) + "\n"
					+ "行驶区域：" + c.getString(c.getColumnIndex("TRAVEL_AREA"))
					+ "\n" + "到期时间：" + endTime + "\n" + "承保险别："
					+ c.getString(c.getColumnIndex("INSURANCE_GRADE")) + "\n"
					+ "创建人：" + c.getInt(c.getColumnIndex("CREATOR")) + "\n"
					+ "创建时间：" + c.getString(c.getColumnIndex("CREATE_TIME"))
					+ "\n" + "\n");

		}
		c.close();
		return insurance;
	}

	public Cursor queryTheCursorInsuranceCard(String PlateNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM or_car_insurance where CAR_NUMBER='"
						+ PlateNumber + "' COLLATE NOCASE", null);
		return c;
	}

	// 货物保险证

	public InsuranceCardDataEntity queryInsuranceCard_Cargo(String PlateNumber) {

		Cursor c = queryTheCursorInsuranceCard_Cargo(PlateNumber);
		InsuranceCardDataEntity insurance = new InsuranceCardDataEntity();
		while (c.moveToNext()) {
			String endTime = c.getString(c.getColumnIndex("INSURANCE_TIME"));
			if (StringUtils.isNotBlank(endTime)) {
				String a[] = endTime.split(",");
				endTime = a[1];
			}
			insurance.setPlateNumber(c.getString(c
					.getColumnIndex("LICENSE_NUMBER")));
			// insurance
			// .setEndDate(c.getString(c.getColumnIndex("INSURANCE_TIME")));
			insurance.setInsuranceInfo("\n" + "牌照号："
					+ c.getString(c.getColumnIndex("LICENSE_NUMBER"))
					+ "\n"
					+ "发动机号码："
					+ c.getString(c.getColumnIndex("ENGINE_NUMBER"))
					+ "\n"
					+ "签单日期："
					+ c.getString(c.getColumnIndex("SIGN_DATE"))
					+ "\n"
					+ "邮编："
					+ c.getString(c.getColumnIndex("POSTCODE"))
					+ "\n"
					+ "被保险人："
					+ c.getString(c.getColumnIndex("INSURED_PERSON"))
					+ "\n"
					+ "联系电话："
					+ c.getString(c.getColumnIndex("TELEPHONE_NUMBER"))
					+ "\n"
					+ "保险公司名称："
					+ c.getString(c.getColumnIndex("INSURANCE_COMPANY"))
					+ "\n"
					+ "核保："
					+ c.getString(c.getColumnIndex("UNDERWRITING"))
					+ "\n"
					+ "制单："
					+ c.getString(c.getColumnIndex("PREPARE_DOCUMENT"))
					+ "\n"
					+ "总保险费："
					+ c.getDouble(c.getColumnIndex("PREMIUM_FEE"))
					+ "\n"
					+ "保险合同争议解决方式："
					+ queryType(c.getInt(c
							.getColumnIndex("INSURANCE_SOLVE_WAY"))) + "\n"
					+ "验真码："
					+ c.getString(c.getColumnIndex("VERIFICATION_CODE")) + "\n"
					+ "营业地址："
					+ c.getString(c.getColumnIndex("BUSINESS_ADDRESS")) + "\n"
					+ "到期时间：" + endTime + "\n" + "保单号："
					+ c.getString(c.getColumnIndex("WARRANTY_NUMBER")) + "\n"
					+ "全国统一客服电话："
					+ c.getString(c.getColumnIndex("SERVICE_TEL")) + "\n"
					+ "危险物名称：" + c.getString(c.getColumnIndex("DANGER_NAME"))
					+ "\n" + "保险人联系地址："
					+ c.getString(c.getColumnIndex("INSURER_ADDRESS")) + "\n"
					+ "危险车辆数：" + c.getInt(c.getColumnIndex("RISK_CAR_COUNT"))
					+ "\n" + "承保险别：" + c.getString(c.getColumnIndex("VIN"))
					+ "\n" + "传真：" + c.getString(c.getColumnIndex("FAX"))
					+ "\n" + "危险货物运输地域范围："
					+ c.getString(c.getColumnIndex("TRANSMISSION_RANGE"))
					+ "\n" + "经办：" + c.getString(c.getColumnIndex("HANDLE"))
					+ "\n" + "创建人：" + c.getInt(c.getColumnIndex("CREATOR"))
					+ "\n" + "创建时间："
					+ c.getString(c.getColumnIndex("CREATE_TIME")) + "\n"
					+ "\n");
		}
		c.close();
		return insurance;
	}

	public Cursor queryTheCursorInsuranceCard_Cargo(String PlateNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM or_carrier_warranty where LICENSE_NUMBER='"
						+ PlateNumber + "' COLLATE NOCASE", null);
		return c;
	}

	// 特种设备使用登记

	public SpecialEquipUsageDataEntity querySpecialEquipUsage(String PlateNumber) {

		Cursor c = queryTheCursorSpecialEquipUsage(PlateNumber);
		SpecialEquipUsageDataEntity specialequipusage = new SpecialEquipUsageDataEntity();
		while (c.moveToNext()) {

			specialequipusage.setPlateNumber(c.getString(c
					.getColumnIndex("CAR_NUMBER")));
			specialequipusage.setEndDate(c.getString(c
					.getColumnIndex("NEXT_CHECKOUT_DATE")));
			specialequipusage.setSpecialequipmentInfo("使用登记证编号:"
					+ c.getString(c.getColumnIndex("NUMBER_REGISTER")) + "\n"
					+ "单位内编号:" + c.getString(c.getColumnIndex("NUMBER")) + "\n"
					+ "使用单位:" + c.getString(c.getColumnIndex("COMPANY")) + "\n"
					+ "注册代码:" + c.getString(c.getColumnIndex("REGISTER_CODE"))
					+ "\n" + "压力容器名称:"
					+ c.getString(c.getColumnIndex("CONTAINER_NAME")) + "\n"
					+ "制造单位："
					+ c.getString(c.getColumnIndex("MANUFACTURING_COMPANY"))
					+ "\n" + "制造许可证号:"
					+ c.getString(c.getColumnIndex("MANUFACTURING_NUMBER"))
					+ "\n" + "充装介质:"
					+ c.getString(c.getColumnIndex("TANK_INTRUDUTION")) + "\n"
					+ "启用日期:" + c.getString(c.getColumnIndex("START_DATE"))
					+ "\n" + "发证日期:"
					+ c.getString(c.getColumnIndex("CERTIFICATE_DATE")) + "\n"
					+ "发证机构:"
					+ c.getString(c.getColumnIndex("CERTIFICATE_COMPANY"))
					+ "\n" + "下次检验日期:"
					+ c.getString(c.getColumnIndex("NEXT_CHECKOUT_DATE"))
					+ "\n" + "车牌号码:"
					+ c.getString(c.getColumnIndex("CAR_NUMBER")) + "\n"
					+ "罐式集装箱联合国危险品编号:"
					+ c.getString(c.getColumnIndex("TANK_CONTAINER_NUMBER"))
					+ "\n" + "型号:" + c.getString(c.getColumnIndex("VERSION"))
					+ "\n" + "结构形式:"
					+ c.getString(c.getColumnIndex("TACTIC_FORM")) + "\n"
					+ "装卸方式:"
					+ queryType(c.getInt(c.getColumnIndex("HANDLING_MODE")))
					+ "\n" + "保温方式:"
					+ queryType(c.getInt(c.getColumnIndex("HEAT_MODE"))) + "\n"
					+ "底盘(车架)号码:"
					+ c.getString(c.getColumnIndex("BATHOLITH_NUMBER")) + "\n"
					+ "安全阀型号:"
					+ c.getString(c.getColumnIndex("SAFETY_VALVE_TYPE")) + "\n"
					+ "爆破片型号:"
					+ c.getString(c.getColumnIndex("RUPTURE_DISK_TYPE")) + "\n"
					+ "紧急切断阀数量："
					+ c.getString(c.getColumnIndex("URGENCY_VALVE_TYPE"))
					+ "\n" + "液面计数量:"
					+ c.getString(c.getColumnIndex("LIQUID_LEVEL_TYPE")) + "\n"
					+ "罐体外筒材料："
					+ c.getString(c.getColumnIndex("TANK_OUTER_MATERIAL"))
					+ "\n" + "罐体内筒材料:"
					+ c.getString(c.getColumnIndex("TANK_INNER_MATERIAL"))
					+ "\n" + "充装系数:"
					+ c.getString(c.getColumnIndex("FILLING_RATIO")) + "\n"
					+ "充装介质:" + c.getString(c.getColumnIndex("INTRUDUTION"))
					+ "\n" + "罐体颜色:"
					+ queryType(c.getInt(c.getColumnIndex("TANK_COLOR")))
					+ "\n" + "创建人:" + c.getInt(c.getColumnIndex("CREATOR"))
					+ "\n" + "创建时间:"
					+ c.getString(c.getColumnIndex("CREATE_TIME")) + "\n"
					+ "车辆唯一标示:" + c.getString(c.getColumnIndex("VIN")));

		}
		c.close();
		return specialequipusage;
	}

	public Cursor queryTheCursorSpecialEquipUsage(String PlateNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM or_special_equipment where CAR_NUMBER='"
						+ PlateNumber + "' COLLATE NOCASE", null);
		return c;
	}

	// 行驶证

	// 从业资格证
	public JobCardDataEntity queryJobCard(String certNumber) {

		Cursor c = queryTheCursorJobCard(certNumber);
		JobCardDataEntity jobcard = new JobCardDataEntity();
		while (c.moveToNext()) {

			String cert_type = c.getString(c.getColumnIndex("CERT_TYPE"));
			String a[] = cert_type.split(",");
			cert_type = "";
			for (int i = 0; i < a.length; i++) {
				cert_type = queryType(a[i]) + "," + cert_type;
			}

			jobcard.setCertNumber(c.getString(c.getColumnIndex("CERT_NUMBER")));
			jobcard.setEndDate(c.getString(c.getColumnIndex("EXPIRE_DATE")));
			jobcard.setJobInfo("姓名：" + c.getString(c.getColumnIndex("NAME"))
					+ "\n" + "从业资格证号码：" + "\n"
					+ c.getString(c.getColumnIndex("CERT_NUMBER")) + "\n"
					+ "身份证号：" + "\n"
					+ c.getString(c.getColumnIndex("ID_NUMBER")) + "\n" + "住址："
					+ c.getString(c.getColumnIndex("ADDRESS")) + "\n"
					+ "从业资格证类别:" + cert_type + "\n"  + "创建时间："
					+ c.getString(c.getColumnIndex("CREATE_TIME")) + "\n"
					+ "发证机关："
					+ queryType(c.getInt(c.getColumnIndex("ISSUE_ORG"))) + "\n"
					+ "有效期：" + c.getString(c.getColumnIndex("EXPIRE_DATE")));

		}
		c.close();
		return jobcard;
	}

	public Cursor queryTheCursorJobCard(String certNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM or_job_cert where CERT_NUMBER='"
				+ certNumber + "' COLLATE NOCASE", null);
		return c;
	}

	// 驾驶证

	public DriverLicenseCardDataEntity queryDriverLicenseCard(
			String driverLicenseNumber) {

		Cursor c = queryTheCursorDriverLicenseCard(driverLicenseNumber);
		DriverLicenseCardDataEntity driverlicensecard = new DriverLicenseCardDataEntity();
		while (c.moveToNext()) {
			int sex = 0;
			sex = c.getInt(c.getColumnIndex("SEX"));
			String sexname = "";
			if (sex == 0) {
				sexname = "男";
			} else {
				sexname = "女";
			}
			driverlicensecard.setDriverLicenseNumber(c.getString(c
					.getColumnIndex("DRIVER_LICENSE_NUMBER")));
			driverlicensecard.setDrivername(c.getString(c
					.getColumnIndex("NAME")));
			driverlicensecard.setEndDate(c.getString(c
					.getColumnIndex("EXPIRE_DATE")));
			driverlicensecard.setDriverInfo("驾照号码:" + "\n"
					+ c.getString(c.getColumnIndex("DRIVER_LICENSE_NUMBER"))
					+ "\n"
					+ "姓名:"
					+ c.getString(c.getColumnIndex("NAME"))
					+ "\n"
					+ "性别:"
					+ sexname
					+ "\n"
					+ "国籍:"
					+ queryType(c.getString(c.getColumnIndex("NATIONALITY")))
					+ "\n"
					+ "初次领证日期:"
					+ c.getString(c.getColumnIndex("FIRST_OBTAIN_DATE"))
					+ "\n"
					+ "驾照类型:"
					+ queryType(c.getString(c
							.getColumnIndex("DRIVER_LICENSE_TYPE"))) + "\n"
					+ "状态:" + c.getInt(c.getColumnIndex("STATUS")) + "\n"
					
					+ "创建时间:" + c.getString(c.getColumnIndex("CREATE_TIME"))
					+ "\n" + "有效期限:"
					+ c.getString(c.getColumnIndex("EXPIRE_DATE")) + "\n"
					+ "档案编号:" + c.getString(c.getColumnIndex("CERT_CODE")));

		}
		c.close();
		return driverlicensecard;
	}

	public Cursor queryTheCursorDriverLicenseCard(String driverLicenseNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM or_driver where DRIVER_LICENSE_NUMBER='"
						+ driverLicenseNumber + "' COLLATE NOCASE", null);
		return c;
	}

	// 用户信息表
	public void addUserInformation(UserInformationDataEntity userinformation) {
		db.execSQL(
				"INSERT INTO UserInformation VALUES(null, ?, ?)",
				new Object[] {
						// userinformation.getUserId(),
						userinformation.getUsername(),
						userinformation.getPassword()

				});
	}

	public void updateUserInformation(UserInformationDataEntity userinformation) {
		db.execSQL("UPDATE UserInformation " + "SET password = '"
				+ userinformation.getPassword() + "' " + "WHERE username = '"
				+ userinformation.getUsername() + "' ");
	}

	public UserInformationDataEntity queryUserInformation(String username) {

		Cursor c = queryTheCursorUserInformation(username);
		UserInformationDataEntity userinformation = new UserInformationDataEntity();
		while (c.moveToNext()) {
			userinformation.setUserId(c.getInt(c.getColumnIndex("userID")));
			userinformation.setUsername(c.getString(c
					.getColumnIndex("username")));
			userinformation.setPassword(c.getString(c
					.getColumnIndex("password")));

		}
		c.close();
		return userinformation;
	}

	public Cursor queryTheCursorUserInformation(String userName) {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM UserInformation where username='"
				+ userName + "'", null);
		return c;
	}

	/**
	 * close database
	 */
	// 最新路查记录表

	public LatestTransportRecordDataEntity queryLatestTransportRecord(
			String plateNumber) {

		Cursor c = queryTheCursorLatestTransportRecord(plateNumber);
		LatestTransportRecordDataEntity latestransportrecord = new LatestTransportRecordDataEntity();
		while (c.moveToNext()) {

			latestransportrecord.setPlateNumber(c.getString(c
					.getColumnIndex("PLATE_NUMBER")));
			latestransportrecord.setCargoname(c.getString(c
					.getColumnIndex("CARGO_NAME")));

			latestransportrecord
					.setWeight(c.getInt(c.getColumnIndex("WEIGHT")));
			latestransportrecord.setPlateNumber_gua(c.getString(c
					.getColumnIndex("TAIL_PLATE_NUMBER")));
			latestransportrecord.setTransportrecordInfo("准行证号:"
					+ c.getString(c.getColumnIndex("PERMIT_NUMBER"))
					+
					// "\n"+
					// ":"+
					// object.getString("EST_TIME")+
					"\n" + "基站ID:"
					+ c.getString(c.getColumnIndex("BASESTATION_ID")) + "\n"
					+ "货物名称:" + c.getString(c.getColumnIndex("CARGO_NAME"))
					+ "\n" + ":"
					+ c.getString(c.getColumnIndex("REUPLOAD_TIME")) + "\n"
					+ "操作员:" + c.getString(c.getColumnIndex("OPERATOR")) + "\n"
					+ "是否写过卡:"
					+ c.getString(c.getColumnIndex("WRITE_CARD_STATUS"))

					+ "\n" + "道路运输证号:"
					+ c.getString(c.getColumnIndex("TRANSPORT_NUMBER")) + "\n"
					+ "司机ID:" + c.getString(c.getColumnIndex("DRIVER_ID"))
					+ "\n" + "上传时间:"
					+ c.getString(c.getColumnIndex("UPLOAD_TIME")) + "\n"
					+ "创建时间:" + c.getString(c.getColumnIndex("CREATE_TIME"))
					+ "\n" + "总重量:" + c.getInt(c.getColumnIndex("WEIGHT"))
					+ "\n" + "挂车号码:"
					+ c.getString(c.getColumnIndex("TAIL_PLATE_NUMBER")) + "\n"
					+ "车牌号码:" + c.getString(c.getColumnIndex("PLATE_NUMBER"))
					+ "\n" + ":"
					+ c.getString(c.getColumnIndex("SUPERCARGO_ID")) + "\n"
					+ "公司ID:" + c.getInt(c.getColumnIndex("COMPANY_ID")));
		}
		c.close();
		return latestransportrecord;
	}

	public Cursor queryTheCursorLatestTransportRecord(String plateNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT * FROM ae_transportrecord where PLATE_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		return c;
	}

	// 车辆基本信息表

	public CarBasicInfoDataEntity queryTheCursorCarBasicInfo(
			String plateNumber, String plateNumber_gua) {
		// System.out.println("query database");
		DateFormat newdate = new DateFormat();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime1 = null;
		try {
			dateTime1 = dateFormat.parse(newdate.getDate3());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CarBasicInfoDataEntity carbasicinfo = new CarBasicInfoDataEntity();
		Cursor maxweight_c = db.rawQuery(
				"SELECT SUM_QUALITY FROM or_car where PLATE_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		Cursor traction_quality_c = db.rawQuery(
				"SELECT TRACTION_QUALITY FROM or_car where PLATE_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		Cursor check_quality_c = db.rawQuery(
				"SELECT CHECK_QUALITY FROM or_car where PLATE_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		Cursor check_quality_tail_c = db.rawQuery(
				"SELECT CHECK_QUALITY FROM or_car where PLATE_NUMBER='"
						+ plateNumber_gua + "' COLLATE NOCASE", null);
		Cursor maxweight_tail_c = db.rawQuery(
				"SELECT SUM_QUALITY FROM or_car where PLATE_NUMBER='"
						+ plateNumber_gua + "' COLLATE NOCASE", null);
		Cursor reorgannize_quality_c = db.rawQuery(
				"SELECT REORGANIZE_QUALITY FROM or_car where PLATE_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		Cursor permitcard_c = db.rawQuery(
				"SELECT EXPIRE_DATE FROM or_permit_run_cert where PLATE_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		Cursor transportCardHead_c = db.rawQuery(
				"SELECT EXPIRE_DATE FROM or_transport_cert where PLATE_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		Cursor transportCardTail_c = db.rawQuery(
				"SELECT EXPIRE_DATE FROM or_transport_cert where PLATE_NUMBER='"
						+ plateNumber_gua + "' COLLATE NOCASE", null);
		Cursor insuranceHead_c = db.rawQuery(
				"SELECT INSURANCE_DATE FROM or_car_insurance where CAR_NUMBER='"
						+ plateNumber + "' COLLATE NOCASE", null);
		Cursor insuranceTail_c = db.rawQuery(
				"SELECT INSURANCE_DATE FROM or_car_insurance where CAR_NUMBER='"
						+ plateNumber_gua + "' COLLATE NOCASE", null);
		Cursor insuranceCargo_c = db.rawQuery(
				"SELECT INSURANCE_TIME FROM or_carrier_warranty where LICENSE_NUMBER='"
						+ plateNumber_gua + "' COLLATE NOCASE", null);
		Cursor specialequipusage_c = db.rawQuery(
				"SELECT NEXT_CHECKOUT_DATE FROM or_special_equipment where CAR_NUMBER='"
						+ plateNumber_gua + "' COLLATE NOCASE", null);
		carbasicinfo.setMaxweight(0);
		carbasicinfo.setTraction_quality(0);
		carbasicinfo.setCheck_quality(0);
		carbasicinfo.setReorganize_quality(0);
		carbasicinfo.setCheck_quality_tail(0);
		carbasicinfo.setMaxweight_tail(0);
		carbasicinfo.setStatus4InsuranceCertCargo(-1);
		carbasicinfo.setStatus4InsuranceCertHead(-1);
		carbasicinfo.setStatus4InsuranceCertTail(-1);
		carbasicinfo.setStatus4PermitRunCert(-1);
		carbasicinfo.setStatus4SEquipCheck(-1);
		carbasicinfo.setStatus4TransportCertHead(-1);
		carbasicinfo.setStatus4TransportCertTail(-1);
		while (maxweight_c.moveToNext()) {
			double a = maxweight_c.getDouble(maxweight_c
					.getColumnIndex("SUM_QUALITY"));
			carbasicinfo.setMaxweight(a);
		}
		maxweight_c.close();

		while (maxweight_tail_c.moveToNext()) {
			double a = maxweight_tail_c.getDouble(maxweight_tail_c
					.getColumnIndex("SUM_QUALITY"));
			carbasicinfo.setMaxweight_tail(a);
		}
		maxweight_tail_c.close();

		while (check_quality_tail_c.moveToNext()) {
			double a = check_quality_tail_c.getDouble(check_quality_tail_c
					.getColumnIndex("CHECK_QUALITY"));
			carbasicinfo.setCheck_quality_tail(a);
		}
		check_quality_tail_c.close();

		while (traction_quality_c.moveToNext()) {
			double a = traction_quality_c.getDouble(traction_quality_c
					.getColumnIndex("TRACTION_QUALITY"));
			carbasicinfo.setTraction_quality(a);
		}
		traction_quality_c.close();
		while (check_quality_c.moveToNext()) {
			double a = check_quality_c.getDouble(check_quality_c
					.getColumnIndex("CHECK_QUALITY"));
			carbasicinfo.setCheck_quality(a);
		}
		check_quality_c.close();
		while (reorgannize_quality_c.moveToNext()) {
			double a = reorgannize_quality_c.getDouble(reorgannize_quality_c
					.getColumnIndex("REORGANIZE_QUALITY"));
			carbasicinfo.setReorganize_quality(a);
		}
		reorgannize_quality_c.close();

		while (permitcard_c.moveToNext()) {
			String endTime = permitcard_c.getString(permitcard_c
					.getColumnIndex("EXPIRE_DATE"));
			if (!StringUtils.isNotBlank(endTime)) {
				carbasicinfo.setStatus4PermitRunCert(-1);
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					carbasicinfo.setStatus4PermitRunCert(1);
				} else {
					carbasicinfo.setStatus4PermitRunCert(0);
				}
			}

		}
		permitcard_c.close();
		while (transportCardHead_c.moveToNext()) {
			String endTime = transportCardHead_c.getString(transportCardHead_c
					.getColumnIndex("EXPIRE_DATE"));
			if (!StringUtils.isNotBlank(endTime)) {
				carbasicinfo.setStatus4TransportCertHead(-1);
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					carbasicinfo.setStatus4TransportCertHead(1);
				} else {
					carbasicinfo.setStatus4TransportCertHead(0);
				}
			}
		}
		transportCardHead_c.close();
		while (transportCardTail_c.moveToNext()) {
			String endTime = transportCardTail_c.getString(transportCardTail_c
					.getColumnIndex("EXPIRE_DATE"));
			if (!StringUtils.isNotBlank(endTime)) {
				carbasicinfo.setStatus4TransportCertTail(-1);
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					carbasicinfo.setStatus4TransportCertTail(1);
				} else {
					carbasicinfo.setStatus4TransportCertTail(0);
				}
			}
		}
		transportCardTail_c.close();
		while (insuranceHead_c.moveToNext()) {
			String endTime = insuranceHead_c.getString(insuranceHead_c
					.getColumnIndex("INSURANCE_DATE"));
			if (StringUtils.isNotBlank(endTime)) {
				String a[] = endTime.split(",");
				endTime = a[1];
			}
			if (!StringUtils.isNotBlank(endTime)) {
				carbasicinfo.setStatus4InsuranceCertHead(-1);
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					carbasicinfo.setStatus4InsuranceCertHead(1);
				} else {
					carbasicinfo.setStatus4InsuranceCertHead(0);
				}
			}
		}
		insuranceHead_c.close();
		while (insuranceTail_c.moveToNext()) {
			String endTime = insuranceTail_c.getString(insuranceTail_c
					.getColumnIndex("INSURANCE_DATE"));
			if (StringUtils.isNotBlank(endTime)) {
				String a[] = endTime.split(",");
				endTime = a[1];
			}
			if (!StringUtils.isNotBlank(endTime)) {
				carbasicinfo.setStatus4InsuranceCertTail(-1);
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					carbasicinfo.setStatus4InsuranceCertTail(1);
				} else {
					carbasicinfo.setStatus4InsuranceCertTail(0);
				}
			}
		}
		insuranceTail_c.close();
		while (insuranceCargo_c.moveToNext()) {
			String endTime = insuranceCargo_c.getString(insuranceCargo_c
					.getColumnIndex("INSURANCE_TIME"));

			if (StringUtils.isNotBlank(endTime)) {
				String a[] = endTime.split(",");
				endTime = a[1];
			}
			if (!StringUtils.isNotBlank(endTime)) {
				carbasicinfo.setStatus4InsuranceCertCargo(-1);
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					carbasicinfo.setStatus4InsuranceCertCargo(1);
				} else {
					carbasicinfo.setStatus4InsuranceCertCargo(0);
				}
			}
		}
		insuranceCargo_c.close();
		while (specialequipusage_c.moveToNext()) {
			String endTime = specialequipusage_c.getString(specialequipusage_c
					.getColumnIndex("NEXT_CHECKOUT_DATE"));
			if (!StringUtils.isNotBlank(endTime)) {
				carbasicinfo.setStatus4SEquipCheck(-1);
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					carbasicinfo.setStatus4SEquipCheck(1);
				} else {
					carbasicinfo.setStatus4SEquipCheck(0);
				}
			}
		}
		specialequipusage_c.close();

		return carbasicinfo;
	}

	// 驾驶证信息状态

	public DriverCertStatusDataEntity queryDrivercertstatus(
			String driverLicenseNumber) {

		DateFormat newdate = new DateFormat();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime1 = null;
		try {
			dateTime1 = dateFormat.parse(newdate.getDate3());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Cursor c = queryTheCursorDriverCertStatus(driverLicenseNumber);
		DriverCertStatusDataEntity drivercertstatus = new DriverCertStatusDataEntity();

		while (c.moveToNext()) {
			String endTime = c.getString(c.getColumnIndex("EXPIRE_DATE"));
			if (endTime.equals("")) {
				drivercertstatus.setStatus("-1");
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					drivercertstatus.setStatus("1");
				} else {
					drivercertstatus.setStatus("0");
				}
			}
			drivercertstatus.setEndDate(endTime);
		}
		c.close();
		return drivercertstatus;
	}

	public Cursor queryTheCursorDriverCertStatus(String driverLicenseNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT EXPIRE_DATE FROM or_driver where DRIVER_LICENSE_NUMBER='"
						+ driverLicenseNumber + "'", null);
		return c;
	}

	// 从业资格证状态

	public JobCertStatusDataEntity queryJobCertStatus(String certNumber) {
		DateFormat newdate = new DateFormat();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime1 = null;
		try {
			dateTime1 = dateFormat.parse(newdate.getDate3());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Cursor c = queryTheCursorJobCertStatus(certNumber);
		JobCertStatusDataEntity jobcertstatus = new JobCertStatusDataEntity();
		while (c.moveToNext()) {
			String endTime = c.getString(c.getColumnIndex("EXPIRE_DATE"));
			if (endTime.equals("")) {
				jobcertstatus.setStatus("-1");
			} else {
				Date dateTime2 = null;
				try {
					dateTime2 = dateFormat.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = dateTime1.compareTo(dateTime2);
				if (i > 0) {
					jobcertstatus.setStatus("1");
				} else {
					jobcertstatus.setStatus("0");
				}
			}
			jobcertstatus.setEndDate(endTime);
		}
		c.close();
		return jobcertstatus;
	}

	public Cursor queryTheCursorJobCertStatus(String certNumber) {
		// System.out.println("query database");
		Cursor c = db.rawQuery(
				"SELECT EXPIRE_DATE FROM or_job_cert where CERT_NUMBER='"
						+ certNumber + "'", null);
		return c;
	}

	// 失败上传车辆记录数据库
	public void addCarsFailUpload(CarsFailUploadDataEntity carsfailupload) {
		// System.out.println("add database");
		db.execSQL(
				"INSERT INTO CarsFailUpload VALUES(null, ?, ? ,? ,? ,? ,? ,? , ?,?,?,?)",
				new Object[] { carsfailupload.getUserID(),
						carsfailupload.getBaseStationID(),
						carsfailupload.getIsOverWeight(),
						carsfailupload.getStatus4PermitRunCert(),
						carsfailupload.getStatus4TransportCert_Head(),
						carsfailupload.getStatus4TransportCert_Tail(),
						carsfailupload.getStatus4InsuranceCert_Head(),
						carsfailupload.getStatus4InsuranceCert_Cargo(),
						carsfailupload.getStatus4InsuranceCert_Tail(),
						carsfailupload.getStatus4SpecialEquipUsage(),
						carsfailupload.getPlateNumber()

				});
	}

	public CarsFailUploadDataEntity queryCarsFailUpload(String ID) {

		Cursor c = queryTheCursorCarsFailUpload();
		CarsFailUploadDataEntity carsfailupload = new CarsFailUploadDataEntity();
		while (c.moveToNext()) {

			carsfailupload.setUserID(c.getString(c.getColumnIndex("userID")));
			carsfailupload.setBaseStationID(c.getString(c
					.getColumnIndex("baseStationID")));
			carsfailupload.setIsOverWeight(c.getString(c
					.getColumnIndex("isOverWeight")));
			carsfailupload.setStatus4PermitRunCert(c.getString(c
					.getColumnIndex("status4PermitRunCert")));
			carsfailupload.setStatus4TransportCert_Head(c.getString(c
					.getColumnIndex("status4TransportCert_Head")));
			carsfailupload.setStatus4TransportCert_Tail(c.getString(c
					.getColumnIndex("status4TransportCert_Tail")));
			carsfailupload.setStatus4InsuranceCert_Head(c.getString(c
					.getColumnIndex("status4InsuranceCert_Head")));
			carsfailupload.setStatus4InsuranceCert_Cargo(c.getString(c
					.getColumnIndex("status4InsuranceCert_Cargo")));
			carsfailupload.setStatus4InsuranceCert_Tail(c.getString(c
					.getColumnIndex("status4InsuranceCert_Tail")));
			carsfailupload.setStatus4SpecialEquipUsage(c.getString(c
					.getColumnIndex("status4SpecialEquipUsage")));
			carsfailupload.setPlateNumber(c.getString(c
					.getColumnIndex("plateNumber")));

		}
		c.close();
		return carsfailupload;
	}

	public void DeleteCarsFailUpload(Integer id) {
		db.execSQL("DELETE FROM CarsFailUpload where ID=" + id);
	}

	public Cursor queryTheCursorCarsFailUpload() {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM CarsFailUpload", null);
		return c;
	}

	// 失败上传人员记录数据库
	public void addPeopleFailUpload(PeopleFailUploadDataEntity peoplefailupload) {
		// System.out.println("add database");
		db.execSQL(
				"INSERT INTO PeopleFailUpload VALUES(null, ?, ? ,? ,? ,?)",
				new Object[] { peoplefailupload.getUserID(),
						peoplefailupload.getBaseStationID(),
						peoplefailupload.getStatus4DriverLicense(),
						peoplefailupload.getStatus4JobCert(),
						peoplefailupload.getDriverName() });
	}

	public PeopleFailUploadDataEntity queryPeopleFailUpload(String ID) {

		Cursor c = queryTheCursorPeopleFailUpload();
		PeopleFailUploadDataEntity peoplefailupload = new PeopleFailUploadDataEntity();
		while (c.moveToNext()) {

			peoplefailupload.setUserID(c.getString(c.getColumnIndex("userID")));
			peoplefailupload.setBaseStationID(c.getString(c
					.getColumnIndex("baseStationID")));
			peoplefailupload.setStatus4DriverLicense(c.getString(c
					.getColumnIndex("status4DriverLicense")));
			peoplefailupload.setStatus4JobCert(c.getString(c
					.getColumnIndex("status4JobCert")));
			peoplefailupload.setDriverName(c.getString(c
					.getColumnIndex("driverName")));
		}
		c.close();
		return peoplefailupload;
	}

	public void DeletePeopleFailUpload(Integer id) {
		db.execSQL("DELETE FROM PeopleFailUpload where ID=" + id);
	}

	public Cursor queryTheCursorPeopleFailUpload() {
		// System.out.println("query database");
		Cursor c = db.rawQuery("SELECT * FROM PeopleFailUpload", null);
		return c;
	}

	// 数据版本信息
	// 上传任务管理
	public void addDataVersion(DataVersionInfo version) {
		System.out.println("add database");
		db.execSQL("INSERT INTO DataVersion VALUES(null, ?)", new Object[] {

		version.getDataVersionCode() });
	}

	public int queryDataVersion() {
		Cursor c = queryTheCursorDataVersion();
		int version = 1;
		while (c.moveToNext()) {
			version = c.getInt(c.getColumnIndex("DataVersionCode"));
		}
		c.close();
		return version;
	}

	public Cursor queryTheCursorDataVersion() {
		// System.out.println("query database");
		// int listitemID1 = listitemID+1;
		// int listitemID2 = listitemID+10;
		Cursor c = db.rawQuery(
				"SELECT * FROM DataVersion order by ID desc limit 1", null);
		return c;
	}

	// 整体更新数据库
	public void dosql(String[] sqlss) {

		for (int i = 1; i < sqlss.length; i++) {
			try {
				db.execSQL(sqlss[i]);
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

	}
	/**
	 * 通过tagcode 查询 platenumber、
	 */
	public String transPlateNumber(String tagcode)
	{
		String platenumber = null;
		Cursor c = queryTheCursorPlateNumber(tagcode);
		while (c.moveToNext()) {
			platenumber = c.getString(c.getColumnIndex("PLATE_NUMBER"));
		}
		c.close();
		return platenumber;
	}
	public Cursor queryTheCursorPlateNumber(String tagcode) {

		Cursor c = db.rawQuery(
				"SELECT PLATE_NUMBER FROM or_car where TAG_CODE='"+tagcode+"' COLLATE NOCASE", null);
		return c;
	}
	
	public void closeDB() {
		db.close();
	}

}
