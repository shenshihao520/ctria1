package com.fntech.m10.u1.rfid.reader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface IDCService {
	String EPC = "EPC";
	String VERSION = "Version";
	String PRODUCTNO = "ProductNo";
	String PRODUCTTYPE = "ProductType";
	String CERTIFICATENO = "CertificateNo";
	String SCOPEOFBUSINESS = "ScopeOfBusiness";
	String ISSUEYEAR = "IssueYear";
	String ISSUEMONTH = "IssueMonth";
	String ISSUEDAY = "IssueDay";
	String ENTERPRISEWEIGHT = "EnterpriseWeight";
	String CHECKWEIGHT = "CheckWeight";
	String TRANSPORTPROPERTIES = "TransportProperties";
	String DRIVERID = "DriverID";
	String DRIVERNATIONALITY = "DriverNationality";
	String DRIVERCLASS = "DriverClass";
	String ISSUETIME = "IssueTime";
	String VALIDPERIOD = "ValidPeriod";
	String LICENSENO = "LicenseNo";
	String DRIVERTYPE = "DriverType";
	String CERTIFICATECARDNO = "CertificateCardNo";
	String CERTIFICATECARDRANGE = "CertificateCardRange";
	String CERTIFICATEISSUETIME = "CertificateIssueTime";
	String CERTIFICATEVALIDTIME = "CertificateValidTime";

	Set<String> inventory() throws SecurityException, IOException;

	boolean issue(Map<String, String> map) throws InvalidParamException,
			SecurityException, IOException;

	Map<String, String> check(String epc) throws InvalidParamException,
			SecurityException, IOException;

	boolean confirm(Map<String, String> map)
			throws InvalidParamException, SecurityException, IOException;

	String issueDriver(Map<String, String> map)
			throws InvalidParamException, SecurityException, IOException;

	Map<String, String> checkDriver(String epc)
			throws InvalidParamException, SecurityException, IOException;
}
