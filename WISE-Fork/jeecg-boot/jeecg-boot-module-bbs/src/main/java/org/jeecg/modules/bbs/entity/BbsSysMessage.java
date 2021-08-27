package org.jeecg.modules.bbs.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 系统消息
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@ApiModel(value="bbs_sys_message对象", description="系统消息")
@Data
@TableName("bbs_sys_message")
public class BbsSysMessage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
	/**推送类型*/
	@Excel(name = "推送类型", width = 15, dicCode = "bbs_sys_message_type")
    @Dict(dicCode = "bbs_sys_message_type")
    @ApiModelProperty(value = "推送类型")
    private java.lang.String type;
	/**区域编码*/
	@Excel(name = "区域编码", width = 15)
    @ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
	/**封面*/
	@Excel(name = "封面", width = 15)
    @ApiModelProperty(value = "封面")
    private java.lang.String imageUrl;
	/**贴子ID*/
	@Excel(name = "贴子ID", width = 15)
    @ApiModelProperty(value = "贴子ID")
    private java.lang.String topicId;
	/**归类*/
	@Excel(name = "归类", width = 15)
    @ApiModelProperty(value = "归类")
    private java.lang.String messageSame;
	/**消息排序*/
	@Excel(name = "消息排序", width = 15)
    @ApiModelProperty(value = "消息排序")
    private java.lang.Integer messageSort;
	/**推送状态*/
	@Excel(name = "推送状态", width = 15, dicCode = "bbs_sys_message_status")
    @Dict(dicCode = "bbs_sys_message_status")
    @ApiModelProperty(value = "推送状态")
    private java.lang.Integer status;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
}
