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
 * @Description: 活动
 * @Author: jeecg-boot
 * @Date: 2021-06-12
 * @Version: V1.0
 */
@Data
@TableName("bbs_activity")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bbs_activity对象", description = "活动")
public class BbsActivity implements Serializable {
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
     * 活动标题
     */
    @Excel(name = "活动标题", width = 15)
    @ApiModelProperty(value = "活动标题")
    private java.lang.String title;
    /**
     * 封面
     */
    @Excel(name = "封面", width = 15)
    @ApiModelProperty(value = "封面")
    private java.lang.String imageUrl;
    /**
     * 活动内容
     */
    @Excel(name = "活动内容", width = 15)
    @ApiModelProperty(value = "活动内容")
    private java.lang.String content;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;
    /**
     * 截止时间
     */
    @Excel(name = "截止时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止时间")
    private java.util.Date endTime;
    /**
     * 需要人数
     */
    @Excel(name = "需要人数", width = 15)
    @ApiModelProperty(value = "需要人数")
    private java.lang.Integer needPeopleCount;
    /**
     * 活动现状
     */
    @Excel(name = "活动现状", width = 15)
    @ApiModelProperty(value = "活动现状")
    private java.lang.String activitySituation;
    /**
     * 负责人
     */
    @Excel(name = "负责人", width = 15)
    @ApiModelProperty(value = "负责人")
    private java.lang.String princiaplPeopleName;
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
     * 访问总数
     */
    @Excel(name = "访问总数", width = 15)
    @ApiModelProperty(value = "访问总数")
    private java.lang.Integer hitsCount;
    /**
     * 省市区
     */
    @Excel(name = "省市区", width = 15)
    @ApiModelProperty(value = "省市区")
    private java.lang.String provinces;
    /**
     * 地点
     */
    @Excel(name = "地点", width = 15)
    @ApiModelProperty(value = "地点")
    private java.lang.String site;
    /**
     * 活动类型
     */
    @Excel(name = "活动类型", width = 15, dicCode = "bbs_activity_type")
    @Dict(dicCode = "bbs_activity_type")
    @ApiModelProperty(value = "活动类型")
    private java.lang.String activityType;
    /**
     * 推送类型
     */
    @Excel(name = "推送类型", width = 15, dicCode = "bbs_activity_push_type")
    @Dict(dicCode = "bbs_activity_push_type")
    @ApiModelProperty(value = "推送类型")
    private java.lang.String pushType;
    /**
     * 区域编码
     */
    @Excel(name = "区域编码", width = 15)
    @ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
    /**
     * 跳转链接
     */
    @Excel(name = "跳转链接", width = 15)
    @ApiModelProperty(value = "跳转链接")
    private java.lang.String skipUrl;

    // ****行星万象修改位置戳****
    /**
     * 是否已报名
     */
    @TableField(exist = false)
    private java.lang.Boolean isApply;

    /**
     * 姓名
     */
    @TableField(exist = false)

    private java.lang.String applyChineseName;
    /**
     * 手机
     */
    @TableField(exist = false)

    private java.lang.String applyPhoneNumber;
    /**
     * 微信
     */
    @TableField(exist = false)

    private java.lang.String applyWechat;
    /**
     * 备注
     */
    @TableField(exist = false)

    private java.lang.String applyRemark;
}
