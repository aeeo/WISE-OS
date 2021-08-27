package org.jeecg.modules.bbs.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.bbs.entity.BbsMessageBoard;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 留言板
 * @Author: jeecg-boot
 * @Date:   2021-01-13
 * @Version: V1.0
 */
public interface IBbsMessageBoardService extends IService<BbsMessageBoard> {

    /**
     * 区域留言数量
     * @param sysOrgCode
     * @return
     */
    int queryMessageBoardCount(@Param("sysOrgCode") String sysOrgCode);
}
