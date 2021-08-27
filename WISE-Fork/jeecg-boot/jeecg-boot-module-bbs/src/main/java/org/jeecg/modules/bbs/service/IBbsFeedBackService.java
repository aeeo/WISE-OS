package org.jeecg.modules.bbs.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsFeedBack;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.bbs.entity.BbsFeedBackFullDto;

/**
 * @Description: 用户反馈
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
public interface IBbsFeedBackService extends IService<BbsFeedBack> {

    Result<?> addFeedBack(BbsFeedBackFullDto bbsFeedBackFullDto);
}
