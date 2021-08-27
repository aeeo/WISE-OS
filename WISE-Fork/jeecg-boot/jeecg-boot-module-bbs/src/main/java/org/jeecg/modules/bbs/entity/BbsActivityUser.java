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

/**
 * @Description: 活动用户
 * @Author: jeecg-boot
 * @Date: 2021-06-14
 * @Version: V1.0
 */
@Data
@TableName("bbs_activity_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bbs_activity_user对象", description = "活动用户")
public class BbsActivityUser implements Serializable {
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
     * 活动ID
     */
    @Excel(name = "活动ID", width = 15)
    @ApiModelProperty(value = "活动ID")
    private java.lang.String activityId;
    /**
     * 姓名
     */
    @Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String chineseName;
    /**
     * 手机
     */
    @Excel(name = "手机", width = 15)
    @ApiModelProperty(value = "手机")
    private java.lang.String phoneNumber;
    /**
     * 微信
     */
    @Excel(name = "微信", width = 15)
    @ApiModelProperty(value = "微信")
    private java.lang.String wechat;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**
     * 报名时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报名时间")
    private java.util.Date createTime;
    /**
     * 报名状态
     */
    @Excel(name = "报名状态", width = 15)
    @ApiModelProperty(value = "报名状态")
    private java.lang.String applyStatus;


    // ****行星万象修改位置戳****
    /**
     * 是否已报名
     */
    @TableField(exist = false)
    private java.lang.String activityTitle;
}
