package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oss.QiNiuUtil;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.mapper.*;
import org.jeecg.modules.bbs.service.*;
import org.jeecg.modules.cache.BbsRedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
@Service
public class BbsTopicServiceImpl extends ServiceImpl<BbsTopicMapper, BbsTopic> implements IBbsTopicService {

    @Autowired
    private BbsReplyServiceImpl bbsReplyServiceImpl;
    @Resource
    private BbsTopicMapper bbsTopicMapper;
    @Resource
    private BbsTopicFullDtoMapper bbsTopicFullDtoMapper;
    @Resource
    private BbsTopicImageMapper bbsTopicImageMapper;
    @Resource
    private BbsTopicTagMapper bbsTopicTagMapper;
    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private IBbsTopicService bbsTopicServiceService;
    @Autowired
    private IBbsTopicFullDtoService bbsTopicFullDtoService;
    @Autowired
    private IBbsTopicImageService bbsTopicImageService;
    @Autowired
    private IBbsTopicTagService bbsTopicTagService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private BbsUserPraiseServiceImpl bbsUserPraiseServiceImpl;
    @Autowired
    private BbsUserStarServiceImpl bbsUserStarService;
    @Autowired
    private BbsTopicLinkMapper bbsTopicLinkMapper;
    @Autowired
    private BbsClassServiceImpl bbsClassService;
    @Autowired
    private BbsRegionServiceImpl bbsRegionService;
    @Autowired
    private IBbsUserTopicClickService bbsUserTopicClickService;
    @Autowired
    private IBbsUserPraiseService bbsUserPraiseService;
    @Autowired
    private IBbsReplyService bbsReplyService;
    @Autowired
    private IBbsUserMessageService bbsUserMessageService;
    @Autowired
    private IBbsSysMessageService bbsSysMessageService;
    @Autowired
    private IBbsUserSysMessageService bbsUserSysMessageService;
    @Autowired
    private IBbsInformService bbsInformService;
    @Autowired
    private BbsRedisUtils bbsRedisUtils;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public void saveMain(BbsTopic bbsTopic, List<BbsTopicImage> bbsTopicImageList, List<BbsTopicTag> bbsTopicTagList, List<BbsTopicLink> bbsTopicLinkList) {
        bbsTopicMapper.insert(bbsTopic);

        if (bbsTopicImageList != null && bbsTopicImageList.size() > 0) {
            for (BbsTopicImage entity : bbsTopicImageList) {
                //外键设置
                entity.setTopicId(bbsTopic.getId());
                bbsTopicImageMapper.insert(entity);
            }
        }
        if (bbsTopicTagList != null && bbsTopicTagList.size() > 0) {
            for (BbsTopicTag entity : bbsTopicTagList) {
                //外键设置
                entity.setTopicId(bbsTopic.getId());
                bbsTopicTagMapper.insert(entity);
            }
        }
        if (bbsTopicLinkList != null && bbsTopicLinkList.size() > 0) {
            for (BbsTopicLink entity : bbsTopicLinkList) {
                //外键设置
                entity.setTopicId(bbsTopic.getId());
                bbsTopicLinkMapper.insert(entity);
            }
        }

        //加入redis           redis中存的是BbsTopicFullDto，但本质上和BbsTopicPage一样
        bbsRedisUtils.addTopic(bbsTopicFullDtoService.queryTopicFullDtoByIdFromSql(bbsTopic.getId()));
    }

    @Override
    public void updateMain(BbsTopic bbsTopic, List<BbsTopicImage> bbsTopicImageList, List<BbsTopicTag> bbsTopicTagList, List<BbsTopicLink> bbsTopicLinkList) {
        updataTopic(bbsTopic, bbsTopicImageList, bbsTopicTagList, bbsTopicLinkList);
        //更新redis
        List<BbsTopicFullDto> bbsTopicFullDtos = new ArrayList<>();
        bbsTopicFullDtos.add(bbsTopicFullDtoService.queryTopicFullDtoByIdFromSql(bbsTopic.getId()));
//        bbsRedisUtils.updateTopic(bbsTopicFullDtos);      上面查询已经更新
    }

