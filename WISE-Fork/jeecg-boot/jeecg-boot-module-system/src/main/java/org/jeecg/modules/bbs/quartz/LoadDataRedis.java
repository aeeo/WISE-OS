package org.jeecg.modules.bbs.quartz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.jeecg.modules.bbs.service.IBbsRegionService;
import org.jeecg.modules.bbs.service.IBbsTopicFullDtoService;
import org.jeecg.modules.bbs.service.impl.BbsUserRecordServiceImpl;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 启动项目后, 加载数据库数据到redis中
 */
@Component
@Slf4j
public class LoadDataRedis {
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private IBbsClassService bbsClassService;
    @Autowired
    private BbsUserRecordServiceImpl bbsUserRecordService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IBbsTopicFullDtoService bbsTopicFullDtoService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 区域
     */
    public static final String BBS_REGION_REGIONCODE = "bbs_region_regioncode_";
    public static final Long BBS_REGION_REGIONCODE_TIME = (long) 30 * 24 * 60 * 60 * 1000;          //30天
    /**
     * 版块
     */
    public static final String BBS_REGION_REGIONCODE_CLASSCODE = "bbs_region_regioncode_classcode";
    public static final Long BBS_REGION_REGIONCODE_ClASSCODE_TIME = (long) 30 * 24 * 60 * 60 * 1000;          //30天
    /**
     * 贴子
     */
    public static final String BBS_TOPIC_TOPICID = "bbc_topic_topicid_";
    public static final Long BBS_TOPIC_TOPICID_TIME = (long) 30 * 24 * 60 * 60 * 1000;          //30天

    /**
     * 区域贴子排行榜
     */
    public static final String BBS_RANK_REGION_CLASS_TOPICID = "bbs_rank_region_class_topicid_";
    public static final Long BBS_RANK_REGION_CLASS_TIME = (long) 30 * 24 * 60 * 60 * 1000;          //30天

    /**
     * 初始化数据
     */
    @PostConstruct // 是java注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
    public void reload() {
        loadRegion();               //加载区域
        loadTopicRank();            //加载帖子排行榜
    }

    public void loadRegion() {
        List<BbsRegion> bbsRegions = bbsRegionService.lambdaQuery().list();
        for (BbsRegion bbsRegion : bbsRegions) {
            String regionCode = (String) redisUtil.get(BBS_REGION_REGIONCODE + bbsRegion.getRegionCode());
            if (null != regionCode || regionCode.isEmpty()) {
                redisUtil.set(BBS_REGION_REGIONCODE + bbsRegion.getRegionCode(), bbsRegion, BBS_REGION_REGIONCODE_TIME);
                log.info("行星万象缓存加载区域：" + bbsRegion.getFullName());
            }
        }
        for (BbsRegion bbsRegion : bbsRegions) {
            if(bbsRegion.getRegionStatus() != 2) {continue;}//不加载未上线区域


            Page<BbsTopicFullDto> page = new Page<BbsTopicFullDto>(1, 20);
            IPage<BbsTopicFullDto> bbsTopicFullDtos12 = bbsTopicFullDtoService.queryTopicFullDto(page, regionCode,classCode, topicType);
            bbsTopicFullDtoService.queryTopicFullDto()
        }

    }

    public void loadTopicRank() {

    }
}