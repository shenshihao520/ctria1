package com.fntech.m10.u1.rfid.reader;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fntech.m10.u1.model.Message;
import com.fntech.m10.u1.rfid.utils.Tools;

public class DCService implements IDCService {
	private final static String IIS_IOT = "00110016";
	private final static int USER_BANK = 3;
	private final static int EPC_BANK = 1;
	private final static String PC = "3000";
	private final static int V_USER3_OFFSET_W = 22;
	private final static int V_USER3_LENGTH_W = 10;
	private final static int V_USER3_LENGTH = 160;
	private final static int D_EPC_OFFSET_W = 1;
	private final static int D_EPC_LENGTH_W = 7;
	private final static int D_USER_OFFSET_W = 0;
	private final static int D_USER_LENGTH_W = 32;
	private final static int D_USER_LENGTH = 512;

	public Set<String> inventory() throws SecurityException, IOException {
		Set<String> epcs = new HashSet<String>();
		String[] strEpc = Manager.getInstance().inventory().split("\\r\\n");
		if (strEpc == null) {
			return epcs;
		}
		for (int i = 1; i < strEpc.length - 2; i++) {
			if (strEpc[i].startsWith("end") || strEpc[i].startsWith("$>")
					|| strEpc[i].startsWith("ok")) {
				continue;
			}

			String epc = strEpc[i].trim();

			if (epc.length() != 32) {
				continue;
			}
			epcs.add(epc.substring(4, 28));
		}
		return epcs;
	}

	public boolean issue(Map<String, String> map) throws InvalidParamException,
			SecurityException, IOException {
		if (map == null) {
			throw new InvalidParamException("Input is null.");
		}

		String issueData = "";

		String epc = map.get(EPC);
		if (epc == null) {
			throw new InvalidParamException("EPC is null.");
		}
		Pattern pattern = Pattern.compile("[A-Fa-f0-9]{24}");
		Matcher matcher = pattern.matcher(epc);
		if (!matcher.matches()) {
			throw new InvalidParamException("EPC is invalid.");
		}

		String version = map.get(VERSION);
		if (version == null) {
			throw new InvalidParamException("Version is null.");
		}
		pattern = Pattern.compile("[0-1]{4}");
		matcher = pattern.matcher(version);
		if (!matcher.matches()) {
			throw new InvalidParamException("Version is invalid.");
		}
		issueData += version;

		issueData += getZeros(12) + getZeros(28); // 危化品序号12位，危化品分类28位

		String certificateNo = map.get(CERTIFICATENO);
		if (certificateNo == null) {
			throw new InvalidParamException("CertificateNo is null.");
		}
		pattern = Pattern.compile("[0-9]{12}");
		matcher = pattern.matcher(certificateNo);
		if (!matcher.matches()) {
			throw new InvalidParamException("CertificateNo is invalid.");
		}
		issueData += bcd2Binary(certificateNo);

		String scopeOfBusiness = map.get(SCOPEOFBUSINESS);
		if (scopeOfBusiness == null) {
			throw new InvalidParamException("ScopeOfBusiness is null.");
		}
		pattern = Pattern.compile("[3][0-9]{3}");
		matcher = pattern.matcher(scopeOfBusiness);
		if (!matcher.matches()) {
			throw new InvalidParamException("ScopeOfBusiness is invalid.");
		}
		issueData += bcd2Binary(scopeOfBusiness);

		String issueYear = map.get(ISSUEYEAR);
		String issueMonth = map.get(ISSUEMONTH);
		String issueDay = map.get(ISSUEDAY);
		String issueTime = issueYear + "-" + issueMonth + "-" + issueDay;
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
		format.setLenient(false);
		try {
			format.parse(issueTime);
		} catch (ParseException e) {
			throw new InvalidParamException("IssueTime is invalid.");
		}
		issueData += dec2Binary(issueYear, 7) + dec2Binary(issueMonth, 4)
				+ dec2Binary(issueDay, 5);

		issueData += getZeros(12) + getZeros(12) + getZeros(4); // 填报重量12位，检测重量12位，运输属性4位

		issueData += dec2Binary("" + CRC8.calcCrc8(issueData), 8);

		if (issueData.length() != V_USER3_LENGTH) {
			return false;
		}

		issueData = binaryString2hexString(issueData);

		String strAscPass = IIS_IOT;
		if (!java.util.regex.Pattern.matches("[0-9a-fA-F]{1,}", strAscPass)) {
			return false;
		}

		if (!selectMask(epc)) {
			return false;
		}

		long access_pass = Tools.bytesToInt2(Tools.HexString2Bytes(strAscPass));
		Message result = Manager.getInstance().writeTagMemory(V_USER3_LENGTH_W,
				USER_BANK, V_USER3_OFFSET_W, issueData, access_pass);
		if (result.getCode() != 0) {
			return false;
		}

		result = Manager.getInstance().readRagMemory(V_USER3_LENGTH_W,
				USER_BANK, V_USER3_OFFSET_W, access_pass);
		if (result.getCode() != 0) {
			return false;
		}

		String[] results = result.getResult().split("\\r\\n");
		if (results.length < 2) {
			return false;
		}
		String[] values = results[1].split(",");
		if (values.length < 2) {
			return false;
		}

		return values[0].equalsIgnoreCase(issueData)
				&& values[1].substring(6, 30).equalsIgnoreCase(epc);
	}

