package cn.gyyx.elves.cron.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gyyx.elves.cron.dao.CronDao;
import cn.gyyx.elves.cron.job.JobTaskManager;
import cn.gyyx.elves.cron.pojo.TaskCron;
import cn.gyyx.elves.cron.pojo.TaskResult;
import cn.gyyx.elves.cron.service.CronService;
import cn.gyyx.elves.util.DateUtils;
import cn.gyyx.elves.util.ExceptionUtil;
import cn.gyyx.elves.util.SecurityUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @ClassName: CronServiceImpl
 * @Description: Cron模块服务接口实现类
 * @author East.F
 * @date 2016年11月7日 上午9:30:21
 */
@Service("elvesConsumerService")
public class CronServiceImpl implements CronService{

	private static final Logger LOG=Logger.getLogger(CronServiceImpl.class);
	
	@Autowired
	private CronDao cronDao;
	
	@Autowired
	private JobTaskManager jobTaskManager;
	
	@Override
	public Map<String, Object> createCron(Map<String, Object> params){
		LOG.info("cron module reveive createCron job ,params : "+params);
		Map<String, Object> rs=new HashMap<String, Object>();
		String cronId=SecurityUtil.getUniqueKey();
		try{
			//必传参数
			String ip = params.get("ip")==null?"":params.get("ip").toString().trim();
			String app = params.get("app")==null?"":params.get("app").toString().trim();
			String func = params.get("func")==null?"":params.get("func").toString().trim();
			String mode = params.get("mode")==null?"":params.get("mode").toString().trim();
			String rule = params.get("rule")==null?"":params.get("rule").toString().trim();
			//非必传参数
			String param = params.get("param")==null?"":params.get("param").toString().trim();
			String proxy = params.get("proxy")==null?"":params.get("proxy").toString().trim();
			int timeout = params.get("timeout")==null?60:(int)params.get("timeout");
			
			TaskCron cron=new TaskCron();
			cron.setId(cronId);
			cron.setIp(ip);
			cron.setApp(app);
			cron.setFunc(func);
			cron.setMode(mode);
			cron.setParam(param);
			cron.setProxy(proxy);
			cron.setRule(rule);
			cron.setTimeout(timeout);
			cron.setFlag(0);
			cron.setCreateTime(DateUtils.currentTimestamp2String(""));
			int flag = cronDao.add(cron);
			LOG.debug("create cron flag :"+flag);
			rs.put("flag", "true");
			rs.put("error","");
			rs.put("id", cronId);
		}catch(Exception e){
			String error =ExceptionUtil.getStackTraceAsString(e);
			LOG.error("create cron fail, error : "+error);
			rs.put("flag", "false");
			rs.put("error","[500]"+error);
		}
		return rs;
	}

	@Override
	@Transactional
	public Map<String, Object> startCron(Map<String, Object> params){
		LOG.info("cron module reveive start cron job ,params:"+params);
		Map<String, Object> rs=new HashMap<String, Object>();
		String id = params.get("id")==null?"":params.get("id").toString().trim();
		try{
			TaskCron cron=cronDao.queryById(id);
			if(null==cron){
				rs.put("flag", "false");
				rs.put("error","[411.1]Not Find Cron Job To Operate");
				return rs;
			}
			
			if(cron.getFlag()>0){
				rs.put("flag", "true");
				rs.put("error","");
				return rs;
			}
			
			int flag = cronDao.updateFlag(id,1);
			LOG.debug("start cron flag updateFlag :"+flag);
			boolean result = jobTaskManager.addJob(cron);
			LOG.debug("job manager add job result :"+result);
			rs.put("flag", "true");
			rs.put("error","");
			return rs;
		}catch(Exception e){
			String error =ExceptionUtil.getStackTraceAsString(e);
			LOG.error("start cron fail, error : "+error);
			rs.put("flag", "false");
			rs.put("error","[500]"+error);
		}
		return rs;
	}

