package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsTopicTag;
import org.jeecg.modules.bbs.mapper.BbsTopicTagMapper;
import org.jeecg.modules.bbs.service.IBbsTopicTagService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 帖子标签
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
@Service
public class BbsTopicTagServiceImpl extends ServiceImpl<BbsTopicTagMapper, BbsTopicTag> implements IBbsTopicTagService {
	
	@Autowired
	private BbsTopicTagMapper bbsTopicTagMapper;
	
	@Override
	public List<BbsTopicTag> selectByMainId(String mainId) {
		return bbsTopicTagMapper.selectByMainId(mainId);
	}
}
