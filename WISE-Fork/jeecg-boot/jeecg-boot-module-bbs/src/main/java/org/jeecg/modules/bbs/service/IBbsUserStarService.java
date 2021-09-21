package org.jeecg.modules.bbs.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsUserStar;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户收藏
 * @Author: jeecg-boot
 * @Date:   2021-01-12
 * @Version: V1.0
 */
public interface IBbsUserStarService extends IService<BbsUserStar> {

    Result<?> clickStar(String username, String topicId, Boolean isStar);
}
