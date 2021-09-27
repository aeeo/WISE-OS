package org.jeecg.modules.bbs.service;

import org.jeecg.modules.bbs.entity.BbsClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 版块
 * @Author: jeecg-boot
 * @Date: 2021-02-28
 * @Version: V1.0
 */
public interface IBbsClassService extends IService<BbsClass> {

    public List<BbsClass> selectByMainId(String mainId);

    //获取有序版块列表
    public List<BbsClass> selectBbsClassListSort(String regionId);
}
