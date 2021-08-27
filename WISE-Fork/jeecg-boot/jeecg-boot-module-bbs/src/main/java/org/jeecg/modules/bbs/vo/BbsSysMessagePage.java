package org.jeecg.modules.bbs.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.bbs.entity.BbsUserSysMessage;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 系统消息
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
@Data
@ApiModel(value = "bbs_sys_messagePage对象", description = "系统消息")
public class BbsSysMessagePage {

    /**
     * 主键
     */
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
     * 标题
     */
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**
     * 内容
     */
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**
     * 区域编码
     */
    @Excel(name = "区域编码", width = 15)
    @ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
    /**
     * 图片
     */
    @Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String imageUrl;
    /**
     * 推送状态
     */
    @Excel(name = "推送状态", width = 15)
    @ApiModelProperty(value = "推送状态")
    private java.lang.Integer status;
    /**
     * 贴子ID
     */
    @Excel(name = "贴子ID", width = 15)
    @ApiModelProperty(value = "贴子ID")
    private java.lang.String topicId;
    /**
     * 推送类型
     */
    @Excel(name = "推送类型", width = 15)
    @ApiModelProperty(value = "推送类型")
    private java.lang.String type;
    /**
     * 归类
     */
    @Excel(name = "归类", width = 15)
    @ApiModelProperty(value = "归类")
    private java.lang.String messageSame;
    /**
     * 消息排序
     */
    @Excel(name = "消息排序", width = 15)
    @ApiModelProperty(value = "消息排序")
    private java.lang.Integer messageSort;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;

    @ExcelCollection(name = "用户系统消息")
    @ApiModelProperty(value = "用户系统消息")
    private List<BbsUserSysMessage> bbsUserSysMessageList;

}
