package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsWaimaiUser;
import org.jeecg.modules.bbs.mapper.BbsWaimaiUserMapper;
import org.jeecg.modules.bbs.service.IBbsWaimaiUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 用户点击外卖记录
 * @Author: jeecg-boot
 * @Date:   2021-05-27
 * @Version: V1.0
 */
@Service
public class BbsWaimaiUserServiceImpl extends ServiceImpl<BbsWaimaiUserMapper, BbsWaimaiUser> implements IBbsWaimaiUserService {

    @Resource
    private IBbsWaimaiUserService bbsWaimaiUserService;
    @Override
    public int queryWaimaiUserCount(String sysOrgCode) {
        return bbsWaimaiUserService.lambdaQuery().eq(BbsWaimaiUser::getSysOrgCode, sysOrgCode).count();
    }
}
