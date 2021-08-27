package org.jeecg.modules.bbs.service;

import org.jeecg.modules.bbs.entity.BbsUserSysMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 用户系统消息
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
public interface IBbsUserSysMessageService extends IService<BbsUserSysMessage> {

	public List<BbsUserSysMessage> selectByMainId(String mainId);
}
