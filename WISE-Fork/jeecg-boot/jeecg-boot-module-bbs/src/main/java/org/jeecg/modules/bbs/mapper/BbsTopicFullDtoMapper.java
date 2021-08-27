package org.jeecg.modules.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;
import org.jeecg.modules.bbs.entity.BbsUserPraise;
import org.jeecg.modules.bbs.entity.BbsUserStar;

import java.util.List;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2021-01-24
 * @Version: V1.0
 */
public interface BbsTopicFullDtoMapper extends BaseMapper<BbsTopicFullDto> {
    //topic固定不变的数据(分页批量)
    List<BbsTopicFullDto> queryTopicFullDtoFix(long current, long size, String regionCode, String classCode, int[] topicType);

    //topic固定不变的数据(分页批量)    用户发布的，不区分区域版块
    List<BbsTopicFullDto> queryUserPublishTopicFix(long current, long size, String regionCode, String classCode, int[] topicType, String username);

    //topic固定不变的数据(单条)
    BbsTopicFullDto queryTopicFullDtoFixById(String topicId);

    //topic根据用户不同改变的数据
    List<BbsUserStar> queryTopicFullDtoFle(List<String> topicIdList);

    List<BbsUserStar> queryTopicFullDtoUserStar(List<String> topicIdList, String username);

    List<BbsUserPraise> queryTopicFullDtoUserPraise(List<String> topicIdList, String username);
}
