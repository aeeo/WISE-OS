package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsTopicLink;
import org.jeecg.modules.bbs.mapper.BbsTopicLinkMapper;
import org.jeecg.modules.bbs.service.IBbsTopicLinkService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 帖子跳转链接
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@Service
public class BbsTopicLinkServiceImpl extends ServiceImpl<BbsTopicLinkMapper, BbsTopicLink> implements IBbsTopicLinkService {
	
	@Autowired
	private BbsTopicLinkMapper bbsTopicLinkMapper;
	
	@Override
	public List<BbsTopicLink> selectByMainId(String mainId) {
		return bbsTopicLinkMapper.selectByMainId(mainId);
	}
}
