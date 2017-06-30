# cron #

## 1.简介 ##

Cron 组件基于Quartz框架实现的，负责管理Agent上的定时任务。


## 2.Quart介绍 ##

Cron 组件,各种计划任务的调度基于Quart框架实现（官方网站：<a>http://www.quartz-scheduler.org</a>）

Quartz是一套非常高效的轻量级框架，设计的核心类包括 Scheduler, Job 以及 Trigger。其中，Job 负责定义需要执行的任务，Trigger 负责设置调度策略，Scheduler 将二者组装在一起，并触发任务开始执行，在开发上十分的方便快捷，而且有很高执行效率。

这里提供一下Quartz的cron规则语法，如果想对Quartz有深入的了解，请自己查询相关资料学习。
<table class="table table-bordered table-striped table-condensed">
	<tr>
		<th>位置</th>
		<th>时间域</th>
		<th>允许值</th>
		<th>特殊值</th>
	</tr>
	<tr>
		<td>1</td>
		<td>秒</td>
		<td>0-59</td>
		<td>, - * /</td>
	</tr>
	<tr>
		<td>2</td>
		<td>分钟</td>
		<td>0-59</td>
		<td>, - * /</td>
	</tr>
	<tr>
		<td>3</td>
		<td>小时</td>
		<td>0-23</td>
		<td>, - * /</td>
	</tr>
	<tr>
		<td>4</td>
		<td>日期</td>
		<td>1-31</td>
		<td>, - * ? / L W C</td>
	</tr>
	<tr>
		<td>5</td>
		<td>月份</td>
		<td>1-12</td>
		<td>, - * /</td>
	</tr>
	<tr>
		<td>6</td>
		<td>星期</td>
		<td>1-7</td>
		<td>, - * ? / L C #</td>
	</tr>
	<tr>
		<td>7</td>
		<td>年份（可选）</td>
		<td>1970－2099</td>
		<td>, - * /</td>
	</tr>
</table>

## 3.组件服务 ##


### 3.1 getCron 根据AgentIp获取计划任务###

	接收

	{
		"mqkey":"openapi.cron.getCron.40499CCA100F214G",
		"mqtype":"call",
		"agentip":"192.168.6.116"
	}
	
	回复
	{
		"mqkey":"cron.openapi.getCron.40499CCA100F214G",
		"cron_list":[
			{
		        "id" : "BF0EE718FCC41307",
		        "agent_ip" :"192.168.1.1",
		        "mode" :"ssp",
		        "app" : "testapp",
		        "func" :"mod1",
		        "param" : "",
		        "timeout" :5000,
		        "proxy" : "test",
		        "rule" : "0/5 * * * * ?",
		        "flag" : 0,
		        "create_time":"2016-06-21 20:14:42"
		    },
			...
		]

	}       


### 3.2 createCron 添加计划任务###

	接收
	{
		"mqkey":"openapi.cron.createCron.40499CCA100F214",
		"mqtype":"cast",
		"id":"88499CCA100F214"
		"agent_ip":"192.168.6.116",
		"mode":"sap",
		"app":"testApp",
		"func":"test',
		"rule":"0/60 * * * * ?",
		"param":"",
		"timeout":0,
		"proxy":""
	}
	
	
### 3.3 startCron 启动计划任务 ###

	接收
	{
		"mqkey":"openapi.cron.startCron.40499CCA100F214",
		"mqtype":"cast",
		"id":"88499CCA100F214"
	}
	
### 3.4 stopCron 暂停计划任务###

	接收
	{
		"mqkey":"openapi.cron.stopCron.40499CCA100F214",
		"mqtype":"cast",
		"id":"88499CCA100F214"
	}
	
### 3.5 delCron 删除计划任务###

	接收
	{
		"mqkey":"openapi.cron.delCron.40499CCA100F214",
		"mqtype":"cast",
		"id":"88499CCA100F214"
	}
	

## 4.数据库设计 ##

为了避免Cron组件重启后，之前加载的计划任务不丢失，故将计划任务存储到MySql中。

### 物理模型###

![](/docs/cron.png)

### Sql语句 ###

	CREATE TABLE `task_cron` (
	  `id` varchar(16) NOT NULL COMMENT '主键Id',
	  `agent_ip` varchar(15) NOT NULL COMMENT '客户端IP',
	  `mode` enum('sap','sanp') NOT NULL COMMENT '模式(sap,sanp)',
	  `app` varchar(32) NOT NULL COMMENT '模块',
	  `func` varchar(32) NOT NULL COMMENT '方法',
	  `param` text COMMENT '参数',
	  `timeout` int(11) DEFAULT '0' COMMENT '超时时间',
	  `proxy` varchar(15) DEFAULT NULL COMMENT '代理器',
	  `rule` varchar(30) NOT NULL COMMENT '规则(quartz表达式)',
	  `flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态(0:暂停,1:正常)',
	  `create_time` datetime NOT NULL COMMENT '任务接收时间',
	  PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计划任务表'




