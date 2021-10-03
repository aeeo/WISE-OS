package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;
import org.jeecg.modules.bbs.entity.BbsUserPraise;
import org.jeecg.modules.bbs.entity.BbsUserStar;
import org.jeecg.modules.bbs.mapper.BbsTopicFullDtoMapper;
import org.jeecg.modules.bbs.mapper.BbsTopicMapper;
import org.jeecg.modules.bbs.service.IBbsTopicFullDtoService;
import org.jeecg.modules.cache.BbsRedisUtils;
import org.jeecg.modules.cache.LoadDataRedis;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2021-01-24
 * @Version: V1.0
 */
@Service
@Slf4j
public class BbsTopicFullDtoServiceImpl extends ServiceImpl<BbsTopicFullDtoMapper, BbsTopicFullDto> implements IBbsTopicFullDtoService {
    @Resource
    private BbsTopicMapper bbsTopicMapper;
    @Resource
    private BbsTopicFullDtoMapper bbsTopicFullDtoMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BbsRedisUtils bbsRedisUtils;


    @Override
    public IPage<BbsTopicFullDto> queryTopicFullDto(Page<BbsTopicFullDto> page, String regionCode, String classCode, int[] topicType) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //从redis拿
        Set<Object> bbsTopicFullDtos = redisUtil.zReverseRange(LoadDataRedis.BBS_RANK_REGION_CLASS + regionCode + "_" + classCode, (page.getCurrent() - 1) * page.getSize(), page.getCurrent() * page.getSize() - 1);
        Long zSize = redisUtil.zSize(LoadDataRedis.BBS_RANK_REGION_CLASS + regionCode + "_" + classCode);
        List<BbsTopicFullDto> bbsTopicFullDtosList = new ArrayList<>();
        Iterator<Object> iterator = bbsTopicFullDtos.iterator();
        ArrayList<String> topicIdList1 = new ArrayList<>();
        while (iterator.hasNext()) {
            topicIdList1.add(LoadDataRedis.BBS_TOPIC_TOPICID + (String) iterator.next());
        }
        List<Object> bbsTopicFullDto1 = (List<Object>)redisUtil.mget(topicIdList1);
        for (Object o : bbsTopicFullDto1) {
            //如果帖子在redis中不存在，则需要查库
            if (null == o) {
                BbsTopicFullDto bbsTopicFullDto = (BbsTopicFullDto) o;
                BbsTopicFullDto bbsTopicFullDto2 = queryTopicFullDtoById(bbsTopicFullDto.getId());
                if(null!=bbsTopicFullDto2){
                    bbsTopicFullDtosList.add(bbsTopicFullDto2);
                }else{
//                    如果库里也没有应该删除redis排行榜中的记录，但是zset删除没看懂，先留空，不重要
//                    Set<Object> objects = new HashSet<>();
//                    objects.add(bbsTopicFullDto.getId());
//                    redisUtil.zRmove(LoadDataRedis.BBS_RANK_REGION_CLASS + regionCode + "_" + classCode,objects);
                }
            } else {
                bbsTopicFullDtosList.add((BbsTopicFullDto) o);
            }
        }
        page.setRecords(bbsTopicFullDtosList);
        page.setTotal(zSize);

