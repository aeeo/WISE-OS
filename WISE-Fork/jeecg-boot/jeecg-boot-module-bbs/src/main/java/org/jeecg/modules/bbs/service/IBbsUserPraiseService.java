package org.jeecg.modules.bbs.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsUserPraise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户点赞记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface IBbsUserPraiseService extends IService<BbsUserPraise> {

    Result<?> clickPraise(String topicId, Boolean isPraise, String messageId);

    Result<?> clickReplyPraise(String replyId, Boolean isPraise);

    int queryTopicPraiseCount(String sysOrgCode);

    int queryMessageBoardPraiseCount(String sysOrgCode);
}
