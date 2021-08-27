package org.jeecg.modules.bbs.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsInform;
import org.jeecg.modules.bbs.entity.BbsReply;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.mapper.BbsInformMapper;
import org.jeecg.modules.bbs.service.IBbsInformService;
import org.jeecg.modules.bbs.service.IBbsReplyService;
import org.jeecg.modules.bbs.service.IBbsTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 举报列表
 * @Author: jeecg-boot
 * @Date:   2021-03-03
 * @Version: V1.0
 */
@Service
public class BbsInformServiceImpl extends ServiceImpl<BbsInformMapper, BbsInform> implements IBbsInformService {
    @Autowired
    private IBbsTopicService bbsTopicService;
    @Autowired
    private IBbsInformService bbsInformService;
    @Autowired
    private IBbsReplyService bbsReplyService;

    @Override
    @Transactional
    public Result<?> informTopic(BbsInform bbsInform) {
        /**
         * 同一用户对一个贴子只能举报一次
         */
        BbsInform oneInform = bbsInformService.lambdaQuery().eq(BbsInform::getTopicId, bbsInform.getTopicId())
                .eq(BbsInform::getCreateBy, bbsInform.getCreateBy()).one();
        //如果用户没有举报过这个贴子
        if (null == oneInform) {
            //插入举报信息
            //UpdateWrapper<BbsInform> bbsInformUpdateWrapper = new UpdateWrapper<>();
            //bbsInformUpdateWrapper.set("topic_id", bbsInform.getTopicId())
            //        .set("user_id", bbsInform.getUserId())
            //        .eq("topic_id",bbsInform.getTopicId())
            //        .eq("user_id",bbsInform.getUserId());
            bbsInformService.save(bbsInform);
            //bbsInformService.saveOrUpdate(bbsInform, bbsInformUpdateWrapper);

            BbsTopic oneTopic = bbsTopicService.lambdaQuery().eq(BbsTopic::getId, bbsInform.getTopicId()).one();
            if (oneTopic.getInformCount() >= 1) {
                //如果贴子被举报次数大于2次，冻结贴子
                bbsTopicService.lambdaUpdate().eq(BbsTopic::getId, bbsInform.getTopicId())
                        .set(BbsTopic::getInformCount, oneTopic.getInformCount() + 1)
                        .set(BbsTopic::getStatus, 2)
                        .update();
            } else {
                //贴子举报人数+1
                bbsTopicService.lambdaUpdate().eq(BbsTopic::getId, bbsInform.getTopicId()).set(BbsTopic::getInformCount, oneTopic.getInformCount() + 1).update();
            }
            return Result.OK("已收到您的举报。");
        } else {
            //不能重复举报
            return Result.error("请勿重复举报！");
        }
    }

    @Override
    @Transactional
    public Result<?> informReply(BbsInform bbsInform) {
        /**
         * 同一用户对一个贴子只能举报一次
         */
        BbsInform oneInform = bbsInformService.lambdaQuery().eq(BbsInform::getReplyId, bbsInform.getReplyId())
                .eq(BbsInform::getCreateBy, bbsInform.getCreateBy()).one();

        //如果用户没有举报过这个贴子
        if (null == oneInform) {
            //插入举报信息
            //UpdateWrapper<BbsInform> bbsInformUpdateWrapper = new UpdateWrapper<>();
            //bbsInformUpdateWrapper.set("reply_id", bbsInform.getReplyId())
            //        .set("user_id", bbsInform.getUserId())
            //        .set("informed_user_id", bbsInform.getInformedUserId())
            //        .eq("reply_id",bbsInform.getReplyId())
            //        .eq("user_id",bbsInform.getUserId());
            bbsInformService.save(bbsInform);
            //bbsInformService.saveOrUpdate(bbsInform, bbsInformUpdateWrapper);

            BbsReply oneReply = bbsReplyService.lambdaQuery().eq(BbsReply::getId, bbsInform.getReplyId()).one();

            if (oneReply.getInformCount() >= 1) {
                //评论举报次数+1
                bbsReplyService.lambdaUpdate().eq(BbsReply::getId, bbsInform.getReplyId())
                        .set(BbsReply::getInformCount, oneReply.getInformCount() + 1)
                        .update();
            } else {
                //贴子举报人数+1
                bbsReplyService.lambdaUpdate().eq(BbsReply::getId, bbsInform.getReplyId()).set(BbsReply::getInformCount, oneReply.getInformCount() + 1).update();
            }
            return Result.OK("已收到您的举报。");
        } else {
            //不能重复举报
            return Result.error("请勿重复举报！");
        }
    }
}
