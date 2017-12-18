package nari.selfSynchronization.bean;

public class Task {
	
	private String taskOID = "";
	//0:未明确更新（不做更新）	1:更新专题图	2:更新大馈线
	private int TaskType;
	private TaskRequest req;
	
	public Task(String taskOID, int taskType, TaskRequest req) {
		super();
		this.taskOID = taskOID;
		TaskType = taskType;
		this.req = req;
	}

	public TaskRequest getReq() {
		return req;
	}

	public int getTaskType() {
		return TaskType;
	}

	public String getTaskOID() {
		return taskOID;
	}

}
