package cn.gyyx.elves.cron.service;

import java.util.Map;

/**
 * @ClassName: CronService
 * @Description: Cron模块提供的服务接口
 * @author East.F
 * @date 2016年11月7日 上午9:30:03
 */
public interface CronService {

	/**
	 * @Title: createCron
	 * @Description: 创建
	 * @param params
	 * @return Map<String,Object>    返回类型
	 */
	public Map<String,Object> createCron(Map<String, Object> params);
	
	/**
	 * @Title: startCron
	 * @Description: 开启计划任务
	 * @param params
	 * @return
	 * @return Map<String,Object>    返回类型
	 */
	public Map<String,Object> startCron(Map<String, Object> params);
	
	/**
	 * @Title: stopCron
	 * @Description: 暂停计划任务
	 * @param params
	 * @return Map<String,Object>    返回类型
	 */
	public Map<String,Object> stopCron(Map<String, Object> params);
	
	/**
	 * @Title: deleteCron
	 * @Description: 删除计划任务
	 * @param params
	 * @return Map<String,Object>    返回类型
	 */
	public Map<String,Object> deleteCron(Map<String, Object> params);
	
	/**
	 * @Title: cronList
	 * @Description: 根据AgentIp获取计划任务
	 * @param params
	 * @return Map<String,Object>    返回类型
	 */
	public Map<String,Object> cronList(Map<String, Object> params);
	
	/**
	 * @Title: cronDetail
	 * @Description: 根据id获取计划任务最近一次执行结果
	 * @param params
	 * @return Map<String,Object>    返回类型
	 */
	public Map<String,Object> cronDetail(Map<String, Object> params);
	
	/**
	 * @Title: cronResult
	 * @Description: TODO
	 * @param params
	 * @return 设定文件
	 * @return Map<String,Object>    返回类型
	 */
	public Map<String,Object> cronResult(Map<String, Object> params); 
}
