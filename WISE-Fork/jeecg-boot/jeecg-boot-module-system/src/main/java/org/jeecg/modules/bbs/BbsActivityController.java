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
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.BbsActivity;
import org.jeecg.modules.bbs.entity.BbsActivityUser;
import org.jeecg.modules.bbs.entity.BbsUserRecord;
import org.jeecg.modules.bbs.service.IBbsActivityService;
import org.jeecg.modules.bbs.service.IBbsActivityUserService;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 活动
 * @Author: jeecg-boot
 * @Date: 2021-06-10
 * @Version: V1.0
 */
@Api(tags = "活动")
@RestController
@RequestMapping("/bbs/bbsActivity")
@Slf4j
public class BbsActivityController extends JeecgController<BbsActivity, IBbsActivityService> {
    @Autowired
    private IBbsActivityService bbsActivityService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private IBbsActivityUserService bbsActivityUserService;

    /**
     * 分页列表查询
     *
     * @param bbsActivity
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "活动-分页列表查询")
    @ApiOperation(value = "活动-分页列表查询", notes = "活动-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsActivity bbsActivity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsActivity> queryWrapper = QueryGenerator.initQueryWrapper(bbsActivity, req.getParameterMap());
        Page<BbsActivity> page = new Page<BbsActivity>(pageNo, pageSize);
        IPage<BbsActivity> pageList = bbsActivityService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsActivity
     * @return
     */
    @AutoLog(value = "活动-添加")
    @ApiOperation(value = "活动-添加", notes = "活动-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsActivity bbsActivity) {
        bbsActivityService.save(bbsActivity);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsActivity
     * @return
     */
    @AutoLog(value = "活动-编辑")
    @ApiOperation(value = "活动-编辑", notes = "活动-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsActivity bbsActivity) {
        bbsActivityService.updateById(bbsActivity);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动-通过id删除")
    @ApiOperation(value = "活动-通过id删除", notes = "活动-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsActivityService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "活动-批量删除")
    @ApiOperation(value = "活动-批量删除", notes = "活动-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsActivityService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动-通过id查询")
    @ApiOperation(value = "活动-通过id查询", notes = "活动-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsActivity bbsActivity = bbsActivityService.getById(id);
        if (bbsActivity == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsActivity);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsActivity
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsActivity bbsActivity) {
        return super.exportXls(request, bbsActivity, BbsActivity.class, "活动");
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
        return super.importExcel(request, response, BbsActivity.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsActivity
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "活动-分页列表查询")
    @ApiOperation(value = "活动-分页列表查询", notes = "活动-分页列表查询")
    @GetMapping(value = "/wise/mini/list")
    public Result<?> queryPageListWiseMini(BbsActivity bbsActivity,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        BbsUserRecord one = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();

        QueryWrapper<BbsActivity> queryWrapper = QueryGenerator.initQueryWrapper(bbsActivity, req.getParameterMap());
        queryWrapper.eq("region_code", one.getRegionCode()).or()
                .eq("push_type", "0")
                .orderByDesc("create_time");

        Page<BbsActivity> page = new Page<BbsActivity>(pageNo, pageSize);
        IPage<BbsActivity> pageList = bbsActivityService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动-通过id查询")
    @ApiOperation(value = "活动-通过id查询", notes = "活动-通过id查询")
    @GetMapping(value = "/wise/mini/queryById")
    public Result<?> queryByIdWiseMini(@RequestParam(name = "id", required = true) String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        BbsActivity bbsActivity = bbsActivityService.getById(id);
        if (bbsActivity == null) {
            return Result.error("未找到对应数据");
        }

        if (null != sysUser) {
            //完善报名信息
            BbsActivityUser bbsActivityUser = bbsActivityUserService.lambdaQuery()
                    .eq(BbsActivityUser::getCreateBy, sysUser.getUsername())
                    .eq(BbsActivityUser::getActivityId, id).one();
            if (null != bbsActivityUser && bbsActivityUser.getApplyStatus().equals("0")) {
                bbsActivity.setApplyChineseName(bbsActivityUser.getChineseName());
                bbsActivity.setApplyPhoneNumber(bbsActivityUser.getPhoneNumber());
                bbsActivity.setApplyWechat(bbsActivityUser.getWechat());
                bbsActivity.setApplyRemark(bbsActivityUser.getRemark());
                bbsActivity.setIsApply(true);
            } else if (null != bbsActivityUser && bbsActivityUser.getApplyStatus().equals("1")) {
                bbsActivity.setApplyChineseName(bbsActivityUser.getChineseName());
                bbsActivity.setApplyPhoneNumber(bbsActivityUser.getPhoneNumber());
                bbsActivity.setApplyWechat(bbsActivityUser.getWechat());
                bbsActivity.setApplyRemark(bbsActivityUser.getRemark());
                bbsActivity.setIsApply(false);
            }else{
                bbsActivity.setIsApply(false);
            }
        }

        return Result.OK(bbsActivity);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动-通过id查询")
    @ApiOperation(value = "活动-通过id查询", notes = "活动-通过id查询")
    @GetMapping(value = "/wise/mini/queryById/anon")
    public Result<?> queryByIdWiseMiniAnon(@RequestParam(name = "id", required = true) String id) {

        BbsActivity bbsActivity = bbsActivityService.getById(id);
        if (bbsActivity == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsActivity);
    }
}
