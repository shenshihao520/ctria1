package cn.apputest.ctria.myapplication;


import java.text.SimpleDateFormat;

/**
 * 请求头生成类
 */
//数据交互的头
public class RequestHead {

    String userName;
    String requestDatetime;
    String appID;
    String userSecretKey;
    String signature;

    public String getUserName() {
        userName = "dsfsd";
        return userName;
    }

    public String getRequestDatetime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy/MM/dd hh:mm:ss");
        requestDatetime = sDateFormat.format(new java.util.Date());
        return requestDatetime;
        // ="2015/08/05 12:12:23"
    }

    public String getAppID() {
        appID = "10";
        return appID;
    }

    public String getUserSecretKey() {
        userSecretKey = "f1290186a5d0b1ceab27f4e77c0c5d68";
        return userSecretKey;
    }

    public String getSignature() {
        signature = MD5.md5(getUserName() + getUserSecretKey() + getAppID()
                + getRequestDatetime());
        // String a=userName+userSecretKey+appID+requestDatetime;
        // System.out.println(a);
        return signature;
    }

}
