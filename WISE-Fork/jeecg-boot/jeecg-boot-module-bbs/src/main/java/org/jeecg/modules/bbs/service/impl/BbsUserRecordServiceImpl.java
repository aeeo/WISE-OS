package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.mapper.BbsUserRecordMapper;
import org.jeecg.modules.bbs.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户信息记录
 * @Author: jeecg-boot
 * @Date: 2021-01-13
 * @Version: V1.0
 */
@Service
public class BbsUserRecordServiceImpl extends ServiceImpl<BbsUserRecordMapper, BbsUserRecord> implements IBbsUserRecordService {
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private IBbsUserSysMessageService bbsUserSysMessageService;
    @Autowired
    private BbsUserMessageServiceImpl bbsUserMessageService;
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private IBbsTopicFullDtoService bbsTopicFullDtoService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public BbsUserRecord getFullUserRecord(String username) {
        BbsUserRecord bbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, username).one();
        if (null == bbsUserRecord) {
            return null;
        }
        /**未读收到的评论数量*/
        int count1 = bbsUserMessageService.lambdaQuery().eq(BbsUserMessage::getReceiveUsername,username)
                .eq(BbsUserMessage::getStatus, "1")
                .ne(BbsUserMessage::getCreateBy, username).count();
        /**未读系统消息数量*/
        int count2 = bbsUserSysMessageService.lambdaQuery()
                .eq(BbsUserSysMessage::getReceiveUsername, username)
                .eq(BbsUserSysMessage::getStatus, "1")
                .in(BbsUserSysMessage::getRegionCode, bbsUserRecord.getRegionCode(), "all")
                .eq(BbsUserSysMessage::getStatus, "1")
                .count();
        bbsUserRecord.setUserMessageCount(count1);
        bbsUserRecord.setUserSysMessageCount(count2);
        //区域名
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        bbsUserRecord.setRegionFullName(bbsRegion.getFullName());
        //bbsUserRecord.setCreateByName(sysUser.getRealname());
        //bbsUserRecord.setAvatar(sysUser.getAvatar());
        //bbsUserRecord.setSex(sysUser.getSex());

        //角色
        bbsUserRecord.setRoleCodeList(sysBaseAPI.getUserRoleSet(username));
        return bbsUserRecord;
    }

    @Override
    public BbsUserRecord setUserRecord(BbsUserRecord bbsUserRecord) {
        bbsUserRecordService.updateById(bbsUserRecord);
        return bbsUserRecord;
    }
}
