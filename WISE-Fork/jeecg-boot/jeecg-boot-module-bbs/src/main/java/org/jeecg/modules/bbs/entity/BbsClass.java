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
 * @Description: 版块
 * @Author: jeecg-boot
 * @Date:   2021-05-02
 * @Version: V1.0
 */
@ApiModel(value="bbs_class对象", description="版块")
@Data
@TableName("bbs_class")
public class BbsClass implements Serializable {
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
	/**版块编码*/
	@Excel(name = "版块编码", width = 15)
    @ApiModelProperty(value = "版块编码")
    private java.lang.String classCode;
	/**版块名称*/
	@Excel(name = "版块名称", width = 15)
    @ApiModelProperty(value = "版块名称")
    private java.lang.String className;
	/**区域编码*/
    @ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
	/**版块排序*/
	@Excel(name = "版块排序", width = 15)
    @ApiModelProperty(value = "版块排序")
    private java.lang.Integer classSort;
	/**版块贴子数量*/
	@Excel(name = "版块贴子数量", width = 15)
    @ApiModelProperty(value = "版块贴子数量")
    private java.lang.Integer classTopicCount;
}