	@Override
	public Map<String, Object> stopCron(Map<String, Object> params){
		LOG.info("cron module reveive stop cron job ,params:"+params);
		Map<String, Object> rs=new HashMap<String, Object>();
		String id = params.get("id")==null?"":params.get("id").toString().trim();
		try{
			TaskCron cron=cronDao.queryById(id);
			if(null==cron){
				rs.put("flag", "false");
				rs.put("error","[411.1]Not Find Cron Job To Operate");
				return rs;
			}
			
			if(cron.getFlag()==0){
				rs.put("flag", "true");
				rs.put("error","");
				return rs;
			}
			
			int flag = cronDao.updateFlag(id,0);
			LOG.debug("stop cron flag updateFlag :"+flag);
			boolean result = jobTaskManager.deleteJob(id);
			LOG.debug("job manager delete job result :"+result);
			rs.put("flag", "true");
			rs.put("error","");
			return rs;
		}catch(Exception e){
			String error =ExceptionUtil.getStackTraceAsString(e);
			LOG.error("stop cron fail, error : "+error);
			rs.put("flag", "false");
			rs.put("error","[500]"+error);
		}
		return rs;
	}
	
	@Override
	public Map<String, Object> deleteCron(Map<String, Object> params){
		LOG.info("cron module reveive delete cron job ,params:"+params);
		Map<String, Object> rs=new HashMap<String, Object>();
		String id = params.get("id")==null?"":params.get("id").toString().trim();
		try{
			TaskCron cron=cronDao.queryById(id);
			if(null==cron){
				rs.put("flag", "false");
				rs.put("error","[411.1]Not Find Cron Job To Operate");
				return rs;
			}
			
			//删除任务，删除任务结果，删除quartz任务
			int flag = cronDao.delete(id);
			LOG.debug("delete cron flag updateFlag :"+flag);
			int resultFlag = cronDao.deleteResult(id);
			LOG.debug("delete cronResult flag updateFlag :"+resultFlag);
			boolean result = jobTaskManager.deleteJob(id);
			LOG.debug("job manager delete job result :"+result);
			
			rs.put("flag", "true");
			rs.put("error","");
			return rs;
		}catch(Exception e){
			String error =ExceptionUtil.getStackTraceAsString(e);
			LOG.error("delete cron fail, error : "+error);
			rs.put("flag", "false");
			rs.put("error","[500]"+error);
		}
		return rs;
		
	}
	
	@Override
	public Map<String, Object> cronList(Map<String, Object> params){
		LOG.info("cron module reveive cronList job ,params:"+params);
		Map<String, Object> rs=new HashMap<String, Object>();
		String ip=params.get("ip")==null?"":params.get("ip").toString().trim();
		try{
			List<String> list=cronDao.queryIdsByIp(ip);
			rs.put("flag", "true");
			rs.put("error","");
			rs.put("result", list==null?new ArrayList<String>():list);
		}catch(Exception e){
			String error =ExceptionUtil.getStackTraceAsString(e);
			LOG.error("query cronList fail, error : "+error);
			rs.put("flag", "false");
			rs.put("error","[500]"+error);
		}
		return rs;
	}
	
