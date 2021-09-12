package org.jeecg.modules.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weixin")
public class WeiXinConfig {
    private String appid;
    private String secret;
}
