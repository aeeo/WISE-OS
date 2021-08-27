package org.jeecg.modules.bbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.bbs.entity.BbsUserPraise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户点赞记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-30
 * @Version: V1.0
 */
public interface BbsUserPraiseMapper extends BaseMapper<BbsUserPraise> {

    int queryTopicPraiseCount(String sysOrgCode);
}
