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
public class BbsQuartzWeek implements Job {
    /**
     *初始化Bean   类加@Component
     */
    public static BbsQuartzWeek bbsQuartzWeek;
    @PostConstruct
    public void init() {
        bbsQuartzWeek = this;
    }
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;

    @Override
    //@AutoLog(value = "每周一次定时任务")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //log.info(String.format("每周一次定时任务。 时间:" + DateUtils.getTimestamp()));

        //本周内发布贴子数
        //本周内发布留言次数
        //本周内点赞次数
        //本周内评论次数
        bbsQuartzWeek.bbsUserRecordService.lambdaUpdate()
                .set(BbsUserRecord::getDayPublishTopic, 0)
                .set(BbsUserRecord::getDayPublishPraise,0)
                .set(BbsUserRecord::getDayPublishReply, 0)
                .set(BbsUserRecord::getDayPublishMessage,0)
                .update();

        System.out.println("每周一次定时任务！");
    }




}
