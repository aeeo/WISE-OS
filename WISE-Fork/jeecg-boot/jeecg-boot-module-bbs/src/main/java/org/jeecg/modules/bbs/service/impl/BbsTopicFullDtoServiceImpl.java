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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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



    @Override
    public IPage<BbsTopicFullDto> queryTopicFullDto(Page<BbsTopicFullDto> page, String regionCode, String classCode, int[] topicType) {
        //先从redis拿
        Set<Object> bbsTopicFullDtos = redisUtil.zRange(LoadDataRedis.BBS_RANK_REGION_CLASS, page.getCurrent() - 1, page.getCurrent() + page.getSize());
        List<BbsTopicFullDto> bbsTopicFullDtosList1 = new ArrayList<>();
        while (bbsTopicFullDtos.iterator().hasNext()) {
            bbsTopicFullDtosList1.add((BbsTopicFullDto) bbsTopicFullDtos.iterator().next());
        }
        IPage<BbsTopicFullDto> page1 = new Page<BbsTopicFullDto> ();
        return page1.setRecords(bbsTopicFullDtosList1);

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
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
        //List<String> topicIdList = new ArrayList<>();
        //
        //for (BbsTopicFullDto record : bbsTopicFullDtosList) {
        //    topicIdList.add(record.getId());
        //}
        ////根据用户进行数据封装
        //if (topicIdList.size() != 0) {
        //    List<BbsUserStar> bbsUserStars = bbsTopicFullDtoMapper.queryTopicFullDtoUserStar(topicIdList, sysUser.getUsername());
        //    List<BbsUserPraise> bbsUserPraises = bbsTopicFullDtoMapper.queryTopicFullDtoUserPraise(topicIdList, sysUser.getUsername());
        //    //封装用户点赞和收藏信息
        //    for (BbsTopicFullDto record : bbsTopicFullDtosList) {
        //        for (BbsUserStar bbsUserStar : bbsUserStars) {
        //            if (record.getId().equals(bbsUserStar.getTopicId())) {
        //                record.setUserIsStar(true);
        //            }
        //        }
        //        for (BbsUserPraise bbsUserPraise : bbsUserPraises) {
        //            if (record.getId().equals(bbsUserPraise.getTopicId())) {
        //                record.setUserIsPraise(true);
        //            }
        //        }
        //    }
        //}
        //bbsTopicFullDtosList.sort((l, r) -> r.getCreateTime().compareTo(l.getCreateTime()));
        //return page;
    }

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
        bbsTopicFullDtosList.sort((l, r) -> r.getCreateTime().compareTo(l.getCreateTime()));
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
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //查出固定不变的数据（已审核的贴子）
        //手动分页，先筛选帖子，再封装数据
        BbsTopicFullDto bbsTopicFullDtosList = bbsTopicFullDtoMapper.queryTopicFullDtoFixById(topicId);
        if (null == bbsTopicFullDtosList) {
            return null;
        }
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

        //查出固定不变的数据（已审核的贴子）
        //手动分页，先筛选帖子，再封装数据
        BbsTopicFullDto bbsTopicFullDtosList = bbsTopicFullDtoMapper.queryTopicFullDtoFixById(topicId);
        bbsTopicFullDtosList.getBbsTopicLinkList().sort((l, r) -> l.getSort().compareTo(r.getSort()));

        return bbsTopicFullDtosList;
    }
}
