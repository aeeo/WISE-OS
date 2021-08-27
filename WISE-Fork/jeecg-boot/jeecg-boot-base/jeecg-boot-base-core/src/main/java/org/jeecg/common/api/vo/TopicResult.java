package org.jeecg.common.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.constant.CommonConstant;

import java.io.Serializable;

/**
 *   接口返回数据格式
 *   返回代码 1000-1100
 *   1000内容有误
 *   	1001 	内容过长
 *   	1002	内容过短
 *   	1003	格式有误
 *   	1004	包含敏感信息
 * @author scott
 * @email jeecgos@163.com
 * @date  2019年1月19日
 */
@Data
@ApiModel(value="接口返回对象", description="接口返回对象")
public class TopicResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@ApiModelProperty(value = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回代码")
	private Integer code = 0;

	/**
	 * 返回数据对象 data
	 */
	@ApiModelProperty(value = "返回数据对象")
	private T result;

	/**
	 * 时间戳
	 */
	@ApiModelProperty(value = "时间戳")
	private long timestamp = System.currentTimeMillis();

	/**
	 * 前端请求时间戳（数据校验）
	 */
	@ApiModelProperty(value = "时间戳")
	private long timestampRequest = 0;

	/**
	 * 前端请求版块下标（数据校验）
	 */
	@ApiModelProperty(value = "时间戳")
	private int classIndex = -1;

	public TopicResult() {
		
	}
	
	public TopicResult<T> success(String message) {
		this.message = message;
		this.code = CommonConstant.SC_OK_200;
		this.success = true;
		return this;
	}

	@Deprecated
	public static TopicResult<Object> ok() {
		TopicResult<Object> r = new TopicResult<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage("成功");
		return r;
	}

	@Deprecated
	public static TopicResult<Object> ok(String msg) {
		TopicResult<Object> r = new TopicResult<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage(msg);
		return r;
	}

	@Deprecated
	public static TopicResult<Object> ok(Object data) {
		TopicResult<Object> r = new TopicResult<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setResult(data);
		return r;
	}
	public static<T> TopicResult<T> OK() {
		TopicResult<T> r = new TopicResult<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage("成功");
		return r;
	}

	public static<T> TopicResult<T> OK(T data) {
		TopicResult<T> r = new TopicResult<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setResult(data);
		return r;
	}
	public static<T> TopicResult<T> OK(T data,long timestampRequest,int classIndex) {
		TopicResult<T> r = new TopicResult<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setResult(data);

		r.setClassIndex(classIndex);
		r.setTimestampRequest(timestampRequest);
		return r;
	}

	public static<T> TopicResult<T> OK(String msg, T data) {
		TopicResult<T> r = new TopicResult<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}
	
	public static TopicResult<Object> error(String msg) {
		return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
	}
	
	public static TopicResult<Object> error(int code, String msg) {
		TopicResult<Object> r = new TopicResult<Object>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

	public TopicResult<T> error500(String message) {
		this.message = message;
		this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
		this.success = false;
		return this;
	}
	/**
	 * 无权限访问返回结果
	 */
	public static TopicResult<Object> noauth(String msg) {
		return error(CommonConstant.SC_JEECG_NO_AUTHZ, msg);
	}

	@JsonIgnore
	private String onlTable;

}