package org.jeecg.modules.bbs.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsReply;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 帖子回复
 * @Author: jeecg-boot
 * @Date:   2021-01-02
 * @Version: V1.0
 */
public interface IBbsReplyService extends IService<BbsReply> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addBbsReply(BbsReply bbsReply);
	
	/**修改节点*/
	void updateBbsReply(BbsReply bbsReply) throws JeecgBootException;
	
	/**删除节点*/
	void deleteBbsReply(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<BbsReply> queryTreeListNoPage(QueryWrapper<BbsReply> queryWrapper);

	Result<?> addReply(BbsReply bbsReply);


	/**删除节点*/
	void deleteBbsReplyWiseMini(String id) throws JeecgBootException;

	/**
	 * 查询区域评论数量
	 * @param sysOrgCode
	 * @return
	 */
    int queryReplyCount(String sysOrgCode);
}
