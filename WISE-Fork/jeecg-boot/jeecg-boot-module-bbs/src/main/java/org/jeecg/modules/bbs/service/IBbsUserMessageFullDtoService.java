package org.jeecg.modules.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.bbs.entity.BbsUserMessage;
import org.jeecg.modules.bbs.entity.BbsUserMessageFullDto;

/**
 * @Description: 互动消息
 * @Author: jeecg-boot
 * @Date:   2021-01-30
 * @Version: V1.0
 */
public interface IBbsUserMessageFullDtoService extends IService<BbsUserMessageFullDto> {

    IPage<BbsUserMessageFullDto> queryUserMessageFullList(Page<BbsUserMessageFullDto> page);
}
