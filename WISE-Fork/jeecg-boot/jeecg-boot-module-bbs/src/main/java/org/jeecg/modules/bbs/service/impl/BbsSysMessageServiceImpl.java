package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.bbs.entity.BbsSysMessage;
import org.jeecg.modules.bbs.entity.BbsUserRecord;
import org.jeecg.modules.bbs.entity.BbsUserSysMessage;
import org.jeecg.modules.bbs.mapper.BbsSysMessageMapper;
import org.jeecg.modules.bbs.mapper.BbsUserSysMessageMapper;
import org.jeecg.modules.bbs.service.IBbsSysMessageService;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.jeecg.modules.bbs.service.IBbsUserSysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jeecg.common.api.vo.Result;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 系统消息
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
@Service
public class BbsSysMessageServiceImpl extends ServiceImpl<BbsSysMessageMapper, BbsSysMessage> implements IBbsSysMessageService {

    @Autowired
    private IBbsSysMessageService bbsSysMessageService;
    @Autowired
    private IBbsUserSysMessageService bbsUserSysMessageService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Resource
    private BbsSysMessageMapper bbsSysMessageMapper;
    @Resource
    private BbsUserSysMessageMapper bbsUserSysMessageMapper;


    /**
     * 发布系统消息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSysMessage(BbsSysMessage bbsSysMessage) {
        bbsSysMessageService.save(bbsSysMessage);
        //查询所有用户信息
        List<BbsUserRecord> bbsUserRecordList = bbsUserRecordService.query().list();

        List<BbsUserSysMessage> bbsUserSysMessageList = new ArrayList<>();
        for (int i = 0; i < bbsUserRecordList.size(); i++) {
            BbsUserSysMessage bbsUserSysMessage = new BbsUserSysMessage();
            bbsUserSysMessage.setSysMessageId(bbsSysMessage.getId());
            bbsUserSysMessage.setReceiveUsername(bbsUserRecordList.get(i).getCreateBy());
            bbsUserSysMessageList.add(bbsUserSysMessage);
        }
        //给bbsusersysmessage表中插入信息，为所有符合地区的用户创建消息记录
        bbsUserSysMessageService.saveBatch(bbsUserSysMessageList);
    }

    /**
     * 查询用户的系统消息
     * @param page
     * @param username
     * @return
     */
    @Override
    @Transactional
    public Result<?> queryUserMessageList(Page<BbsSysMessage> page, String username, String regionCode) {
        List<BbsSysMessage> bbsSysMessageList = bbsUserSysMessageMapper.queryUserMessageList(page, username, regionCode);
        page.setRecords(bbsSysMessageList);
        //设置贴子状态为已读
        bbsUserSysMessageMapper.setReadUserMessageList(username, regionCode);
        return Result.OK(page);
    }

    @Override
    @Transactional
    public void saveMain(BbsSysMessage bbsSysMessage, List<BbsUserSysMessage> bbsUserSysMessageList) {
        bbsSysMessageMapper.insert(bbsSysMessage);
        if (bbsUserSysMessageList != null && bbsUserSysMessageList.size() > 0) {
            for (BbsUserSysMessage entity : bbsUserSysMessageList) {
                //外键设置
                entity.setSysMessageId(bbsSysMessage.getId());
                bbsUserSysMessageMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(BbsSysMessage bbsSysMessage, List<BbsUserSysMessage> bbsUserSysMessageList) {
        bbsSysMessageMapper.updateById(bbsSysMessage);

        //1.先删除子表数据
        bbsUserSysMessageMapper.deleteByMainId(bbsSysMessage.getId());

        //2.子表数据重新插入
        if (bbsUserSysMessageList != null && bbsUserSysMessageList.size() > 0) {
            for (BbsUserSysMessage entity : bbsUserSysMessageList) {
                //外键设置
                entity.setSysMessageId(bbsSysMessage.getId());
                bbsUserSysMessageMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        bbsUserSysMessageMapper.deleteByMainId(id);
        bbsSysMessageMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            bbsUserSysMessageMapper.deleteByMainId(id.toString());
            bbsSysMessageMapper.deleteById(id);
        }
    }

}
