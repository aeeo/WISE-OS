package org.jeecg.modules.bbs.quartz;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 示例不带参定时任务
 * 
 * @author Scott
 */
@Slf4j
public class OnlinePeople implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	   log.info(String.format(" Jeecg-Boot 普通定时任务OnlinePeople !  时间:" + DateUtils.getTimestamp()));

	}
}
