package cn.apputest.caas.extinterface.iisiot;

public class RequestResult {

private int errorCode =0;

private Object result;

public int getErrorCode() {
	return errorCode;
}

public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
}

public Object getResult() {
	return result;
}

public void setResult(Object result) {
	this.result = result;
}

public RequestResult(int errorCode, Object result) {
	super();
	this.errorCode = errorCode;
	this.result = result;
}

public RequestResult() {
	super();
}

@Override
public String toString() {
	return "RequestResult [errorCode=" + errorCode + ", result=" + result+ "]";
}




}
