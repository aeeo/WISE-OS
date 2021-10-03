package org.jeecg.modules.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;
import org.jeecg.modules.bbs.entity.BbsUserPraise;
import org.jeecg.modules.bbs.entity.BbsUserStar;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2021-01-24
 * @Version: V1.0
 */
public interface IBbsTopicFullDtoService extends IService<BbsTopicFullDto> {

    /**
     * 固定信息，包含是否点赞，是否收藏
     * @param page
     * @param regionCode
     * @param classCode
     * @param topicType
     * @return
     */
    IPage<BbsTopicFullDto> queryTopicFullDto(Page<BbsTopicFullDto> page, String regionCode, String classCode, int[] topicType);

    /**
     * 固定信息，不包含是否点赞，是否收藏
     * @param page
     * @param regionCode
     * @param classCode
     * @param topicType
     * @return
     */
    IPage<BbsTopicFullDto> queryTopicFullDtoFix(Page<BbsTopicFullDto> page, String regionCode, String classCode, int[] topicType);

    //先redis后库
    BbsTopicFullDto queryTopicFullDtoById(String topicId);

    //直接库
    BbsTopicFullDto queryTopicFullDtoByIdFromSql(String topicId);

    IPage<BbsTopicFullDto> queryUserPublishTopicFullDto(Page<BbsTopicFullDto> page, HttpServletRequest req, int[] topicType, String username);

    BbsTopicFullDto queryTopicFullDtoByIdAnon(String topicId);

    List<BbsUserStar> queryTopicFullDtoUserStar(List<String> topicIdList, String username);
    List<BbsUserPraise> queryTopicFullDtoUserPraise(List<String> topicIdList, String username);

}
