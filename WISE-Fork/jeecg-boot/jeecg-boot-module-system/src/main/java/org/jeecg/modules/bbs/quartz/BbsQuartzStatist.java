package org.jeecg.modules.bbs.quartz;

import io.swagger.annotations.Api;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.bbs.BbsStatistController;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.service.*;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "BBS_定时统计")
@RestController
@RequestMapping("/bbs/BbsStatist/quartz")
public class BbsQuartzStatist implements Job {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysLogService sysLogService;
    @Autowired
    private IBbsTopicService bbsTopicService;
    @Autowired
    private IBbsStatistService bbsStatistService;
    @Autowired
    private IBbsUserPraiseService bbsUserPraiseService;
    @Autowired
    private IBbsReplyService bbsReplyService;
    @Autowired
    private IBbsMessageBoardService bbsMessageBoardService;
    @Autowired
    private IBbsWaimaiUserService bbsWaimaiUserService;
    @Autowired
    private BbsStatistController bbsStatistController;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        /**
         * 问题现象：
         * 安全框架用的shiro，然后写了一个定时任务，报错，看报错内容不让执行定时任务。未绑定SecurityManager啥的。
         * 解决方法：
         * 看报错是ThreadContext未绑定SecurityManager，那就帮顶下，在定时任务最开始的代码前加上：
         */
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        ThreadContext.bind(manager);



        List<BbsRegion> list = bbsRegionService.lambdaQuery()
                .eq(BbsRegion::getRegionStatus, 2)
                .and(l -> l.isNotNull(BbsRegion::getSysOrgCode).ne(BbsRegion::getSysOrgCode, ""))
                .list();
        if (null == list) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            bbsStatistController.todayCountStatist(list.get(i).getSysOrgCode());
        }
        System.out.println("每天凌晨11:50统计所有区域的数据！");
    }
}
