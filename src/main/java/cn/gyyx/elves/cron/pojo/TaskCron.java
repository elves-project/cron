package cn.gyyx.elves.cron.pojo;

import java.io.Serializable;

/**
 * @ClassName: TaskCron
 * @Description: task_cron 计划任务表 POJO
 * @author East.F
 * @date 2016年11月4日 下午4:41:42
 */
public class TaskCron implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;					//主键ID
	
	private String ip;				    //客户端IP
	
	private String mode;				//模式('P','NP')
	
	private String app;					//模块
	
	private String func;				//方法
	
	private String param;				//参数
	
	private int timeout;				//超时时间
	
	private String proxy;				//代理器
	
	private String rule;				//规则
	
	private int flag;					//状态(0:暂停,1:正常)
	
	private String createTime;			//创建时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
