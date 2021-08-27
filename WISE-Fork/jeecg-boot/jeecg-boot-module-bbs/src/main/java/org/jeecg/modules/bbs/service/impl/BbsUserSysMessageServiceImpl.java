package org.jeecg.modules.bbs.service.impl;

import org.jeecg.modules.bbs.entity.BbsUserSysMessage;
import org.jeecg.modules.bbs.mapper.BbsUserSysMessageMapper;
import org.jeecg.modules.bbs.service.IBbsUserSysMessageService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 用户系统消息
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@Service
public class BbsUserSysMessageServiceImpl extends ServiceImpl<BbsUserSysMessageMapper, BbsUserSysMessage> implements IBbsUserSysMessageService {
	
	@Autowired
	private BbsUserSysMessageMapper bbsUserSysMessageMapper;
	
	@Override
	public List<BbsUserSysMessage> selectByMainId(String mainId) {
		return bbsUserSysMessageMapper.selectByMainId(mainId);
	}
}
