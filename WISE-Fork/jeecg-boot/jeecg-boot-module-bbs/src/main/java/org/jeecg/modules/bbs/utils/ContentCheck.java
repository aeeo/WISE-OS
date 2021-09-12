package org.jeecg.modules.bbs.utils;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsSys;
import org.jeecg.modules.bbs.service.IBbsSysService;
import org.jeecg.modules.config.WeiXinConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 内容审核工具类
 *
 * @author mrzhao
 */
@Component
public class ContentCheck {

    @Autowired
    private IBbsSysService bbsSysService;

    /**
     *初始化Bean   类加@Component
     */
    public static ContentCheck contentCheck;
    @PostConstruct
    public void init() {
        contentCheck = this;
    }

    //https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/sec-check/security.msgSecCheck.html
    //https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
    //access_token 的存储至少要保留 512 个字符空间；
    //access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
    //建议开发者使用中控服务器统一获取和刷新 access_token，其他业务逻辑服务器所使用的 access_token 均来自于该中控服务器，不应该各自去刷新，否则容易造成冲突，导致 access_token 覆盖而影响业务；
    //access_token 的有效期通过返回的 expires_in 来传达，目前是7200秒之内的值，中控服务器需要根据这个有效时间提前去刷新。在刷新过程中，中控服务器可对外继续输出的老 access_token，此时公众平台后台会保证在5分钟内，新老 access_token 都可用，这保证了第三方业务的平滑过渡；
    //access_token 的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新 access_token 的接口，这样便于业务服务器在API调用获知 access_token 已超时的情况下，可以触发 access_token 的刷新流程。
    @Autowired
    WeiXinConfig weiXinConfig;



    public String getAccessToken() {
        String URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + contentCheck.weiXinConfig.getAppid() + "&secret=" + contentCheck.weiXinConfig.getSecret();
        String CHECKURL = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL, String.class);
        String body = responseEntity.getBody();
        JSONObject parse = JSONObject.parseObject(body);
        return parse.get("access_token").toString();
    }

    public boolean stringCheck(String content, String accessToken) {
        String URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + contentCheck.weiXinConfig.getAppid() + "&secret=" + contentCheck.weiXinConfig.getSecret();
        String CHECKURL = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=";

        String checkUrl = CHECKURL + accessToken;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject jsonObjectDate = new JSONObject();
        jsonObjectDate.put("content", content);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(checkUrl, jsonObjectDate, String.class);
        String body = responseEntity.getBody();
        JSONObject parse = JSONObject.parseObject(body);
        String errcode = parse.get("errcode").toString();
        if (!errcode.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    //敏感词内容审核
    public Result<?> checkBySensitiveWord(String content) {
        //敏感词内容审核
        BbsSys bbsSensitiveWord = contentCheck.bbsSysService.lambdaQuery().eq(BbsSys::getSysKey, "sensitive_word").one();
        String[] sensitiveWordArr = bbsSensitiveWord.getSysValueString().split("\\|");
        for (int i = 0; i < sensitiveWordArr.length; i++) {
            if (content.contains(sensitiveWordArr[i])) {
                return Result.error("包含敏感信息，请修改后发布！");
            }
        }
        return Result.OK();
    }

    //微信接口内容审核
    public Result<?> checkByWeiXin(String content) {
        //微信接口内容审核
        BbsSys bbsSys = contentCheck.bbsSysService.lambdaQuery().eq(BbsSys::getSysKey, "access_token").one();
        Date date = new Date();
        //查询 access_token是否过期   设置为1小时
        boolean contentCheckResult = false;
        if ((date.getTime() - bbsSys.getUpdateTime().getTime()) > 7200000 / 2) {
            //过期，重新获取
            String accessToken = this.getAccessToken();
            //入库
            boolean updateSuccess = contentCheck.bbsSysService.lambdaUpdate().eq(BbsSys::getId, bbsSys.getId()).set(BbsSys::getSysValueString, accessToken).set(BbsSys::getUpdateTime, date).update();
            if (updateSuccess) {
                contentCheckResult = this.stringCheck(content, accessToken);
            } else {
                System.out.printf("更新access_token失败");
            }
        } else {
            contentCheckResult = this.stringCheck(content, bbsSys.getSysValueString());
        }
        if (contentCheckResult) {
            return Result.OK();
        } else {
            return Result.error("包含敏感信息，请修改后发布！");
        }
    }
}
