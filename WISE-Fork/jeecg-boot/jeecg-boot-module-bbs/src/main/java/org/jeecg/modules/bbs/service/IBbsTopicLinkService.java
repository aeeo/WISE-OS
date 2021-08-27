package org.jeecg.modules.bbs.service;

import org.jeecg.modules.bbs.entity.BbsTopicLink;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 帖子跳转链接
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
public interface IBbsTopicLinkService extends IService<BbsTopicLink> {

	public List<BbsTopicLink> selectByMainId(String mainId);
}
