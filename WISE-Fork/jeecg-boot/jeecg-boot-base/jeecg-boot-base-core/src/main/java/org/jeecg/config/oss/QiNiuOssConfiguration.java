package org.jeecg.config.oss;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecg.common.util.oss.QiNiuUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: feige
 * @Date: Created in 17:43 2019/2/25
 * @Description:
 */
@Configuration
public class QiNiuOssConfiguration {

    @Value("${jeecg.qiniuoss.accessKey}")
    private String accessKey;
    @Value("${jeecg.qiniuoss.secretKey}")
    private String secretKey;
    @Value("${jeecg.qiniuoss.bucket}")
    private String bucket;
    @Value("${jeecg.qiniuoss.doMain}")
    private String doMain;

    @Bean
    public void initQiniuOssUtilConfiguration() {
        QiNiuUtil.setAccessKey(accessKey);
        QiNiuUtil.setSecretKey(secretKey);
        QiNiuUtil.setBucket(bucket);
        QiNiuUtil.setDoMain(doMain);
    }
//    /**
//     * 华东机房,配置自己空间所在的区域
//     */
//    @Bean
//    public com.qiniu.storage.Configuration qiniuConfig() {
//        return new com.qiniu.storage.Configuration(Zone.zone2());
//    }
//
//    /**
//     * 构建一个七牛上传工具实例
//     */
//    @Bean
//    public UploadManager uploadManager() {
//        return new UploadManager(qiniuConfig());
//    }



//    /**
//     * 认证信息实例
//     * @return
//     */
//    @Bean
//    public Auth auth() {
//        return Auth.create(accessKey, secretKey);
//    }
//
//    /**
//     * 构建七牛空间管理实例
//     */
//    @Bean
//    public BucketManager bucketManager() {
//        return new BucketManager(auth(), qiniuConfig());
//    }


}