	public Map<String, String> check(String epc) throws InvalidParamException,
			SecurityException, IOException {
		Map<String, String> mapMemory = new HashMap<String, String>();

		if (epc == null) {
			throw new InvalidParamException("EPC is null.");
		}
		Pattern pattern = Pattern.compile("[A-Fa-f0-9]{24}");
		Matcher matcher = pattern.matcher(epc);
		if (!matcher.matches()) {
			throw new InvalidParamException("EPC is invalid.");
		}

		String strAscPass = IIS_IOT;
		if (!java.util.regex.Pattern.matches("[0-9a-fA-F]{1,}", strAscPass)) {
			return mapMemory;
		}

		if (!selectMask(epc)) {
			return mapMemory;
		}

		long access_pass = Tools.bytesToInt2(Tools.HexString2Bytes(strAscPass));
		Message result = Manager.getInstance().readRagMemory(V_USER3_LENGTH_W,
				USER_BANK, V_USER3_OFFSET_W, access_pass);
		if (result.getCode() != 0) {
			return mapMemory;
		}

		String[] results = result.getResult().split("\\r\\n");
		if (results.length < 2) {
			return mapMemory;
		}
		String[] values = results[1].split(",");
		if (values.length < 2) {
			return mapMemory;
		}

		if (!(values[0].length() == V_USER3_LENGTH_W * 4 && values[1]
				.substring(6, 30).equalsIgnoreCase(epc))) {
			return mapMemory;
		}

		String memory = hexString2binaryString(values[0]);

		mapMemory.put(VERSION, memory.substring(0, 4));

		mapMemory.put(PRODUCTNO, binary2Dec(memory.substring(4, 16), 4));

		mapMemory.put(PRODUCTTYPE, memory.substring(16, 44));

		mapMemory.put(CERTIFICATENO, binary2BCD(memory.substring(44, 92)));

		mapMemory.put(SCOPEOFBUSINESS, binary2BCD(memory.substring(92, 108)));

		mapMemory.put(ISSUEYEAR, binary2Dec(memory.substring(108, 115), 2));

		mapMemory.put(
				ISSUEMONTH,
				adjust(String.valueOf(Integer.parseInt(
						memory.substring(115, 119), 2) + 1), 2));

		mapMemory.put(
				ISSUEDAY,
				adjust(String.valueOf(Integer.parseInt(
						memory.substring(119, 124), 2) + 1), 2));

		mapMemory
				.put(ENTERPRISEWEIGHT, code2Weight(memory.substring(124, 136)));

		mapMemory.put(CHECKWEIGHT, code2Weight(memory.substring(136, 148)));

		mapMemory.put(TRANSPORTPROPERTIES, memory.substring(148, 152));

		return mapMemory;
	}

