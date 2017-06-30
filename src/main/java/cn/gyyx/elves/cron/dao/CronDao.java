package cn.gyyx.elves.cron.dao;

import java.util.List;

import cn.gyyx.elves.cron.pojo.TaskCron;
import cn.gyyx.elves.cron.pojo.TaskResult;

/**
 * @ClassName: CronDao
 * @Description: cron dao类
 * @author East.F
 * @date 2016年11月7日 上午9:27:37
 */
public interface CronDao {
	
	public List<TaskCron> queryByIp(String ip);
	
	public List<String> queryIdsByIp(String ip);
	
	public List<TaskCron> queryAllCron(int flag);
	
	public TaskCron queryById(String id);
	
	public int add(TaskCron cron);
	
	public int delete(String id);
	
	public int updateFlag(String id,int flag);
	
	public int insertResult(TaskResult result);
	
	public int deleteResult(String cronId);
	
	public TaskResult queryResult(String cronId);
}
