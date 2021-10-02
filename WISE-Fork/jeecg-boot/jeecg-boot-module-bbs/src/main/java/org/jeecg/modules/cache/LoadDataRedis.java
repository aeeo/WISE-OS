package org.jeecg.modules.cache;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.bbs.entity.BbsClass;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.jeecg.modules.bbs.service.IBbsRegionService;
import org.jeecg.modules.bbs.service.IBbsTopicFullDtoService;
import org.jeecg.modules.bbs.service.impl.BbsUserRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    private IBbsTopicFullDtoService bbsTopicFullDtoService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 区域
     */
    public static final String BBS_REGION_REGIONCODE = "bbs:region:";
    public static final Long BBS_REGION_REGIONCODE_TIME = (long) 30 * 24 * 60 * 60 * 1000;          //30天
    /**
     * 版块
     */
    public static final String BBS_CLASS_REGIONCODE_CLASSCODE = "bbs:class:";
    public static final Long BBS_CLASS_REGIONCODE_CLASSCODE_TIME = (long) 30 * 24 * 60 * 60 * 1000;          //30天
    /**
     * 区域贴子排行榜
     */
    public static final String BBS_RANK_REGION_CLASS = "bbs:rank:";
    public static final Long BBS_RANK_REGION_CLASS_TIME = (long) 30 * 24 * 60 * 60 * 1000;          //30天


    /**
     * 贴子
     */
    public static final String BBS_TOPIC_TOPICID = "bbs:topic:";
    public static final Long BBS_TOPIC_TOPICID_TIME = (long) 24 * 60 * 60 * 1000;          //1天


    /**
     * 初始化数据
     */
    @PostConstruct // 是java注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
    public void reload() {
        loadData();               //加载数据
    }

    /**
     * 此方法用于启动加载数据到redis，redis中的数据分为两部分管理
     * 一部分为JeecgBoot权限部门用户等信息
     * 另一部分为行星万象的区域，帖子，板块排行和用户等数据
     *
     * 区域，板块等信息加载后基本不变，修改后Reids和mysql同步
     * 帖子保持redis中最新，每天用定时任务去回写到mysql，在系统加载时如果redis中已经存在则不加载
     * redis会自动持久化（会丢失十分钟左右的数据）,这样在系统重启后就不会丢失topic数据。
     */
    public void loadData() {
        List<BbsRegion> bbsRegions = bbsRegionService.lambdaQuery().list();
        for (BbsRegion bbsRegion : bbsRegions) {
            //BbsRegion regionCode = (BbsRegion) redisUtil.get(BBS_REGION_REGIONCODE + bbsRegion.getRegionCode());
            redisUtil.set(BBS_REGION_REGIONCODE + bbsRegion.getRegionCode(), bbsRegion, BBS_REGION_REGIONCODE_TIME);
            log.info("行星万象缓存加载区域：" + bbsRegion.getFullName());
        }
        for (BbsRegion bbsRegion : bbsRegions) {
            if (bbsRegion.getRegionStatus() != 2) {
                continue;           //不加载未上线区域
            }
            //版块
            List<BbsClass> bbsClassList = bbsClassService.lambdaQuery().eq(BbsClass::getRegionCode, bbsRegion.getRegionCode()).list();
            for (BbsClass bbsClass : bbsClassList) {
                redisUtil.set(BBS_CLASS_REGIONCODE_CLASSCODE + bbsRegion.getRegionCode() + "_" + bbsClass.getClassCode(), bbsClass, BBS_CLASS_REGIONCODE_CLASSCODE_TIME);
                log.info("行星万象缓存加载版块：" + bbsClass.getClassName());
                //贴子排行榜
                Page<BbsTopicFullDto> page = new Page<BbsTopicFullDto>(1, Integer.MAX_VALUE);
                //前面为最新
                IPage<BbsTopicFullDto> bbsTopicFullDtoIPage = bbsTopicFullDtoService.queryTopicFullDtoFix(page, bbsRegion.getRegionCode(), bbsClass.getClassCode(), new int[]{0, 1, 2});
                Double score = (double)Integer.MAX_VALUE;
                for (BbsTopicFullDto topicFullDto : bbsTopicFullDtoIPage.getRecords()) {
                    //Set<ZSetOperations.TypedTuple<String>> typedTuples1 = redisUtil.getRedisTemplate().opsForZSet().rangeWithScores(BBS_RANK_REGION_CLASS + bbsRegion.getRegionCode() + "_" + bbsClass.getClassCode(), 0, 0);
                    //Iterator<ZSetOperations.TypedTuple<String>> iterator = typedTuples1.iterator();
                    //ZSetOperations.TypedTuple<String> next = iterator.next();
                    redisUtil.zAdd(BBS_RANK_REGION_CLASS + bbsRegion.getRegionCode() + "_" + bbsClass.getClassCode(), topicFullDto.getId(), score);
                    log.info("行星万象缓存加载排行：" + bbsClass.getClassCode() + topicFullDto.getContent());

                    //贴子，已经存在于redis的帖子不做更新，保证redis中的帖子是最新的
                    Object topic = redisUtil.get(BBS_TOPIC_TOPICID + topicFullDto.getId());
                    if (null == topic) {
                        redisUtil.set(BBS_TOPIC_TOPICID + topicFullDto.getId(), topicFullDto, BBS_TOPIC_TOPICID_TIME);
                    }
                    log.info("行星万象缓存加载贴子：" + topicFullDto.getContent());
                    score--;
                }
            }
        }
    }
}