	public boolean confirm(Map<String, String> map)
			throws InvalidParamException, SecurityException, IOException {
		if (map == null) {
			throw new InvalidParamException("Input is null.");
		}

		String epc = map.get(EPC);
		if (epc == null) {
			throw new InvalidParamException("EPC is null.");
		}
		Pattern pattern = Pattern.compile("[A-Fa-f0-9]{24}");
		Matcher matcher = pattern.matcher(epc);
		if (!matcher.matches()) {
			throw new InvalidParamException("EPC is invalid.");
		}

		String productNo = map.get(PRODUCTNO);
		if (productNo == null) {
			throw new InvalidParamException("ProductNo is null.");
		}
		pattern = Pattern.compile("[0-9]{1,4}");
		matcher = pattern.matcher(productNo);
		if (!matcher.matches()) {
			throw new InvalidParamException("ProductNo is invalid.");
		}

		String productType = map.get(PRODUCTTYPE);
		if (productType == null) {
			throw new InvalidParamException("ProductType is null.");
		}
		pattern = Pattern.compile("[0-1]{28}");
		matcher = pattern.matcher(productType);
		if (!matcher.matches()) {
			throw new InvalidParamException("ProductType is invalid.");
		}

		String enterpriseWeight = map.get(ENTERPRISEWEIGHT);
		if (enterpriseWeight == null) {
			throw new InvalidParamException("EnterpriseWeight is null.");
		}
		pattern = Pattern.compile("[0-9]{1,3}[.][0-9]");
		matcher = pattern.matcher(enterpriseWeight);
		if (!matcher.matches()) {
			throw new InvalidParamException("EnterpriseWeight is invalid.");
		}

		String transportProperties = map.get(TRANSPORTPROPERTIES);
		if (transportProperties == null) {
			throw new InvalidParamException("TransportProperties is null.");
		}
		pattern = Pattern.compile("[0-1]{4}");
		matcher = pattern.matcher(transportProperties);
		if (!matcher.matches()) {
			throw new InvalidParamException("TransportProperties is invalid.");
		}

		String strAscPass = IIS_IOT;
		if (!java.util.regex.Pattern.matches("[0-9a-fA-F]{1,}", strAscPass)) {
			return false;
		}

		if (!selectMask(epc)) {
			return false;
		}

		long access_pass = Tools.bytesToInt2(Tools.HexString2Bytes(strAscPass));
		Message result = Manager.getInstance().readRagMemory(V_USER3_LENGTH_W,
				USER_BANK, V_USER3_OFFSET_W, access_pass);
		if (result.getCode() != 0) {
			return false;
		}

		String[] results = result.getResult().split("\\r\\n");
		if (results.length < 2) {
			return false;
		}
		String[] values = results[1].split(",");
		if (values.length < 2) {
			return false;
		}

		if (!(values[0].length() == V_USER3_LENGTH_W * 4 && values[1]
				.substring(6, 30).equalsIgnoreCase(epc))) {
			return false;
		}

		String memory = hexString2binaryString(values[0]);
		memory = replace(memory, dec2Binary(productNo, 12), 4);
		memory = replace(memory, productType, 16);
		memory = replace(memory, weight2Code(enterpriseWeight), 124);
		memory = replace(memory, transportProperties, 148);

		String value = memory.substring(0, V_USER3_LENGTH - 8);

		memory = value + dec2Binary("" + CRC8.calcCrc8(value), 8);
		memory = binaryString2hexString(memory);
		result = Manager.getInstance().writeTagMemory(V_USER3_LENGTH_W,
				USER_BANK, V_USER3_OFFSET_W, memory, access_pass);
		if (result.getCode() != 0) {
			return false;
		}

		result = Manager.getInstance().readRagMemory(V_USER3_LENGTH_W,
				USER_BANK, V_USER3_OFFSET_W, access_pass);
		if (result.getCode() != 0) {
			return false;
		}

		results = result.getResult().split("\\r\\n");
		if (results.length < 2) {
			return false;
		}
		values = results[1].split(",");
		if (values.length < 2) {
			return false;
		}

		return values[0].equalsIgnoreCase(memory)
				&& values[1].substring(6, 30).equalsIgnoreCase(epc);

	}

