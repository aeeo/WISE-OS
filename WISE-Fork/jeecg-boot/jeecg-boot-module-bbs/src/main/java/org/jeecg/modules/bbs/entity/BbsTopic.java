package org.jeecg.modules.bbs.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2021-09-17
 * @Version: V1.0
 */
@ApiModel(value = "bbs_topic对象", description = "帖子")
@Data
@TableName("bbs_topic")
public class BbsTopic implements Serializable {
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
     * 版块专题ID
     */
    @Excel(name = "版块专题ID", width = 15)
    @ApiModelProperty(value = "版块专题ID")
    private java.lang.String speciaiId;
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
     * 类型
     */
    @Excel(name = "类型", width = 15, dicCode = "bbs_topic_type")
    @Dict(dicCode = "bbs_topic_type")
    @ApiModelProperty(value = "类型")
    private java.lang.String topicType;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    private java.lang.String enabled;
    /**
     * 访问总数
     */
    @Excel(name = "访问总数", width = 15)
    @ApiModelProperty(value = "访问总数")
    private java.lang.Integer hitsCount;
    /**
     * 回复总数
     */
    @Excel(name = "回复总数", width = 15)
    @ApiModelProperty(value = "回复总数")
    private java.lang.Integer replyCount;
    /**
     * 最后回复用户ID
     */
    @Excel(name = "最后回复用户ID", width = 15)
    @ApiModelProperty(value = "最后回复用户ID")
    private java.lang.String repliedBy;
    /**
     * 最后回复时间
     */
    @Excel(name = "最后回复时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后回复时间")
    private java.util.Date repliedTime;
    /**
     * 帖子图标ID
     */
    @Excel(name = "帖子图标ID", width = 15)
    @ApiModelProperty(value = "帖子图标ID")
    private java.lang.String iconId;
    /**
     * 是否删除
     */
    @Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.String delFlag;
    /**
     * 帖子展示图片
     */
    @Excel(name = "帖子展示图片", width = 15)
    @ApiModelProperty(value = "帖子展示图片")
    private java.lang.String topicImage;
    /**
     * 帖子状态
     */
    @Excel(name = "帖子状态", width = 15, dicCode = "bbs_status")
    @Dict(dicCode = "bbs_status")
    @ApiModelProperty(value = "帖子状态")
    private java.lang.String status;
    /**
     * 联系方式
     */
    @Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String contact;
    /**
     * 地址
     */
    @Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private java.lang.String site;
    /**
     * 热门回复ID
     */
    @Excel(name = "热门回复ID", width = 15)
    @ApiModelProperty(value = "热门回复ID")
    private java.lang.String hotReply;
    /**
     * 点赞总数
     */
    @Excel(name = "点赞总数", width = 15)
    @ApiModelProperty(value = "点赞总数")
    private java.lang.Integer praiseCount;
    /**
     * 收藏总数
     */
    @Excel(name = "收藏总数", width = 15)
    @ApiModelProperty(value = "收藏总数")
    private java.lang.Integer starCount;
    /**
     * 举报次数
     */
    @Excel(name = "举报次数", width = 15)
    @ApiModelProperty(value = "举报次数")
    private java.lang.Integer informCount;
    /**
     * 区域编码
     */
    @Excel(name = "区域编码", width = 15)
    @ApiModelProperty(value = "区域编码")
    private java.lang.String regionCode;
    /**
     * 区域全名
     */
    @Excel(name = "区域全名", width = 15)
    @ApiModelProperty(value = "区域全名")
    private java.lang.String regionFullName;
    /**
     * 版块编码
     */
    @Excel(name = "版块编码", width = 15)
    @ApiModelProperty(value = "版块编码")
    private java.lang.String classCode;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 发布时间
     */
    @Excel(name = "发布时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布时间")
    private java.util.Date publicTime;
    /**
     * 编辑时间
     */
    @Excel(name = "编辑时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "编辑时间")
    private java.util.Date editTime;

    /**
     * 是否匿名
     */
    @Excel(name = "是否匿名", width = 15, dicCode = "bbs_topic_anon")
    @Dict(dicCode = "bbs_topic_anon")
    @ApiModelProperty(value = "是否匿名")
    private java.lang.Integer anon;


    // ****行星万象修改位置戳****
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private java.lang.String createByName;
    /**
     * 用户头像
     */
    @TableField(exist = false)
    private java.lang.String createByAvatar;
    /**
     * 版块名
     */
    @TableField(exist = false)
    private java.lang.String className;

}