	@Override
	public Map<String, Object> cronDetail(Map<String, Object> params) {
		LOG.info("cron module reveive cronDetail job ,params:"+params);
		Map<String, Object> rs=new HashMap<String, Object>();
		String id=params.get("id")==null?"":params.get("id").toString().trim();
		try {
			TaskCron cron=cronDao.queryById(id);
			if(null==cron){
				rs.put("flag", "false");
				rs.put("error","[411.1]Not Find Cron Job To Operate");
				return rs;
			}
			rs.put("ip",cron.getIp());
			rs.put("app", cron.getApp());
			rs.put("func", cron.getFunc());
			rs.put("param", cron.getParam()==null?"":cron.getParam());
			rs.put("mode",cron.getMode());
			rs.put("proxy", cron.getProxy()==null?"":cron.getProxy());
			rs.put("timeout",cron.getTimeout());
			rs.put("rule",cron.getRule());
			rs.put("cron_flag",cron.getFlag());
			rs.put("create_time",cron.getCreateTime()==null?"":cron.getCreateTime().substring(0,cron.getCreateTime().length()-2));
			TaskResult result = cronDao.queryResult(id);
			if(null==result){
				rs.put("last_exec_time","");
				rs.put("last_exec_result",new HashMap<String,Object>());
			}else{
				Map<String,Object> workerData = new HashMap<String,Object>();
				workerData.put("worker_flag", result.getWorkerFlag());
				workerData.put("worker_message", result.getWorkerMessage());
				workerData.put("worker_costtime", result.getWorkerCosttime());
				
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("flag", result.getFlag());
				data.put("error", result.getError()==null?"":result.getError());
				data.put("result", workerData);
				
				rs.put("last_exec_time",result.getEndTime()==null?"":result.getEndTime().substring(0,result.getEndTime().length()-2));
				rs.put("last_exec_result",data);
			}
			return rs;
		} catch (Exception e) {
			String error =ExceptionUtil.getStackTraceAsString(e);
			LOG.error("query cronDetail fail, error : "+error);
			rs.put("flag", "false");
			rs.put("error","[500]"+error);
			return rs;
		}
	}
	
	@Override
	public Map<String,Object> cronResult(Map<String, Object> params){
		LOG.info("cron module reveive cronResult job ,params:"+params);
		String flag = params.get("flag")==null?"":params.get("flag").toString().trim();
		String error = params.get("error")==null?"":params.get("error").toString().trim();
		String result = params.get("result")==null?"":params.get("result").toString().trim();
		try {
			if(StringUtils.isBlank(flag)||StringUtils.isBlank(result)){
				LOG.error("scheduler send cronResult some paramater is empty,flag:"+flag+",result:"+result);
			}
			if("true".equals(flag)||"false".equals(flag)){
				Map<String, Object> data =JSON.parseObject(result,new TypeReference<Map<String, Object>>(){});
				if(null!=data){
					String id = data.get("id")==null?"":data.get("id").toString().trim();
					String workerFlag = data.get("worker_flag")==null?"":data.get("worker_flag").toString().trim();
					String workerMessage = data.get("worker_message")==null?"":data.get("worker_message").toString().trim();
					String workerCosttime = data.get("worker_costtime")==null?"":data.get("worker_costtime").toString().trim();
					if(StringUtils.isBlank(id)||StringUtils.isBlank(workerMessage)||StringUtils.isBlank(workerCosttime)){
						LOG.error("scheduler send cronResult some paramater is empty,id:"+id+",workerMessage:"+workerMessage+",workerCosttime:"+workerCosttime);
					}
					if("0".equals(workerFlag)||"1".equals(workerFlag)||"-1".equals(workerFlag)){
						TaskResult taskResult = new TaskResult();
						taskResult.setCronId(id);
						taskResult.setError(error);
						taskResult.setFlag(flag);
						taskResult.setWorkerFlag(Integer.parseInt(workerFlag));
						taskResult.setWorkerCosttime(Integer.parseInt(workerCosttime));
						taskResult.setWorkerMessage(workerMessage);
						taskResult.setEndTime(DateUtils.currentTimestamp2String(DateUtils.DEFAULT_DATETIME_FORMAT));
						//删除之前的结果，保存最后一次执行结果
						int deleteFlag = cronDao.deleteResult(id);
						int insertFlag = cronDao.insertResult(taskResult);
						LOG.debug("delete before result and inset new result,deleteFlag:"+deleteFlag+",insertFlag"+insertFlag);
					}else{
						LOG.error("scheduler send cronResult workerFlag paramater error:"+workerFlag);
					}
				}
			}else{
				LOG.error("scheduler send cronResult flag paramater error:"+flag);
			}
		} catch (Exception e) {
			LOG.error("query cronResult fail, error : "+ExceptionUtil.getStackTraceAsString(e));
		}
		return null;
	}
}
