package org.jeecg.modules.bbs.service;

import org.jeecg.modules.bbs.entity.BbsWaimaiUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户点击外卖记录
 * @Author: jeecg-boot
 * @Date:   2021-05-27
 * @Version: V1.0
 */
public interface IBbsWaimaiUserService extends IService<BbsWaimaiUser> {

    /**
     * 用户点击外卖次数
     * @param sysOrgCode
     * @return
     */
    int queryWaimaiUserCount(String sysOrgCode);
}
