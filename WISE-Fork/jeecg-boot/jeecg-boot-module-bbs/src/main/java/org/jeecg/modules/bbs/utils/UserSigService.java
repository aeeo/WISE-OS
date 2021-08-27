package org.jeecg.modules.bbs.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserSigService {
    @Value("${IMConfig.sdkAppId}")
    private long sdkAppId;
    @Value("${IMConfig.secretKey}")
    private String secretKey;
    private long expire = 60 * 60 * 24 * 7;


    public String generateUserSig(String username){
        TLSSigAPIv2 api = new TLSSigAPIv2(sdkAppId, secretKey);
        return api.genUserSig(username, expire);
    }
}
