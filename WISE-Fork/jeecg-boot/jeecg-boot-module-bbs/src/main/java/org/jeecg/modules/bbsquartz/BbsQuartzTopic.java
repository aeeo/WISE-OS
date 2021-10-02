package org.jeecg.modules.bbsquartz;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.entity.BbsUserRecord;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.jeecg.modules.bbs.service.impl.BbsTopicServiceImpl;
import org.jeecg.modules.cache.BbsRedisUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
public class BbsQuartzTopic implements Job {
    /**
     * 初始化Bean   类加@Component
     */
    public static BbsQuartzTopic bbsQuartzDay;

    @PostConstruct
    public void init() {
        bbsQuartzDay = this;
    }

    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private BbsRedisUtils bbsRedisUtils;

    @Override
    //@AutoLog(value = "定时任务增加帖子浏览量")
    /**
     * 10分钟一次，每次1-10个浏览量
     * 1小时50个
     * 1天1200个
     * 1周7000个
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(String.format("定时任务增加帖子浏览量。"));
        int randomRange = 9;
        int expetHitCount = 7000;           //期望点击量
        int random = 0;
        Date date = new Date();
        if (date.getHours() > 8 && date.getHours() < 23) {
            random = new Random().nextInt(randomRange) + 1;
        }

        Set<String> allRank = bbsRedisUtils.getAllRank();
        Iterator<String> iterator = allRank.iterator();
        List<String> ranks = new ArrayList<>();
        while (iterator.hasNext()) {
            String rankKey = iterator.next();
            ranks.add(rankKey);
        }
        //获取榜单下排名前20的帖子
        List<String> rankTopic = bbsRedisUtils.getRankTopic(ranks, randomRange * 3);

        //随机限制
        int random1 = new Random().nextInt(random) + 1;
        int random2 = new Random().nextInt(random1) + 1;
        int random3 = new Random().nextInt(random2) + 1;
        int random4 = new Random().nextInt(random3);
        //所有帖子浏览量随机增加
        bbsRedisUtils.updateTopicHitCount(rankTopic, random,expetHitCount + random1 * random2 * random3 * random4);
    }
}