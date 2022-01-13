package org.jeecg.modules.bbs.utils;

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
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BbsAuthUtils {
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

    /**
     * 每次打开小程序获取缓存数据，token需要生成
     *
     * @param token
     * @return
     */
    public MiNiStorage getMiNiTokenStorage(String token, String username) {
        MiNiStorage miNiStorageCache = (MiNiStorage) redisUtil.get(CommonConstant.PREFIX_MINI_USER_INFO + username);
        if (null != miNiStorageCache) {
            miNiStorageCache.setToken(token);
            return miNiStorageCache;
        }

        MiNiStorage miNiStorage = new MiNiStorage();
        miNiStorage.setToken(token);
        miNiStorage.setSysUser(sysUserService.getUserByName(username));
        BbsUserRecord bbsUserRecord = bbsUserRecordService.getFullUserRecord(username);
        miNiStorage.setBbsUserRecord(bbsUserRecord);
        log.info("getMiNiTokenStorage:username=" + username);
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        miNiStorage.setBbsRegion(bbsRegion);
        List<BbsClass> bbsClassList = bbsClassService.selectBbsClassListSort(bbsRegion.getRegionCode());
        miNiStorage.setBbsClassList(bbsClassList);
        miNiStorage.setUserBehaviorLimit(convertLimit(bbsRegion, bbsUserRecord));

        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_MINI_USER_INFO + username, miNiStorage);
        redisUtil.expire(CommonConstant.PREFIX_MINI_USER_INFO + username, 5 * 60 * 1000);           //5分钟
        return miNiStorage;
    }

    /**
     * 获取小程序缓存数据，token已经缓存，不用返回
     *
     * @return
     */
    public MiNiStorage getMiNiStorage(String username) {
        MiNiStorage miNiStorageCache = (MiNiStorage) redisUtil.get(CommonConstant.PREFIX_MINI_USER_INFO + username);
        if (null != miNiStorageCache) {
            return miNiStorageCache;
        }
        MiNiStorage miNiStorage = new MiNiStorage();
        if (username.isEmpty()) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            miNiStorage.setSysUser(sysUserService.getUserByName(loginUser.getUsername()));
        } else {
            //第一次登录时没有token，只能根据用户名获取
            miNiStorage.setSysUser(sysUserService.getUserByName(username));
        }
        BbsUserRecord bbsUserRecord = bbsUserRecordService.getFullUserRecord(username);
        miNiStorage.setBbsUserRecord(bbsUserRecord);
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        miNiStorage.setBbsRegion(bbsRegion);
        List<BbsClass> bbsClassList = bbsClassService.selectBbsClassListSort(bbsRegion.getRegionCode());
        miNiStorage.setBbsClassList(bbsClassList);
        miNiStorage.setUserBehaviorLimit(convertLimit(bbsRegion, bbsUserRecord));

        setMiNiStorage(miNiStorage);
        return miNiStorage;
    }
    /**
     * 缓存小程序用户信息
     * @return
     */
    public MiNiStorage setMiNiStorage(MiNiStorage miNiStorage) {
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_MINI_USER_INFO + miNiStorage.getSysUser().getUsername(), miNiStorage);
        redisUtil.expire(CommonConstant.PREFIX_MINI_USER_INFO + miNiStorage.getSysUser().getUsername(), 30 * 24 * 60 * 60 * 1000);           //30天
        return miNiStorage;
    }

    /**
     * 从数据库更新用户信息
     * @return
     */
    public MiNiStorage getMiNiStorageFromSql(String username) {
        MiNiStorage miNiStorage = new MiNiStorage();
        miNiStorage.setSysUser(sysUserService.getUserByName(username));
        BbsUserRecord bbsUserRecord = bbsUserRecordService.getFullUserRecord(username);
        miNiStorage.setBbsUserRecord(bbsUserRecord);
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        miNiStorage.setBbsRegion(bbsRegion);
        List<BbsClass> bbsClassList = bbsClassService.selectBbsClassListSort(bbsRegion.getRegionCode());
        miNiStorage.setBbsClassList(bbsClassList);
        miNiStorage.setUserBehaviorLimit(convertLimit(bbsRegion, bbsUserRecord));
        setMiNiStorage(miNiStorage);
        return miNiStorage;
    }

    //转化用户限制
    public UserBehaviorLimit convertLimit(BbsRegion bbsRegion, BbsUserRecord bbsUserRecord) {
        UserBehaviorLimit userBehaviorLimit = new UserBehaviorLimit();
        //发布贴子判断
        userBehaviorLimit.setCanDayPunlishTopic(bbsRegion.getDayPublishTopic() > bbsUserRecord.getDayPublishTopic());
        userBehaviorLimit.setCanWeekPunlishTopic(bbsRegion.getWeekPublishTopic() > bbsUserRecord.getWeekPublishTopic());
        //发布留言判断
        userBehaviorLimit.setCanDayPunlishMessage(bbsRegion.getDayPublishMessage() > bbsUserRecord.getDayPublishMessage());
        userBehaviorLimit.setCanWeekPunlishMessage(bbsRegion.getWeekPublishMessage() > bbsUserRecord.getWeekPublishMessage());
        //发布评论判断
        userBehaviorLimit.setCanDayPunlishReply(bbsRegion.getDayPublishReply() > bbsUserRecord.getDayPublishReply());
        userBehaviorLimit.setCanWeekPunlishReply(bbsRegion.getWeekPublishReply() > bbsUserRecord.getWeekPublishReply());
        //点赞判断
        userBehaviorLimit.setCanDayPunlishparise(bbsRegion.getDayPublishPraise() > bbsUserRecord.getDayPublishPraise());
        userBehaviorLimit.setCanWeekPunlishparise(bbsRegion.getWeekPublishPraise() > bbsUserRecord.getWeekPublishPraise());
        return userBehaviorLimit;
    }


    /**
     * 微信小程序登录接口
     */
    public String minilogin(SysLoginModel sysLoginModel) {
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();

        //1. 校验用户是否有效
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        Result result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            log.error("小程序登录--用户无效");
            return "";
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            log.error("小程序登录--用户名或密码错误");
            return "";
        }
        //获取token
        String token = loginAndGenerateToken(sysUser);

        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",小程序登录成功！", CommonConstant.LOG_TYPE_1, null, loginUser);

        return token;
    }

    /**
     * 小程序打开生成token和本地缓存信息
     *
     * @param sysUser
     * @return
     */
    private String loginAndGenerateToken(SysUser sysUser) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);     //生成签名，5分钟后过期，放到redis后长时间有效
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 30);
        return token;
    }

    //判断微信小程序用户是否授权个人基本信息
    public boolean judgeMiniUserAuth() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        SysUser sysUser1 = sysUserService.lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()).one();
        if (null == sysUser1.getRealname()) {
            return false;
        } else {
            return true;
        }
    }
}
