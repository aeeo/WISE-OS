package org.jeecg.modules.bbs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsSysMessage;
import org.jeecg.modules.bbs.entity.BbsUserSysMessage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 系统消息
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
public interface IBbsSysMessageService extends IService<BbsSysMessage> {
    void addSysMessage(BbsSysMessage bbsSysMessage);

    Result<?> queryUserMessageList(Page<BbsSysMessage> page, String username, String regionCode);


    /**
     * 添加一对多
     */
    public void saveMain(BbsSysMessage bbsSysMessage, List<BbsUserSysMessage> bbsUserSysMessageList);

    /**
     * 修改一对多
     */
    public void updateMain(BbsSysMessage bbsSysMessage, List<BbsUserSysMessage> bbsUserSysMessageList);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

}
