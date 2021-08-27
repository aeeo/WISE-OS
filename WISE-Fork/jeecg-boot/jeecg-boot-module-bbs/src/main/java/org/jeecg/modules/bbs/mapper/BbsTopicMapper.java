package org.jeecg.modules.bbs.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.bbs.entity.BbsTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;

import java.util.List;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
public interface BbsTopicMapper extends BaseMapper<BbsTopic> {

    IPage<BbsTopic> searchTopicByKeyword(Page<BbsTopic> page, String regionCode, String keyword);

}
