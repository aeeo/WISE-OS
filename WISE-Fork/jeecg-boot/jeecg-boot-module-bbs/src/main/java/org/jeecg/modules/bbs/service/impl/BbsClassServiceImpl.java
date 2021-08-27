package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsClass;
import org.jeecg.modules.bbs.mapper.BbsClassMapper;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 版块
 * @Author: jeecg-boot
 * @Date:   2021-02-28
 * @Version: V1.0
 */
@Service
public class BbsClassServiceImpl extends ServiceImpl<BbsClassMapper, BbsClass> implements IBbsClassService {
	
	@Autowired
	private BbsClassMapper bbsClassMapper;
	
	@Override
	public List<BbsClass> selectByMainId(String mainId) {
		return bbsClassMapper.selectByMainId(mainId);
	}
}