	@Override
	public String issueDriver(Map<String, String> map)
			throws InvalidParamException, SecurityException, IOException {
		if (map == null) {
			throw new InvalidParamException("Input is null.");
		}

		String newEPC = hexString2binaryString(PC) + getZeros(8) + "0010"
				+ "0001" + getZeros(8);

		String epc = map.get(EPC);
		if (epc == null) {
			throw new InvalidParamException("EPC is null.");
		}
		Pattern pattern = Pattern.compile("[A-Fa-f0-9]{24}");
		Matcher matcher = pattern.matcher(epc);
		if (!matcher.matches()) {
			throw new InvalidParamException("EPC is invalid.");
		}

		String driverID = map.get(DRIVERID);
		if (driverID == null) {
			throw new InvalidParamException("DriverID is null.");
		}
		pattern = Pattern.compile("[0-9]{18}");
		matcher = pattern.matcher(driverID);
		if (!matcher.matches()) {
			throw new InvalidParamException("DriverID is invalid.");
		}
		newEPC += driverId2binaryString(driverID) + getZeros(16);

		newEPC += dec2Binary("" + CRC8.calcCrc8(newEPC), 8);
		newEPC = binaryString2hexString(newEPC);
		if (!selectMask(epc)) {
			return "";
		}

		String strAscPass = IIS_IOT;
		long access_pass = Tools.bytesToInt2(Tools.HexString2Bytes(strAscPass));
		Message result = Manager.getInstance().writeTagMemory(D_EPC_LENGTH_W,
				EPC_BANK, D_EPC_OFFSET_W, newEPC, access_pass);
		if (result.getCode() != 0) {
			return "";
		}

		Set<String> set = inventory();
		if (set.size() <= 0) {
			return "";
		}
		Iterator<String> iter = set.iterator();
		boolean isEPCFound = false;
		while (iter.hasNext()) {
			if ((PC + iter.next()).equalsIgnoreCase(newEPC)) {
				isEPCFound = true;
				break;
			}
		}

		if (!isEPCFound) {
			return "";
		}

		newEPC = newEPC.substring(4, 28);
		String issueData = getZeros(208);

		String driverNationality = map.get(DRIVERNATIONALITY);
		if (driverNationality == null) {
			throw new InvalidParamException("DriverNationality is null.");
		}
		pattern = Pattern.compile("[0-9]{3}");
		matcher = pattern.matcher(driverNationality);
		if (!matcher.matches()) {
			throw new InvalidParamException("DriverNationality is invalid.");
		}
		issueData += bcd2Binary(driverNationality);

		String driverClass = map.get(DRIVERCLASS);
		if (driverClass == null) {
			throw new InvalidParamException("DriverClass is null.");
		}
		String codeClass = encodeClass(driverClass);
		if (codeClass == null) {
			throw new InvalidParamException("DriverClass is invalid.");
		}
		issueData += codeClass;

		String issueTime = map.get(ISSUETIME);
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		format.setLenient(false);
		try {
			format.parse(issueTime);
		} catch (ParseException e) {
			throw new InvalidParamException("IssueTime is invalid.");
		}
		issueData += dec2Binary(issueTime.substring(0, 2), 7)
				+ dec2Binary(""
						+ (Integer.parseInt(issueTime.substring(2, 4)) - 1), 4)
				+ dec2Binary(""
						+ (Integer.parseInt(issueTime.substring(4, 6)) - 1), 5);

		String validPeriod = map.get(VALIDPERIOD);
		if (validPeriod == null) {
			throw new InvalidParamException("ValidPeriod is null.");
		}
		pattern = Pattern.compile("[0-9]{1,2}");
		matcher = pattern.matcher(validPeriod);
		if (!matcher.matches()) {
			throw new InvalidParamException("ValidPeriod is invalid.");
		}
		issueData += dec2Binary("" + (Integer.parseInt(validPeriod) - 1), 4);

		String licenseNo = map.get(LICENSENO);
		if (licenseNo == null) {
			throw new InvalidParamException("LicenseNo is null.");
		}
		pattern = Pattern.compile("[0-9]{12}");
		matcher = pattern.matcher(licenseNo);
		if (!matcher.matches()) {
			throw new InvalidParamException("LicenseNo is invalid.");
		}
		issueData += bcd2Binary(licenseNo);

		issueData += dec2Binary("" + CRC8.calcCrc8(issueData), 8);

		issueData += getZeros(96) + getZeros(4);

		String driverType = map.get(DRIVERTYPE);
		if (driverType == null) {
			throw new InvalidParamException("DriverType is null.");
		}
		pattern = Pattern.compile("[0-1]{4}");
		matcher = pattern.matcher(driverType);
		if (!matcher.matches()) {
			throw new InvalidParamException("DriverType is invalid.");
		}
		issueData += driverType;

		String certificateCardNo = map.get(CERTIFICATECARDNO);
		if (certificateCardNo == null) {
			throw new InvalidParamException("CertificateCardNo is null.");
		}
		pattern = Pattern.compile("[0-9]{12}");
		matcher = pattern.matcher(certificateCardNo);
		if (!matcher.matches()) {
			throw new InvalidParamException("CertificateCardNo is invalid.");
		}
		issueData += bcd2Binary(certificateCardNo);

		String certificateCardRange = map.get(CERTIFICATECARDRANGE);
		if (certificateCardRange == null) {
			throw new InvalidParamException("CertificateCardRange is null.");
		}
		pattern = Pattern.compile("[0-1]{8}");
		matcher = pattern.matcher(certificateCardRange);
		if (!matcher.matches()) {
			throw new InvalidParamException("CertificateCardRange is invalid.");
		}
		issueData += certificateCardRange;

		String certificateIssueTime = map.get(CERTIFICATEISSUETIME);
		try {
			format.parse(certificateIssueTime);
		} catch (ParseException e) {
			throw new InvalidParamException("CertificateIssueTime is invalid.");
		}
		issueData += dec2Binary(certificateIssueTime.substring(0, 2), 7)
				+ dec2Binary(
						""
								+ (Integer.parseInt(certificateIssueTime
										.substring(2, 4)) - 1), 4)
				+ dec2Binary(
						""
								+ (Integer.parseInt(certificateIssueTime
										.substring(4, 6)) - 1), 5);

		String certificateValidTime = map.get(CERTIFICATEVALIDTIME);
		try {
			format.parse(certificateValidTime);
		} catch (ParseException e) {
			throw new InvalidParamException("CertificateValidTime is invalid.");
		}
		issueData += dec2Binary(certificateValidTime.substring(0, 2), 7)
				+ dec2Binary(
						""
								+ (Integer.parseInt(certificateValidTime
										.substring(2, 4)) - 1), 4)
				+ dec2Binary(
						""
								+ (Integer.parseInt(certificateValidTime
										.substring(4, 6)) - 1), 5);

		issueData += getZeros(8);

		issueData += dec2Binary("" + CRC8.calcCrc8(issueData), 8);

		if (issueData.length() != D_USER_LENGTH) {
			return "";
		}

		issueData = binaryString2hexString(issueData);

		if (!selectMask(newEPC)) {
			return "";
		}

		result = Manager.getInstance().writeTagMemory(D_USER_LENGTH_W,
				USER_BANK, D_USER_OFFSET_W, issueData, access_pass);
		if (result.getCode() != 0) {
			return "";
		}

		result = Manager.getInstance().readRagMemory(D_USER_LENGTH_W,
				USER_BANK, D_USER_OFFSET_W, access_pass);
		if (result.getCode() != 0) {
			return "";
		}

		String[] results = result.getResult().split("\\r\\n");
		if (results.length < 2) {
			return "";
		}
		String[] values = results[1].split(",");
		if (values.length < 2) {
			return "";
		}

		if (values[0].equalsIgnoreCase(issueData)
				&& values[1].substring(6, 30).equalsIgnoreCase(newEPC)) {
			return newEPC;
		} else {
			return "";
		}

	}

