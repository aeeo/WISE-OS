package org.jeecg.modules.bbsquartz;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.jeecg.modules.bbs.service.impl.BbsTopicServiceImpl;
import org.jeecg.modules.cache.BbsRedisUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
public class BbsQuartzTopicToSqlFromRedis implements Job {
    /**
     * 初始化Bean   类加@Component
     */
    public static BbsQuartzTopicToSqlFromRedis bbsQuartzDay;

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
    /**
     * 定时任务，从redis读取数据持久化到数据库
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(String.format("持久化Redis帖子到数据库。"));

        Set<String> allTopic = bbsRedisUtils.getAllTopic();
        Iterator<String> iterator = allTopic.iterator();
        List<String> topicIds = new ArrayList<>();
        while (iterator.hasNext()) {
            String topicId = iterator.next();
            topicIds.add(topicId);
        }

        //所有帖子持久化
        List<BbsTopicFullDto> allTopicList = bbsRedisUtils.getAllTopic(topicIds);
        for (BbsTopicFullDto bbsTopicFullDtoItem : allTopicList) {
            BbsTopic topic = new BbsTopic();
            BeanUtils.copyProperties(bbsTopicFullDtoItem,topic);
            bbsTopicService.updataTopic(topic, bbsTopicFullDtoItem.getBbsTopicImageList(), bbsTopicFullDtoItem.getBbsTopicTagList(), bbsTopicFullDtoItem.getBbsTopicLinkList());
        }
    }
}