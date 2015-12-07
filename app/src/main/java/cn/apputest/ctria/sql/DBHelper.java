package cn.apputest.ctria.sql;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

/**
 * 
 * @author Shihao Shen 数据库 创建更新类
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	String sqls;
	private static int VERSON = 1;// 默认的数据库版本
	private SharedPreferences preferences;

	// 继承SQLiteOpenHelper类的类必须有自己的构造函数
	// 该构造函数4个参数，直接调用父类的构造函数。其中第一个参数为该类本身；第二个参数为数据库的名字；
	// 第3个参数是用来设置游标对象的，这里一般设置为null；参数四是数据库的版本号。
	public DBHelper(Context context, String name, CursorFactory factory,
			int verson) {
		super(context, name, factory, verson);
	}

	// 该构造函数有3个参数，因为它把上面函数的第3个参数固定为null了
	public DBHelper(Context context, String name, int verson) {
		this(context, name, null, verson);
	}

	// 该构造函数只有2个参数，在上面函数 的基础山将版本号固定了
	public DBHelper(Context context, String name) {
		this(context, name, VERSON);
	}

	// 该函数在数据库第一次被建立时调用
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("CREATE TABLE `ae_transportrecord`"
				+ " (  `ID` INTEGER  PRIMARY KEY AUTOINCREMENT, "
				+ " `PLATE_NUMBER` char(7)  DEFAULT '',  "
				+ "`PERMIT_NUMBER` char(11) DEFAULT NULL, "
				+ " `TRANSPORT_NUMBER` char(11) DEFAULT NULL, "
				+ " `TAIL_PLATE_NUMBER` char(11) DEFAULT NULL, "
				+ " `CARGO_NAME` varchar(20) DEFAULT NULL,"
				+ "  `WEIGHT` int(11) DEFAULT NULL, "
				+ " `EST_TIME` int(11) DEFAULT NULL, "
				+ " `DRIVER_ID` char(18) DEFAULT NULL, "
				+ " `SUPERCARGO_ID` char(18) DEFAULT NULL, "
				+ " `OPERATOR` char(11) DEFAULT NULL, "
				+ " `UPLOAD_TIME` date , "
				+ " `WRITE_CARD_STATUS` int(11) DEFAULT NULL, "
				+ " `COMPANY_ID` int(10) , "
				+ " `BASESTATION_ID` varchar(20) DEFAULT NULL, "
				+ " `REUPLOAD_TIME` date DEFAULT NULL, "
				+ " `VIN` varchar(20) DEFAULT NULL, "
				+ " `CREATE_TIME` datetime DEFAULT NULL, "
				+ " `IS_DEL` int(1) DEFAULT NULL)");
		arg0.execSQL("CREATE TABLE `or_car`"
				+ " (  `ID` char(32)  PRIMARY KEY,  "
				+ "`VIN` varchar(17) DEFAULT '' ,"
				+ "`TAG_CODE` varchar(40) DEFAULT '' ,"
				+ "  `PLATE_NUMBER` char(7) , "
				+ " `CAR_TYPE` smallint(4) DEFAULT NULL, "
				+ " `POSSESSOR` varchar(5) DEFAULT NULL , "
				+ " `ADDRESS` varchar(50) DEFAULT NULL, "
				+ " `CAR_USAGE` smallint(4)  ,  "
				+ "`BRAND_PROPERTY` varchar(20) DEFAULT NULL ,"
				+ "  `ENGINE_NUMBER` varchar(10) DEFAULT NULL ,"
				+ "  `REGISTER_DATE` date DEFAULT NULL, "
				+ " `ISSUE_OFFICE` varchar(30)  DEFAULT '', "
				+ " `DRIVING_LICENSE_PIC` varchar(100)  DEFAULT '', "
				+ " `ISSUE_DATE` date DEFAULT NULL, "
				+ " `SUM_QUALITY` double(11,2) DEFAULT NULL, "
				+ " `REORGANIZE_QUALITY` double(11,2) DEFAULT NULL , "
				+ " `CHECK_QUALITY` double(11,2) DEFAULT NULL, "
				+ " `TRACTION_QUALITY` double(11,2) DEFAULT NULL, "
				+ " `GABARITE_LENGTH` int(11) DEFAULT NULL, "
				+ " `GABARITE_WIDTH` int(11) DEFAULT NULL, "
				+ " `GABARITE_HEIGHT` int(11) DEFAULT NULL ,"
				+ "  `INDATE` date DEFAULT NULL ,"
				+ "  `CHECKOUT_DATE` date DEFAULT NULL, "
				+ " `CREATE_TIME` datetime , " + " `CREATOR` int(11) )");
		arg0.execSQL("CREATE TABLE `or_car_insurance` "
				+ "(  `ID` char(32)  PRIMARY KEY, " + " `VIN` varchar(20) , "
				+ " `INSURANCE_NUMBER` varchar(20) , "
				+ " `NSURANCE_COMPANY` varchar(30) , "
				+ " `INSURANCE_PEOPLE` varchar(5) , "
				+ " `CAR_NUMBER` char(7) ,  "
				+ "`FACTORY_MODEL` varchar(20) ,  "
				+ "`ENGINE_NUMBER` varchar(20) , "
				+ " `EMPLOY_NATURE` int(4) , "
				+ " `PASSENGERS_NUMBER` varchar(20) ,  "
				+ "`DRIVER` varchar(5) , " + "`TRAVEL_AREA` varchar(100) ,"
				+ "  `INSURANCE_DATE` varchar(21)  ,"
				+ " `INSURANCE_GRADE` varchar(20) , "
				+ " `GUARANTEE_PICTURE` varchar(50) , "
				+ " `CREATOR` int(11) , " + " `CREATE_TIME` datetime )");
		arg0.execSQL("CREATE TABLE `or_carrier_warranty` "
				+ "(  `ID` char(32)   PRIMARY KEY, "
				+ " `WARRANTY_NUMBER` varchar(20)  , "
				+ " `VIN` varchar(20) DEFAULT NULL, "
				+ " `INSURANCE_COMPANY` varchar(25) , "
				+ " `VERIFICATION_CODE` varchar(18)  ,  "
				+ "`INSURED_PERSON` varchar(5)  , "
				+ " `BUSINESS_ADDRESS` varchar(40) DEFAULT NULL,  "
				+ "`POSTCODE` varchar(6) DEFAULT NULL,  "
				+ "`TELEPHONE_NUMBER` varchar(12) ,  "
				+ "`FAX` varchar(11) DEFAULT NULL, "
				+ " `RISK_CAR_COUNT` int(11) DEFAULT NULL, "
				+ "`LICENSE_NUMBER` char(7), "
				+ " `ENGINE_NUMBER` varchar(20) ,"
				+ "  `TRANSMISSION_RANGE` varchar(30) DEFAULT NULL, "
				+ " `DANGER_NAME` varchar(20) DEFAULT NULL, "
				+ " `PREMIUM_FEE` double(11,2) DEFAULT NULL , "
				+ " `INSURANCE_TIME` varchar(21) DEFAULT NULL , "
				+ " `INSURANCE_SOLVE_WAY` int(4) DEFAULT NULL, "
				+ " `INSURER_ADDRESS` varchar(40) DEFAULT NULL, "
				+ " `SERVICE_TEL` varchar(20) DEFAULT NULL,"
				+ "  `UNDERWRITING` varchar(20) DEFAULT NULL, "
				+ " `PREPARE_DOCUMENT` varchar(20) DEFAULT NULL, "
				+ " `HANDLE` varchar(20) DEFAULT NULL ,"
				+ "  `SIGN_DATE` date DEFAULT NULL, "
				+ " `WARRANTY_PICTURE` varchar(50) DEFAULT NULL, "
				+ " `CREATOR` int(11) ,  " + "`CREATE_TIME` datetime )");
		arg0.execSQL("CREATE TABLE `or_dangerchemical` " +
				"(`ID` varchar(11) PRIMARY KEY," +
				"`ALIAS_NAME` varchar(32) DEFAULT NULL ," +
				"`NAME` varchar(50) DEFAULT NULL ," +
				"`ENGLISH_NAME` varchar(50) DEFAULT NULL ," +
				"`CAS_NUMBER` varchar(50) ," +
				"`CATEGORY` varchar(50) ," +
				"`CATEGORY_CODE` char(28)" +
				")");
		arg0.execSQL("CREATE TABLE `or_basestation_fix` " +
				"(`ID` varchar(11) PRIMARY KEY," +
				"`NAME` varchar(100) DEFAULT NULL ," +
				"`ADDRESS` varchar(100) DEFAULT NULL ," +
				"`LONGITUDE` double DEFAULT NULL ," +
				"`LATITUDE` double ," +
				"`ALTITUDE` double ," +
				"`STATUS` char(28) ," +
				"`TYPE` char(28)" +
				")");
		
		arg0.execSQL("CREATE TABLE `or_company_dangerchemical` " +
				"(`ID` varchar(11) PRIMARY KEY," +
				"`DC_ID` varchar(32) DEFAULT NULL ," +
				"`COMPANY_ID` char(32) DEFAULT NULL ," +
				"`PHYSICAL_CHEMICAL` text DEFAULT NULL ," +
				"`SPECIAL_MSG` text ," +
				"`HAZARD_INFO` text ," +
				"`SAFETY_MEASURES` text ," +
				"`EMERGENCY_DISPOSAL` text ," +
				"`CREATE_TIME` datetime ," +
				"`CREATOR` char(28)" +
				")");

		arg0.execSQL("CREATE TABLE `or_driver`"
				+ " (  `ID` char(32)  PRIMARY KEY,"
				+ "  `NAME` varchar(20)  , " + " `SEX` tinyint(4) , "
				+ " `NATIONALITY` char(4) , "
				+ " `DRIVER_LICENSE_TYPE` char(4) ,  "
				+ "`DRIVER_LICENSE_NUMBER` char(18) ,"
				+ "  `EDT_ID` char(30) DEFAULT NULL, "
				+ " `FIRST_OBTAIN_DATE` date , " + " `EXPIRE_DATE` date , "
				+ " `CERT_CODE` varchar(20)  , "
				+ " `CREATE_TIME` datetime  , " + " `CREATOR` int(11) , "
				+ " `IS_DEL` int(10) , " + " `STATUS` unsigned int(10) , "
				+ " `DRIVER_LICENSE_PIC` varchar(100) )");
		arg0.execSQL("CREATE TABLE `or_environment_checkout`"
				+ " (  `ID` varchar(40)  PRIMARY KEY,  "
				+ "`MARK_NUMBER` varchar(16) ,  "
				+ "`VIN` varchar(20) DEFAULT NULL, "
				+ " `CAR_NUMBER` char(7)  , " + " `CAR_RECORD_DATE` date , "
				+ " `CAR_FUEL` int(4) , " + " `CAR_DISCHARGE` varchar(5)  ,"
				+ "  `EFFECTIVE_DATE` date , " + " `VERIFY_DATE` date  ,  "
				+ "`ENVIRONMENT_PICTURE` varchar(100)  , "
				+ " `CREATOR` int(11) , " + " `CREATE_TIME` datetime  )");
		arg0.execSQL("CREATE TABLE `or_hc_company`"
				+ " (  `ID` char(32)  PRIMARY KEY, "
				+ " `NAME` varchar(20)  UNIQUE, "
				+ " `PERSON_CHARGE` varchar(20) DEFAULT NULL , "
				+ " `PHONE` varchar(20) DEFAULT NULL , "
				+ " `ADDRESS` varchar(100)  , "
				+ " `DESCRIPTION` varchar(500) DEFAULT NULL , "
				+ " `STATUS` char(1) DEFAULT '0' )");
		arg0.execSQL("CREATE TABLE `or_permit_run_cert` "
				+ "(`ID` char(32)  PRIMARY KEY,"
				+ "`VIN` varchar(20) DEFAULT NULL,"
				+ "`CERT_NUMBER` char(7) DEFAULT NULL ,"
				+ "`PLATE_NUMBER` char(7)  DEFAULT '' ,"
				+ "`EXPIRE_DATE` date DEFAULT NULL," + "`PERMIT_LINES` text ,"
				+ "`FORBID_RUN_TIME` varchar(100) DEFAULT NULL ,"
				+ "`PERMIT_RUN_TYPE` char(4) DEFAULT NULL ,"
				+ "`PERMIT_CERT_PIC` varchar(100) DEFAULT NULL ,"
				+ "`CREATE_TIME` datetime )");

		arg0.execSQL("CREATE TABLE `or_job_cert`"
				+ " (  `ID` char(32)  PRIMARY KEY, " + " `NAME` varchar(20) , "
				+ " `CERT_NUMBER` char(18)  , " + " `ID_NUMBER` char(18) ,  "
				+ "`ADDRESS` varchar(100)  ,  "
				+ "`CERT_TYPE` varchar(100)  , " + " `ISSUE_ORG` int(11) , "
				+ " `EXPIRE_DATE` date , " + " `CERT_PIC` varchar(200) , "
				+ " `CREATE_TIME` datetime , " + " `CREATOR` int(11) , "
				+ " `IS_DEL` int(10) )");
		arg0.execSQL("CREATE TABLE `or_special_equipment` "
				+ "(  `ID` char(32)  PRIMARY KEY,  " + "`VIN` varchar(20) , "
				+ " `NUMBER_REGISTER` varchar(11) DEFAULT NULL , "
				+ " `NUMBER` varchar(20) DEFAULT NULL  , "
				+ " `COMPANY` varchar(20) DEFAULT NULL  , "
				+ " `REGISTER_CODE` varchar(20) DEFAULT NULL ,  "
				+ "`CONTAINER_NAME` varchar(20) DEFAULT NULL , "
				+ " `MANUFACTURING_COMPANY` varchar(20) DEFAULT NULL , "
				+ " `MANUFACTURING_NUMBER` varchar(20) DEFAULT NULL , "
				+ " `INTRUDUTION` varchar(20) DEFAULT NULL , "
				+ " `START_DATE` date DEFAULT NULL, "
				+ " `CERTIFICATE_DATE` date DEFAULT NULL , "
				+ " `CERTIFICATE_COMPANY` varchar(20) DEFAULT NULL , "
				+ " `NEXT_CHECKOUT_DATE` date DEFAULT NULL ,  "
				+ "`CAR_NUMBER` varchar(7)  DEFAULT '' ,  "
				+ "`TANK_CONTAINER_NUMBER` varchar(20) DEFAULT NULL , "
				+ " `VERSION` varchar(20) DEFAULT NULL , "
				+ " `TACTIC_FORM` varchar(20) DEFAULT NULL , "
				+ " `CONTAINER_LENGTH` double(10,2) DEFAULT NULL, "
				+ " `CONTAINER_WIDTH` double(10,2) DEFAULT NULL, "
				+ " `CONTAINER_HIGH` double(10,2) DEFAULT NULL ,  "
				+ "`HANDLING_MODE` int(4) DEFAULT NULL , "
				+ " `HEAT_MODE` int(4) DEFAULT NULL , "
				+ " `BATHOLITH_NUMBER` varchar(20) DEFAULT NULL,"
				+ "  `NET_WEIGHT` double(10,2) DEFAULT NULL , "
				+ " `LOAD_WEIGHT` double(10,2) DEFAULT NULL , "
				+ " `SAFETY_VALVE_COUNT` int(11) DEFAULT NULL ,"
				+ "  `SAFETY_VALVE_TYPE` varchar(20) DEFAULT NULL , "
				+ " `RUPTURE_DISK_COUNT` int(11) DEFAULT NULL,  "
				+ "`RUPTURE_DISK_TYPE` varchar(20) DEFAULT NULL ,  "
				+ "`URGENCY_VALVE_COUNT` int(11) DEFAULT NULL,  "
				+ "`URGENCY_VALVE_TYPE` varchar(20) DEFAULT NULL,  "
				+ "`LIQUID_LEVEL_COUNT` int(11) DEFAULT NULL ,"
				+ "  `LIQUID_LEVEL_TYPE` varchar(20) DEFAULT NULL , "
				+ " `CRASHPROOF_EQUIPMENT_COUNT` int(11) DEFAULT NULL ,  "
				+ "`MANAGE_NUMBER` varchar(20) DEFAULT NULL,"
				+ "  `CASING_VOLUME` double(10,2) DEFAULT NULL, "
				+ " `TANK_OD` double(10,2) DEFAULT NULL, "
				+ " `TANK_LENGTH` double(10,2) DEFAULT NULL , "
				+ " `TANK_INNER_MATERIAL` varchar(20) DEFAULT NULL , "
				+ " `TANK_OUTER_MATERIAL` varchar(20) DEFAULT NULL,  "
				+ "`TEST_PRESSURE` double(10,2) DEFAULT NULL ,  "
				+ "`MAXIMUM_WORKING_PRESSURE` double(10,2) DEFAULT NULL , "
				+ " `DESIGN_TEMPERATURE` double(10,2) DEFAULT NULL, "
				+ " `MAXIMUM_WEIGHT` double(10,2) DEFAULT NULL ,"
				+ "  `FILLING_RATIO` varchar(20) DEFAULT NULL, "
				+ " `TANK_INTRUDUTION` varchar(20) DEFAULT NULL , "
				+ " `MANHOLE_LOCATION` int(4) DEFAULT NULL, "
				+ " `INSCRIPTION_LOCTION` int(4) DEFAULT NULL,  "
				+ "`TANK_COLOR` int(4) DEFAULT NULL, "
				+ " `TANK_CANISTER_THICKNESS` double(10,2) DEFAULT NULL, "
				+ " `TANK_COVER_THICKNESS` double(10,2) DEFAULT NULL, "
				+ " `TANK_OUTER_THICKNESS` double(10,2) DEFAULT NULL, "
				+ " `SPECIAL_EQUIPMENT_PICTURE` varchar(50) DEFAULT NULL, "
				+ " `CREATOR` int(11) DEFAULT NULL, "
				+ " `CREATE_TIME` datetime )");

		arg0.execSQL("CREATE TABLE `or_transport_cert`"
				+ " (  `ID` char(32)  PRIMARY KEY, " + " `VIN` varchar(17) , "
				+ " `CERT_NUMBER` varchar(11)  , " + " `COMPANY` char(30)  , "
				+ " `ADDRESS` varchar(30)  , " + " `PLATE_NUMBER` char(7)  , "
				+ " `BUS_LICENSE_NUMBER` varchar(12)  , "
				+ " `ECONOMY_TYPE` varchar(25)  , "
				+ " `CAR_TYPE` varchar(25) ,"
				+ "  `MAX_LOAD_WEIGHT` int(11) , " + " `CAR_SIZE_L` int(10) , "
				+ " `CAR_SIZE_W` int(10) ," + "  `CAR_SIZE_H` int(10) , "
				+ " `BUSSINESS_SCOPE` varchar(30)  ,"
				+ "  `ISSUE_ORG` varchar(20)  ," + "  `ISSUE_DATE` date , "
				+ " `EXPIRE_DATE` date , " + " `REMARK` varchar(200) ,  "
				+ "`TRANSPORT_CERT_PIC` varchar(100) ,"
				+ "`CREATE_TIME` datetime DEFAULT NULL,"
				+ "  `CERT_ATTACH_NUMBER` varchar(11) DEFAULT NULL)");
		arg0.execSQL("CREATE TABLE `sys_dictionaries`"
				+ " (  `ZD_ID` char(4)  PRIMARY KEY , "
				+ " `NAME` varchar(100) DEFAULT NULL, "
				+ " `BIANMA` varchar(100) DEFAULT NULL,"
				+ "  `ORDY_BY` int(10) DEFAULT NULL, "
				+ " `PARENT_ID` char(4) ," + "  `JB` int(10) DEFAULT NULL, "
				+ " `P_BM` varchar(1000) DEFAULT NULL)");

		arg0.execSQL("create table UserInformation"
				+ "(userID INTEGER PRIMARY KEY,username VARCHAR,password VARCHAR)");
		arg0.execSQL("create table UploadTask_Cars"
				+ "(taskID INTEGER PRIMARY KEY AUTOINCREMENT,checkcategory VARCHAR,time VARCHAR,uploadcondition VARCHAR)");
		arg0.execSQL("create table UploadTask_People"
				+ "(taskID INTEGER PRIMARY KEY AUTOINCREMENT,checkcategory VARCHAR,time VARCHAR,uploadcondition VARCHAR)");
		arg0.execSQL("create table CarsFailUpload"
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,userID varchar,baseStationID varchar,isOverWeight varchar,"
				+ "status4PermitRunCert varchar,status4TransportCert_Head varchar,status4TransportCert_Tail varchar,"
				+ "status4InsuranceCert_Head varchar,status4InsuranceCert_Cargo varchar,status4InsuranceCert_Tail varchar,status4SpecialEquipUsage varchar,plateNumber varchar)");
		arg0.execSQL("create table PeopleFailUpload"
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,userID varchar,baseStationID varchar,status4DriverLicense varchar,status4JobCert varchar,driverName varchar)");
		arg0.execSQL("create table Message"
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MessageTitle varchar,Message varchar)");
		arg0.execSQL("create table DataVersion"
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DataVersionCode INTEGER)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println("update a sqlite database");

		// db.beginTransaction(); // 手动设置开始事务
		// try {
		// getSQL();
		// if (sqls != null) {
		// String sql[] = sqls.split(";");
		// for (int i = 0; i < sql.length; i++) {
		// System.out.println(sql[i]);
		// db.execSQL(sql[i]);
		// }
		// }
		// db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交。
		// // VERSON = arg2;
		// // 在setTransactionSuccessful和endTransaction之间不进行任何数据库操作
		// } catch (Exception e) {
		// // MyLog.printStackTraceString(e);
		// } finally {
		// // String VersionCode = "0";
		// // preferences = getSharedPreferences("data", Login.MODE_PRIVATE);
		// // SharedPreferences.Editor editor = preferences.edit();
		// // editor.putString("DataVerSion", VersionCode);
		// // editor.commit();
		// db.endTransaction(); // 处理完成
		// }

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i("xinye", "#############数据库降级了##############：" + VERSON);
		super.onDowngrade(db, oldVersion, newVersion);

	}

	/**
	 * 使用zip进行解压缩
	 * 

	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = Base64.decode(
					new String(compressedStr.getBytes(), "UTF-8").getBytes(),
					Base64.DEFAULT);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}

	private String getSQL() {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				String url = "http://192.168.0.114:8080/dangerchemical/api/common/v1/syncDateApp?dataIndex=8";
				HttpClient client = new DefaultHttpClient(); // 连接
				HttpGet httpget = new HttpGet(url);
				HttpResponse response;
				String sbuf = null;
				try {
					response = client.execute(httpget); // 得到数据
					HttpEntity httpentity = response.getEntity();
					if (httpentity != null) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(httpentity.getContent(),
										"utf-8"));
						sbuf = br.readLine(); // 读出来
					}
					sqls = unzip(sbuf);

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqls;

	}

}