    @Transactional
    public void updataTopic(BbsTopic bbsTopic, List<BbsTopicImage> bbsTopicImageList, List<BbsTopicTag> bbsTopicTagList, List<BbsTopicLink> bbsTopicLinkList) {
        bbsTopicMapper.updateById(bbsTopic);

        //1.先删除子表数据
        bbsTopicImageMapper.deleteByMainId(bbsTopic.getId());
        bbsTopicTagMapper.deleteByMainId(bbsTopic.getId());
        bbsTopicLinkMapper.deleteByMainId(bbsTopic.getId());

        //2.子表数据重新插入
        if (bbsTopicImageList != null && bbsTopicImageList.size() > 0) {
            for (BbsTopicImage entity : bbsTopicImageList) {
                //外键设置
                entity.setTopicId(bbsTopic.getId());
                bbsTopicImageMapper.insert(entity);
            }
        }
        if (bbsTopicTagList != null && bbsTopicTagList.size() > 0) {
            for (BbsTopicTag entity : bbsTopicTagList) {
                //外键设置
                entity.setTopicId(bbsTopic.getId());
                bbsTopicTagMapper.insert(entity);
            }
        }
        if (bbsTopicLinkList != null && bbsTopicLinkList.size() > 0) {
            for (BbsTopicLink entity : bbsTopicLinkList) {
                //外键设置
                entity.setTopicId(bbsTopic.getId());
                bbsTopicLinkMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        bbsTopicImageMapper.deleteByMainId(id);
        bbsTopicTagMapper.deleteByMainId(id);
        bbsTopicLinkMapper.deleteByMainId(id);
        bbsTopicMapper.deleteById(id);

        bbsRedisUtils.deleteTopicById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            bbsTopicImageMapper.deleteByMainId(id.toString());
            bbsTopicTagMapper.deleteByMainId(id.toString());
            bbsTopicLinkMapper.deleteByMainId(id.toString());
            bbsTopicMapper.deleteById(id);

            bbsRedisUtils.deleteTopicById(id.toString());
        }
    }


    // ****行星万象修改位置戳****

    @Override
    @AutoLog
    public Result<?> searchTopicByKeyword(String keyword, HttpServletRequest req, int pageNo, int pageSize) {
        //添加区域筛选
        Page<BbsTopic> page = new Page<>(pageNo, pageSize);
        Wrapper<BbsTopic> objectQueryWrapper = new QueryWrapper<BbsTopic>()
                .eq("region_code", req.getHeader("regioncode"))
                .and(t -> t.like("title", keyword)
                        .or()
                        .like("content", keyword))
                .orderByDesc("praise_count");
        IPage<BbsTopic> pageList = bbsTopicService.page(page, objectQueryWrapper);
        return Result.OK(pageList);
    }

    @Override
    @AutoLog
    @Transactional
    public Result<?> deletePublishTopic(String topicId) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        /**
         * 1.根据id删除帖子
         * 2.根据帖子图片id删除帖子图片
         * 3.根据图片url删除七牛图片
         * 4.用户记录发布贴子数量-1
         * 5.根据帖子id删除帖子评论
         * 6.（用户评论记录和点赞记录不删除）
         */
        //3.根据图片url删除七牛图片
        List<BbsTopicImage> bbsTopicImages = bbsTopicImageService.lambdaQuery().eq(BbsTopicImage::getTopicId, topicId).list();
        for (BbsTopicImage bbsTopicImage : bbsTopicImages) {
            Boolean delete = QiNiuUtil.delete(bbsTopicImage.getTopicImage());
            //if (!delete) {
            //    return null;
            //}
        }
        //删除帖子依赖关系
        this.deleteTopicDependData(topicId);
        //1.根据id删除帖子、2.根据帖子图片id删除帖子图片           删除标签、删除链接
        this.delMain(topicId);

        //boolean removeTopicFlag = bbsTopicService.removeById(topicId);
        //HashMap<String, Object> removeTopicImageMap = new HashMap<>();
        //removeTopicImageMap.put("topic_id", topicId);
        //bbsTopicImageService.removeByMap(removeTopicImageMap);
        return Result.OK("删除成功!");
    }

    @Override
    @AutoLog
    @Transactional
    public void deletePublishTopicBatch(List<String> topicId) {
        for (String item : topicId) {
            deletePublishTopic(item);
        }
    }

    //删除帖子依赖关系
    private Boolean deleteTopicDependData(String topicId) {
        BbsTopic byId = bbsTopicService.getById(topicId);
        //4.用户记录发布贴子数量-1
        BbsUserRecord userRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, byId.getCreateBy()).one();
        bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, byId.getCreateBy()).set(BbsUserRecord::getTopicCount, userRecord.getTopicCount() - 1).update();

        //获取贴子下所有Reply
        QueryWrapper<BbsReply> eqReply = new QueryWrapper<>();
        eqReply.eq("topic_id", topicId);
        List<String> replyIdList = bbsReplyService.list(eqReply).stream().map(BbsReply::getId).collect(Collectors.toList());

        QueryWrapper<BbsUserTopicClick> eq = new QueryWrapper<>();
        eq.eq("topic_id", topicId);
        bbsUserTopicClickService.remove(eq);

        QueryWrapper<BbsUserStar> eq1 = new QueryWrapper<>();
        eq1.eq("topic_id", topicId);
        bbsUserStarService.remove(eq1);

        QueryWrapper<BbsUserPraise> eq2 = new QueryWrapper<>();
        eq2.eq("topic_id", topicId);
        bbsUserPraiseService.remove(eq2);

        QueryWrapper<BbsUserMessage> eq4 = new QueryWrapper<>();
        eq4.eq("topic_id", topicId);
        bbsUserMessageService.remove(eq4);

        QueryWrapper<BbsInform> eq5 = new QueryWrapper<>();
        eq5.eq("topic_id", topicId);
        bbsInformService.remove(eq5);

        replyIdList.forEach(replyId -> {
            QueryWrapper<BbsUserPraise> eqReply2 = new QueryWrapper<>();
            eqReply2.eq("reply_id", replyId);
            bbsUserPraiseService.remove(eqReply2);

            QueryWrapper<BbsUserMessage> eqReply4 = new QueryWrapper<>();
            eqReply4.eq("reply_id", replyId);
            bbsUserMessageService.remove(eqReply4);

            QueryWrapper<BbsInform> eqReply5 = new QueryWrapper<>();
            eqReply5.eq("reply_id", replyId);
            bbsInformService.remove(eqReply5);
        });
        QueryWrapper<BbsReply> eq3 = new QueryWrapper<>();
        eq3.eq("topic_id", topicId);
        bbsReplyService.remove(eq3);


        //系统消息推送关联的是帖子也要删除，暂时不管，如果关联，提示无法删除
        //QueryWrapper<BbsSysMessage> eq6 = new QueryWrapper<>();
        //eq6.eq("topic_id", topicId);
        //bbsSysMessageService.remove(eq6);
        //
        //QueryWrapper<BbsUserSysMessage> eq7 = new QueryWrapper<>();
        //eq7.eq("topic_id", topicId);
        //bbsUserSysMessageService.remove(eq7);

        return true;
    }
}
