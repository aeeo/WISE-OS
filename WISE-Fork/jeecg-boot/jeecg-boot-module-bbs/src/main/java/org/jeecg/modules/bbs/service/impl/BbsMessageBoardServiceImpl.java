package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsMessageBoard;
import org.jeecg.modules.bbs.mapper.BbsMessageBoardMapper;
import org.jeecg.modules.bbs.service.IBbsMessageBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 留言板
 * @Author: jeecg-boot
 * @Date:   2021-01-13
 * @Version: V1.0
 */
@Service
public class BbsMessageBoardServiceImpl extends ServiceImpl<BbsMessageBoardMapper, BbsMessageBoard> implements IBbsMessageBoardService {

    @Autowired
    private IBbsMessageBoardService bbsMessageBoardService;
    @Override
    public int queryMessageBoardCount(String sysOrgCode) {
        return bbsMessageBoardService.lambdaQuery().eq(BbsMessageBoard::getSysOrgCode, sysOrgCode).count();
    }
}