	@Override
	public Map<String, String> checkDriver(String epc)
			throws InvalidParamException, SecurityException, IOException {
		Map<String, String> mapMemory = new HashMap<String, String>();

		if (epc == null) {
			throw new InvalidParamException("EPC is null.");
		}
		Pattern pattern = Pattern.compile("[A-Fa-f0-9]{24}");
		Matcher matcher = pattern.matcher(epc);
		if (!matcher.matches()) {
			throw new InvalidParamException("EPC is invalid.");
		}

		String strAscPass = IIS_IOT;
		if (!java.util.regex.Pattern.matches("[0-9a-fA-F]{1,}", strAscPass)) {
			return mapMemory;
		}

		if (!selectMask(epc)) {
			return mapMemory;
		}

		long access_pass = Tools.bytesToInt2(Tools.HexString2Bytes(strAscPass));
		Message result = Manager.getInstance().readRagMemory(D_USER_LENGTH_W,
				USER_BANK, D_USER_OFFSET_W, access_pass);
		if (result.getCode() != 0) {
			return mapMemory;
		}

		String[] results = result.getResult().split("\\r\\n");
		if (results.length < 2) {
			return mapMemory;
		}
		String[] values = results[1].split(",");
		if (values.length < 2) {
			return mapMemory;
		}

		if (!(values[0].length() == D_USER_LENGTH_W * 4 && values[1].substring(
				6, 30).equalsIgnoreCase(epc))) {
			return mapMemory;
		}

		epc = hexString2binaryString(epc);

		String memory = hexString2binaryString(values[0]);

		mapMemory.put(DRIVERID, binaryString2DriverID(epc.substring(24, 72)));

		mapMemory
				.put(DRIVERNATIONALITY, binary2BCD(memory.substring(208, 220)));

		mapMemory.put(DRIVERCLASS, decodeClass(memory.substring(220, 228)));

		mapMemory.put(ISSUETIME, decodeTime(memory.substring(228, 244)));

		mapMemory.put(VALIDPERIOD,
				"" + (Integer.parseInt(memory.substring(244, 248), 2) + 1));

		mapMemory.put(LICENSENO, binary2BCD(memory.substring(248, 296)));

		mapMemory.put(DRIVERTYPE, memory.substring(404, 408));

		mapMemory
				.put(CERTIFICATECARDNO, binary2BCD(memory.substring(408, 456)));

		mapMemory.put(CERTIFICATECARDRANGE, memory.substring(456, 464));

		mapMemory.put(CERTIFICATEISSUETIME,
				decodeTime(memory.substring(464, 480)));

		mapMemory.put(CERTIFICATEVALIDTIME,
				decodeTime(memory.substring(480, 496)));

		return mapMemory;
	}

