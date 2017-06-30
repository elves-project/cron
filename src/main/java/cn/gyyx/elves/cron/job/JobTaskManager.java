package cn.gyyx.elves.cron.job;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import cn.gyyx.elves.cron.pojo.TaskCron;

/**
 * @ClassName: JobTaskManager
 * @Description: quartz 计划任务管理管理类（job 增加、删除）
 * @author East.F
 * @date 2016年5月24日 下午3:01:10
 */
@Component
public class JobTaskManager{

	private static final Logger LOG=Logger.getLogger(JobTaskManager.class);
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	/**
	 * @Title: addJob
	 * @Description: 添加job任务
	 * @param job
	 * @return boolean    返回类型
	 * @throws SchedulerException 
	 */
	public boolean addJob(TaskCron job) throws SchedulerException{
		if (job == null ) {
			return false;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		//Scheduler.DEFAULT_GROUP 默认分组
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getId(), Scheduler.DEFAULT_GROUP);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在，创建一个
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(QuartzJobHandler.class).withIdentity(job.getId(), Scheduler.DEFAULT_GROUP).build();
			jobDetail.getJobDataMap().put("scheduleJob", job);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getRule());
			trigger = TriggerBuilder.newTrigger().withIdentity(job.getId(), Scheduler.DEFAULT_GROUP).withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
			return true;
		}
		return false;
		
	}

	/**
	 * @Title: deleteJob
	 * @Description: 删除job任务
	 * @param jobId
	 * @throws SchedulerException 设定文件
	 * @return boolean    返回类型
	 */
	public boolean deleteJob(String jobId) throws SchedulerException{
		LOG.info("JobTaskManager deleteJob,jobId:"+jobId);
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobId, Scheduler.DEFAULT_GROUP);
		scheduler.deleteJob(jobKey);
		return true;
	}
}
