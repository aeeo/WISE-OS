package org.jeecg.modules.bbs.mapper;

import java.util.List;
import org.jeecg.modules.bbs.entity.BbsTopicTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 帖子标签
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
public interface BbsTopicTagMapper extends BaseMapper<BbsTopicTag> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<BbsTopicTag> selectByMainId(@Param("mainId") String mainId);
}
