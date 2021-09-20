package org.jeecg.modules.bbs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsUserRecord;

/**
 * @Description: 用户信息记录
 * @Author: jeecg-boot
 * @Date:   2021-01-13
 * @Version: V1.0
 */
public interface IBbsUserRecordService extends IService<BbsUserRecord> {

    BbsUserRecord getFullUserRecord(String username);
}
