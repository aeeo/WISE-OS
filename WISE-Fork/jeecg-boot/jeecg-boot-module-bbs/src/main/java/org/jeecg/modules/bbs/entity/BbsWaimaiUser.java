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
 * @Description: 用户点击外卖记录
 * @Author: jeecg-boot
 * @Date:   2021-05-27
 * @Version: V1.0
 */
@Data
@TableName("bbs_waimai_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bbs_waimai_user对象", description="用户点击外卖记录")
public class BbsWaimaiUser implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
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
	/**外卖ID*/
	@Excel(name = "外卖ID", width = 15)
    @ApiModelProperty(value = "外卖ID")
    private java.lang.String waimaiId;
	/**区域编码*/
	@Excel(name = "区域编码", width = 15)
    @ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
	/**用户*/
    @ApiModelProperty(value = "用户")
    private java.lang.String createBy;
	/**进入方式*/
	@Excel(name = "进入方式", width = 15, dicCode = "bbs_user_waimai_type")
	@Dict(dicCode = "bbs_user_waimai_type")
    @ApiModelProperty(value = "进入方式")
    private java.lang.Integer type;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**使用状态1*/
	@Excel(name = "使用状态1", width = 15, dicCode = "bbs_user_waimai_use_status")
	@Dict(dicCode = "bbs_user_waimai_use_status")
    @ApiModelProperty(value = "使用状态1")
    private java.lang.Integer useStatus;

    // ****行星万象修改位置戳****
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private java.lang.String createByName;
    /**
     * 外卖标题
     */
    @TableField(exist = false)
    private java.lang.String waimaiTitle;
    /**
     * 区域名
     */
    @TableField(exist = false)
    private java.lang.String regionName;

}
