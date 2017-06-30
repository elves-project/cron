package cn.gyyx.elves.cron.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.gyyx.elves.cron.pojo.TaskCron;

/**
 * @ClassName: QuartzJobHandler
 * @Description: quartz job 控制器
 * @author East.F
 * @date 2016年5月25日 上午11:18:23
 */
public class QuartzJobHandler implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		TaskCron job = (TaskCron) context.getMergedJobDataMap().get("scheduleJob");
		JobTaskInvoker.invoke(job);
	}
}
