package org.jeecg.modules.bbs.mapper;

import java.util.List;
import org.jeecg.modules.bbs.entity.BbsTopicImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 帖子图片
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
public interface BbsTopicImageMapper extends BaseMapper<BbsTopicImage> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<BbsTopicImage> selectByMainId(@Param("mainId") String mainId);
}
