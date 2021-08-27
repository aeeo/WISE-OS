package org.jeecg.modules.bbs.service;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsClass;
import org.jeecg.modules.bbs.entity.BbsRegion;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 地区
 * @Author: jeecg-boot
 * @Date:   2021-02-28
 * @Version: V1.0
 */
public interface IBbsRegionService extends IService<BbsRegion> {

	void addRegion(BbsRegion bbsRegion, List<BbsClass> bbsClassList);
	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(BbsRegion bbsRegion,List<BbsClass> bbsClassList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(BbsRegion bbsRegion,List<BbsClass> bbsClassList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


    void initAllRegionClass();
}