	private String getZeros(int length) {
		String zeros = "";
		for (int i = 0; i < length; i++) {
			zeros = zeros + "0";
		}
		return zeros;
	}

	private String decodeTime(String s) {
		return binary2Dec(s.substring(0, 7), 2)
				+ adjust(
						String.valueOf(Integer.parseInt(s.substring(7, 11), 2) + 1),
						2)
				+ adjust(
						String.valueOf(Integer.parseInt(s.substring(11, 16), 2) + 1),
						2);

	}

	private String code2Weight(String code) {
		return "" + Integer.parseInt(code.substring(0, 8), 2) + "."
				+ Integer.parseInt(code.substring(8, 12), 2);
	}

	private String weight2Code(String weight) {
		String[] array = weight.split("\\.");
		return dec2Binary(array[0], 8) + dec2Binary(array[1], 4);
	}

	private String bcd2Binary(String str) {
		char[] strChar = str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			String strBin = Integer.toBinaryString(strChar[i]);
			result += strBin.substring(strBin.length() - 4, strBin.length());// 截取后四位
		}
		return result;
	}

	private String binary2BCD(String str) {
		String result = "";
		for (int i = 0; i < str.length() / 4; i++) {
			result += Integer.parseInt(str.substring(4 * i, 4 * (i + 1)), 2);
		}

		return result;
	}

	/**
	 * 将十进制字符串转化成二进制
	 * 
	 * @param str
	 *            字符串
	 * @param len
	 *            输出长度，超出则截取后len位，不足在前补0
	 * @return
	 */
	private String dec2Binary(String dec, int len) {
		String strBin = Integer.toBinaryString(Integer.valueOf(dec));
		if (strBin.length() > len) {
			strBin = strBin.substring(strBin.length() - len, strBin.length());
		} else if (strBin.length() < len) {
			strBin = getZeros(len - strBin.length()) + strBin;
		}
		return strBin;
	}

	private String binary2Dec(String bin, int len) {
		String strDec = String.valueOf(Integer.parseInt(bin, 2));
		if (strDec.length() > len) {
			strDec = strDec.substring(strDec.length() - len, strDec.length());
		} else if (strDec.length() < len) {
			strDec = getZeros(len - strDec.length()) + strDec;
		}
		return strDec;
	}

	private String binaryString2hexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	private String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	private String replace(String source, String value, int offset) {

		return source.substring(0, offset) + value
				+ source.substring(offset + value.length());

	}

	private boolean selectMask(String epc) throws SecurityException,
			IOException {
		return Manager.getInstance().selectMask(0, 112, 1, 16, PC + epc, 4, 1)
				.getCode() == 0;
	}

	private char getLast(String id) {
		int[] coefficient = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
				10, 5, 8, 4, 2 };
		char[] last = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4',
				'3', '2' };
		char[] string = id.toCharArray();
		int sum = 0;
		for (int i = 0; i < string.length; i++) {
			sum += (string[i] - '0') * coefficient[i];
		}
		return last[sum % 11];
	}

	private String driverId2binaryString(String id) {
		String strBin = "";
		strBin += dec2Binary(id.substring(0, 2), 7);
		strBin += dec2Binary(id.substring(2, 4), 7);
		strBin += dec2Binary(id.substring(4, 6), 7);
		strBin += dec2Binary(""
				+ (Integer.parseInt(id.substring(6, 10)) - 1945), 8);
		strBin += dec2Binary("" + (Integer.parseInt(id.substring(10, 12)) - 1),
				4);
		strBin += dec2Binary("" + (Integer.parseInt(id.substring(12, 14)) - 1),
				5);
		strBin += dec2Binary(id.substring(14, 17), 10);
		return strBin;
	}

	private String binaryString2DriverID(String binaryString) {
		String driverID = "";
		driverID += binary2Dec(binaryString.substring(0, 7), 2);
		driverID += binary2Dec(binaryString.substring(7, 14), 2);
		driverID += binary2Dec(binaryString.substring(14, 21), 2);
		driverID += (Integer.parseInt(binary2Dec(
				binaryString.substring(21, 29), 3)) + 1945);
		driverID += adjust(String.valueOf(Integer.parseInt(
				binaryString.substring(29, 33), 2) + 1), 2);
		driverID += adjust(String.valueOf(Integer.parseInt(
				binaryString.substring(33, 38), 2) + 1), 2);
		driverID += binary2Dec(binaryString.substring(38, 48), 3);
		driverID += getLast(driverID);
		return driverID;
	}

	private String adjust(String s, int length) {
		if (s.length() < length) {
			s = getZeros(length - s.length()) + s;
		}
		return s;
	}

	private String encodeClass(String str) {
		if (str.equals("A1")) {
			return "00000000";
		} else if (str.equals("A2")) {
			return "00000001";
		} else if (str.equals("A3")) {
			return "00000010";
		} else if (str.equals("B1")) {
			return "00010000";
		} else if (str.equals("B2")) {
			return "00010001";
		} else if (str.equals("C1")) {
			return "00100000";
		} else if (str.equals("C2")) {
			return "00100001";
		} else if (str.equals("C3")) {
			return "00100010";
		} else if (str.equals("C4")) {
			return "00100011";
		} else if (str.equals("C5")) {
			return "00100100";
		} else if (str.equals("D")) {
			return "00110000";
		} else if (str.equals("E")) {
			return "01000000";
		} else if (str.equals("F")) {
			return "01010000";
		} else if (str.equals("M")) {
			return "01100000";
		} else if (str.equals("N")) {
			return "01110000";
		} else if (str.equals("P")) {
			return "10000000";
		}
		return null;
	}

	private String decodeClass(String str) {
		if (str.equals("00000000")) {
			return "A1";
		} else if (str.equals("00000001")) {
			return "A2";
		} else if (str.equals("00000010")) {
			return "A3";
		} else if (str.equals("00010000")) {
			return "B1";
		} else if (str.equals("00010001")) {
			return "B2";
		} else if (str.equals("00100000")) {
			return "C1";
		} else if (str.equals("00100001")) {
			return "C2";
		} else if (str.equals("00100010")) {
			return "C3";
		} else if (str.equals("00100011")) {
			return "C4";
		} else if (str.equals("00100100")) {
			return "C5";
		} else if (str.equals("00110000")) {
			return "D";
		} else if (str.equals("01000000")) {
			return "E";
		} else if (str.equals("01010000")) {
			return "F";
		} else if (str.equals("01100000")) {
			return "M";
		} else if (str.equals("01110000")) {
			return "N";
		} else if (str.equals("10000000")) {
			return "P";
		}
		return null;
	}
}

