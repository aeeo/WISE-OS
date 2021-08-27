package org.jeecg.modules.bbs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 用户反馈
 * @Author: jeecg-boot
 * @Date:   2021-01-24
 * @Version: V1.0
 */
@Data
@TableName("bbs_feed_back")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bbs_feed_back对象", description="用户反馈")
public class BbsFeedBackFullDto extends BbsFeedBack implements Serializable {
    private static final long serialVersionUID = 1L;

}
