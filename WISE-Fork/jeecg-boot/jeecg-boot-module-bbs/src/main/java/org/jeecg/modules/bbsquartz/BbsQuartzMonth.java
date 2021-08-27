package org.jeecg.modules.bbsquartz;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.bbs.entity.BbsUserRecord;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Slf4j
@Component
public class BbsQuartzMonth implements Job {
    /**
     *初始化Bean   类加@Component
     */
    public static BbsQuartzMonth bbsQuartzMonth;
    @PostConstruct
    public void init() {
        bbsQuartzMonth = this;
    }
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;

    @Override
    //@AutoLog(value = "每月一次定时任务")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //log.info(String.format("每月一次定时任务。 时间:" + DateUtils.getTimestamp()));

        //当月区域切换次数
        bbsQuartzMonth.bbsUserRecordService.lambdaUpdate()
                .set(BbsUserRecord::getRegionSwitchCount,0)
                .update();
        System.out.println("每月一次定时任务！");
    }
}
