package org.jeecg.modules.bbs.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 互动消息
 * @Author: jeecg-boot
 * @Date:   2021-05-21
 * @Version: V1.0
 */
@Data
@TableName("bbs_user_message")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bbs_user_message对象", description="互动消息")
public class BbsUserMessage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**消息类型*/
	@Excel(name = "消息类型", width = 15)
    @ApiModelProperty(value = "消息类型")
    private String messageType;
	/**点赞评论用户ID*/
	//@Excel(name = "点赞评论用户ID", width = 15)
    //@ApiModelProperty(value = "点赞评论用户ID")
    //private String userId;
	/**点赞评论用户真实姓名*/
	//@Excel(name = "点赞评论用户真实姓名", width = 15)
    //@ApiModelProperty(value = "点赞评论用户真实姓名")
    //private String realName;
	/**评论内容*/
	@Excel(name = "评论内容", width = 15)
    @ApiModelProperty(value = "评论内容")
    private String replyContent;
	/**第一张图片*/
	@Excel(name = "第一张图片", width = 15)
    @ApiModelProperty(value = "第一张图片")
    private String topicImageUrl;
	/**留言ID*/
	@Excel(name = "留言ID", width = 15)
    @ApiModelProperty(value = "留言ID")
    private String messageBoardId;
	/**用户头像*/
	//@Excel(name = "用户头像", width = 15)
    //@ApiModelProperty(value = "用户头像")
    //private String receiveUserAvatar;
	/**接收消息用户名*/
	@Excel(name = "接收消息用户名", width = 15)
    @ApiModelProperty(value = "接收消息用户名")
    private String receiveUsername;
	/**贴子ID*/
	@Excel(name = "贴子ID", width = 15)
    @ApiModelProperty(value = "贴子ID")
    private String topicId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
	/**评论id*/
	@Excel(name = "评论id", width = 15)
    @ApiModelProperty(value = "评论id")
    private String replyId;
	/**区域编码*/
	@Excel(name = "区域编码", width = 15)
    @ApiModelProperty(value = "区域编码")
    private String regionCode;
	/**区域名*/
	//@Excel(name = "区域名", width = 15)
    //@ApiModelProperty(value = "区域名")
    //private String regionName;
}
