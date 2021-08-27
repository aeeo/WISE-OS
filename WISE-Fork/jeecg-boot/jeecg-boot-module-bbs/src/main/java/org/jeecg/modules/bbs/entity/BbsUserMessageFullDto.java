package org.jeecg.modules.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 互动消息
 * @Author: jeecg-boot
 * @Date: 2021-05-21
 * @Version: V1.0
 */
@Data
@TableName("bbs_user_message")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bbs_user_message对象", description = "互动消息")
public class BbsUserMessageFullDto extends BbsUserMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**点赞评论用户真实姓名*/
    @Excel(name = "点赞评论用户真实姓名", width = 15)
    @ApiModelProperty(value = "点赞评论用户真实姓名")
    private String realname;

    /**用户头像*/
    @Excel(name = "用户头像", width = 15)
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    /**区域名*/
    @Excel(name = "区域名", width = 15)
    @ApiModelProperty(value = "区域名")
    private String regionName;
}