        //
        //String classCodeDeal = classCode;
        //if ("index".equals(classCodeDeal)) {
        //    classCodeDeal = "";
        //}
        ////查出固定不变的数据（已审核的贴子）
        ////手动分页，先筛选帖子，再封装数据
        //List<BbsTopicFullDto> bbsTopicFullDtosList = bbsTopicFullDtoMapper.queryTopicFullDtoFix((page.getCurrent() - 1) * page.getSize(), page.getSize(), regionCode, classCodeDeal, topicType);
        ////置顶帖只显示当前区域的
        //for (int i = 0; i < bbsTopicFullDtosList.size(); i++) {
        //    if (bbsTopicFullDtosList.get(i).getTopicType().equals("1")) {
        //        if (!classCode.equals(bbsTopicFullDtosList.get(i).getClassCode())) {
        //            bbsTopicFullDtosList.remove(i);
        //        }
        //    }
        //}
        ////图片实体类加上构造方法，贴子如果没有图片，还是会返回一个空图片实体，去除
        //for (int i = 0; i < bbsTopicFullDtosList.size(); i++) {
        //    if (1 == bbsTopicFullDtosList.get(i).getBbsTopicImageList().size() && null == bbsTopicFullDtosList.get(i).getBbsTopicImageList().get(0).getId()) {
        //        bbsTopicFullDtosList.get(i).setBbsTopicImageList(null);
        //    }
        //    if (1 == bbsTopicFullDtosList.get(i).getBbsTopicTagList().size() && null == bbsTopicFullDtosList.get(i).getBbsTopicTagList().get(0).getId()) {
        //        bbsTopicFullDtosList.get(i).setBbsTopicTagList(null);
        //    }
        //}
        //
        //page.setRecords(bbsTopicFullDtosList);
        //
        List<String> topicIdList = new ArrayList<>();
        for (BbsTopicFullDto record : bbsTopicFullDtosList) {
            topicIdList.add(record.getId());
        }
        //根据用户进行数据封装
        if (topicIdList.size() != 0) {
            List<BbsUserStar> bbsUserStars = bbsTopicFullDtoMapper.queryTopicFullDtoUserStar(topicIdList, sysUser.getUsername());
            List<BbsUserPraise> bbsUserPraises = bbsTopicFullDtoMapper.queryTopicFullDtoUserPraise(topicIdList, sysUser.getUsername());
            //封装用户点赞和收藏信息
            for (BbsTopicFullDto record : bbsTopicFullDtosList) {
                record.setUserIsStar(false);        //清空缓存的信息
                record.setUserIsPraise(false);
                for (BbsUserStar bbsUserStar : bbsUserStars) {
                    if (record.getId().equals(bbsUserStar.getTopicId())) {
                        record.setUserIsStar(true);
                    }
                }
                for (BbsUserPraise bbsUserPraise : bbsUserPraises) {
                    if (record.getId().equals(bbsUserPraise.getTopicId())) {
                        record.setUserIsPraise(true);
                    }
                }
            }
        }
        bbsTopicFullDtosList.sort((l, r) -> r.getPublicTime().compareTo(l.getPublicTime()));
        return page;
    }

    /**
     * redis加载专用
     * @param page
     * @param regionCode
     * @param classCode
     * @param topicType
     * @return
     */
    @Override
    public IPage<BbsTopicFullDto> queryTopicFullDtoFix(Page<BbsTopicFullDto> page, String regionCode, String classCode, int[] topicType) {
        String classCodeDeal = classCode;
        if ("index".equals(classCodeDeal)) {
            classCodeDeal = "";
        }
        //查出固定不变的数据（已审核的贴子）
        //手动分页，先筛选帖子，再封装数据
        List<BbsTopicFullDto> bbsTopicFullDtosList = bbsTopicFullDtoMapper.queryTopicFullDtoFix((page.getCurrent() - 1) * page.getSize(), page.getSize(), regionCode, classCodeDeal, topicType);
        //置顶帖只显示当前区域的
        for (int i = 0; i < bbsTopicFullDtosList.size(); i++) {
            if (bbsTopicFullDtosList.get(i).getTopicType().equals("1")) {
                if (!classCode.equals(bbsTopicFullDtosList.get(i).getClassCode())) {
                    bbsTopicFullDtosList.remove(i);
                }
            }
        }
        //图片实体类加上构造方法，贴子如果没有图片，还是会返回一个空图片实体，去除
        for (int i = 0; i < bbsTopicFullDtosList.size(); i++) {
            if (1 == bbsTopicFullDtosList.get(i).getBbsTopicImageList().size() && null == bbsTopicFullDtosList.get(i).getBbsTopicImageList().get(0).getId()) {
                bbsTopicFullDtosList.get(i).setBbsTopicImageList(null);
            }
            if (1 == bbsTopicFullDtosList.get(i).getBbsTopicTagList().size() && null == bbsTopicFullDtosList.get(i).getBbsTopicTagList().get(0).getId()) {
                bbsTopicFullDtosList.get(i).setBbsTopicTagList(null);
            }
        }

        page.setRecords(bbsTopicFullDtosList);

        List<String> topicIdList = new ArrayList<>();
        for (BbsTopicFullDto record : bbsTopicFullDtosList) {
            topicIdList.add(record.getId());
        }
        bbsTopicFullDtosList.sort((l, r) -> r.getPublicTime().compareTo(l.getPublicTime()));
        return page;
    }

    @Override
    public IPage<BbsTopicFullDto> queryUserPublishTopicFullDto(Page<BbsTopicFullDto> page, HttpServletRequest req, int[] topicType, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String regionCode = req.getHeader("regioncode");
        String classCode = req.getHeader("classCode");
        if ("index".equals(classCode)) {
            classCode = "";
        }
        //查出固定不变的数据（已审核的贴子）
        //手动分页，先筛选帖子，再封装数据
        List<BbsTopicFullDto> bbsTopicFullDtosList = bbsTopicFullDtoMapper.queryUserPublishTopicFix((page.getCurrent() - 1) * page.getSize(), page.getSize(), regionCode, classCode, topicType, username);


        //图片实体类加上构造方法，贴子如果没有图片，还是会返回一个空图片实体，去除
        for (int i = 0; i < bbsTopicFullDtosList.size(); i++) {
            if (1 == bbsTopicFullDtosList.get(i).getBbsTopicImageList().size() && null == bbsTopicFullDtosList.get(i).getBbsTopicImageList().get(0).getId()) {
                bbsTopicFullDtosList.get(i).setBbsTopicImageList(null);
            }
            if (1 == bbsTopicFullDtosList.get(i).getBbsTopicTagList().size() && null == bbsTopicFullDtosList.get(i).getBbsTopicTagList().get(0).getId()) {
                bbsTopicFullDtosList.get(i).setBbsTopicTagList(null);
            }
        }

        page.setRecords(bbsTopicFullDtosList);

        List<String> topicIdList = new ArrayList<>();

        for (BbsTopicFullDto record : bbsTopicFullDtosList) {
            topicIdList.add(record.getId());
        }
        //根据用户进行数据封装
        if (topicIdList.size() != 0) {
            List<BbsUserStar> bbsUserStars = bbsTopicFullDtoMapper.queryTopicFullDtoUserStar(topicIdList, sysUser.getUsername());
            List<BbsUserPraise> bbsUserPraises = bbsTopicFullDtoMapper.queryTopicFullDtoUserPraise(topicIdList, sysUser.getUsername());
            //封装用户点赞和收藏信息
            for (BbsTopicFullDto record : bbsTopicFullDtosList) {
                for (BbsUserStar bbsUserStar : bbsUserStars) {
                    if (record.getId().equals(bbsUserStar.getTopicId())) {
                        record.setUserIsStar(true);
                    }
                }
                for (BbsUserPraise bbsUserPraise : bbsUserPraises) {
                    if (record.getId().equals(bbsUserPraise.getTopicId())) {
                        record.setUserIsPraise(true);
                    }
                }
            }
        }
        bbsTopicFullDtosList.sort((l, r) -> r.getCreateTime().compareTo(l.getCreateTime()));


        return page;
    }


    @Override
    public BbsTopicFullDto queryTopicFullDtoById(String topicId) {
        BbsTopicFullDto bbsTopicFullDto = (BbsTopicFullDto)redisUtil.get(LoadDataRedis.BBS_TOPIC_TOPICID + topicId);
        if (null != bbsTopicFullDto) {
            List<String> bbsTopicFullDtos = new ArrayList<>();
            bbsTopicFullDtos.add(topicId);
            bbsRedisUtils.updateTopicHitCount(bbsTopicFullDtos, 1, Integer.MAX_VALUE);
            bbsTopicFullDto.setHitsCount(bbsTopicFullDto.getHitsCount() + 1);
            return bbsTopicFullDto;
        }
        BbsTopicFullDto bbsTopicFullDto1 = queryTopicFullDtoByIdFromSql(topicId);
        return bbsTopicFullDto1;
    }
    @Override
    public BbsTopicFullDto queryTopicFullDtoByIdFromSql(String topicId) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查出固定不变的数据（已审核的贴子）
        //手动分页，先筛选帖子，再封装数据
        BbsTopicFullDto bbsTopicFullDtosList = bbsTopicFullDtoMapper.queryTopicFullDtoFixById(topicId);
        if (null == bbsTopicFullDtosList) {
            return null;
        }
        //更新redis
        bbsTopicFullDtosList.setHitsCount(bbsTopicFullDtosList.getHitsCount() + 1);
        redisUtil.set(LoadDataRedis.BBS_TOPIC_TOPICID + topicId,bbsTopicFullDtosList);
        bbsTopicFullDtosList.getBbsTopicLinkList().sort((l, r) -> l.getSort().compareTo(r.getSort()));
        //只有1条，但还是封装list
        List<String> topicIdList = new ArrayList<>();
        topicIdList.add(bbsTopicFullDtosList.getId());
        //根据用户进行数据封装
        if (topicIdList.size() != 0) {
            List<BbsUserStar> bbsUserStars = bbsTopicFullDtoMapper.queryTopicFullDtoUserStar(topicIdList, sysUser.getUsername());
            List<BbsUserPraise> bbsUserPraises = bbsTopicFullDtoMapper.queryTopicFullDtoUserPraise(topicIdList, sysUser.getUsername());
            //封装用户点赞和收藏信息
            if (!bbsUserStars.isEmpty()) {
                bbsTopicFullDtosList.setUserIsStar(true);
            }
            if (!bbsUserPraises.isEmpty()) {
                bbsTopicFullDtosList.setUserIsPraise(true);
            }
        }
        return bbsTopicFullDtosList;
    }

    @Override
    public BbsTopicFullDto queryTopicFullDtoByIdAnon(String topicId) {
        BbsTopicFullDto bbsTopicFullDto = (BbsTopicFullDto)redisUtil.get(LoadDataRedis.BBS_TOPIC_TOPICID + topicId);
        if (null != bbsTopicFullDto) {
            List<String> bbsTopicFullDtos = new ArrayList<>();
            bbsTopicFullDtos.add(topicId);
            bbsRedisUtils.updateTopicHitCount(bbsTopicFullDtos, 1, Integer.MAX_VALUE);
            bbsTopicFullDto.setHitsCount(bbsTopicFullDto.getHitsCount() + 1);
            return bbsTopicFullDto;
        }

        //查出固定不变的数据（已审核的贴子）
        //手动分页，先筛选帖子，再封装数据
        BbsTopicFullDto bbsTopicFullDtosList = bbsTopicFullDtoMapper.queryTopicFullDtoFixById(topicId);
        bbsTopicFullDtosList.getBbsTopicLinkList().sort((l, r) -> l.getSort().compareTo(r.getSort()));
        return bbsTopicFullDtosList;
    }
}
