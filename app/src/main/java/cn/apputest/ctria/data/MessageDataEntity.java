package cn.apputest.ctria.data;

/**
 * @author 作者Shihao Shen:
 * @version 创建时间：2015-10-21 下午3:45:52 消息实体
 */
public class MessageDataEntity {
	private Integer ID;
	private String MessageTitle;
	private String Message;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getMessageTitle() {
		return MessageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		MessageTitle = messageTitle;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

}
