package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.bbs.entity.BbsUserTopicClick;
import org.jeecg.modules.bbs.mapper.BbsUserTopicClickMapper;
import org.jeecg.modules.bbs.service.IBbsUserTopicClickService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 用户浏览记录表
 * @Author: jeecg-boot
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Service
public class BbsUserTopicClickServiceImpl extends ServiceImpl<BbsUserTopicClickMapper, BbsUserTopicClick> implements IBbsUserTopicClickService {

    @Resource
    BbsUserTopicClickMapper bbsUserTopicClickMapper;

    @Override
    public IPage<BbsUserTopicClick> queryRecently(Page<BbsUserTopicClick> page, String username) {
        IPage<BbsUserTopicClick> pageList = bbsUserTopicClickMapper.queryRecently(page, username);
        return pageList;
    }
}
