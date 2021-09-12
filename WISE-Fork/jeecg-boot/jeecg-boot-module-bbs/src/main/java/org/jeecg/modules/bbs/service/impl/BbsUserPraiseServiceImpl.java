package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.mapper.BbsUserPraiseMapper;
import org.jeecg.modules.bbs.service.IBbsUserPraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户点赞记录表
 * @Author: jeecg-boot
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Service
public class BbsUserPraiseServiceImpl extends ServiceImpl<BbsUserPraiseMapper, BbsUserPraise> implements IBbsUserPraiseService {
    @Autowired
    private IBbsUserPraiseService bbsUserPraiseService;
    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private BbsUserRecordServiceImpl bbsUserRecordService;
    @Autowired
    private BbsMessageBoardServiceImpl bbsMessageBoardService;
    @Autowired
    private BbsUserMessageServiceImpl bbsUserMessageService;
    @Autowired
    private BbsTopicImageServiceImpl bbsTopicImageService;
    @Autowired
    private BbsReplyServiceImpl bbsReplyService;
    @Autowired
    private BbsRegionServiceImpl bbsRegionService;
    @Resource
    private BbsUserPraiseMapper bbsUserPraiseMapper;

    @Override
    @Transactional
    public Result<?> clickPraise(String topicId, Boolean isPraise, String messageId) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if ((null == topicId || topicId.isEmpty()) && (null == messageId || messageId.isEmpty())) {
            return Result.error(1001, "点赞信息Id不能为空");
        }
        // 判断点赞数量是否超出限制
        // 用户记录
        BbsUserRecord bbsUserRecordOne = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();


