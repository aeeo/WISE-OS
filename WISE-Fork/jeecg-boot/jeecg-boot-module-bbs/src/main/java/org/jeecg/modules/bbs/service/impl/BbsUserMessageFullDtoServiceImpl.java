package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.BbsUserMessage;
import org.jeecg.modules.bbs.entity.BbsUserMessageFullDto;
import org.jeecg.modules.bbs.mapper.BbsUserMessageFullDtoMapper;
import org.jeecg.modules.bbs.mapper.BbsUserMessageMapper;
import org.jeecg.modules.bbs.service.IBbsUserMessageFullDtoService;
import org.jeecg.modules.bbs.service.IBbsUserMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 互动消息
 * @Author: jeecg-boot
 * @Date:   2021-01-30
 * @Version: V1.0
 */
@Service
public class BbsUserMessageFullDtoServiceImpl extends ServiceImpl<BbsUserMessageFullDtoMapper, BbsUserMessageFullDto> implements IBbsUserMessageFullDtoService {
    @Resource
    private BbsUserMessageFullDtoMapper bbsUserMessageFullDtoMapper;

    @Override
    public IPage<BbsUserMessageFullDto> queryUserMessageFullList(Page<BbsUserMessageFullDto> page) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        IPage<BbsUserMessageFullDto> bbsUserMessageIPage = bbsUserMessageFullDtoMapper.queryUserMessageFullList(page, sysUser.getUsername());

        return bbsUserMessageIPage;
    }

}
