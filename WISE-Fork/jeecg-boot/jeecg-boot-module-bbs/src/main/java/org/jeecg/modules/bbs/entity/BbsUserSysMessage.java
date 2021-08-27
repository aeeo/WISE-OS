package org.jeecg.modules.bbs.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 用户系统消息
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@ApiModel(value="bbs_user_sys_message对象", description="用户系统消息")
@Data
@TableName("bbs_user_sys_message")
public class BbsUserSysMessage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
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
	/**阅读状态*/
	@Excel(name = "阅读状态", width = 15, dicCode = "bbs_sys_message_read_status")
    @ApiModelProperty(value = "阅读状态")
    private java.lang.Integer status;
	/**区域编码*/
	@Excel(name = "区域编码", width = 15)
    @ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
	/**系统消息ID*/
    @ApiModelProperty(value = "系统消息ID")
    private java.lang.String sysMessageId;
	/**消息类型*/
	@Excel(name = "消息类型", width = 15, dicCode = "bbs_sys_message_type")
    @ApiModelProperty(value = "消息类型")
    private java.lang.String type;
	/**接收用户名*/
	@Excel(name = "接收用户名", width = 15)
    @ApiModelProperty(value = "接收用户名")
    private java.lang.String receiveUsername;
}