class CRC8 {
	static byte[] crc8_tab = { (byte) 0, (byte) 94, (byte) 188, (byte) 226,
			(byte) 97, (byte) 63, (byte) 221, (byte) 131, (byte) 194,
			(byte) 156, (byte) 126, (byte) 32, (byte) 163, (byte) 253,
			(byte) 31, (byte) 65, (byte) 157, (byte) 195, (byte) 33,
			(byte) 127, (byte) 252, (byte) 162, (byte) 64, (byte) 30,
			(byte) 95, (byte) 1, (byte) 227, (byte) 189, (byte) 62, (byte) 96,
			(byte) 130, (byte) 220, (byte) 35, (byte) 125, (byte) 159,
			(byte) 193, (byte) 66, (byte) 28, (byte) 254, (byte) 160,
			(byte) 225, (byte) 191, (byte) 93, (byte) 3, (byte) 128,
			(byte) 222, (byte) 60, (byte) 98, (byte) 190, (byte) 224, (byte) 2,
			(byte) 92, (byte) 223, (byte) 129, (byte) 99, (byte) 61,
			(byte) 124, (byte) 34, (byte) 192, (byte) 158, (byte) 29,
			(byte) 67, (byte) 161, (byte) 255, (byte) 70, (byte) 24,
			(byte) 250, (byte) 164, (byte) 39, (byte) 121, (byte) 155,
			(byte) 197, (byte) 132, (byte) 218, (byte) 56, (byte) 102,
			(byte) 229, (byte) 187, (byte) 89, (byte) 7, (byte) 219,
			(byte) 133, (byte) 103, (byte) 57, (byte) 186, (byte) 228,
			(byte) 6, (byte) 88, (byte) 25, (byte) 71, (byte) 165, (byte) 251,
			(byte) 120, (byte) 38, (byte) 196, (byte) 154, (byte) 101,
			(byte) 59, (byte) 217, (byte) 135, (byte) 4, (byte) 90, (byte) 184,
			(byte) 230, (byte) 167, (byte) 249, (byte) 27, (byte) 69,
			(byte) 198, (byte) 152, (byte) 122, (byte) 36, (byte) 248,
			(byte) 166, (byte) 68, (byte) 26, (byte) 153, (byte) 199,
			(byte) 37, (byte) 123, (byte) 58, (byte) 100, (byte) 134,
			(byte) 216, (byte) 91, (byte) 5, (byte) 231, (byte) 185,
			(byte) 140, (byte) 210, (byte) 48, (byte) 110, (byte) 237,
			(byte) 179, (byte) 81, (byte) 15, (byte) 78, (byte) 16, (byte) 242,
			(byte) 172, (byte) 47, (byte) 113, (byte) 147, (byte) 205,
			(byte) 17, (byte) 79, (byte) 173, (byte) 243, (byte) 112,
			(byte) 46, (byte) 204, (byte) 146, (byte) 211, (byte) 141,
			(byte) 111, (byte) 49, (byte) 178, (byte) 236, (byte) 14,
			(byte) 80, (byte) 175, (byte) 241, (byte) 19, (byte) 77,
			(byte) 206, (byte) 144, (byte) 114, (byte) 44, (byte) 109,
			(byte) 51, (byte) 209, (byte) 143, (byte) 12, (byte) 82,
			(byte) 176, (byte) 238, (byte) 50, (byte) 108, (byte) 142,
			(byte) 208, (byte) 83, (byte) 13, (byte) 239, (byte) 177,
			(byte) 240, (byte) 174, (byte) 76, (byte) 18, (byte) 145,
			(byte) 207, (byte) 45, (byte) 115, (byte) 202, (byte) 148,
			(byte) 118, (byte) 40, (byte) 171, (byte) 245, (byte) 23,
			(byte) 73, (byte) 8, (byte) 86, (byte) 180, (byte) 234, (byte) 105,
			(byte) 55, (byte) 213, (byte) 139, (byte) 87, (byte) 9, (byte) 235,
			(byte) 181, (byte) 54, (byte) 104, (byte) 138, (byte) 212,
			(byte) 149, (byte) 203, (byte) 41, (byte) 119, (byte) 244,
			(byte) 170, (byte) 72, (byte) 22, (byte) 233, (byte) 183,
			(byte) 85, (byte) 11, (byte) 136, (byte) 214, (byte) 52,
			(byte) 106, (byte) 43, (byte) 117, (byte) 151, (byte) 201,
			(byte) 74, (byte) 20, (byte) 246, (byte) 168, (byte) 116,
			(byte) 42, (byte) 200, (byte) 150, (byte) 21, (byte) 75,
			(byte) 169, (byte) 247, (byte) 182, (byte) 232, (byte) 10,
			(byte) 84, (byte) 215, (byte) 137, (byte) 107, 53 };

