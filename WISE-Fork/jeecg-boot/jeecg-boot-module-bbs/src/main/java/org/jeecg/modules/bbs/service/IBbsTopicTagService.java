package org.jeecg.modules.bbs.service;

import org.jeecg.modules.bbs.entity.BbsTopicTag;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 帖子标签
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
public interface IBbsTopicTagService extends IService<BbsTopicTag> {

	public List<BbsTopicTag> selectByMainId(String mainId);
}
