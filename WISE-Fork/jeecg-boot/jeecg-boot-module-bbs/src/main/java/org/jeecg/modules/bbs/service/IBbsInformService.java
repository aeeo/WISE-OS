package org.jeecg.modules.bbs.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsInform;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 举报列表
 * @Author: jeecg-boot
 * @Date:   2021-03-03
 * @Version: V1.0
 */
public interface IBbsInformService extends IService<BbsInform> {

    Result<?> informTopic(BbsInform bbsInform);

    Result<?> informReply(BbsInform bbsInform);
}
