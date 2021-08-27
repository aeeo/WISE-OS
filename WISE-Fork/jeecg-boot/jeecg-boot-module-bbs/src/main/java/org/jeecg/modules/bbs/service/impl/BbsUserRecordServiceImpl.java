package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.mapper.BbsUserRecordMapper;
import org.jeecg.modules.bbs.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户信息记录
 * @Author: jeecg-boot
 * @Date: 2021-01-13
 * @Version: V1.0
 */
@Service
public class BbsUserRecordServiceImpl extends ServiceImpl<BbsUserRecordMapper, BbsUserRecord> implements IBbsUserRecordService {

}
