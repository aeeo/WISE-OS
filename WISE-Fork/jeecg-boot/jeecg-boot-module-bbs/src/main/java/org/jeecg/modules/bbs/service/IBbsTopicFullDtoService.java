package org.jeecg.modules.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.bbs.entity.BbsTopicFullDto;

import javax.servlet.http.HttpServletRequest;

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

    BbsTopicFullDto queryTopicFullDtoById(String topicId);

    IPage<BbsTopicFullDto> queryUserPublishTopicFullDto(Page<BbsTopicFullDto> page, HttpServletRequest req, int[] topicType, String username);

    BbsTopicFullDto queryTopicFullDtoByIdAnon(String topicId);
}
