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
public class BbsQuartzDay implements Job {
    /**
     *初始化Bean   类加@Component
     */
    public static BbsQuartzDay bbsQuartzDay;
    @PostConstruct
    public void init() {
        bbsQuartzDay = this;
    }

    @Autowired
    private IBbsUserRecordService bbsUserRecordService;

    @Override
    //@AutoLog(value = "每天一次定时任务")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //log.info(String.format("每天一次定时任务。 时间:" + DateUtils.getTimestamp()));
        //当天发布贴子数
        //当天发布留言次数
        //当天点赞次数
        //当天评论次数
        bbsQuartzDay.bbsUserRecordService.lambdaUpdate()
                .set(BbsUserRecord::getDayPublishTopic, 0)
                .set(BbsUserRecord::getDayPublishPraise,0)
                .set(BbsUserRecord::getDayPublishReply, 0)
                .set(BbsUserRecord::getDayPublishMessage,0)
                .update();
        System.out.println("每天一次定时任务！");
    }



}