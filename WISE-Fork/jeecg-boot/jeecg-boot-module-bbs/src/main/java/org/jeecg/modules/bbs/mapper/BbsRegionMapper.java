package org.jeecg.modules.bbs.mapper;

import org.jeecg.modules.bbs.entity.BbsRegion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 地区
 * @Author: jeecg-boot
 * @Date:   2021-02-28
 * @Version: V1.0
 */
public interface BbsRegionMapper extends BaseMapper<BbsRegion> {

    /**
     * 获取表中最大的region_id
     */
    int maxRegionId();
}
