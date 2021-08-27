package org.jeecg.modules.bbs.mapper;

import java.util.List;
import org.jeecg.modules.bbs.entity.BbsClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 版块
 * @Author: jeecg-boot
 * @Date:   2021-03-08
 * @Version: V1.0
 */
public interface BbsClassMapper extends BaseMapper<BbsClass> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<BbsClass> selectByMainId(@Param("mainId") String mainId);

    void addClassByRegionFullName(String regionCode, String fullName);
}
