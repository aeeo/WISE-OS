package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsClass;
import org.jeecg.modules.bbs.mapper.BbsClassMapper;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public List<BbsClass> selectBbsClassListSort(String regionId) {
		List<BbsClass> bbsClassList = this.lambdaQuery().eq(BbsClass::getRegionCode, regionId).list().stream().sorted(Comparator.comparing(BbsClass::getClassSort)).collect(Collectors.toList());
		return bbsClassList;
	}
}
