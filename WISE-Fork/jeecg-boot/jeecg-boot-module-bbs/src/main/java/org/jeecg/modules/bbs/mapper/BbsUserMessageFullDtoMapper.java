package org.jeecg.modules.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.bbs.entity.BbsUserMessageFullDto;

/**
 * @Description: 互动消息
 * @Author: jeecg-boot
 * @Date:   2021-01-30
 * @Version: V1.0
 */
public interface BbsUserMessageFullDtoMapper extends BaseMapper<BbsUserMessageFullDto> {

    IPage<BbsUserMessageFullDto> queryUserMessageFullList(Page<BbsUserMessageFullDto> page, String username);
}
