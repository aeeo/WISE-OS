package org.jeecg.modules.system.service.impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oss.QiNiuUtil;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.bbs.utils.BbsAuthUtils;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysUserDepVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author: scott
 * @Date: 2018-12-20
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysDepartRoleUserMapper departRoleUserMapper;
    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;
    @Resource
    private BaseCommonService baseCommonService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BbsAuthUtils bbsAuthUtils;

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
        SysUser user = userMapper.getUserByName(username);
        String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            return Result.error("旧密码输入错误!");
        }
        if (oConvertUtils.isEmpty(newpassword)) {
            return Result.error("新密码不允许为空!");
        }
        if (!newpassword.equals(confirmpassword)) {
            return Result.error("两次输入密码不一致!");
        }
        String password = PasswordUtil.encrypt(username, newpassword, user.getSalt());
        this.userMapper.update(new SysUser().setPassword(password), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        return Result.ok("密码重置成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> changePassword(SysUser sysUser) {
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setPassword(passwordEncode);
        this.userMapper.updateById(sysUser);
        return Result.ok("密码修改成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String userId) {
        //1.删除用户
        this.removeById(userId);
        return false;
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchUsers(String userIds) {
        //1.删除用户
        this.removeByIds(Arrays.asList(userIds.split(",")));
        return false;
    }

    @Override
    public SysUser getUserByName(String username) {
        return userMapper.getUserByName(username);
    }


    @Override
    @Transactional
    public void addUserWithRole(SysUser user, String roles) {
        this.save(user);
        if (oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    @Transactional
    public void editUserWithRole(SysUser user, String roles) {
        this.updateById(user);
        //先删后加
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        if (oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }


    @Override
    public List<String> getRole(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    @Override
    public Set<String> getUserRolesSet(String username) {
        // 查询用户拥有的角色集合
        List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
//        log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
        return new HashSet<>(roles);
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    @Override
    public Set<String> getUserPermissionsSet(String username) {
        Set<String> permissionSet = new HashSet<>();
        List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
        for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
            if (oConvertUtils.isNotEmpty(po.getPerms())) {
                permissionSet.add(po.getPerms());
            }
        }
        log.info("-------通过数据库读取用户拥有的权限Perms------username： " + username + ",Perms size: " + (permissionSet == null ? 0 : permissionSet.size()));
        return permissionSet;
    }

    @Override
    public SysUserCacheInfo getCacheUser(String username) {
        SysUserCacheInfo info = new SysUserCacheInfo();
        info.setOneDepart(true);
//		SysUser user = userMapper.getUserByName(username);
//		info.setSysUserCode(user.getUsername());
//		info.setSysUserName(user.getRealname());


        LoginUser user = sysBaseAPI.getUserByName(username);
        if (user != null) {
            info.setSysUserCode(user.getUsername());
            info.setSysUserName(user.getRealname());
            info.setSysOrgCode(user.getOrgCode());
        }

        //多部门支持in查询
        List<SysDepart> list = sysDepartMapper.queryUserDeparts(user.getId());
        List<String> sysMultiOrgCode = new ArrayList<String>();
        if (list == null || list.size() == 0) {
            //当前用户无部门
            //sysMultiOrgCode.add("0");
        } else if (list.size() == 1) {
            sysMultiOrgCode.add(list.get(0).getOrgCode());
        } else {
            info.setOneDepart(false);
            for (SysDepart dpt : list) {
                sysMultiOrgCode.add(dpt.getOrgCode());
            }
        }
        info.setSysMultiOrgCode(sysMultiOrgCode);

        return info;
    }

    // 根据部门Id查询
    @Override
    public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username) {
        return userMapper.getUserByDepId(page, departId, username);
    }

    @Override
    public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username) {
        return userMapper.getUserByDepIds(page, departIds, username);
    }

    @Override
    public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
        List<SysUserDepVo> list = this.baseMapper.getDepNamesByUserIds(userIds);

        Map<String, String> res = new HashMap<String, String>();
        list.forEach(item -> {
                    if (res.get(item.getUserId()) == null) {
                        res.put(item.getUserId(), item.getDepartName());
                    } else {
                        res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
                    }
                }
        );
        return res;
    }

    @Override
    public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = queryWrapper.lambda();

        lambdaQueryWrapper.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_0);
        lambdaQueryWrapper.inSql(SysUser::getId, "SELECT user_id FROM sys_user_depart WHERE dep_id = '" + departId + "'");

        return userMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page) {
        List<SysUserSysDepartModel> list = baseMapper.getUserByOrgCode(page, orgCode, userParams);
        Integer total = baseMapper.getUserByOrgCodeTotal(orgCode, userParams);

        IPage<SysUserSysDepartModel> result = new Page<>(page.getCurrent(), page.getSize(), total);
        result.setRecords(list);

        return result;
    }

    // 根据角色Id查询
    @Override
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
        return userMapper.getUserByRoleId(page, roleId, username);
    }


    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, key = "#username")
    public void updateUserDepart(String username, String orgCode) {
        baseMapper.updateUserDepart(username, orgCode);
    }


    @Override
    public SysUser getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }


    @Override
    public SysUser getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    @Transactional
    public void addUserWithDepart(SysUser user, String selectedParts) {
//		this.save(user);  //保存角色的时候已经添加过一次了
        if (oConvertUtils.isNotEmpty(selectedParts)) {
            String[] arr = selectedParts.split(",");
            for (String deaprtId : arr) {
                SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
                sysUserDepartMapper.insert(userDeaprt);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public void editUserWithDepart(SysUser user, String departs) {
        this.updateById(user);  //更新角色的时候已经更新了一次了，可以再跟新一次
        String[] arr = {};
        if (oConvertUtils.isNotEmpty(departs)) {
            arr = departs.split(",");
        }
        //查询已关联部门
        List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        if (userDepartList != null && userDepartList.size() > 0) {
            for (SysUserDepart depart : userDepartList) {
                //修改已关联部门删除部门用户角色关系
                if (!Arrays.asList(arr).contains(depart.getDepId())) {
                    List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(
                            new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId, depart.getDepId()));
                    List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
                    if (roleIds != null && roleIds.size() > 0) {
                        departRoleUserMapper.delete(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, user.getId())
                                .in(SysDepartRoleUser::getDroleId, roleIds));
                    }
                }
            }
        }
        //先删后加
        sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        if (oConvertUtils.isNotEmpty(departs)) {
            for (String departId : arr) {
                SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
                sysUserDepartMapper.insert(userDepart);
            }
        }
    }


    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(SysUser sysUser) {
        Result<?> result = new Result<Object>();
        //情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在，请注册");
            baseCommonService.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }
        //情况2：根据用户信息查询，该用户已注销
        //update-begin---author:王帅   Date:20200601  for：if条件永远为falsebug------------
        if (CommonConstant.DEL_FLAG_1.equals(sysUser.getDelFlag())) {
            //update-end---author:王帅   Date:20200601  for：if条件永远为falsebug------------
            baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已注销");
            return result;
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
            baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }

    @Override
    public List<SysUser> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_1);
        return userMapper.selectLogicDeleted(wrapper);
    }

    @Override
    public boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity) {
        String ids = String.format("'%s'", String.join("','", userIds));
        return userMapper.revertLogicDeleted(ids, updateEntity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> userIds) {
        String ids = String.format("'%s'", String.join("','", userIds));
        // 1. 删除用户
        int line = userMapper.deleteLogicDeleted(ids);
        // 2. 删除用户部门关系
        line += sysUserDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getUserId, userIds));
        //3. 删除用户角色关系
        line += sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
        return line != 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNullPhoneEmail() {
        userMapper.updateNullByEmptyString("email");
        userMapper.updateNullByEmptyString("phone");
        return true;
    }

    @Override
    public void saveThirdUser(SysUser sysUser) {
        //保存用户
        String userid = UUIDGenerator.generate();
        sysUser.setId(userid);
        baseMapper.insert(sysUser);
        //获取第三方角色
        SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, "third_role"));
        //保存用户角色
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(sysRole.getId());
        userRole.setUserId(userid);
        sysUserRoleMapper.insert(userRole);
    }

    @Override
    public List<SysUser> queryByDepIds(List<String> departIds, String username) {
        return userMapper.queryByDepIds(departIds, username);
    }

    @Override
    public Result<?> perfectUser(Map<String, String> userInfo) {
        //iv = iv.replace(' ', '+');
        //encryptedData = encryptedData.replace(' ', '+');
        ////获取openid和sessionKey
        //String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + "wx1c762e241bcb316c" + "&secret=" + "c38d02dd83470f1ab1e0708cb04ee407" + "&js_code=" + code + "&grant_type=authorization_code";
        //RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        ////{"session_key":"mCSO3hJF8f6YJKt2yoyfXg==","openid":"oYFNg5QoBlDs3Gz7UebDLvf58lUc"}
        //String body = responseEntity.getBody();
        //JSONObject parse = JSONObject.parseObject(body);
        //String openid = parse.get("openid").toString();
        //String sessionKey = parse.get("session_key").toString();
        //
        //// 被加密的数据
        //byte[] dataByte = cn.hutool.core.codec.Base64.decode(encryptedData);
        //// 加密秘钥
        //byte[] keyByte = cn.hutool.core.codec.Base64.decode(sessionKey);
        //// 偏移量
        //byte[] ivByte = Base64.decode(iv);
        //try {
        //	// 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        //	int base = 16;
        //	if (keyByte.length % base != 0) {
        //		int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
        //		byte[] temp = new byte[groups * base];
        //		Arrays.fill(temp, (byte) 0);
        //		System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
        //		keyByte = temp;
        //	}
        //	// 初始化
        //	Security.addProvider(new BouncyCastleProvider());
        //	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        //	SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        //	AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        //	parameters.init(new IvParameterSpec(ivByte));
        //	cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
        //	byte[] resultByte = cipher.doFinal(dataByte);
        //	if (null != resultByte && resultByte.length > 0) {
        //		String result = new String(resultByte, "UTF-8");
        //		JSONObject jsonObject = JSON.parseObject(result);
        //
        //
        //		//保存用户头像  完善七牛后 通过微信返回的头像url，保存成流，提交给千牛OSS
        //		URL avatarUrlGet = new URL(jsonObject.getString("avatarUrl"));
        //		HttpURLConnection conn = (HttpURLConnection) avatarUrlGet.openConnection();
        //		conn.setRequestMethod("GET");
        //		conn.setConnectTimeout(3 * 1000);
        //		InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
        //		QiNiuUtil qiNiuUtil = new QiNiuUtil();
        //		String avatarName = qiNiuUtil.uploadStream(inStream, "jfif");       //微信返回头像类型jfif
        //
        //		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //		sysUser.setRealname(jsonObject.getString("nickName"));
        //		sysUser.setAvatar(avatarName);
        //		sysUser.setSex(Integer.parseInt(jsonObject.getString("gender")));
        //		//sysUser.set(jsonObject.getString("country"));
        //
        //		//根据用户username 更新用户信息
        //		sysUserService.lambdaUpdate()
        //				.eq(SysUser::getUsername, sysUser.getUsername())
        //				.set(SysUser::getRealname, sysUser.getRealname())
        //				.set(SysUser::getAvatar, avatarName)
        //				.set(SysUser::getSex, sysUser.getSex())
        //				.update();
        //
        //		//更新shiro中的用户信息
        //
        //		//更新redis中的用户信息
        //		redisUtil.set(CacheConstant.SYS_USERS_CACHE + sysUser.getUsername(), sysUser);
        //	}
        //} catch (NoSuchAlgorithmException e) {
        //	log.error(e.getMessage(), e);
        //} catch (NoSuchPaddingException e) {
        //	log.error(e.getMessage(), e);
        //} catch (InvalidParameterSpecException e) {
        //	log.error(e.getMessage(), e);
        //} catch (IllegalBlockSizeException e) {
        //	log.error(e.getMessage(), e);
        //} catch (BadPaddingException e) {
        //	log.error(e.getMessage(), e);
        //} catch (UnsupportedEncodingException e) {
        //	log.error(e.getMessage(), e);
        //} catch (InvalidKeyException e) {
        //	log.error(e.getMessage(), e);
        //} catch (InvalidAlgorithmParameterException e) {
        //	log.error(e.getMessage(), e);
        //} catch (NoSuchProviderException e) {
        //	log.error(e.getMessage(), e);
        //} catch (ProtocolException e) {
        //	e.printStackTrace();
        //} catch (MalformedURLException e) {
        //	e.printStackTrace();
        //} catch (IOException e) {
        //	e.printStackTrace();
        //}
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String avatarUrl = userInfo.get("avatarUrl");

        //保存用户头像  完善七牛后 通过微信返回的头像url，保存成流，提交给千牛OSS
        URL avatarUrlGet = null;
        String avatarName = null;
        try {
            avatarUrlGet = new URL(avatarUrl);
            HttpURLConnection conn = (HttpURLConnection) avatarUrlGet.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            QiNiuUtil qiNiuUtil = new QiNiuUtil();
            avatarName = qiNiuUtil.uploadStream(inStream, "jfif");       //微信返回头像类型jfif
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sysUser.setRealname(userInfo.get("nickName"));
        sysUser.setAvatar(avatarName);
        sysUser.setSex(Integer.parseInt(userInfo.get("gender")));

        //根据用户username 更新用户信息
        sysUserService.lambdaUpdate()
                .eq(SysUser::getUsername, sysUser.getUsername())
                .set(SysUser::getRealname, sysUser.getRealname())
                .set(SysUser::getAvatar, avatarName)
                .set(SysUser::getSex, sysUser.getSex())
                .update();

        //更新redis中的用户信息
        redisUtil.set(CacheConstant.SYS_USERS_CACHE + sysUser.getUsername(), sysUser);
        bbsAuthUtils.getMiNiStorageFromSql(sysUser.getUsername());
        return Result.OK("更新用户信息成功");
    }

}
