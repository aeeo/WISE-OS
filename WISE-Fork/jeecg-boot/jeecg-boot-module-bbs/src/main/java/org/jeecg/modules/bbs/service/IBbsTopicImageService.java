package org.jeecg.modules.bbs.service;

import org.jeecg.modules.bbs.entity.BbsTopicImage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 帖子图片
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
public interface IBbsTopicImageService extends IService<BbsTopicImage> {

	public List<BbsTopicImage> selectByMainId(String mainId);
}
