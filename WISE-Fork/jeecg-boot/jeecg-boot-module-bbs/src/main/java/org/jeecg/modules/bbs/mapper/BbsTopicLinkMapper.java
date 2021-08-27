package org.jeecg.modules.bbs.mapper;

import java.util.List;
import org.jeecg.modules.bbs.entity.BbsTopicLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 帖子跳转链接
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
public interface BbsTopicLinkMapper extends BaseMapper<BbsTopicLink> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<BbsTopicLink> selectByMainId(@Param("mainId") String mainId);
}
