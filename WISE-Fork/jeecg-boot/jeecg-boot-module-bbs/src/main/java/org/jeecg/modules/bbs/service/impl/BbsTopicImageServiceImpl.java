package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsTopicImage;
import org.jeecg.modules.bbs.mapper.BbsTopicImageMapper;
import org.jeecg.modules.bbs.service.IBbsTopicImageService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 帖子图片
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
@Service
public class BbsTopicImageServiceImpl extends ServiceImpl<BbsTopicImageMapper, BbsTopicImage> implements IBbsTopicImageService {
	
	@Autowired
	private BbsTopicImageMapper bbsTopicImageMapper;
	
	@Override
	public List<BbsTopicImage> selectByMainId(String mainId) {
		return bbsTopicImageMapper.selectByMainId(mainId);
	}
}
