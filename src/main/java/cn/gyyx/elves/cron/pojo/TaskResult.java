package cn.gyyx.elves.cron.pojo;

import java.io.Serializable;

/**
 * @ClassName: TaskResult
 * @Description: task_result 计划任务执行结果表 POJO
 * @author East.F
 * @date 2017年6月5日 上午10:49:44
 */
public class TaskResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;						//主键ID
	
	private String cronId;				//task_cron的Id
	
	private String flag;				//任务状态（"true","false"）
	
	private String error;				//任务执行false时原因
	
	private int workerFlag;				//agent-worker执行状态(0-失败,1-成功,-1-系统错误)
	
	private String workerMessage;		//agent-worker执行结果
	
	private int workerCosttime;			//agent-worker执行耗时
	
	private String endTime;				//执行结束时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCronId() {
		return cronId;
	}

	public void setCronId(String cronId) {
		this.cronId = cronId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getWorkerFlag() {
		return workerFlag;
	}

	public void setWorkerFlag(int workerFlag) {
		this.workerFlag = workerFlag;
	}

	public String getWorkerMessage() {
		return workerMessage;
	}

	public void setWorkerMessage(String workerMessage) {
		this.workerMessage = workerMessage;
	}

	public int getWorkerCosttime() {
		return workerCosttime;
	}

	public void setWorkerCosttime(int workerCosttime) {
		this.workerCosttime = workerCosttime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
