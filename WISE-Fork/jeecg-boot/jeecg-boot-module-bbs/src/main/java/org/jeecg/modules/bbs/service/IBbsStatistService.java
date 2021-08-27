package org.jeecg.modules.bbs.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.bbs.entity.BbsStatist;

import java.util.List;

/**
 * @Description: 统计
 * @Author: jeecg-boot
 * @Date: 2021-05-31
 * @Version: V1.0
 */
public interface IBbsStatistService extends IService<BbsStatist> {

    /**
     * 获取昨日统计数据
     *
     * @param sysOrgCode
     * @return
     */
    JSONObject yesterdayCountInfo(String sysOrgCode);

    /**
     * 获取昨日统计数据项
     *
     * @param sysOrgCode
     * @param name
     * @return
     */
    int yesterdayCount(String sysOrgCode, String name);


    /**
     * 保存日统计数据
     *
     * @param jsonObject
     * @param sysOrgCode
     * @return
     */
    boolean insertDayStatistData(JSONObject jsonObject, String sysOrgCode);
}
