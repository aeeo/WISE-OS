package org.jeecg.modules.bbs.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.bbs.entity.BbsReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 帖子回复
 * @Author: jeecg-boot
 * @Date:   2021-01-28
 * @Version: V1.0
 */
public interface BbsReplyMapper extends BaseMapper<BbsReply> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
