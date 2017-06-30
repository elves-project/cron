package cn.gyyx.elves.cron.job;

import java.util.HashMap;

import org.apache.log4j.Logger;

import cn.gyyx.elves.cron.pojo.TaskCron;
import cn.gyyx.elves.util.ExceptionUtil;
import cn.gyyx.elves.util.SecurityUtil;
import cn.gyyx.elves.util.SpringUtil;
import cn.gyyx.elves.util.mq.MessageProducer;

import com.alibaba.fastjson.JSON;

/**
 * @ClassName: JobTaskInvoker
 * @Description: job定时执行，代理业务处理类
 * @author East.F
 * @date 2016年5月24日 下午3:55:28
 */
public class JobTaskInvoker {

	private static final Logger LOG=Logger.getLogger(JobTaskInvoker.class);
	
	/**
	 * @Title: invokMethod
	 * @Description: job定时执行，代理业务处理类
	 * @param job 设定文件
	 * @return void    返回类型
	 */
	public static void invoke(TaskCron job){
		LOG.info("Job invoker run,Id:"+job.getId());
		try{
			MessageProducer producer=SpringUtil.getBean(MessageProducer.class);
			//发送 cron.scheduler 消息到mq
			HashMap<String, Object> body = new HashMap<String, Object>();
			body.put("id", job.getId());
			body.put("ip", job.getIp());
			body.put("mode", job.getMode());
			body.put("app", job.getApp());
			body.put("func", job.getFunc());
			body.put("type", "cron");
			
			body.put("proxy", job.getProxy());
			body.put("param", job.getParam());
			body.put("timeout", job.getTimeout());
			
			HashMap<String, Object> message = new HashMap<String, Object>();
			message.put("mqkey", "cron.scheduler.asyncJob");
			message.put("mqtype", "cast");
			message.put("mqbody", body);
			
			producer.cast("cron.scheduler",JSON.toJSONString(message));
		}catch(Exception e){
			LOG.error("Job invoker exception:"+ExceptionUtil.getStackTraceAsString(e));
		}
	}
}
