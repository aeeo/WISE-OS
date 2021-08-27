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

/**
 * @Description: 举报列表
 * @Author: jeecg-boot
 * @Date: 2021-05-26
 * @Version: V1.0
 */
@Data
@TableName("bbs_inform")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bbs_inform对象", description = "举报列表")
public class BbsInform implements Serializable {
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
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 举报类型
     */
    @Excel(name = "举报类型", width = 15, dicCode = "bbs_inform_type")
    @Dict(dicCode = "bbs_inform_type")
    @ApiModelProperty(value = "举报类型")
    private java.lang.Integer type;
    /**
     * 举报用户名
     */
    @Excel(name = "举报用户名", width = 15)
    @ApiModelProperty(value = "举报用户名")
    private java.lang.String informUsername;
    /**
     * 被举报用户名
     */
    @Excel(name = "被举报用户名", width = 15)
    @ApiModelProperty(value = "被举报用户名")
    private java.lang.String beInformUsername;
    /**
     * 处理结果
     */
    @Excel(name = "处理结果", width = 15, dicCode = "bbs_inform_result_type")
    @Dict(dicCode = "bbs_inform_result_type")
    @ApiModelProperty(value = "处理结果")
    private java.lang.Integer resultType;
    /**
     * 处理意见
     */
    @Excel(name = "处理意见", width = 15)
    @ApiModelProperty(value = "处理意见")
    private java.lang.String resultSuggestion;
    /**
     * 贴子ID
     */
    @Excel(name = "贴子ID", width = 15)
    @ApiModelProperty(value = "贴子ID")
    private java.lang.String topicId;
    /**
     * 评论ID
     */
    @Excel(name = "评论ID", width = 15)
    @ApiModelProperty(value = "评论ID")
    private java.lang.String replyId;
    /**
     * 留言ID
     */
    @Excel(name = "留言ID", width = 15)
    @ApiModelProperty(value = "留言ID")
    private java.lang.String messageId;


    // ****行星万象修改位置戳****
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private java.lang.String createByName;
    /**
     * 举报用户昵称
     */
    @TableField(exist = false)
    private java.lang.String informUsernameRealname;
    /**
     * 被举报用户昵称
     */
    @TableField(exist = false)
    private java.lang.String beInformUsernameRealname;
    /**
     * 帖子标题
     */
    @TableField(exist = false)
    private java.lang.String topicTitle;
    /**
     * 帖子内容
     */
    @TableField(exist = false)
    private java.lang.String topicContent;
    /**
     * 评论内容
     */
    @TableField(exist = false)
    private java.lang.String replyContent;
    /**
     * 留言内容
     */
    @TableField(exist = false)
    private java.lang.String messageContent;
    /**
     * 留言内容
     */
    @TableField(exist = false)
    private java.lang.String regionCode;
    /**
     * 留言内容
     */
    @TableField(exist = false)
    private java.lang.String regionName;
    /**
     * 留言内容
     */
    @TableField(exist = false)
    private java.lang.String classCode;
    /**
     * 留言内容
     */
    @TableField(exist = false)
    private java.lang.String className;
}
