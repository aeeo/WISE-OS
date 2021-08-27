package org.jeecg.common.util.oss;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.oss.QiNiuOssConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云工具
 *
 * @author Zero
 * @date 2019-05-28
 */
@Slf4j
@Data
public class QiNiuUtil {
    private static QiNiuOssConfiguration qiNiuOssConfiguration = new QiNiuOssConfiguration();;

    /**
     * 基础配置
     */
    private static String accessKey;
    private static String secretKey;
    private static String bucket;
    private static String doMain;

    public static String getAccessKey() {
        return accessKey;
    }

    public static void setAccessKey(String accessKey) {
        QiNiuUtil.accessKey = accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        QiNiuUtil.secretKey = secretKey;
    }

    public static String getBucket() {
        return bucket;
    }

    public static void setBucket(String bucket) {
        QiNiuUtil.bucket = bucket;
    }

    public static String getDoMain() {
        return doMain;
    }

    public static void setDoMain(String doMain) {
        QiNiuUtil.doMain = doMain;
    }

    //构造一个带指定 Region 对象的配置类
    static Configuration cfg = new Configuration(Zone.zone2());
    static UploadManager uploadManager = new UploadManager(cfg);

    /**
     * 七牛云上传文件  暂时无用
     *
     * @param file     文件地址
     * @param fileName 文件名
     * @return 返回结果
     */
    public static String fileUpload(File file, String fileName) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg;
        cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "file/" + System.currentTimeMillis();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String result;
        try {
            Response response = uploadManager.put(file, key + fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            //DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("{七牛文件上传key: " + putRet.key + ",七牛excel文件传hash: " + putRet.hash + "}");
            result = doMain + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            result = null;
        }
        return result;
    }

    /**
     * 认证信息实例
     *
     * @return
     */
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }
    /**
     * 获取uptoken，用于微信小程序客户端上传
     */
    public static String getUptoken() {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    /**
     * 文件流上传  返回文件名
     */
    public static String uploadStreamFile(MultipartFile multipartFile,String fileType) {

        //默认文件名为 用户id + 时间戳 + 后缀
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String key = sysUser.getId() + "_" +  System.currentTimeMillis() + "." + fileType;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            //以流的方式上传
            InputStream inputStream = multipartFile.getInputStream();
            Response response = uploadManager.put(inputStream, key, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 删除文件
     */
    public static Boolean delete(String file) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        //String key = "https://" + doMain + "/" +file;
        String key = file;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
            return false;
        }
        log.info("删除文件：" + file);
        return true;
    }




    /**
     * 微信头像上传，返回文件名
     * @param inputStream
     * @param fileType
     * @return
     */
    public String uploadStream(InputStream inputStream,String fileType) {
        //默认文件名为 用户id + 时间戳 + 后缀
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String key = sysUser.getId() + "_" +  System.currentTimeMillis() + "." + fileType;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            //以流的方式上传
            Response response = uploadManager.put(inputStream, key, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return key;
    }
}
