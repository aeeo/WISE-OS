package org.jeecg.modules.bbs.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.entity.BbsUserRecord;
import org.jeecg.modules.bbs.entity.BbsUserStar;
import org.jeecg.modules.bbs.mapper.BbsUserStarMapper;
import org.jeecg.modules.bbs.service.IBbsTopicImageService;
import org.jeecg.modules.bbs.service.IBbsTopicTagService;
import org.jeecg.modules.bbs.service.IBbsUserStarService;
import org.jeecg.modules.cache.BbsRedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 用户收藏
 * @Author: jeecg-boot
 * @Date:   2021-01-12
 * @Version: V1.0
 */
@Service
public class BbsUserStarServiceImpl extends ServiceImpl<BbsUserStarMapper, BbsUserStar> implements IBbsUserStarService {
    @Autowired
    private IBbsUserStarService bbsUserStarService;
    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private BbsUserRecordServiceImpl bbsUserRecordService;
    @Autowired
    private BbsRedisUtils bbsRedisUtils;

    @Override
    @Transactional
    public Result<?> clickStar(String username,String topicId,Boolean isStar) {
        /***
         * 1、如果isStar==true，BbsUserStar插入
         * 2、帖子收藏量+1
         * 3、用户记录 不存在就创建 麦子+5 用户收藏量+1
         *
         * 1、如果isStar==false，否则，BbsUserStar删除
         * 2、帖子收藏量-1
         * 3、用户记录 不存在就创建 麦子-5 用户收藏量-1
         */
        BbsUserStar oneBbsUserStar = bbsUserStarService.lambdaQuery().eq(BbsUserStar::getCreateBy, username).eq(BbsUserStar::getTopicId, topicId).one();
        BbsTopic oneBbsTopic = bbsTopicService.lambdaQuery().eq(BbsTopic::getId, topicId).one();
        BbsUserRecord oneBbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, username).one();
        BbsUserRecord beUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, oneBbsTopic.getCreateBy()).one();
        if (isStar) {
            //1、如果one1==null，BbsUserStar插入收藏记录，否则，提示
            if (null == oneBbsUserStar) {
                BbsUserStar bbsUserStar = new BbsUserStar();
                bbsUserStar.setCreateBy(username);
                bbsUserStar.setCreateTime(new Date());
                bbsUserStar.setTopicId(topicId);
                bbsUserStarService.save(bbsUserStar);
            } else {
                return Result.error("已收藏，无法再次收藏");
            }
            //2、帖子收藏量+1
            oneBbsTopic.setStarCount(oneBbsTopic.getStarCount() + 1);
            bbsTopicService.updateById(oneBbsTopic);
            List<String> topicIds = new ArrayList<>();
            topicIds.add(oneBbsTopic.getId());
            bbsRedisUtils.updateTopicStarCount(topicIds,1);

            //3、用户记录 麦子+5 用户收藏量+1
            bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, username)
                    .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() + 5)
                    .set(BbsUserRecord::getStarCount, oneBbsUserRecord.getStarCount() + 1).update();

            //被收藏用户记录 麦子+20 被收藏量+1
            bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, oneBbsTopic.getCreateBy())
                    .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() + 20)
                    .set(BbsUserRecord::getBeStarCount, beUserRecord.getBeStarCount() + 1).update();
        } else {
            //1、如果isStar==false，BbsUserStar删除
            if (null == oneBbsUserStar) {
                return Result.error("未收藏，无法取消收藏");
            } else {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("topic_id", topicId);
                hashMap.put("create_by", username);
                boolean b = bbsUserStarService.removeByMap(hashMap);
                if (!b) {
                    return Result.error("无法取消收藏");
                }
            }
            //2、帖子收藏量-1
            oneBbsTopic.setStarCount(oneBbsTopic.getStarCount() - 1);
            bbsTopicService.updateById(oneBbsTopic);
            List<String> topicIds = new ArrayList<>();
            topicIds.add(oneBbsTopic.getId());
            bbsRedisUtils.updateTopicStarCount(topicIds,-1);

            //3、用户记录 麦子-5 用户收藏量-1
            boolean b = bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, username)
                    .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() - 5)
                    .set(BbsUserRecord::getStarCount, oneBbsUserRecord.getStarCount() - 1).update();

            //被收藏用户记录 麦子-20 被收藏量-1
            bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, oneBbsTopic.getCreateBy())
                    .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() - 20)
                    .set(BbsUserRecord::getBeStarCount, beUserRecord.getBeStarCount() - 1).update();
        }
        return Result.OK("成功！");
    }
}
