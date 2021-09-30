package org.jeecg.modules.cache;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.jeecg.modules.bbs.service.IBbsRegionService;
import org.jeecg.modules.bbs.service.IBbsTopicFullDtoService;
import org.jeecg.modules.bbs.service.impl.BbsUserRecordServiceImpl;
import org.jeecg.modules.bbs.vo.BbsTopicPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 启动项目后, 加载数据库数据到redis中
 */
@Component
@Slf4j
public class BbsRedisUtils {
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
    static Double precision = 0.00001;              //新增贴子精度


    /**
     * 新增贴子，加入redis
     *
     * @param bbsTopic
     */
    public void addTopic(BbsTopicFullDto bbsTopic) {
        Double precision = 0.00001;
        //获取除score不为Integer.MAX_VALUE的最大值
        Set<ZSetOperations.TypedTuple<Object>> typedTuples1 = redisUtil.zReverseRangeByScoreWithScoresLR(
                LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_" + bbsTopic.getClassCode(),
                Integer.MAX_VALUE - 1,
                Integer.MAX_VALUE - precision,
                0,
                1);
        //如果此板块为空，则typedTuples1 为null，则会无法添加，加判断
        if (null != typedTuples1) {
            Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples1.iterator();
            //置顶帖排到最顶部
            if ("1".equals(bbsTopic.getTopicType())) {
                redisUtil.zAdd(LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_" + bbsTopic.getClassCode(), bbsTopic.getId(), Integer.MAX_VALUE);
                return;
            }
            if (iterator.hasNext()) {
                ZSetOperations.TypedTuple<Object> next = iterator.next();
                Double score = next.getScore();
                //如果该帖子是别的版块的，同时更新到index版块
                if(!"index".equals(bbsTopic.getClassCode())){
                    redisUtil.zAdd(LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_index", bbsTopic.getId(), score + precision);
                }
                redisUtil.zAdd(LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_" + bbsTopic.getClassCode(), bbsTopic.getId(), score + precision);
            } else {
                if(!"index".equals(bbsTopic.getClassCode())){
                    redisUtil.zAdd(LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_index", bbsTopic.getId(), Integer.MAX_VALUE - 1);
                }
                redisUtil.zAdd(LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_" + bbsTopic.getClassCode(), bbsTopic.getId(), Integer.MAX_VALUE - 1);
            }
        } else {
            if(!"index".equals(bbsTopic.getClassCode())){
                redisUtil.zAdd(LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_index", bbsTopic.getId(), Integer.MAX_VALUE - 1);
            }
            redisUtil.zAdd(LoadDataRedis.BBS_RANK_REGION_CLASS + bbsTopic.getRegionCode() + "_" + bbsTopic.getClassCode(), bbsTopic.getId(), Integer.MAX_VALUE - 1);
        }

        //贴子
        redisUtil.set(LoadDataRedis.BBS_TOPIC_TOPICID + bbsTopic.getId(), bbsTopic, LoadDataRedis.BBS_TOPIC_TOPICID_TIME);
    }

    /**
     * 贴子全量更新
     */
    public void updateTopic(List<BbsTopicFullDto> bbsTopicFullDtoList) {
        //创建Pipeline对象
        List<Object> objectList = redisUtil.getRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (BbsTopicFullDto bbsTopicFullDto : bbsTopicFullDtoList) {
                    redisUtil.set(LoadDataRedis.BBS_TOPIC_TOPICID + bbsTopicFullDto.getId(), bbsTopicFullDto, LoadDataRedis.BBS_TOPIC_TOPICID_TIME);
                }
                return null;
            }
        });
    }

    /**
     * 贴子浏览量
     */
    public void updateTopicHitCount(List<String> topicIds, int count) {
        List<Object> objectList = redisUtil.getRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i < topicIds.size(); i++) {
                    topicIds.set(i, LoadDataRedis.BBS_TOPIC_TOPICID + "" + topicIds.get(i));
                }
                List<Object> objectList1 = redisUtil.mget(topicIds);

                for (Object bbsTopicFullDtoItem : objectList1) {
                    BbsTopicFullDto bbsTopicFullDto = (BbsTopicFullDto) bbsTopicFullDtoItem;
                    bbsTopicFullDto.setHitsCount(bbsTopicFullDto.getHitsCount() + count);
                    redisUtil.set(LoadDataRedis.BBS_TOPIC_TOPICID + bbsTopicFullDto.getId(), bbsTopicFullDto, LoadDataRedis.BBS_TOPIC_TOPICID_TIME);
                }
                return null;
            }
        });
    }

    /**
     * 贴子点赞量
     */
    public void updateTopicPraiseCount(List<String> topicIds, int count) {
        List<Object> objectList = redisUtil.getRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i < topicIds.size(); i++) {
                    topicIds.set(i, LoadDataRedis.BBS_TOPIC_TOPICID + "" + topicIds.get(i));
                }
                List<Object> objectList1 = redisUtil.mget(topicIds);

                for (Object bbsTopicFullDtoItem : objectList1) {
                    BbsTopicFullDto bbsTopicFullDto = (BbsTopicFullDto) bbsTopicFullDtoItem;
                    bbsTopicFullDto.setPraiseCount(bbsTopicFullDto.getPraiseCount() + count);
                    redisUtil.set(LoadDataRedis.BBS_TOPIC_TOPICID + bbsTopicFullDto.getId(), bbsTopicFullDto, LoadDataRedis.BBS_TOPIC_TOPICID_TIME);
                }
                return null;
            }
        });
    }

    /**
     * 贴子收藏量+1
     */
    public void updateTopicStarCount(List<String> topicIds, int count) {
        List<Object> objectList = redisUtil.getRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i < topicIds.size(); i++) {
                    topicIds.set(i, LoadDataRedis.BBS_TOPIC_TOPICID + "" + topicIds.get(i));
                }
                List<Object> objectList1 = redisUtil.mget(topicIds);

                for (Object bbsTopicFullDtoItem : objectList1) {
                    BbsTopicFullDto bbsTopicFullDto = (BbsTopicFullDto) bbsTopicFullDtoItem;
                    bbsTopicFullDto.setStarCount(bbsTopicFullDto.getStarCount() + count);
                    redisUtil.set(LoadDataRedis.BBS_TOPIC_TOPICID + bbsTopicFullDto.getId(), bbsTopicFullDto, LoadDataRedis.BBS_TOPIC_TOPICID_TIME);
                }
                return null;
            }
        });
    }

    /**
     * 根据id查询帖子
     */
    public BbsTopicFullDto getTopicById(String topicId) {
        log.info("Redis--根据id查询帖子：" + topicId);
        return (BbsTopicFullDto) redisUtil.get(LoadDataRedis.BBS_TOPIC_TOPICID + topicId);
    }

    /**
     * 删除帖子
     */
    public void deleteTopicById(String topicId) {
        BbsTopicFullDto topicById = this.getTopicById(topicId);
        log.info("Redis--删除帖子：" + topicById);
        redisUtil.zRemove(LoadDataRedis.BBS_RANK_REGION_CLASS + topicById.getRegionCode() + "_" + topicById.getClassCode(),topicId);
        redisUtil.del(LoadDataRedis.BBS_TOPIC_TOPICID + topicId);
    }
}