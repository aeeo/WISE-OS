package org.jeecg.modules.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.bbs.entity.BbsUserTopicClick;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户浏览记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-19
 * @Version: V1.0
 */
public interface IBbsUserTopicClickService extends IService<BbsUserTopicClick> {

    IPage<BbsUserTopicClick> queryRecently(Page<BbsUserTopicClick> page,String username);
}
