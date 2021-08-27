package org.jeecg.modules.bbs.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.bbs.entity.BbsFeedBack;
import org.jeecg.modules.bbs.entity.BbsFeedBackFullDto;
import org.jeecg.modules.bbs.entity.BbsTopicImage;
import org.jeecg.modules.bbs.mapper.BbsFeedBackMapper;
import org.jeecg.modules.bbs.service.IBbsFeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: 用户反馈
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
@Service
public class BbsFeedBackServiceImpl extends ServiceImpl<BbsFeedBackMapper, BbsFeedBack> implements IBbsFeedBackService {

    @Autowired
    BbsFeedBackServiceImpl bbsFeedBackService;
    @Autowired
    BbsTopicImageServiceImpl bbsTopicImageService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public Result<?> addFeedBack(BbsFeedBackFullDto bbsFeedBackFullDto) {
        LoginUser sysUserShiro = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //用户真实姓名和头像会发生变化，用户真实姓名和头像从redis中获取，本来应该从shiro中取，但是shiro中缓存更新不及时，比较复杂，暂时从redis中取
        LoginUser sysUser = (LoginUser)redisUtil.get(CacheConstant.SYS_USERS_CACHE + sysUserShiro.getUsername());

        if (null == sysUser) {
            sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        }
        bbsFeedBackFullDto.setUserAvatar(sysUser.getAvatar());
        bbsFeedBackFullDto.setCreateTime(new Date());
        bbsFeedBackFullDto.setCreateBy(sysUser.getUsername());
        //处理人默认管理员
        bbsFeedBackFullDto.setDisposeUsername("o_XHg4pJvz9U7d_RoU-oDWPiqBVE");
        bbsFeedBackFullDto.setDisposeResult("正在审核中...");

        bbsFeedBackService.save(bbsFeedBackFullDto);
        //保存图片  暂时将图片都存到bbs_topic_image表中
        for (BbsTopicImage bbsTopicImage : bbsFeedBackFullDto.getImageUrl()) {
            bbsTopicImage.setTopicId(bbsFeedBackFullDto.getId());
        }
        bbsTopicImageService.saveBatch(bbsFeedBackFullDto.getImageUrl());
        return Result.OK("添加成功！");
    }
}
