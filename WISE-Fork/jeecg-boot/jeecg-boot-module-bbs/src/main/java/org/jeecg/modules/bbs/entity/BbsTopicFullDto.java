package org.jeecg.modules.bbs.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2020-12-16
 * @Version: V1.0
 */
@ApiModel(value = "bbs_topic对象", description = "帖子")
@Data
public class BbsTopicFullDto extends BbsTopic implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "bbsCreateById")
    private String bbsCreateByName;
    @ApiModelProperty(value = "avatar")
    private String avatar;
    @ApiModelProperty(value = "userRole")
    private String userRole;

    @ApiModelProperty(value = "bbsTopicImage")
    private List<BbsTopicImage> bbsTopicImageList;

    @ApiModelProperty(value = "bbsTopicTagList")
    private List<BbsTopicTag> bbsTopicTagList;

    @ApiModelProperty(value = "bbsTopicLinkList")
    private List<BbsTopicLink> bbsTopicLinkList;

    @ApiModelProperty(value = "sex")
    private String sex;

    /**
     * 用户是否点赞
     */
    @ApiModelProperty(value = "bbsTopicIsPraise")
    private Boolean userIsPraise;
    /**
     * 用户是否收藏
     */
    @ApiModelProperty(value = "userIsStar")
    private Boolean userIsStar;
}
