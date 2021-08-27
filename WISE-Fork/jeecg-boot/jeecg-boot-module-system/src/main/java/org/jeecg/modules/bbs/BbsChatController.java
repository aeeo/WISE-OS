package org.jeecg.modules.bbs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.bbs.utils.UserSigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "BBS_聊天")
@RestController
@RequestMapping("/bbs/bbsChat")
@Slf4j
public class BbsChatController {

    @Autowired
    private UserSigService userSigService;

    // ****行星万象修改位置戳****
    @GetMapping("/wise/mini/getUserSig")
    @AutoLog(value = "获取userSig")
    @ApiOperation(value = "获取userSig", notes = "获取userSig")
    public String getOpenId(@RequestParam(name = "username") String username) throws Exception {
        return userSigService.generateUserSig(username);
    }
}


