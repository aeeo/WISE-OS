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
 * @Description: 外卖推广
 * @Author: jeecg-boot
 * @Date: 2021-05-27
 * @Version: V1.0
 */
@Data
@TableName("bbs_waimai")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bbs_waimai对象", description = "外卖推广")
public class BbsWaimai implements Serializable {
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
     * 类型
     */
    @Excel(name = "类型", width = 15, dicCode = "bbs_waimai_type")
    @Dict(dicCode = "bbs_waimai_type")
    @ApiModelProperty(value = "类型")
    private java.lang.Integer type;
    /**
     * Logo
     */
    @Excel(name = "Logo", width = 15)
    @ApiModelProperty(value = "Logo")
    private java.lang.String logoUrl;
    /**
     * 标题
     */
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**
     * 标语
     */
    @Excel(name = "标语", width = 15)
    @ApiModelProperty(value = "标语")
    private java.lang.String tagline;
    /**
     * 主图
     */
    @Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
    private java.lang.String masterUrl;
    /**
     * APPID
     */
    @Excel(name = "APPID", width = 15)
    @ApiModelProperty(value = "APPID")
    private java.lang.String appId;
    /**
     * PATH
     */
    @Excel(name = "PATH", width = 15)
    @ApiModelProperty(value = "PATH")
    private java.lang.String path;
    /**
     * 逻辑删除标志
     */
    @Excel(name = "逻辑删除标志", width = 15, dicCode = "bbs_delete_flag")
    @Dict(dicCode = "bbs_delete_flag")
    @ApiModelProperty(value = "逻辑删除标志")
    private java.lang.Integer deleteFlag;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15, dicCode = "bbs_status")
    @Dict(dicCode = "bbs_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**
     * 序号
     */
    @Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer sort;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    // ****行星万象修改位置戳****
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private java.lang.String createByName;
}
