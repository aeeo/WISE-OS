package org.jeecg.modules.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 帖子回复
 * @Author: jeecg-boot
 * @Date: 2021-05-26
 * @Version: V1.0
 */
@Data
@TableName("bbs_reply")
@ApiModel(value = "bbs_reply对象", description = "帖子回复")
public class BbsReply implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 帖子ID
     */
    @Excel(name = "帖子ID", width = 15)
    @ApiModelProperty(value = "帖子ID")
    private java.lang.String topicId;
    /**
     * 评论内容
     */
    @Excel(name = "评论内容", width = 15)
    @ApiModelProperty(value = "评论内容")
    private java.lang.String content;
    /**
     * 用户
     */
    @ApiModelProperty(value = "用户")
    private java.lang.String createBy;
    /**
     * 点赞数
     */
    @Excel(name = "点赞数", width = 15)
    @ApiModelProperty(value = "点赞数")
    private java.lang.Integer praise;
    /**
     * 回复数
     */
    @Excel(name = "回复数", width = 15)
    @ApiModelProperty(value = "回复数")
    private java.lang.Integer replyCount;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 是否有子节点
     */
    @Excel(name = "是否有子节点", width = 15)
    @ApiModelProperty(value = "是否有子节点")
    private java.lang.String hasChild;
    /**
     * 父级节点
     */
    @Excel(name = "父级节点", width = 15)
    @ApiModelProperty(value = "父级节点")
    private java.lang.String pid;
    /**
     * 回复用户
     */
    @Excel(name = "回复用户", width = 15)
    @ApiModelProperty(value = "回复用户")
    private java.lang.String beReplyUsername;
    /**
     * 部门编码
     */
    @ApiModelProperty(value = "部门编码")
    private java.lang.String sysOrgCode;
    /**
     * 举报次数
     */
    @Excel(name = "举报次数", width = 15)
    @ApiModelProperty(value = "举报次数")
    private java.lang.Integer informCount;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15, dicCode = "bbs_status")
    @Dict(dicCode = "bbs_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**
     * 逻辑删除
     */
    @Excel(name = "逻辑删除", width = 15, dicCode = "bbs_delete_flag")
    @Dict(dicCode = "bbs_delete_flag")
    @ApiModelProperty(value = "逻辑删除")
    private java.lang.String deleteFlag;

    // ****行星万象修改位置戳****
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private java.lang.String createByName;
    /**
     * 贴子内容
     */
    @TableField(exist = false)
    private java.lang.String topicContent;


    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String realname;


    /**
     * 被回复用户头像
     */
    @TableField(exist = false)
    private String beReplyAvatar;

    /**
     * 被回复用户昵称
     */
    @TableField(exist = false)
    private String beReplyRealname;

    /**
     * 用户回复子节点
     */
    @TableField(exist = false)
    private List<BbsReply> childFullReply;

    /**
     * 用户是否点赞
     */
    @TableField(exist = false)
    private boolean userIsPraise;

}
