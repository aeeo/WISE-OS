package org.jeecg.modules.bbs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.bbs.controller.BbsRegionController;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.jeecg.modules.bbs.service.IBbsRegionService;
import org.jeecg.modules.bbs.service.IBbsReplyService;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.jeecg.modules.bbs.utils.BbsAuthUtils;
import org.jeecg.modules.config.WeiXinConfig;
import org.jeecg.modules.system.controller.LoginController;
import org.jeecg.modules.system.controller.SysUserController;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: BBS认证
 * @Author: jeecg-boot
 * @Date: 2020-12-10
 * @Version: V1.0
 */
@Api(tags = "BBS_认证")
@RestController
@RequestMapping("/bbs/bbsAuth")
@Slf4j
public class BbsAuthController extends JeecgController<BbsReply, IBbsReplyService> {

    @Autowired
    WeiXinConfig weiXinConfig;

    @Autowired
    SysUserController sysUserController;
    @Autowired
    LoginController loginController;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private BbsRegionController bbsRegionController;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IBbsClassService bbsClassService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private BaseCommonService baseCommonService;
    @Autowired
    private BbsAuthUtils bbsAuthUtils;

    // ****行星万象修改位置戳****
    @GetMapping("/wise/mini/getToken")
    @AutoLog(value = "获取token和本地缓存数据")
    @ApiOperation(value = "获取token", notes = "获取token和本地缓存数据")
    public Result<?> getOpenId(@RequestParam(name = "code") String code, @RequestParam(name = "regionCode") String regionCode) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + weiXinConfig.getAppid() + "&secret=" + weiXinConfig.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        //{"session_key":"mCSO3hJF8f6YJKt2yoyfXg==","openid":"oYFNg5QoBlDs3Gz7UebDLvf58lUc"}
        String body = responseEntity.getBody();
        JSONObject parse = JSONObject.parseObject(body);
        String openid = parse.get("openid").toString();

        //先登录，失败再添加
        SysLoginModel sysLoginModel = new SysLoginModel();
        sysLoginModel.setUsername(openid);
        sysLoginModel.setPassword(openid);
        String token = bbsAuthUtils.minilogin(sysLoginModel);       //小程序登录，无验证码
        if (!token.isEmpty()) {
            //之前历史原因，如果没有部门，添加默认部门，部门关联在区域编码上，如果有部门，就不管了
            SysUser userByName = sysUserService.getUserByName(openid);
            ArrayList<String> list = new ArrayList<>();
            list.add(userByName.getId());
            Map<String, String> depNamesByUserIds = sysUserService.getDepNamesByUserIds(list);
            if (depNamesByUserIds.size() == 0) {
                //部门分为用户部门和当前所在部门
                sysUserService.addUserWithDepart(userByName, "4ed96f554f9f4a219a7e282b7005e26f");//默认设置用户部门西安文理学院
                sysUserService.updateUserDepart(userByName.getUsername(), "A03A03A01A01");  //设置用户当前所在部门
            }

            //用户如果是通过分享进入，所在区域为分享链接中的区域，否则，不做设置
            if (null != regionCode && !"undefined".equals(regionCode)) {
                BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, regionCode).one();
                if (null != regionOne) {
                    bbsRegionController.switchRegion(regionOne, openid);
                } else {
                    //如果此区域不存在，则忽略
                }
            }
            return Result.OK(bbsAuthUtils.getMiNiTokenStorage(token, openid));
        }
        //添加用户，添加用户和用户记录，此处按理应该添加事务，等升级到微服务一起处理
        SysUser user = new SysUser();
        user.setCreateTime(new Date());// 设置创建时间
        String salt = oConvertUtils.randomGen(8);
        String passwordEncode = PasswordUtil.encrypt(openid, openid, salt);
        user.setSalt(salt);
        user.setUsername(openid);
        user.setUpdateBy("o_XHg4pJvz9U7d_RoU-oDWPiqBVE");   //行星万象
        user.setUpdateTime(new Date());
        user.setCreateBy("o_XHg4pJvz9U7d_RoU-oDWPiqBVE");
        user.setCreateTime(new Date());
        user.setPassword(passwordEncode);
        //邮箱和手机号不能重复
        user.setStatus(CommonConstant.USER_UNFREEZE);
        user.setDelFlag(CommonConstant.DEL_FLAG_0);
        user.setActivitiSync(CommonConstant.ACT_SYNC_0);

        sysUserService.addUserWithRole(user, "1345343571064266754");//默认设置角色为BBS_普通用户 1345343571064266754
        sysUserService.addUserWithDepart(user, "4ed96f554f9f4a219a7e282b7005e26f");//默认设置用户部门西安文理学院
        SysUser userByName = sysUserService.getUserByName(user.getUsername());
        sysUserService.updateUserDepart(userByName.getUsername(), "A03A03A01A01");  //设置用户当前所在部门

        //在创建用户的同时创建用户信息记录表
        BbsUserRecord bbsUserRecord = new BbsUserRecord();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, openid).one();
        bbsUserRecord.setCreateBy(sysUser.getUsername());


        bbsUserRecordService.save(bbsUserRecord);
        //用户如果是通过分享进入，所在区域为分享链接中的区域，否则，不做设置
        if (null != regionCode && !"undefined".equals(regionCode) && !"".equals(regionCode)) {
            BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, regionCode).one();
            if (null != regionOne) {
                bbsRegionController.switchRegion(regionOne, openid);
            } else {
                //如果此区域不存在，则忽略
            }
        }

        //登录
        token = bbsAuthUtils.minilogin(sysLoginModel);
        return Result.OK(bbsAuthUtils.getMiNiTokenStorage(token, openid));
    }

    /**
     * 用户行为限制
     *
     * @return
     */
    @AutoLog(value = "用户行为限制")
    @ApiOperation(value = "用户行为限制", notes = "用户行为限制")
    @GetMapping(value = "/wise/mini/userBehaviorLimit")
    public Result<?> queryPageList() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        BbsUserRecord bbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        return Result.OK(bbsAuthUtils.convertLimit(bbsRegion, bbsUserRecord));
    }

    @RequestMapping(value = "/wise/mini/querySysUser", method = RequestMethod.GET)
    public Result<?> querySysUser() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysUser userByName = sysUserService.getUserByName(sysUser.getUsername());
        return Result.OK(userByName);
    }

    @AutoLog(value = "完善用户信息")
    @ApiOperation(value = "完善用户信息", notes = "微信小程序用户授权后完善用户信息")
    @PostMapping("/wise/mini/perfectUser")
    public Result<?> perfectUser(@RequestBody Map<String, String> userInfo) {
        return sysUserService.perfectUser(userInfo);
    }


    @AutoLog(value = "获取小程序缓存数据")
    @ApiOperation(value = "获取小程序缓存数据", notes = "获取小程序缓存数据")
    @GetMapping("/wise/mini/getUserAllInfo")
    public Result<?> getMiNiStorageController() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.OK(bbsAuthUtils.getMiNiStorage(sysUser.getUsername()));
    }
}
