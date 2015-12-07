package cn.apputest.ctria.myapplication;

/**
 * @author Shenshihao Url 存放类
 */
public class Url {
    // 存放所有的Url
    String BaseUrl = "http://111.198.72.101:8081/appbackend/api";
    String login = BaseUrl + "/common/v1/login";
    public String modifypwd = BaseUrl + "/common/v1/modifypwd";

    String carcheckrecord = BaseUrl + "/ctria/v1/carcheckrecord";
    String personcheckrecord = BaseUrl + "/ctria/v1/personcheckrecord";
    String versoninfo = BaseUrl + "/common/v1/versoninfo";
    String log = BaseUrl + "/common/v1/log";

    String dateversioninfo = BaseUrl + "/common/v1/syncDateApp?dataIndex=";
    String download = BaseUrl + "/common/v1/downloadLatestApp?appID=10";

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    // 192.168.0.104:8080
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getModifypwd() {
        return modifypwd;
    }

    public void setModifypwd(String modifypwd) {
        this.modifypwd = modifypwd;
    }


    public String getCarcheckrecord() {
        return carcheckrecord;
    }

    public void setCarcheckrecord(String carcheckrecord) {
        this.carcheckrecord = carcheckrecord;
    }

    public String getPersoncheckrecord() {
        return personcheckrecord;
    }

    public void setPersoncheckrecord(String personcheckrecord) {
        this.personcheckrecord = personcheckrecord;
    }


    public String getVersoninfo() {
        return versoninfo;
    }

    public void setVersoninfo(String versoninfo) {
        this.versoninfo = versoninfo;
    }

    public String getDateversioninfo() {
        return dateversioninfo;
    }

    public void setDateversioninfo(String dateversioninfo) {
        this.dateversioninfo = dateversioninfo;
    }

}
