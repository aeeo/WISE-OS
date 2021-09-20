package org.jeecg.modules.bbs.entity;

import lombok.Data;
import org.jeecg.modules.system.entity.SysUser;

import java.util.List;

@Data
public class MiNiStorage {
    private String token;
    private BbsRegion bbsRegion;
    private SysUser sysUser;
    private BbsUserRecord bbsUserRecord;
    private UserBehaviorLimit userBehaviorLimit;        //用户限制
    private List<BbsClass> bbsClassList;
}