        // 查询用户所在区域设置
        BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecordOne.getRegionCode()).one();
        if (bbsUserRecordOne.getDayPublishPraise() >= regionOne.getDayPublishPraise()) {
            return Result.error(1000, "今天已经点赞" + regionOne.getDayPublishPraise() + "次，超出限制，请等明天再次尝试！");
        } else if (bbsUserRecordOne.getWeekPublishPraise() >= regionOne.getWeekPublishPraise()) {
            return Result.error(1000, "本周已经点赞" + regionOne.getWeekPublishPraise() + "次，超出限制，请等下周再次尝试！");
        }
        //点赞贴子
        if (null != topicId) {
            /***
             * 1、如果isPraise==true，BbsUserPraise插入
             * 2、帖子点赞量+1
             * 3、用户记录 不存在就创建 麦子+1 用户点赞量+1
             * 4、贴子收到的赞 插入一条点赞信息
             *
             * 1、如果isPraise==false，否则，BbsUserPraise删除
             * 2、帖子点赞量-1
             * 3、用户记录 不存在就创建 麦子-1 用户点赞量-1
             * 4、贴子收到的赞 删除一条点赞信息
             */
            BbsUserPraise oneBbsUserPraise = bbsUserPraiseService.lambdaQuery().eq(BbsUserPraise::getCreateBy, sysUser.getUsername()).eq(BbsUserPraise::getTopicId, topicId).one();
            BbsTopic oneBbsTopic = bbsTopicService.lambdaQuery().eq(BbsTopic::getId, topicId).one();
            BbsUserRecord oneBbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
            BbsUserRecord beUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, oneBbsTopic.getCreateBy()).one();
            if (isPraise) {
                //1、如果one1！=null，BbsUserPraise插入点赞记录，否则，提示
                if (null == oneBbsUserPraise) {
                    BbsUserPraise bbsUserPraise = new BbsUserPraise();
                    bbsUserPraise.setCreateBy(sysUser.getUsername());
                    bbsUserPraise.setCreateTime(new Date());
                    bbsUserPraise.setTopicId(topicId);
                    bbsUserPraiseService.save(bbsUserPraise);
                } else {
                    return Result.error(1001, "已点赞，请勿重复点赞");
                }
                //2、帖子点赞量+1
                oneBbsTopic.setPraiseCount(oneBbsTopic.getPraiseCount() + 1);
                bbsTopicService.updateById(oneBbsTopic);
                //3、用户记录 不存在就创建 麦子+1 用户点赞量+1
                boolean b = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                        .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() + 1)
                        .set(BbsUserRecord::getPraiseCount, oneBbsUserRecord.getPraiseCount() + 1)
                        .set(BbsUserRecord::getDayPublishPraise, oneBbsUserRecord.getDayPublishPraise() + 1)
                        .set(BbsUserRecord::getWeekPublishPraise, oneBbsUserRecord.getWeekPublishPraise() + 1)
                        .update();
                //被点赞用户 陨石+2 被点赞量+1
                boolean b1 = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, oneBbsTopic.getCreateBy())
                        .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() + 2)
                        .set(BbsUserRecord::getBePraiseCount, beUserRecord.getBePraiseCount() + 1)
                        .update();

                //4、贴子收到赞 用户接收信息表 插入一条点赞信息
                BbsUserMessage bbsUserMessage = new BbsUserMessage();
                bbsUserMessage.setCreateBy(sysUser.getUsername());
                bbsUserMessage.setMessageType("2");
                bbsUserMessage.setReceiveUsername(oneBbsTopic.getCreateBy());
                bbsUserMessage.setTopicId(oneBbsTopic.getId());
                bbsUserMessage.setRegionCode(bbsUserRecordOne.getRegionCode());
                //封装一张贴子图片
                List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.lambdaQuery().eq(BbsTopicImage::getTopicId, oneBbsTopic.getId()).list();
                if (bbsTopicImageList.size() != 0) {
                    bbsUserMessage.setTopicImageUrl(bbsTopicImageList.get(0).getTopicImage());
                }
                bbsUserMessageService.saveOrUpdate(bbsUserMessage);
            } else {
                //1、如果isPraise==false，BbsUserPraise删除
                if (null == oneBbsUserPraise) {
                    return Result.error(1001, "未点赞，无法取消点赞");
                } else {
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("topic_id", topicId);
                    hashMap.put("create_by", sysUser.getUsername());
                    boolean b = bbsUserPraiseService.removeByMap(hashMap);
                    if (!b) {
                        return Result.error(1001, "无法取消点赞");
                    }
                }
                //2、帖子点赞量-1
                oneBbsTopic.setPraiseCount(oneBbsTopic.getPraiseCount() - 1);
                bbsTopicService.updateById(oneBbsTopic);
                //3、用户记录 陨石-1 用户点赞量-1 日点赞数量-1 周点赞数量-1 总点赞量-1
                boolean b = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                        .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() - 1)
                        .set(BbsUserRecord::getPraiseCount, oneBbsUserRecord.getPraiseCount() - 1)
                        .set(BbsUserRecord::getDayPublishPraise, oneBbsUserRecord.getDayPublishPraise() - 1)
                        .set(BbsUserRecord::getWeekPublishPraise, oneBbsUserRecord.getWeekPublishPraise() - 1)
                        .update();
                //被点赞用户 陨石-2 被点赞量-1
                boolean b1 = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, oneBbsTopic.getCreateBy())
                        .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() - 2)
                        .set(BbsUserRecord::getBePraiseCount, beUserRecord.getBePraiseCount() - 1)
                        .update();

                //4、贴子取消赞 删除一条点赞信息
                QueryWrapper<BbsUserMessage> userMessageQWrapper = new QueryWrapper<>();
                userMessageQWrapper.eq("topic_id", topicId);
                userMessageQWrapper.eq("create_by", sysUser.getUsername());
                userMessageQWrapper.eq("message_type", "2");
                bbsUserMessageService.remove(userMessageQWrapper);
            }
        } else if (null != messageId) {
            /***
             * 1、如果isPraise==true，BbsUserPraise插入
             * 2、帖子点赞量+1
             * 3、用户记录 不存在就创建 麦子+1 用户点赞量+1
             * 4、留言板收到的赞 插入一条点赞信息
             *
             * 1、如果isPraise==false，否则，BbsUserPraise删除
             * 2、帖子点赞量-1
             * 3、用户记录 不存在就创建 麦子-1 用户点赞量-1
             */
            BbsUserPraise oneBbsUserPraise = bbsUserPraiseService.lambdaQuery().eq(BbsUserPraise::getCreateBy, sysUser.getUsername()).eq(BbsUserPraise::getMessageId, messageId).one();
            BbsMessageBoard oneBbsMessageBoard = bbsMessageBoardService.lambdaQuery().eq(BbsMessageBoard::getId, messageId).one();
            BbsUserRecord oneBbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
            BbsUserRecord beUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, oneBbsMessageBoard.getCreateBy()).one();
            if (isPraise) {
                //1、如果one1！=null，BbsUserPraise插入点赞记录，否则，提示
                if (null == oneBbsUserPraise) {
                    BbsUserPraise bbsUserPraise = new BbsUserPraise();
                    bbsUserPraise.setCreateBy(sysUser.getUsername());
                    bbsUserPraise.setCreateTime(new Date());
                    bbsUserPraise.setMessageId(messageId);
                    bbsUserPraiseService.save(bbsUserPraise);
                } else {
                    return Result.error(1001, "已点赞，无法再次点赞");
                }
                //2、留言点赞量+1
                oneBbsMessageBoard.setPraiseCount(oneBbsMessageBoard.getPraiseCount() + 1);
                bbsMessageBoardService.updateById(oneBbsMessageBoard);
                //3、用户记录 不存在就创建 麦子+1 用户点赞量+1
                boolean b = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                        .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() + 1)
                        .set(BbsUserRecord::getPraiseCount, oneBbsUserRecord.getPraiseCount() + 1)
                        .set(BbsUserRecord::getDayPublishPraise, oneBbsUserRecord.getDayPublishPraise() + 1)
                        .set(BbsUserRecord::getWeekPublishPraise, oneBbsUserRecord.getWeekPublishPraise() + 1)
                        .update();
                //被点赞用户 陨石+2 被点赞量+1
                boolean b1 = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, oneBbsMessageBoard.getCreateBy())
                        .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() + 2)
                        .set(BbsUserRecord::getBePraiseCount, beUserRecord.getBePraiseCount() + 1)
                        .update();

                //4、留言收到赞 插入一条点赞信息
                BbsUserMessage bbsUserMessage = new BbsUserMessage();
                bbsUserMessage.setCreateBy(sysUser.getUsername());
                //消息类型 1留言被点赞 2信息被点赞 3信息被评论
                bbsUserMessage.setMessageType("1");
                bbsUserMessage.setReceiveUsername(oneBbsMessageBoard.getCreateBy());
                bbsUserMessage.setMessageBoardId(oneBbsMessageBoard.getId());
                bbsUserMessage.setReplyContent(oneBbsMessageBoard.getContent());
                bbsUserMessage.setRegionCode(bbsUserRecordOne.getRegionCode());
                bbsUserMessageService.saveOrUpdate(bbsUserMessage);
            } else {
                //1、如果isPraise==false，BbsUserPraise删除
                if (null == oneBbsUserPraise) {
                    return Result.error(1001, "未点赞，无法取消点赞");
                } else {
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("message_id", messageId);
                    hashMap.put("create_by", sysUser.getUsername());
                    boolean b = bbsUserPraiseService.removeByMap(hashMap);
                    if (!b) {
                        return Result.error(1001, "无法取消点赞");
                    }
                }
                //2、留言点赞量-1
                oneBbsMessageBoard.setPraiseCount(oneBbsMessageBoard.getPraiseCount() - 1);
                bbsMessageBoardService.updateById(oneBbsMessageBoard);
                //3、用户记录 麦子-1 用户点赞量-1
                boolean b = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                        .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() - 1)
                        .set(BbsUserRecord::getPraiseCount, oneBbsUserRecord.getPraiseCount() - 1)
                        .set(BbsUserRecord::getDayPublishPraise, oneBbsUserRecord.getDayPublishPraise() - 1)
                        .set(BbsUserRecord::getWeekPublishPraise, oneBbsUserRecord.getWeekPublishPraise() - 1)
                        .update();
                //被点赞用户 陨石-2 被点赞量-1
                boolean b1 = bbsUserRecordService.lambdaUpdate()
                        .eq(BbsUserRecord::getCreateBy, oneBbsMessageBoard.getCreateBy())
                        .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() - 2)
                        .set(BbsUserRecord::getBePraiseCount, beUserRecord.getBePraiseCount() - 1)
                        .update();

                //4、留言板取消赞 删除一条点赞信息
                QueryWrapper<BbsUserMessage> userMessageQWrapper = new QueryWrapper<>();
                userMessageQWrapper.eq("message_board_id", messageId);
                userMessageQWrapper.eq("create_by", sysUser.getUsername());
                userMessageQWrapper.eq("message_type", "1");
                bbsUserMessageService.remove(userMessageQWrapper);
            }
        }
        return Result.OK("成功");
    }

    /**
     * 评论点赞
     *
     * @param replyId
     * @param isPraise
     * @return
     */
    @Override
    @Transactional
    public Result<?> clickReplyPraise(String replyId, Boolean isPraise) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 判断点赞数量是否超出限制
        // 用户记录
        BbsUserRecord bbsUserRecordOne = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        // 查询用户所在区域设置
        BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecordOne.getRegionCode()).one();
        if (bbsUserRecordOne.getDayPublishPraise() >= regionOne.getDayPublishPraise()) {
            return Result.error(1000, "今天已经点赞" + regionOne.getDayPublishPraise() + "次，超出限制，请等明天再次尝试！");
        } else if (bbsUserRecordOne.getWeekPublishPraise() >= regionOne.getWeekPublishPraise()) {
            return Result.error(1000, "本周已经点赞" + regionOne.getWeekPublishPraise() + "次，超出限制，请等下周再次尝试！");
        }
        /***
         * 1、如果isPraise==true，BbsUserPraise插入
         * 2、评论点赞量+1
         * 3、用户记录 不存在就创建 麦子+1 用户点赞量+1
         * 4、贴子收到的赞 插入一条点赞信息
         *
         * 1、如果isPraise==false，否则，BbsUserPraise删除
         * 2、帖子点赞量-1
         * 3、用户记录 不存在就创建 麦子-1 用户点赞量-1
         * 4、贴子收到的赞 删除一条点赞信息
         */
        BbsUserPraise oneBbsUserPraise = bbsUserPraiseService.lambdaQuery().eq(BbsUserPraise::getCreateBy, sysUser.getUsername()).eq(BbsUserPraise::getReplyId, replyId).one();
        BbsReply oneBbsReply = bbsReplyService.lambdaQuery().eq(BbsReply::getId, replyId).one();
        BbsUserRecord oneBbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        BbsUserRecord beUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, oneBbsReply.getCreateBy()).one();
        if (isPraise) {
            //1、如果one1！=null，BbsUserPraise插入点赞记录，否则，提示
            if (null == oneBbsUserPraise) {
                BbsUserPraise bbsUserPraise = new BbsUserPraise();
                bbsUserPraise.setCreateBy(sysUser.getUsername());
                bbsUserPraise.setCreateTime(new Date());
                bbsUserPraise.setReplyId(replyId);
                bbsUserPraiseService.save(bbsUserPraise);
            } else {
                return Result.error(1001, "已点赞，无法再次点赞");
            }
            //2、评论点赞量+1
            oneBbsReply.setPraise(oneBbsReply.getPraise() + 1);
            bbsReplyService.updateById(oneBbsReply);
            //3、用户记录 麦子+1 用户点赞量+1
            bbsUserRecordService.lambdaUpdate()
                    .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                    .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() + 1)
                    .set(BbsUserRecord::getPraiseCount, oneBbsUserRecord.getPraiseCount() + 1)
                    .set(BbsUserRecord::getDayPublishPraise, oneBbsUserRecord.getDayPublishPraise() + 1)
                    .set(BbsUserRecord::getWeekPublishPraise, oneBbsUserRecord.getWeekPublishPraise() + 1)
                    .update();
            //被点赞用户 陨石+2 被点赞量+1
            boolean b1 = bbsUserRecordService.lambdaUpdate()
                    .eq(BbsUserRecord::getCreateBy, oneBbsReply.getCreateBy())
                    .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() + 2)
                    .set(BbsUserRecord::getBePraiseCount, beUserRecord.getBePraiseCount() + 1)
                    .update();

            //4、评论收到的赞 插入一条点赞信息
            //消息类型 1留言被点赞 2信息被点赞 3信息被评论 4评论收到赞
            BbsUserMessage bbsUserMessage = new BbsUserMessage();
            bbsUserMessage.setCreateBy(sysUser.getUsername());
            bbsUserMessage.setMessageType("4");
            bbsUserMessage.setReceiveUsername(oneBbsReply.getCreateBy());
            bbsUserMessage.setTopicId(oneBbsReply.getTopicId());
            bbsUserMessage.setReplyId(oneBbsReply.getId());
            bbsUserMessage.setReplyContent(oneBbsReply.getContent());
            bbsUserMessage.setRegionCode(regionOne.getRegionCode());
            //封装一张贴子图片
            List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.lambdaQuery().eq(BbsTopicImage::getTopicId, oneBbsReply.getTopicId()).list();
            if (bbsTopicImageList.size() != 0) {
                bbsUserMessage.setTopicImageUrl(bbsTopicImageList.get(0).getTopicImage());
            }
            bbsUserMessageService.saveOrUpdate(bbsUserMessage);
        } else {
            //1、如果isPraise==false，BbsUserPraise删除
            if (null == oneBbsUserPraise) {
                return Result.error(1001, "未点赞，无法取消点赞");
            } else {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("reply_id", replyId);
                hashMap.put("create_by", sysUser.getUsername());
                boolean b = bbsUserPraiseService.removeByMap(hashMap);
                if (!b) {
                    return Result.error(1001, "无法取消点赞");
                }
            }
            //2、帖子点赞量-1
            oneBbsReply.setPraise(oneBbsReply.getPraise() - 1);
            bbsReplyService.updateById(oneBbsReply);
            //3、用户记录 麦子-1 用户点赞量-1
            boolean b = bbsUserRecordService.lambdaUpdate()
                    .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                    .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() - 1)
                    .set(BbsUserRecord::getPraiseCount, oneBbsUserRecord.getPraiseCount() - 1)
                    .set(BbsUserRecord::getDayPublishPraise, oneBbsUserRecord.getDayPublishPraise() - 1)
                    .set(BbsUserRecord::getWeekPublishPraise, oneBbsUserRecord.getWeekPublishPraise() - 1)
                    .update();

            //被点赞用户 陨石-2 被点赞量-1
            boolean b1 = bbsUserRecordService.lambdaUpdate()
                    .eq(BbsUserRecord::getCreateBy, oneBbsReply.getCreateBy())
                    .set(BbsUserRecord::getStoneCount, beUserRecord.getStoneCount() - 2)
                    .set(BbsUserRecord::getBePraiseCount, beUserRecord.getBePraiseCount() - 1)
                    .update();

            //4、评论取消赞 删除一条点赞信息
            QueryWrapper<BbsUserMessage> userMessageQWrapper = new QueryWrapper<>();
            userMessageQWrapper.eq("reply_id", replyId);
            userMessageQWrapper.eq("create_by", sysUser.getUsername());
            userMessageQWrapper.eq("message_type", "4");
            bbsUserMessageService.remove(userMessageQWrapper);
        }
        return Result.OK("成功");
    }

    @Override
    public int queryTopicPraiseCount(String sysOrgCode) {
        return bbsUserPraiseMapper.queryTopicPraiseCount(sysOrgCode);
    }

    @Override
    public int queryMessageBoardPraiseCount(String sysOrgCode) {
        return bbsUserPraiseService.lambdaQuery().eq(BbsUserPraise::getSysOrgCode, sysOrgCode).isNotNull(BbsUserPraise::getMessageId).count();
    }
}