	/**
	 * 计算数组的CRC8校验值
	 * 
	 * @param str
	 *            二进制字符串
	 * @return CRC8校验值
	 */
	public static byte calcCrc8(String str) {
		byte[] bytes = new byte[str.length() / 8];
		for (int i = 0; i < str.length() / 8; i++) {
			String aByte = str.substring(str.length() - 8 * (i + 1),
					str.length() - 8 * i);
			bytes[i] = (byte) Integer.parseInt(aByte, 2);
		}
		return calcCrc8(bytes, 0, bytes.length, (byte) 0);
	}

	/**
	 * 计算数组的CRC8校验值
	 * 
	 * @param data
	 *            需要计算的数组
	 * @return CRC8校验值
	 */
	public static byte calcCrc8(byte[] data) {
		return calcCrc8(data, 0, data.length, (byte) 0);
	}

	/**
	 * 计算CRC8校验值
	 * 
	 * @param data
	 *            数据
	 * @param offset
	 *            起始位置
	 * @param len
	 *            长度
	 * @return 校验值
	 */
	public static byte calcCrc8(byte[] data, int offset, int len) {
		return calcCrc8(data, offset, len, (byte) 0);
	}

	/**
	 * 计算CRC8校验值
	 * 
	 * @param data
	 *            数据
	 * @param offset
	 *            起始位置
	 * @param len
	 *            长度
	 * @param preval
	 *            之前的校验值
	 * @return 校验值
	 */
	public static byte calcCrc8(byte[] data, int offset, int len, byte preval) {
		byte ret = preval;
		for (int i = offset; i < (offset + len); ++i) {
			ret = crc8_tab[(0x00ff & (ret ^ data[i]))];
		}
		return ret;
	}
}
