package org.jeecg.modules.bbs.vo;

import java.util.List;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsClass;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 地区
 * @Author: jeecg-boot
 * @Date:   2021-11-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="bbs_regionPage对象", description="地区")
public class BbsRegionPage {

	/**主键*/
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
	/**区域编码*/
	@Excel(name = "区域编码", width = 15)
	@ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
	/**全称*/
	@Excel(name = "全称", width = 15)
	@ApiModelProperty(value = "全称")
    private java.lang.String fullName;
	/**简称*/
	@Excel(name = "简称", width = 15)
	@ApiModelProperty(value = "简称")
    private java.lang.String name;
	/**经度*/
	@Excel(name = "经度", width = 15)
	@ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
	@ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
	/**地图展示名*/
	@Excel(name = "地图展示名", width = 15)
	@ApiModelProperty(value = "地图展示名")
    private java.lang.String content;
	/**地区id*/
	@Excel(name = "地区id", width = 15)
	@ApiModelProperty(value = "地区id")
    private java.lang.Integer regionId;
	/**省份*/
	@Excel(name = "省份", width = 15)
	@ApiModelProperty(value = "省份")
    private java.lang.String province;
	/**每日发布贴子数*/
	@Excel(name = "每日发布贴子数", width = 15)
	@ApiModelProperty(value = "每日发布贴子数")
    private java.lang.Integer dayPublishTopic;
	/**每周发布贴子数*/
	@Excel(name = "每周发布贴子数", width = 15)
	@ApiModelProperty(value = "每周发布贴子数")
    private java.lang.Integer weekPublishTopic;
	/**每日发布留言数*/
	@Excel(name = "每日发布留言数", width = 15)
	@ApiModelProperty(value = "每日发布留言数")
    private java.lang.Integer dayPublishMessage;
	/**每周发布留言数*/
	@Excel(name = "每周发布留言数", width = 15)
	@ApiModelProperty(value = "每周发布留言数")
    private java.lang.Integer weekPublishMessage;
	/**每日评论数*/
	@Excel(name = "每日评论数", width = 15)
	@ApiModelProperty(value = "每日评论数")
    private java.lang.Integer dayPublishReply;
	/**每周评论数*/
	@Excel(name = "每周评论数", width = 15)
	@ApiModelProperty(value = "每周评论数")
    private java.lang.Integer weekPublishReply;
	/**每日点赞数*/
	@Excel(name = "每日点赞数", width = 15)
	@ApiModelProperty(value = "每日点赞数")
    private java.lang.Integer dayPublishPraise;
	/**每周点赞数*/
	@Excel(name = "每周点赞数", width = 15)
	@ApiModelProperty(value = "每周点赞数")
    private java.lang.Integer weekPublishPraise;
	/**区域类型*/
	@Excel(name = "区域类型", width = 15, dicCode = "bbs_region_type")
    @Dict(dicCode = "bbs_region_type")
	@ApiModelProperty(value = "区域类型")
    private java.lang.Integer regionType;
	/**缩放等级*/
	@Excel(name = "缩放等级", width = 15)
	@ApiModelProperty(value = "缩放等级")
    private java.lang.Integer scale;
	/**范围半径*/
	@Excel(name = "范围半径", width = 15)
	@ApiModelProperty(value = "范围半径")
    private java.lang.Integer radius;
	/**区域等级*/
	@Excel(name = "区域等级", width = 15)
	@ApiModelProperty(value = "区域等级")
    private java.lang.Integer regionGrade;
	/**运营者账号*/
	@Excel(name = "运营者账号", width = 15)
	@ApiModelProperty(value = "运营者账号")
    private java.lang.String operatorUsername;
	/**公众号二维码*/
	@Excel(name = "公众号二维码", width = 15)
	@ApiModelProperty(value = "公众号二维码")
    private java.lang.String officeAccountImage;
	/**区域头像*/
	@Excel(name = "区域头像", width = 15)
	@ApiModelProperty(value = "区域头像")
    private java.lang.String regionImage;
	/**收款码*/
	@Excel(name = "收款码", width = 15)
	@ApiModelProperty(value = "收款码")
    private java.lang.String payeeImage;
	/**朋友圈二维码*/
	@Excel(name = "朋友圈二维码", width = 15)
	@ApiModelProperty(value = "朋友圈二维码")
    private java.lang.String circleFriendsImage;
	/**横向偏移量*/
	@Excel(name = "横向偏移量", width = 15)
	@ApiModelProperty(value = "横向偏移量")
    private java.lang.Integer anchorx;
	/**纵向偏移量*/
	@Excel(name = "纵向偏移量", width = 15)
	@ApiModelProperty(value = "纵向偏移量")
    private java.lang.Integer anchory;
	/**文本边缘留白*/
	@Excel(name = "文本边缘留白", width = 15)
	@ApiModelProperty(value = "文本边缘留白")
    private java.lang.Integer padding;
	/**显示方式*/
	@Excel(name = "显示方式", width = 15)
	@ApiModelProperty(value = "显示方式")
    private java.lang.String display;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "bbs_region_region_status")
    @Dict(dicCode = "bbs_region_region_status")
	@ApiModelProperty(value = "状态")
    private java.lang.Integer regionStatus;
	/**区域关联部门ID*/
	@Excel(name = "区域关联部门ID", width = 15)
	@ApiModelProperty(value = "区域关联部门ID")
    private java.lang.String regionDepartId;
	/**小商店ID*/
	@Excel(name = "小商店ID", width = 15)
	@ApiModelProperty(value = "小商店ID")
    private java.lang.String miniStoreAppid;
	/**私有区域*/
	@Excel(name = "私有区域", width = 15)
	@ApiModelProperty(value = "私有区域")
    private java.lang.Integer isPrivate;

	@ExcelCollection(name="版块")
	@ApiModelProperty(value = "版块")
	private List<BbsClass> bbsClassList;

}
