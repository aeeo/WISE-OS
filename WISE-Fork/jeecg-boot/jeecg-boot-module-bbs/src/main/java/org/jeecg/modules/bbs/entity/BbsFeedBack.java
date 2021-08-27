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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 用户反馈
 * @Author: jeecg-boot
 * @Date: 2021-05-29
 * @Version: V1.0
 */
@Data
@TableName("bbs_feed_back")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bbs_feed_back对象", description = "用户反馈")
public class BbsFeedBack implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 用户
     */
    @ApiModelProperty(value = "用户")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
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
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 反馈内容
     */
    @Excel(name = "反馈内容", width = 15)
    @ApiModelProperty(value = "反馈内容")
    private java.lang.String content;
    /**
     * 处理结果
     */
    @Excel(name = "处理结果", width = 15)
    @ApiModelProperty(value = "处理结果")
    private java.lang.String disposeResult;
    /**
     * 处理人
     */
    @Excel(name = "处理人", width = 15)
    @ApiModelProperty(value = "处理人")
    private java.lang.String disposeUsername;
    /**
     * 处理历史
     */
    @Excel(name = "处理历史", width = 15)
    @ApiModelProperty(value = "处理历史")
    private java.lang.String disposeHistory;
    /**
     * 联系方式
     */
    @Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String userContact;
    /**
     * 处理进度
     */
    @Excel(name = "处理进度", width = 15, dicCode = "bbs_feedback_status")
    @Dict(dicCode = "bbs_feedback_status")
    @ApiModelProperty(value = "处理进度")
    private java.lang.String disposeStatus;
    /**
     * 处理时间
     */
    @Excel(name = "处理时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "处理时间")
    private java.util.Date disposeDate;
    // ****行星万象修改位置戳****
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private java.lang.String createByName;
    /**
     * 用户头像
     */
    @TableField(exist = false)
    private java.lang.String userAvatar;
    /**
     * 处理人姓名
     */
    @TableField(exist = false)
    private java.lang.String disposeRealName;
    /**
     * 处理人角色
     */
    @TableField(exist = false)
    private java.lang.String disposeUserRole;
    /**
     * 处理人头像
     */
    @TableField(exist = false)
    private java.lang.String disposeUserAvatar;

    /**
     * 反馈图片
     */
    @TableField(exist = false)
    private List<BbsTopicImage> imageUrl;
}
