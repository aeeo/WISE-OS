package org.jeecg.modules.bbs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.bbs.entity.BbsActivity;
import org.jeecg.modules.bbs.entity.BbsActivityUser;
import org.jeecg.modules.bbs.service.IBbsActivityService;
import org.jeecg.modules.bbs.service.IBbsActivityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 活动用户
 * @Author: jeecg-boot
 * @Date: 2021-06-12
 * @Version: V1.0
 */
@Api(tags = "活动用户")
@RestController
@RequestMapping("/bbs/bbsActivityUser")
@Slf4j
public class BbsActivityUserController extends JeecgController<BbsActivityUser, IBbsActivityUserService> {
    @Autowired
    private IBbsActivityUserService bbsActivityUserService;
    @Autowired
    private IBbsActivityService bbsActivityService;


    /**
     * 分页列表查询
     *
     * @param bbsActivityUser
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "活动用户-分页列表查询")
    @ApiOperation(value = "活动用户-分页列表查询", notes = "活动用户-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "bbs/operator/activity/activityuser/BbsActivityUserList")
    public Result<?> queryPageList(BbsActivityUser bbsActivityUser,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsActivityUser> queryWrapper = QueryGenerator.initQueryWrapper(bbsActivityUser, req.getParameterMap());
        Page<BbsActivityUser> page = new Page<BbsActivityUser>(pageNo, pageSize);
        IPage<BbsActivityUser> pageList = bbsActivityUserService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsActivityUser
     * @return
     */
    @AutoLog(value = "活动用户-添加")
    @ApiOperation(value = "活动用户-添加", notes = "活动用户-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsActivityUser bbsActivityUser) {
        bbsActivityUserService.save(bbsActivityUser);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsActivityUser
     * @return
     */
    @AutoLog(value = "活动用户-编辑")
    @ApiOperation(value = "活动用户-编辑", notes = "活动用户-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsActivityUser bbsActivityUser) {
        bbsActivityUserService.updateById(bbsActivityUser);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动用户-通过id删除")
    @ApiOperation(value = "活动用户-通过id删除", notes = "活动用户-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsActivityUserService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "活动用户-批量删除")
    @ApiOperation(value = "活动用户-批量删除", notes = "活动用户-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsActivityUserService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动用户-通过id查询")
    @ApiOperation(value = "活动用户-通过id查询", notes = "活动用户-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsActivityUser bbsActivityUser = bbsActivityUserService.getById(id);
        if (bbsActivityUser == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsActivityUser);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsActivityUser
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsActivityUser bbsActivityUser) {
        return super.exportXls(request, bbsActivityUser, BbsActivityUser.class, "活动用户");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BbsActivityUser.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsActivityUser
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "活动用户-分页列表查询")
    @ApiOperation(value = "活动用户-分页列表查询", notes = "活动用户-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    @PermissionData(pageComponent = "bbs/operator/activity/activityuser/BbsActivityUserList")
    public Result<?> queryPageListWiseBack(BbsActivityUser bbsActivityUser,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsActivityUser> queryWrapper = QueryGenerator.initQueryWrapper(bbsActivityUser, req.getParameterMap());
        Page<BbsActivityUser> page = new Page<BbsActivityUser>(pageNo, pageSize);
        IPage<BbsActivityUser> pageList = bbsActivityUserService.page(page, queryWrapper);

        for (int i = 0; i < pageList.getRecords().size(); i++) {
            BbsActivity bbsActivity = bbsActivityService.lambdaQuery().eq(BbsActivity::getId, pageList.getRecords().get(i).getActivityId()).one();
            pageList.getRecords().get(i).setActivityTitle(bbsActivity.getTitle());
        }

        return Result.OK(pageList);
    }


    /**
     * 报名
     *
     * @param bbsActivityUser
     * @return
     */
    @AutoLog(value = "活动用户-报名")
    @ApiOperation(value = "活动用户-报名", notes = "活动用户-报名")
    @PostMapping(value = "/wise/mini/add")
    public Result<?> addWiseMini(@RequestBody BbsActivityUser bbsActivityUser) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();


        BbsActivity one = bbsActivityService.lambdaQuery()
                .eq(BbsActivity::getId, bbsActivityUser.getActivityId())
                .one();

        if (null != one) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = one.getStartTime();
            Date endTime = one.getEndTime();
            String startDateFormat = simpleDateFormat.format(startTime);
            String endDateFormat = simpleDateFormat.format(endTime);

            String currentDateFormat = DateUtils.datetimeFormat.get().format(new Date());

            if (startDateFormat.compareTo(currentDateFormat) < 0
                    && endDateFormat.compareTo(currentDateFormat) > 0) {
                BbsActivityUser bbsActivityUser2 = bbsActivityUserService.lambdaQuery()
                        .eq(BbsActivityUser::getPhoneNumber, bbsActivityUser.getPhoneNumber())
                        .eq(BbsActivityUser::getActivityId, bbsActivityUser.getActivityId())
                        .eq(BbsActivityUser::getApplyStatus, "0")
                        .one();
                if (null != bbsActivityUser2) {
                    return Result.error(1000, "该手机号已被使用！");
                }

                //判断是否已经报名
                BbsActivityUser bbsActivityUser1 = bbsActivityUserService.lambdaQuery()
                        .eq(BbsActivityUser::getCreateBy, sysUser.getUsername())
                        .eq(BbsActivityUser::getActivityId, bbsActivityUser.getActivityId())
                        .one();
                if (null != bbsActivityUser1 && bbsActivityUser1.getApplyStatus().equals("0")) {
                    return Result.error(1000, "请勿重复报名！");
                }else if (null != bbsActivityUser1 && bbsActivityUser1.getApplyStatus().equals("1")) {
                    bbsActivityUser.setId(bbsActivityUser1.getId());
                    bbsActivityUser.setApplyStatus("0");
                    bbsActivityUserService.saveOrUpdate(bbsActivityUser);
                }else if(null == bbsActivityUser1){
                    bbsActivityUserService.save(bbsActivityUser);
                }
                return Result.OK("报名成功！");
            } else if (endDateFormat.compareTo(currentDateFormat) < 0) {
                return Result.error(1000, "活动已结束！");
            } else if (startDateFormat.compareTo(currentDateFormat) > 0) {
                return Result.error(1000, "活动还未开始！");
            } else {
                return Result.error(1000, "报名失败");
            }
        } else {
            return Result.error(1000, "活动已失效！");
        }
    }

    /**
     * 取消报名
     *
     * @param bbsActivityUser
     * @return
     */
    @AutoLog(value = "活动用户-取消报名")
    @ApiOperation(value = "活动用户-取消报名", notes = "活动用户-取消报名")
    @PostMapping(value = "/wise/mini/canclApply")
    public Result<?> canclApplyWiseMini(@RequestBody BbsActivityUser bbsActivityUser) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();


        BbsActivityUser bbsActivityUser1 = bbsActivityUserService.lambdaQuery()
                .eq(BbsActivityUser::getActivityId, bbsActivityUser.getActivityId())
                .eq(BbsActivityUser::getCreateBy, sysUser.getUsername())
                .one();

        if (null != bbsActivityUser1) {
            bbsActivityUser1.setApplyStatus("1");
            boolean b = bbsActivityUserService.updateById(bbsActivityUser1);
            if (b) {
                return Result.OK("取消成功！");
            } else {
                return Result.error(1000, "无法取消！");
            }

        } else {
            return Result.error(1000, "没有报名，无法取消！");
        }
    }
}
