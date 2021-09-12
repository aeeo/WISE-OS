package org.jeecg.modules.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.List;

/**
 * @Description: 用户信息记录
 * @Author: jeecg-boot
 * @Date: 2021-05-27
 * @Version: V1.0
 */
@Data
@TableName("bbs_user_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bbs_user_record对象", description = "用户信息记录")
public class BbsUserRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
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
     * 用户账号
     */
    @Excel(name = "用户账号", width = 15)
    @ApiModelProperty(value = "用户账号")
    private java.lang.String userAccount;
    /**
     * 陨石数
     */
    @Excel(name = "陨石数", width = 15)
    @ApiModelProperty(value = "陨石数")
    private java.lang.Integer stoneCount;
    /**
     * 帖子数
     */
    @Excel(name = "帖子数", width = 15)
    @ApiModelProperty(value = "帖子数")
    private java.lang.Integer topicCount;
    /**
     * 留言数
     */
    @Excel(name = "留言数", width = 15)
    @ApiModelProperty(value = "留言数")
    private java.lang.Integer messageCount;
    /**
     * 消息数
     */
    @Excel(name = "消息数", width = 15)
    @ApiModelProperty(value = "消息数")
    private java.lang.Integer informationCount;
    /**
     * 收藏数
     */
    @Excel(name = "收藏数", width = 15)
    @ApiModelProperty(value = "收藏数")
    private java.lang.Integer starCount;
    /**
     * 被收藏数
     */
    @Excel(name = "被收藏数", width = 15)
    @ApiModelProperty(value = "被收藏数")
    private java.lang.Integer beStarCount;
    /**
     * 浏览帖子数
     */
    @Excel(name = "浏览帖子数", width = 15)
    @ApiModelProperty(value = "浏览帖子数")
    private java.lang.Integer clickTopicCount;
    /**
     * 评论数
     */
    @Excel(name = "评论数", width = 15)
    @ApiModelProperty(value = "评论数")
    private java.lang.Integer commentCount;
    /**
     * 被评论数
     */
    @Excel(name = "被评论数", width = 15)
    @ApiModelProperty(value = "被评论数")
    private java.lang.Integer beCommentCount;
    /**
     * 点赞数
     */
    @Excel(name = "点赞数", width = 15)
    @ApiModelProperty(value = "点赞数")
    private java.lang.Integer praiseCount;
    /**
     * 被赞数
     */
    @Excel(name = "被赞数", width = 15)
    @ApiModelProperty(value = "被赞数")
    private java.lang.Integer bePraiseCount;
    /**
     * 当天发布贴子数
     */
    @Excel(name = "当天发布贴子数", width = 15)
    @ApiModelProperty(value = "当天发布贴子数")
    private java.lang.Integer dayPublishTopic;
    /**
     * 本周发布贴子数
     */
    @Excel(name = "本周发布贴子数", width = 15)
    @ApiModelProperty(value = "本周发布贴子数")
    private java.lang.Integer weekPublishTopic;
    /**
     * 当天发布留言数
     */
    @Excel(name = "当天发布留言数", width = 15)
    @ApiModelProperty(value = "当天发布留言数")
    private java.lang.Integer dayPublishMessage;
    /**
     * 本周发布留言数
     */
    @Excel(name = "本周发布留言数", width = 15)
    @ApiModelProperty(value = "本周发布留言数")
    private java.lang.Integer weekPublishMessage;
    /**
     * 当天点赞次数
     */
    @Excel(name = "当天点赞次数", width = 15)
    @ApiModelProperty(value = "当天点赞次数")
    private java.lang.Integer dayPublishPraise;
    /**
     * 本周点赞次数
     */
    @Excel(name = "本周点赞次数", width = 15)
    @ApiModelProperty(value = "本周点赞次数")
    private java.lang.Integer weekPublishPraise;
    /**
     * 当天评论次数
     */
    @Excel(name = "当天评论次数", width = 15)
    @ApiModelProperty(value = "当天评论次数")
    private java.lang.Integer dayPublishReply;
    /**
     * 本周评论次数
     */
    @Excel(name = "本周评论次数", width = 15)
    @ApiModelProperty(value = "本周评论次数")
    private java.lang.Integer weekPublishReply;
    /**
     * 当前区域编码
     */
    @Excel(name = "当前区域编码", width = 15)
    @ApiModelProperty(value = "当前区域编码")
    private java.lang.String regionCode;
    /**
     * 本月区域切换次数
     */
    @Excel(name = "本月区域切换次数", width = 15)
    @ApiModelProperty(value = "本月区域切换次数")
    private java.lang.Integer regionSwitchCount;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;


    // ****行星万象修改位置戳****
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private java.lang.String createByName;

    /**
     * 当前区域名
     */
    @TableField(exist = false)
    private String regionFullName;
    /**
     * 头像
     */
    @TableField(exist = false)
    private String avatar;
    /**
     * 性别（1：男 2：女）
     */
    @TableField(exist = false)
    private Integer sex;

    /**
     * 系统消息数量
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "系统消息数量")
    private int userSysMessageCount;
    /**
     * 互动数量
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "互动数量")
    private int userMessageCount;

    /**
     * 发布贴子集
     */
    @TableField(exist = false)
    private List<BbsTopicFullDto> bbsTopicFullDtoList;

}
