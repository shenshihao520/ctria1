package cn.apputest.ctria.data;

/**
 * 
 * @author Shihao Shen 上传任务实体
 * 
 */
public class UploadTaskDataEntity {

	public int taskID;
	public String checkcategory;
	public String time;
	public String uploadcondition;

	public UploadTaskDataEntity(String checkcategory, String time,
			String uploadcondition) {

		// this.taskID = taskID;

		this.checkcategory = checkcategory;
		this.time = time;
		this.uploadcondition = uploadcondition;
	}

	public UploadTaskDataEntity() {
		// TODO Auto-generated constructor stub
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getCheckcategory() {
		return checkcategory;
	}

	public void setCheckcategory(String checkcategory) {
		this.checkcategory = checkcategory;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUploadcondition() {
		return uploadcondition;
	}

	public void setUploadcondition(String uploadcondition) {
		this.uploadcondition = uploadcondition;
	}

}
