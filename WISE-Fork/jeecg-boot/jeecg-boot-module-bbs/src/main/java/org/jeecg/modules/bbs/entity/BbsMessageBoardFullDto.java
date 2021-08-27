package org.jeecg.modules.bbs.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 留言板
 * @Author: jeecg-boot
 * @Date: 2021-01-02
 * @Version: V1.0
 */
@Data
@ApiModel(value = "bbs_message_board完整对象", description = "完整留言板")
public class BbsMessageBoardFullDto extends BbsMessageBoard implements Serializable {
    /**
     * 用户是否点赞
     */
    @ApiModelProperty(value = "userIsPraise")
    private Boolean userIsPraise;
}
