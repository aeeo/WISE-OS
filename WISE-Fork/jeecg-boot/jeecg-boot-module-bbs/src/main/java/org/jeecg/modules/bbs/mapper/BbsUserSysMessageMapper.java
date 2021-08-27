package org.jeecg.modules.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.bbs.entity.BbsSysMessage;
import org.jeecg.modules.bbs.entity.BbsUserSysMessage;

import java.util.List;

/**
 * @Description: 用户系统消息
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
public interface BbsUserSysMessageMapper extends BaseMapper<BbsUserSysMessage> {

    List<BbsSysMessage> queryUserMessageList(Page<BbsSysMessage> page, String username, String regionCode);

    void setReadUserMessageList(String username, String regionCode);

    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<BbsUserSysMessage> selectByMainId(@Param("mainId") String mainId);
}
